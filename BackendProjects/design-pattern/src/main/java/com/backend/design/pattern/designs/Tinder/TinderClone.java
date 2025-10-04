package com.backend.design.pattern.designs.Tinder;

import com.backend.design.pattern.designs.Tinder.Enums.Gender;
import com.backend.design.pattern.designs.Tinder.Enums.SwipeAction;
import com.backend.design.pattern.designs.Tinder.Models.Location;
import com.backend.design.pattern.designs.Tinder.Models.Preference;
import com.backend.design.pattern.designs.Tinder.Models.User;
import com.backend.design.pattern.designs.Tinder.Models.UserProfile;

import java.util.List;

public class TinderClone {

    public static void main(String[] args) {
        try {
            // Get the dating app instance
            TinderApp app = TinderApp.getInstance();

            // Create users
            User user1 = app.createUser("user1");
            User user2 = app.createUser("user2");

            // Set user1 profile
            UserProfile profile1 = user1.getProfile();
            profile1.setName("Rohan");
            profile1.setAge(28);
            profile1.setGender(Gender.MALE);
            profile1.setBio("I am a software developer");
            profile1.addPhoto("rohan_photo1.jpg");
            profile1.addInterest("Coding", "Programming");
            profile1.addInterest("Travel", "Lifestyle");
            profile1.addInterest("Music", "Entertainment");

            // Setup user1 preferences
            Preference pref1 = user1.getPreference();
            pref1.addGenderPreference(Gender.FEMALE);
            pref1.setAgeRange(25, 30);
            pref1.setMaxDistance(10.0);
            pref1.addInterest("Coding");
            pref1.addInterest("Travel");

            // Setup user2 profile
            UserProfile profile2 = user2.getProfile();
            profile2.setName("Neha");
            profile2.setAge(27);
            profile2.setGender(Gender.FEMALE);
            profile2.setBio("Art teacher who loves painting and traveling.");
            profile2.addPhoto("neha_photo1.jpg");
            profile2.addInterest("Painting", "Art");
            profile2.addInterest("Travel", "Lifestyle");
            profile2.addInterest("Music", "Entertainment");

            // Setup user2 preferences
            Preference pref2 = user2.getPreference();
            pref2.addGenderPreference(Gender.MALE);
            pref2.setAgeRange(27, 30);
            pref2.setMaxDistance(15.0);
            pref2.addInterest("Coding");
            pref2.addInterest("Movies");

            // Set location for user1
            Location location1 = new Location();
            location1.setLatitude(1.01);
            location1.setLongitude(1.02);
            profile1.setLocation(location1);

            // Set location for user2 (Close to user1, within 5km)
            Location location2 = new Location();
            location2.setLatitude(1.03);
            location2.setLongitude(1.04);
            profile2.setLocation(location2);

            // Display user profiles
            System.out.println("---- User Profiles ----");
            app.displayUser("user1");
            app.displayUser("user2");

            // Find nearby users for user1 (within 5km)
            System.out.println("\n---- Nearby Users for user1 (within 5km) ----");
            List<User> nearbyUsers = app.findNearbyUsers("user1", 5.0);
            System.out.println("Found " + nearbyUsers.size() + " nearby users");
            for (User user : nearbyUsers) {
                System.out.println("- " + user.getProfile().getName() + " (" + user.getUserID() + ")");
            }

            // User1 swipes right on User2
            System.out.println("\n---- Swipe Actions ----");
            System.out.println("User1 swipes right on User2");
            app.swipe("user1", "user2", SwipeAction.RIGHT);

            // User2 swipes right on User1 (creating a match)
            System.out.println("User2 swipes right on User1");
            app.swipe("user2", "user1", SwipeAction.RIGHT);

            // Send messages in the chat room
            System.out.println("\n---- Chat Room ----");
            app.sendMessage("user1", "user2", "Hi Neha, Kaise ho?");

            app.sendMessage("user2", "user1", "Hi Rohan, Ma bdiya tum btao");

            // Display the chat room
            app.displayChatRoom("user1", "user2");
        } catch (Exception e) {
            System.out.println("SOME ERRORED OCCURED !!");
        }
    }
}
