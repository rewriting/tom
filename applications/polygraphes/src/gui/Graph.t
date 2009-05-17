package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import adt.polygraphicprogramgui.types.*;
import adt.polygraphicprogramgui.types.onepath.*;
import adt.polygraphicprogramgui.types.twopath.*;
import javax.swing.JScrollPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Graph extends JPanel {
	int debug = 0;
	private ArrayList<Object> elts;

	public Graph() {
		this.setLayout(null); // on enleve le layout pour pouvoir afficher les
								// objets en coordonnees
		this.elts = new ArrayList<Object>();
	}

	public ArrayList<Object> getGraphObjectList() {
		return this.elts;
	}

	public void ajouterElement(Object o) {
		this.elts.add(o);
		if (o instanceof TwoCell) {
			Iterator<OnePath> itfil = XMLhandlerGui.getListeOnePathSource(
					((TwoPath) o)).iterator();
			while (itfil.hasNext()) {
				OnePath op = (OnePath) itfil.next();
				if (op instanceof OneCell)
					this.ajouterElement(op);
			}
			Iterator<OnePath> itfil2 = XMLhandlerGui.getListeOnePathTarget(
					((TwoPath) o)).iterator();
			while (itfil2.hasNext()) {
				OnePath op = (OnePath) itfil2.next();
				if (op instanceof OneCell)
					this.ajouterElement(op);
			}
		} else if (o instanceof TwoId) {
			Iterator<OnePath> itfil = XMLhandlerGui.getListeOnePathSource(
					((TwoId) o)).iterator();
			while (itfil.hasNext()) {
				OnePath op = (OnePath) itfil.next();
				if (op instanceof OneCell)
					this.ajouterElement(op);
			}
		}
	}

	/** Add Component Without a Layout Manager (Absolute Positioning) */
	@SuppressWarnings("unused")
	private void addComponent(Container container, Component c, int x, int y,
			int width, int height) {
		c.setBounds(x, y, width, height);
		container.add(c);
	}

	public void buildGraph(Graphics g){
 		
 		Graphics2D g2 = (Graphics2D) g; 		//apparamment plus optimise
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    
 		Iterator<Object> it = this.elts.iterator();
 		while (it.hasNext()){
 			Object tmp = it.next();
 			if (tmp instanceof OneCell){
 				OneCell tmp2 = (OneCell)tmp;
 				
 				g2.setColor(Color.BLUE);
 				g2.drawLine(tmp2.getx(),tmp2.gety(),tmp2.getx(), tmp2.gety()+tmp2.gethauteur());
 				
 				g2.setColor(Color.BLACK);
 				FontMetrics metrics = g2.getFontMetrics(this.getFont());
			    int hgt = metrics.getHeight();
			    int adv = metrics.stringWidth(tmp2.getName());
			 	g2.drawString(tmp2.getName(),tmp2.getX()+(tmp2.getLargeur()/2)-(adv/2)-2,tmp2.getY()+(tmp2.getHauteur()/2)+(hgt/2));
 			} else if (tmp instanceof TwoCell){
 				TwoCell tmp2 = (TwoCell)tmp;
 				
 				//Cellule
 				g2.setColor(Color.RED);
 				g2.drawRect(tmp2.getx(),tmp2.gety()+VarGlobale.filhauteurdefaut,tmp2.getLargeur(),tmp2.getHauteur()-(VarGlobale.filhauteurdefaut*2));
 				
 				//Texte
 				g2.setColor(Color.BLACK);	
			    FontMetrics metrics = g2.getFontMetrics(this.getFont());
			    int hgt = metrics.getHeight();
			    int adv = metrics.stringWidth(tmp2.getName());
			    g2.drawString(tmp2.getName(),(tmp2.getLargeur()- adv)/2+4+tmp2.getX(),(tmp2.getHauteur()+hgt)/2-4+tmp2.getY());
 			}
 		}
	}

	// method to draw from center with radius
	public void drawCircle(Graphics cg, int xCenter, int yCenter, int r) {
		cg.drawOval(xCenter - r, yCenter - r, 2 * r, 2 * r);
	}// end drawCircle

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//System.out.println("Appel de Paintcomponent numero  " + debug);
		debug++;
		buildGraph(g);
	}
}
