package day1_live;

import java.util.Iterator;

public class Ob {
	public static void main(String[] args) {
		Iterable<Integer> iter = new Iterable<Integer>() {
			@Override
			public Iterator<Integer> iterator() {
				return new Iterator<Integer>() {
					int i = 0;
					final static int MAX = 10;

					@Override
					public boolean hasNext() {
						return i < MAX;
					}

					@Override
					public Integer next() {
						return ++i;
					}
				};
			}
		};

		for (Integer integer : iter) {
			System.out.println("integer = " + integer);
		}
	}
}
