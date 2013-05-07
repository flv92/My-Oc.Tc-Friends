package fr.flv92.oc.tc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SourceCodeDownloader {

    public static BufferedReader Connect(String url2) throws Exception {

        //Set URL
        URL url = new URL(url2);
        URLConnection spoof = url.openConnection();

        //Spoof the connection so we look like a web browser
        spoof.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
        BufferedReader in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));
        return in;
    }
}