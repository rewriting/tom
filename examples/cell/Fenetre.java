/*
 *	Fenetre.java
 *	par Blaise Potard et Emmanuel Hainry
 *
 *	structure de la fenetre d'affichage
 */

package Cell;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Fenetre extends JFrame
{
    Fenetre(Affichage affi)
	{
	    super("Animation 2D");
	    	    addWindowListener(new WindowAdapter()
			      {
				  public void windowClosing(WindowEvent e)
				      {
					  System.exit(0);
				      }
			      });
	    getContentPane().setLayout(new BorderLayout());
	    Etat p1 = affi.e;
	    getContentPane().add(p1, BorderLayout.CENTER);
	    getContentPane().add(TDCA.zb, BorderLayout.EAST);
	    setLocation(50, 50);
	}
    
    //    	public void paint(Graphics g)
    //{
    //	setVisible(true);
    //}
}
	
