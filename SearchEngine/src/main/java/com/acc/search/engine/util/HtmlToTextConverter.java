package com.acc.search.engine.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlToTextConverter {
    public static void main(String[] args) throws IOException {
        String fileName = "demo.html";
        extractText(Paths.get("D:\\UWindsor\\Advanced Computing Concepts\\Project\\Workspace\\SearchEngine\\src\\main\\resources\\static\\dat\\html/" + fileName));
        System.out.println("\nConverted HTML file \""
                + fileName + "\" to Text in \"D:\\UWindsor\\Advanced Computing Concepts\\Project\\Workspace\\SearchEngine\\src\\main\\resources\\static\\dat\\text\" folder...");
    }

    public static Path extractText(Path path) {
        Path textPath = null;
        try {
            Document document = Jsoup.parse(path.toFile(),null);
            String text = document.text();
            textPath = Paths.get("D:\\UWindsor\\Advanced Computing Concepts\\Project\\Workspace\\SearchEngine\\src\\main\\resources\\static\\dat\\text/" + path.getFileName().toString()
                    .replace(".html", ".txt"));
            Files.write(textPath, text.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textPath;
    }
}
