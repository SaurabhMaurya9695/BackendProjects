package OOPS_ADVANCE;


class MyHashMap<K,V extends Number>{
    K key ;
    V value ;

    MyHashMap(){

    }

    MyHashMap(K key , V value){
        this.key = key ;
        this.value = value;
    }

    public K getKey() {
        return key;
    }
    public void setKey(K key) {
        this.key = key;
    }
    public V getValue() {
        return value;
    }
    public void setValue(V value) {
        this.value = value;
    }

}


public class MyHashMapGenericClass {
    public static void main(String[] args) {
        
        // key  -> Integer , Value -> Integer
        // HashMap<Integer,Integer> mp = new HashMap<>();
        // mp.put(1, 2);
        // mp.put(2, 3);

        // key  -> Object , Value -> Object
        // HashMap mp1 = new HashMap<>();
        // mp1.put("One", 2);
        // mp1.put(2, "Three");


        // MyHashMap mp = new MyHashMap("Rauank" , 12);
        // System.out.println(mp.getKey());
        // System.out.println(mp.getValue());
        
        MyHashMap<String  , Double> mp = new MyHashMap<>("Rauank", 12.5);
        System.out.println(mp.getKey());
        System.out.println(mp.getValue());
        
        MyHashMap<Character  , Integer> mp1 = new MyHashMap<>('R', 12);
        System.out.println(mp1.getKey());
        System.out.println(mp1.getValue());

        // MyHashMap<String  , String> mp2 = new MyHashMap<>("Rauank" , "Verma");
        // System.out.println(mp2.getKey());
        // System.out.println(mp2.getValue());


        // MyHashMap<String  , Character> mp4 = new MyHashMap<>("Rauank" , '2');
        // System.out.println(mp4.getKey());
        // System.out.println(mp4.getValue());

        // because we make the value as Number 


       
    }
}
