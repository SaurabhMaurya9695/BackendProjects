package com.backend.design.pattern.designs.Tinder.Models;

import com.backend.design.pattern.designs.Tinder.Enums.Gender;

import java.util.ArrayList;
import java.util.List;

public class Preference {

    private List<Gender> _interestedList;
    private int minAge;
    private int maxAge;
    private double maxDistance;
    private List<String> _interest;

    public Preference() {
        minAge = 18;
        maxAge = 60;
        maxDistance = 100.0;
        _interestedList = new ArrayList<>();
        _interest = new ArrayList<>();
    }

    public void addGenderPreference(Gender gender) {
        _interestedList.add(gender);
    }

    public void removeGenderPreference(Gender gender) {
        _interestedList.remove(gender);
    }

    public void addInterest(String interest) {
        _interest.add(interest);
    }

    public void removeInterest(String interest) {
        _interest.remove(interest);
    }

    public boolean isInterestedInGender(Gender gender) {
        return _interestedList.contains(gender);
    }

    public void setAgeRange(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public boolean isAgeInRange(int age) {
        return age >= minAge && age <= maxAge;
    }

    public boolean isDistanceAcceptable(double distance) {
        return distance <= maxDistance;
    }

    public List<Gender> getInterestedList() {
        return _interestedList;
    }

    public void setInterestedList(List<Gender> interestedList) {
        _interestedList = interestedList;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public List<String> getInterest() {
        return _interest;
    }

    public void setInterest(List<String> interest) {
        _interest = interest;
    }
}
