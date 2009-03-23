package modele;

import java.awt.Rectangle;

public interface Structure {

	public void setNom(String tmp);
	
	public int getMX();

	public int getMY();

	public int getMWidth();

	public int getMHeight();
	
	public void setMX(int mx);

	public void setMY(int my);
	
	public void allongerFils(int taillehaut);
	
	public void setMWidth(int width);
	
	public void setMHeight(int height);
	
	public void MAJFil();
	
	public Rectangle getRectangle();
}
