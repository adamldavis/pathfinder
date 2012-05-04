package com.adamldavis.pathfinder;

/**
 * Super class of all PathFinder implementations for find paths in a 2D grid of
 * arbitrary obstacles.
 * 
 * @author <A HREF="http://www.adamldavis.com">Adam L. Davis</A>
 * @version Version 1.0, May 27, 2001
 */
public abstract class PathFinder
{

    private static final boolean bDEBUG = false;

    /** Distance variable. */
    int distance = Integer.MAX_VALUE;

    /**
     * Grid which holds the inputed grid.
     */
    PathGrid mygrid;

    /**
     * Tells which grids spaces are duds (meaning that they have been searched
     * already).
     */
    PathGrid doneGrid;

    public PathFinder()
    {
        this( 30000 );
    }

    /**
     * Initializes this pathfinder with a maximum possible distance for a path.
     * @param maxDistance Maximum possible distance of a path in the grid.
     */
    public PathFinder( int maxDistance )
    {
        this.distance = maxDistance;
    }

    public int getDistance()
    {
        return distance;
    }

    /**
     * Should find a path in the given grid.
     * 
     * @param grid
     *            True means it is an obstacle.
     */
    public abstract int[] findPath( PathGrid grid, int x1, int y1, int x2, int y2 );

    /**
     * Initializes and checks for obvious problems.
     */
    protected boolean hasProblem( PathGrid grid, int x1, int y1, int x2, int y2 )
    {
        boolean problem = false;

        /* Sanity check. */
        if ( grid == null || grid.getWidth() == 0 ) return true;

        /* do initializing. */
        mygrid = grid;
        doneGrid = new SimplePathGrid( mygrid.getWidth(), mygrid.getHeight() );

        if ( mygrid.getGrid( x1, y1 ) || mygrid.getGrid( x2, y2 ) ) problem = true;

        return problem;
    }

}//class
