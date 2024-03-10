package OOPS_ADVANCE;

import java.util.Scanner;

public class Exceptions {
    public static void main(String[] args) {

        System.out.println("Starting Normally");

        Scanner scn = new Scanner(System.in);
        int a = scn.nextInt();
        int b = scn.nextInt();
        char operation = scn.next().charAt(0);

        switch (operation) {
            case '+':{
                System.out.println(a + b);   
                break;
            }
            case '*':{
                System.out.println(a * b);   
                break;
            }
            case '/':{
                try {
                    System.out.println(a / b);       
                } catch (Exception e) {
                    System.out.println("Arthmatic Exception Occured Here !!");
                }
                
                break;
            }
            case '-':{
                System.out.println(a - b);   
                break;
            }
            default:{
                System.out.println("Invalid Operation");
                break;
            }
        }
        scn.close();
        System.out.println("Terminated Normally");
    }
    public static void ex1(String[] args) {
        String s = null;
        System.out.println(s.charAt(0));
    }
}