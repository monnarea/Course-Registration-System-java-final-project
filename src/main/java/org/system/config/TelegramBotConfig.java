package org.system.config;

import org.system.controller.TelegramBotController;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class TelegramBotConfig {

    private static final String BOT_TOKEN = "8548292141:AAFDaDumykPNfBCGnhh0Zz8olzWsMlQrfbI";
    private static final String BOT_NAME = "CS_studnet_bot";
    public static void startBot() throws Exception {
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {

            TelegramBotController bot = new TelegramBotController(BOT_TOKEN);
            botsApplication.registerBot(BOT_TOKEN, bot);

            System.out.println("🔗 Open your bot: https://t.me/" + BOT_NAME);

            // Keep the bot alive indefinitely
            Thread.currentThread().join();
        }
        catch (Exception e) {
            System.out.println("Bot failed: " + e.getMessage());
        }
    }


    public static void main(String[] args) throws Exception {
        startBot();
    }
}