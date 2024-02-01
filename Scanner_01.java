import java.util.ArrayList;
import java.util.Scanner;

public class Scanner_01 {
    public static void main(String[] args) {
        ArrayList<Integer> lst = new ArrayList<>();  // this goes in GC
        Scanner scn = new Scanner(System.in);
        int t = scn.nextInt();
        System.out.println(t);
    }
}
