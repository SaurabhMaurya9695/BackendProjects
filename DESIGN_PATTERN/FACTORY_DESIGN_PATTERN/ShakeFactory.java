package DESIGN_PATTERN.FACTORY_DESIGN_PATTERN;

public class ShakeFactory {

    public static IShake getFactory(String type){
        if(type.trim().equalsIgnoreCase("OREO")){
            return new Oreo();
        }
        else if(type.trim().equalsIgnoreCase("VANILLA")){
            return new Vanilla();
        }
        else if(type.trim().equalsIgnoreCase("BADAM")){
            return new BadamShake();
        }
        return null;
    }

}
