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
	private Pair<K, V>[] buckets;

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
		this.buckets = (Pair<K, V>[]) new Pair[capacity];
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
	 * Represents a key-value pair in the {@link HashMap}.
	 *
	 * @param <K> The key type.
	 * @param <V> The value type.
	 */
	public static class Pair<K, V> implements Map.Entry<K, V>
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
		private Pair<K, V> next;

		/**
		 * Creates a new key-value pair.
		 *
		 * @param key   The key in the key-value pair.
		 * @param value The value in the key-value pair.
		 * @param next  The next entry in the bucket.
		 */
		public Pair(int hash, K key, V value, Pair<K, V> next)
		{
			this.hash = hash;
			this.key = key;
			this.value = value;
			this.next = next;
		}

		/**
		 * Creates a new key-value pair.
		 *
		 * @param key   The key in the key-value pair.
		 * @param value The value in the key-value pair.
		 * @param next  The next pair in the bucket.
		 */
		public Pair(K key, V value, Pair<K, V> next)
		{
			this(key.hashCode(), key, value, next);
		}

		/**
		 * Creates a new key-value pair.
		 *
		 * @param key   The key in the key-value pair.
		 * @param value The value in the key-value pair.
		 */
		public Pair(K key, V value)
		{
			this(key.hashCode(), key, value, null);
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
		public void setNext(Pair next)
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
	 * @return <tt>true</tt> if this map contains a mapping for the specified
	 * key
	 * @throws NullPointerException When the key is <code>null</code>.
	 */
	@Override public boolean containsKey(Object key)
	{
		if (key == null)
			throw new NullPointerException("Keys cannot be null.");

		int        hashCode    = key.hashCode();
		int        bucketIndex = hashCode % buckets.length;
		Pair<K, V> current     = buckets[bucketIndex];

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
		for (Pair<K, V> head : buckets) {
			Pair<K, V> current = head;
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

		int        hashCode    = key.hashCode();
		int        bucketIndex = hashCode % buckets.length;
		Pair<K, V> current     = buckets[bucketIndex];

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

		int hashCode = key.hashCode();

		V result = placeInStorage(hashCode, key, value, null);

		return result;
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
	@Override
	public V remove(Object key)
	{
		if (key == null)
			throw new NullPointerException("Keys cannot be null");

		int        hashCode    = key.hashCode();
		int        bucketIndex = hashCode % buckets.length;
		Pair<K, V> head        = buckets[bucketIndex];

		Pair<K, V> previous = null;
		Pair<K, V> current  = head;

		while (current != null) {

			if (current.hash == hashCode && current.key.equals(key)) {
				V before = current.getValue();
				if (previous != null)
					previous.setNext(current.next);
				else
					buckets[bucketIndex] = current.next;
				size--;
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
	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		if (m == null)
			throw new NullPointerException();

		if (needsExpansion(buckets, m.size() + size)) {
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
	@Override
	public void clear()
	{
		this.size = 0;
		this.buckets = (Pair<K, V>[]) new Pair[DEFAULT_CAPACITY];
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
		for (Pair<K, V> head : buckets) {
			Pair<K, V> current = head;
			while (current != null) {
				result.add(current.value);
				current = current.next;
			}
		}

		return result;
	}

	/**
	 * Checks if the storage needs expanding if it had the provided number of <code>entries</code>. The storage
	 * needs to be expanded when the number of entries equals or exceeds the <code>storage.length(capacity) *
	 * loadFactor</code>.
	 *
	 * @param storage The storage.
	 * @param entries The number of entries to use, when checking whether or not the storage needs to be expanded.
	 * @return Whether or not the storage needs an expansion.
	 */

	private boolean needsExpansion(Pair<K, V>[] storage, int entries)
	{
		return entries >= storage.length * loadFactor;
	}

	/**
	 * Places an pair using the provided <code>hashCode</code>, <code>key</code>, <code>value</code> and
	 * <code>pair</code> into a bucket in <code>this.buckets</code>. The internal storage of the {@link HashMap}
	 * might then be expanded.
	 *
	 * @param hashCode The hashCode of the pair.
	 * @param key      The key of the pair.
	 * @param value    The value of the pair.
	 * @param pair     An existing {@link Pair} instance. Can be provided so the method doesn't need to create a new
	 *                 instance. A new instance will only be created if this argument is <code>null</code>.
	 * @return The value that was replaced. Returns <code>null</code> if no value was replaced.
	 */
	public V placeInStorage(int hashCode, K key, V value, Pair<K, V> pair)
	{
		V replace = place(buckets, hashCode, key, value, pair);
		if (needsExpansion(buckets, size))
			expand();

		return replace;
	}

	/**
	 * Places an pair using the provided <code>hashCode</code>, <code>key</code>, <code>value</code> and
	 * <code>pair</code> into a bucket in the provided <code>storage</code>. The storage cannot be expanded by this
	 * method.
	 *
	 * @param storage  The storage where the buckets are stored.
	 * @param hashCode The hashCode of the pair.
	 * @param key      The key of the pair.
	 * @param value    The value of the pair.
	 * @param pair     An existing {@link Pair} instance. Can be provided so the method doesn't need to create a new
	 *                 instance. A new instance will only be created if this argument is <code>null</code>.
	 * @return The value that was replaced. Returns <code>null</code> if no value was replaced.
	 */
	private V place(Pair<K, V>[] storage, int hashCode, K key, V value, Pair<K, V> pair)
	{
		int        bucketIndex = hashCode % storage.length;
		Pair<K, V> head        = storage[bucketIndex];

		if (head == null) {
			storage[bucketIndex] = pair == null ? new Pair<>(hashCode, key, value, null) : pair;
			size++;
			return null;
		}

		if (head.hash == hashCode && head.key.equals(key)) {
			V before = head.getValue();
			head.setValue(value);
			return before;
		}

		Pair<K, V> previous = head;
		Pair<K, V> current  = head.next;

		while (true) {

			if (current == null) {
				previous.setNext(pair == null ? new Pair<>(hashCode, key, value, null) : pair);
				size++;
				return null;
			}

			if (current.hash == hashCode && current.key.equals(key)) {
				current.setValue(value);
				return current.value;
			}

			previous = current;
			current = current.next;
		}
	}

	/**
	 * Expands the {@link HashMap} by doubling the <code>capacity</code> and expanding the entries.
	 */
	private void expand()
	{
		int count = 0;

		Pair<K, V>[] newArray = (Pair<K, V>[]) new Pair[buckets.length * 2];

		for (Pair<K, V> head : buckets) {
			if (count == size)
				break;
			Pair<K, V> current = head;
			while (current != null) {
				place(newArray, current.hash, current.key, current.value, current);
				count++;
				current = current.next;
			}
		}

		this.buckets = newArray;
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
	 * Unsupported operation.
	 */
	@Override public PairSet entrySet()
	{
		return new PairSet();
	}

	public class PairSet implements Set<Map.Entry<K, V>>
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
			return HashMap.this.isEmpty();
		}

		/**
		 * Returns <tt>true</tt> if this set contains the specified element.
		 * More formally, returns <tt>true</tt> if and only if this set
		 * contains an element <tt>e</tt> such that
		 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
		 *
		 * @param o element whose presence in this set is to be tested
		 * @return <tt>true</tt> if this set contains the specified element
		 * @throws NullPointerException When the provided object is null.
		 */
		@Override public boolean contains(Object o)
		{
			if (o == null)
				throw new NullPointerException("Argument given to contains cannot be null.");

			for (Pair<K, V> current : buckets) {
				while (current != null) {
					if (current.equals(o))
						return true;

					current = current.next;
				}
			}

			return false;
		}

		/**
		 * Returns an iterator over the elements in this set.  The elements are
		 * returned in no particular order (unless this set is an instance of some
		 * class that provides a guarantee).
		 *
		 * @return an iterator over the elements in this set
		 */
		@Override public PairIterator iterator()
		{
			return new PairIterator();
		}

		/**
		 * An implementation of the {@link Iterator} interface that enables iteration of the {@link PairSet} with
		 * minimal memory overhead.
		 */
		public class PairIterator implements Iterator<Map.Entry<K, V>>
		{

			/**
			 * The index of the bucket where the last returned entry is from.
			 */
			private int currentBucket;

			/**
			 * The previously returned entry. The {@link PairIterator#hasNext()} method only returns
			 * <code>true</code> when this field is not null. The next entry is found before returning the current
			 * entry from the {@link PairIterator#next()} method.
			 */
			private Pair<K, V> nextEntry;

			/**
			 * Creates a new {@link PairIterator}.
			 */
			public PairIterator()
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
			@Override public Pair<K, V> next()
			{
				if (nextEntry == null)
					throw new NoSuchElementException();

				Pair<K, V> result = nextEntry;
				nextEntry = getNextEntry(result);
				return result;
			}

			/**
			 * Finds the next available entry from the current element. Returns either <code>current.next</code> or
			 * the head of the next bucket.
			 *
			 * @param current The last returned entry.
			 * @return Returns the next entry. Returns <code>null</code> if there are no next entries.
			 */
			private Pair<K, V> getNextEntry(Pair<K, V> current)
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
			private Pair<K, V> getNextHead()
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
		@Override public Pair<K, V>[] toArray()
		{
			int          nextIndex = 0;
			Pair<K, V>[] result    = (Pair<K, V>[]) new Pair[size];
			for (Pair<K, V> current : buckets) {
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
		 * @return an array containing all the elements in this set
		 * @throws ArrayStoreException  if the runtime type of the specified array
		 *                              is not a supertype of the runtime type of every element in this
		 *                              set
		 * @throws NullPointerException if the specified array is null
		 */
		@Override public <T> T[] toArray(T[] a)
		{
			if (a == null)
				throw new NullPointerException("Array cannot be null.");

			int size = size();
			T[] r = a.length >= size ? a :
					(T[]) java.lang.reflect.Array
							.newInstance(a.getClass().getComponentType(), size);
			PairIterator it = iterator();

			int nextIndex = 0;
			while (it.hasNext()) {
				r[nextIndex] = (T) it.next();
				nextIndex++;
			}

			return r;
		}

		/**
		 * Adds the specified element to this set if it is not already present
		 * (optional operation).  More formally, adds the specified element
		 * <tt>e</tt> to this set if the set contains no element <tt>e2</tt>
		 * such that
		 * <tt>(e==null&nbsp;?&nbsp;e2==null&nbsp;:&nbsp;e.equals(e2))</tt>.
		 * If this set already contains the element, the call leaves the set
		 * unchanged and returns <tt>false</tt>.  In combination with the
		 * restriction on constructors, this ensures that sets never contain
		 * duplicate elements.
		 * <p>
		 * <p>The stipulation above does not imply that sets must accept all
		 * elements; sets may refuse to add any particular element, including
		 * <tt>null</tt>, and throw an exception, as described in the
		 * specification for {@link Collection#add Collection.add}.
		 * Individual set implementations should clearly document any
		 * restrictions on the elements that they may contain.
		 *
		 * @param entry element to be added to this set
		 * @return <tt>true</tt> if this set did not already contain the specified
		 * element
		 * @throws NullPointerException When the provided <code>entry</code> is null.
		 */
		@Override public boolean add(Map.Entry<K, V> entry)
		{
			if (entry == null)
				throw new NullPointerException("Pair cannot be null.");

			for (Pair<K, V> current : buckets) {
				while (current != null) {
					if (current.equals(entry))
						return false;

					current = current.next;
				}
			}

			placeInStorage(entry.getKey().hashCode(), entry.getKey(), entry.getValue(), null);
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
		 * @return <tt>true</tt> if this set contained the specified element
		 * @throws NullPointerException When the provided object is null.
		 */
		@Override public boolean remove(Object o)
		{
			if (o == null)
				throw new NullPointerException("Pair cannot be null.");

			for (int x = 0; x < buckets.length; x++) {
				Pair<K, V> current  = buckets[x];
				Pair<K, V> previous = null;
				while (current != null) {
					if (current.equals(o)) {
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
		 * @return <tt>true</tt> if this set contains all of the elements of the
		 * specified collection
		 * @throws NullPointerException When encountering a null element in the provided collection.
		 * @see #contains(Object)
		 */
		@Override public boolean containsAll(Collection<?> c)
		{
			if (c == null)
				throw new NullPointerException();

			for (int x = 0; x < buckets.length; x++) {
				Pair<K, V> current = buckets[x];
				while (current != null) {
					if (!c.contains(current)) {
						return false;
					}

					current = current.next;
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
		 * @return <tt>true</tt> if this set changed as a result of the call
		 * @throws NullPointerException if the specified collection contains one
		 *                              or more null elements and this set does not permit null
		 *                              elements, or if the specified collection is null
		 * @see #add(Object)
		 */
		@Override public boolean addAll(Collection<? extends Map.Entry<K, V>> c)
		{
			boolean changed = false;

			for (Map.Entry<K, V> entry : c) {
				if (add(entry)) {
					changed = true;
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
		 * @return <tt>true</tt> if this set changed as a result of the call
		 * @throws NullPointerException If the collection c is
		 *                              <code>null</code>.
		 * @see #remove(Object)
		 */
		@Override public boolean retainAll(Collection<?> c)
		{
			if (c == null)
				throw new NullPointerException();

			boolean changed = false;

			for (int x = 0; x < buckets.length; x++) {
				Pair<K, V> current  = buckets[x];
				Pair<K, V> previous = null;
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
		 * @return <tt>true</tt> if this set changed as a result of the call
		 * @throws NullPointerException When the provided collection
		 *                              c is null.
		 * @see #remove(Object)
		 * @see #contains(Object)
		 */
		@Override public boolean removeAll(Collection<?> c)
		{
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
		throw new UnsupportedOperationException();
	}
}
