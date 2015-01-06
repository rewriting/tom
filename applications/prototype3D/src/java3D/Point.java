package java3D;

/*
 * Point class.
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

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Sphere;

import projet.Noeud;

public class Point implements Comparable<Point> {

	private Noeud n;

	private Repere repere;

	private Appearance app;

	private ColoringAttributes color;

	private Sphere sphere;
	
	public Point() {
		n = new Noeud();
		repere = new Repere();
		app = new Appearance();
		color = new ColoringAttributes();
		sphere = new Sphere();
	}

	public Point(Noeud n1, Repere rep) {
		n = n1;
		repere = rep;
		app = new Appearance();
		color = new ColoringAttributes();
		sphere = new Sphere();
	}

	public void setColor(ColoringAttributes c) {
		app.setColoringAttributes(c);
	}

	public Noeud getN() {
		return n;
	}

	public void setN(Noeud noeud) {
		n = noeud;
	}

	public Sphere getSphere() {
		return sphere;
	}

	public String getUserData() {
		return sphere.getUserData().toString();
	}

	@SuppressWarnings("static-access")
	public TransformGroup creerPoint(int k) {
		/*
		 * On prepare la possibilite de changer la couleur du point
		 */
		app.setCapability(app.ALLOW_COLORING_ATTRIBUTES_READ);
		app.setCapability(app.ALLOW_COLORING_ATTRIBUTES_WRITE);
		color.setCapability(color.ALLOW_COLOR_READ);
		color.setCapability(color.ALLOW_COLOR_WRITE);
		color.setColor(repere.COULEUR_ARRETE);
		app.setColoringAttributes(color);
		/*
		 * Creation de la translation pour placer notre sphere ou on veut
		 */
		Transform3D translate = new Transform3D();
		translate.set(new Vector3f(n.getX(), n.getY(), n.getZ()));
		TransformGroup TG = new TransformGroup(translate);
		sphere = new Sphere((float) (1 / (Math.pow(repere.getEchelle(), n
				.getProfondeur() + 4))), app);
		sphere.setUserData("neutre");
		TG.addChild(sphere);
		/*
		 * Mise a jour des donnees generales
		 */
		if (n.getProfondeur() > repere.getNiveauMax()) {
			repere.setNiveauMax();
		}
		repere.addListeNoeud(n);
		repere.addListePoints(this);
		repere.addPoint(this);

		return TG;
	}

	public void changerCouleur(Color3f couleur) {
		color.setColor(couleur);
		app.setColoringAttributes(color);
	}

	public void addUserData(String s) {
		sphere.setUserData(s);
	}

	/*
	 * Permet de comparer deux points en fonction des noeuds de ces points
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Point p) {
		// TODO Auto-generated method stub
		return n.compareTo(p.getN());
	}

}
