package org.system.controller;

import org.system.service.MajorService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MajorController {
    private Scanner scanner = new Scanner(System.in);
    private MajorService majorService = new MajorService();
    public void displayAllMajor(){
        majorService.displayAllMajor();
    }

    public void displayMajorById(){
        while (true) {
            try {
                System.out.print("Please Major Id: ");
                int majorId = scanner.nextInt();
                majorService.displayMajorById(majorId);
                return;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input ! Please input number");
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
                System.out.print("Enter Id to update: ");
                int majorId = scanner.nextInt();
                majorService.update(majorId);
                return;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input ! Please input number");
                scanner.nextLine();
            }
        }

    }

    public void delete(){
        while (true) {
            try {
                System.out.print("Enter Id to delete: ");
                int majorId = scanner.nextInt();
                majorService.delete(majorId);
                return;
            }catch (InputMismatchException e){
                System.out.println("Invalid input ! Please input number");
                scanner.nextLine();
            }

        }

        }

}
