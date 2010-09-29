package modele;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import modele.Fil.typec;

public class Composition1 implements Structure,StructureComposition{
	private int epsilon = 4;
	private int mX;
	private int mY;
	private int mWidth;
	private int mHeight;
	private Rectangle rec;
	
	private Structure f;
	private Structure g;
	
	public Composition1(Structure f, Structure g) {
		this.f = f;
		this.g = g;
		this.rec = new Rectangle();
		System.out.println("DEBUG Compo 1");
		mX = f.getMX(); // on récupere les coordonnées de la cellule la plus
						// haute cad f -> coord de la compo 1
		mY = f.getMY();
		
		//On divise la taille des fils par deux pour avoir un espace equivalent a un fil au final ??
		mHeight = f.getMHeight() + g.getMHeight(); // hauteur de la compo
		g.setMY(f.getMY() + f.getMHeight());		//Ajustement Y de g
//		if (f.getMWidth() != g.getMWidth()) {				//Si les tailles sont identiques pas la peine de changer la largeur/longueur
//			if (f.getMWidth() > g.getMWidth()) {
//				System.out.println("F large");
//				mWidth = f.getMWidth();
//				if(g instanceof Composition0) this.setMWidthComposition0(f.getMWidth()); //arrangement spécial
//				else g.setMWidth(f.getMWidth());
//				this.MAJFil();
//				
//				this.rec.width = f.getRectangle().width;
//			} else {
//				System.out.println("G grand");
//				mWidth = g.getMWidth();
//				f.setMWidth(g.getMWidth());
//				this.MAJFil();
//				
//				this.rec.width = g.getRectangle().width;
//			}
//		}
		this.MAJFil();
		coordonnerFil();
		
		this.rec.height = mHeight;
		this.rec.x = f.getRectangle().x;
		this.rec.y = f.getRectangle().y;
	}

	public ArrayList<Fil> recupererFils(Structure s,boolean b){
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
	
	//|--------|
	//|   F    |
	//|--------|
	//|--------|
	//|   G    |
	//|--------|
	//Permet de coordonner les fils au niveau de X
	public void coordonnerFil(){
		ArrayList<Fil> listebas = new ArrayList<Fil>(); //on recupere les fils bas de f
		listebas = this.recupererFils(f, false);
		Iterator<Fil> itFBas = listebas.iterator();
		ArrayList<Fil> listehaut = new ArrayList<Fil>(); //on recupere les fils haut de g
		listehaut = this.recupererFils(g, true);
		Iterator<Fil> itGHaut = listehaut.iterator();
		while (itFBas.hasNext() && itGHaut.hasNext()) { //on adapte les fils haut de G aux positions X de F
			Fil fGHaut = (Fil) itGHaut.next();
			Fil fFBas = (Fil) itFBas.next();
			//fGHaut.setMX(fFBas.getMX());
			int separationDistance = (fFBas.getMX()+fFBas.getMWidth()) - fGHaut.getMX();
			if (separationDistance < 0){
				separationDistance = - separationDistance;
			}
			fFBas.setMTypeC(typec.BOTTOMRIGHT);
			fGHaut.setMTypeC(typec.TOPLEFT);
			fGHaut.setMWidth(separationDistance/2);
			fFBas.setMWidth(separationDistance/2);
			fFBas.setMX(fFBas.getMX() - fFBas.getMWidth());
		}
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
	
	// METHODE POUR LE CAS SUIVANT UNIQUEMENT :
	//
	// |--------|
	// |   F    |  //F peut être n'importe quoi (DeuxCellules ou StructureComposition)
	// |--------|
	// |---||---|
	// | G1|| G2|
	// |---||---|
	//
	//Traitement spécial pour le cas où le bas de la composition est de type composition0
	//En effet, dans ce cas, la taille des 2 Deuxcellules de la composition0 depend du nombre de fils
	// de ces cellules.
	//
	// NE PAS UTILISER DANS TOUT AUTRE CAS!!!!!
	
	public void setMWidthComposition0(int width){
		System.out.println("TRAITEMENT");
		//On récupere les fils bas de f de la composition1
		ArrayList<Fil> f1liste;
		if(f instanceof StructureComposition){
			 f1liste = this.recupererFils(((StructureComposition)f).getF(),false);
			 f1liste.addAll(this.recupererFils(((StructureComposition)f).getG(),false));
		}else{
			 f1liste = ((DeuxCellules)f).getListFils(false);
		}
		
		// on recupere les fils de G1
		ArrayList<Fil> gliste = this.recupererFils(((StructureComposition)g).getF(),true);
		//on donne à sep, la position en X du dernier fil qui doit être connecté à G1
		int sep = f1liste.get(gliste.size()-1).getMX(); 
		System.out.println(sep);
		((Composition0)g).getF().setMWidth(sep+epsilon); //on donne à G1, la taille voulu
		((Composition0)g).getG().setMX(sep+2*epsilon); //on deplace G2 vers la gauche de G1
		((Composition0)g).getG().setMWidth(width-sep-2*epsilon); //on donne à G2 la taille du "reste"
	}

	@Override
	public Structure getF() {
		return f;
	}

	@Override
	public Structure getG() {
		return g;
	}
	
	public void MAJFil(){
		g.MAJFil();
	}

	@Override
	public Rectangle getRectangle() {
		return this.rec;
	}

}
