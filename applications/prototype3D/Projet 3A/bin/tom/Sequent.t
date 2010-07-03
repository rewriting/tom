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
	private static LinkedList<Ligne> listeLigneFinale = new LinkedList<Ligne>();
	private static LinkedList<Couple> listeCouple = new LinkedList<Couple>();
	
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
	
	public static LinkedList<Couple> getListeCouple() {
		return listeCouple;
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
			listeNoeud[3 * i] = new Noeud(3 * i, 0, 1, 1, 1, "-1", i);
			listeNoeud[3 * i].setFormule(`False());
			listeNoeud[3 * i].setEstFinal();
			repere.dessinerPoint(listeNoeud[3 * i], i);
			listeNoeud[3 * i + 1] = new Noeud(3 * i + 1, 0, 1, 1, 1,
					"", i);
			repere.dessinerPoint(listeNoeud[3 * i + 1], i);
			listeNoeud[3 * i + 1].setFormule(sequent[i]);
			listeNoeud[3 * i + 2] = new Noeud(3 * i + 2, 0, 1, 1, 1,
					"-1", i);
			listeNoeud[3 * i + 2].setFormule(`False());
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
						n.getProfondeurInitiale(), repere.getNiveauMax(), "-1", 0);
				repere.dessinerPoint(temp, -1);
				n.setEstFinal();
				temp.setFormule(n.getFormule());
				for (Ligne l : listeLigne) {
					if (l.getListePoints().contains(n)) {
						l.enleverNoeud(n);
						l.ajouterNoeud(temp);
					}
				}
			}
		}
		/*
		 * On recopie les lignes en tant que ligne finale
		 */
		listeLigneFinale = (LinkedList<Ligne>) listeLigne.clone();
	}
	
	public static void dessiner() {
		/*
		 * Une fois que l'on a dessine toutes les lignes, on les supprime pour
		 * preparer le graphe suivant
		 */
		LinkedList<Ligne> temp = (LinkedList<Ligne>) listeLigne.clone();
		for (Ligne l : temp) {
			l.relierPoints();
			listeLigne.remove(l);
		}		
	}
	
	public static void decomposerFormule(Formule f, Noeud n, int i) {
		n.setFormule(f);
		TreeSet<Noeud> temp = new TreeSet<Noeud>();
		%match(f) {
			And(x,y) -> { n.setFormule(`And(x,y));
						  temp = repere.dessinerAND(n, i, false);
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
			Or(x,y) -> { n.setFormule(`Or(x,y));
						 temp = repere.dessinerOR(n, i);
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
			Input(x) -> { n.setFormule(`Input(x));
						  n.setEstFinal(); }
			True() -> { n.setFormule(`True());
						n.setEstFinal(); }
			False() -> { n.setFormule(`False());
						 n.setEstFinal(); }
			Neg(x) -> { n.setFormule(`Neg(x));
						n.setEstFinal(); }
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
		/*
		 * Si aucun point n'a ete traite, la ligne est finale, et est ajoutee en tant que telle
		 */
		if (!termine) {
			listeLigneFinale.add(l);
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
	
	public static boolean verifierPreuve(String[] listeCouple) {
		/*
		 * On commence par creer les couples de formules correspondant 
		 * aux entrees de l'utilisateur
		 */
		String[] couple = new String[2];
		LinkedList<Couple> listeCoupleFinale = new LinkedList<Couple>();
		for (int i=0; i<listeCouple.length; i++) {
			couple = listeCouple[i].split(" ");
			listeCoupleFinale.add(new Couple(new Noeud(reecrireFormule(couple[0])), new Noeud(reecrireFormule(couple[1])), repere));
		}
		boolean resultat = true;
		boolean temp = false;
		for (Ligne l : listeLigneFinale) {
			for (Couple c : listeCoupleFinale) {
				if (c.estDansLigne(l)) {
					temp = true;
				}
			}
			resultat = resultat && temp;
		}
		return resultat;
	}
	
	public static boolean trouverPreuve() {
		boolean resultat = true;
		for (Ligne l : listeLigneFinale) {
			if (l.contientCouple().estUnCouple()) {
				listeCouple.add(l.contientCouple());
			} else {
				/*
				 * Si au moins une ligne ne contient pas de "vrais" couples, il
				 * n'y a pas de preuves
				 */
				resultat = false;
			}
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
						mot1 = s.substring(0, i);
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
			case 'N':
				if (s.length() == 6) {
					String mot1 = s.substring(i+4, s.length()-1);
					return `Neg(reecrireFormule(mot1));					
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
			default:
				if (s.length() == 1) {
					return `Input(s);
				}
				break;
			}
		}
		return tom_make_False();
	}

}
