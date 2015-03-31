package tom;

/*
 * Sequent class.
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

import java3D.Repere;
import java3D.Point;
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
	private static TreeSet<Noeud> listeAndTemp = new TreeSet<Noeud>();
	private static TreeSet<Noeud> listeAndTemp2 = new TreeSet<Noeud>();
	private static TreeSet<Noeud> listeAndTempDroit = new TreeSet<Noeud>();
	
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
		 * On prepare le futur graphe
		 */
		supprimerLigne();
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
		ordonnerNoeudsTerminaux();
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
				if (hauteur != 0) {
					Noeud temp = new Noeud(n.getX(), n.getY() + hauteur, n
							.getZ(), n.getProfondeurInitiale(), repere
							.getNiveauMax(), n.getPosition(), 0);
					repere.dessinerPoint(temp, n.getNumeroSequent());
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
		}
		/*
		 * On recopie les lignes en tant que ligne finale
		 */
		listeLigneFinale = (LinkedList<Ligne>) listeLigne.clone();
	}
	
	@SuppressWarnings("unchecked")
	public static void dessiner() {
		/*
		 * On relie les points de toutes les lignes
		 */
		LinkedList<Ligne> temp = (LinkedList<Ligne>) listeLigneFinale.clone();
		for (Ligne l : temp) {
			l.relierPoints();
		}		
	}
	
	public static void supprimerLigne() {
		/*
		 * Permet de reinitialiser les listes de lignes en vue du prochain graphe
		 */
		listeLigne.clear();
		listeLigneFinale.clear();
	}
	
	public static void supprimerCouple() {
		/*
		 * Permet de reinitialiser la liste des couples en vue de nouvelles
		 * recherches de preuves
		 */
		listeCouple.clear();
	}
	
	public static void ordonnerNoeudsTerminaux() {
		int i = 1;
		/*
		 * On fait la correspondance entre les noeuds des lignes et les points
		 */
		for (Point p : repere.getListePoints()) {
			for (Ligne l : listeLigne) {
				for (Noeud n : l.getListePoints()) {
					if (p.getN().compareTo(n) == 0) {
						p.getN().setEstDansLigneFinale(true);
					}
				}
			}
		}
		/*
		 * On garde comme points seulement ceux qui constituent les lignes (definis ci-dessus)
		 * On ne prend pas les points superflus (debut et fin de formules)
		 */
		TreeSet<Point> temp = new TreeSet<Point>();
		for (Point p : repere.getListePoints()) {
			if (p.getN().getEstDansLigneFinale() && (p.getN().getY()>0 || !p.getN().getFormule().isFalse())) {
				p.addUserData(i + "");
				i++;
				temp.add(p);
			}
		}
		repere.setListePoints(temp);
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
						  n.setEstFinal();}
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
						And(x,y) -> { Ligne temporaire = dupliquerFormule("and", l, n, true, `x, `y);
									  Ligne temporaire2 = dupliquerFormule("and", l, n, false, `x, `y);
									  decomposerFormuleDerivation(temporaire);
									  decomposerFormuleDerivation(temporaire2);
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
			l.ajouterNumeroNoeud();
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
		Noeud noeudTemp = new Noeud();
		temp.remove(n);
		/*
		 * copie de la formule "principale"
		 */
		for (Noeud n1 : temp) {
			if (!n1.getEstFinal()) {
				if (s.equals("or")) {
					noeudTemp = repere.dessinerORDerivation(n1,
							n1.getNumeroSequent()).getLast();
					noeudTemp.setFormule(n1.getFormule());
					nouvelleListePoints.add(noeudTemp);
				} else {
					if (gauche) {
						listeAndTemp = repere.dessinerAND(n1, n1
								.getNumeroSequent(), true);
						listeAndTemp.first().setFormule(n1.getFormule());
						listeAndTemp.last().setFormule(n1.getFormule());
						nouvelleListePoints.add(listeAndTemp.first());
						listeAndTempDroit.add(listeAndTemp.last());
					} else {
						for (Noeud n2 : listeAndTempDroit) {
							nouvelleListePoints.add(n2);
						}
						listeAndTempDroit.clear();
					}
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
			if (gauche) {
				listeAndTemp2 = repere.dessinerAND(n, n.getNumeroSequent(), false);
				listeAndTemp2.first().setFormule(f2);
				nouvelleListePoints.add(listeAndTemp2.first());
			} else {
				listeAndTemp2.last().setFormule(f1);
				nouvelleListePoints.add(listeAndTemp2.last());
			}
		}
		Ligne l1 = new Ligne(nouvelleListePoints, repere);
		l.ajouterLigneFille(l1);
		return l1;
	}
	
	public static boolean verifierPreuve(LinkedList<Couple> listeCouple) {
		/*
		 * On regarde si chaque ligne finale contient au moins l'un des couples
		 * selectionnes
		 */
		/*
		 * Cas du graphe normal
		 */
		boolean resultat = true;
		if (!Interface.getDerivation()) {
			boolean temp = false;
			for (Ligne l : listeLigneFinale) {
				temp = false;
				for (Couple c : listeCouple) {
					if (c.estDansLigne(l)) {
						temp = true;
					}
				}
				resultat = resultat && temp;
			}
		/*
		 * Cas de la derivation
		 */
		} else {
			for (Ligne l : listeLigne) {
				for (Couple c : listeCouple) {
					if (c.estDansLigne(l)) {
						l.validerLigne();
					}
				}
			}
			for (Ligne l : listeLigneFinale) {
				resultat = resultat && l.getValidee();
			}
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
