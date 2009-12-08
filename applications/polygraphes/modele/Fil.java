package modele;
import java.awt.Rectangle;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class Fil extends JComponent implements Structure{ 
	private int mWidth;
	private int mHeight;
	private int mX;
	private int mY;
	private int ctrlx; //coord x du point central de la courve
	private int ctrly; //idem en y
		
	public Fil(){
		this.mX = 0;
		this.mY = 0;
		this.ctrlx = 0;
		this.ctrly = 0;
		this.mWidth = 1; //deux pixels d'épaisseurs pour commencer
		this.mHeight =20;	
	}
	
	public int getMHeight(){
		
		return this.mHeight;
	}
	
	public int getMWidth(){
		return this.mWidth;
	}
	
	public int getMX(){
		return this.mX;
	}
	
	public int getMY(){
		return this.mY;
	}
	
	public void setMWidth(int width){
		this.mWidth = width;	
	}
	
	public void setMHeight(int height){
		this.mHeight = height;
	}
	
	public void setMX(int x){
		this.mX = x;
	}	
	
	public void setMY(int y){
		this.mY = y;
	}

	@Override
	public void setNom(String tmp) {
		// TODO Auto-generated method stub
		
	}
	
	public int getCtrlx() {
		return ctrlx;
	}

	public void setCtrlx(int ctrlx) {
		this.ctrlx = ctrlx;
	}

	public int getCtrly() {
		return ctrly;
	}

	public void setCtrly(int ctrly) {
		this.ctrly = ctrly;
	}

	@Override
	public void allongerFils(int taillehaut) {
		this.setMHeight(this.getMHeight()+taillehaut);
		
	}

	@Override
	public void MAJFil() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Rectangle getRectangle() {
		// TODO Auto-generated method stub
		return null;
	}
	
	  /*public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    MAJ();
	    g.setColor(Color.BLUE);
	    g.drawLine(0,0,0,this.mHeight);
	    g.setColor(Color.GREEN);
	   // g.drawRect(epsilon,epsilon,longueur,hauteur);
	  }*/
}
