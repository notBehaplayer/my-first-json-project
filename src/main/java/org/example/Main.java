package org.example;

import org.example.User.User;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        User user = new User();
        Scanner sc = new Scanner(System.in);

        user.userInterface.addFile();

        while (true) {
            int choice;
            while (true) {
                System.out.print("""
                        Welcome to bank!
                        1. Add user.
                        2. Show users.
                        3. Deposit.
                        4. Withdraw.
                        5. Exit.
                        """);
                String choiceInput = sc.nextLine();
                try {
                    choice = Integer.parseInt(choiceInput);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number!");
                }
            }


            switch (choice) {
                case 1:
                    user.userInterface.addUser();
                    break;
                case 2:
                    user.userInterface.showUsers();
                    break;
                case 3:
                    user.userInterface.deposit();
                    break;
                case 4:
                    user.userInterface.withdraw();
                    break;
                case 5:
                    System.out.println("Thanks for using our bank!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }
}