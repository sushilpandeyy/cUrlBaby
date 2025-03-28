package com.curlbaby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestHandler {
    private final UIManager uiManager = new UIManager();
    private final JsonFormatter jsonFormatter = new JsonFormatter();
    
    public void executeGetRequest(String urlString) {
        HttpURLConnection connection = null;
        try {
            // Normalize URL (add http:// if missing)
            if (!urlString.startsWith("http://") && !urlString.startsWith("https://")) {
                urlString = "http://" + urlString;
            }
            
            uiManager.printRequestInfo(urlString);
            
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int status = connection.getResponseCode();
            uiManager.printStatusInfo(status, connection.getResponseMessage());
            
            // Print headers
            uiManager.printHeadersSection();
            connection.getHeaderFields().forEach((key, values) -> {
                if (key != null) {
                    uiManager.printHeader(key, String.join(", ", values));
                }
            });
            
            // Read and print response
            BufferedReader reader;
            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            
            uiManager.printResponseBodySection();
            String line;
            StringBuilder responseContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();
            
            // Pretty print the response if it's JSON
            String response = responseContent.toString();
            if (response.trim().startsWith("{") || response.trim().startsWith("[")) {
                try {
                    String formatted = jsonFormatter.formatJson(response);
                    System.out.println(formatted);
                } catch (Exception e) {
                    System.out.println(response);
                }
            } else {
                System.out.println(response);
            }
            
        } catch (IOException e) {
            uiManager.printError("Error: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}