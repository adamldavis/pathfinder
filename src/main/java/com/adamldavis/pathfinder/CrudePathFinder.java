/** Copyright 2012, Adam L. Davis. All rights reserved. */
package com.adamldavis.pathfinder;

import java.awt.Point;
import java.util.Stack;

/**
 * CrudePathFinder attempts to do an exhaustive search to find the shortest path but is limited to a certain number of iterations.
 * So it might take a long time to complete.
 *
 * @author <A HREF="http://www.adamldavis.com">Adam L. Davis</A>
 * @version Version 1.0, May 28, 2001
 */
public class CrudePathFinder extends PathFinder {

	static class Finder {

		Point p1;
		Point p2;
		int dir;
		Path path;
		PathGrid trav;

		public Finder(int x1, int y1, int x2, int y2, int k, Path pat, PathGrid tr) {
			p1 = new Point(x1, y1);
			p2 = new Point(x2, y2);
			dir = k;
			path = pat;
			trav = tr;
		}

		public int getX1() {
			return (int) p1.getX();
		}

		public int getY1() {
			return (int) p1.getY();
		}

		public int getX2() {
			return (int) p2.getX();
		}

		public int getY2() {
			return (int) p2.getY();
		}

		public Point getP1() {
			return p1;
		}

		public Point getP2() {
			return p2;
		}

		public int getDir() {
			return dir;
		}

		public Path getPath() {
			return path;
		}

		public PathGrid getTrav() {
			return trav;
		}
	}
	
	private static final boolean bDEBUG = false;
	/**
	 * The maximum number of interations that the findPath method can take.
	 */
	private static final int MAX_ITER = 10000;
	/**
	 * Stack used to implement depth-first-search.
	 */
	Stack pathStack;

	public CrudePathFinder() {
		super();
	}

	/**
	 * Initializes this pathfinder with a maximum possible distance for a
	 * path.
	 *
	 * @param maxDistance Maximum possible distance of a path in the grid.
	 */
	public CrudePathFinder(int maxDistance) {
		super(maxDistance);
	}

	/**
	 * Finds a shortest path given an array and a starting point and end
	 * point. Returns path in the form of an array of directions. <BR> 0 =
	 * up, 1 = right, 2 = down, 3 = left.
	 *
	 * @param grid True means it is an obstacle.
	 */
	public int[] findPath(PathGrid grid, int x1, int y1, int x2, int y2) {
		int n = 0;
		Path ret = null;
		Path temp;

		/*
		 * call the super.
		 */
		if (super.hasProblem(grid, x1, y1, x2, y2)) {
			return null;
		}

		pathStack = new Stack();

		PathGrid trav = new SimplePathGrid(mygrid.getWidth(), mygrid.getHeight());
		temp = new Path(); //new path.
		int xdif = x2 - x1;
		int ydif = y2 - y1;
		if (Math.abs(xdif) > Math.abs(ydif)) {
			doPush(x1, y1, x2, y2, xdif > 0 ? 3 : 1, temp, trav, -1*xdif);
			doPush(x1, y1, x2, y2, 0, temp, trav, -1 * ydif);
			doPush(x1, y1, x2, y2, 2, temp, trav, ydif);
			doPush(x1, y1, x2, y2, xdif > 0 ? 1 : 3, temp, trav, xdif); //1st
		} else {
			doPush(x1, y1, x2, y2, ydif > 0 ? 0 : 2, temp, trav, -1*ydif);
			doPush(x1, y1, x2, y2, 1, temp, trav, -1 * xdif);
			doPush(x1, y1, x2, y2, 3, temp, trav, xdif);
			doPush(x1, y1, x2, y2, ydif > 0 ? 2 : 0, temp, trav, ydif); //1st
		}

		while (!pathStack.empty()) {
			n++;
			if (n > MAX_ITER) {
				break; //to stop long loops.
			}
			temp = findPath((Finder) pathStack.pop());
			
			if (temp != null) {
				ret = temp;
			}
		}
		if (bDEBUG) {
			System.out.println("crude.path = " + ret);
			System.out.println("Crude n=" + n);
		}

		if (ret == null) {
			return null;
		} else {
			return (ret.getArray());
		}
	}

	private Path findPath(Finder f) {
		Path temp;

		temp = findPath(f.getX1(), f.getY1(), f.getX2(), f.getY2(), f.getDir(), f.getPath(), f.getTrav());
		return temp;
	}

	/**
	 * This method does all the work. w = direction.
	 */
	private Path findPath(int x1, int y1, int x2, int y2, int w, Path path, PathGrid trav) {
		if (bDEBUG) {
			System.out.println("c findPath " + x1 + "," + y1 + ":" + x2 + "," + y2 + "\t" + "distance = "
				+ distance);
		}
		int i;
		Path temp;
		Path ret = null;
		int d = path.size();

		if (mygrid.getGrid(x1, y1) || goingInCircle(x1, y1, trav)) {
			//this keeps us from going in circles.
			return ret;
		} else if (x1 == x2 && y1 == y2) { // found path
			if (d < distance) {
				distance = d;
				ret = path;
			}
		} //if we already found a better path.
		else if ((int) Point.distance(x1, y1, x2, y2) + d >= distance) {
			return ret;
		} else {
			/*
			 * count it traveled.
			 */
			trav.setGrid(x1, y1, true);

			switch (w) {
				case 0:
					--y1;
					break;
				case 1:
					++x1;
					break;
				case 2:
					++y1;
					break;
				case 3:
					--x1;
					break;
				default:
			}
			if (!isDud(x1, y1, trav)) {
				temp = new Path(path);
				temp.add(w); //adds this direction to path.

				/*
				 * Try moving in 4 directions.
				 */
				int xdif = x2 - x1;
				int ydif = y2 - y1;
				if (xdif == 0 && ydif == 0) {
					pathStack.push(new Finder(x1, y1, x2, y2, -1, temp, trav.copy()));
				} else if (Math.abs(xdif) > Math.abs(ydif)) {
					doPush(x1, y1, x2, y2, xdif > 0 ? 3 : 1, temp, trav, -1*xdif);
					doPush(x1, y1, x2, y2, 0, temp, trav, -1 * ydif);
					doPush(x1, y1, x2, y2, 2, temp, trav, ydif);
					doPush(x1, y1, x2, y2, xdif > 0 ? 1 : 3, temp, trav, xdif); //1st
				} else {
					doPush(x1, y1, x2, y2, ydif > 0 ? 0 : 2, temp, trav, -1*ydif);
					doPush(x1, y1, x2, y2, 1, temp, trav, -1 * xdif);
					doPush(x1, y1, x2, y2, 3, temp, trav, xdif);
					doPush(x1, y1, x2, y2, ydif > 0 ? 2 : 0, temp, trav, ydif); //1st
				}
			}
		}
		return ret;
	}

	// i = direction.
	private void doPush(int x1, int y1, int x2, int y2, int i, Path path, PathGrid trav, int dif) {
		//if (dif == 0) return;
		
		pathStack.push(new Finder(x1, y1, x2, y2, i, path, trav.copy()));
	}

	/**
	 * Tells us if a space is occupied or has already been visited.
	 *
	 * @return True if the space is an obstacle or has already been visited.
	 */
	protected boolean gridValue(int i, int j, PathGrid trav) {
		return (mygrid.getGrid(i, j) || trav.getGrid(i, j));
	}

	/**
	 * Tells us if a space is occupied or has already been visited. Uses
	 * CrudePathFinder.gridValue(int,int,PathGrid).
	 *
	 * @return True if the space is an obstacle or has already been visited
	 * or has been found to be a dead-end.
	 */
	boolean isDud(int i, int j, PathGrid trav) {
		return (gridValue(i, j, trav) || doneGrid.getGrid(i, j));
	}

	/**
	 * Finds out if we've gone in a useless semi-circle. Finds out how many
	 * spaces around a certain point have already been visited.
	 */
	protected boolean goingInCircle(int x, int y, PathGrid trav) {
		int num = 0;
		int orig = 0;
		//if it's possible to get to a point and I've been there
		// then up the counter.
		for (int i = 0; i < 4; i++) {
			if (!mygrid.getGrid(x + dx(i), y + dy(i)) && trav.getGrid(x + dx(i), y + dy(i))) {
				num++;
				orig = i;
			}
		}
		if (num > 1) {
			return true;
		}

		for (int i = 0; i < 4; i++) {
			if (i != orig) {
				if (!mygrid.getGrid(x + dx(i), y + dy(i))
					&& !mygrid.getGrid(x + 2 * dx(i), y + 2 * dy(i))
					&& trav.getGrid(x + 2 * dx(i), y + 2 * dy(i))) {
					return true;
				}
			}
		}
		return false;
	}

	protected int dx(int dir) {
		int ret = 0;
		switch (dir) {
			case 1:
				ret = 1;
				break;
			case 3:
				ret = -1;
				break;
			default:
		}
		return ret;
	}

	protected int dy(int dir) {
		int ret = 0;
		switch (dir) {
			case 2:
				ret = 1;
				break;
			case 0:
				ret = -1;
				break;
			default:
		}
		return ret;
	}
}//CrudePathFinder
