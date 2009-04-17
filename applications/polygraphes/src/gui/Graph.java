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

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Graph extends JPanel {
	int debug = 0;
	private ArrayList<Object> elts;
	private int controle;
	
	public Graph(){
		this.elts = new  ArrayList<Object>();
	}
	
	public ArrayList<Object> getGraphObjectList(){
		return this.elts;	
	}
	
	public void ajouterElement(Object o){
		this.elts.add(o);		
		if(o instanceof TwoCell){
			Iterator<OneCell> itfil = XMLhandlerGui.getListeOneCellSource(((TwoCell)o)).iterator();
			while (itfil.hasNext()){
				this.ajouterElement((OneCell)itfil.next());
				controle++;
			}
			Iterator<OneCell> itfil2 = XMLhandlerGui.getListeOneCellTarget(((TwoCell)o)).iterator();
			while (itfil2.hasNext()){
				this.ajouterElement((OneCell)itfil2.next());
				controle++;
			}
		}else if(o instanceof TwoId){
			Iterator<OneCell> itfil = XMLhandlerGui.getListeOneCellSource(((TwoId)o)).iterator();
			while (itfil.hasNext()){
				this.ajouterElement((OneCell)itfil.next());
				controle++;
			}
		}
		controle++;
		//System.out.println("CONTROLE :"+controle);
	}
	
	/** Add Component Without a Layout Manager (Absolute Positioning) */ 
 	@SuppressWarnings("unused")
	private void addComponent(Container container,Component c,int x,int y,int width,int height) 
 	{ 
 		c.setBounds(x,y,width,height); 
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
 				// this.addComponent(this, (Fil)tmp, ((Fil)tmp).getmX(), ((Fil)tmp).getmY(), ((Fil)tmp).getmWidth(), ((Fil)tmp).getmHeight());
 				g2.setColor(Color.BLUE);
 				g2.drawLine(tmp2.getx(),tmp2.gety(),tmp2.getx(), tmp2.gety()+tmp2.gethauteur());
 				g2.setColor(Color.GREEN);
 				g2.drawString(tmp2.getName(),tmp2.getX()-(tmp2.getLargeur()/2),tmp2.getY()+(tmp2.getHauteur()/2));
 				  //System.out.println("Fil : X = " + tmp2.getmX() + " Y = " + tmp2.getmY() + " Width = " + tmp2.getmWidth() + " Height = " + tmp2.getmHeight());
 			} else if (tmp instanceof TwoCell){
 				
 				TwoCell tmp2 = (TwoCell)tmp;
 				//this.addComponent(this, (DeuxCellules)tmp, ((DeuxCellules)tmp).getmX(), ((DeuxCellules)tmp).getmY(), ((DeuxCellules)tmp).getmWidth(), ((DeuxCellules)tmp).getmHeight());
 				g2.setColor(Color.RED);
 				//g2.drawRect(((Double)tmp2.getRec().getX()).intValue(), ((Double)tmp2.getRec().getY()).intValue(), ((Double)tmp2.getRec().getWidth()).intValue(), ((Double)tmp2.getRec().getHeight()).intValue());
 				g2.drawRect(tmp2.getx(),tmp2.gety()+20,tmp2.getLargeur(),tmp2.getHauteur()-40);
 				g2.setColor(Color.black);
			    // get metrics from the graphics
			    FontMetrics metrics = g2.getFontMetrics(this.getFont());
			    // get the height of a line of text in this font and render context
			    int hgt = metrics.getHeight();
			    // get the advance of my text in this font and render context
			    int adv = metrics.stringWidth(tmp2.getName());
			    // calculate the size of a box to hold the text with some padding.
			    //Dimension size = new Dimension(adv+2, hgt+2);
			    g2.drawString(tmp2.getName(),(tmp2.getLargeur()- adv)/2+4+tmp2.getX(),(tmp2.getHauteur()+hgt)/2-4+tmp2.getY());
			    
			    //Debug contour de l'objet 2 cellules car impossible de faire pour composition
				/*RandomColor colorGen = new RandomColor();

				// affichage des zones de contact
				Iterator<Integer> contactit = tmp2.getContactHaut().getListContacts().iterator();
				while (contactit.hasNext()) {
					int tmpct = (int) (contactit.next());
					drawCircle(g2,tmp2.getMX() + tmpct, tmp2.getMY(),3);
				}
				contactit = tmp2.getContactBas().getListContacts().iterator();
				while (contactit.hasNext()) {
					int tmpct = (int) (contactit.next());
					drawCircle(g2,tmp2.getMX() + tmpct, tmp2.getMY()+tmp2.getMHeight(),3);
				}

				g2.setColor(colorGen.randomColor());
				//g2.drawRect(tmp2.getRectangle().x, tmp2.getRectangle().y, tmp2.getRectangle().width, tmp2.getRectangle().height);
			  	
			    System.out.println("DeuxCellules : Nom = " + tmp2.getNom()+ " X = " + tmp2.getMX() + " Y = " + tmp2.getMY() + " Width = " + tmp2.getMWidth() + " Height = " + tmp2.getMHeight());*/
 			}/*
 			else if ((tmp instanceof Composition0) || (tmp instanceof Composition1)){
 				//juste du debug
 				StructureComposition tmpstruct = (StructureComposition)tmp;
 				//g2.setStroke(s)
 				g2.setColor(Color.green);
 				g2.drawRect(tmpstruct.getRectangle().x, tmpstruct.getRectangle().y ,tmpstruct.getRectangle().width, tmpstruct.getRectangle().height);
	
 			}*/
 		}
 	}
 	
    // Convenience method to draw from center with radius
    public void drawCircle(Graphics cg, int xCenter, int yCenter, int r) {
        cg.drawOval(xCenter-r, yCenter-r, 2*r, 2*r);
    }//end drawCircle

    
 	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    //System.out.println("Appel de Paintcomponent numero  " +debug);
	    debug++;
	    buildGraph(g);
	  }
}
