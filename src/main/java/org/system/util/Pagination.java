package org.system.util;

import java.util.List;
import java.util.Scanner;

/**
 * Generic pagination helper.
 * Splits any List into pages of PAGE_SIZE rows and loops until the user
 * presses 0 (back).  The caller supplies a lambda that knows how to render
 * one page of items.
 */
public class Pagination {

    public static final int PAGE_SIZE = 10;

    private static final String yellow = "\u001B[33m";
    private static final String cyan   = "\u001B[36m";
    private static final String red    = "\u001B[31m";
    private static final String green  = "\u001B[32m";
    private static final String reset  = "\u001B[0m";

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * @param items    full list retrieved from the service/DAO
     * @param renderer lambda (or method reference) that receives the sub-list
     *                 for ONE page and prints it
     */
    public static <T> void paginate(List<T> items, PageRenderer<T> renderer) {
        if (items == null || items.isEmpty()) return;

        int totalPages = (int) Math.ceil((double) items.size() / PAGE_SIZE);
        int currentPage = 1;

        while (true) {
            // ── Slice the page ────────────────────────────────────────────────
            int from = (currentPage - 1) * PAGE_SIZE;
            int to   = Math.min(from + PAGE_SIZE, items.size());
            List<T> page = items.subList(from, to);

            // ── Render the table ──────────────────────────────────────────────
            renderer.render(page);

            // ── Page info ─────────────────────────────────────────────────────
            System.out.println(cyan +
                    "  Page " + currentPage + " / " + totalPages +
                    "   (rows " + (from + 1) + "–" + to +
                    " of " + items.size() + ")" + reset);

            // ── Navigation prompt ─────────────────────────────────────────────
            System.out.println(yellow +
                    "  ┌─────────────────────────────────┐\n" +
                    "  │  1. Next Page                   │\n" +
                    "  │  2. Previous Page               │\n" +
                    "  │  0. Back                        │\n" +
                    "  └─────────────────────────────────┘" + reset);
            System.out.print(yellow + "  Enter option: " + reset);

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(red + "  Invalid input! Please enter 0, 1, or 2." + reset);
                continue;
            }

            switch (choice) {
                case 1 -> {
                    if (currentPage < totalPages) {
                        currentPage++;
                    } else {
                        System.out.println(green + "  Already on the last page." + reset);
                    }
                }
                case 2 -> {
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.out.println(green + "  Already on the first page." + reset);
                    }
                }
                case 0 -> { return; }
                default -> System.out.println(red + "  Invalid option. Enter 0, 1, or 2." + reset);
            }
        }
    }

    /** Functional interface for the page renderer. */
    @FunctionalInterface
    public interface PageRenderer<T> {
        void render(List<T> pageItems);
    }
}
