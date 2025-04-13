package com.backend.design.pattern.factory;

public class WindowsOperatingSystem extends OperatingSystem {

    WindowsOperatingSystem(String version, String architecture) {
        super(version, architecture);
    }

    @Override
    public void deleteDirectory() {
        System.out.println("Deleting directory in WindowsOperatingSystem");
    }

    @Override
    public void setDirectory() {
        System.out.println("Setting directory in WindowsOperatingSystem");
    }
}
