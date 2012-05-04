/*
 * Created on May 12, 2005 by Adam. 
 * Interface for "SimplePathGrid"
 *
 */
package com.adamldavis.pathfinder;

/**
 * Interface for 2D grids of boolean values.
 * True means it is an obstacle.
 *
 * @author <A HREF="http://www.adamldavis.com">Adam L. Davis</A>
 */
public interface PathGrid
{

	/** True means it is an obstacle. */
    public boolean getGrid( int x, int y );

    public void setGrid( int x, int y, boolean yes );

    public int getWidth();

    public int getHeight();

    public PathGrid copy();

    public String toString();
}