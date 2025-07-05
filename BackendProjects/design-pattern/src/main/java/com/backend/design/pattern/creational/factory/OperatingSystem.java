package com.backend.design.pattern.creational.factory;

// It is a kind of class where we define the common things / functionality which we want to see for different os
public abstract class OperatingSystem {

    private String version;
    private String architecture;

    public OperatingSystem(String version, String architecture) {
        this.version = version;
        this.architecture = architecture;
    }

    private String getVersion() {
        return version;
    }

    private void setVersion(String version) {
        this.version = version;
    }

    private String getArchitecture() {
        return architecture;
    }

    private void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    // these are the methods which we want to be common in both of the OS
    public abstract void deleteDirectory();

    public abstract void setDirectory();
}
