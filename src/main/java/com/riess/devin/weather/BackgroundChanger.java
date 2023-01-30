package com.riess.devin.weather;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BackgroundChanger {

    private String name;

    public void getData(String src) {
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(src);
            HttpResponse response = httpClient.execute(request);
            String htmlResponse = EntityUtils.toString(response.getEntity());
            //System.out.println(htmlResponse);
            int firstIndex = htmlResponse.indexOf("IMG SRC=");
            int lastIndex = htmlResponse.indexOf("jpg", firstIndex);
            int nameFirstIndex = htmlResponse.indexOf("<b>", lastIndex);
            int nameLastIndex = htmlResponse.indexOf("</b>", nameFirstIndex);
            name = htmlResponse.substring(nameFirstIndex + 4, nameLastIndex - 1);
            String urlString = htmlResponse.substring(firstIndex + 9, lastIndex + 3);
            //String encodedUrl = URLEncoder.encode(urlString, "UTF-8");

            try(InputStream in = new URL("https://apod.nasa.gov/apod/" + urlString).openStream()){
                Files.copy(in, Paths.get("/Users/devinriess/Library/Mobile Documents/com~apple~CloudDocs/Documents/ImageOfTheDay/" + name + ".jpg"));
            }


            System.out.println(urlString);

            EntityUtils.consume(response.getEntity());
        } catch (IOException e ) {
            System.out.println(e);
        }
    }


    public void setWallpaper(String path)
            throws Exception {
        path = path + name + ".jpg";
        File file = new File(path);
        String as[] = {
                "osascript",
                "-e", "tell application \"Finder\"",
                "-e", "set desktop picture to POSIX file \"" + file.getAbsolutePath() + "\"",
                "-e", "end tell"
        };
        Runtime runtime = Runtime.getRuntime();
        Process process;
        process = runtime.exec(as);
    }

    public static void main(String[] args) throws java.lang.Exception {
        BackgroundChanger backgroundChanger = new BackgroundChanger();
        backgroundChanger.getData("https://apod.nasa.gov/apod/astropix.html");
        backgroundChanger.setWallpaper("/Users/devinriess/Library/Mobile Documents/com~apple~CloudDocs/Documents/ImageOfTheDay/");

    }


}
