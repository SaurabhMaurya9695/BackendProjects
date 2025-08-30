package com.backend.design.pattern.structural.compositePattern;

public interface IFileSystemItem {

    void ls();

    IFileSystemItem cd(String directory);

    int getSize();

    String getName();

    boolean isFolder();

    void openAll();
}
