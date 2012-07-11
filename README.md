[![Build Status](https://secure.travis-ci.org/adamd/pathfinder.png?branch=master)](http://travis-ci.org/adamd/pathfinder)
PathFinder for Java
===================

PathFinder is a project for finding paths in a 2-dimensional array from one point to another,
for example, finding a path for a character in a 2D game.
It uses interfaces to make it reusable in many different contexts.

The 2D-grid can be of any size.
In this grid only four directions of movement are possible: up, down, left, right.

There are several interfaces and three implementations of path-finders (AntPathFinder is recommended).

Running the Example
-------------------

If you just want to see it in action, type the following:
	
	java com.adamldavis.pathfinder.PathPanel
	
Resize the window. Left click to create obstacles. Then right click and drag to find a path.

Usage
-----

First you need to implement the PathGrid interface or use SimplePathGrid which has a 2D array of booleans.

	PathGrid grid = new SimplePathGrid(width, height);

Then you need to create a pathFinder and use it.

	PathFinder finder = new AntPathFinder(200);
	int[] path = finder.findPath(grid, x1, y1, x2, y2);

The path array is a series of numbers representing directions (0 = up, 1 = right, 2 = down, 3 = left).
The AntPathFinder will very quickly determine a path to the target using an optimized depth-first search.
It does not necessarily find the shortest path, but in most cases it does.


Known Issues
-------------
CrudePathFinder attempts to do an exhaustive search to find the shortest path but is limited to a certain number of iterations.
So it might take a long time to complete.

The other path finder, RandomPathFinder, breaks a path in half randomly, then delegates to CrudePathFinder.

I wrote the bulk of this code a long time ago, so please don't judge me by it.

License
-------

Copyright 2012, Adam L. Davis. All rights reserved.
Made Available for use under a BSD-style license. See LICENSE
