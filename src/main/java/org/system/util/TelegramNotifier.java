package org.system.util;

import java.io.OutputStream;

import java.net.HttpURLConnection;

import java.net.URL;

import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;

public class TelegramNotifier {

    private static final String BOT_TOKEN = "8377156239:AAHpbr4cmTNMYzsZPMMXT71iuFZ48XXBi1A"; // ← CHANGE

    private static final String GROUP_CHAT_ID = "1903757857"; // ← CHANGE

    public static void send(String message) {
        try {
            String urlStr = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";

            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");

            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String params = "chat_id=" + GROUP_CHAT_ID +
                    "&text=" + URLEncoder.encode(message, StandardCharsets.UTF_8) +
                    "&parse_mode=HTML";

            try (OutputStream os = conn.getOutputStream()) {

                byte[] input = params.getBytes(StandardCharsets.UTF_8);

                os.write(input, 0, input.length);
            }

            conn.getResponseCode(); // we don't care about response for simplicity

            conn.disconnect();

        } catch (Exception e) {

            System.err.println("Telegram notification failed: " + e.getMessage());
        }
    }
}