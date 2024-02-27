package OOPS_ADVANCE;

import java.util.ArrayList;

// class Student{
//     private String name ;
//     private String rollno;

//     Student(){
        
//     }
//     //parameterize constr
//     Student(String receiveName , String receiveRollNo){
//         this.name = receiveName;
//         this.rollno = receiveRollNo;
//     }

//     public String getName() {
//         return name;
//     }
//     public void setName(String name) {
//         this.name = name;
//     }
//     public String getRollno() {
//         return rollno;
//     }
//     public void setRollno(String rollno) {
//         this.rollno = rollno;
//     }
// }

public class Generics {

    @SuppressWarnings("removal")
    public static void main(String[] args) {
        
        // without generic 
        // ArrayList<Integer> lst = new ArrayList<>();
        // // this is an array list which can store integers
        // lst.add(10);
        // lst.add(20);
        // lst.add(30);

        // ArrayList<Object> lst = new ArrayList<>();
        // // object is a parent class so you can inject all type of data whithin it 
        // lst.add(10);
        // lst.add("Rauank");
        // lst.add(2.0f);
        // lst.add(9.0);

        // Student stu = new Student();
        // stu.setName("Rauank");
        // stu.setRollno("12");

        // lst.add(stu);
        
        // System.out.println(lst);

        Object [] obj = new Object[10];
        obj[0] = 10 ; // premitive -> it is written as a premitive but behind the scene in backend it
        // is written as new Integer(10);
        obj[1] = new Integer(8); // wrapper
        // what is the main differnce between above two syntaxes ?

        obj[2] = "Raunak"; // string literal -> we have String Constant Poll (SCP);
        obj[3] = new StringBuilder("Raunak"); // string builder 
        // what is the main differnce between above two syntaxes ?

        System.out.println(obj); // address or hashValue 

        // for(int i = 0 ; i< obj.length ; i ++){
        //     System.out.println(obj[i]);
        // }
        
        // ((StringBuilder)obj[2]).append("world"); // downcasting 

        if(obj[2] instanceof StringBuilder){ // downcasting checking
            ((StringBuilder)obj[2]).append("world");
        }
        else{
            System.out.println("This is not possible");
        }

        
        for(int i = 0 ; i< obj.length ; i ++){
            System.out.println(obj[i]);
        }







    }
}