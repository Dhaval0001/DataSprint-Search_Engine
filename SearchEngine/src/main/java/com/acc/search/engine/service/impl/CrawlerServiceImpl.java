package com.acc.search.engine.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.acc.search.engine.service.CrawlerService;

@Service
public class CrawlerServiceImpl implements CrawlerService{

	private static ArrayList<String> linkList = new ArrayList<>();
	
	@Override
	public String[] getURLList(String url) {
		getLinks(url);
        htmlToText();
		String[] urlList = linkList.toArray(new String[linkList.size()]);
        return urlList;
	}
	
	private static void getLinks(String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String s = link.attr("abs:href");
                String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
                Pattern p1 = Pattern.compile(regex);
                Matcher m1 = p1.matcher(s);
                while (m1.find()) {
                    linkList.add(m1.group(0));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void converter (String inpath, String outpath) throws IOException {   // convert html page into text using jsoup
        File in = new File(inpath);
        Document doc = Jsoup.parse(in, "UTF-8");    // jsoup read
        String output = doc.text(); // transfer
        BufferedWriter writerTxt = new BufferedWriter(new FileWriter(outpath)); // write .txt file
        writerTxt.write(output);
        writerTxt.close();
    }

    private static void htmlToText() {
        try {
            for (String s : linkList) {
                String regex = "[a-zA-Z0-9]+";
                Pattern p2 = Pattern.compile(regex);
                Matcher m2 = p2.matcher(s);
                StringBuffer sb = new StringBuffer();
                while (m2.find()) {
                    sb.append(m2.group(0));
                }

                String linkAdress = sb.substring(0);
                Document docNewLink = Jsoup.connect(s).get();
                String htmlOutputPath = "dat/html/";
                String html = docNewLink.html();
                File htmlOutputFolder = new File(htmlOutputPath);
                if (!htmlOutputFolder.exists() && !htmlOutputFolder.isDirectory()) {
                    htmlOutputFolder.mkdir();
                }   // check whether outputfolder is exist, if not create it
                PrintWriter out = new PrintWriter(htmlOutputPath + linkAdress + ".html");
                out.println(html);
                out.close();

                String txtOutputPath = "dat/text/";
                File txtOutputFolder = new File(txtOutputPath);
                if (!txtOutputFolder.exists() && !txtOutputFolder.isDirectory()) {
                    txtOutputFolder.mkdir();
                }    // check whether outputfolder is exist, if not create it
                File folder = new File(htmlOutputPath);
                File[] fileStream = folder.listFiles();
                assert fileStream != null;
                for (File f : fileStream) {   // traverse the folder
                    if (!f.isDirectory()) {  // if not folder then transfer html to text
                        String inpath = htmlOutputPath + f.getName();
                        String outpath = txtOutputPath + f.getName().replaceAll(".htm", "") + ".txt";
                        converter(inpath, outpath);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(">> We can not fetch this url:"+e);
        }
    }


}
