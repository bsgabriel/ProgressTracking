package com.progress.tracking.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class WsUtil {

    public static String sendGet(String url, Map<String, String> headers, Map<String, String> queryParams) throws Exception {
        if (queryParams != null && !queryParams.isEmpty()) {
            StringBuilder queryString = new StringBuilder();
            queryString.append("?");
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                queryString.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                        .append("&");
            }

            queryString.deleteCharAt(queryString.length() - 1);
            url += queryString.toString();
        }

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet())
                con.setRequestProperty(entry.getKey(), entry.getValue());
        }

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();

        return response.toString();
    }

    public static String sendPost(String url, Map<String, String> headers, String json) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("POST");

        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet())
                connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        connection.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.writeBytes(json);
            wr.flush();
        }

        int responseCode = connection.getResponseCode();

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        connection.disconnect();

        return response.toString();
    }

}
