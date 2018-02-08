package group3.hashmap;

import com.nitorcreations.junit.runners.NestedRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(NestedRunner.class)
public class HashMapTest
{

	private HashMap<Integer, Integer> instance;

	@Before
	public void setUp()
	{
		this.instance = new HashMap<>();
	}

	@Test
	public void size() throws Exception
	{
		assertEquals(0, instance.size());
		instance.put(0, 0);
		assertEquals(1, instance.size());
		instance.put(0, 0);
		assertEquals(1, instance.size());
		instance.put(1, 0);
		assertEquals(2, instance.size());
	}


	@Test
	public void isEmpty() throws Exception
	{
		assertTrue(instance.isEmpty());
		instance.put(0, 0);
		assertFalse(instance.isEmpty());
	}

	@Test
	public void containsKey() throws Exception
	{
		assertFalse(instance.containsKey(0));
		instance.put(0, 0);
		assertTrue(instance.containsKey(0));
	}

	@Test(expected = NullPointerException.class)
	public void containsKeyThrowsNullPointerException() throws Exception
	{
		instance.containsKey(null);
	}

	@Test
	public void containsValue() throws Exception
	{
		assertFalse(instance.containsValue(1));
		instance.put(0, 1);
		assertTrue(instance.containsValue(1));
		instance.remove(0);
		assertFalse(instance.containsValue(1));
	}

	@Test
	public void get() throws Exception
	{
		assertNull(instance.get(0));
		instance.put(0, 15);
		assertEquals(15, (long) instance.get(0));
	}

	@Test(expected = NullPointerException.class)
	public void getThrowsNullPointerException() throws Exception
	{
		instance.get(null);
	}

	@Test
	public void put() throws Exception
	{
		assertFalse(instance.containsKey(0));
		instance.put(0, 156);
		assertTrue(instance.containsKey(0));
		assertEquals(156, (long) instance.get(0));
		assertEquals(156, (long) instance.put(0, 200));
	}

	@Test(expected = NullPointerException.class)
	public void putThrowsNullPointerException() throws Exception
	{
		instance.put(null, 1);
	}

	@Test
	public void remove() throws Exception
	{
		instance.put(0, 250);
		assertFalse(instance.isEmpty());
		assertEquals(250, (long) instance.remove(0));
		assertTrue(instance.isEmpty());
	}

	@Test(expected = NullPointerException.class)
	public void removeThrowsNullPointerException() throws Exception
	{
		instance.remove(null);
	}

	@Test
	public void putAll() throws Exception
	{
		java.util.HashMap<Integer, Integer> parameter = new java.util.HashMap<>();
		parameter.put(0, 0);
		parameter.put(1, 1);
		parameter.put(2, 2);

		assertEquals(0, instance.size());
		instance.putAll(parameter);
		assertEquals(3, instance.size());
	}

	@Test(expected = NullPointerException.class)
	public void putAllThrowsNullPointerException()
	{
		instance.putAll(null);
	}

	@Test
	public void clear() throws Exception
	{
		instance.put(0, 0);
		instance.put(1, 1);

		assertFalse(instance.isEmpty());
		assertEquals(2, instance.size());

		instance.clear();

		assertTrue(instance.isEmpty());
		assertEquals(0, instance.size());
	}

	@Test
	public void values() throws Exception
	{
		instance.put(0, 0);
		instance.put(1, 1);
		instance.put(2, 2);
		instance.put(9, 9);

		Collection<Integer> values = instance.values();

		assertTrue(values.contains(0));
		assertTrue(values.contains(1));
		assertTrue(values.contains(2));
		assertTrue(values.contains(9));
	}

	@Test
	public void getCapacity() throws Exception
	{
		assertEquals(16, instance.getCapacity());
		instance = new HashMap<>(32);
		assertEquals(32, instance.getCapacity());
	}

	@Test
	public void getLoadFactor() throws Exception
	{
		assertEquals(0.75, instance.getLoadFactor(), 0.0001);
		instance = new HashMap<>(0.1);
		assertEquals(0.1, instance.getLoadFactor(), 0.0001);
	}

	@Test
	public void canHandleCollisions() throws Exception
	{
		instance = new HashMap<>(10);

		instance.put(7, 1);
		instance.put(17, 2);
		instance.put(27, 3);
		instance.put(37, 4);
		instance.put(47, 5);

		assertEquals(1, (long) instance.get(7));
		assertEquals(2, (long) instance.get(17));
		assertEquals(3, (long) instance.get(27));
		assertEquals(4, (long) instance.get(37));
		assertEquals(5, (long) instance.get(47));

		assertEquals(5, instance.size());
	}

	@Test
	public void canExpand() throws Exception
	{
		instance = new HashMap<>(4);
		assertEquals(4, instance.getCapacity());
		instance.put(1, 1);
		instance.put(2, 1);
		instance.put(3, 1);
		assertEquals(8, instance.getCapacity());
	}

	@Test
	public void entrySet() throws Exception
	{
		instance.put(0, 0);
		instance.put(1, 1);

		Set<java.util.Map.Entry<Integer, Integer>> entrySet = instance.entrySet();
		entrySet.add(new HashMap.Pair<>(3, 3));
		assertEquals(3, instance.size());
	}

	public static class PairSetTest
	{

		private HashMap<Integer, Integer> map;
		private HashMap.PairSet           set;

		private HashMap.Pair<Integer, Integer> pair(Integer key, Integer value)
		{
			return new HashMap.Pair<>(key, value);
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

		@Test(expected = NullPointerException.class)
		public void containsThrowsNullPointerException()
		{
			set.contains(null);
		}

		@Test
		public void iterator()
		{
			map.put(0, 0);
			map.put(1, 1);
			map.put(2, 2);

			HashMap.PairSet.PairIterator it = set.iterator();

			int counter = 0;
			while (it.hasNext() == true) {
				counter++;
				it.next();
			}
			assertEquals(3, counter);
		}

		public static class PairIteratorTest
		{

			private HashMap<Integer, Integer>    map;
			private HashMap.PairSet              set;
			private HashMap.PairSet.PairIterator it;

			@Before
			public void setUp()
			{
				map = new HashMap<>();
				set = map.entrySet();
			}

			@Test
			public void hasNext()
			{
				HashMap.PairSet.PairIterator it = set.iterator();

				assertTrue(set.isEmpty());
				assertFalse(it.hasNext());

				setUp();
				set.add(pair(0, 0));
				set.add(pair(1, 1));
				it = set.iterator();

				assertFalse(set.isEmpty());
				assertTrue(it.hasNext());
				it.next();
				assertTrue(it.hasNext());
				it.next();
				assertFalse(it.hasNext());
			}


			@Test
			public void next()
			{
				hasNext();
			}

			private HashMap.Pair<Integer, Integer> pair(Integer key, Integer value)
			{
				return new HashMap.Pair<>(key, value);
			}
		}

		@Test
		public void toArray()
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
		public void toArrayArgument()
		{
			// No elements
			Object[] array = (HashMap.Pair<Integer,
					Integer>[]) new
					HashMap.Pair[0];
			array = set.<Object>toArray(array);
			assertEquals(0, array.length);

			// Exact length
			array = (Object[]) new HashMap.Pair[3];
			set.add(pair(0, 0));
			set.add(pair(1, 1));
			set.add(pair(2, 2));
			array = set.<Object>toArray(array);
			assertEquals(3, array.length);

			// Greater length
			set.add(pair(3, 3));
			array = (Object[]) new HashMap.Pair[3];
			array = set.<Object>toArray(array);
			assertEquals(4, array.length);
		}

		@Test(expected = NullPointerException.class)
		public void toArrayArgumentThrowsNullPointerException() throws Exception
		{
			set.toArray(null);
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


		@Test(expected = NullPointerException.class)
		public void addThrowsNullPointerException() throws Exception
		{
			set.add(null);
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

		@Test(expected = NullPointerException.class)
		public void removeThrowsNullPointerException() throws Exception
		{
			set.remove(null);
		}

		@Test
		public void containsAll() throws Exception
		{
			set.add(pair(0, 0));
			set.add(pair(1, 1));

			Collection<HashMap.Pair<Integer, Integer>> c;

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

		@Test(expected = NullPointerException.class)
		public void containsAllThrowsNullPointerException() throws Exception
		{
			set.contains(null);
		}

		@Test
		public void addAll() throws Exception
		{
			Collection<HashMap.Pair<Integer, Integer>> c;

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

		@Test(expected = NullPointerException.class)
		public void addAllThrowsNullPointerException() throws Exception
		{
			set.addAll(null);
		}

		@Test
		public void retainAll() throws Exception
		{
			set.add(pair(0, 0));
			set.add(pair(1, 1));
			set.add(pair(2, 2));

			Collection<HashMap.Pair<Integer, Integer>> c;

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

		@Test(expected = NullPointerException.class)
		public void retainAllThrowsNullPointerException() throws Exception
		{
			set.retainAll(null);
		}

		@Test
		public void removeAll()
		{
			set.add(pair(0, 0));
			set.add(pair(1, 1));
			set.add(pair(2, 2));

			Collection<HashMap.Pair<Integer, Integer>> c;

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

		@Test(expected = NullPointerException.class)
		public void removeAllThrowsNullPointerException() throws Exception
		{
			set.removeAll(null);
		}

		@Test(expected = NullPointerException.class)
		public void removeAllThrowsNullPointerExceptionOnElement() throws Exception
		{
			Collection<HashMap.Pair<Integer, Integer>> c = new ArrayList<>();
			c.add(null);
			set.removeAll(c);
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
}