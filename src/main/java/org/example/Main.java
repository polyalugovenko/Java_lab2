package org.example;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String[] charArray = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
        Map<String, Integer>  map = new HashMap<>();
        HashMapCustom<String, Integer> custom_map = new HashMapCustom<>();
        for (int i =0; i<10; i++){
            map.put(charArray[i], i);
            custom_map.put(charArray[i], i);
        }
        System.out.println(map.entrySet());
        System.out.println(custom_map.entrySet());
        map.put(null, 0);
        custom_map.put(null, 0);
        if(custom_map.equals(map)){
            System.out.println("True");
        }
        else{
            System.out.println("False");
        }
    }
}