package tom;

import projet.Ligne;
import projet.Noeud;
import tom.donnees.types.Formule;

public class Couple {
	
	private Formule f1;
	private Formule f2;
	
	public Couple() {
	}
	
	public Couple(Formule x, Formule y) {
		f1 = x;
		f2 = y;
	}
	
	public boolean estDansLigne(Ligne l) {
		boolean temp1 = false;
		boolean temp2 = false;
		for (Noeud n : l.getListePoints()) {
			if (n.getFormule() == f1) {
				temp1 = true;
			}
			if (n.getFormule() == f2) {
				temp2 = true;
			}
		}
		return (temp1 && temp2);
	}

}
