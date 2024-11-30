package org.example;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HashMapTest {

    Map<String, Integer> map = new HashMap<>();
    HashMapCustom<String, Integer> custom_map = new HashMapCustom<>();
    String[] charArray = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};


    @BeforeEach
    public void createMap(){
        map = new HashMap<>();
        custom_map = new HashMapCustom<>();
    }

    @Test
    public void putTest(){
        for (int i =0; i<10; i++){
            map.put(charArray[i], i);
            custom_map.put(charArray[i], i);
        }
        map.put(null, 0);
        custom_map.put(null, 0);
        Assertions.assertEquals(custom_map, map);
    }

    @Test
    public void getTest(){
        for (int i =0; i<10; i++){
            map.put(charArray[i], i);
            custom_map.put(charArray[i], i);
        }

        for(String ch: charArray){
            Assertions.assertEquals(custom_map.get(ch), map.get(ch));
        }
    }

    @Test
    public void containsKeyTest(){
        for (int i =0; i<10; i++){
            map.put(charArray[i], i);
            custom_map.put(charArray[i], i);
        }
        for(String ch: charArray){
            Assertions.assertEquals(custom_map.containsKey(ch), map.containsKey(ch));
        }
    }

    @Test
    public void containsValueTest(){
        for (int i =0; i<10; i++){
            map.put(charArray[i], i);
            custom_map.put(charArray[i], i);
        }
        for(int i =0; i<10; i++){
            Assertions.assertEquals(custom_map.containsValue(i), map.containsValue(i));
        }
    }

    @Test
    public void sizeTest(){
        for (int i =0; i<10; i++){
            map.put(charArray[i], i);
            custom_map.put(charArray[i], i);
        }
        Assertions.assertEquals(custom_map.size(), map.size());
    }

    @Test
    public void isEmptyTest(){
        for (int i =0; i<10; i++){
            map.put(charArray[i], i);
            custom_map.put(charArray[i], i);
        }
        for (String ch: charArray){
            map.remove(ch);
            custom_map.remove(ch);
        }
        Assertions.assertEquals(custom_map.isEmpty(), map.isEmpty());
    }

    @Test
    public void removeTest(){ //ок
        for (int i =0; i<10; i++){
            map.put(charArray[i], i);
            custom_map.put(charArray[i], i);
        }
        for (int i =0; i<4; i++){
            map.remove(charArray[i]);
            custom_map.remove(charArray[i]);
        }
        Assertions.assertEquals(custom_map, map);
    }

    @Test
    public void removeTest2(){ //ок
        for (int i =0; i<10; i++){
            map.put(charArray[i], i);
            custom_map.put(charArray[i], i);
        }
        for (int i =0; i<4; i++){
            map.remove(charArray[i], i);
            custom_map.remove(charArray[i], i);
        }
        Assertions.assertEquals(custom_map, map);
    }

    @Test
    public void putAllTest(){
        for (int i =0; i<10; i++){
            map.put(charArray[i], i);
            custom_map.put(charArray[i], i);
        }
        Map testMap = new HashMap<>();
        char[] testArray = {'o', 'p', 'q', 's'};
        for (int i =0; i<4; i++){
            testMap.put(testArray[i], i);
        }
        map.putAll(testMap);
        custom_map.putAll(testMap);
        Assertions.assertEquals(custom_map, map);
    }

    @Test
    public void clearTest(){
        for (int i =0; i<10; i++){
            map.put(charArray[i], i);
            custom_map.put(charArray[i], i);
        }
        map.clear();
        custom_map.clear();
        Assertions.assertEquals(custom_map, map);
    }

    @Test
    public void keySetTest(){
        for (int i =0; i<10; i++){
            map.put(charArray[i], i);
            custom_map.put(charArray[i], i);
        }
        Assertions.assertEquals(custom_map.keySet(), map.keySet());
    }

    @Test
    public void valuesTest(){
        for (int i =0; i<10; i++){
            map.put(charArray[i], i);
            custom_map.put(charArray[i], i);
        }
        Assertions.assertEquals(new ArrayList<>(custom_map.values()), new ArrayList<>(map.values()));
    }

    @Test
    public void entrySetTest(){
        for (int i =0; i<10; i++){
            map.put(charArray[i], i);
            custom_map.put(charArray[i], i);
        }
        var entriesFromCustomHashMap = custom_map.entrySet();
        var entriesFromMap = map.entrySet();
        for(var el: entriesFromCustomHashMap){
            if(entriesFromMap.contains(el.getValue())){
                throw new RuntimeException("This element is missing");
            }
        }
    }

    @Test
    public void IteratorTest(){
        for (int i =0; i<2; i++){
            map.put(charArray[i], i);
            custom_map.put(charArray[i], i);
        }

        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        Iterator<Map.Entry<String, Integer>> customIterator = custom_map.iterator();

        while (customIterator.hasNext() && iterator.hasNext()) {
            Map.Entry<String, Integer> customEntry = customIterator.next();
            Map.Entry<String, Integer> standardEntry = iterator.next();
            Assertions.assertEquals(customEntry.getKey(), standardEntry.getKey());
            Assertions.assertEquals(customEntry.getValue(), standardEntry.getValue());
        }

        iterator.remove();
        customIterator.remove();

        Assertions.assertEquals(custom_map.size(), map.size());
        iterator = map.entrySet().iterator();
        customIterator = custom_map.iterator();

        Map.Entry<String, Integer> customEntry = customIterator.next();
        Map.Entry<String, Integer> standardEntry = iterator.next();
        Assertions.assertEquals(customEntry.getKey(), standardEntry.getKey());
        Assertions.assertEquals(customEntry.getValue(), standardEntry.getValue());

        iterator.remove();
        customIterator.remove();

        Assertions.assertEquals(custom_map.isEmpty(), map.isEmpty());
    }
}
