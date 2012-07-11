package com.adamldavis.pathfinder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class AntPathFinderTest {

	AntPathFinder finder;

	@Before
	public void setUp() throws Exception {
		finder = new AntPathFinder();
	}

	@After
	public void tearDown() throws Exception {
		finder = null;
	}

	@DataPoint
	public static final PathGrid grid = makeGrid(12, 12);
	@DataPoint
	public static final PathGrid grid32 = makeGrid(32, 32);
	@DataPoint
	public static final PathGrid grid64 = makeGrid(55, 64);

	@Theory
	public void testFindPath(PathGrid grid) {
		int[] path = finder.findPath(grid, 0, 0, grid.getWidth() - 1,
				grid.getHeight() - 1);
		assertNotNull(path);
		assertThat(path.length, is(grid.getWidth() + grid.getHeight() - 2));
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < grid.getWidth() - 1; i++) {
			list.add(1); // right
		}
		for (int i = 0; i < grid.getHeight() - 1; i++) {
			list.add(2); // down
		}
		assertThat(path, equalTo(unwrap(list)));
	}

	// makes a grid completely blocked in the middle
	public static PathGrid makeGrid(int w, int h) {
		boolean[][] bb = new boolean[w][h];

		for (int x = 1; x < w; x++)
			for (int y = 1; y < h; y++)
				bb[x][y] = true;
		// clear top and bottom rows;
		Arrays.fill(bb[0], false);
		Arrays.fill(bb[bb.length - 1], false);
		return new SimplePathGrid(bb);
	}

	// arg! dollar can do this
	private int[] unwrap(List<Integer> list) {
		int[] a = new int[list.size()];
		for (int i = 0; i < a.length; i++)
			a[i] = list.get(i);
		return a;
	}
}
