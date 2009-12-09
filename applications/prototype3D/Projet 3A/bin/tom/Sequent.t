package tom;

import java.util.LinkedList;
import java.util.TreeSet;

import java3D.Repere;
import projet.Noeud;
import projet.Ligne;
import fenetre.Interface;

import tom.donnees.*;
import tom.donnees.types.*;
import tom.library.sl.*;

public class Sequent {
	
	private static Repere repere = new Repere();
	private static boolean estFini = false;
	private static LinkedList<Ligne> listeLigne = new LinkedList<Ligne>();
	
	%include { donnees/Donnees.tom }
	%include { sl.tom }
	
	public static boolean getEstFini() {
		return estFini;
	}

	public static void setEstFini() {
		estFini = !estFini;
	}

	public static Repere getRepere() {
		return repere;
	}

	public final static void main(String[] args, Repere rep) {
		/*
		 * On interprete les formules rentres par l'utilisateur en tant que
		 * Formule puis on genere les arbres respectifs
		 */
		genererGraphe(args, rep);
		/*
		 * On reporte les noeuds terminaux au plus haut niveau, et on les relie
		 * entre eux seulement si ce n'est pas un graphe "de derivation"
		 */
		if (!Interface.getDerivation()) {
			noeudTerminaux();
			dessiner();
		}
		/*
		 * On verifie la preuve
		 */
		if (!Interface.getDerivation()) {
			LinkedList<Couple> listeCouple = new LinkedList<Couple>();
			listeCouple.add(new Couple(`True(),`False()));
			System.out.println("est-ce une preuve ? " +verifierPreuve(listeCouple));
		}
		/*
		 * On dessine le tout dans le repere
		 */
		repere.dessiner(repere);
	}

	@SuppressWarnings("unchecked")
	public static void genererGraphe(String[] args, Repere rep) {
		repere = rep;
		Formule[] sequent = new Formule[args.length];
		Noeud[] listeNoeud = new Noeud[3 * args.length];
		Ligne l = new Ligne(repere);
		for (int i = 0; i < args.length; i++) {
			sequent[i] = reecrireFormule(args[i]);
			/*
			 * Chaque formule est associee a trois noeuds : l'entree, le point
			 * de depart de la construction de l'arbre et la sortie
			 */
			listeNoeud[3 * i] = new Noeud(3 * i, 0, 1, 1, 1, "neutre", "-1", i);
			listeNoeud[3 * i].setEstFinal();
			repere.dessinerPoint(listeNoeud[3 * i], i);
			listeNoeud[3 * i + 1] = new Noeud(3 * i + 1, 0, 1, 1, 1, "depart",
					"", i);
			repere.dessinerPoint(listeNoeud[3 * i + 1], i);
			listeNoeud[3 * i + 1].setFormule(sequent[i]);
			listeNoeud[3 * i + 2] = new Noeud(3 * i + 2, 0, 1, 1, 1, "neutre",
					"-1", i);
			listeNoeud[3 * i + 2].setEstFinal();
			repere.dessinerPoint(listeNoeud[3 * i + 2], i);

			/*
			 * On prepare la ligne initiale
			 */
			l.ajouterNoeud(listeNoeud[3 * i]);
			l.ajouterNoeud(listeNoeud[3 * i + 1]);
			l.ajouterNoeud(listeNoeud[3 * i + 2]);
		}
		
		/*
		 * Dans le cas de la derivation, on lance la procedure en traitant la
		 * premiere ligne
		 */
		listeLigne.add(l);
		if (Interface.getDerivation()) {
			decomposerFormuleDerivation(l);
		} else {
			for (int i=0; i<args.length; i++) {
				decomposerFormule(sequent[i], listeNoeud[3*i+1], i);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void noeudTerminaux() {
		/*
		 * On rajoute les noeuds terminaux au niveau le plus haut
		 */
		LinkedList<Noeud> listeTemp = (LinkedList<Noeud>) repere
				.getListeNoeud().clone();
		for (Noeud n : listeTemp) {
			if (n.getEstFinal()) {
				float hauteur = 0;
				for (int i = 0; i < repere.getNiveauMax() - n.getProfondeur(); i++) {
					hauteur = (float) (hauteur + (1 / Math.pow(2, n
							.getProfondeur()
							+ i)));
				}
				Noeud temp = new Noeud(n.getX(), n.getY() + hauteur, n.getZ(),
						n.getProfondeurInitiale(), repere.getNiveauMax(), n
								.getEtat(), "-1", 0);
				repere.dessinerPoint(temp, -1);
				n.setEstFinal();
				for (Ligne l : listeLigne) {
					if (l.getListePoints().contains(n)) {
						l.enleverNoeud(n);
						l.ajouterNoeud(temp);
					}
				}
			}
		}
	}
	
	public static void dessiner() {
		for (Ligne l : listeLigne) {
			l.relierPoints();
		}
	}

	@SuppressWarnings("unchecked")
	public static void relierNoeudsTerminaux() {
		/*
		 * On relie tous les noeuds terminaux entre eux, pour avoir le "chemin"
		 * de la preuve
		 */
		LinkedList<Noeud> listeTemp = (LinkedList<Noeud>) repere
				.getListeNoeud().clone();
		LinkedList<Float> listeZ = new LinkedList<Float>();
		TreeSet<Noeud> pointsFinaux = new TreeSet<Noeud>();
		for (Noeud n : listeTemp) {
			if (n.getProfondeur() == repere.getNiveauMax()
					&& !pointsFinaux.contains(n)) {
				pointsFinaux.add(n);
			}
		}
		boolean trouve = false;
		for (Noeud n : pointsFinaux) {
			TreeSet<Noeud> listeTemp2 = new TreeSet<Noeud>();
			float perimetre;
			/*
			 * Determination du perimetre de recherche
			 */
			if (n.getEtat() == "neutre") {
				perimetre = 1;
			} else {
				perimetre = (float) (3 / Math.pow(2,
						n.getProfondeurInitiale() - 1));
			}
			/*
			 * On selectionne les points a relier
			 */
			for (Noeud p : pointsFinaux) {
				if (p.getEtat() != "neutre") {
					if (n.getEtat() == "neutre") {
						if (p.getX() <= n.getX() + perimetre
								&& p.getZ() <= n.getZ() + perimetre
								&& p.getZ() >= n.getZ() - perimetre
								&& p.getX() > n.getX()) {
							listeTemp2.add(p);
						}
					} else {
						if (p.getX() <= n.getX() + perimetre
								&& p.getZ() <= n.getZ() + perimetre / 2
								&& p.getZ() >= n.getZ() - perimetre / 2
								&& p.getX() > n.getX()) {
							listeTemp2.add(p);
						}
					}
				}
			}
			/*
			 * On relie les points. Si aucun point n'est selectionne, on prend
			 * le prochain point finissant un sequent
			 */
			for (Noeud q : listeTemp2) {
				if (!trouve || (!listeZ.contains(q.getZ()))) {
					repere.dessinerSegment(n, q);
				}
				trouve = true;
				listeZ.add(q.getZ());
			}
			if (!trouve) {
				boolean recherche = false;
				for (Noeud t : pointsFinaux) {
					if (!recherche && t.getX() > n.getX()
							&& t.getEtat().equals("neutre")) {
						recherche = true;
						repere.dessinerSegment(t, n);
					}
				}
			}
			trouve = false;
		}
	}
	
	public static void decomposerFormule(Formule f, Noeud n, int i) {
		n.setFormule(f);
		TreeSet<Noeud> temp = new TreeSet<Noeud>();
		%match(f) {
			And(x,y) -> { temp = repere.dessinerAND(n, i, false);
						  LinkedList<Ligne> temp2 = (LinkedList<Ligne>) listeLigne
								.clone();
						  for (Ligne l : temp2) {
							if (l.getListePoints().contains(n)) {
								l.enleverNoeud(n);
								Ligne l1 = new Ligne((TreeSet<Noeud>) l.getListePoints().clone(), repere);
								l1.ajouterNoeud(temp.first());
								Ligne l2 = new Ligne((TreeSet<Noeud>) l.getListePoints().clone(), repere);
								l2.ajouterNoeud(temp.last());
								listeLigne.remove(l);
								listeLigne.add(l1);
								listeLigne.add(l2);
							}
						  }
						  decomposerFormule(`x, temp.last(),i);
						  decomposerFormule(`y, temp.first(),i);
						}
			Or(x,y) -> { temp = repere.dessinerOR(n, i);
						 for (Ligne l : listeLigne) {
							if (l.getListePoints().contains(n)) {
								l.enleverNoeud(n);
								l.ajouterNoeud(temp.first());
								l.ajouterNoeud(temp.last());
							}
						 }
						 decomposerFormule(`x, temp.first(),i);
						 decomposerFormule(`y, temp.last(),i);
					    }
			Input(x) -> { n.setEstFinal(); }
			True() -> { n.setEstFinal(); }
			False() -> { n.setEstFinal(); }
		}
	}
	
	public static void decomposerFormuleDerivation(Ligne l) {
		boolean termine = false;
		/*
		 * On relie les points de toutes les lignes
		 */
		if (!l.getListePoints().isEmpty()) {
			l.relierPoints();
			listeLigne.add(l);
		}
		/*
		 * On decompose la formule situee sur le premier noeud qui reste encore a traiter
		 */
		for (Noeud n : l.getListePoints()) {
			if (!termine) {
				if (!n.getEstFinal()) {
					%match(n.getFormule()) {
						Or(x,y) -> { decomposerFormuleDerivation(dupliquerFormule("or", l, n, true, `x, `y));
									 termine = true;
								   }
						And(x,y) -> { decomposerFormuleDerivation(dupliquerFormule("and", l, n, true, `x, `y));
									  decomposerFormuleDerivation(dupliquerFormule("and", l, n, false, `x, `y));
									  termine = true;
									}
					}
				}
			}
		}
	}
	
	public static Ligne dupliquerFormule(String s, Ligne l, Noeud n, boolean gauche, Formule f1, Formule f2) {
		/*
		 * On dessine la formule "principale" de la ligne, et on la reproduit sur les autres
		 * noeuds de cette meme ligne, puis on traite la ou les nouvelle(s) ligne(s) engendree(s)
		 */
		TreeSet<Noeud> temp = (TreeSet<Noeud>) l.getListePoints().clone();
		TreeSet<Noeud> nouvelleListePoints = new TreeSet<Noeud>();
		temp.remove(n);
		/*
		 * copie de la formule "principale"
		 */
		for (Noeud n1 : temp) {
			if (!n1.getEstFinal()) {
				if (s.equals("or")) {
					nouvelleListePoints.add(repere.dessinerORDerivation(n1,n1.getNumeroSequent()).getLast());
				} else if (gauche) {
					TreeSet<Noeud> temp2 = repere.dessinerAND(n1, n1.getNumeroSequent(), true);
					nouvelleListePoints.add(temp2.first());
				} else {
					TreeSet<Noeud> temp2 = repere.dessinerAND(n1, n1.getNumeroSequent(), true);
					nouvelleListePoints.add(temp2.last());
				}
			}
		}
		/*
		 * Formule "principale"
		 */
		if (s.equals("or")) {
			temp = repere.dessinerOR(n, n.getNumeroSequent());
			temp.first().setFormule(f1);
			nouvelleListePoints.add(temp.first());
			temp.last().setFormule(f2);
			nouvelleListePoints.add(temp.last());
		} else {
			temp = repere.dessinerAND(n, n.getNumeroSequent(), false);
			if (gauche) {
				temp.first().setFormule(f2);
				nouvelleListePoints.add(temp.first());
			} else {
				temp.last().setFormule(f1);
				nouvelleListePoints.add(temp.last());
			}
		}
		listeLigne.remove(l);
		return new Ligne(nouvelleListePoints, repere);
	}
	
	public static boolean verifierPreuve(LinkedList<Couple> listeCouple) {
		boolean resultat = true;
		boolean temp = false;
		for (Ligne l : listeLigne) {
			for (Couple c : listeCouple) {
				if (c.estDansLigne(l)) {
					temp = true;
				}
			}
			resultat = resultat && temp;
		}
		return resultat;
	}
	
	%strategy ajouterFormule(Formule f1) extends Identity() {
		visit Formule {
			And(x,y) -> { try {
							return `And(ajouterFormule(f1).visit(x),ajouterFormule(f1).visit(y));
						  } catch (VisitFailure e) {
						  	System.out.println("Erreur : " + e);
						  };
						}
			Or(x,y) -> { try {
							return `Or(ajouterFormule(f1).visit(x),ajouterFormule(f1).visit(y));
						 } catch (VisitFailure e) {
						  	System.out.println("Erreur : " + e);
						 };
						}
			Input(x) -> { return f1; }
			False() -> { return f1; }
			True() -> { return f1; }
		}
	}
	
	public static Formule reecrireFormule(String s) {
		int compteur = 0;
		for (int i = 0; i < s.length(); i++) {
			switch (s.charAt(i)) {
			case '(':
				compteur++;
				break;
			case ')':
				compteur--;
				break;
			case '\\':
				if (compteur == 0) {
					String mot1 = "";
					String mot2 = "";
					if ((s.startsWith("("))
							&& s.substring(i - 1, i).equals(")")) {
						mot1 = s.substring(1, i - 1);
					} else {
						mot1 = s.substring(0, i);
					}
					if ((s.substring(i + 2).startsWith("(")) && s.endsWith(")")) {
						mot2 = s.substring(i + 3, s.length() - 1);
					} else {
						mot2 = s.substring(i + 2, s.length());
					}
					return `Or(reecrireFormule(mot1),
							reecrireFormule(mot2));
				}
				break;
			case '/':
				if (compteur == 0) {
					String mot1 = "";
					String mot2 = "";
					if ((s.startsWith("("))
							&& s.substring(i - 1, i).equals(")")) {
						mot1 = s.substring(1, i - 1);
					} else {
						mot1 = s.substring(0, i - 1);
					}
					if ((s.substring(i + 2).startsWith("(")) && s.endsWith(")")) {
						mot2 = s.substring(i + 3, s.length() - 1);
					} else {
						mot2 = s.substring(i + 2, s.length());
					}
					return `And(reecrireFormule(mot1),
							reecrireFormule(mot2));
				}
				break;
			case 'T':
				if (s.length() == 4) {
					return `True();
				}
				break;
			case 'F':
				if (s.length() == 5) {
					return `False();
				}
				break;
			case 'A':
				if (s.length() == 1) {
					return `Input("A");
				}
				break;
			default:
				break;
			}
		}
		return tom_make_False();
	}

}
