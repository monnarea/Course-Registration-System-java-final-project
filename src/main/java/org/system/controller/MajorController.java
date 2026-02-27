package org.system.controller;

import org.system.service.MajorService;

import java.util.Scanner;

public class MajorController {
    private Scanner scanner = new Scanner(System.in);
    private MajorService majorService = new MajorService();
    public void displayAllMajor(){
        majorService.displayAllMajor();
    }

    public void displayMajorById(){
        System.out.print("Please Major Id: ");
        int majorId = scanner.nextInt();
        majorService.displayMajorById(majorId);
    }

    public void create(){
        majorService.create();
    }

    public void update(){
        System.out.print("Enter Id to update: ");
        int majorId = scanner.nextInt();
        majorService.update(majorId);
    }

    public void delete(){
        System.out.print("Enter Id to delete: ");
        int majorId = scanner.nextInt();
        majorService.delete(majorId);
    }
}
