package DESIGN_PRINCIPLES.DIP.Solution;

public class DebitCard implements IBankService{

    @Override
    public void doTransaction(long amount){
        System.out.println("Transaction has been via DebitCard : " + amount);
    }
}
