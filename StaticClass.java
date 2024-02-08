// A java program to demonstrate use
// of static keyword with Classes

import java.io.*;

public class StaticClass {

    private static String str = "GeeksforGeeks";

    // Static class
    static class MyNestedClass {

        // non-static method
        public void disp() {
            System.out.println(str);
        }
    }

    public static void main(String args[]) {
        GFG.MyNestedClass obj = new GFG.MyNestedClass();
        obj.disp();
    }
}
