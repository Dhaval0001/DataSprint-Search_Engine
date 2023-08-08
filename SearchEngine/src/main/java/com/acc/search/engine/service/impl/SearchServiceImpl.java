package com.acc.search.engine.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.springframework.stereotype.Service;

import com.acc.search.engine.dto.Doc;
import com.acc.search.engine.service.SearchService;
import com.acc.search.engine.util.Entry;
import com.acc.search.engine.util.PriorityQueue;
import com.acc.search.engine.util.SortedPriorityQueue;
import com.acc.search.engine.util.TST;

@SuppressWarnings("rawtypes")
@Service
public class SearchServiceImpl implements SearchService{

	
	//scan file and get frequency store in hashmap
    public static void scanFile(String fileName, TST<Integer> tst)  throws IOException{
        StringBuffer sb = new StringBuffer();
        FileReader file = new FileReader(fileName);
        BufferedReader br = new BufferedReader(file);
//		TST<Integer> tst = new TST<Integer>();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        String article = sb.toString();
        // split  ,*)?;/&#<-.!:\"\"''\n to get word by use Tokenizer
        StringTokenizer st = new StringTokenizer(article, " ,`*$|~(){}_@><=+[]\\?;/&#<-.!:\"\"''\n");
        while(st.hasMoreTokens()) {
            String word = st.nextToken();  //return string until next token;
            if (tst.contains(word)) {
                int count = tst.get(word);
                tst.put(word, count + 1);
            } else {
                tst.put(word, 1);
            }
        }
    }

    @Override
    public PriorityQueue<Integer,String> occurencesOfQueryString(String scan) throws IOException {
        String txtPath = "src/main/resources/static/dat/text/";
        String htmlPath = "src/main/resources/static/dat/html/";

        File txt = new File(txtPath);
        File[] Files = txt.listFiles();
        File html = new File(htmlPath);
        File[] Webs = html.listFiles();

        PriorityQueue<Integer,String> pq = new SortedPriorityQueue<>();
        // scan new files
        for (int i = 0; i < Files.length; i++) {
            if (Files[i].isFile()) {
                TST<Integer> tst = new TST<Integer>();
                String path = (txtPath + Files[i].getName());
                scanFile(path, tst);// get occurrence from matching given word
                if (tst.get(scan) != null) {
                    //store occurrence and web name in priority queue
                    pq.insert(tst.get(scan), Webs[i].getName());
                }
            }
        }
        return pq;
    }

	@Override
	public Doc[] queue2List(PriorityQueue pq) throws IOException {
		Doc[] queryResults = new Doc[pq.size()];
        @SuppressWarnings("unchecked")
		Iterator<Entry<Integer, String>> s = pq.iterator();
        int flag = 0;
        while(s.hasNext()) {
            Entry<Integer, String> tmp = s.next();
            Doc doc = new Doc(tmp.getKey(), tmp.getValue());
            queryResults[(pq.size() - 1) - flag] = doc;
            flag++;
        }
        return queryResults;
	}

}
