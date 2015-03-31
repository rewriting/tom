package java3D;

/*
 * Repere class.
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

import projet.Noeud;
import tom.Sequent;
import fenetre.Interface;

import java.applet.Applet;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.media.j3d.Locale;
import javax.vecmath.*;

public class Repere extends Applet {

	public static final Color3f COULEUR_CHERCHE = new Color3f(0.7f, 0.5f, 0.2f);

	public static final Color3f COULEUR_OR = new Color3f(0.7f, 0.5f, 0.7f);

	public static final Color3f COULEUR_AND = new Color3f(0.3f, 0.5f, 0.3f);

	public static final Color3f COULEUR_DERIV = new Color3f(0.15f, 0.15f, 0.15f);

	public static final Color3f COULEUR_COUPLE = new Color3f(0.3f, 0.7f, 0.9f);

	public static final Color3f COULEUR_ARRETE = new Color3f(0.6f, 0.6f, 0.6f);

	private static final double ECHELLE = 2;

	private static final long serialVersionUID = 1L;

	private Frame frame;

	private SimpleUniverse simpleU;

	private BranchGroup parent = new BranchGroup();

	private BranchGroup removable = new BranchGroup();

	private BranchGroup temp = new BranchGroup();

	private Locale locale;

	private LinkedList<Noeud> listeNoeud = new LinkedList<Noeud>();

	private TreeSet<Point> listePoints = new TreeSet<Point>();

	private TreeSet<Point> listePointsTotal = new TreeSet<Point>();

	private Sequent sequent;

	private int niveauMax = 1;

	public Repere() {
	}

	public LinkedList<Noeud> getListeNoeud() {
		return listeNoeud;
	}

	public void addListePoints(Point p) {
		listePoints.add(p);
	}

	public TreeSet<Point> getListePoints() {
		return listePoints;
	}

	public TreeSet<Point> getListePointsTotal() {
		return listePointsTotal;
	}

	public void addListeNoeud(Noeud n) {
		listeNoeud.add(n);
	}

	public static double getEchelle() {
		return ECHELLE;
	}

	public static Color3f getCouleurCherche() {
		return COULEUR_CHERCHE;
	}

	public static Color3f getCouleurDerivation() {
		return COULEUR_DERIV;
	}

	public int getNiveauMax() {
		return niveauMax;
	}

	public void setNiveauMax() {
		niveauMax++;
	}

	public void setSequent(Sequent s) {
		sequent = s;
	}

	public Sequent getSequent() {
		return sequent;
	}

	public void setListePoints(TreeSet<Point> liste) {
		listePoints = liste;
	}

	public void addPoint(Point p) {
		listePointsTotal.add(p);
	}

	/**
	 * Creation de la scene 3D qui contient tous les objets 3D
	 * 
	 * @return scene 3D
	 */
	public BranchGroup createSceneGraph(SimpleUniverse su) {
		/*
		 * Creation des axes qui serviront de repere
		 */
		LineArray axisX = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisX.setCoordinate(0, new Point3f(0f, 0f, 0f));
		axisX.setCoordinate(1, new Point3f(10f, 0f, 0f));
		axisX.setColor(0, new Color3f(1f, 0f, 0f));
		axisX.setColor(1, new Color3f(1f, 0f, 0f));
		axisX.setUserData("axeX");

		LineArray axisY = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisY.setCoordinate(0, new Point3f(0f, 0f, 0f));
		axisY.setCoordinate(1, new Point3f(0f, 10f, 0f));
		axisY.setColor(0, new Color3f(0f, 1f, 0f));
		axisY.setColor(1, new Color3f(0f, 1f, 0f));
		axisY.setUserData("axeY");

		LineArray axisZ = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisZ.setCoordinate(0, new Point3f(0f, 0f, 0f));
		axisZ.setCoordinate(1, new Point3f(0f, 0f, 10f));
		axisZ.setColor(0, new Color3f(0f, 0f, 1f));
		axisZ.setColor(1, new Color3f(0f, 0f, 1f));
		axisZ.setUserData("axeZ");
		/*
		 * Creation du groupe de transformation a partir de l'objet
		 * SimpleUniverse. On recupere en fait la position de la camera qui sera
		 * modifiee a la souris grace au TransformGroup tg
		 */
		TransformGroup tg = su.getViewingPlatform().getViewPlatformTransform();
		/*
		 * Creation comportement navigation (rotation) a la souris
		 */
		MouseRotate mouseRotate = new MouseRotate(MouseBehavior.INVERT_INPUT);
		mouseRotate.setTransformGroup(tg);
		/*
		 * Champ d'action de la souris (rotation)
		 */
		mouseRotate
				.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000));
		/*
		 * Ajout du comportement rotation a la souris a l'objet parent de la
		 * scene 3D
		 */
		parent.addChild(mouseRotate);
		/*
		 * Creation comportement navigation (translation) a la souris
		 */
		MouseTranslate mouseTranslate = new MouseTranslate(
				MouseBehavior.INVERT_INPUT);
		mouseTranslate.setTransformGroup(tg);
		/*
		 * Champ d'action de la souris (translation)
		 */
		mouseTranslate.setSchedulingBounds(new BoundingSphere(new Point3d(),
				1000));
		/*
		 * Ajout du comportement translation a la souris a l'objet parent de la
		 * scene 3D
		 */
		parent.addChild(mouseTranslate);
		/*
		 * Creation comportement navigation (zoom) a la souris
		 */
		MouseWheelZoom mouseZoom = new MouseWheelZoom(
				MouseBehavior.INVERT_INPUT);
		mouseZoom.setFactor(0.05);
		mouseZoom.setTransformGroup(tg);
		/*
		 * Champ d'action de la souris (zoom)
		 */
		mouseZoom.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000));
		/*
		 * Ajout du comportement zoom a la souris a l'objet parent de la scene
		 * 3D
		 */
		parent.addChild(mouseZoom);
		/*
		 * Ajout des trois axes a l'objet parent de la scene 3D
		 */
		parent.addChild(new Shape3D(axisX));
		parent.addChild(new Shape3D(axisY));
		parent.addChild(new Shape3D(axisZ));

		return parent;
	}

	public static boolean estSelectionne(Noeud n) {
		/*
		 * Determine si un noeud est "selectionne", c'est-a-dire s'il doit
		 * apparaitre plus clairement
		 */
		if (n.getPosition().startsWith(Interface.getPlaceSubSequent())) {
			return true;
		}
		return false;
	}

	public void dessinerPoint(Noeud n, int k) {
		parent.addChild(new Point(n, this).creerPoint(k));
	}

	public void dessinerSegment(Noeud n1, Noeud n2, boolean couple) {
		/*
		 * Creation du segment entre deux noeuds
		 */
		LineArray segment = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		segment.setCoordinate(0, new Point3f(n1.getX(), n1.getY(), n1.getZ()));
		segment.setCoordinate(1, new Point3f(n2.getX(), n2.getY(), n2.getZ()));
		segment.setUserData("segment");
		/*
		 * On rend possible l'ajout apres generation du graphe si cela est
		 * souhaite
		 */
		if (!couple) {
			segment.setColor(0, COULEUR_ARRETE);
			segment.setColor(1, COULEUR_ARRETE);
			parent.addChild(new Shape3D(segment));
		} else {
			segment.setColor(0, COULEUR_COUPLE);
			segment.setColor(1, COULEUR_COUPLE);
			temp.addChild(new Shape3D(segment));
		}
	}

	public void dessinerCourbe(Noeud n1, Noeud n2) {
		/*
		 * Creation du lien entre deux formules d'un couple
		 */
		if (n1 != n2) {
			/*
			 * Cas general
			 */
			Noeud pointIntermediaire = new Noeud((n1.getX() + n2.getX()) / 2,
					(float) (n1.getY() + 1), (n1.getZ() + n2.getZ()) / 2);
			dessinerSegment(n1, pointIntermediaire, true);
			dessinerSegment(n2, pointIntermediaire, true);
		} else {
			/*
			 * Cas d'une formule TRUE
			 */
			Noeud pointIntermediaire1 = new Noeud((float) (n1.getX() - (0.25)),
					(float) (n1.getY() + 1), n1.getZ());
			Noeud pointIntermediaire2 = new Noeud((float) (n1.getX() + (0.25)),
					(float) (n1.getY() + 1), n1.getZ());
			dessinerSegment(n1, pointIntermediaire1, true);
			dessinerSegment(n2, pointIntermediaire2, true);
			dessinerSegment(pointIntermediaire1, pointIntermediaire2, true);
		}
	}

	@SuppressWarnings("static-access")
	public TreeSet<Noeud> dessinerOR(Noeud n, int k) {
		/*
		 * Creation des points engendres par le "OR" et affichage de ces
		 * derniers
		 */
		Noeud n1 = new Noeud((float) (n.getX() - (1 / (Math.pow(ECHELLE, n
				.getProfondeur())))), (float) (n.getY() + (1 / (Math.pow(
				ECHELLE, n.getProfondeur())))), n.getZ(),
				n.getProfondeur() + 1, n.getProfondeurInitiale() + 1, n
						.getPosition()
						+ "0", k);
		Noeud n2 = new Noeud((float) (n.getX() + (1 / (Math.pow(ECHELLE, n
				.getProfondeur())))), (float) (n.getY() + (1 / (Math.pow(
				ECHELLE, n.getProfondeur())))), n.getZ(),
				n.getProfondeur() + 1, n.getProfondeurInitiale() + 1, n
						.getPosition()
						+ "1", k);

		dessinerPoint(n1, k);
		dessinerPoint(n2, k);
		dessinerSegment(n, n1, false);
		dessinerSegment(n, n2, false);
		dessinerSegment(n1, n2, false);
		/*
		 * Creation d'un double triangle (deux faces) pour colorier la structure
		 * engendree par le "OR"
		 */
		Shape3D shape = new Triangle(n, n1, n2, this).creerTriangle("or", k,
				false);
		shape.setCapability(shape.ALLOW_GEOMETRY_WRITE);
		shape.setCapability(shape.ALLOW_GEOMETRY_READ);

		Shape3D shape2 = new Triangle(n, n2, n1, this).creerTriangle("or", k,
				false);
		shape2.setCapability(shape.ALLOW_GEOMETRY_WRITE);
		shape2.setCapability(shape.ALLOW_GEOMETRY_READ);

		parent.addChild(shape);
		parent.addChild(shape2);
		/*
		 * Mise a jour des donnees generales
		 */
		if (n.getProfondeur() == niveauMax) {
			niveauMax++;
		}
		listeNoeud.add(n1);
		listeNoeud.add(n2);
		/*
		 * On renvoie les points crees pour continuer l'algorithme
		 */
		TreeSet<Noeud> liste = new TreeSet<Noeud>();
		liste.add(n1);
		liste.add(n2);
		return liste;
	}

	@SuppressWarnings("static-access")
	public TreeSet<Noeud> dessinerAND(Noeud n, int k, boolean deriv) {
		/*
		 * Creation des points engendres par le "AND" et affichage de ces
		 * derniers
		 */
		Noeud n1 = new Noeud(n.getX(), (float) (n.getY() + (1 / (Math.pow(
				ECHELLE, n.getProfondeur())))), (float) (n.getZ() - (1 / (Math
				.pow(ECHELLE, n.getProfondeur())))), n.getProfondeur() + 1, n
				.getProfondeurInitiale() + 1, n.getPosition() + "1", k);
		Noeud n2 = new Noeud(n.getX(), (float) (n.getY() + (1 / (Math.pow(
				ECHELLE, n.getProfondeur())))), (float) (n.getZ() + (1 / (Math
				.pow(ECHELLE, n.getProfondeur())))), n.getProfondeur() + 1, n
				.getProfondeurInitiale() + 1, n.getPosition() + "0", k);

		dessinerPoint(n1, k);
		dessinerPoint(n2, k);
		dessinerSegment(n, n1, false);
		dessinerSegment(n, n2, false);
		dessinerSegment(n1, n2, false);
		/*
		 * Creation d'un double triangle (deux faces) pour colorier la structure
		 * engendree par le "AND"
		 */
		Shape3D shape = new Triangle(n, n1, n2, this).creerTriangle(deriv
				+ "and", k, deriv);
		shape.setCapability(shape.ALLOW_GEOMETRY_WRITE);
		shape.setCapability(shape.ALLOW_GEOMETRY_READ);

		Shape3D shape2 = new Triangle(n, n2, n1, this).creerTriangle(deriv
				+ "and", k, deriv);
		shape2.setCapability(shape.ALLOW_GEOMETRY_WRITE);
		shape2.setCapability(shape.ALLOW_GEOMETRY_READ);

		parent.addChild(shape);
		parent.addChild(shape2);
		/*
		 * Mise a jour des donnees generales
		 */
		if (n.getProfondeur() == niveauMax) {
			niveauMax++;
		}
		if (deriv) {
			n1.setFormule(n.getFormule());
			n2.setFormule(n.getFormule());
		}
		listeNoeud.add(n1);
		listeNoeud.add(n2);
		/*
		 * On renvoie les points crees pour continuer l'algorithme
		 */
		TreeSet<Noeud> liste = new TreeSet<Noeud>();
		liste.add(n1);
		liste.add(n2);
		return liste;
	}

	public LinkedList<Noeud> dessinerORDerivation(Noeud n, int k) {
		/*
		 * Creation du point engendre par le "OR" reproduit
		 */
		Noeud n1 = new Noeud(n.getX(), (float) (n.getY() + (1 / (Math.pow(
				ECHELLE, n.getProfondeur())))), n.getZ(),
				n.getProfondeur() + 1, n.getProfondeurInitiale() + 1, n
						.getPosition(), k);
		dessinerPoint(n1, k);
		dessinerSegment(n, n1, false);
		/*
		 * Mise a jour des donnees generales
		 */
		if (n.getProfondeur() == niveauMax) {
			niveauMax++;
		}
		n1.setFormule(n.getFormule());
		listeNoeud.add(n1);
		/*
		 * On renvoie les points crees pour continuer l'algorithme
		 */
		LinkedList<Noeud> liste = new LinkedList<Noeud>();
		liste.add(n1);
		return liste;
	}

	@SuppressWarnings( { "static-access", "static-access" })
	public void dessiner(Repere repere) {
		/*
		 * Dessine la fenêtre 3D avec les objets qui vont avec
		 */
		this.setLayout(new BorderLayout());

		Canvas3D canvas3D = new Canvas3D(SimpleUniverse
				.getPreferredConfiguration());
		this.add(canvas3D, BorderLayout.CENTER);

		simpleU = new SimpleUniverse(canvas3D);
		simpleU.getViewingPlatform().setNominalViewingTransform();

		BranchGroup scene = createSceneGraph(simpleU);
		scene.addChild(removable);
		removable.setCapability(removable.ALLOW_DETACH);
		temp.setCapability(temp.ALLOW_DETACH);
		scene.compile();

		simpleU.addBranchGraph(scene);
		locale = simpleU.getLocale();

		frame = new MainFrame(repere, 512, 512);
	}

	public void fermerFenetre() {
		frame.setVisible(false);
	}

	@SuppressWarnings("static-access")
	public void rajouterCouple() {
		/*
		 * Rajoute des liens entre les couples selectionnes
		 */
		temp.compile();
		try {
			locale.replaceBranchGraph(removable, temp);
		} catch (CapabilityNotSetException e) {
			System.out.println("Capability Not Set Exception : " + e);
		}
		removable = temp;
		temp = new BranchGroup();
		temp.setCapability(temp.ALLOW_DETACH);
	}

	@SuppressWarnings("static-access")
	public void effacerCouple() {
		/*
		 * Supprime les couples et les liens entre ceux-ci
		 */
		temp = new BranchGroup();
		temp.setCapability(temp.ALLOW_DETACH);
		temp.compile();
		try {
			locale.replaceBranchGraph(removable, temp);
		} catch (CapabilityNotSetException e) {
			System.out.println("Capability Not Set Exception : " + e);
		}
		removable = temp;
		temp = new BranchGroup();
		temp.setCapability(temp.ALLOW_DETACH);
	}

	public Point getPoint(TreeSet<Point> liste, int k) {
		/*
		 * Donne le k-ème element de la liste triee 'liste'
		 */
		Iterator i = liste.iterator();
		int j = 0;
		Point temp = new Point();
		while (j != k) {
			temp = (Point) i.next();
			j++;
		}
		return temp;
	}

	public void actualiserPoints() {
		/*
		 * Actualise la couleur des points que se soit pour See Sequent ou bien
		 * pour les couples
		 */
		for (Point p : listePointsTotal) {
			if (!p.getUserData().startsWith("neutre")
					&& (p.getUserData().equals(Interface.getNumeroNode() + "") || Interface
							.getListeNumeroPoints().contains(
									Integer.valueOf(p.getUserData())))) {
				p.changerCouleur(COULEUR_COUPLE);
			} else if ((p.getN().getNumeroSequent() + p.getN().getPosition())
					.startsWith(Interface.getNumeroSequent()
							+ Interface.getPlaceSubSequent())) {
				p.changerCouleur(COULEUR_CHERCHE);
			} else {
				p.changerCouleur(COULEUR_ARRETE);
			}
		}
	}

	public void actualiser() {
		/*
		 * Permet de changer en direct les couleurs des triangles
		 */
		for (int k = 0; k < parent.numChildren(); k++) {
			Object o = parent.getChild(k);
			/*
			 * Cas des triangles
			 */
			if (o instanceof Shape3D) {
				Shape3D shape = (Shape3D) o;
				Enumeration e = shape.getAllGeometries();
				while (e.hasMoreElements()) {
					Geometry g = (Geometry) e.nextElement();
					/*
					 * On remet la couleur de base à tous les triangles
					 */
					if (!g.getUserData().toString().equals("segment")
							&& !g.getUserData().toString().contains("axe")
							&& !g.getUserData().toString().equals("point")) {
						TriangleArray t = (TriangleArray) g;
						for (int i = 0; i < 3; i++) {
							/*
							 * Cas normal
							 */
							if (t.getUserData().toString().contains("false")) {
								if (t.getUserData().toString().endsWith("or")) {
									t.setColor(i, COULEUR_OR);
								} else {
									t.setColor(i, COULEUR_AND);
								}
								/*
								 * Cas d'un triangle obtenu par derivation
								 */
							} else {
								t.setColor(i, COULEUR_DERIV);
							}
						}
					}
					/*
					 * Puis on fait ressortir ceux qui sont recherches
					 */
					if (g.getUserData().toString().startsWith(
							Interface.getNumeroSequent()
									+ Interface.getPlaceSubSequent())) {
						TriangleArray t = (TriangleArray) g;
						for (int i = 0; i < 3; i++) {
							t.setColor(i, COULEUR_CHERCHE);
						}
					}
				}
			}
		}
		/*
		 * On traite egalement le cas des points
		 */
		actualiserPoints();
	}

	public static void main(String[] args) {
		new MainFrame(new Repere(), 512, 512);
	}
}
