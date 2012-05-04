/*
 * Created on May 12, 2005 by Adam. 
 *
 */
package com.adamldavis.pathfinder;

import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.JApplet;


/**
 * Displays a PathGrid object, allows the user
 * to draw obstacles, and dynamically uses a PathFinder 
 * to find a path through obstacles.
 * 
 * @author <A HREF="http://www.adamldavis.com">Adam L. Davis</A>
 * @version Version 1.0, May 1, 2005
 */
public class PathFinderApplet extends JApplet
{

    PathPanel panel = null;
    /**
     * @throws java.awt.HeadlessException
     */
    public PathFinderApplet() throws HeadlessException
    {
        super();
    }
    
    /**
     * @see java.applet.Applet#destroy()
     */
    public void destroy()
    {
        super.destroy();
        panel = null;
    }
    /**
     * @see java.applet.Applet#init()
     */
    public void init()
    {
        super.init();
        panel = new PathPanel();
    }
    /**
     * @see java.awt.Component#resize(java.awt.Dimension)
     */
    public void resize( Dimension dim )
    {
        super.resize( dim );
        this.panel.refreshGrid(dim.width, dim.height);
    }
    /**
     * @see java.awt.Component#resize(int, int)
     */
    public void resize( int w, int h )
    {
        super.resize( w, h );
        this.panel.refreshGrid(w, h);
    }
    /**
     * @see java.applet.Applet#start()
     */
    public void start()
    {
        super.start();
    }
    /**
     * @see java.applet.Applet#stop()
     */
    public void stop()
    {
        super.stop();
    }
}
