package com.example.pomparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ogData {
    private DatabaseManager dbManager;

    public ogData(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void parseAndStore(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            String ogTitle = getMetaTagContent(doc, "og:title");
            String ogDescription = getMetaTagContent(doc, "og:description");
            String ogImage = getMetaTagContent(doc, "og:image");
            String ogUrl = getMetaTagContent(doc, "og:url");

            boolean foundOgTags = false;

            Map<String, Object> columnValues = new HashMap<>();
            columnValues.put("url", url);

            if (!ogTitle.isEmpty()) {
                System.out.println("OG Title: " + ogTitle);
                columnValues.put("og_title", ogTitle);
                foundOgTags = true;
            }
            if (!ogDescription.isEmpty()) {
                System.out.println("OG Description: " + ogDescription);
                columnValues.put("og_description", ogDescription);
                foundOgTags = true;
            }
            if (!ogImage.isEmpty()) {
                System.out.println("OG Image: " + ogImage);
                columnValues.put("og_image", ogImage);
                foundOgTags = true;
            }
            if (!ogUrl.isEmpty()) {
                System.out.println("OG URL: " + ogUrl);
                columnValues.put("og_url", ogUrl);
                foundOgTags = true;
            }

            if (!foundOgTags) {
                System.out.println("No Open Graph tags found.");
            } else {
                dbManager.insertWebsite(columnValues);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getMetaTagContent(Document doc, String attr) {
        Element metaTag = doc.selectFirst("meta[property=" + attr + "]");
        return metaTag != null ? metaTag.attr("content") : "";
    }
}
