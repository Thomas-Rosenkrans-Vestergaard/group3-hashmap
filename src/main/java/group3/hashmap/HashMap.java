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
	 * Cached {@link Set<Map.Entry<K, V>>} that can be returned when the
	 * {@link HashMap#entrySet()} method is called.
	 */
	private Set<Map.Entry<K, V>> cachedNodeSet = null;

	/**
	 * Cached {@link Set<K>} that can be returned when the
	 * {@link HashMap#keySet()} method is called.
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
		this.loadFactor = loadFactor;
		this.buckets = (Node<K, V>[]) new Node[capacity];
		this.size = 0;
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
	 * Represents a key-value node in the {@link HashMap}.
	 *
	 * @param <K> The key type.
	 * @param <V> The value type.
	 */
	public static class Node<K, V> implements Map.Entry<K, V>
	{

		/**
		 * The hashCode of the key.
		 */
		private final int hash;

		/**
		 * The key of the key-value node.
		 */
		private final K key;

		/**
		 * The value of the key-value node.
		 */
		private V value;

		/**
		 * The next entry in this bucket.
		 */
		private Node<K, V> next;

		/**
		 * Creates a new key-value node.
		 *
		 * @param key   The key in the key-value node.
		 * @param value The value in the key-value node.
		 * @param next  The next entry in the bucket.
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
		 * @param key   The key in the key-value node.
		 * @param value The value in the key-value node.
		 * @param next  The next node in the bucket.
		 */
		public Node(K key, V value, Node<K, V> next)
		{
			this(key.hashCode(), key, value, next);
		}

		/**
		 * Creates a new key-value node.
		 *
		 * @param key   The key in the key-value node.
		 * @param value The value in the key-value node.
		 */
		public Node(K key, V value)
		{
			this(key.hashCode(), key, value, null);
		}

		/**
		 * Returns the key in the key-value node.
		 *
		 * @return The key in the key-value node.
		 */
		public K getKey()
		{
			return this.key;
		}

		/**
		 * Returns the value in the key-value node.
		 *
		 * @return The value in the key-value node.
		 */
		public V getValue()
		{
			return this.value;
		}

		/**
		 * Sets the value in the key-value node.
		 *
		 * @param value The new value to set.
		 *
		 * @return The value just replaced.
		 */
		public V setValue(V value)
		{
			V before = this.value;
			this.value = value;
			return before;
		}

		/**
		 * Sets the reference to the next key-value node in the bucket.
		 *
		 * @param next The reference to set.
		 */
		void setNext(Node<K, V> next)
		{
			this.next = next;
		}

		@Override public boolean equals(Object o)
		{
			if (this == o) return true;
			if (!(o instanceof Map.Entry)) return false;
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
			return Objects.equals(key, entry.getKey()) &&
				   Objects.equals(value, entry.getValue());
		}

		@Override public int hashCode()
		{
			return Objects.hash(hash, key, value);
		}
	}

	/**
	 * Returns the number of key-value mappings in this map.
	 *
	 * @return the number of key-value mappings in this map
	 */
	@Override public int size()
	{
		return size;
	}

	/**
	 * Returns <tt>true</tt> if this map contains no key-value mappings.
	 *
	 * @return <tt>true</tt> if this map contains no key-value mappings
	 */
	@Override public boolean isEmpty()
	{
		return size == 0;
	}

	/**
	 * Returns <tt>true</tt> if this map contains a mapping for the specified
	 * key.  More formally, returns <tt>true</tt> if and only if
	 * this map contains a mapping for a key <tt>k</tt> such that
	 * <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be
	 * at most one such mapping.)
	 *
	 * @param key key whose presence in this map is to be tested
	 *
	 * @return <tt>true</tt> if this map contains a mapping for the specified
	 * key
	 */
	@Override public boolean containsKey(Object key)
	{
		Node<K, V> node = findNode(hash(key), key);

		return node != null;
	}

	/**
	 * Returns <tt>true</tt> if this map maps one or more keys to the
	 * specified value.  More formally, returns <tt>true</tt> if and only if
	 * this map contains at least one mapping to a value <tt>v</tt> such that
	 * <tt>(value==null ? v==null : value.equals(v))</tt>.
	 *
	 * @param value value whose presence in this map is to be tested
	 *
	 * @return <tt>true</tt> if this map maps one or more keys to the
	 * specified value
	 */
	@Override public boolean containsValue(Object value)
	{
		for (Node<K, V> head : buckets) {
			Node<K, V> current = head;
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
	 *
	 * @return the value to which the specified key is mapped, or
	 * {@code null} if this map contains no mapping for the key
	 */
	@Override public V get(Object key)
	{
		Node<K, V> node = findNode(hash(key), key);

		return node == null ? null : node.value;
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
	 *
	 * @return the previous value associated with <tt>key</tt>, or
	 * <tt>null</tt> if there was no mapping for <tt>key</tt>.
	 * (A <tt>null</tt> return can also indicate that the map
	 * previously associated <tt>null</tt> with <tt>key</tt>,
	 * if the implementation supports <tt>null</tt> values.)
	 */
	@Override public V put(K key, V value)
	{
		return putNode(hash(key), key, value, null);
	}

	/**
	 * Removes the mapping for a key from this map if it is present.
	 * More formally, if this map contains a mapping
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
	 *
	 * @return the previous value associated with <tt>key</tt>, or
	 * <tt>null</tt> if there was no mapping for <tt>key</tt>.
	 */
	@Override
	public V remove(Object key)
	{
		Node<K, V> removed = removeNode(hash(key), key);

		return removed == null ? null : removed.value;
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
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		if (m != null) {
			for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
				K key   = entry.getKey();
				V value = entry.getValue();
				putNode(hash(key), key, value, null);
			}
		}
	}

	/**
	 * Removes all of the mappings from this map (optional operation).
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
	@Override
	public Collection<V> values()
	{
		Collection<V> result = new ArrayList<>();
		for (Node<K, V> head : buckets) {
			Node<K, V> current = head;
			while (current != null) {
				result.add(current.value);
				current = current.next;
			}
		}

		return result;
	}

	/**
	 * Returns the current capacity of the {@link HashMap}. The capacity is the number of buckets in the
	 * {@link HashMap}. The capacity will automatically increase depending on the <code>loadFactor</code> of the
	 * {@link HashMap}.
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
	 * Checks if the storage needs expanding if it had the provided number of <code>entries</code>. The storage
	 * needs to be expanded when the number of entries equals or exceeds the <code>storage.length(capacity) *
	 * loadFactor</code>.
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
	 *              <code>node.next</code> reference is always removed.
	 *
	 * @return The value that was replaced. Returns <code>null</code> if no value was replaced.
	 */
	private V putNode(int hash, K key, V value, Node<K, V> node)
	{
		int        bucketIndex = index(hash);
		Node<K, V> head        = buckets[bucketIndex];

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

		if (matches(hash, key, head.hash, head.key)) {
			V before = head.getValue();
			head.setValue(value);
			return before;
		}

		Node<K, V> previous = head;
		Node<K, V> current  = head.next;

		while (true) {

			if (current == null) {
				previous.setNext(node == null ? new Node<>(hash, key, value, null) : node);
				size++;
				if (needsExpansion(buckets, size))
					expand();
				return null;
			}

			if (matches(hash, key, current.hash, current.key)) {
				current.setValue(value);
				return current.value;
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
		int        bucketIndex = index(hash);
		Node<K, V> head        = buckets[bucketIndex];

		Node<K, V> previous = null;
		Node<K, V> current  = head;
		while (current != null) {
			if (matches(current, hash, key)) {
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
	 * Finds the node matching the provided hash and key.
	 *
	 * @param hash The hash to find the matching node from.
	 * @param key  The key to find the matching node from.
	 *
	 * @return The matching node. Returns null if no node could be found.
	 */
	private Node<K, V> findNode(int hash, Object key)
	{

		int        bucketIndex = index(hash);
		Node<K, V> current     = buckets[bucketIndex];
		while (current != null) {
			if (matches(hash, key, current.hash, current.key)) {
				return current;
			}

			current = current.next;
		}

		return null;
	}

	private Node<K, V> nodeFromEntry(Object o)
	{
		if (!(o instanceof Map.Entry)) {
			return null;
		}

		Map.Entry e   = (Map.Entry) o;
		Object    key = e.getKey();
		return findNode(hash(key), key, e.getValue());
	}

	/**
	 * Finds the node matching the provided hash and key.
	 *
	 * @param hash  The hash to find the matching node from.
	 * @param key   The key to find the matching node from.
	 * @param value An optional value that the matching <code>node</code> must also match.
	 *
	 * @return The matching node. Returns null if no node could be found.
	 */
	private Node<K, V> findNode(int hash, Object key, Object value)
	{
		int        bucketIndex = index(hash);
		Node<K, V> current     = buckets[bucketIndex];

		while (current != null) {
			if (matches(hash, key, current.hash, current.key) && (value == null ? value == current.value : value
					.equals(current.value))) {
				return current;
			}

			current = current.next;
		}

		return null;
	}

	/**
	 * Returns the bucket index assigned to the provided hash.
	 *
	 * @param hash The hash that the bucket index should be placed in.
	 *
	 * @return The bucket index.
	 */
	private int index(int hash)
	{
		return Math.abs(hash) % buckets.length;
	}

	/**
	 * Expands the {@link HashMap} by doubling the <code>capacity</code> and expanding the entries.
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
	 * Checks if the provided <code>node</code> matches with the provided <code>hash</code> and <code>key</code>. The
	 * variables match when
	 *
	 * @param node The <code>node</code> to match the <code>hash</code> and <code>key</code> to.
	 * @param hash The <code>hash</code> the <code>node</code> must match.
	 * @param key  The <code>key</code> the <code>node</code> must match.
	 *
	 * @return <code>true</code> if the provided <code>node</code> matches with the provided <code>hash</code> and
	 * <code>key</code>.
	 */
	private boolean matches(Node<K, V> node, int hash, Object key)
	{
		return node.key == key && node.hash == hash;
	}

	/**
	 * Checks if the provided <code>hashA</code> and <code>keyA</code> matches the provided <code>hashB</code> and
	 * <code>keyB</code>. The arguments match when <code>keyA == null ? keyA == keyB : keyA.equals(keyB)</code>.
	 *
	 * @param hashA The first hash.
	 * @param keyA  The first key.
	 * @param hashB The second hash.
	 * @param keyB  The second key.
	 *
	 * @return True when the arguments match.
	 */
	private boolean matches(int hashA, Object keyA, int hashB, Object keyB)
	{
		return hashA == hashB && (keyA == null ? keyA == keyB : keyA.equals(keyB));
	}

	/**
	 * Hashes the provided key.
	 *
	 * @param key The key to hash.
	 *
	 * @return The resulting hash.
	 */
	private int hash(Object key)
	{
		return key == null ? 0 : key.hashCode();
	}

	/**
	 * Returns a {@link Set} view of the mappings contained in this map.
	 * The set is backed by the map, so changes to the map are
	 * reflected in the set, and vice-versa.  If the map is modified
	 * while an iteration over the set is in progress (except through
	 * the iterator's own <tt>remove</tt> operation, or through the
	 * <tt>setValue</tt> operation on a map entry returned by the
	 * iterator) the results of the iteration are undefined.  The set
	 * supports element removal, which removes the corresponding
	 * mapping from the map, via the <tt>Iterator.remove</tt>,
	 * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
	 * <tt>clear</tt> operations.  It does not support the
	 * <tt>add</tt> or <tt>addAll</tt> operations.
	 *
	 * @return a set view of the mappings contained in this map
	 */
	@Override public Set<Map.Entry<K, V>> entrySet()
	{
		if (cachedNodeSet == null)
			cachedNodeSet = new NodeSet();

		return cachedNodeSet;
	}

	/**
	 * {@link HashMap} backed {@link Set} allowing for {@link Set} operations on the {@link HashMap}.
	 */
	private class NodeSet implements Set<Map.Entry<K, V>>
	{

		/**
		 * Returns the number of elements in this set (its cardinality).  If this
		 * set contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
		 * <tt>Integer.MAX_VALUE</tt>.
		 *
		 * @return the number of elements in this set (its cardinality)
		 */
		@Override public int size()
		{
			return HashMap.this.size();
		}

		/**
		 * Returns <tt>true</tt> if this set contains no elements.
		 *
		 * @return <tt>true</tt> if this set contains no elements
		 */
		@Override public boolean isEmpty()
		{
			return HashMap.this.size == 0;
		}

		/**
		 * Returns <tt>true</tt> if this set contains the specified element.
		 * More formally, returns <tt>true</tt> if and only if this set
		 * contains an element <tt>e</tt> such that
		 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
		 *
		 * @param o element whose presence in this set is to be tested
		 *
		 * @return <tt>true</tt> if this set contains the specified element
		 */
		@Override public boolean contains(Object o)
		{
			Node<K, V> found = nodeFromEntry(o);

			return found != null;
		}

		/**
		 * Returns an iterator over the elements in this set.  The elements are
		 * returned in no particular order.
		 *
		 * @return an iterator over the elements in this set
		 */
		@Override public NodeIterator iterator()
		{
			return new NodeIterator();
		}

		/**
		 * An implementation of the {@link Iterator} interface that enables iteration of the {@link NodeSet} with
		 * minimal memory overhead.
		 */
		private class NodeIterator implements Iterator<Map.Entry<K, V>>
		{

			/**
			 * The index of the bucket where the last returned entry is from.
			 */
			private int currentBucket;

			/**
			 * The previously returned entry. The {@link NodeIterator#hasNext()} method only returns
			 * <code>true</code> when this field is not null. The next entry is found before returning the current
			 * entry from the {@link NodeIterator#next()} method.
			 */
			private Node<K, V> nextEntry;

			/**
			 * Creates a new {@link NodeIterator}.
			 */
			NodeIterator()
			{
				for (int x = 0; x < buckets.length; x++) {
					if (buckets[x] != null) {
						nextEntry = buckets[x];
						currentBucket = x;
						break;
					}
				}
			}

			/**
			 * Returns {@code true} if the iteration has more elements.
			 * (In other words, returns {@code true} if {@link #next} would
			 * return an element rather than throwing an exception.)
			 *
			 * @return {@code true} if the iteration has more elements
			 */
			@Override public boolean hasNext()
			{
				return nextEntry != null;
			}

			/**
			 * Returns the next element in the iteration.
			 *
			 * @return the next element in the iteration
			 * @throws NoSuchElementException if the iteration has no more elements
			 */
			@Override public Node<K, V> next()
			{
				if (nextEntry == null)
					throw new NoSuchElementException();

				Node<K, V> result = nextEntry;
				nextEntry = getNextEntry(result);
				return result;
			}

			/**
			 * Finds the next available entry from the current element. Returns either <code>current.next</code> or
			 * the head of the next bucket.
			 *
			 * @param current The last returned entry.
			 *
			 * @return Returns the next entry. Returns <code>null</code> if there are no next entries.
			 */
			private Node<K, V> getNextEntry(Node<K, V> current)
			{
				if (current.next == null)
					return getNextHead();

				return current.next;
			}

			/**
			 * Returns the head of the next bucket.
			 *
			 * @return The head of the next bucket. Returns <code>null</code> if there are no more heads.
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
		 * Returns an array containing all of the elements in this set. The resulting array can be modified without
		 * affecting the set.
		 *
		 * @return the array containing all the elements in this set
		 */
		@Override public Node<K, V>[] toArray()
		{
			int          nextIndex = 0;
			Node<K, V>[] result    = (Node<K, V>[]) new Node[size];
			for (Node<K, V> current : buckets) {
				while (current != null) {
					result[nextIndex++] = current;
					current = current.next;
				}
			}

			return result;
		}

		/**
		 * Returns an array containing all of the elements in this set; the
		 * runtime type of the returned array is that of the specified array.
		 * If the set fits in the specified array, it is returned therein.
		 * Otherwise, a new array is allocated with the runtime type of the
		 * specified array and the size of this set.
		 * <p>
		 * <p>If this set fits in the specified array with room to spare
		 * (i.e., the array has more elements than this set), the element in
		 * the array immediately following the end of the set is set to
		 * <tt>null</tt>.  (This is useful in determining the length of this
		 * set <i>only</i> if the caller knows that this set does not contain
		 * any null elements.)
		 * <p>
		 * <p>If this set makes any guarantees as to what order its elements
		 * are returned by its iterator, this method must return the elements
		 * in the same order.
		 * <p>
		 * This method allows precise control over the runtime type of the output array.
		 * <p>
		 * <p>Suppose <tt>x</tt> is a set known to contain only strings.
		 * The following code can be used to dump the set into a newly allocated
		 * array of <tt>String</tt>:
		 * <p>
		 * <pre>
		 *     String[] y = x.toArray(new String[0]);</pre>
		 * <p>
		 *
		 * @param a the array into which the elements of this set are to be
		 *          stored, if it is big enough; otherwise, a new array of the same
		 *          runtime type is allocated for this purpose.
		 *
		 * @return an array containing all the elements in this set
		 * @throws ArrayStoreException if the runtime type of the specified array
		 *                             is not a supertype of the runtime type of every element in this
		 *                             set
		 */
		@Override public <T> T[] toArray(T[] a)
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
		 * Adds the provided entry to the {@link NodeSet} when it does not already exist in the set. The entry is
		 * only added when a {@link Node} with the an equal key and value doesn't exist in the set. Null values
		 * cannot be allowed.
		 *
		 * @param entry element to be added to this set
		 *
		 * @return <tt>true</tt> if this set did not already contain the specified
		 * element
		 */
		@Override public boolean add(Map.Entry<K, V> entry)
		{
			if (entry == null || nodeFromEntry(entry) == null)
				return false;

			K key = entry.getKey();
			putNode(key.hashCode(), key, entry.getValue(), null);
			return true;
		}

		/**
		 * Removes the specified element from this set if it is present
		 * (optional operation).  More formally, removes an element <tt>e</tt>
		 * such that
		 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>, if
		 * this set contains such an element.  Returns <tt>true</tt> if this set
		 * contained the element (or equivalently, if this set changed as a
		 * result of the call).  (This set will not contain the element once the
		 * call returns.)
		 *
		 * @param o object to be removed from this set, if present
		 *
		 * @return <tt>true</tt> if this set contained the specified element
		 */
		@Override public boolean remove(Object o)
		{
			if (!(o instanceof Map.Entry))
				return false;

			Map.Entry<K, V> e = (Map.Entry<K, V>) o;

			for (int x = 0; x < buckets.length; x++) {
				Node<K, V> current  = buckets[x];
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
		 * Returns <tt>true</tt> if this set contains all of the elements of the
		 * specified collection.  If the specified collection is also a set, this
		 * method returns <tt>true</tt> if it is a <i>subset</i> of this set.
		 *
		 * @param c collection to be checked for containment in this set
		 *
		 * @return <tt>true</tt> if this set contains all of the elements of the
		 * specified collection
		 * @see #contains(Object)
		 */
		@Override public boolean containsAll(Collection<?> c)
		{
			if (c != null) {
				for (int x = 0; x < buckets.length; x++) {
					Node<K, V> current = buckets[x];
					while (current != null) {
						if (!c.contains(current)) {
							return false;
						}

						current = current.next;
					}
				}
			}

			return true;
		}

		/**
		 * Adds all of the elements in the specified collection to this set if
		 * they're not already present (optional operation).  If the specified
		 * collection is also a set, the <tt>addAll</tt> operation effectively
		 * modifies this set so that its value is the <i>union</i> of the two
		 * sets.  The behavior of this operation is undefined if the specified
		 * collection is modified while the operation is in progress.
		 *
		 * @param c collection containing elements to be added to this set
		 *
		 * @return <tt>true</tt> if this set changed as a result of the call
		 * @see #add(Object)
		 */
		@Override public boolean addAll(Collection<? extends Map.Entry<K, V>> c)
		{
			boolean changed = false;
			if (c != null) {
				for (Map.Entry<K, V> entry : c) {
					if (add(entry)) {
						changed = true;
					}
				}
			}

			return changed;
		}

		/**
		 * Retains only the elements in this set that are contained in the
		 * specified collection (optional operation).  In other words, removes
		 * from this set all of its elements that are not contained in the
		 * specified collection.  If the specified collection is also a set, this
		 * operation effectively modifies this set so that its value is the
		 * <i>intersection</i> of the two sets.
		 *
		 * @param c collection containing elements to be retained in this set
		 *
		 * @return <tt>true</tt> if this set changed as a result of the call
		 * @see #remove(Object)
		 */
		@Override public boolean retainAll(Collection<?> c)
		{
			if (c == null || c.isEmpty()) {
				clear();
				return true;
			}

			boolean changed = false;
			for (int x = 0; x < buckets.length; x++) {
				Node<K, V> current  = buckets[x];
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
		 * Removes from this set all of its elements that are contained in the
		 * specified collection (optional operation).  If the specified
		 * collection is also a set, this operation effectively modifies this
		 * set so that its value is the <i>asymmetric set difference</i> of
		 * the two sets.
		 *
		 * @param c collection containing elements to be removed from this set
		 *
		 * @return <tt>true</tt> if this set changed as a result of the call
		 * @see #remove(Object)
		 * @see #contains(Object)
		 */
		@Override public boolean removeAll(Collection<?> c)
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
		 * Removes all of the elements from this set (optional operation).
		 * The set will be empty after this call returns.
		 */
		@Override public void clear()
		{
			HashMap.this.clear();
		}
	}

	/**
	 * Returns a {@link Set} view of the keys contained in this map.
	 * The set is backed by the map, so changes to the map are
	 * reflected in the set, and vice-versa.  If the map is modified
	 * while an iteration over the set is in progress (except through
	 * the iterator's own <tt>remove</tt> operation), the results of
	 * the iteration are undefined.  The set supports element removal,
	 * which removes the corresponding mapping from the map, via the
	 * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
	 * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt>
	 * operations.  It does not support the <tt>add</tt> or <tt>addAll</tt>
	 * operations.
	 *
	 * @return a set view of the keys contained in this map
	 */
	@Override public Set<K> keySet()
	{
		if (cachedKeySet == null)
			cachedKeySet = new KeySet();

		return cachedKeySet;
	}

	/**
	 * {@link HashMap} backed {@link Set} allowing for {@link Set} operations on the keys in the {@link HashMap}.
	 */
	private class KeySet implements Set<K>
	{
		/**
		 * Returns the number of elements in this set (its cardinality).  If this
		 * set contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
		 * <tt>Integer.MAX_VALUE</tt>.
		 *
		 * @return the number of elements in this set (its cardinality)
		 */
		@Override public int size()
		{
			return HashMap.this.size;
		}

		/**
		 * Returns <tt>true</tt> if this set contains no elements.
		 *
		 * @return <tt>true</tt> if this set contains no elements
		 */
		@Override public boolean isEmpty()
		{
			return HashMap.this.size == 0;
		}

		/**
		 * Returns <tt>true</tt> if this set contains the specified element.
		 * More formally, returns <tt>true</tt> if and only if this set
		 * contains an element <tt>e</tt> such that
		 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
		 *
		 * @param o element whose presence in this set is to be tested
		 *
		 * @return <tt>true</tt> if this set contains the specified element
		 */
		@Override public boolean contains(Object o)
		{
			return findNode(hash(o), o) != null;
		}

		/**
		 * Returns an iterator over the elements in this set.  The elements are
		 * returned in no particular order (unless this set is an instance of some
		 * class that provides a guarantee).
		 *
		 * @return an iterator over the elements in this set
		 */
		@Override public Iterator<K> iterator()
		{
			return new KeyIterator();
		}

		/**
		 * Implementation of {@link Iterator} allowing iteration over the keys in the nodes in the {@link HashMap}
		 * with minimal overhead.
		 */
		private class KeyIterator implements Iterator<K>
		{

			/**
			 * The index of the bucket where the <code>nextEntry</code> is from.
			 */
			private int currentBucket;

			/**
			 * The previously returned entry. The {@link KeySet.KeyIterator#hasNext()} method only returns
			 * <code>true</code> when this field is not null. The next entry is found before returning the current
			 * entry from the {@link KeySet.KeyIterator#next()} method.
			 */
			private Node<K, V> nextEntry;

			/**
			 * Creates a new {@link NodeSet.NodeIterator}.
			 */
			KeyIterator()
			{
				for (int x = 0; x < buckets.length; x++) {
					if (buckets[x] != null) {
						nextEntry = buckets[x];
						currentBucket = x;
						break;
					}
				}
			}

			/**
			 * Returns {@code true} if the iteration has more keys.
			 * (In other words, returns {@code true} if {@link #next} would
			 * return an key rather than throwing an exception.)
			 *
			 * @return {@code true} if the iteration has more keys
			 */
			@Override public boolean hasNext()
			{
				return nextEntry != null;
			}

			/**
			 * Returns the next key in the iteration.
			 *
			 * @return the next key in the iteration
			 * @throws NoSuchElementException if the iteration has no more keys.
			 */
			@Override public K next()
			{
				if (nextEntry == null)
					throw new NoSuchElementException();

				Node<K, V> result = nextEntry;
				nextEntry = getNextEntry(result);
				return result.key;
			}

			/**
			 * Finds the next available entry from the current element. Returns either <code>current.next</code> or
			 * the head of the next bucket.
			 *
			 * @param current The last returned entry.
			 *
			 * @return Returns the next entry. Returns <code>null</code> if there are no next entries.
			 */
			private Node<K, V> getNextEntry(Node<K, V> current)
			{
				if (current.next == null)
					return getNextHead();

				return current.next;
			}

			/**
			 * Returns the head of the next bucket.
			 *
			 * @return The head of the next bucket. Returns <code>null</code> if there are no more heads.
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
		 * Returns an array containing all of the elements in this set.
		 * If this set makes any guarantees as to what order its elements
		 * are returned by its iterator, this method must return the
		 * elements in the same order.
		 * <p>
		 * <p>The returned array will be "safe" in that no references to it
		 * are maintained by this set.  (In other words, this method must
		 * allocate a new array even if this set is backed by an array).
		 * The caller is thus free to modify the returned array.
		 * <p>
		 * <p>This method acts as bridge between array-based and collection-based
		 * APIs.
		 *
		 * @return an array containing all the elements in this set
		 */
		@Override public Object[] toArray()
		{
			int      nextIndex = 0;
			Object[] result    = new Object[size];
			for (Node<K, V> current : buckets) {
				while (current != null) {
					result[nextIndex++] = current.key;
					current = current.next;
				}
			}

			return result;
		}

		/**
		 * Returns an array containing all of the elements in this set; the
		 * runtime type of the returned array is that of the specified array.
		 * If the set fits in the specified array, it is returned therein.
		 * Otherwise, a new array is allocated with the runtime type of the
		 * specified array and the size of this set.
		 * <p>
		 * <p>If this set fits in the specified array with room to spare
		 * (i.e., the array has more elements than this set), the element in
		 * the array immediately following the end of the set is set to
		 * <tt>null</tt>.  (This is useful in determining the length of this
		 * set <i>only</i> if the caller knows that this set does not contain
		 * any null elements.)
		 * <p>
		 * <p>If this set makes any guarantees as to what order its elements
		 * are returned by its iterator, this method must return the elements
		 * in the same order.
		 * <p>
		 * <p>Like the {@link #toArray()} method, this method acts as bridge between
		 * array-based and collection-based APIs.  Further, this method allows
		 * precise control over the runtime type of the output array, and may,
		 * under certain circumstances, be used to save allocation costs.
		 * <p>
		 * <p>Suppose <tt>x</tt> is a set known to contain only strings.
		 * The following code can be used to dump the set into a newly allocated
		 * array of <tt>String</tt>:
		 * <p>
		 * <pre>
		 *     String[] y = x.toArray(new String[0]);</pre>
		 * <p>
		 * Note that <tt>toArray(new Object[0])</tt> is identical in function to
		 * <tt>toArray()</tt>.
		 *
		 * @param a the array into which the elements of this set are to be
		 *          stored, if it is big enough; otherwise, a new array of the same
		 *          runtime type is allocated for this purpose.
		 *
		 * @return an array containing all the elements in this set
		 * @throws ArrayStoreException if the runtime type of the specified array
		 *                             is not a supertype of the runtime type of every element in this
		 *                             set
		 */
		@Override public <T> T[] toArray(T[] a)
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
		 * Removes the specified element from this set if it is present
		 * (optional operation).  More formally, removes an element <tt>e</tt>
		 * such that
		 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>, if
		 * this set contains such an element.  Returns <tt>true</tt> if this set
		 * contained the element (or equivalently, if this set changed as a
		 * result of the call).  (This set will not contain the element once the
		 * call returns.)
		 *
		 * @param o object to be removed from this set, if present
		 *
		 * @return <tt>true</tt> if this set contained the specified element
		 */
		@Override public boolean remove(Object o)
		{
			return removeNode(hash(o), o) != null;
		}

		/**
		 * Returns <tt>true</tt> if this set contains all of the elements of the
		 * specified collection.  If the specified collection is also a set, this
		 * method returns <tt>true</tt> if it is a <i>subset</i> of this set.
		 *
		 * @param c collection to be checked for containment in this set
		 *
		 * @return <tt>true</tt> if this set contains all of the elements of the
		 * specified collection
		 * @see #contains(Object)
		 */
		@Override public boolean containsAll(Collection<?> c)
		{
			if (c == null || c.isEmpty()) {
				return true;
			}

			for (Node<K, V> bucket : buckets) {
				Node<K, V> current = bucket;
				while (current != null) {
					if (!c.contains(current.key)) {
						return false;
					}

					current = current.next;
				}
			}

			return true;
		}

		/**
		 * Retains only the elements in this set that are contained in the
		 * specified collection (optional operation).  In other words, removes
		 * from this set all of its elements that are not contained in the
		 * specified collection.  If the specified collection is also a set, this
		 * operation effectively modifies this set so that its value is the
		 * <i>intersection</i> of the two sets.
		 *
		 * @param c collection containing elements to be retained in this set
		 *
		 * @return <tt>true</tt> if this set changed as a result of the call
		 * @see #remove(Object)
		 */
		@Override public boolean retainAll(Collection<?> c)
		{
			if (c == null || c.isEmpty()) {
				clear();
				return true;
			}

			boolean changed = false;
			for (int x = 0; x < buckets.length; x++) {
				Node<K, V> current  = buckets[x];
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
		 * Removes from this set all of its elements that are contained in the
		 * specified collection (optional operation).  If the specified
		 * collection is also a set, this operation effectively modifies this
		 * set so that its value is the <i>asymmetric set difference</i> of
		 * the two sets.
		 *
		 * @param c collection containing elements to be removed from this set
		 *
		 * @return <tt>true</tt> if this set changed as a result of the call
		 * @see #remove(Object)
		 * @see #contains(Object)
		 */
		@Override public boolean removeAll(Collection<?> c)
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
		 * Removes all of the elements from this set (optional operation).
		 * The set will be empty after this call returns.
		 */
		@Override public void clear()
		{
			HashMap.this.clear();
		}

		/**
		 * Unsupported operation.
		 */
		@Override public boolean addAll(Collection<? extends K> c)
		{
			throw new UnsupportedOperationException();
		}

		/**
		 * Unsupported operation.
		 */
		@Override public boolean add(K k)
		{
			throw new UnsupportedOperationException();
		}
	}
}
