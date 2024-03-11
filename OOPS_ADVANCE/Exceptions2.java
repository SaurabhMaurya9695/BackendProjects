package OOPS_ADVANCE;

import java.util.Scanner;

class Calculater {
    public int add(int a , int b){
        return (a + b) ;
    }

    public int subtract(int a , int b){
        return (a - b) ;
    }

    public int multiply(int a , int b){
        return (a * b) ;
    }
    public int divide(int a , int b){
        return (a / b) ;
    }
}

public class Exceptions2 {
    public static void ex1(String[] args) {   // ["dddd" , "asdsds" , "asfuyfs"]
        System.out.println(args);
        try{
            System.out.println(args[0]);
            Integer a = Integer.parseInt(args[0]); // converting string to integer
            Integer b = Integer.parseInt(args[1]);
            try{
                System.out.println(a / b);
            }
            catch(Exception E){
                System.out.println("This is An Exception :" + E.getClass());
            }
        }
        catch(Exception E){
            System.out.println("Please Pass Some value To args" + E.getClass());
        }
        finally{
            System.out.println("clean up code");
        }
        
    }


    public static void ex2(String[] args) {
        try{
            Integer a = Integer.parseInt(args[0]); // converting string to integer
            Integer b = Integer.parseInt(args[1]);
            try{
                System.out.println(a / b);
            }
            catch(ArithmeticException e){
                System.out.println("This is An ArithmeticException so please" 
                + "Pass Value greator then zero");
            }

            int aa = 10 ;
            int bb = 20 ;
            System.out.println(aa + bb);
        }
        catch(Exception e){
            System.out.println("some Exception occured : " + e.getClass());
        }
        catch(ArithmeticException e){
            System.out.println("This is An ArithmeticException so please" 
            + "Pass Value greator then zero");
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("This is A IndexOutOfBoundsException so please" 
            + "Pass some args");
        }
        catch(NumberFormatException e){
            System.out.println("This is A NumberFormatException so please" 
            + "Pass some valid args which is number");
        }
        
        finally{
            System.out.println("clean up code");
        }
    }


    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);

        try{
            int x = scn.nextInt();
            int y = scn.nextInt();

            Calculater cal = new Calculater();

            System.out.println(cal.add(x, y));
            System.out.println(cal.subtract(x, y));
            System.out.println(cal.multiply(x, y));
            try {
                System.out.println(cal.divide(x, y));
            } catch (Exception e) {
                System.out.println(e.getClass());
            }
            
        }
        catch(Exception e){
            System.out.println("Please pass Some valid Input :) ");
        }
        finally{
            scn.close();
        }
    }
}