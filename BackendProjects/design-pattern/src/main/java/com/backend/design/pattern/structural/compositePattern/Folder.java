package com.backend.design.pattern.structural.compositePattern;

import java.util.ArrayList;
import java.util.List;

// Composite
public class Folder implements IFileSystemItem {

    private final String _folderName;
    private final List<IFileSystemItem> _subItems; // can contain files and folders

    public Folder(String folderName) {
        this._folderName = folderName;
        this._subItems = new ArrayList<>();
    }

    public void add(IFileSystemItem item) {
        _subItems.add(item);
    }

    @Override
    public void ls() {
        System.out.println("📁 Folder: " + _folderName);
        for (IFileSystemItem item : _subItems) {
            item.ls();
        }
    }

    @Override
    public IFileSystemItem cd(String directory) {
        for (IFileSystemItem item : _subItems) {
            if (item.isFolder() && item.getName().equals(directory)) {
                System.out.println("➡️ Changing directory to: " + item.getName());
                return item;
            }
        }
        System.out.println("❌ Folder not found: " + directory);
        return null;
    }

    @Override
    public int getSize() {
        int total = 0;
        for (IFileSystemItem item : _subItems) {
            total += item.getSize();
        }
        return total;
    }

    @Override
    public String getName() {
        return _folderName;
    }

    @Override
    public boolean isFolder() {
        return true;
    }

    @Override
    public void openAll() {
        System.out.println("📂 Opening folder: " + _folderName);
        for (IFileSystemItem subItem : _subItems) {
            subItem.openAll();
        }
    }
}
