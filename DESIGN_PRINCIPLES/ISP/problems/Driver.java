package DESIGN_PRINCIPLES.ISP.problems;

public class Driver {
    public static void main(String[] args) {
        IWorker human = new Men();
        IWorker robot = new Robot();

        human.eat();
        human.work();

        robot.eat();
        robot.work();
    }
}
