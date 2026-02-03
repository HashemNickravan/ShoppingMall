package com.shoppingmall.model;

public class User {

    private String id;
    private String username;
    private String password;
    private Role role;
    private long balance;

    public User(String id, String username, String password, Role role, long balance) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.balance = balance;
    }

    public User(String id, String username, String password, String admin) {
    }

    public User(String username, String password, Role role, int i) {
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public long getBalance() { return balance; }
    public void setBalance(long balance) { this.balance = balance; }
}
