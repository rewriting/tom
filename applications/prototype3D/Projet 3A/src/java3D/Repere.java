package java3D;

import projet.Noeud;
import tom.Sequent;
import fenetre.Interface;

import java.applet.Applet;
import java.awt.*;
import java.util.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.media.j3d.Locale;
import javax.vecmath.*;

public class Repere extends Applet {

	private static final long serialVersionUID = 1L;

	private static final double ECHELLE = 2;

	private Frame frame;

	private BranchGroup parent = new BranchGroup();

	private static BranchGroup conteneur = new BranchGroup();

	private TransformGroup mouseTransform = new TransformGroup();

	private LinkedList<Noeud> listeNoeud = new LinkedList<Noeud>();

	public static final Color3f COULEUR = new Color3f(0.7f, 0.5f, 0.2f);
	
	public static final Color3f COULEUR_OR = new Color3f(0.7f, 0.5f, 0.7f);
	
	public static final Color3f COULEUR_AND = new Color3f(0.3f, 0.5f, 0.3f);

	public static final Color3f COULEUR_DERIV = new Color3f(0.15f, 0.15f,
			0.15f);

	private int niveauMax = 1;

	private static ViewingPlatform camera = null;

	private static Viewer view = null;

	private SimpleUniverse simpleU;

	private Locale locale = null;

	private static boolean premiereFenetre = true;

	public Repere() {
	}

	public LinkedList<Noeud> getListeNoeud() {
		return listeNoeud;
	}

	public static BranchGroup getConteneur() {
		return conteneur;
	}

	public static double getEchelle() {
		return ECHELLE;
	}

	public static Color3f getCouleur() {
		return COULEUR;
	}

	public static Color3f getCouleurDerivation() {
		return COULEUR_DERIV;
	}

	public int getNiveauMax() {
		return niveauMax;
	}

	/**
	 * Creation de la scene 3D qui contient tous les objets 3D
	 * 
	 * @return scene 3D
	 */
	public BranchGroup createSceneGraph() {
		/*
		 * Creation des axes qui serviront de repere
		 */
		LineArray axisX = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisX.setCoordinate(0, new Point3f(0f, 0f, 0f));
		axisX.setCoordinate(1, new Point3f(10f, 0f, 0f));
		axisX.setColor(0, new Color3f(1f, 0f, 0f));
		axisX.setColor(1, new Color3f(1f, 0f, 0f));

		LineArray axisY = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisY.setCoordinate(0, new Point3f(0f, 0f, 0f));
		axisY.setCoordinate(1, new Point3f(0f, 10f, 0f));
		axisY.setColor(0, new Color3f(0f, 1f, 0f));
		axisY.setColor(1, new Color3f(0f, 1f, 0f));

		LineArray axisZ = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisZ.setCoordinate(0, new Point3f(0f, 0f, 0f));
		axisZ.setCoordinate(1, new Point3f(0f, 0f, 10f));
		axisZ.setColor(0, new Color3f(0f, 0f, 1f));
		axisZ.setColor(1, new Color3f(0f, 0f, 1f));

		/*
		 * Le groupe de transformation sera modifie par le comportement de la
		 * souris
		 */
		mouseTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		mouseTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		/*
		 * Creation comportement rotation a la souris
		 */
		MouseRotate rotate = new MouseRotate(mouseTransform);
		rotate.setSchedulingBounds(new BoundingSphere());
		parent.addChild(rotate);

		/*
		 * Creation comportement deplacement a la souris
		 */
		MouseTranslate translate = new MouseTranslate(mouseTransform);
		translate.setSchedulingBounds(new BoundingSphere());
		parent.addChild(translate);

		/*
		 * Creation comportement zoom a la souris
		 */
		MouseWheelZoom zoom = new MouseWheelZoom(mouseTransform);
		zoom.setSchedulingBounds(new BoundingSphere());
		parent.addChild(zoom);

		mouseTransform.addChild(new Shape3D(axisX));
		mouseTransform.addChild(new Shape3D(axisY));
		mouseTransform.addChild(new Shape3D(axisZ));

		parent.addChild(mouseTransform);

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
		/*
		 * Creation d'une apparence pour la sphere
		 */
		Appearance app = new Appearance();
		ColoringAttributes color = new ColoringAttributes();
		if (estSelectionne(n) && k == Interface.getPositionSequent()) {
			color.setColor(COULEUR);
		} else {
			color.setColor(0.6f, 0.6f, 0.6f);
		}
		app.setColoringAttributes(color);

		/*
		 * Creation de la translation pour placer notre sphere ou on veut
		 */
		Transform3D translate = new Transform3D();
		translate.set(new Vector3f(n.getX(), n.getY(), n.getZ()));
		TransformGroup TG = new TransformGroup(translate);
		TG.addChild(new Sphere(
				(float) (1 / (Math.pow(2, n.getProfondeur() + 4))), app));

		/*
		 * Mise a jour des donnees generales
		 */
		if (n.getProfondeur() > niveauMax) {
			niveauMax++;
		}
		listeNoeud.add(n);
		mouseTransform.addChild(TG);
	}

	public void dessinerSegment(Noeud n1, Noeud n2) {
		/*
		 * Creation du segment entre deux noeuds
		 */
		LineArray segment = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		segment.setCoordinate(0, new Point3f(n1.getX(), n1.getY(), n1.getZ()));
		segment.setCoordinate(1, new Point3f(n2.getX(), n2.getY(), n2.getZ()));
		segment.setColor(0, new Color3f(0.6f, 0.6f, 0.6f));
		segment.setColor(1, new Color3f(0.6f, 0.6f, 0.6f));

		mouseTransform.addChild(new Shape3D(segment));
	}

	public TreeSet<Noeud> dessinerOR(Noeud n, int k) {
		/*
		 * Creation des points engendres par le "OR" et affichage de ces
		 * derniers
		 */
		Noeud n1 = new Noeud((float) (n.getX() - (1 / (Math.pow(ECHELLE, n
				.getProfondeur())))), (float) (n.getY() + (1 / (Math.pow(
				ECHELLE, n.getProfondeur())))), n.getZ(),
				n.getProfondeur() + 1, n.getProfondeurInitiale() + 1, "OR", n
						.getPosition()
						+ "0", k);
		Noeud n2 = new Noeud((float) (n.getX() + (1 / (Math.pow(ECHELLE, n
				.getProfondeur())))), (float) (n.getY() + (1 / (Math.pow(
				ECHELLE, n.getProfondeur())))), n.getZ(),
				n.getProfondeur() + 1, n.getProfondeurInitiale() + 1, "OR", n
						.getPosition()
						+ "1", k);
		dessinerPoint(n1, k);
		dessinerPoint(n2, k);
		dessinerSegment(n, n1);
		dessinerSegment(n, n2);
		dessinerSegment(n1, n2);

		/*
		 * Creation d'un double triangle (deux faces) pour colorier la structure
		 * engendree par le "OR"
		 */
		mouseTransform
				.addChild(new Triangle(n, n1, n2, this).creerTriangle("or", k, false));
		mouseTransform
				.addChild(new Triangle(n, n2, n1, this).creerTriangle("or", k, false));

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

	public TreeSet<Noeud> dessinerAND(Noeud n, int k, boolean deriv) {
		/*
		 * Creation des points engendres par le "AND" et affichage de ces
		 * derniers
		 */
		Noeud n1 = new Noeud(n.getX(), (float) (n.getY() + (1 / (Math.pow(
				ECHELLE, n.getProfondeur())))), (float) (n.getZ() - (1 / (Math
				.pow(ECHELLE, n.getProfondeur())))), n.getProfondeur() + 1, n
				.getProfondeurInitiale() + 1, "AND", n.getPosition() + "1", k);
		Noeud n2 = new Noeud(n.getX(), (float) (n.getY() + (1 / (Math.pow(
				ECHELLE, n.getProfondeur())))), (float) (n.getZ() + (1 / (Math
				.pow(ECHELLE, n.getProfondeur())))), n.getProfondeur() + 1, n
				.getProfondeurInitiale() + 1, "AND", n.getPosition() + "0", k);
		dessinerPoint(n1, k);
		dessinerPoint(n2, k);
		dessinerSegment(n, n1);
		dessinerSegment(n, n2);
		dessinerSegment(n1, n2);

		/*
		 * Creation d'un double triangle (deux faces) pour colorier la structure
		 * engendree par le "AND"
		 */
		mouseTransform
				.addChild(new Triangle(n, n1, n2, this).creerTriangle("and", k, deriv));
		mouseTransform
				.addChild(new Triangle(n, n2, n1, this).creerTriangle("and", k, deriv));

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
				n.getProfondeur() + 1, n.getProfondeurInitiale() + 1, "OR", n
						.getPosition()
						+ "1", k);
		dessinerPoint(n1, k);
		dessinerSegment(n, n1);

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
		/*
		 * if (premiereFenetre) { simpleU = new SimpleUniverse(canvas3D);
		 * simpleU.getViewingPlatform().setNominalViewingTransform();
		 * setCamera(); view = simpleU.getViewer(); premiereFenetre = false; }
		 * else { simpleU = new SimpleUniverse(camera, view);
		 * System.out.println("camera : " + camera); }
		 */
		/*
		 * camera.setCapability(ViewPlatform.ALLOW_POLICY_READ);
		 * camera.setCapability(ViewPlatform.ALLOW_POLICY_WRITE);
		 */
		/*
		 * simpleU.removeAllLocales();
		 * simpleU.getViewingPlatform().removeAllChildren();
		 * System.out.println("parent : " + camera.getParent());
		 * simpleU.getViewingPlatform().setViewPlatform(camera); }
		 */

		/*
		 * scene.addChild(initialiser()); scene.addChild(conteneur);
		 */
		BranchGroup scene = createSceneGraph();
		/* scene.addChild(createSceneGraph()); */
		// scene.setCapability(BranchGroup.ALLOW_DETACH);
		scene.compile();

		simpleU.addBranchGraph(scene);
		// locale = simpleU.getLocale();

		frame = new MainFrame(repere, 512, 512);
	}

	public void fermerFenetre() {
		frame.setVisible(false);
	}

	public void actualiser2() {
		System.out.println("fonction appelee");
		System.out.println("place subsequent : "
				+ Interface.getPlaceSubSequent());
		for (int k = 0; k < mouseTransform.numChildren(); k++) {
			Object o = mouseTransform.getChild(k);
			// System.out.println(k + " type : " + o.toString());
			if (o instanceof Shape3D) {
				// System.out.println("Shape3D trouve");
				Shape3D shape = (Shape3D) o;
				// System.out.println("getUerData : "
				// + shape.getUserData().toString());
				if (!shape.getUserData().toString().equals("neutre")) {
					System.out.println("trouve");
					/*
					 * shape.setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE);
					 * shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
					 * shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
					 * shape.setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_READ);
					 */
					Appearance app = new Appearance();
					ColoringAttributes color = new ColoringAttributes();
					color.setColor(new Color3f(0.5f, 0.5f, 0.5f));
					app.setColoringAttributes(color);
					/*
					 * Material material = new Material(new Color3f(0.0f, 0.0f,
					 * 1.0f), new Color3f(0.0f, 0.0f, 0.0f), new Color3f( 0.5f,
					 * 0.5f, 0.5f), new Color3f(1.0f, 1.0f, 1.0f), 64);
					 * app.setMaterial(material);
					 */
					shape.setAppearance(app);
					shape.setUserData(Interface.getPlaceSubSequent());
				}
			}
		}
	}

	/*
	 * public void actualiser() { BranchGroup ancienneScene = new BranchGroup();
	 * ancienneScene = conteneur; BranchGroup nouvelleScene = new BranchGroup();
	 * nouvelleScene.setCapability(BranchGroup.ALLOW_DETACH); nouvelleScene =
	 * Sequent.genererGraphe(Interface.getListeSequent(),
	 * Interface.getListeRepere().getLast()); nouvelleScene.compile();
	 * locale.replaceBranchGraph(ancienneScene, Sequent.genererGraphe(
	 * Interface.getListeSequent(), Interface.getListeRepere() .getLast())); //
	 * conteneur = nouvelleScene; }
	 */

	public ViewingPlatform getCamera() {
		return camera;
	}

	public void setCamera() {
		camera = simpleU.getViewingPlatform();
	}

	public static void main(String[] args) {
		new MainFrame(new Repere(), 512, 512);
	}
}
