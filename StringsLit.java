public class StringsLit {
    public static void main(String[] args) {

        /*
         * String s = "Rauank";
         * String t = s ;
         * System.out.println(t);
         * System.out.println(s == t);
         * 
         */


        //  String s = new String("Rauank"); // created object in SCP
        //  String t = "Rauank" ; // created a new object in SCP
        //  System.out.println(t); 
        //  System.out.println(s == t); // due to diff in address op is false
        //  System.out.println(s.equals(t)); // this check for values not for address

        String s ="raunak";
        String t = s.concat("");
        System.out.println(t); // raunak
        System.out.println(s); // rauank
        System.out.println(s == t); // false 
        System.out.println(s.equals(t)); // true

         
    }
}
