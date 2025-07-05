package com.backend.design.pattern.creational.factory;

public class LinuxOperatingSystem extends OperatingSystem {

    LinuxOperatingSystem(String version, String architecture) {
        super(version, architecture);
    }

    @Override
    public void deleteDirectory() {
        System.out.println("Deleting directory in LinuxOperatingSystem");
    }

    @Override
    public void setDirectory() {
        System.out.println("Setting directory in LinuxOperatingSystem");
    }
}
