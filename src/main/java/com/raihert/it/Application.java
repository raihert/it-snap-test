package com.raihert.it;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Arrays.asList;

public class Application {
    private static ApplicationFacade facade = new ApplicationFacade();

    public static void main(final String... args) {
        Logger.getGlobal().setLevel(Level.ALL);

        handle();
    }

    private static void handle() {
        System.out.println("Starting an application");
        System.out.println("To exit, please enter QUIT");

        final Scanner in = new Scanner(System.in);

        while (true) {
            System.out.print("Please enter a currency code (USD, EUR, GBP, etc.): ");

            String line = in.nextLine();

            System.out.println();

            if (line.isEmpty() || !line.matches("^[a-zA-Z]{3}$")) {
                System.out.println("Try again");

                continue;
            }

            if (asList("exit", "quit").contains(line.toLowerCase())) {
                break;
            }

            try {
                System.out.println(facade.handle(line.toUpperCase()));
            } catch (final Exception ex) {
                System.out.println(ex.getMessage());
                System.err.println(ex.getMessage());
            }
        }
    }

}
