


// class Student{
//     String name ;
//     String address ;
//     String pinCode;
//     String city;
//     String country;
//     String collegeName ;
//     String collegeColor ;
//     String collegeAddress;

//     void show(){
//         System.out.println(this.address + " " + this.city + " " + this.collegeAddress
//         +" " + this.collegeColor + " " + this.collegeName + " " + this.country 
//         + " " + this.name);
//     }
// }

// public class Aggregation {
//     public static void main(String[] args) {
//         Student student = new Student() ;
//         student.address ="x";
//         student.name ="rauank";
//         student.pinCode ="1234";
//         student.city ="lucknow";
//         student.country = "India";
//         student.collegeName= "SRM";
//         student.collegeColor ="white";
//         student.collegeAddress ="tiwariganj";

//     }
// }





class Address{
    String pinCode;
    String city;
    String country;
}

class College{
    String collegeName ;
    String collegeColor ;
    String collegeAddress;
}


class Student{
    private String name ;
    //Student has an addresss & college
    private Address address;
    private College college ;
    // by this way we can achive aggrgation 
    Student(){
        
    }
    
    Student(String name , Address address , College college){
        this.name = name ;
        this.address = address;
        this.college = college ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public College getCollege() {
        return college;
    }

    

    // @Override
    public String toString() {
        return "Student [name=" + name + ", address=" + address + ", college=" + college + "]";
    }

    public void setCollege(College college) {
        this.college = college;
    }
}

public class Aggregation {
    public static void main(String[] args) {
        Student student = new Student() ;
        student.setName( "rauank Verma"); 
        
        //1st way to set obj 
        Address address = new Address() ;
        address.city = "Lucknow";
        address.country = "India";
        address.pinCode = "1234";

        College college = new College() ;
        college.collegeAddress = "tiwargiang";
        college.collegeColor = "white";
        college.collegeName =  "SRMCEM" ;

        student.setAddress(address);
        student.setCollege(college);

        

    }
}
