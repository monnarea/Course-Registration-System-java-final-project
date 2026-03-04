package org.system.controller;

import org.system.service.MajorService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MajorController {
    private Scanner scanner = new Scanner(System.in);
    private MajorService majorService = new MajorService();
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red = "\u001B[31m";
    public static final String cyan = "\u001B[36m";
    public static final String white = "\u001B[37m";
    public void displayAllMajor(){
        majorService.displayAllMajor();
    }


    public void displayMajorById(){
        while (true) {
            try {
                System.out.print(yellow+"Please Major Id: ");
                int majorId = scanner.nextInt();
                majorService.displayMajorById(majorId);
                return;
            } catch (InputMismatchException e) {
                System.out.println(red+"Invalid input ! Please input number");
                scanner.nextLine();
            }
        }

    }

    public void create(){
        majorService.create();
    }

    public void update(){
        while (true) {
            try {
                System.out.print(yellow+"Enter Id to update: ");
                int majorId = scanner.nextInt();
                majorService.update(majorId);
                return;
            } catch (InputMismatchException e) {
                System.out.println(red+"Invalid input ! Please input number");
                scanner.nextLine();
            }
        }

    }

    public void delete(){
        while (true) {
            try {
                System.out.print(yellow+"Enter Id to delete: ");
                int majorId = scanner.nextInt();
                majorService.delete(majorId);
                return;
            }catch (InputMismatchException e){
                System.out.println(red+"Invalid input ! Please input number");
                scanner.nextLine();
            }

        }

        }

}
