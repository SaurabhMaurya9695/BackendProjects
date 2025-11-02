package com.backend.system.design.LLD.ParkingLot.SaurabhWay.strategy;

import com.backend.system.design.LLD.ParkingLot.SaurabhWay.models.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PriceByStartTimeAndEndTime implements PriceStrategy {

    private final Vehicle _vehicle;
    private final String _startTime;
    private final String _endTime;

    public PriceByStartTimeAndEndTime(Vehicle vehicle) {
        this._vehicle = vehicle;
        this._startTime = vehicle.getStartTime();
        this._endTime = vehicle.getEndTime();
    }

    @Override
    public int calculatePrice() {
        if (_startTime == null || _endTime == null) {
            throw new IllegalArgumentException("Start time or end time cannot be null");
        }

        try {
            // Same format as used when storing times
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy");

            // Parse start and end times
            LocalDateTime start = LocalDateTime.parse(_startTime, formatter);
            LocalDateTime end = LocalDateTime.parse(_endTime, formatter);

            // Calculate total minutes parked
            long minutes = Duration.between(start, end).toMinutes();

            // Round up to the nearest hour
            long hours = (minutes + 59) / 60;

            // Price rate (could be extended per vehicle type later)
            int pricePerHour = 10;

            return (int) (hours * pricePerHour);
        } catch (Exception e) {
            System.err.println("Error calculating price: " + e.getMessage());
            return 0;
        }
    }

    public Vehicle getVehicle() {
        return _vehicle;
    }

    public String getStartTime() {
        return _startTime;
    }

    public String getEndTime() {
        return _endTime;
    }
}
