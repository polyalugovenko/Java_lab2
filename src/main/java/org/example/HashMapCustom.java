package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HashMapCustom<K, V> implements Map<K, V>, Iterable<Map.Entry<K, V>> {
    private static final int DEFAULT_CAPACITY= 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node < K, V> [] table;
    private int size;
    private int threshold;
    private Set<Entry<K,V>> entrySet;
    public HashMapCustom() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashMapCustom(int capacity, double loadFactor) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        if (loadFactor <= 0) {
            throw new IllegalArgumentException("Load factor must be positive");
        }
        this.table = new Node[capacity];
        this.size = 0;
        this.threshold = (int) (capacity * loadFactor);
    }

    @NotNull
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new EntryIterator();
    }


    private static class Node<K,V> implements Entry<K, V>{
        private final K key;
        private V value;
        Node<K,V> next;

        public Node(K key, V value, Node<K,V> next){
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return this.value;
        }

        @Override
        public final String toString() {
            return key + "=" + value; }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;

            if (obj instanceof Node) {
                Node<K, V> node = (Node<K, V>) obj;
                return key.equals(node.getKey()) &&
                        value.equals(node.getValue());
            }
            return false;
        }
    }



    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();

        if (table == null || table.length == 0) {
            return "{}";
        }

        for (Node<K, V> buckets : table) {
            while (buckets != null) {
                sb.append(buckets);
                if (buckets.next != null) {
                    sb.append(", ");
                }
                buckets = buckets.next;
            }
        }

        if (!sb.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }

        return '{' + sb.toString() + '}';
    }


    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof HashMap<?,?>)){
            return false;
        }
        HashMap map = (HashMap) obj;
        if (map.size() != size){return false;}
        for(Node<K, V> node: table){
            while(node!=null){
                V otherValue = (V) map.get(node.key);
                if(!otherValue.equals(node.value)) {return false;}
                node = node.next;
            }
        }
        return true;
    }

    @Override
    public int size() { //ок
        return size;
    }

    @Override
    public boolean isEmpty() { //ок
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) { //ок
        int index = key == null ? 0 : key.hashCode() % table.length;// это индекс для добавляемой ноды
        Node<K, V> current = table[index]; // это элемент по индексу, который получили выше

        while (current != null){
            if (current.key.equals(key)){
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) { //ок
        for (Node<K, V> node: table){
            while(node!=null){
                if (node.value.equals(value)){
                    return true;
                }
                node = node.next;
            }
        }
        return false;
    }

    @Override
    public V put(K key, V value){ //ок

        Node<K, V> node = new Node<>(key, value, null); // это нода, которую планируем добавить
        int index = key == null ? 0 : key.hashCode() % table.length; // это индекс для добавляемой ноды
        Node<K, V> current = table[index]; // это элемент по индексу, который получили выше

        if (current == null){ // если по этому индексу ничего нет, то просто вставляе
            table[index] = node;
            size++;
            current = table[index]; // чтобы NPO не кидал, потому что каррент нулл
        } else{
            while(current.next != null){ // если по индексу что-то есть, то чекаем весь бакет
                if (current.key.equals(key)){ // если совпадение ключей, то это не новый элемент, а замена старого
                    current.value = value; // переписываем и выходим из функции
                }
                current = current.next;
            }
            if(current.key.equals(key)) { // следующий элемент пустой, но из цикла мы уже вышли и его ключ не проверен
                current.value = value; // если ключ совпал, то это перезапись
            } else{
                current.next = node; // иначе добавляем следующую ноду
                size++;
            }
        }
        if (size > threshold)
            resize();
        return current.value;
    }

    @Override
    public V get(Object key){ //ок

        int index = key == null ? 0 : key.hashCode() % table.length; // это индекс для добавляемой ноды
        Node<K, V> current = table[index]; // это элемент по индексу, который получили выше

        while (current != null){
            if (current.key.equals(key)){
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public V remove(Object key){

        int index = key == null ? 0 : key.hashCode() % table.length;; // это индекс для добавляемой ноды
        Node<K, V> current = table[index]; // это элемент по индексу, который получили выше
        Node<K, V> toReturn = current;
        Node<K, V> previous = null; // предыдущий
        
        while (current!= null){
            if (current.key.equals(key)){ // нашли совпадение ключей
                if(previous == null){ // если это первый элемент из бакета
                    table[index] = table[index].next;
                    size--;
                    return toReturn.value;
                }else{
                    previous.next = current.next;
                    size--;
                    return toReturn.value;
                }
            }
            previous = current;
            current = current.next;
        }
        return null;
    }


    private void resize(){
        Node<K, V> [] old_table = table;
        table = new Node[table.length * 2];
        size = 0;
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> bucket: old_table){
            while (bucket != null){
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }


    @Override
    public void putAll(Map<? extends K, ? extends V> m) { //ок
        for (Entry<? extends K, ? extends V> e : m.entrySet()) {
            K key = e.getKey();
            V value = e.getValue();
            put(key, value);
        }
    }

    @Override
    public void clear() {
        HashMapCustom.Node<K,V>[] tab;
        if ((tab = table) != null) {
            // Очищаем все элементы в массиве
            for (int i = 0; i < tab.length; i++) {
                tab[i] = null;
            }
        }
        size = 0; // Сбрасываем размер
    }

    @Override
    public Set<K> keySet() { //ок
        Set<K> ks = new HashSet<>();
        for (Node<K, V> node: table){
            while (node!=null){
                ks.add(node.key);
                node = node.next;
            }
        }
        return ks;
    }

    @Override
    public Collection<V> values() { //ок
        Collection<V> values = new ArrayList<>();
        for (Node<K, V> node: table){
            while (node!=null){
                values.add(node.value);
                node = node.next;
            }
        }
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() { //ок
        Set<Entry<K, V>> es = new HashSet<>();
        for (Node<K, V> node: table){
            while (node!=null){
                es.add(node);
                node = node.next;
            }
        }
        return es;
    }

    private class EntryIterator implements Iterator<Entry<K, V>> {
        private Node<K, V>[] table;
        private int index;
        private Node<K, V> current;
        private Node<K, V> lastReturned;

        public EntryIterator() {
            this.table = HashMapCustom.this.table;
            this.index = 0;
            this.current = null;
            this.lastReturned = null;
            advance(); // Инициализируем начальное состояние
        }

        private void advance() {
            while (index < table.length && (current == null)) {
                current = table[index++];
            }
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Node<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("The following element is missing!");
            }
            lastReturned = current;
            current = current.next;
            if (current == null) {
                advance();
            }
            return lastReturned;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            HashMapCustom.this.remove(lastReturned.getKey());
            lastReturned = null;
        }
    }

}
