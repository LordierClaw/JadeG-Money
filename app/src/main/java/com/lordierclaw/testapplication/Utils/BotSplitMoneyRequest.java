package com.lordierclaw.testapplication.Utils;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class BotSplitMoneyRequest extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        String url = "http://103.214.22.161:5555/isGoingToEat";
        String requestBody = "{\"message\": [\"" + String.join("\", \"", strings) + "\"]}";
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Gửi yêu cầu POST
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            con.getOutputStream().write(requestBody.getBytes(StandardCharsets.UTF_8));

            // Nhận phản hồi
            int responseCode = con.getResponseCode();
            String response = "";
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(con.getInputStream());
                while (scanner.hasNextLine()) {
                    response += scanner.nextLine();
                }
                scanner.close();
            }

            // Chuyển đổi phản hồi thành đối tượng JsonObject
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(response, JsonArray.class);
            for (int i = 0; i < jsonArray.size(); i++) {
                Integer k = gson.fromJson(jsonArray.get(i), Integer.class);
                if (k == 1) return "true";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }
}