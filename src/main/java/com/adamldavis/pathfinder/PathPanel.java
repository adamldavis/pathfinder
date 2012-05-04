/** Copyright 2012, Adam L. Davis. All rights reserved. */
package com.adamldavis.pathfinder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Displays a PathGrid object, allows the user
 * to draw obstacles, and dynamically uses a PathFinder 
 * to find a path through obstacles.
 * <p>
 * Left click to create obstacles. Then right click and drag to find a path.
 *  
 * @author <A HREF="http://www.adamldavis.com">Adam L. Davis</A>
 */
public class PathPanel extends JPanel
{
    public static final int LEFT_ = 3;

    public static final int DOWN_ = 2;

    public static final int RIGHT_ = 1;

    public static final int UP_ = 0;
    
    public static final String HELP_TEXT = 
        "Left click to create obstacles. Then right click and drag to find a path.";
    /*-----------------------------------------------------------------------*/
    // THE VIEW
    /*-----------------------------------------------------------------------*/

    /** bDEBUG = true if debugging */
    private static final boolean bDEBUG = false;

    /**
     * Size of each obstacle.
     */
    public static final int PIX = 3;

    /**
     * Space of each block including obstacle and interval spacing.
     */
    public static final int SPACE = 4;

    /** grid. */
    private PathGrid grid;

    private int xStart = 0;

    private int yStart = 0;

    private int[] path;

    int maxX;

    int maxY;

    public PathPanel()
    {
        path = null;
        refreshGrid( 0, 0 );
        newMouseAdapter();
    }

    private void newMouseAdapter()
    {
        this.addMouseListener( new MouseAdapter()
        {

            public void mousePressed( MouseEvent evt )
            {
                mousePress( evt );
            }

            public void mouseReleased( MouseEvent evt )
            {
                mouseRelease( evt );
            }
        } );
        this.addMouseMotionListener( new MouseMotionAdapter()
        {

            public void mouseDragged( MouseEvent evt )
            { //mouseClicked
                mouseDrag( evt );
            }

            public void mouseMoved( MouseEvent evt )
            {
            }
        } );
    }

    /**
     * Refreshes the size of the grid based on the given 
     * size in pixels of the panel.
     * @param w Width of space for panel in pixels.
     * @param h Height of space for panel in pixels.
     */
    public void refreshGrid( int w, int h )
    {
        PathGrid old = grid;
        int w2 = ( this.getWidth() / SPACE );
        int h2 = ( this.getHeight() / SPACE );
        grid = new SimplePathGrid( w2, h2 );
        int xmin = Math.min( w, w2 );
        int ymin = Math.min( h, h2 );

        //fill new array w/ old.
        for ( int i = 0; i < xmin; i++ )
            for ( int j = 0; j < ymin; j++ )
                grid.setGrid( i, j, old.getGrid( i, j ) );
        //fill in empty space if any w/ "false"
        if ( w2 > w ) for ( int i = w; i < w2; i++ )
            for ( int j = 0; j < h2; j++ )
                grid.setGrid( i, j, false );
        if ( h2 > h ) for ( int i = 0; i < w2; i++ )
            for ( int j = h; j < h2; j++ )
                grid.setGrid( i, j, false );
        //set new boundries of grid[][].
        maxX = w2;
        maxY = h2;
    }//refreshGrid

    /** Modifier. */
    public void setLife( int i, int j, boolean breath )
    {
        int x = ( i / SPACE );
        int y = ( j / SPACE );
        if ( ( x > 0 ) && ( y > 0 ) && ( x < maxX ) && ( y < maxY ) ) grid.setGrid( x, y, breath );
    }

    /**
     * Clears the board.
     */
    public void restart()
    {
        refreshGrid( 0, 0 );
    }

    /*-----------------------------------------------------------------------*/
    //The paint methods.
    /**
     * The paintComponent method. Overides paintComponent( Graphics g)
     * 
     * @param g
     *            The Graphics object that is used to paint on this panel.
     */
    public void paintComponent( Graphics g )
    {
        int i;
        int j;
        int w;
        int h;
        w = this.getWidth();
        h = this.getHeight();

        g.setColor( Color.black );
        g.fillRect( 0, 0, w, h );
        g.setColor( Color.green );

        if ( grid.getWidth() == 0 )
            refreshGrid( 0, 0 );
        else
            refreshGrid( maxX, maxY ); //refresh size of "grid"

        w /= SPACE;
        h /= SPACE;

        for ( i = 0; i < w; i++ )
            for ( j = 0; j < h; j++ )
                if ( grid.getGrid(i,j) ) g.fillRect( SPACE * i, SPACE * j, PIX, PIX );

        if ( path != null ) paintPath( path, g );
    }

    private void paintPath( int[] path, Graphics g )
    {
        int x = xStart;
        int y = yStart;
        if ( path.length <= 2 ) return;
        g.setColor( Color.red );
        g.fillRect( SPACE * x, SPACE * y, PIX, PIX );

        for ( int i = 0; i < path.length; i++ )
        {
            switch ( path[i] ) {
            case UP_:
                y--;
                break;
            case RIGHT_:
                x++;
                break;
            case DOWN_:
                y++;
                break;
            case LEFT_:
                x--;
                break;
            default:
            }
            g.fillRect( SPACE * x, SPACE * y, PIX, PIX );
        }
    }//paintPath

    /**
     * Called when the mouse is dragged.
     * 
     * @param evt
     *            The event.
     */
    public void mouseDrag( MouseEvent evt )
    {
        if ( bDEBUG )
        {
            System.out.println( "in mouseDrag: " + evt.paramString() );
        }
        if ( evt.getModifiers() > 10 )
        {
            int i = evt.getX();
            int j = evt.getY();
            this.setLife( i, j, true );
            this.repaint();
        }
    }//mouseDrag

    public void mousePress( MouseEvent evt )
    {
        if ( bDEBUG )
        {
            System.out.println( "in mousePress: " + evt.paramString() );
        }
        if ( evt.getModifiers() < 10 )
        {
            int i = ( evt.getX() / SPACE );
            int j = ( evt.getY() / SPACE );
            xStart = i;
            yStart = j;
        }

    }//mousePress

    public void mouseRelease( MouseEvent evt )
    {
        if ( bDEBUG )
        {
            System.out.println( "in mouseRelease: " + evt.paramString() );
        }
        if ( evt.getModifiers() < 10 )
        {
            int i = ( evt.getX() / SPACE );
            int j = ( evt.getY() / SPACE );
            if ( !inBounds( i, j ) ) return;

            //   distance must be less than x*y
            PathFinder pf = new AntPathFinder( maxX * maxY );
            path = pf.findPath( grid, xStart, yStart, i, j );
        }
        this.repaint();
    }//mouseRelease

    private boolean inBounds( int i, int j )
    {
        if ( i < 0 || j < 0 ) return false;
        if ( i > maxX || j > maxY ) return false;
        return true;
    }

    /*-----------------------------------------------------------------------*/
    /** 
     * Run this main to see test out PathFinders.
     * 
     */
    public static void main( String[] args )
    {
        JFrame f = new JFrame();
        PathPanel pp = new PathPanel();

        f.addWindowListener( new WindowAdapter()
        {
            public void windowClosing( WindowEvent e )
            {
                System.exit( 0 );
            }
        } );

        f.getContentPane().add( pp );
        f.setSize( 100, 120 );
        f.setVisible( true );
    }

} //PathPanel
