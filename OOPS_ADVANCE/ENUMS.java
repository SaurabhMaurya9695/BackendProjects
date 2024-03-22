package OOPS_ADVANCE;

import java.util.Scanner;

public class ENUMS {
    
    enum IPL{
        RCB("Virat Kholi") , CSK("Rituraj"), KKR("Iyyer") , MI("Rohit") , PSBK("Shiker") , LSG("Rahul") , SRH("Commins") , DC("Pant") , RR("Samson") , GL("Gill") ;
        
        private String captainName ;
        private IPL(String name){
            this.captainName = name;
        }
    }


    public static void main(String[] args) throws Exception {
        Scanner scn  = new Scanner(System.in);
        try{
            int date = scn.nextInt();
            switch (date) {
                case 22:
                    System.out.println(IPL.CSK.captainName + " VS " + IPL.RCB.captainName);
                    break;
                case 23:
                    System.out.println(IPL.RR.captainName + " VS " + IPL.LSG.captainName);
                    break;
                case 24:
                    System.out.println(IPL.GL.captainName + " VS " + IPL.KKR.captainName);
                    break;
                case 25:
                    System.out.println(IPL.KKR.captainName + " VS " + IPL.GL.captainName);
                    break;
                case 26:
                    System.out.println(IPL.LSG.captainName + " VS " + IPL.RR.captainName);
                    break;
            
                default:
                    System.out.println("There is not matched sheduled on this date ");
                    break;
            }
        }
        catch(Exception e){
            System.out.println("InputFormatException occured !!");
        }


    }
}
