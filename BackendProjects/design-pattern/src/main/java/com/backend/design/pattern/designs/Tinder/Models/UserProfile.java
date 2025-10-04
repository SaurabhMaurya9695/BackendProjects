package com.backend.design.pattern.designs.Tinder.Models;

import com.backend.design.pattern.designs.Tinder.Enums.Gender;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {

    private String name;
    private int age;
    private Gender gender;
    private String bio;
    private List<String> photos;
    private List<Interest> interests;
    private Location location;

    public UserProfile() {
        name = "";
        age = 0;
        gender = Gender.OTHER;
        photos = new ArrayList<>();
        interests = new ArrayList<>();
        location = new Location();
    }

    public void setName(String n) {
        name = n;
    }

    public void setAge(int a) {
        age = a;
    }

    public void setGender(Gender g) {
        gender = g;
    }

    public void setBio(String b) {
        bio = b;
    }

    public void addPhoto(String photoUrl) {
        photos.add(photoUrl);
    }

    public void removePhoto(String photoUrl) {
        photos.remove(photoUrl);
    }

    public void addInterest(String name, String category) {
        Interest interest = new Interest(name, category);
        interests.add(interest);
    }

    public void removeInterest(String name) {
        interests.removeIf(i -> i.getName().equals(name));
    }

    public void setLocation(Location loc) {
        location = loc;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public String getBio() {
        return bio;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public Location getLocation() {
        return location;
    }

    public void display() {
        System.out.println("===== Profile =====");
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.print("Gender: ");
        switch (gender) {
            case MALE:
                System.out.print("Male");
                break;
            case FEMALE:
                System.out.print("Female");
                break;
            case NON_BINARY:
                System.out.print("Non-binary");
                break;
            case OTHER:
                System.out.print("Other");
                break;
        }
        System.out.println();
        System.out.println("Bio: " + bio);
        System.out.print("Photos: ");
        for (String photo : photos) {
            System.out.print(photo + ", ");
        }
        System.out.println();
        System.out.print("Interests: ");
        for (Interest i : interests) {
            System.out.print(i.getName() + " (" + i.getCategory() + "), ");
        }
        System.out.println();
        System.out.println("Location: " + location.getLatitude() + ", " + location.getLongitude());
        System.out.println("===================");
    }
}
