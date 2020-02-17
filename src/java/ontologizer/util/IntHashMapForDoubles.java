/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontologizer.util;

/**
 * Stripped-down-version of the general HashMap function suitable
 * for integer keys and double values.
 * 
 * @author Sebastian Bauer
 * @see java.util.HashMap
 */
public class IntHashMapForDoubles
{
	/**
	 * The default initial capacity - MUST be a power of two.
	 */
	static final int DEFAULT_INITIAL_CAPACITY = 16;

	/**
	 * The maximum capacity, used if a higher value is implicitly specified
	 * by either of the constructors with arguments.
	 * MUST be a power of two <= 1<<30.
	 */
	static final int MAXIMUM_CAPACITY = 1 << 30;

	/**
	 * The load factor used when none specified in constructor.
	 */
	static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/**
	 * The table, resized as necessary. Length MUST Always be a power of two.
	 */
	transient Entry[] table;

	/**
	 * The number of key-value mappings contained in this map.
	 */
	transient int size;

	/**
	 * The next size value at which to resize (capacity * load factor).
	 * @serial
	 */
	int threshold;

	/**
	 * The load factor for the hash table.
	 *
	 * @serial
	 */
	final float loadFactor;

	/**
	 * The number of times this HashMap has been structurally modified
	 * Structural modifications are those that change the number of mappings in
	 * the HashMap or otherwise modify its internal structure (e.g.,
	 * rehash).  This field is used to make iterators on Collection-views of
	 * the HashMap fail-fast.  (See ConcurrentModificationException).
	 */
	transient volatile int modCount;

	/**
	 * Constructs an empty <tt>HashMap</tt> with the specified initial
	 * capacity and load factor.
	 *
	 * @param  initialCapacity the initial capacity
	 * @param  loadFactor      the load factor
	 * @throws IllegalArgumentException if the initial capacity is negative
	 *         or the load factor is nonpositive
	 */
	public IntHashMapForDoubles(int initialCapacity, float loadFactor)
	{
	    if (initialCapacity < 0)
	        throw new IllegalArgumentException("Illegal initial capacity: " +
	                                           initialCapacity);
	    if (initialCapacity > MAXIMUM_CAPACITY)
	        initialCapacity = MAXIMUM_CAPACITY;
	    if (loadFactor <= 0 || Float.isNaN(loadFactor))
	        throw new IllegalArgumentException("Illegal load factor: " +
	                                           loadFactor);
	
	    // Find a power of 2 >= initialCapacity
	    int capacity = 1;
	    while (capacity < initialCapacity)
	        capacity <<= 1;
	
	    this.loadFactor = loadFactor;
	    threshold = (int)(capacity * loadFactor);
	    table = new Entry[capacity];
	    init();
	}

	/**
	 * Constructs an empty <tt>HashMap</tt> with the specified initial
	 * capacity and the default load factor (0.75).
	 *
	 * @param  initialCapacity the initial capacity.
	 * @throws IllegalArgumentException if the initial capacity is negative.
	 */
	public IntHashMapForDoubles(int initialCapacity)
	{
	    this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * Constructs an empty <tt>HashMap</tt> with the default initial capacity
	 * (16) and the default load factor (0.75).
	 */
	public IntHashMapForDoubles()
	{
	    this.loadFactor = DEFAULT_LOAD_FACTOR;
	    threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
	    table = new Entry[DEFAULT_INITIAL_CAPACITY];
	    init();
	}


	// internal utilities

	/**
	 * Initialization hook for subclasses. This method is called
	 * in all constructors and pseudo-constructors (clone, readObject)
	 * after HashMap has been initialized but before any entries have
	 * been inserted.  (In the absence of this method, readObject would
	 * require explicit knowledge of subclasses.)
	 */
	void init()
	{
	}

	/**
	 * Applies a supplemental hash function to a given hashCode, which
	 * defends against poor quality hash functions.  This is critical
	 * because HashMap uses power-of-two length hash tables, that
	 * otherwise encounter collisions for hashCodes that do not differ
	 * in lower bits. Note: Null keys always map to hash 0, thus index 0.
	 */
	static int hash(int h) {
	    // This function ensures that hashCodes that differ only by
	    // constant multiples at each bit position have a bounded
	    // number of collisions (approximately 8 at default load factor).
	    h ^= (h >>> 20) ^ (h >>> 12);
	    return h ^ (h >>> 7) ^ (h >>> 4);
	}

	/**
	 * Returns index for hash code h.
	 */
	static int indexFor(int h, int length) {
	    return h & (length-1);
	}

	/**
	 * Returns the number of key-value mappings in this map.
	 *
	 * @return the number of key-value mappings in this map
	 */
	public int size() {
	    return size;
	}

	/**
	 * Returns <tt>true</tt> if this map contains no key-value mappings.
	 *
	 * @return <tt>true</tt> if this map contains no key-value mappings
	 */
	public boolean isEmpty() {
	    return size == 0;
	}

	/**
	 * Returns the value to which the specified key is mapped,
	 * or {@code null} if this map contains no mapping for the key.
	 *
	 * <p>More formally, if this map contains a mapping from a key
	 * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
	 * key.equals(k))}, then this method returns {@code v}; otherwise
	 * it returns {@code null}.  (There can be at most one such mapping.)
	 *
	 * <p>A return value of {@code null} does not <i>necessarily</i>
	 * indicate that the map contains no mapping for the key; it's also
	 * possible that the map explicitly maps the key to {@code null}.
	 * The {@link #containsKey containsKey} operation may be used to
	 * distinguish these two cases.
	 *
	 * @see #put(Object, Object)
	 */
	public double get(int key)
	{
	    int hash = hash(key);
	    for (Entry e = table[indexFor(hash, table.length)]; e != null; e = e.next)
	    {
	    	
	        if (/*e.hash == hash &&*/ e.key == key)
	            return e.value;
	    }
	    return Double.NaN;
	}

	/**
	 * Returns <tt>true</tt> if this map contains a mapping for the
	 * specified key.
	 *
	 * @param   key   The key whose presence in this map is to be tested
	 * @return <tt>true</tt> if this map contains a mapping for the specified
	 * key.
	 */
	public boolean containsKey(int key)
	{
		int hash = hash(key);
	    for (Entry e = table[indexFor(hash, table.length)]; e != null; e = e.next)
	    {
	        if (/*e.hash == hash && */e.key == key)
	            return true;
	    }
	    return false;
	}

	/**
	 * Associates the specified value with the specified key in this map.
	 * If the map previously contained a mapping for the key, the old
	 * value is replaced.
	 *
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return the previous value associated with <tt>key</tt>, or
	 *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
	 *         (A <tt>null</tt> return can also indicate that the map
	 *         previously associated <tt>null</tt> with <tt>key</tt>.)
	 */
	public void put(int  key, double value)
	{
	    int hash = hash(key);
	    int i = indexFor(hash, table.length);
	    for (Entry e = table[i]; e != null; e = e.next)
	    {
	        if (/*e.hash == hash && */e.key == key)
	        {
	            e.value = value;
	            return;
	        }
	    }
	
	    modCount++;
	    addEntry(hash, key, value, i);
	}

	/**
	 * Rehashes the contents of this map into a new array with a
	 * larger capacity.  This method is called automatically when the
	 * number of keys in this map reaches its threshold.
	 *
	 * If current capacity is MAXIMUM_CAPACITY, this method does not
	 * resize the map, but sets threshold to Integer.MAX_VALUE.
	 * This has the effect of preventing future calls.
	 *
	 * @param newCapacity the new capacity, MUST be a power of two;
	 *        must be greater than current capacity unless current
	 *        capacity is MAXIMUM_CAPACITY (in which case value
	 *        is irrelevant).
	 */
	void resize(int newCapacity)
	{
	    Entry[] oldTable = table;
	    int oldCapacity = oldTable.length;
	    if (oldCapacity == MAXIMUM_CAPACITY) {
	        threshold = Integer.MAX_VALUE;
	        return;
	    }
	
	    Entry[] newTable = new Entry[newCapacity];
	    transfer(newTable);
	    table = newTable;
	    threshold = (int)(newCapacity * loadFactor);
	}

	/**
	 * Transfers all entries from current table to newTable.
	 */
	void transfer(Entry[] newTable) {
	    Entry[] src = table;
	    int newCapacity = newTable.length;
	    for (int j = 0; j < src.length; j++) {
	        Entry e = src[j];
	        if (e != null)
	        {
	            src[j] = null;
	            do
	            {
	                Entry next = e.next;
	                int i = indexFor(e.hash, newCapacity);
	                e.next = newTable[i];
	                newTable[i] = e;
	                e = next;
	            } while (e != null);
	        }
	    }
	}
	/**
	 * Removes all of the mappings from this map.
	 * The map will be empty after this call returns.
	 */
	public void clear() {
	    modCount++;
	    Entry[] tab = table;
	    for (int i = 0; i < tab.length; i++)
	        tab[i] = null;
	    size = 0;
	}

	private static final class Entry
	{
	    final int key;
	    double value;
	    Entry next;
	    final int hash;
	
	    /**
	     * Creates new entry.
	     */
	    Entry(int h, int k, double v, Entry n)
	    {
	        value = v;
	        next = n;
	        key = k;
	        hash = h;
	    }

	    public final int getKey()
	    {
	        return key;
	    }
	
	    public final double getValue()
	    {
	        return value;
	    }
	
	    public final double setValue(double newValue)
	    {
	    	double oldValue = value;
	        value = newValue;
	        return oldValue;
	    }
	}

	/**
	 * Adds a new entry with the specified key, value and hash code to
	 * the specified bucket.  It is the responsibility of this
	 * method to resize the table if appropriate.
	 *
	 * Subclass overrides this to alter the behavior of put method.
	 */
	void addEntry(int hash, int key, double value, int bucketIndex)
	{
		Entry e = table[bucketIndex];
		
	    table[bucketIndex] = new Entry(hash, key, value, e);
	    if (size++ >= threshold)
	        resize(2 * table.length);
	}

	/**
	 * Like addEntry except that this version is used when creating entries
	 * as part of Map construction or "pseudo-construction" (cloning,
	 * deserialization).  This version needn't worry about resizing the table.
	 *
	 * Subclass overrides this to alter the behavior of HashMap(Map),
	 * clone, and readObject.
	 */
	void createEntry(int hash, int key, double value, int bucketIndex)
	{
		Entry e = table[bucketIndex];
	    table[bucketIndex] = new Entry(hash, key, value, e);
	    size++;
	}
}

