package org.example.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class User {
    public UserInterface userInterface = new UserInterface() {
        private final Scanner sc = new Scanner(System.in);
        ArrayList<UserInfo> users = new ArrayList<>();
        private final Random rand = new Random();

        @Override
        public void addFile() {
            String directoryPath = "./DB";
            String filePath = directoryPath + "/users.json";

            File directory = new File(directoryPath);
            File file = new File(filePath);

            try {

                if (!directory.exists()) {
                    if (directory.mkdirs()) {
                        System.out.println("Directory created: " + directoryPath);
                    } else {
                        System.out.println("Failed to create directory: " + directoryPath);
                    }
                }

                if (!file.exists()) {
                    if (file.createNewFile()) {
                        System.out.println("File created: " + filePath);
                        try (Writer writer = new FileWriter(file)) {
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            gson.toJson(new ArrayList<>(), writer);
                        }
                    } else {
                        System.out.println("Failed to create file: " + filePath);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error during file creation: " + e.getMessage());
            }
        }

        @Override
        public void addUser() {
            createUser();
        }

        private void createUser() {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String filePath = "./DB/users.json";
            File file = new File(filePath);
            if (file.exists()) {
                try (Reader reader = new FileReader(file)) {
                    Type userArrayListType = new TypeToken<ArrayList<UserInfo>>() {
                    }.getType();
                    users = gson.fromJson(reader, userArrayListType);
                } catch (IOException e) {
                    System.out.println("Error reading users file: " + e.getMessage());
                }
            }
            System.out.println("To add a new user please enter your name: ");
            String name = sc.nextLine();
            if (name.isBlank()) name = "No name";

            System.out.println("Your surname: ");
            String surname = sc.nextLine();
            if (surname.isBlank()) surname = "No Surname";

            System.out.println("Your username: ");
            String username = sc.nextLine();
            if (username.isBlank()) username = "User " + (users.size() + 1);

            System.out.println("Your password: ");
            String password = sc.nextLine();
            if (password.isBlank()) password = String.valueOf(UUID.randomUUID());

            int age;
            while (true) {
                System.out.println("Your age: ");
                String ageInput = sc.nextLine();
                if (ageInput.isBlank()) ageInput = String.valueOf(rand.nextInt(10, 50));
                try {
                    age = Integer.parseInt(ageInput);
                    if (age < 10 || age > 100) {
                        System.out.println("Please enter a valid age between 10 and 100.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number for age!");
                }
            }

            UserInfo newUser = new UserInfo(name, surname, username, password, age, 0, String.valueOf(UUID.randomUUID()));


            boolean doesUserExist = users.stream().anyMatch(userInfo ->
                    Objects.equals(userInfo.getId(), newUser.getId()) ||
                            Objects.equals(userInfo.getUsername(), newUser.getUsername())
            );

            if (doesUserExist) {
                System.out.println("User already exists!");
            } else {
                users.add(newUser);

                try (Writer writer = new FileWriter(file)) {
                    gson.toJson(users, writer);
                } catch (IOException e) {
                    System.out.println("Error saving users: " + e.getMessage());
                }

                System.out.println("User added!");
            }
        }

        @Override
        public void showUsers() {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String filePath = "./DB/users.json";
            File file = new File(filePath);

            try {
                if (file.exists()) {
                    try (Reader reader = new FileReader(file)) {
                        Type userArrayListType = new TypeToken<ArrayList<UserInfo>>() {
                        }.getType();
                        users = gson.fromJson(reader, userArrayListType);

                        if (users.isEmpty()) {
                            System.out.println("No users found.");
                        } else {
                            for (int i = 0; i < users.size(); i++) {
                                System.out.println((i + 1) + ". Name: " + users.get(i).getUsername()
                                        + ". Surname: " + users.get(i).getSurname()
                                        + ". Username: " + users.get(i).getUsername()
                                        + ". Password: " + users.get(i).getPassword()
                                        + ". Age: " + users.get(i).getAge()
                                        + ". Balance $" + users.get(i).getBalance()
                                        + ". ID: " + users.get(i).getId());
                            }
                        }
                    }
                } else {
                    System.out.println("No users file found.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void deposit() {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String filePath = "./DB/users.json";
            File file = new File(filePath);

            try {
                if (file.exists()) {
                    try (Reader reader = new FileReader(file)) {
                        Type userArrayListType = new TypeToken<ArrayList<UserInfo>>() {
                        }.getType();
                        users = gson.fromJson(reader, userArrayListType);

                        if (users.isEmpty()) {
                            System.out.println("No users found.");
                        } else {
                            System.out.println("Enter the user id: ");
                            String id = sc.nextLine();

                            int depositAmount;
                            for (UserInfo user : users) {
                                if (user.getId().equals(id)) {
                                    System.out.println("Enter the amount of money you want to deposit: ");
                                    while (true) {
                                        String depositInput = sc.nextLine();
                                        if (depositInput.isBlank()) depositInput = "5";
                                        try {
                                            depositAmount = Integer.parseInt(depositInput);
                                            break;
                                        } catch (NumberFormatException e) {
                                            System.out.println("Please enter a number!");
                                        }
                                    }

                                    user.setBalance(user.getBalance() + depositAmount);
                                }
                            }

                            System.out.println("Funds were successfully deposited!");
                        }
                    }
                } else {
                    System.out.println("No users file found.");
                }

                try (Writer writer = new FileWriter(file)) {
                    gson.toJson(users, writer);
                } catch (IOException e) {
                    System.out.println("Error saving users: " + e.getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void withdraw() {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String filePath = "./DB/users.json";
            File file = new File(filePath);

            try {
                if (file.exists()) {
                    try (Reader reader = new FileReader(file)) {
                        Type userArrayListType = new TypeToken<ArrayList<UserInfo>>() {
                        }.getType();
                        users = gson.fromJson(reader, userArrayListType);

                        if (users.isEmpty()) {
                            System.out.println("No users found.");
                        } else {
                            boolean isFound = true;
                            boolean isAmountTrue = true;
                            System.out.println("Enter the user id: ");
                            String id = sc.nextLine();

                            int withdrawAmount;
                            for (UserInfo user : users) {
                                if (user.getId().equals(id)) {
                                    System.out.println("Enter the amount of money you want to withdraw: ");
                                    while (true) {
                                        String withdrawInput = sc.nextLine();
                                        if (withdrawInput.isBlank()) withdrawInput = "5";
                                        try {
                                            withdrawAmount = Integer.parseInt(withdrawInput);
                                            break;
                                        } catch (NumberFormatException e) {
                                            System.out.println("Please enter a number!");
                                        }
                                    }

                                    if (user.getBalance() > withdrawAmount) {
                                        user.setBalance(user.getBalance() - withdrawAmount);
                                    } else {
                                        isAmountTrue = false;
                                        break;
                                    }

                                    isFound = true;
                                    break;

                                } else {
                                    isFound = false;
                                }
                            }

                            if (!isFound || !isAmountTrue) {
                                System.out.println("User is not found or Withdrawing amount is too big!!");
                            }

                            if (isFound && isAmountTrue) {
                                System.out.println("Funds were successfully withdrawn!");
                            }
                        }
                    }
                } else {
                    System.out.println("No users file found.");
                }

                try (Writer writer = new FileWriter(file)) {
                    gson.toJson(users, writer);
                } catch (IOException e) {
                    System.out.println("Error saving users: " + e.getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
