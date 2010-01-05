package projet;

import java.util.TreeSet;

import tom.Couple;
import tom.donnees.types.Formule;

import java3D.Repere;

public class Ligne {

	private TreeSet<Noeud> listePoints;

	private Repere repere;

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
		Noeud precedent = listePoints.first();
		for (Noeud n : listePoints) {
			repere.dessinerSegment(precedent, n, false);
			precedent = n;
		}
	}

}
