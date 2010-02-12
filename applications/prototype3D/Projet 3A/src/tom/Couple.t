package tom;

import projet.Ligne;
import projet.Noeud;
import java3D.Repere;

import tom.donnees.*;
import tom.donnees.types.*;
import tom.library.sl.*;

public class Couple {
	
	private Noeud n1;
	private Noeud n2;
	private Repere repere;
	
	%include { donnees/Donnees.tom }
	%include { sl.tom }
	
	public Couple() {
		n1 = new Noeud(`False());
		n2 = new Noeud(`False());
		repere = new Repere();
	}
	
	public Couple(Noeud n, Repere r) {
		n1 = n;
		n2 = n;
		repere = r;
	}
	
	public Couple(Noeud noeud1, Noeud noeud2, Repere r) {
		n1 = noeud1;
		n2 = noeud2;
		repere = r;
	}
	
	public Formule getF1() {
		return n1.getFormule();
	}
	
	public Formule getF2() {
		return n2.getFormule();
	}
	
	public boolean estUnCouple() {
		/*
		 * Un couple est un couple si les deux formules qui le composent sont une negation
		 * l'une de l'autre
		 */
		boolean resultat = false;
		Formule temp1 = `Neg(n1.getFormule());
		Formule temp2 = `Neg(n2.getFormule());
		resultat = (temp1 == n2.getFormule() || temp2 == n1.getFormule());
		/*
		 * Un couple est egalement un couple si il contient "True"
		 */
		temp1 = `True();
		resultat = resultat || (temp1 == n1.getFormule() && temp1 == n2.getFormule());
		return resultat;
	}
	
	public boolean estDansLigne(Ligne l) {
		boolean temp1 = false;
		boolean temp2 = false;
		for (Noeud n : l.getListePoints()) {
			if (n.getFormule() == n1.getFormule()) {
				temp1 = true;
			}
			if (n.getFormule() == n2.getFormule()) {
				temp2 = true;
			}
		}
		return (temp1 && temp2);
	}
	
	public void dessinerCouple() {
		/*
		 * On commence par verifier que les deux noeuds sont bien a la meme hauteur
		 */
		if (n1.getY() == n2.getY()) {
			repere.dessinerCourbe(n1,n2);
		}
	}

}
