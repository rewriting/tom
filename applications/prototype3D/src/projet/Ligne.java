package projet;

/*
 * Ligne class.
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

import java.util.LinkedList;
import java.util.TreeSet;

import tom.Couple;
import tom.donnees.types.Formule;

import java3D.Repere;

public class Ligne {

	private TreeSet<Noeud> listePoints;
	
	private LinkedList<Ligne> lignesFilles = new LinkedList<Ligne>();

	private Repere repere;
	
	private boolean validee = false;

	public Ligne(Repere r) {
		listePoints = new TreeSet<Noeud>();
		repere = r;
	}

	public Ligne(TreeSet<Noeud> l, Repere r) {
		listePoints = l;
		repere = r;
	}

	public TreeSet<Noeud> getListePoints() {
		return listePoints;
	}

	public void ajouterNoeud(Noeud n) {
		listePoints.add(n);
	}

	public void enleverNoeud(Noeud n) {
		listePoints.remove(n);
	}
	
	public boolean getValidee() {
		return validee;
	}
	
	public void validerLigne() {
		validee = true;
		for (Ligne l : lignesFilles) {
			l.validerLigne();
		}
	}
	
	public void ajouterLigneFille(Ligne l) {
		lignesFilles.add(l);
	}

	public Couple contientCouple() {
		/*
		 * Determine si une ligne contient au moins un couple. Si c'est le cas,
		 * on retourne l'un de ces couples. Un noeud contenant "True" est
		 * egalement suffisant
		 */
		Couple resultat = new Couple();
		Couple couple = new Couple();
		TreeSet<Noeud> temp1 = (TreeSet<Noeud>) getListePoints().clone();
		boolean termine = false;
		Noeud temp2 = temp1.first();
		while (!termine) {
			for (Noeud n : getListePoints()) {
				if (!temp2.equals(n) && n.getFormule() != null
						&& temp2.getFormule() != null) {
					couple = new Couple(temp2, n, repere);
					/*
					 * "Vrai" couple
					 */
					if (couple.estUnCouple()) {
						termine = true;
						resultat = couple;
					} else if (temp2.getFormule().isTrue()) {
					/*
					 * Noeud contenant un "True", traite par la suite comme un
					 * couple pour ce qui concerne la recherche de preuves
					 */
						couple = new Couple(temp2, repere);
						termine = true;
						resultat = couple;
					}
				}
			}
			temp1.remove(temp2);
			if (!temp1.isEmpty()) {
				temp2 = temp1.first();
			} else {
				termine = true;
			}
		}
		return resultat;
	}

	public void relierPoints() {
		/*
		 * Relie tous les points de la ligne entre eux
		 */
		Noeud precedent = listePoints.first();
		for (Noeud n : listePoints) {
			repere.dessinerSegment(precedent, n, false);
			precedent = n;
		}
	}
	
	public void ajouterNumeroNoeud() {
		int i = 0;
		for (Noeud n : listePoints) {
			n.setNumeroNoeud(i);
			i++;
		}
	}

}
