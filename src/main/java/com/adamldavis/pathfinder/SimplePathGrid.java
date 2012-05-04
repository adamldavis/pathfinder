/** Copyright 2012, Adam L. Davis. All rights reserved. */
package com.adamldavis.pathfinder;

/**
 * 
 * SimplePathGrid represents a boolean 2-dimensional grid.
 * 
 * @author <A HREF="http://www.adamldavis.com">Adam L. Davis</A>
 */
public class SimplePathGrid implements PathGrid
{

    private boolean[][] grid = null;

    private int width;

    private int height;

    public SimplePathGrid( boolean[][] g )
    {
        this.grid = g;
        setX( g.length );
        setY( g[0].length );
    }

    public SimplePathGrid( int x, int y )
    {
        this.width = x;
        this.height = y;
        grid = new boolean[x][y];
        for ( int i = 0; i < x; i++ )
            for ( int j = 0; j < y; j++ )
                grid[i][j] = false;
    }

    public boolean getGrid( int x, int y )
    {
        if ( x < 0 || y < 0 ) return true; //true means it is an obstacle.
        if ( x >= grid.length || y >= grid[0].length ) return true;
        return grid[x][y];
    }

    public void setGrid( int x, int y, boolean yes )
    {
        grid[x][y] = yes;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    private void setX( int x )
    {
        this.width = x;
    }

    private void setY( int y )
    {
        this.height = y;
    }

    public PathGrid copy()
    {
        PathGrid ret;
        boolean[][] g = new boolean[grid.length][grid[0].length];

        for ( int i = 0; i < g.length; i++ )
            for ( int j = 0; j < g[0].length; j++ )
                g[i][j] = grid[i][j];

        ret = new SimplePathGrid( g );
        return ret;
    }

    public String toString()
    {
        String str = "";
        for ( int j = 0; j < grid[0].length; j++ )
        {
            for ( int i = 0; i < grid.length; i++ )
            {
                if ( grid[i][j] )
                    str += "#";
                else
                    str += "-";
            }
            str += "\n";
        }
        return str;
    }
}//PathGrid
