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

	private static boolean tom_equal_term_Sequent(Object t1, Object t2) {
		return (t1 == t2);
	}

	private static boolean tom_is_sort_Sequent(Object t) {
		return (t instanceof tom.donnees.types.Sequent);
	}

	private static boolean tom_equal_term_Formule(Object t1, Object t2) {
		return (t1 == t2);
	}

	private static boolean tom_is_sort_Formule(Object t) {
		return (t instanceof tom.donnees.types.Formule);
	}

	private static boolean tom_is_fun_sym_Input(tom.donnees.types.Formule t) {
		return (t instanceof tom.donnees.types.formule.Input);
	}

	private static tom.donnees.types.Formule tom_make_Input(String t0) {
		return tom.donnees.types.formule.Input.make(t0);
	}

	private static String tom_get_slot_Input_A(tom.donnees.types.Formule t) {
		return t.getA();
	}

	private static boolean tom_is_fun_sym_False(tom.donnees.types.Formule t) {
		return (t instanceof tom.donnees.types.formule.False);
	}

	private static tom.donnees.types.Formule tom_make_False() {
		return tom.donnees.types.formule.False.make();
	}

	private static boolean tom_is_fun_sym_True(tom.donnees.types.Formule t) {
		return (t instanceof tom.donnees.types.formule.True);
	}

	private static tom.donnees.types.Formule tom_make_True() {
		return tom.donnees.types.formule.True.make();
	}

	private static boolean tom_is_fun_sym_And(tom.donnees.types.Formule t) {
		return (t instanceof tom.donnees.types.formule.And);
	}

	private static tom.donnees.types.Formule tom_make_And(
			tom.donnees.types.Formule t0, tom.donnees.types.Formule t1) {
		return tom.donnees.types.formule.And.make(t0, t1);
	}

	private static tom.donnees.types.Formule tom_get_slot_And_f1(
			tom.donnees.types.Formule t) {
		return t.getf1();
	}

	private static tom.donnees.types.Formule tom_get_slot_And_f2(
			tom.donnees.types.Formule t) {
		return t.getf2();
	}

	private static boolean tom_is_fun_sym_Or(tom.donnees.types.Formule t) {
		return (t instanceof tom.donnees.types.formule.Or);
	}

	private static tom.donnees.types.Formule tom_make_Or(
			tom.donnees.types.Formule t0, tom.donnees.types.Formule t1) {
		return tom.donnees.types.formule.Or.make(t0, t1);
	}

	private static tom.donnees.types.Formule tom_get_slot_Or_f1(
			tom.donnees.types.Formule t) {
		return t.getf1();
	}

	private static tom.donnees.types.Formule tom_get_slot_Or_f2(
			tom.donnees.types.Formule t) {
		return t.getf2();
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

	private static tom.library.sl.Strategy tom_make_Identity() {
		return (new tom.library.sl.Identity());
	}

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
		if (Interface.getDerivation()) {
			LinkedList<Couple> listeCouple = new LinkedList<Couple>();
			listeCouple.add(new Couple(tom_make_True(), tom_make_False()));
			System.out.println("est-ce une preuve ? " + verifierPreuve(listeCouple));
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
			for (int i = 0; i < args.length; i++) {
				decomposerFormule(sequent[i], listeNoeud[3 * i + 1], i);
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
		TreeSet<Noeud> temp = new TreeSet<Noeud>();
		{
			{
				if (tom_is_sort_Formule(f)) {
					if (tom_is_fun_sym_And(((tom.donnees.types.Formule) f))) {
						temp = repere.dessinerAND(n, i, false);
						LinkedList<Ligne> temp2 = (LinkedList<Ligne>) listeLigne
								.clone();
						for (Ligne l : temp2) {
							if (l.getListePoints().contains(n)) {
								l.enleverNoeud(n);
								Ligne l1 = new Ligne((TreeSet<Noeud>) l
										.getListePoints().clone(), repere);
								l1.ajouterNoeud(temp.first());
								Ligne l2 = new Ligne((TreeSet<Noeud>) l
										.getListePoints().clone(), repere);
								l2.ajouterNoeud(temp.last());
								listeLigne.remove(l);
								listeLigne.add(l1);
								listeLigne.add(l2);
							}
						}
						decomposerFormule(
								tom_get_slot_And_f1(((tom.donnees.types.Formule) f)),
								temp.last(), i);
						decomposerFormule(
								tom_get_slot_And_f2(((tom.donnees.types.Formule) f)),
								temp.first(), i);
					}
				}
			}
			{
				if (tom_is_sort_Formule(f)) {
					if (tom_is_fun_sym_Or(((tom.donnees.types.Formule) f))) {
						temp = repere.dessinerOR(n, i);
						for (Ligne l : listeLigne) {
							if (l.getListePoints().contains(n)) {
								l.enleverNoeud(n);
								l.ajouterNoeud(temp.first());
								l.ajouterNoeud(temp.last());
							}
						}
						decomposerFormule(
								tom_get_slot_Or_f1(((tom.donnees.types.Formule) f)),
								temp.first(), i);
						decomposerFormule(
								tom_get_slot_Or_f2(((tom.donnees.types.Formule) f)),
								temp.last(), i);
					}
				}
			}
			{
				if (tom_is_sort_Formule(f)) {
					if (tom_is_fun_sym_Input(((tom.donnees.types.Formule) f))) {
						n.setEstFinal();
					}
				}
			}
			{
				if (tom_is_sort_Formule(f)) {
					if (tom_is_fun_sym_True(((tom.donnees.types.Formule) f))) {
						n.setEstFinal();
					}
				}
			}
			{
				if (tom_is_sort_Formule(f)) {
					if (tom_is_fun_sym_False(((tom.donnees.types.Formule) f))) {
						n.setEstFinal();
					}
				}
			}
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
		 * On decompose la formule situee sur le premier noeud qui reste encore
		 * a traiter
		 */
		for (Noeud n : l.getListePoints()) {
			if (!termine) {
				if (!n.getEstFinal()) {
					{
						{
							Object tomMatch2NameNumber_freshVar_0 = n
									.getFormule();
							if (tom_is_sort_Formule(tomMatch2NameNumber_freshVar_0)) {
								if (tom_is_fun_sym_Or(((tom.donnees.types.Formule) tomMatch2NameNumber_freshVar_0))) {
									decomposerFormuleDerivation(dupliquerFormule(
											"or",
											l,
											n,
											true,
											tom_get_slot_Or_f1(((tom.donnees.types.Formule) tomMatch2NameNumber_freshVar_0)),
											tom_get_slot_Or_f2(((tom.donnees.types.Formule) tomMatch2NameNumber_freshVar_0))));
									termine = true;
								}
							}
						}
						{
							Object tomMatch2NameNumber_freshVar_4 = n
									.getFormule();
							if (tom_is_sort_Formule(tomMatch2NameNumber_freshVar_4)) {
								if (tom_is_fun_sym_And(((tom.donnees.types.Formule) tomMatch2NameNumber_freshVar_4))) {
									tom.donnees.types.Formule tom_x = tom_get_slot_And_f1(((tom.donnees.types.Formule) tomMatch2NameNumber_freshVar_4));
									tom.donnees.types.Formule tom_y = tom_get_slot_And_f2(((tom.donnees.types.Formule) tomMatch2NameNumber_freshVar_4));
									decomposerFormuleDerivation(dupliquerFormule(
											"and", l, n, true, tom_x, tom_y));
									decomposerFormuleDerivation(dupliquerFormule(
											"and", l, n, false, tom_x, tom_y));
									termine = true;
								}
							}
						}
					}

				}
			}
		}
	}

	public static Ligne dupliquerFormule(String s, Ligne l, Noeud n,
			boolean gauche, Formule f1, Formule f2) {
		/*
		 * On dessine la formule "principale" de la ligne, et on la reproduit
		 * sur les autres noeuds de cette meme ligne, puis on traite la ou les
		 * nouvelle(s) ligne(s) engendree(s)
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
					nouvelleListePoints.add(repere.dessinerORDerivation(n1,
							n1.getNumeroSequent()).getLast());
				} else if (gauche) {
					TreeSet<Noeud> temp2 = repere.dessinerAND(n1, n1
							.getNumeroSequent(), true);
					nouvelleListePoints.add(temp2.first());
				} else {
					TreeSet<Noeud> temp2 = repere.dessinerAND(n1, n1
							.getNumeroSequent(), true);
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
			temp = false;
			for (Couple c : listeCouple) {
				if (c.estDansLigne(l)) {
					temp = true;
				}
			}
			resultat = resultat && temp;
		}
		return resultat;
	}

	public static class ajouterFormule extends
			tom.library.sl.AbstractStrategyBasic {
		private tom.donnees.types.Formule f1;

		public ajouterFormule(tom.donnees.types.Formule f1) {
			super(tom_make_Identity());
			this.f1 = f1;
		}

		public tom.donnees.types.Formule getf1() {
			return f1;
		}

		public tom.library.sl.Visitable[] getChildren() {
			tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
			stratChilds[0] = super.getChildAt(0);
			return stratChilds;
		}

		public tom.library.sl.Visitable setChildren(
				tom.library.sl.Visitable[] children) {
			super.setChildAt(0, children[0]);
			return this;
		}

		public int getChildCount() {
			return 1;
		}

		public tom.library.sl.Visitable getChildAt(int index) {
			switch (index) {
			case 0:
				return super.getChildAt(0);
			default:
				throw new IndexOutOfBoundsException();
			}
		}

		public tom.library.sl.Visitable setChildAt(int index,
				tom.library.sl.Visitable child) {
			switch (index) {
			case 0:
				return super.setChildAt(0, child);
			default:
				throw new IndexOutOfBoundsException();
			}
		}

		@SuppressWarnings("unchecked")
		public tom.donnees.types.Formule visit_Formule(
				tom.donnees.types.Formule tom__arg,
				tom.library.sl.Introspector introspector)
				throws tom.library.sl.VisitFailure {
			{
				{
					if (tom_is_sort_Formule(tom__arg)) {
						if (tom_is_fun_sym_And(((tom.donnees.types.Formule) tom__arg))) {

							try {
								return tom_make_And(
										tom_make_ajouterFormule(f1)
												.visit(
														tom_get_slot_And_f1(((tom.donnees.types.Formule) tom__arg))),
										tom_make_ajouterFormule(f1)
												.visit(
														tom_get_slot_And_f2(((tom.donnees.types.Formule) tom__arg))));
							} catch (VisitFailure e) {
								System.out.println("Erreur : " + e);
							}
							;
						}
					}
				}
				{
					if (tom_is_sort_Formule(tom__arg)) {
						if (tom_is_fun_sym_Or(((tom.donnees.types.Formule) tom__arg))) {
							try {
								return tom_make_Or(
										tom_make_ajouterFormule(f1)
												.visit(
														tom_get_slot_Or_f1(((tom.donnees.types.Formule) tom__arg))),
										tom_make_ajouterFormule(f1)
												.visit(
														tom_get_slot_Or_f2(((tom.donnees.types.Formule) tom__arg))));
							} catch (VisitFailure e) {
								System.out.println("Erreur : " + e);
							}
							;
						}
					}
				}
				{
					if (tom_is_sort_Formule(tom__arg)) {
						if (tom_is_fun_sym_Input(((tom.donnees.types.Formule) tom__arg))) {
							return f1;
						}
					}
				}
				{
					if (tom_is_sort_Formule(tom__arg)) {
						if (tom_is_fun_sym_False(((tom.donnees.types.Formule) tom__arg))) {
							return f1;
						}
					}
				}
				{
					if (tom_is_sort_Formule(tom__arg)) {
						if (tom_is_fun_sym_True(((tom.donnees.types.Formule) tom__arg))) {
							return f1;
						}
					}
				}
			}
			return _visit_Formule(tom__arg, introspector);
		}

		@SuppressWarnings("unchecked")
		public tom.donnees.types.Formule _visit_Formule(
				tom.donnees.types.Formule arg,
				tom.library.sl.Introspector introspector)
				throws tom.library.sl.VisitFailure {
			if (!((environment == null))) {
				return ((tom.donnees.types.Formule) any.visit(environment,
						introspector));
			} else {
				return any.visitLight(arg, introspector);
			}
		}

		@SuppressWarnings("unchecked")
		public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
				throws tom.library.sl.VisitFailure {
			if (tom_is_sort_Formule(v)) {
				return ((T) visit_Formule(((tom.donnees.types.Formule) v),
						introspector));
			}
			if (!((environment == null))) {
				return ((T) any.visit(environment, introspector));
			} else {
				return any.visitLight(v, introspector);
			}
		}
	}

	private static tom.library.sl.Strategy tom_make_ajouterFormule(
			tom.donnees.types.Formule t0) {
		return new ajouterFormule(t0);
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
					return tom_make_Or(reecrireFormule(mot1),
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
					return tom_make_And(reecrireFormule(mot1),
							reecrireFormule(mot2));
				}
				break;
			case 'T':
				if (s.length() == 4) {
					return tom_make_True();
				}
				break;
			case 'F':
				if (s.length() == 5) {
					return tom_make_False();
				}
				break;
			case 'A':
				if (s.length() == 1) {
					return tom_make_Input("A");
				}
				break;
			default:
				break;
			}
		}
		return tom_make_False();
	}

}
