package org.system.config;

import org.system.controller.TelegramBotController;
import org.system.view.StudentMenu;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import java.util.Scanner;

import static org.system.view.StudentMenu.*;

public class TelegramBotConfig {

    private static final Scanner scanner  = new Scanner(System.in);
    private static final String BOT_TOKEN = "8548292141:AAFDaDumykPNfBCGnhh0Zz8olzWsMlQrfbI";
    private static final String BOT_NAME  = "CS_studnet_bot";

    /**
     * Starts the Telegram bot with the student ID from the console registration.
     * This ensures enrollments made in the bot are linked to the correct student.
     *
     * @param studentId the ID returned by StudentService.createStudent()
     */
    public static void startBot(int studentId) throws Exception {
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {

            // Pass studentId into the bot so it can save enrollments correctly
            TelegramBotController bot = new TelegramBotController(BOT_TOKEN, studentId);
            botsApplication.registerBot(BOT_TOKEN, bot);

            System.out.println("\n🔗 Open this link to enroll: https://t.me/" + BOT_NAME);
            System.out.println("   Your Student ID is: " + studentId);
            System.out.print(yellow + "[-] Press [0] to go back to the main menu: ");

            while (true) {
                try {
                    int option = Integer.parseInt(scanner.nextLine().trim());
                    if (option == 0) {
                        new StudentMenu().studentStart();
                        return;
                    } else {
                        System.out.print(yellow + "Invalid option. Press [0] to go back: ");
                    }
                } catch (NumberFormatException e) {
                    System.out.print(purple + "Invalid input. Press [0] to go back: ");
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Bot failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        // For standalone testing — use student ID 1
        startBot(1);
    }
}