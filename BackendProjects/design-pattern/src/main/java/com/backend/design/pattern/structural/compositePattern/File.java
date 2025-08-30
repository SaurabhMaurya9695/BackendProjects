package com.backend.design.pattern.structural.compositePattern;

// Leaf node
public class File implements IFileSystemItem {

    private final String _fileName;
    private final int _size;

    public File(String name, int size) {
        this._size = size;
        this._fileName = name;
    }

    @Override
    public void ls() {
        System.out.println("📄 File : " + _fileName + " [size=" + _size + "]");
    }

    @Override
    public IFileSystemItem cd(String directory) {
        System.out.println("❌ Cannot cd into a file: " + _fileName);
        return null;
    }

    @Override
    public int getSize() {
        return _size;
    }

    @Override
    public String getName() {
        return _fileName;
    }

    @Override
    public boolean isFolder() {
        return false;
    }

    @Override
    public void openAll() {
        System.out.println("🔓 Opening file: " + _fileName);
    }
}
