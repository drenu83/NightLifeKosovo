package com.example.nightlifekosovo.Models;

public class User {
    private String id;
    private String Username;
    private String Name;
    private String Surname;
    private String Birthday;
    private String Phone;
    private String Email;
    private String Type;
    private String imageUrl;

    public User() {
        this.Username = Username;
        this.Name = Name;
        this.Surname = Surname;
        this.Birthday = Birthday;
        this.Phone = Phone;
        this.Email = Email;
        this.Type = Type;
        this.imageUrl = imageUrl;
    }

    public User(String username, String name, String surname, String birthday, String phone, String email, String type, String imageUrl) {
        this.Username = username;
        this.Name = name;
        this.Surname = surname;
        this.Birthday = birthday;
        this.Phone = phone;
        this.Email = email;
        this.Type = type;
        this.imageUrl = imageUrl;
    }

    public User(String id, String username, String name, String surname, String birthday, String phone, String email, String type, String imageUrl) {
        this.id = id;
        this.Username = username;
        this.Name = name;
        this.Surname = surname;
        this.Birthday = birthday;
        this.Phone = phone;
        this.Email = email;
        this.Type = type;
        this.imageUrl = imageUrl;
    }

    public User(String username, String name, String surname, String birthday, String phone, String email, String type) {
        this.Username = username;
        this.Name = name;
        this.Surname = surname;
        this.Birthday = birthday;
        this.Phone = phone;
        this.Email = email;
        this.Type = type;
    }


    public User(String name, String phone, String email) {
        this.Name = name;
        this.Phone = phone;
        this.Email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        this.Surname = surname;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        this.Birthday = birthday;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
