package projet;

import java.util.TreeSet;

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
	
	public void relierPoints() {
		Noeud precedent = listePoints.first();
		for (Noeud n : listePoints) {
			repere.dessinerSegment(precedent, n);
			precedent = n;
		}
	}

}
