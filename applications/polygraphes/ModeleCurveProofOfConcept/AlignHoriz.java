/*package modele;

import java.awt.Graphics;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class AlignHoriz extends JPanel{
	private int mX;
	private int mY;
	private int mWidth;
	private int mHeight;
	private int epsilon = 4;
	
	private JPanel f;
	private JPanel g;
	
	
	public AlignHoriz(JPanel f,JPanel g){
		//super(new FlowLayout());
		this.setLayout(null);
		this.f=f;
		this.g=g;
		if(f instanceof DeuxCellules && g instanceof DeuxCellules){
			mX=((DeuxCellules)f).getMX();
			mY=((DeuxCellules)f).getMY();
			mWidth=((DeuxCellules)f).getMWidth()+epsilon+((DeuxCellules)g).getMWidth();
			if(((DeuxCellules)f).getMHeight()>=((DeuxCellules)g).getMHeight()){
				System.out.println("F grand");
				mHeight=((DeuxCellules)f).getMHeight();
				int difftaille=((DeuxCellules)f).getMHeight()-((DeuxCellules)g).getMHeight();
				((DeuxCellules)g).allongerFils(difftaille);
			}else{
				System.out.println("G grand");
				mHeight=((DeuxCellules)g).getMHeight();
				int difftaille=((DeuxCellules)g).getMHeight()-((DeuxCellules)f).getMHeight();
				((DeuxCellules)f).allongerFils(difftaille);
			}
		}
		((DeuxCellules)g).setEspaceX(((DeuxCellules)f).getMX()+((DeuxCellules)f).getMWidth()+epsilon);
		...etc etc...
		System.out.println("F :"+((DeuxCellules)f).getMX());
		System.out.println("G :"+((DeuxCellules)g).getMX());
		((DeuxCellules)g).setMX(((DeuxCellules)f).getMX()+epsilon+((DeuxCellules)f).getMWidth());
		System.out.println("F :"+((DeuxCellules)f).getMX());
		System.out.println("G :"+((DeuxCellules)g).getMX());
		
		
		this.add(f);
		this.add(g);
		this.setSize(800, 800);
	}
	
	public void paintComponent(Graphics gr) {
		super.paintComponent(gr);
		if(f instanceof DeuxCellules && g instanceof DeuxCellules){
			mX=((DeuxCellules)f).getMX();
			mY=((DeuxCellules)f).getMY();
			mWidth=((DeuxCellules)f).getMWidth()+epsilon+((DeuxCellules)g).getMWidth();
			if(((DeuxCellules)f).getMHeight()>=((DeuxCellules)g).getMHeight()){
				System.out.println("F grand");
				mHeight=((DeuxCellules)f).getMHeight();
				int difftaille=((DeuxCellules)f).getMHeight()-((DeuxCellules)g).getMHeight();
				((DeuxCellules)g).allongerFils(difftaille);
			}else{
				System.out.println("G grand");
				mHeight=((DeuxCellules)g).getMHeight();
				int difftaille=((DeuxCellules)g).getMHeight()-((DeuxCellules)f).getMHeight();
				((DeuxCellules)f).allongerFils(difftaille);
			}
		}
		((DeuxCellules)g).setEspaceX(((DeuxCellules)f).getMX()+((DeuxCellules)f).getMWidth()+epsilon);
		...etc etc...
		System.out.println("F :"+((DeuxCellules)f).getMX());
		System.out.println("G :"+((DeuxCellules)g).getMX());
		((DeuxCellules)g).setMX(((DeuxCellules)f).getMX()+epsilon+((DeuxCellules)f).getMWidth());
		System.out.println("F :"+((DeuxCellules)f).getMX());
		System.out.println("G :"+((DeuxCellules)g).getMX());
		//this.setSize(mWidth, mHeight);
	}
	
	
	
}*/
