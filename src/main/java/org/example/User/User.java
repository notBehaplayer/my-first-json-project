package org.example.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class User {
    public UserInterface userInterface = new UserInterface() {
        private final Scanner scanner = new Scanner(System.in);
        private final ArrayList<UserInfo> users = new ArrayList<>();
        private final Random random = new Random();

        @Override
        public void addFile() {
            String directoryPath = "./DB";
            String filePath = directoryPath + "/users.json";

            File directory = new File(directoryPath);
            File file = new File(filePath);

            try {
                if (!directory.exists() && directory.mkdirs()) {
                } else if (!directory.exists()) {
                }

                if (!file.exists() && file.createNewFile()) {
                    try (Writer writer = new FileWriter(file)) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        gson.toJson(new ArrayList<>(), writer);
                    }
                } else if (!file.exists()) {
                    System.out.println("Failed to create file: " + filePath);
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
                    Type userListType = new TypeToken<ArrayList<UserInfo>>() {
                    }.getType();
                    users.clear();
                    users.addAll(gson.fromJson(reader, userListType));
                } catch (IOException e) {
                    System.out.println("Error reading users file: " + e.getMessage());
                }
            }

            System.out.print("Enter your name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = "No name";

            System.out.print("Enter your surname: ");
            String surname = scanner.nextLine().trim();
            if (surname.isEmpty()) surname = "No Surname";

            System.out.print("Enter your username: ");
            String username = scanner.nextLine().trim();
            if (username.isEmpty()) username = "User " + (users.size() + 1);

            System.out.print("Enter your password: ");
            String password = scanner.nextLine().trim();
            if (password.isEmpty()) password = UUID.randomUUID().toString();

            int age;
            while (true) {
                System.out.print("Enter your age: ");
                String ageInput = scanner.nextLine().trim();
                if (ageInput.isEmpty()) ageInput = String.valueOf(random.nextInt(40) + 10);
                try {
                    age = Integer.parseInt(ageInput);
                    if (age < 10 || age > 100) {
                        System.out.println("Please enter a valid age between 10 and 100.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number for age!");
                }
            }

            UserInfo newUser = new UserInfo(name, surname, username, password, age, 0, UUID.randomUUID().toString());

            if (users.stream().anyMatch(user -> user.getId().equals(newUser.getId()) || user.getUsername().equals(newUser.getUsername()))) {
                System.out.println("User already exists!");
            } else {
                users.add(newUser);
                saveUsersToFile(file, gson);
                System.out.println("User added!");
            }
        }

        @Override
        public void removeUser() {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String filePath = "./DB/users.json";
            File file = new File(filePath);

            if (file.exists()) {
                try (Reader reader = new FileReader(file)) {
                    Type userListType = new TypeToken<ArrayList<UserInfo>>() {
                    }.getType();
                    users.clear();
                    users.addAll(gson.fromJson(reader, userListType));
                } catch (IOException e) {
                    System.out.println("Error reading users file: " + e.getMessage());
                }
            }

            if (users.isEmpty()) {
                System.out.println("No users found.");
            } else {
                boolean isUserFound = false;
                System.out.println("To remove user please enter his id: ");
                String id = scanner.nextLine();

                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getId().equals(id)) {
                        users.remove(i);
                        saveUsersToFile(file, gson);
                        System.out.println("User removed!");
                        isUserFound = true;
                        break;
                    }
                }

                if (!isUserFound) {
                    System.out.println("User not found!");
                }
            }
        }

        @Override
        public void showUsers() {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String filePath = "./DB/users.json";
            File file = new File(filePath);

            if (file.exists()) {
                try (Reader reader = new FileReader(file)) {
                    Type userListType = new TypeToken<ArrayList<UserInfo>>() {
                    }.getType();
                    users.clear();
                    users.addAll(gson.fromJson(reader, userListType));

                    if (users.isEmpty()) {
                        System.out.println("No users found.");
                    } else {
                        for (int i = 0; i < users.size(); i++) {
                            UserInfo user = users.get(i);
                            System.out.printf("%d. Name: %s, Surname: %s, Username: %s, Password: %s, Age: %d, Balance: $%d, ID: %s%n",
                                    i + 1, user.getName(), user.getSurname(), user.getUsername(), user.getPassword(),
                                    user.getAge(), user.getBalance(), user.getId());
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error reading users file: " + e.getMessage());
                }
            } else {
                System.out.println("No users file found.");
            }
        }

        @Override
        public void deposit() {
            processTransaction("deposit");
        }

        @Override
        public void withdraw() {
            processTransaction("withdraw");
        }

        private void processTransaction(String transactionType) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String filePath = "./DB/users.json";
            File file = new File(filePath);

            if (file.exists()) {
                try (Reader reader = new FileReader(file)) {
                    Type userListType = new TypeToken<ArrayList<UserInfo>>() {
                    }.getType();
                    users.clear();
                    users.addAll(gson.fromJson(reader, userListType));

                    if (users.isEmpty()) {
                        System.out.println("No users found.");
                        return;
                    }

                    System.out.print("Enter the user ID: ");
                    String id = scanner.nextLine().trim();

                    UserInfo user = users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);

                    if (user == null) {
                        System.out.println("User not found.");
                        return;
                    }

                    System.out.printf("Enter the amount to %s: ", transactionType);
                    int amount;
                    while (true) {
                        String input = scanner.nextLine().trim();
                        if (input.isEmpty()) input = "5";
                        try {
                            amount = Integer.parseInt(input);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number!");
                        }
                    }

                    if ("withdraw".equals(transactionType) && user.getBalance() < amount) {
                        System.out.println("Insufficient balance!");
                    } else {
                        user.setBalance(user.getBalance() + ("deposit".equals(transactionType) ? amount : -amount));
                        System.out.printf("Transaction successful! New balance: $%.2f%n", user.getBalance());
                        saveUsersToFile(file, gson);
                    }
                } catch (IOException e) {
                    System.out.println("Error reading users file: " + e.getMessage());
                }
            } else {
                System.out.println("No users file found.");
            }
        }

        private void saveUsersToFile(File file, Gson gson) {
            try (Writer writer = new FileWriter(file)) {
                gson.toJson(users, writer);
            } catch (IOException e) {
                System.out.println("Error saving users: " + e.getMessage());
            }
        }
    };
}
