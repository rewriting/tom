package modele;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Composition0 implements Structure,StructureComposition{
	private int epsilon = 4;
	private int mX;
	private int mY;
	private int mWidth;
	private int mHeight;
	private Rectangle rec;
	
	private Structure f;
	private Structure g;
	
	public Composition0(Structure f, Structure g){
		this.f=f;
		this.g=g;
		this.rec = new Rectangle();
		System.out.println("DEBUG Compo 0");
		mX=f.getMX();
		mY=f.getMY();
		
		mWidth=f.getMWidth()+epsilon+g.getMWidth();
		g.setMX(f.getMX()+ f.getMWidth() + epsilon);
	
		if(f.getMHeight()> g.getMHeight()){	
			System.out.println("F grand");
			mHeight=f.getMHeight();
			int difftaille = f.getMHeight() - g.getMHeight();
			g.allongerFils(difftaille);
			this.rec.height = f.getRectangle().height;
		}else{
			System.out.println("G grand");
			mHeight=g.getMHeight();
			int difftaille = g.getMHeight() - f.getMHeight();
			f.allongerFils(difftaille);
			this.rec.height = g.getRectangle().height;
		}
		this.rec.width = mWidth;
		this.rec.x = f.getMX();
		this.rec.y = f.getMY();
	}

	public int getMX() {
		return mX;
	}

	public void setMX(int mx) {
		mX = mx;
		f.setMX(mx);
		g.setMX(mx);
	}

	public int getMY() {
		return mY;
	}

	public void setMY(int my) {
		mY = my;
		f.setMY(my);
		g.setMY(my);
	}

	public int getMWidth() {
		return mWidth;
	}

	public int getMHeight() {
		return mHeight;
	}

	@Override
	public void allongerFils(int taillehaut) {
		f.allongerFils(taillehaut);
		g.allongerFils(taillehaut);
	}

	@Override
	public void setNom(String tmp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMHeight(int height) {
		
	}

	@Override
	public void setMWidth(int width) {
		g.setMWidth(width-f.getMWidth()-4);
		
	}

	@Override
	public Structure getF() {
		return f;
	}

	@Override
	public Structure getG() {
		return g;
	}

	@Override
	public ArrayList<Fil> recupererFils(Structure s, boolean b) {
		if(s instanceof DeuxCellules){
			return ((DeuxCellules)s).getListFils(b);
		}else{
			if(s instanceof Composition0){
				ArrayList<Fil> tmp=new ArrayList<Fil>();
				
				tmp.addAll(((StructureComposition)s).recupererFils(((StructureComposition)s).getF(),b));
				tmp.addAll(((StructureComposition)s).recupererFils(((StructureComposition)s).getG(),b));
				return tmp;
			}else{
				return ((StructureComposition)s).recupererFils(((StructureComposition)s).getG(),b);
			}
		}
	}

	public void MAJFil(){
		f.MAJFil();
		g.MAJFil();
	}

	@Override
	public Rectangle getRectangle() {
		return this.rec;
	}

}
