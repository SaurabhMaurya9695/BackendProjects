package com.backend.design.pattern.creational.factory;

public class OperatingSystemFactory {

    public OperatingSystem getOperatingSystem(String version, String architecture, String os) {
        return switch (os) {
            case "linux" -> new LinuxOperatingSystem(version, architecture);
            case "windows" -> new WindowsOperatingSystem(version, architecture);
            default -> {
                System.out.println("Operating System Not Found");
                yield null;
            }
        };
    }
}
