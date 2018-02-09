package group3.hashmap;

import com.nitorcreations.junit.runners.NestedRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;
import java.util.Map.Entry;

import static org.junit.Assert.*;

@RunWith(NestedRunner.class)
public class HashMapTest
{

	private HashMap<Integer, Integer> map;

	@Before
	public void setUp()
	{
		this.map = new HashMap<>();
	}

	@Test
	public void size() throws Exception
	{
		assertEquals(0, map.size());
		map.put(0, 0);
		assertEquals(1, map.size());
		map.put(0, 0);
		assertEquals(1, map.size());
		map.put(1, 0);
		assertEquals(2, map.size());
	}

	@Test
	public void isEmpty() throws Exception
	{
		assertTrue(map.isEmpty());
		map.put(0, 0);
		assertFalse(map.isEmpty());
	}

	@Test
	public void containsKey() throws Exception
	{
		assertFalse(map.containsKey(0));
		map.put(0, 0);
		assertTrue(map.containsKey(0));
	}

	@Test
	public void containsValue() throws Exception
	{
		assertFalse(map.containsValue(1));
		map.put(0, 1);
		assertTrue(map.containsValue(1));
		map.remove(0);
		assertFalse(map.containsValue(1));
	}

	@Test
	public void get() throws Exception
	{
		assertNull(map.get(0));
		map.put(0, 15);
		map.put(null, 30);
		assertEquals(15, (long) map.get(0));
		assertEquals(30, (long) map.get(null));
	}

	@Test
	public void put() throws Exception
	{
		assertFalse(map.containsKey(0));
		map.put(0, 156);
		map.put(null, 30);
		assertTrue(map.containsKey(0));
		assertEquals(156, (long) map.get(0));
		assertEquals(156, (long) map.put(0, 200));
		assertEquals(30, (long) map.get(null));
		assertEquals(2, map.size()); // Can override values
	}

	@Test
	public void remove() throws Exception
	{
		map.put(0, 250);
		map.put(null, 32);
		assertFalse(map.isEmpty());
		assertEquals(250, (long) map.remove(0));
		assertEquals(32, (long) map.remove(null));
		assertTrue(map.isEmpty());
	}

	@Test
	public void putAll() throws Exception
	{
		java.util.HashMap<Integer, Integer> parameter = new java.util.HashMap<>();
		parameter.put(0, 0);
		parameter.put(1, 1);
		parameter.put(2, 2);

		assertEquals(0, map.size());
		map.putAll(parameter);
		assertEquals(3, map.size());
	}

	@Test
	public void putAllNull() throws Exception
	{
		assertTrue(map.isEmpty());
		map.putAll(new java.util.HashMap<>());
		assertTrue(map.isEmpty());
	}

	@Test
	public void clear() throws Exception
	{
		map.put(0, 0);
		map.put(1, 1);

		assertFalse(map.isEmpty());
		assertEquals(2, map.size());

		map.clear();

		assertTrue(map.isEmpty());
		assertEquals(0, map.size());
	}

	@Test
	public void values() throws Exception
	{
		map.put(0, 0);
		map.put(1, 1);
		map.put(2, 2);
		map.put(9, 9);

		Collection<Integer> values = map.values();

		assertTrue(values.contains(0));
		assertTrue(values.contains(1));
		assertTrue(values.contains(2));
		assertTrue(values.contains(9));
	}

	@Test
	public void capacity() throws Exception
	{
		assertEquals(16, map.capacity());
		map = new HashMap<>(32);
		assertEquals(32, map.capacity());
	}

	@Test
	public void getLoadFactor() throws Exception
	{
		assertEquals(0.75, map.getLoadFactor(), 0.0001);
		map = new HashMap<>(0.1);
		assertEquals(0.1, map.getLoadFactor(), 0.0001);
	}

	@Test
	public void canHandleCollisions() throws Exception
	{
		map = new HashMap<>(10);

		map.put(7, 1);
		map.put(17, 2);
		map.put(27, 3);
		map.put(37, 4);
		map.put(47, 5);

		assertEquals(1, (long) map.get(7));
		assertEquals(2, (long) map.get(17));
		assertEquals(3, (long) map.get(27));
		assertEquals(4, (long) map.get(37));
		assertEquals(5, (long) map.get(47));

		assertEquals(5, map.size());
	}

	@Test
	public void canExpand() throws Exception
	{
		map = new HashMap<>(4);
		assertEquals(4, map.capacity());
		map.put(1, 1);
		map.put(2, 1);
		map.put(3, 1);
		assertEquals(8, map.capacity());
	}

	@Test
	public void entrySet() throws Exception
	{
		assertSame(map.entrySet(), map.entrySet());
	}

	public static class NodeSetTest
	{

		private HashMap<Integer, Integer>        map;
		private Set<Map.Entry<Integer, Integer>> set;

		private HashMap.Node<Integer, Integer> pair(Integer key, Integer value)
		{
			return new HashMap.Node<>(key, value);
		}

		@Before
		public void setUp()
		{
			map = new HashMap<>();
			set = map.entrySet();
		}

		@Test
		public void size()
		{
			assertEquals(0, map.size());
			assertEquals(0, set.size());

			set.add(pair(0, 10));
			map.put(1, 20);

			assertEquals(2, map.size());
			assertEquals(2, set.size());
		}

		@Test
		public void isEmpty()
		{
			assertTrue(set.isEmpty());
			assertTrue(map.isEmpty());

			set.add(pair(1, 1));

			assertFalse(set.isEmpty());
			assertFalse(map.isEmpty());
		}

		@Test
		public void contains()
		{
			assertFalse(set.contains(pair(0, 0)));

			map.put(0, 0);
			set.add(pair(1, 1));

			assertTrue(set.contains(pair(0, 0)));
			assertTrue(set.contains(pair(1, 1)));
		}

		@Test
		public void iterator()
		{
			map.put(0, 0);
			map.put(1, 1);
			map.put(2, 2);

			Iterator<Entry<Integer, Integer>> it = set.iterator();

			int counter = 0;
			while (it.hasNext() == true) {
				counter++;
				it.next();
			}
			assertEquals(3, counter);
		}

		@Test(expected = NoSuchElementException.class)
		public void iteratorThrowsNoSuchElementExceptions()
		{
			Iterator<Entry<Integer, Integer>> it = set.iterator();

			it.next();
		}

		@Test
		public void toArray() throws Exception
		{
			assertEquals(0, set.toArray().length);
			assertEquals(0, set.size());

			set.add(pair(0, 0));
			map.put(1, 1);

			assertEquals(2, set.toArray().length);
			assertEquals(pair(0, 0), set.toArray()[0]);
			assertEquals(pair(1, 1), set.toArray()[1]);
		}

		@Test
		public void toArrayT() throws Exception
		{
			set.toArray((HashMap.Node<Integer, Integer>[]) new HashMap.Node[0]);

			assertEquals(0, set.toArray((HashMap.Node<Integer, Integer>[]) new HashMap.Node[0]).length);
			assertEquals(0, set.size());

			set.add(pair(0, 0));
			map.put(1, 1);

			HashMap.Node<Integer, Integer>[] array = set.toArray((HashMap.Node<Integer, Integer>[]) new HashMap
					.Node[0]);

			assertEquals(2, array.length);
			assertEquals(pair(0, 0), array[0]);
			assertEquals(pair(1, 1), array[1]);
		}

		@Test
		public void add() throws Exception
		{
			assertFalse(set.contains(pair(0, 0)));
			assertFalse(map.containsKey(0));
			set.add(pair(0, 0));
			assertTrue(set.contains(pair(0, 0)));
			assertTrue(map.containsKey(0));
		}

		@Test
		public void remove() throws Exception
		{
			set.add(pair(1, 1));
			set.add(pair(11, 11));

			assertTrue(set.contains(pair(1, 1)));
			assertTrue(set.contains(pair(11, 11)));
			assertTrue(map.containsKey(1));

			assertTrue(set.remove(pair(1, 1)));
			assertFalse(set.contains(pair(1, 1)));
			assertFalse(map.containsKey(1));
		}

		@Test
		public void containsAll() throws Exception
		{
			set.add(pair(0, 0));
			set.add(pair(1, 1));

			Collection<HashMap.Node<Integer, Integer>> c;

			c = new ArrayList<>();
			assertFalse(set.containsAll(c));

			c = new ArrayList<>();
			c.add(pair(0, 0));
			assertFalse(set.containsAll(c));

			c = new ArrayList<>();
			c.add(pair(0, 0));
			c.add(pair(1, 1));
			assertTrue(set.containsAll(c));
		}

		@Test
		public void addAll() throws Exception
		{
			Collection<HashMap.Node<Integer, Integer>> c;

			c = new ArrayList<>();

			c.add(pair(0, 0));
			assertTrue(set.addAll(c));
			assertFalse(set.addAll(c));
			assertTrue(set.contains(pair(0, 0)));
			assertTrue(map.containsKey(0));

			c.add(pair(1, 1));
			assertTrue(set.addAll(c));
			assertEquals(2, set.size());
			assertFalse(set.addAll(c));
			assertEquals(2, set.size());
		}

		@Test
		public void retainAll() throws Exception
		{
			set.add(pair(0, 0));
			set.add(pair(1, 1));
			set.add(pair(2, 2));

			Collection<HashMap.Node<Integer, Integer>> c;

			c = new ArrayList<>();
			assertTrue(set.retainAll(c));
			assertTrue(set.isEmpty());

			set.add(pair(0, 0));
			set.add(pair(1, 1));
			set.add(pair(2, 2));

			assertEquals(3, set.size());

			c.add(pair(0, 0));
			c.add(pair(1, 1));

			set.retainAll(c);
			assertEquals(2, set.size());
			assertEquals(2, map.size());
			assertTrue(set.contains(pair(0, 0)));
			assertTrue(set.contains(pair(1, 1)));
			assertFalse(set.contains(pair(2, 2)));
		}

		@Test
		public void removeAll()
		{
			set.add(pair(0, 0));
			set.add(pair(1, 1));
			set.add(pair(2, 2));

			Collection<HashMap.Node<Integer, Integer>> c;

			c = new ArrayList<>();
			assertFalse(set.removeAll(c));
			assertEquals(3, set.size());

			c = new ArrayList<>();
			c.add(pair(1, 1));
			c.add(pair(2, 2));

			assertTrue(set.removeAll(c));
			assertTrue(set.contains(pair(0, 0)));
			assertFalse(set.contains(pair(1, 1)));
			assertFalse(set.contains(pair(2, 2)));
		}

		@Test
		public void clear() throws Exception
		{
			set.add(pair(0, 0));
			assertFalse(set.isEmpty());
			set.clear();
			assertTrue(set.isEmpty());
		}
	}

	@Test
	public void keySet() throws Exception
	{
		assertSame(map.keySet(), map.keySet());
	}

	public static class KeySetTest
	{

		private HashMap<Integer, Integer> map;
		private Set<Integer>              set;

		@Before
		public void setUp()
		{
			map = new HashMap<>();
			set = map.keySet();
		}

		@Test
		public void size()
		{
			assertEquals(0, map.size());
			assertEquals(0, set.size());
			map.put(1, 20);
			assertEquals(1, map.size());
			assertEquals(1, set.size());
		}

		@Test
		public void isEmpty()
		{
			assertTrue(set.isEmpty());
			assertTrue(map.isEmpty());

			map.put(0, 1);

			assertFalse(set.isEmpty());
			assertFalse(map.isEmpty());
		}

		@Test
		public void contains() throws Exception
		{
			map.put(0, 0);
			assertTrue(set.contains(0));
			assertFalse(set.contains(1));
		}

		@Test
		public void iterator() throws Exception
		{
			map.put(0, 0);
			map.put(1, 1);
			map.put(2, 2);

			Iterator<Integer> it = set.iterator();

			int counter = 0;
			while (it.hasNext()) {
				it.next();
				counter++;
			}

			assertFalse(it.hasNext());
			assertEquals(3, counter);
		}

		@Test(expected = NoSuchElementException.class)
		public void iteratorThrowsNoSuchElementException() throws Exception
		{
			set.iterator().next();
		}

		@Test
		public void toArray() throws Exception
		{
			assertEquals(0, set.toArray().length);

			map.put(0, 0);
			map.put(1, 1);
			map.put(2, 2);

			Object[] array = set.toArray();
			assertEquals(0, array[0]);
			assertEquals(1, array[1]);
			assertEquals(2, array[2]);
			assertEquals(3, array.length);
		}

		@Test
		public void toArrayT() throws Exception
		{
			assertEquals(0, set.toArray(new Long[0]).length);
			assertEquals(0, set.size());

			map.put(0, 0);
			map.put(1, 1);

			Integer[] array = set.toArray(new Integer[2]);

			assertEquals(2, array.length);
			assertEquals((Object) 0, array[0]);
			assertEquals((Object) 1, array[1]);
		}

		@Test
		public void remove() throws Exception
		{
			assertFalse(set.remove(0));
			map.put(0, 0);
			assertTrue(set.contains(0));
			assertTrue(set.remove(0));
			assertFalse(set.contains(0));
		}

		@Test
		public void containsAll() throws Exception
		{
			assertTrue(set.containsAll(null));

			map.put(0, 0);
			map.put(1, 1);
			map.put(2, 2);

			Collection<Integer> c;

			c = new ArrayList<>();
			assertTrue(set.containsAll(c));

			c.add(0);
			c.add(1);
			assertFalse(set.containsAll(c));

			c.add(2);
			assertTrue(set.containsAll(c));
		}

		@Test
		public void retainAll() throws Exception
		{
			map.put(0, 0);
			assertFalse(set.isEmpty());
			assertTrue(set.retainAll(null));
			assertTrue(set.isEmpty());

			map.put(0, 0);
			map.put(1, 1);
			map.put(2, 2);

			Collection<Integer> c;

			c = new ArrayList<>();
			c.add(0);
			c.add(1);

			assertTrue(set.retainAll(c));
			assertEquals(2, set.size());
			assertTrue(set.contains(0));
			assertTrue(set.contains(1));
			assertFalse(set.contains(2));
		}

		@Test
		public void removeAll() throws Exception
		{
			map.put(0, 0);
			map.put(1, 1);
			map.put(2, 2);

			assertFalse(set.removeAll(null));
			assertEquals(3, set.size());
			Collection<Integer> c = new ArrayList<>();

			c.add(0);
			c.add(1);

			assertTrue(set.removeAll(c));

			assertEquals(1, set.size());
			assertTrue(set.contains(2));
		}

		@Test
		public void clear() throws Exception
		{
			map.put(0, 0);
			map.put(1, 1);

			assertFalse(map.isEmpty());
			assertFalse(set.isEmpty());
			assertEquals(2, set.size());

			set.clear();

			assertTrue(map.isEmpty());
			assertTrue(set.isEmpty());
			assertEquals(0, map.size());
			assertEquals(0, set.size());
		}
	}
}