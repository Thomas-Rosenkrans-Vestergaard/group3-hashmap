package group3.hashmap;

import java.util.*;

public class HashMap<K, V> implements Map<K, V>
{

    /**
     * The default number of buckets in the {@link HashMap}.
     */
    private static final int DEFAULT_CAPACITY = 16;

    /**
     * The default load factor.
     */
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    /**
     * The number of buckets in the {@link HashMap}.
     */
    private int capacity = DEFAULT_CAPACITY;

    /**
     * The load factor is a measure of how full the hash table is allowed to get before its capacity is automatically
     * increased. When the number of entries in the hash table exceeds the product of the load factor and the current
     * capacity, the hash table is expanded.
     */
    private double loadFactor = DEFAULT_LOAD_FACTOR;

    /**
     * The number of entries in the {@link HashMap}.
     */
    private int entryCount;

    /**
     * The buckets storing the entries in the {@link HashMap}.
     */
    private List<Entry<K, V>> buckets;

    /**
     * Creates a new empty HashMap.
     */
    public HashMap()
    {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Creates a new {@link HashMap} using the provided <code>capacity</code>.
     *
     * @param capacity The <code>capacity</code> is the number of buckets used.
     */
    public HashMap(int capacity)
    {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Creates a new {@link HashMap} using the provided <code>loadFactor</code>.
     *
     * @param loadFactor The load factor is a measure of how full the hash table is allowed to get before its capacity
     *                   is automatically increased. When the number of entries in the hash table exceeds the product
     *                   of the load factor and the current capacity, the hash table is expanded.
     */
    public HashMap(double loadFactor)
    {
        this(DEFAULT_CAPACITY, loadFactor);
    }

    /**
     * Creates a new empty {@link HashMap} with a provided bucket count.
     *
     * @param capacity   The number of buckets to create in the {@link HashMap}.
     * @param loadFactor The load-factor to use in the {@link HashMap}.
     */
    public HashMap(int capacity, double loadFactor)
    {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.buckets = new ArrayList<>(capacity);
        this.entryCount = 0;
        for (int x = 0; x < capacity; x++) {
            this.buckets.add(null);
        }
    }

    /**
     * Creates a new {@link HashMap} with the entries from the provided {@link Map}.
     *
     * @param m
     */
    public HashMap(Map<? extends K, ? extends V> m)
    {
        this.putAll(m);
    }

    /**
     * Represents a key-value pair in the {@link HashMap}.
     *
     * @param <K> The key type.
     * @param <V> The value type.
     */
    private static class Entry<K, V> implements Map.Entry<K, V>
    {

        /**
         * The hashCode of the key.
         */
        private final int hash;

        /**
         * The key of the key-value pair.
         */
        private final K key;

        /**
         * The value of the key-value pair.
         */
        private V value;

        /**
         * The next entry in this bucket.
         */
        private Entry<K, V> next;

        /**
         * Creates a new key-value pair.
         *
         * @param key   The key in the key-value pair.
         * @param value The value in the key-value pair.
         * @param next  The next entry in this bucket.
         */
        public Entry(int hash, K key, V value, Entry next)
        {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        /**
         * Returns the key in the key-value pair.
         *
         * @return The key in the key-value pair.
         */
        public K getKey()
        {
            return this.key;
        }

        /**
         * Returns the value in the key-value pair.
         *
         * @return The value in the key-value pair.
         */
        public V getValue()
        {
            return this.value;
        }

        /**
         * Sets the value in the key-value pair.
         *
         * @param value The new value to set.
         * @return The value just replaced.
         */
        public V setValue(V value)
        {
            V before = value;
            this.value = value;
            return before;
        }

        /**
         * Sets the reference to the next key-value pair in the bucket.
         *
         * @param next The reference to set.
         */
        public void setNext(Entry next)
        {
            this.next = next;
        }
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    @Override public int size()
    {
        return entryCount;
    }

    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings
     */
    @Override public boolean isEmpty()
    {
        return entryCount == 0;
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * key.  More formally, returns <tt>true</tt> if and only if
     * this map contains a mapping for a key <tt>k</tt> such that
     * <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be
     * at most one such mapping.)
     *
     * @param key key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     * key
     * @throws NullPointerException When the key is <code>null</code>.
     */
    @Override public boolean containsKey(Object key)
    {
        if (key == null)
            throw new NullPointerException("Keys cannot be null.");

        int         hashCode    = key.hashCode();
        int         bucketIndex = Math.abs(hashCode) % capacity;
        Entry<K, V> current     = buckets.get(bucketIndex);

        while (current != null) {
            if (current.hash == hashCode && current.key.equals(key))
                return true;

            current = current.next;
        }

        return false;
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the
     * specified value.  More formally, returns <tt>true</tt> if and only if
     * this map contains at least one mapping to a value <tt>v</tt> such that
     * <tt>(value==null ? v==null : value.equals(v))</tt>.
     *
     * @param value value whose presence in this map is to be tested
     * @return <tt>true</tt> if this map maps one or more keys to the
     * specified value
     */
    @Override public boolean containsValue(Object value)
    {
        for (Entry<K, V> head : buckets) {
            Entry<K, V> current = head;
            while (current != null) {
                if (current.value.equals(value))
                    return true;

                current = current.next;
            }
        }

        return false;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     * <p>
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     * <p>
     * <p>If this map permits null values, then a return value of
     * {@code null} does not <i>necessarily</i> indicate that the map
     * contains no mapping for the key; it's also possible that the map
     * explicitly maps the key to {@code null}.  The {@link #containsKey
     * containsKey} operation may be used to distinguish these two cases.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     * {@code null} if this map contains no mapping for the key
     * @throws NullPointerException When the provided key is <code>null</code>.
     */
    @Override public V get(Object key)
    {
        if (key == null)
            throw new NullPointerException("Key cannot be null");

        int         hashCode    = key.hashCode();
        int         bucketIndex = Math.abs(hashCode) % capacity;
        Entry<K, V> current     = buckets.get(bucketIndex);

        while (current != null) {
            if (current.hash == hashCode && current.key.equals(key)) {
                return current.getValue();
            }

            current = current.next;
        }

        return null;
    }

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation).  If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.  (A map
     * <tt>m</tt> is said to contain a mapping for a key <tt>k</tt> if and only
     * if {@link #containsKey(Object) m.containsKey(k)} would return
     * <tt>true</tt>.)
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>key</tt>,
     * if the implementation supports <tt>null</tt> values.)
     * @throws NullPointerException When the provided key is <code>null</code>.
     */
    @Override public V put(K key, V value)
    {
        if (key == null)
            throw new NullPointerException("Keys cannot be null");

        int         hashCode    = key.hashCode();
        int         bucketIndex = Math.abs(hashCode) % capacity;
        Entry<K, V> head        = buckets.get(bucketIndex);

        if (head == null) {
            buckets.set(bucketIndex, new Entry<>(hashCode, key, value, null));
            entryCount++;
            if (needsExpansion())
                expand();
            return null;
        }

        if (head.hash == hashCode && head.key.equals(key)) {
            V before = head.getValue();
            head.setValue(value);
            return before;
        }

        Entry<K, V> previous = head;
        Entry<K, V> current  = head.next;

        while (true) {

            if (current == null) {
                previous.setNext(new Entry<>(hashCode, key, value, null));
                entryCount++;
                if (needsExpansion())
                    expand();
                return null;
            }

            if (current.hash == hashCode && current.key.equals(key)) {
                current.setValue(value);
                return current.value;
            }

            //if (current.next == null) {
                //current.setNext(new Entry<>(hashCode, key, value, null));
                //entryCount++;
                //if (needsExpansion())
                    //expand();
                //return null;
            //}

            previous = current;
            current = current.next;
        }
    }

    /**
     * Removes the mapping for a key from this map if it is present
     * (optional operation).   More formally, if this map contains a mapping
     * from key <tt>k</tt> to value <tt>v</tt> such that
     * <code>(key==null ?  k==null : key.equals(k))</code>, that mapping
     * is removed.  (The map can contain at most one such mapping.)
     * <p>
     * <p>Returns the value to which this map previously associated the key,
     * or <tt>null</tt> if the map contained no mapping for the key.
     * <p>
     * <p>If this map permits null values, then a return value of
     * <tt>null</tt> does not <i>necessarily</i> indicate that the map
     * contained no mapping for the key; it's also possible that the map
     * explicitly mapped the key to <tt>null</tt>.
     * <p>
     * <p>The map will not contain a mapping for the specified key once the
     * call returns.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * @throws NullPointerException When the provided key is <code>null</code>.
     */
    @Override public V remove(Object key)
    {
        if (key == null)
            throw new NullPointerException("Keys cannot be null");

        int         hashCode    = key.hashCode();
        int         bucketIndex = Math.abs(hashCode) % capacity;
        Entry<K, V> head        = buckets.get(bucketIndex);

        Entry<K, V> previous = null;
        Entry<K, V> current  = head;

        while (current != null) {

            if (current.hash == hashCode && current.key.equals(key)) {
                V before = current.getValue();
                if (previous != null)
                    previous.setNext(current.next);
                else
                    buckets.set(bucketIndex, current.next);
                entryCount--;
                return before;
            }

            previous = current;
            current = current.next;
        }

        return null;
    }

    /**
     * Copies all of the mappings from the specified map to this map
     * (optional operation).  The effect of this call is equivalent to that
     * of calling {@link #put(Object, Object) put(k, v)} on this map once
     * for each mapping from key <tt>k</tt> to value <tt>v</tt> in the
     * specified map.  The behavior of this operation is undefined if the
     * specified map is modified while the operation is in progress.
     *
     * @param m mappings to be stored in this map
     * @throws NullPointerException if the specified map is null
     */
    @Override public void putAll(Map<? extends K, ? extends V> m)
    {
        if (m == null)
            throw new NullPointerException();

        if (needsExpansion(m.size() + entryCount)) {
            expand();
        }

        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Removes all of the mappings from this map (optional operation).
     * The map will be empty after this call returns.
     */
    @Override public void clear()
    {
        this.entryCount = 0;
        for (int x = 0; x < capacity; x++) {
            buckets.set(x, null);
        }
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own <tt>remove</tt> operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not
     * support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a collection view of the values contained in this map
     */
    @Override public Collection<V> values()
    {
        Collection<V> result = new ArrayList<>();
        for (Entry<K, V> head : buckets) {
            Entry<K, V> current = head;
            while (current != null) {
                result.add(current.value);
                current = current.next;
            }
        }

        return result;
    }

    /**
     * Checks if the {@link HashMap} needs an expansion with the provided number of entries. The {@link HashMap}
     * needs to be expanded when the number of entries equals or exceeds the <code>capacity * loadFactor</code>.
     *
     * @param entries The number of entries to use, when checking whether or not the {@link HashMap} needs to be
     *                expanded.
     * @return Whether or not the {@link HashMap} needs an expansion.
     */
    private boolean needsExpansion(int entries)
    {
        return entries >= capacity * loadFactor;
    }

    /**
     * Checks if the {@link HashMap} needs an expansion with the current number of entries. The {@link HashMap}
     * needs to
     * be expanded when the number of entries equals or exceeds the <code>capacity * loadFactor</code>.
     *
     * @return Whether or not the {@link HashMap} needs an expansion.
     */
    private boolean needsExpansion()
    {
        return needsExpansion(entryCount);
    }

    /**
     * Expands the {@link HashMap} by doubling the <code>capacity</code> and expanding the entries.
     */
    private void expand()
    {
        List<Entry<K, V>> entries = getEntries();
        for (int x = 0; x < capacity; x++) {
            buckets.set(x, null);
            buckets.add(null);
        }

        this.capacity *= 2;
        this.entryCount = 0;
        for (Entry<K, V> entry : entries) {
            put(entry.key, entry.value);
        }
    }

    /**
     * Returns a list of the entries in the {@link HashMap}.
     *
     * @return The list of the entries in the {@link HashMap}.
     */
    private List<Entry<K, V>> getEntries()
    {
        List<Entry<K, V>> entries = new ArrayList<>();
        for (Entry<K, V> head : buckets) {
            if (entries.size() == entryCount)
                break;
            Entry<K, V> current = head;
            while (current != null) {
                entries.add(current);
                current = current.next;
            }
        }

        return entries;
    }

    /**
     * Returns the current capacity of the {@link HashMap}. The capacity is the number of buckets in the
     * {@link HashMap}. The capacity will automatically increase depending on the <code>loadFactor</code> of the
     * {@link HashMap}.
     *
     * @return The current capacity.
     */
    public int getCapacity()
    {
        return this.capacity;
    }

    /**
     * Returns the <code>loadFactor</code> used in the {@link HashMap}.  The <code>loadFactor</code> influences how
     * often the {@link HashMap} is expanded. The {@link HashMap} is expanded when the number of entries equals or
     * exceeds the <code>capacity * loadFactor</code>.
     *
     * @return The <code>loadFactor</code>.
     */
    public double getLoadFactor()
    {
        return this.loadFactor;
    }

    /**
     * Unsupported operation.
     */
    @Override public Set<Map.Entry<K, V>> entrySet()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported operation.
     */
    @Override public Set<K> keySet()
    {
        throw new UnsupportedOperationException();
    }
}
