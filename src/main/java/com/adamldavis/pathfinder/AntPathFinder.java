/** Copyright 2012, Adam L. Davis. All rights reserved. */
package com.adamldavis.pathfinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Use this one, it's the best. It attempts to find a route in
 * the most direct way possible, and keeps a list of other possibilities
 * (ant babies) to fall-back on if the current path fails.
 * 
 * @author <A HREF="http://www.adamldavis.com">Adam L. Davis</A>
 * @version Version 2.0, May 2, 2012
 */
public class AntPathFinder extends PathFinder
{

    private static final boolean bDEBUG = false;

    // number of ants
    private static final int MAX_SIZE = 1;

    public AntPathFinder()
    {
        super();
    }

    /**
     * Initializes this pathfinder with a maximum possible distance for a path.
     * @param maxDistance Maximum possible distance of a path in the grid.
     */
    public AntPathFinder( int maxDistance )
    {
        super( maxDistance );
    }

    /**
     * Finds a shortest path given an array and a starting point and end point.
     * Returns path in the form of an array of directions. <BR>
     * 0 = up, 1 = right, 2 = down, 3 = left.
     * 
     * @param grid
     *            True means it is an obstacle.
     */
    public int[] findPath( PathGrid grid, int x1, int y1, int x2, int y2 )
    {
        int size = 0; //number of ants.
        Path ret = null;
        boolean done = false;
        List ants = new ArrayList( 4 );
        List babies = new ArrayList( 100 );

        /* call the super. */
        if ( super.hasProblem( grid, x1, y1, x2, y2 ) ) return null;

        doneGrid.setGrid( x1, y1, true ); //set source as done.

        if ( x1 == x2 && y1 == y2 )
        {
            return ( new int[0] );
        } else
        {
            int[] order = makeOrder( x1, y1, x2, y2 );
            //for (int i=0;i<4;i++) {
            //ants.add(makeAnt(order[i],x1,y1,x2,y2));
            //}
            ants.add( makeAnt( order[0], x1, y1, x2, y2 ) );//<----
        }
        while ( !done )
        {
            size = ants.size();
            /*
             * Add babies to ants.
             */
            while ( size < MAX_SIZE && !babies.isEmpty() )
            {
                ants.add( ( AdamAnt ) babies.remove( 0 ) ); //babies.size()-1
                size++;
            }
            if ( bDEBUG ) System.out.println( "size= " + size );
            if ( bDEBUG ) System.out.println( "babies= " + babies.size() );
            if ( size == 0 ) break;
            for ( int i = 0; i < size; i++ )
            {
                AdamAnt a1 = null;
                AdamAnt aa = ( AdamAnt ) ants.get( i );
                int[] order = makeOrder( aa.getX(), aa.getY(), x2, y2 );

                //Set the direction, remove this for nibbles-like behaviour.
                aa.setDir( order[0] );
                /*
                 * Try to make ants going from this point in other directions.
                 */
                for ( int j = 0; j < 4; j++ )
                {
                    if ( order[j] != aa.getDir() )
                    {
                        a1 = ( AdamAnt ) aa.clone();
                        a1.setDir( order[j] );
                        if ( a1.move() ) babies.add( a1 );
                    }
                }
                /*
                 * Attempt to move. If can't move then remove it.
                 */
                if ( !aa.move() )
                {
                    ants.remove( i );
                    size--;
                }
                /*
                 * Check to see if we're finished.
                 */
                if ( aa.getX() == x2 && aa.getY() == y2 )
                {
                    done = true;
                    ret = aa.getPath();
                    break;
                }
            }//i
        }//while
        if ( bDEBUG ) System.out.println( "ant.path = " + ret );

        if ( ret == null )
            return null;
        else
            return ( ret.getArray() );
    }

    private AdamAnt makeAnt( int i, int x1, int y1, int x2, int y2 )
    {
        return ( new AdamAnt( x1, y1, i, mygrid, doneGrid ) );
    }

    private int[] makeOrder( int x1, int y1, int x2, int y2 )
    {
        int[] ret = new int[4];
        int xadd = 0;
        int yadd = 0;
        if ( Math.abs( x2 - x1 ) > Math.abs( y2 - y1 ) )
            xadd = 1;
        else
            yadd = 1;
        if ( x2 > x1 )
        {
            ret[1 - xadd] = 1;
            ret[2 + xadd] = 3;
        } else
        {
            ret[1 - xadd] = 3;
            ret[2 + xadd] = 1;
        }
        if ( y2 > y1 )
        {
            ret[1 - yadd] = 2;
            ret[2 + yadd] = 0;
        } else
        {
            ret[1 - yadd] = 0;
            ret[2 + yadd] = 2;
        }
        return ret;
    }

}//END OF CLASS AntPathFinder

class AdamAnt extends Path
{

    private static PathGrid mygrid;

    private static PathGrid doneGrid;

    int x = 0;

    int y = 0;

    int dir = 0;

    public AdamAnt( int x, int y, int d, PathGrid mygrid, PathGrid dgrid )
    {
        super();
        this.x = x;
        this.y = y;
        this.dir = d;
        AdamAnt.mygrid = mygrid;
        AdamAnt.doneGrid = dgrid;
    }

    public boolean move()
    {
        boolean moved = false;
        int x1 = x;
        int y1 = y;
        switch ( dir ) {
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
        if ( isOkay( x1, y1 ) )
        {
            moved = true;
            doneGrid.setGrid( x1, y1, true ); //<--the space it's on.
            x = x1;
            y = y1;
            super.add( dir );
        }
        return moved;
    }//move

    protected boolean isOkay( int i, int j )
    {
        boolean okay = true;
        if ( mygrid.getGrid( i, j ) || doneGrid.getGrid( i, j ) ) okay = false;
        return okay;
    }

    public Object clone()
    {
        AdamAnt ant = new AdamAnt(x,y,dir,mygrid,doneGrid);
	
        for (int i=0; i < this.size(); i++)
        {
            ant.add(this.get(i));
        }
        return ant;
    }

    public void setDir( int d )
    {
        dir = d;
    }

    public int getDir()
    {
        return dir;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void setX( int n )
    {
        x = n;
    }

    public void setY( int n )
    {
        y = n;
    }

    public Path getPath()
    {
        return ( Path ) this;
    }
}//AdamAnt
