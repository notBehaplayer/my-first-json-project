package org.example.User;

public class UserInfo {
    private String name;
    private String surname;
    private String username;
    private String password;
    private int age;
    private int balance;
    private String id;

    public UserInfo(String name, String surname, String username, String password, int age, int balance, String id) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.age = age;
        this.balance = balance;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserInfo{" + "name='" + name + '\'' + ", surname='" + surname + '\'' + ", username='" + username + '\'' + ", email='" + password + '\'' + ", age=" + age + ", id='" + id + '\'' + '}';
    }
}
