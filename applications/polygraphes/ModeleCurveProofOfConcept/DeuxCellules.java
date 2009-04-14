package modele;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

public class DeuxCellules implements Structure {
	private Contact ContactHaut;
	private Contact ContactBas;
	private int mX;
	private int mY;
	private int mWidth;
	private int mHeight;
	private String nom;
	private int epsilon = 4;
	private ArrayList<Fil> listFilsHaut;
	private ArrayList<Fil> listFilsBas;
	private Rectangle rec; //rectangle de l'objet Deuxcellules donc cellule plus fils
	
	public DeuxCellules(String nom,int nbhaut, int nbbas){
		this.ContactBas = new Contact();
		this.ContactHaut = new Contact();
		this.mWidth = 100;
		this.mHeight = 30;
		this.mX = 0;
		this.mY = 0; //on part du fait que les fils font 20 pixels en hauteur
		this.rec = new Rectangle(0,21,100,30);
		this.nom = nom;
		this.listFilsHaut = new ArrayList<Fil>();
		this.listFilsBas = new ArrayList<Fil>();
		this.ajouterFil(nbhaut, nbbas);
	}
	
	public Contact getContactHaut() {
		return ContactHaut;
	}

	public void setContactHaut(Contact contactHaut) {
		ContactHaut = contactHaut;
	}

	public Contact getContactBas() {
		return ContactBas;
	}

	public void setContactBas(Contact contactBas) {
		ContactBas = contactBas;
	}

	public String getNom(){
		return this.nom;
	}
	
	//Boolean = true --> fils haut | false --> fils bas
	public ArrayList<Fil> getListFils(boolean haut){
		if (haut == true){
			return this.listFilsHaut;
		} else {
			return this.listFilsBas;
		}
		
	}
	public void setNom(String tmp){
		this.nom = tmp;
	}
	
	public int getMX() {
		return mX;
	}

	public int getMY() {
		return mY;
	}

	public int getMWidth() {
		return mWidth;
	}

	public int getMHeight() {
		return mHeight;
	}
	
	public void setMWidth(int width) {
		rec.width=width;
		mWidth = width;
	}

	public void setMHeight(int height) {
		rec.height=height;
		mHeight = height;
	}

	public void setMX(int mx){
		deplacerFilsX(-this.mX);
		deplacerFilsX(mx);
		mX = mx;
		this.rec.x=mx;
	}

	public void setMY(int my) {
		deplacerFilsY(-this.mY);
		deplacerFilsY(my+20);
		mY = my;
		this.rec.y=my+20;
	}

	private void ajouterFil(int nbhaut, int nbbas){
		int i=0;
		int h=0;
		
		//ajout des fils
		while (i<nbhaut){
			int epsilonhaut = ((mWidth-(3*epsilon))/(nbhaut-1)); //ecart entre les fils
			
			Fil f = new Fil();									
			f.setMX(this.mX + epsilon * 2 + epsilonhaut * i);
			f.setMY(this.mY);				 //-1 pour eviter que les bords se supperposent
			this.ContactHaut.ajouterContact(f.getMX());		 //ajouter la zone de contact
			this.listFilsHaut.add(f);
			i++;
			h=20;
			
		}
		i=0;
		if(h>0) this.mHeight=this.mHeight+20;
		int b=0;
		while(i<nbbas){
			int epsilonbas = ((mWidth-(3*epsilon))/(nbbas-1));
			Fil f = new Fil();
			f.setMX(this.mX + epsilon * 2 + epsilonbas * i);
			f.setMY(this.mY+((Double)rec.getHeight()).intValue()+h);
			this.ContactBas.ajouterContact(f.getMX());
			this.listFilsBas.add(f);
			i++;
			b=1;
		}
		if(b>0) this.mHeight=this.mHeight+20;
	}
	
	public Rectangle getRec() {
		return rec;
	}

	public boolean cleanContactsHaut(){
	    //Clear ou pas des list Contacts
		if (this.listFilsHaut.size() != 0) { //on refait les contacts haut
			this.ContactHaut.clear();
			return true;
		} else {
			return false;	
		}
	}
	
	public boolean cleanContactsBas(){
		if (this.listFilsBas.size() != 0) { //on refait les contacts haut
			this.ContactBas.clear();
			return  true;
		} else {
			return false;
		}
	} 	
	
	public void MAJFil(){
		int i=0;
		int h=0;
		boolean tmph = cleanContactsHaut();
		boolean tmpb = cleanContactsBas();
		
		while (i<listFilsHaut.size()){
			int epsilonhaut = ((mWidth-(3*epsilon))/(listFilsHaut.size()-1));
			Fil f = listFilsHaut.get(i);
			f.setMX(this.mX + epsilon * 2 + epsilonhaut * i);
			f.setMY(this.mY);
			i++;
			h=20;
			if (tmph == true){
	    		this.ContactHaut.ajouterContact(f.getMX()); //violent oui je sais
	    	}
		}
		
		i=0;
		while(i<listFilsBas.size()){
			int epsilonbas = ((mWidth-(3*epsilon))/(listFilsBas.size()-1));
			Fil f = listFilsBas.get(i);
			f.setMX(this.mX + epsilon * 2 + epsilonbas * i);
			f.setMY(this.mY+((Double)rec.getHeight()).intValue()+h);
			i++;
			if (tmpb == true){
	    		this.ContactBas.ajouterContact(f.getMX()); //violent oui je sais
	    	}
		}
	}
	
	public void allongerFils(int taillehaut){
		Iterator<Fil> it = this.listFilsHaut.iterator();
	    Iterator<Fil> it2 = this.listFilsBas.iterator();
		boolean tmph = cleanContactsHaut();
		boolean tmpb = cleanContactsBas();
		
		while (it.hasNext()){
	    	Fil tmp = (Fil)it.next();
	    	tmp.setMHeight(tmp.getMHeight()+taillehaut);
	    	if (tmph == true){
	    		this.ContactHaut.ajouterContact(tmp.getMX()); //violent oui je sais
	    	}
	    }
	    //this.mY=this.mY+taillehaut;
	    rec.y=rec.y+taillehaut;
	    
	    while (it2.hasNext()){
	    	Fil tmp = (Fil)it2.next();
	    	tmp.setMY(tmp.getY()+taillehaut+52);
	    	if (tmpb == true){
	    		this.ContactBas.ajouterContact(tmp.getMX()); //violent oui je sais
	    	}
	    }
	}
	
	public void deplacerFilsX(int deplacementX){
		Iterator<Fil> it;
		boolean tmph = cleanContactsHaut();
		boolean tmpb = cleanContactsBas();
	 		
		//deplacement des fils proporement dit
		it = this.listFilsHaut.iterator();
	    while (it.hasNext()){
	    	Fil tmp = (Fil)it.next();
	    	tmp.setMX(tmp.getMX() + deplacementX);
	    	if (tmph == true){
	    		this.ContactHaut.ajouterContact(tmp.getMX()); //violent oui je sais
	    	}
	    }
	    
	    it = this.listFilsBas.iterator();
	    while (it.hasNext()){
	    	tmpb = true;
	    	Fil tmp = (Fil)it.next();
	    	tmp.setMX(tmp.getMX() + deplacementX);
	    	if (tmpb == true){
	    		this.ContactBas.ajouterContact(tmp.getMX()); //violent oui je sais
	    	}
	    }
	   
	}
	
	
	public void deplacerFilsY(int deplacementX){
		Iterator<Fil> it;
	   		
		//deplacement des fils
		it = this.listFilsHaut.iterator();
	    while (it.hasNext()){
	    	Fil tmp = (Fil)it.next();
	    	tmp.setMY(tmp.getMY() + deplacementX-tmp.getMHeight());//20);
	    }
	    it = this.listFilsBas.iterator();
	    while (it.hasNext()){
	    	Fil tmp = (Fil)it.next();
	    	tmp.setMY(tmp.getMY() + deplacementX-tmp.getMHeight());//20);
	    }
	}

	@Override
	public Rectangle getRectangle() {
		return this.rec;
	}
}
	           
	       