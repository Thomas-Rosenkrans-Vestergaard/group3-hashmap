package tvestergaard.hashmap;

import java.util.*;

public class HashMap<K, V> implements Map<K, V> {

    /**
     * The default number of buckets in the {@link HashMap}.
     */
    private static final int DEFAULT_CAPACITY = 16;

    /**
     * The default load factor.
     */
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    /**
     * The load factor is a measure of how full the hash table is allowed to get before its capacity is automatically
     * increased. When the number of entries in the hash table exceeds the product of the load factor and the current
     * capacity, the hash table is expanded.
     */
    private double loadFactor = DEFAULT_LOAD_FACTOR;

    /**
     * The number of entries in the {@link HashMap}.
     */
    private int size;

    /**
     * The buckets storing the entries in the {@link HashMap}.
     */
    private Node<K, V>[] buckets;

    /**
     * Cached collection of values <code>V</code> that can be returned by the {@link HashMap#values()} method.
     */
    private Collection<V> cachedValueCollection;

    /**
     * Cached set of entries that can be returned by the {@link HashMap#entrySet()} method.
     */
    private Set<Entry<K, V>> cachedEntrySet = null;

    /**
     * Cached set of keys <code>K</code> that can be returned by the {@link HashMap#keySet()} method.
     */
    private Set<K> cachedKeySet = null;

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
     *                   is automatically increased. When the number of entries in the hash table exceeds the
     *                   product of
     *                   the load factor and the current capacity, the hash table is expanded.
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
     *
     * @throws IllegalArgumentException When the provided <code>capacity</code> is less than 2.
     * @throws IllegalArgumentException When the provided <code>loadFactor</code> is outside the range <code>0 <
     *                                  loadFactor <= 1</code>.
     */
    public HashMap(int capacity, double loadFactor)
    {
        if (capacity < 2)
            throw new IllegalArgumentException("Capacity must be greater than 2.");

        if (loadFactor <= 0)
            throw new IllegalArgumentException("Load factor must be greater than zero.");

        if (loadFactor > 1)
            throw new IllegalArgumentException("Load factor must less than or equal to one.");

        this.loadFactor = loadFactor;
        this.buckets = (Node<K, V>[]) new Node[capacity];
        this.size = 0;
    }

    /**
     * Creates a new {@link HashMap} with the entries from the provided <code>map</code>.
     *
     * @param map The map containing the entries to insert into the {@link HashMapIterator}.
     */
    public HashMap(Map<? extends K, ? extends V> map)
    {
        this();
        this.putAll(map);
    }

    /**
     * Represents a key-value pair in the {@link HashMap}.
     *
     * @param <K> The key type.
     * @param <V> The value type.
     */
    public static class Node<K, V> implements Entry<K, V> {

        /**
         * The hash of the key.
         */
        private final int hash;

        /**
         * The key of the {@link Node}.
         */
        private final K key;

        /**
         * The value of the {@link Node}.
         */
        private V value;

        /**
         * The next entry in the linked list bucket of this {@link Node}.
         */
        private Node<K, V> next;

        /**
         * Creates a new key-value node.
         *
         * @param hash  The hash of the provided key.
         * @param key   The key of the {@link Node}.
         * @param value The value of the {@link Node}.
         * @param next  The next entry in the linked list bucket of this {@link Node}.
         */
        public Node(int hash, K key, V value, Node<K, V> next)
        {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        /**
         * Creates a new key-value node.
         *
         * @param key   The key of the {@link Node}.
         * @param value The value of the {@link Node}.
         */
        public Node(K key, V value)
        {
            this(hash(key), key, value, null);
        }

        /**
         * Returns the key of the {@link Node}.
         *
         * @return The key of the {@link Node}.
         */
        public K getKey()
        {
            return this.key;
        }

        /**
         * Returns the value of the {@link Node}.
         *
         * @return The value of the {@link Node}.
         */
        public V getValue()
        {
            return this.value;
        }

        /**
         * Sets the value of the {@link Node}.
         *
         * @param value The new value of the {@link Node}.
         *
         * @return The old value.
         */
        public V setValue(V value)
        {
            V before = this.value;
            this.value = value;
            return before;
        }

        /**
         * Sets the reference to the next entry in the linked list bucket of this {@link Node}.
         *
         * @param next The new reference.
         */
        void setNext(Node<K, V> next)
        {
            this.next = next;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (!(o instanceof Entry)) return false;
            Entry<?, ?> entry = (Entry<?, ?>) o;
            return Objects.equals(key, entry.getKey()) &&
                    Objects.equals(value, entry.getValue());
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(hash, key, value);
        }
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified key.
     *
     * @param key The key whose presence in this map is to be tested.
     *
     * @return <tt>true</tt> if this map contains a mapping for the specified key.
     */
    @Override
    public boolean containsKey(Object key)
    {
        Node<K, V> node = getNode(hash(key), key);

        return node != null;
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the specified value.
     *
     * @param value The value whose presence in this map is to be tested.
     *
     * @return <tt>true</tt> if this map maps one or more keys to the specified value.
     */
    @Override
    public boolean containsValue(Object value)
    {
        for (Node<K, V> head : buckets) {
            Node<K, V> current = head;
            while (current != null) {
                if (current.value == null ? value == null : current.value.equals(value))
                    return true;

                current = current.next;
            }
        }

        return false;
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the
     * key.
     * <p>
     * <p>More formally, if this map contains a mapping from a key {@code k} to a value {@code v} such that {@code
     * (key==null ? k==null : key.equals(k))}, then this method returns {@code v}; otherwise it returns {@code null}.
     * (There can be at most one such mapping.)
     * <p>
     * <p>A return value of {@code null} does not <i>necessarily</i> indicate that the map contains no mapping for the
     * key; it's also possible that the map explicitly maps the key to {@code null}. The {@link #containsKey
     * containsKey} operation may be used to distinguish these two cases.
     *
     * @see #put(Object, Object)
     */
    @Override
    public V get(Object key)
    {
        Node<K, V> node = getNode(hash(key), key);

        return node == null ? null : node.value;
    }

    /**
     * Associates the specified value with the specified key in this map. If the map previously contained a mapping for
     * the key, the old value is replaced.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     *
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no mapping for
     * <tt>key</tt>. (A <tt>null</tt> return can also indicate that the map previously associated <tt>null</tt> with
     * <tt>key</tt>.)
     */
    @Override
    public V put(K key, V value)
    {
        return putNode(hash(key), key, value, null);
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     *
     * @param key key whose mapping is to be removed from the map
     *
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no mapping for
     * <tt>key</tt>. (A <tt>null</tt> return can also indicate that the map previously associated <tt>null</tt> with
     * <tt>key</tt>.)
     */
    @Override
    public V remove(Object key)
    {
        Node<K, V> removed = removeNode(hash(key), key);

        return removed == null ? null : removed.value;
    }

    /**
     * Copies all of the mappings from the specified map to this map. These mappings will replace any mappings
     * that this map had for any of the keys currently in the specified map.
     *
     * @param m mappings to be stored in this map. No entries are added if the provided map is <code>null</code>.
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m)
    {
        if (m != null) {
            for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
                K key = entry.getKey();
                V value = entry.getValue();
                putNode(hash(key), key, value, null);
            }
        }
    }

    /**
     * Removes all of the mappings from this map.
     * <p>
     * The map will be empty after this call returns.
     */
    @Override
    public void clear()
    {
        if (size > 0) {
            for (int x = 0; size > 0 && x < buckets.length; x++) {
                if (buckets[x] != null) {
                    buckets[x] = null;
                    size--;
                }
            }
        }
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map. The collection is backed by the map, so
     * changes to the map are reflected in the collection, and vice-versa.  If the map is modified while an iteration
     * over the collection is in progress (except through the iterator's own <tt>remove</tt> operation), the results of
     * the iteration are undefined.  The collection supports element removal, which removes the corresponding mapping
     * from the map, via the <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not support the <tt>add</tt> or <tt>addAll</tt>
     * operations.
     *
     * @return a view of the values contained in this map
     */
    @Override
    public Collection<V> values()
    {
        if (cachedValueCollection == null)
            cachedValueCollection = new HashMapValueCollection();

        return cachedValueCollection;
    }

    /**
     * Collection implementation allowing for collection methods on the values in the {@link HashMap}.
     */
    private class HashMapValueCollection implements Collection<V> {

        /**
         * Returns the number of elements in this collection.
         *
         * @return The number of elements in this collection.
         */
        @Override
        public int size()
        {
            return size;
        }

        /**
         * Returns <code>true</code> if this collection contains no elements.
         *
         * @return <code>true</code> if this collection contains no elements
         */
        @Override
        public boolean isEmpty()
        {
            return size == 0;
        }

        /**
         * Returns <code>true</code> if this collection contains the specified element.
         *
         * @param o element whose presence in this collection is to be tested.
         *
         * @return <code>true</code> if this collection contains the specified element.
         */
        @Override
        public boolean contains(Object o)
        {
            return containsValue(o);
        }

        /**
         * Returns an iterator over the elements in this collection.  There are no guarantees concerning the order in
         * which the elements are returned.
         *
         * @return an <tt>Iterator</tt> over the elements in this collection.
         */
        @Override
        public Iterator<V> iterator()
        {
            return new HashMapValueIterator();
        }

        /**
         * Iterator implementation for iterating through the values in the {@link HashMap}.
         */
        private class HashMapValueIterator extends HashMapIterator<V> {

            /**
             * Returns {@code true} if the iteration has more elements. (In other words, returns {@code true} if {@link
             * #next} would return an element rather than throwing an exception.)
             *
             * @return {@code true} if the iteration has more elements.
             */
            @Override
            public boolean hasNext()
            {
                return super.hasNext();
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the iteration
             * @throws NoSuchElementException if the iteration has no more elements
             */
            @Override
            public V next()
            {
                return super.nextNode().value;
            }
        }

        /**
         * Returns an array containing all of the elements in this collection.
         *
         * @return an array containing all of the elements in this collection
         */
        @Override
        public Object[] toArray()
        {
            int index = 0;
            Object[] result = new Object[size];
            for (Node<K, V> current : buckets) {
                while (current != null) {
                    result[index++] = current.value;
                    current = current.next;
                }
            }

            return result;
        }

        /**
         * Returns an array containing all of the elements in this collection; the runtime type of the returned
         * array is
         * that of the specified array. If the collection fits in the specified array, it is returned therein.
         * Otherwise, a new array is allocated with the runtime type of the specified array and the size of this
         * collection.
         * <p>
         *
         * @param a the array into which the elements of this collection are to be stored, if it is big enough;
         *          otherwise, a new array of the same runtime type is allocated for this purpose.
         *
         * @return an array containing all of the elements in this collection
         * @throws ArrayStoreException if the runtime type of the specified array is not a supertype of the runtime
         *                             type
         *                             of every element in this collection
         */
        @Override
        public <T> T[] toArray(T[] a)
        {
            int size = size();
            T[] r = a.length >= size && a != null ? a : (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(),
                    size
            );

            int nextIndex = 0;
            for (Node<K, V> current : buckets) {
                while (current != null) {
                    r[nextIndex++] = (T) current.value;
                    current = current.next;
                }
            }

            return r;
        }

        /**
         * Unsupported operation.
         *
         * @throws UnsupportedOperationException
         */
        @Override
        public boolean add(V v)
        {
            throw new UnsupportedOperationException();
        }

        /**
         * Removes a single instance of the specified element from this collection, if it is present.
         *
         * @param o element to be removed from this collection, if present
         *
         * @return <tt>true</tt> if an element was removed as a result of this call
         */
        @Override
        public boolean remove(Object o)
        {
            for (int x = 0; x < buckets.length; x++) {
                Node<K, V> current = buckets[x];
                Node<K, V> previous = null;
                while (current != null) {
                    if (current.value == null ? o == null : current.value.equals(o)) {
                        if (previous != null)
                            previous.setNext(current.next);
                        else
                            buckets[x] = current.next;
                        size--;
                        return true;
                    }

                    previous = current;
                    current = current.next;
                }
            }

            return false;
        }

        /**
         * Returns <tt>true</tt> if this collection contains all of the elements in the specified collection.
         *
         * @param c collection to be checked for containment in this collection
         *
         * @return <tt>true</tt> if this collection contains all of the elements in the specified collection.
         * @see #contains(Object)
         */
        @Override
        public boolean containsAll(Collection<?> c)
        {
            if (c == null || c.isEmpty())
                return false;

            for (Object o : c)
                if (!HashMap.this.containsValue(o))
                    return false;

            return true;
        }

        /**
         * Unsupported operation.
         *
         * @throws UnsupportedOperationException
         */
        @Override
        public boolean addAll(Collection<? extends V> c)
        {
            throw new UnsupportedOperationException();
        }

        /**
         * Removes all of this collection's elements that are also contained in the specified collection. After this
         * call returns, this collection will contain no elements in common with the specified collection.
         *
         * @param c collection containing elements to be removed from this collection.
         *
         * @return <tt>true</tt> if this collection changed as a result of the call.
         * @see #remove(Object)
         * @see #contains(Object)
         */
        @Override
        public boolean removeAll(Collection<?> c)
        {
            if (c == null || c.isEmpty())
                return false;

            boolean changed = false;
            for (int x = 0; x < buckets.length; x++) {
                Node<K, V> current = buckets[x];
                Node<K, V> previous = null;
                while (current != null) {
                    if (c.contains(current.value)) {
                        if (previous != null)
                            previous.setNext(current.next);
                        else
                            buckets[x] = current.next;
                        size--;
                        changed = true;
                    }

                    previous = current;
                    current = current.next;
                }
            }

            return changed;
        }

        /**
         * Retains only the elements in this collection that are contained in the specified collection.  In other
         * words,
         * removes from this collection all of its elements that are not contained in the specified collection.
         *
         * @param c collection containing elements to be retained in this collection
         *
         * @return <tt>true</tt> if this collection changed as a result of the call
         * @see #remove(Object)
         * @see #contains(Object)
         */
        @Override
        public boolean retainAll(Collection<?> c)
        {
            if (c == null || c.isEmpty())
                return false;

            boolean changed = false;
            for (int x = 0; x < buckets.length; x++) {
                Node<K, V> current = buckets[x];
                Node<K, V> previous = null;
                while (current != null) {
                    if (!c.contains(current.value)) {
                        if (previous != null)
                            previous.setNext(current.next);
                        else
                            buckets[x] = current.next;
                        size--;
                        changed = true;
                    }

                    previous = current;
                    current = current.next;
                }
            }

            return changed;
        }

        /**
         * Removes all of the elements from this collection.
         */
        @Override
        public void clear()
        {
            HashMap.this.clear();
        }
    }

    /**
     * Returns the current capacity of the {@link HashMap}. The capacity is the number of buckets in the {@link
     * HashMap}. The capacity will automatically increase depending on the <code>loadFactor</code> of the {@link
     * HashMap}.
     *
     * @return The current capacity.
     */
    public int capacity()
    {
        return this.buckets.length;
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
     * Iterator helper class for iterating through the nodes in the {@link HashMap}.
     *
     * @param <T>
     */
    abstract private class HashMapIterator<T> implements Iterator<T> {

        /**
         * The index of the bucket where the last returned entry is from.
         */
        private int currentBucket;

        /**
         * The {@link Node} last returned by the {@link HashMapIterator#nextNode()} method.
         */
        private Node<K, V> previous;

        /**
         * The previously returned entry.
         */
        private Node<K, V> nextEntry;

        /**
         * Creates a new {@link HashMapIterator}.
         */
        HashMapIterator()
        {
            currentBucket = -1;
            nextEntry = getNextHead();
        }

        /**
         * Returns {@code true} if the iteration has more elements. (In other words, returns {@code true} if  would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext()
        {
            return nextEntry != null;
        }

        /**
         * Removes from the underlying collection the last element returned by this iterator. This method can be called
         * only once per call to {@link #next}.  The behavior of an iterator is unspecified if the underlying
         * collection
         * is modified while the iteration is in progress in any way other than by calling this method.
         *
         * @throws IllegalStateException if the {@code next} method has not yet been called, or the {@code remove}
         *                               method has already been called after the last call to the {@code next} method
         */
        @Override
        public void remove()
        {
            if (previous == null)
                throw new IllegalStateException("Nothing to remove.");

            removeNode(previous.hash, previous.key);

            previous = null;
        }

        /**
         * Returns the nextNode element in the iteration.
         *
         * @return the nextNode element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        public Node<K, V> nextNode()
        {
            if (nextEntry == null)
                throw new NoSuchElementException();

            previous = nextEntry;
            nextEntry = getNextNode(previous);
            return previous;
        }

        /**
         * Finds the nextNode available entry from the current element. Returns either <code>current.nextNode</code> or
         * the head of the nextNode bucket.
         *
         * @param current The last returned entry.
         *
         * @return Returns the nextNode entry. Returns <code>null</code> if there are no nextNode entries.
         */
        private Node<K, V> getNextNode(Node<K, V> current)
        {
            if (current.next == null)
                return getNextHead();

            return current.next;
        }

        /**
         * Returns the head of the nextNode bucket.
         *
         * @return The head of the nextNode bucket. Returns <code>null</code> if there are no more heads.
         */
        private Node<K, V> getNextHead()
        {
            for (int x = currentBucket + 1; x < buckets.length; x++) {
                if (buckets[x] != null) {
                    currentBucket = x;
                    return buckets[x];
                }
            }

            return null;
        }
    }

    /**
     * Checks if the storage needs expanding if it had the provided number of <code>entries</code>. The storage
     * needs to
     * be expanded when the number of entries equals or exceeds the <code>capacity * loadFactor</code>.
     *
     * @param storage The storage.
     * @param entries The number of entries to use, when checking whether or not the storage needs to be expanded.
     *
     * @return Whether or not the storage needs an expansion.
     */

    private boolean needsExpansion(Node<K, V>[] storage, int entries)
    {
        return entries >= storage.length * loadFactor;
    }

    /**
     * Places an node using the provided <code>hashCode</code>, <code>key</code>, <code>value</code> and
     * <code>node</code> into a bucket in the storage of the {@link HashMap}.
     *
     * @param hash  The hashCode of the node.
     * @param key   The key of the node.
     * @param value The value of the node.
     * @param node  An existing {@link Node} instance. Can be provided so the method doesn't need to create a new
     *              instance. A new instance will only be created if this argument is <code>null</code>. The
     *              <code>node.nextNode</code> reference is always removed.
     *
     * @return The value that was replaced. Returns <code>null</code> if no value was replaced.
     */
    private V putNode(int hash, K key, V value, Node<K, V> node)
    {
        int bucketIndex = index(hash);
        Node<K, V> head = buckets[bucketIndex];

        if (node != null) {
            node.next = null;
        }

        if (head == null) {
            buckets[bucketIndex] = node == null ? new Node<>(hash, key, value, null) : node;
            size++;
            if (needsExpansion(buckets, size))
                expand();
            return null;
        }

        if (head.key == null ? key == null : head.key.equals(key)) {
            V before = head.getValue();
            head.setValue(value);
            return before;
        }

        Node<K, V> previous = head;
        Node<K, V> current = head.next;

        while (true) {

            if (current == null) {
                previous.setNext(node == null ? new Node<>(hash, key, value, null) : node);
                size++;
                if (needsExpansion(buckets, size))
                    expand();
                return null;
            }

            if (current.key == null ? key == null : current.key.equals(key)) {
                V before = current.value;
                current.setValue(value);
                return before;
            }

            previous = current;
            current = current.next;
        }
    }

    /**
     * Removes the {@link Node} with the provided <code>hash</code> and <code>key</code> from the map.
     *
     * @param hash The <code>hash</code> of the {@link Node} to remove.
     * @param key  The <code>key</code> of the {@link Node} to remove.
     *
     * @return The removed {@link Node}. Returns <code>null</code> if no {@link Node} was removed.
     */
    private Node<K, V> removeNode(int hash, Object key)
    {
        int bucketIndex = index(hash);
        Node<K, V> head = buckets[bucketIndex];

        Node<K, V> previous = null;
        Node<K, V> current = head;
        while (current != null) {
            if (current.key == null ? key == null : current.key.equals(key)) {
                if (previous != null)
                    previous.setNext(current.next);
                else
                    buckets[bucketIndex] = current.next;
                size--;
                return current;
            }

            previous = current;
            current = current.next;
        }

        return null;
    }

    /**
     * Gets the {@link Node} matching the provided <code>hash</code> and <code>key</code>.
     *
     * @param hash The hash the {@link Node} must match.
     * @param key  The key the {@link Node} must match.
     *
     * @return The matching {@link Node}. Returns <code>null</code> if no matching {@link Node} could be found.
     */
    private Node<K, V> getNode(int hash, Object key)
    {

        int bucketIndex = index(hash);
        Node<K, V> current = buckets[bucketIndex];
        while (current != null) {
            if (current.key == null ? key == null : current.key.equals(key)) {
                return current;
            }

            current = current.next;
        }

        return null;
    }

    /**
     * Gets the {@link Node} matching the key and value from the provided Map.Entry.
     *
     * @param o The Map.Entry that the target {@link Node} must match.
     *
     * @return The matching {@link Node}. Returns <code>null</code> if no matching {@link Node} could be found.
     */
    private Node<K, V> nodeFromEntry(Object o)
    {
        if (!(o instanceof Entry)) {
            return null;
        }

        Entry e = (Entry) o;
        Object key = e.getKey();
        return getNode(hash(key), key, e.getValue());
    }

    /**
     * Gets the node matching the provided hash, key and value.
     *
     * @param hash  The hash that the target {@link Node} must match.
     * @param key   The key that the target {@link Node} must match.
     * @param value The value that the target {@link Node} must match.
     *
     * @return The matching {@link Node}. Returns <code>null</code> if no matching {@link Node} could be found.
     */
    private Node<K, V> getNode(int hash, Object key, Object value)
    {
        int bucketIndex = index(hash);
        Node<K, V> current = buckets[bucketIndex];

        while (current != null) {
            if ((current.key == null ? key == null : current.key.equals(key)) &&
                    (value == null ? value == current.value : value.equals(current.value))) {
                return current;
            }

            current = current.next;
        }

        return null;
    }

    /**
     * Calculates the bucket index from the provided <code>hash</code>.
     *
     * @param hash The hash that the bucket index should be calculated from.
     *
     * @return The resulting bucket index.
     */
    private int index(int hash)
    {
        return Math.abs(hash) % buckets.length;
    }

    /**
     * Doubles the internal storage of the {@link HashMap}. The entries in the {@link HashMap} are reinserted using a
     * recalculated buckets index.
     */
    private void expand()
    {
        Node<K, V>[] before = buckets;
        buckets = (Node<K, V>[]) new Node[buckets.length * 2];
        size = 0;

        for (Node<K, V> head : before) {
            Node<K, V> current = head;
            while (current != null) {
                Node<K, V> next = current.next;
                putNode(current.hash, current.key, current.value, current);
                current = next;
            }
        }
    }

    /**
     * Hashes the provided key.
     *
     * @param key The key to hash.
     *
     * @return The resulting hash.
     */
    private static int hash(Object key)
    {
        return key == null ? 0 : key.hashCode();
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map. The set is backed by the map, so changes to
     * the
     * map are reflected in the set, and vice-versa.  If the map is modified while an iteration over the set is in
     * progress (except through the iterator's own <tt>remove</tt> operation, or through the <tt>setValue</tt>
     * operation
     * on a map entry returned by the iterator) the results of the iteration are undefined.  The set supports element
     * removal, which removes the corresponding mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not support
     * the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a set view of the mappings contained in this map
     */
    @Override
    public Set<Entry<K, V>> entrySet()
    {
        if (cachedEntrySet == null)
            cachedEntrySet = new HashMapEntrySet();

        return cachedEntrySet;
    }

    /**
     * {@link HashMap} backed {@link Set} allowing for {@link Set} operations on the {@link HashMap}.
     */
    private class HashMapEntrySet implements Set<Entry<K, V>> {

        /**
         * Returns the number of elements in this set.
         *
         * @return the number of elements in this set.
         */
        @Override
        public int size()
        {
            return HashMap.this.size();
        }

        /**
         * Returns <tt>true</tt> if this set contains no elements.
         *
         * @return <tt>true</tt> if this set contains no elements
         */
        @Override
        public boolean isEmpty()
        {
            return HashMap.this.size == 0;
        }

        /**
         * Returns <tt>true</tt> if this set contains the specified element.
         *
         * @param o element whose presence in this set is to be tested
         *
         * @return <tt>true</tt> if this set contains the specified element
         */
        @Override
        public boolean contains(Object o)
        {
            Node<K, V> found = nodeFromEntry(o);

            return found != null;
        }

        /**
         * Returns an iterator over the elements in this set.  The elements are returned in no particular order.
         *
         * @return an iterator over the elements in this set
         */
        @Override
        public Iterator<Entry<K, V>> iterator()
        {
            return new HashMapEntryIterator();
        }

        private class HashMapEntryIterator extends HashMapIterator<Entry<K, V>> {
            @Override
            public Entry<K, V> next()
            {
                return nextNode();
            }
        }

        /**
         * Returns an array containing all of the elements in this set. The resulting array can be modified without
         * affecting the set.
         *
         * @return the array containing all the elements in this set
         */
        @Override
        public Node<K, V>[] toArray()
        {
            int nextIndex = 0;
            Node<K, V>[] result = (Node<K, V>[]) new Node[size];
            for (Node<K, V> current : buckets) {
                while (current != null) {
                    result[nextIndex++] = current;
                    current = current.next;
                }
            }

            return result;
        }

        /**
         * Returns an array containing all of the elements in this set; the runtime type of the returned array is that
         * of the specified array. If the set fits in the specified array, it is returned therein. Otherwise, a new
         * array is allocated with the runtime type of the specified array and the size of this set.
         * <p>
         * <p>If this set fits in the specified array with room to spare (i.e., the array has more elements than this
         * set), the element in the array immediately following the end of the set is set to <tt>null</tt>.  (This is
         * useful in determining the length of this set <i>only</i> if the caller knows that this set does not contain
         * any null elements.)
         * <p>
         * <p>If this set makes any guarantees as to what order its elements are returned by its iterator, this method
         * must return the elements in the same order.
         * <p>
         * This method allows precise control over the runtime type of the output array.
         * <p>
         * <p>Suppose <tt>x</tt> is a set known to contain only strings. The following code can be used to dump the set
         * into a newly allocated array of <tt>String</tt>:
         * <p>
         * <pre>
         *     String[] y = x.toArray(new String[0]);</pre>
         * <p>
         *
         * @param a the array into which the elements of this set are to be stored, if it is big enough; otherwise, a
         *          new array of the same runtime type is allocated for this purpose.
         *
         * @return an array containing all the elements in this set
         * @throws ArrayStoreException if the runtime type of the specified array is not a supertype of the runtime
         *                             type
         *                             of every element in this set
         */
        @Override
        public <T> T[] toArray(T[] a)
        {
            int size = size();
            T[] r = a.length >= size && a != null ? a : (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(),
                    size
            );

            int nextIndex = 0;
            for (Node<K, V> current : buckets) {
                while (current != null) {
                    r[nextIndex++] = (T) current;
                    current = current.next;
                }
            }

            return r;
        }

        /**
         * Adds the provided entry to the {@link HashMapEntrySet} when it does not already exist in the set. The entry
         * is only added when a {@link Node} with the an equal key and value doesn't exist in the set. Null values
         * cannot be allowed.
         *
         * @param entry element to be added to this set
         *
         * @return <tt>true</tt> if this set did not already contain the specified element
         */
        @Override
        public boolean add(Entry<K, V> entry)
        {
            if (entry == null || nodeFromEntry(entry) != null)
                return false;

            K key = entry.getKey();
            putNode(key.hashCode(), key, entry.getValue(), null);
            return true;
        }

        /**
         * Removes the specified element from this set if it is present. More formally, removes an element <tt>e</tt>
         * such that <tt>(o==null ? e==null : o.equals(e))</tt>, if this set contains such an element.  Returns
         * <tt>true</tt> if this set contained the element (or equivalently, if this set changed as a result of the
         * call).
         *
         * @param o object to be removed from this set, if present
         *
         * @return <tt>true</tt> if this set contained the specified element
         */
        @Override
        public boolean remove(Object o)
        {
            if (!(o instanceof Entry))
                return false;

            Entry<K, V> e = (Entry<K, V>) o;

            for (int x = 0; x < buckets.length; x++) {
                Node<K, V> current = buckets[x];
                Node<K, V> previous = null;
                while (current != null) {
                    if (current.equals(e)) {
                        if (previous != null)
                            previous.setNext(current.next);
                        else
                            buckets[x] = null;
                        size--;
                        return true;
                    }

                    previous = current;
                    current = current.next;
                }
            }

            return false;
        }

        /**
         * Returns <tt>true</tt> if this set contains all of the elements of the specified collection.  If the
         * specified
         * collection is also a set, this method returns <tt>true</tt> if it is a <i>subset</i> of this set.
         *
         * @param c collection to be checked for containment in this set
         *
         * @return <tt>true</tt> if this set contains all of the elements of the specified collection
         * @see #contains(Object)
         */
        @Override
        public boolean containsAll(Collection<?> c)
        {
            if (c == null || c.isEmpty())
                return true;

            for (Object o : c)
                if (!contains(o))
                    return false;

            return true;
        }

        /**
         * Adds all of the elements in the specified collection to this set if they're not already present.  If the
         * specified collection is also a set, the <tt>addAll</tt> operation effectively modifies this set so that its
         * value is the <i>union</i> of the two sets.  The behavior of this operation is undefined if the specified
         * collection is modified while the operation is in progress.
         *
         * @param c collection containing elements to be added to this set
         *
         * @return <tt>true</tt> if this set changed as a result of the call
         * @see #add(Object)
         */
        @Override
        public boolean addAll(Collection<? extends Entry<K, V>> c)
        {
            boolean changed = false;
            if (c != null) {
                for (Entry<K, V> entry : c) {
                    if (add(entry)) {
                        changed = true;
                    }
                }
            }

            return changed;
        }

        /**
         * Retains only the elements in this set that are contained in the specified collection. In other words,
         * removes
         * from this set all of its elements that are not contained in the specified collection . If the specified
         * collection is also a set, this operation effectively modifies this set so that its value is the
         * <i>intersection</i> of the two sets.
         *
         * @param c collection containing elements to be retained in this set
         *
         * @return <tt>true</tt> if this set changed as a result of the call
         * @see #remove(Object)
         */
        @Override
        public boolean retainAll(Collection<?> c)
        {
            if (c == null || c.isEmpty()) {
                clear();
                return true;
            }

            boolean changed = false;
            for (int x = 0; x < buckets.length; x++) {
                Node<K, V> current = buckets[x];
                Node<K, V> previous = null;
                while (current != null) {
                    if (!c.contains(current)) {
                        if (previous != null)
                            previous.setNext(current.next);
                        else
                            buckets[x] = null;
                        size--;
                        changed = true;
                    }

                    previous = current;
                    current = current.next;
                }
            }

            return changed;
        }

        /**
         * Removes from this set all of its elements that are contained in the specified collection.  If the specified
         * collection is also a set, this operation effectively modifies this set so that its value is the
         * <i>asymmetric
         * set difference</i> of the two sets.
         *
         * @param c collection containing elements to be removed from this set
         *
         * @return <tt>true</tt> if this set changed as a result of the call
         * @see #remove(Object)
         * @see #contains(Object)
         */
        @Override
        public boolean removeAll(Collection<?> c)
        {
            if (c == null || c.isEmpty()) {
                return false;
            }

            boolean changed = false;
            for (Object o : c) {
                if (remove(o)) {
                    changed = true;
                }
            }

            return changed;
        }

        /**
         * Removes all of the elements from this set. The set will be empty after this call returns.
         */
        @Override
        public void clear()
        {
            HashMap.this.clear();
        }
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map. The set is backed by the map, so changes to the
     * map
     * are reflected in the set, and vice-versa.  If the map is modified while an iteration over the set is in progress
     * (except through the iterator's own <tt>remove</tt> operation), the results of the iteration are undefined.  The
     * set supports element removal, which removes the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt>
     * operations.  It does not support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a set view of the keys contained in this map
     */
    @Override
    public Set<K> keySet()
    {
        if (cachedKeySet == null)
            cachedKeySet = new HashMapKeySet();

        return cachedKeySet;
    }

    /**
     * {@link HashMap} backed {@link Set} allowing for {@link Set} operations on the keys in the {@link HashMap}.
     */
    private class HashMapKeySet implements Set<K> {
        /**
         * Returns the number of elements in this set.
         *
         * @return the number of elements in this set.
         */
        @Override
        public int size()
        {
            return HashMap.this.size;
        }

        /**
         * Returns <tt>true</tt> if this set contains no elements.
         *
         * @return <tt>true</tt> if this set contains no elements
         */
        @Override
        public boolean isEmpty()
        {
            return HashMap.this.size == 0;
        }

        /**
         * Returns <tt>true</tt> if this set contains the specified element.
         *
         * @param o element whose presence in this set is to be tested
         *
         * @return <tt>true</tt> if this set contains the specified element
         */
        @Override
        public boolean contains(Object o)
        {
            return getNode(hash(o), o) != null;
        }

        /**
         * Returns an iterator over the elements in this set.
         *
         * @return an iterator over the elements in this set
         */
        @Override
        public Iterator<K> iterator()
        {
            return new HashMapKeyIterator();
        }

        /**
         * Implementation of {@link Iterator} allowing iteration over the keys in the nodes in the {@link HashMap} with
         * minimal overhead.
         */
        private final class HashMapKeyIterator extends HashMapIterator<K> {
            @Override
            public K next()
            {
                return nextNode().key;
            }
        }

        /**
         * Returns an array containing all of the elements in this set. If this set makes any guarantees as to what
         * order its elements are returned by its iterator, this method must return the elements in the same order.
         *
         * @return an array containing all the elements in this set
         */
        @Override
        public Object[] toArray()
        {
            int nextIndex = 0;
            Object[] result = new Object[size];
            for (Node<K, V> current : buckets) {
                while (current != null) {
                    result[nextIndex++] = current.key;
                    current = current.next;
                }
            }

            return result;
        }

        /**
         * Returns an array containing all of the elements in this set; the runtime type of the returned array is that
         * of the specified array. If the set fits in the specified array, it is returned therein. Otherwise, a new
         * array is allocated with the runtime type of the specified array and the size of this set.
         *
         * @param a the array into which the elements of this set are to be stored, if it is big enough; otherwise, a
         *          new array of the same runtime type is allocated for this purpose.
         *
         * @return an array containing all the elements in this set
         * @throws ArrayStoreException if the runtime type of the specified array is not a supertype of the runtime
         *                             type
         *                             of every element in this set
         */
        @Override
        public <T> T[] toArray(T[] a)
        {
            int size = size();
            T[] r = a == null || (a != null && a.length < size) ? (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(),
                    size
            ) : a;

            int nextIndex = 0;
            for (Node<K, V> current : buckets) {
                while (current != null) {
                    r[nextIndex++] = (T) current.key;
                    current = current.next;
                }
            }

            return r;
        }

        /**
         * Removes the specified element from this set if it is present More formally, removes an element <tt>e</tt>
         * such that <tt>(o==null ? e==null : o.equals(e))</tt>, if this set contains such an element.  Returns
         * <tt>true</tt> if this set contained the element (or equivalently, if this set changed as a result of the
         * call).  (This set will not contain the element once the call returns.)
         *
         * @param o object to be removed from this set, if present
         *
         * @return <tt>true</tt> if this set contained the specified element
         */
        @Override
        public boolean remove(Object o)
        {
            return removeNode(hash(o), o) != null;
        }

        /**
         * Returns <tt>true</tt> if this set contains all of the elements of the specified collection.  If the
         * specified
         * collection is also a set, this method returns <tt>true</tt> if it is a <i>subset</i> of this set.
         *
         * @param c collection to be checked for containment in this set
         *
         * @return <tt>true</tt> if this set contains all of the elements of the specified collection
         * @see #contains(Object)
         */
        @Override
        public boolean containsAll(Collection<?> c)
        {
            if (c == null || c.isEmpty()) {
                return true;
            }

            for (Object o : c)
                if (!contains(o))
                    return false;

            return true;
        }

        /**
         * Retains only the elements in this set that are contained in the specified collection. In other words,
         * removes
         * from this set all of its elements that are not contained in the specified collection . If the specified
         * collection is also a set, this operation effectively modifies this set so that its value is the
         * <i>intersection</i> of the two sets.
         *
         * @param c collection containing elements to be retained in this set
         *
         * @return <tt>true</tt> if this set changed as a result of the call
         * @see #remove(Object)
         */
        @Override
        public boolean retainAll(Collection<?> c)
        {
            if (c == null || c.isEmpty()) {
                clear();
                return true;
            }

            boolean changed = false;
            for (int x = 0; x < buckets.length; x++) {
                Node<K, V> current = buckets[x];
                Node<K, V> previous = null;
                while (current != null) {
                    if (!c.contains(current.key)) {
                        if (previous != null)
                            previous.setNext(current.next);
                        else
                            buckets[x] = null;
                        size--;
                        changed = true;
                    }

                    previous = current;
                    current = current.next;
                }
            }

            return changed;
        }

        /**
         * Removes from this set all of its elements that are contained in the specified collection (optional
         * operation).  If the specified collection is also a set, this operation effectively modifies this set so that
         * its value is the <i>asymmetric set difference</i> of the two sets.
         *
         * @param c collection containing elements to be removed from this set
         *
         * @return <tt>true</tt> if this set changed as a result of the call
         * @see #remove(Object)
         * @see #contains(Object)
         */
        @Override
        public boolean removeAll(Collection<?> c)
        {
            if (c == null || c.isEmpty()) {
                return false;
            }

            boolean changed = false;
            for (Object o : c) {
                if (remove(o)) {
                    changed = true;
                }
            }

            return changed;
        }

        /**
         * Removes all of the elements from this set. The set will be empty after this call returns.
         */
        @Override
        public void clear()
        {
            HashMap.this.clear();
        }

        /**
         * Unsupported operation.
         */
        @Override
        public boolean addAll(Collection<? extends K> c)
        {
            throw new UnsupportedOperationException();
        }

        /**
         * Unsupported operation.
         */
        @Override
        public boolean add(K k)
        {
            throw new UnsupportedOperationException();
        }
    }
}
