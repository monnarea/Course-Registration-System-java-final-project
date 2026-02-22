package org.system.view;

import org.system.model.dto.response.CourseResponseDto;

import java.util.List;

public class View {
    public static void printTable(List<CourseResponseDto> courses) {
        System.out.printf("%-5s %-20s %-8s %-8s %-8s %-12s %-12s %-15s %-8s %-12s%n",
                "ID", "Name", "Price", "Credit", "Cap", "Start", "End", "Instructor", "Room", "Created");
        System.out.println("-".repeat(120));
        for (CourseResponseDto c : courses) {
            System.out.printf("%-5d %-20s %-8.2f %-8d %-8d %-12s %-12s %-15s %-8s %-12s%n",
                    c.getCourse_id(),
                    c.getCourse_name(),
                    c.getPrice(),
                    c.getCredit_score(),
                    c.getCapacity(),
                    c.getStart_date(),
                    c.getEnd_date(),
                    c.getInstructor(),
                    c.getRoom(),
                    c.getCreated_at()
            );
        }
    }


}
