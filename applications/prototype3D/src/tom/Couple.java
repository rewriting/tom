package tom;

/*
 * Couple class.
 * 
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
 * Nancy, France.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * To have a copy of the GNU General Public License, please see <http://www.gnu.org/licenses/>.
 *
 * Thomas Boudin e-mail: Thomas.Boudin@mines.inpl-nancy.fr
 *
 */

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

	private static boolean tom_equal_term_char(char t1, char t2) {
		return t1 == t2;
	}

	private static boolean tom_is_sort_char(char t) {
		return true;
	}

	private static boolean tom_equal_term_String(String t1, String t2) {
		return t1.equals(t2);
	}

	private static boolean tom_is_sort_String(String t) {
		return t instanceof String;
	}

	private static boolean tom_equal_term_Formule(Object t1, Object t2) {
		return (t1 == t2);
	}

	private static boolean tom_is_sort_Formule(Object t) {
		return (t instanceof tom.donnees.types.Formule);
	}

	private static tom.donnees.types.Formule tom_make_False() {
		return tom.donnees.types.formule.False.make();
	}

	private static tom.donnees.types.Formule tom_make_True() {
		return tom.donnees.types.formule.True.make();
	}

	private static tom.donnees.types.Formule tom_make_Neg(
			tom.donnees.types.Formule t0) {
		return tom.donnees.types.formule.Neg.make(t0);
	}

	private static boolean tom_equal_term_Strategy(Object t1, Object t2) {
		return (t1.equals(t2));
	}

	private static boolean tom_is_sort_Strategy(Object t) {
		return (t instanceof tom.library.sl.Strategy);
	}

	private static boolean tom_equal_term_Position(Object t1, Object t2) {
		return (t1.equals(t2));
	}

	private static boolean tom_is_sort_Position(Object t) {
		return (t instanceof tom.library.sl.Position);
	}

	private static boolean tom_equal_term_int(int t1, int t2) {
		return t1 == t2;
	}

	private static boolean tom_is_sort_int(int t) {
		return true;
	}

	public Couple() {
		n1 = new Noeud(tom_make_False());
		n2 = new Noeud(tom_make_False());
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
		 * Un couple est un "vrai" couple si les deux formules qui le composent
		 * sont une negation l'une de l'autre
		 */
		boolean resultat = false;
		Formule temp1 = tom_make_Neg(n1.getFormule());
		Formule temp2 = tom_make_Neg(n2.getFormule());
		resultat = (temp1 == n2.getFormule() || temp2 == n1.getFormule());
		/*
		 * Un couple est egalement un couple si il contient "True"
		 */
		temp1 = tom_make_True();
		resultat = resultat
				|| (temp1 == n1.getFormule() && temp1 == n2.getFormule());
		return resultat;
	}

	public boolean estDansLigne(Ligne l) {
		/*
		 * Test si le couple fait parti de la ligne consideree
		 */
		boolean temp1 = false;
		boolean temp2 = false;
		for (Noeud n : l.getListePoints()) {
			if (n.compareTo(n1) == 0) {
				temp1 = true;
			}
			if (n.compareTo(n2) == 0) {
				temp2 = true;
			}
		}
		return (temp1 && temp2);
	}

	public void dessinerCouple() {
		/*
		 * On commence par verifier que les deux noeuds sont bien a la meme
		 * hauteur
		 */
		if (n1.getY() == n2.getY()) {
			repere.dessinerCourbe(n1, n2);
		}
	}

}
