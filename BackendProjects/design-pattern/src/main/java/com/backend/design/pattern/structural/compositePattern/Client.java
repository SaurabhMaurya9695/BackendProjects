package com.backend.design.pattern.structural.compositePattern;

public class Client {

    public static void main(String[] args) {
        try {
            Folder root = new Folder("root");
            root.add(new File("abc.txt", 1));
            root.add(new File("def.txt", 1));
            root.add(new File("ghi.pdf", 3));

            Folder subFolder = new Folder("subFolder1");
            subFolder.add(new File("jkl.txt", 2));
            subFolder.add(new File("mno.doc", 2));

            root.add(subFolder);

            System.out.println("🔝 ROOT: " + root.getName());
            root.ls();

            System.out.println("\n🔓 Opening everything:");
            root.openAll();

            System.out.println("\n📏 Total size of root = " + root.getSize());

            System.out.println("\n➡️ Navigate to subFolder1:");
            IFileSystemItem sub = root.cd("subFolder1");
            if (sub != null) {
                sub.ls();
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error: " + e.getMessage());
        }
    }
}
