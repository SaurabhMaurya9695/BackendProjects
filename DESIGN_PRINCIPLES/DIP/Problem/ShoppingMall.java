package DESIGN_PRINCIPLES.DIP.Problem;

public class ShoppingMall {
    
    // here mall is totally depend on the cards ->
    private DebitCard debitCard;

    ShoppingMall(){

    }

    public ShoppingMall(DebitCard card){
        this.debitCard = card;
    }


    public void doTransaction(long amount){
        this.debitCard.doTransaction(amount);
    }



    public static void main(String[] args) {
        CreditCard creditCard = new CreditCard() ;
        DebitCard debitCard = new DebitCard() ;
        ShoppingMall mall = new ShoppingMall(debitCard);
        mall.doTransaction(5000);
    }

}
