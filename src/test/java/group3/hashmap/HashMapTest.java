package group3.hashmap;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class HashMapTest
{

	private HashMap<Integer, Integer> hashMap;

	@Before
	public void setUp()
	{
		this.hashMap = new HashMap<>();
	}

	@Test
	public void size() throws Exception
	{
		assertEquals(0, hashMap.size());
		hashMap.put(0, 0);
		assertEquals(1, hashMap.size());
		hashMap.put(0, 0);
		assertEquals(1, hashMap.size());
		hashMap.put(1, 0);
		assertEquals(2, hashMap.size());
	}


	@Test
	public void isEmpty() throws Exception
	{
		assertTrue(hashMap.isEmpty());
		hashMap.put(0, 0);
		assertFalse(hashMap.isEmpty());
	}

	@Test
	public void containsKey() throws Exception
	{
		assertFalse(hashMap.containsKey(0));
		hashMap.put(0, 0);
		assertTrue(hashMap.containsKey(0));
	}

	@Test(expected = NullPointerException.class)
	public void containsKeyThrowsNullPointerException() throws Exception
	{
		hashMap.containsKey(null);
	}

	@Test
	public void containsValue() throws Exception
	{
		assertFalse(hashMap.containsValue(1));
		hashMap.put(0, 1);
		assertTrue(hashMap.containsValue(1));
		hashMap.remove(0);
		assertFalse(hashMap.containsValue(1));
	}

	@Test
	public void get() throws Exception
	{
		assertNull(hashMap.get(0));
		hashMap.put(0, 15);
		assertEquals(15, (long) hashMap.get(0));
	}

	@Test(expected = NullPointerException.class)
	public void getThrowsNullPointerException() throws Exception
	{
		hashMap.get(null);
	}

	@Test
	public void put() throws Exception
	{
		hashMap.put(0, 156);
		assertEquals(156, (long) hashMap.get(0));
		assertEquals(156, (long) hashMap.put(0, 200));
	}

	@Test(expected = NullPointerException.class)
	public void putThrowsNullPointerException() throws Exception
	{
		hashMap.put(null, 1);
	}

	@Test
	public void remove() throws Exception
	{
		hashMap.put(0, 250);
		assertFalse(hashMap.isEmpty());
		assertEquals(250, (long) hashMap.remove(0));
		assertTrue(hashMap.isEmpty());
	}

	@Test(expected = NullPointerException.class)
	public void removeThrowsNullPointerException() throws Exception
	{
		hashMap.remove(null);
	}

	@Test
	public void putAll() throws Exception
	{
		java.util.HashMap<Integer, Integer> parameter = new java.util.HashMap<>();
		parameter.put(0, 0);
		parameter.put(1, 1);
		parameter.put(2, 2);

		assertEquals(0, hashMap.size());
		hashMap.putAll(parameter);
		assertEquals(3, hashMap.size());
	}

	@Test(expected = NullPointerException.class)
	public void putAllThrowsNullPointerException()
	{
		hashMap.putAll(null);
	}

	@Test
	public void clear() throws Exception
	{
		hashMap.put(0, 0);
		hashMap.put(1, 1);

		assertFalse(hashMap.isEmpty());
		assertEquals(2, hashMap.size());

		hashMap.clear();

		assertTrue(hashMap.isEmpty());
		assertEquals(0, hashMap.size());
	}

	@Test
	public void values() throws Exception
	{
		hashMap.put(0, 0);
		hashMap.put(1, 1);
		hashMap.put(2, 2);
		hashMap.put(9, 9);

		Collection<Integer> values = hashMap.values();

		assertTrue(values.contains(0));
		assertTrue(values.contains(1));
		assertTrue(values.contains(2));
		assertTrue(values.contains(9));
	}

	@Test
	public void getCapacity() throws Exception
	{
		assertEquals(16, hashMap.getCapacity());
		hashMap = new HashMap<>(32);
		assertEquals(32, hashMap.getCapacity());
	}

	@Test
	public void getLoadFactor() throws Exception
	{
		assertEquals(0.75, hashMap.getLoadFactor(), 0.0001);
		hashMap = new HashMap<>(0.1);
		assertEquals(0.1, hashMap.getLoadFactor(), 0.0001);
	}

	@Test
	public void canHandleCollisions()
	{
		hashMap = new HashMap<>(10);

		hashMap.put(7, 1);
		hashMap.put(17, 2);
		hashMap.put(27, 3);
		hashMap.put(37, 4);
		hashMap.put(47, 5);

		assertEquals(1, (long) hashMap.get(7));
		assertEquals(2, (long) hashMap.get(17));
		assertEquals(3, (long) hashMap.get(27));
		assertEquals(4, (long) hashMap.get(37));
		assertEquals(5, (long) hashMap.get(47));

		assertEquals(5, hashMap.size());
	}
}