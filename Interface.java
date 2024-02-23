

interface Student{
    String name = "Rauank" ;

    // inteface not allow to write the body
    // before the compilation compiler added "public abstract" before the method name
    public abstract void ShowName();
    public abstract void Show();
}

class A implements Student{

    @Override
    public void ShowName() {
        System.out.println("My name is Rauank");
    }

    @Override
    public void Show() {
        System.out.println("show working");
    }
}

public class Interface {
    public static void main(String[] args) {
        Student stu = new A();
        stu.Show();
        stu.ShowName();
    }
}
