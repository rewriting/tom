package projet;

/*
 * Noeud class.
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

import tom.donnees.types.*;
import java3D.Repere;

public class Noeud implements Comparable<Noeud> {

	private final float[] coordonnees = new float[3];

	private int profondeurInitiale;

	private int profondeur;

	private int numeroSequent;

	private int numeroNoeud;

	private boolean estFinal;

	private boolean estDansLigneFinale;

	private String position = "";

	private Formule formule;

	public Noeud() {
		coordonnees[0] = 0;
		coordonnees[1] = 0;
		coordonnees[2] = 0;
		estFinal = false;
		estDansLigneFinale = false;
		position = "0";
		numeroSequent = 0;
		numeroNoeud = -1;
	}

	public Noeud(float x, float y, float z) {
		coordonnees[0] = x;
		coordonnees[1] = y;
		coordonnees[2] = z;
		estFinal = false;
		estDansLigneFinale = false;
		position = "0";
		numeroSequent = 0;
		numeroNoeud = -1;
		setProfondeurInitiale(1);
		setProfondeur(1);
	}

	public Noeud(float x, float y, float z, int i, int j, String s, int k) {
		coordonnees[0] = x;
		coordonnees[1] = y;
		coordonnees[2] = z;
		estFinal = false;
		estDansLigneFinale = false;
		position = s;
		numeroSequent = k;
		numeroNoeud = -1;
		setProfondeurInitiale(i);
		setProfondeur(j);
	}

	public Noeud(Formule f) {
		coordonnees[0] = 0;
		coordonnees[1] = 0;
		coordonnees[2] = 0;
		estFinal = false;
		estDansLigneFinale = false;
		position = "0";
		numeroSequent = 0;
		formule = f;
		numeroNoeud = -1;
	}

	public float[] getCoordonnees() {
		return coordonnees;
	}

	public void setProfondeur(int profondeur) {
		this.profondeur = profondeur;
	}

	public int getProfondeur() {
		return profondeur;
	}

	public String getPosition() {
		return position;
	}

	public int getNumeroSequent() {
		return numeroSequent;
	}

	public Formule getFormule() {
		return formule;
	}

	public void setFormule(Formule f) {
		formule = f;
	}

	public int getNumeroNoeud() {
		return numeroNoeud;
	}

	public void setNumeroNoeud(int n) {
		numeroNoeud = n;
	}

	public void setProfondeurInitiale(int profondeurInitiale) {
		this.profondeurInitiale = profondeurInitiale;
	}

	public int getProfondeurInitiale() {
		return profondeurInitiale;
	}

	public void setEstFinal() {
		this.estFinal = !this.estFinal;
	}

	public boolean getEstFinal() {
		return estFinal;
	}

	public void setEstDansLigneFinale(boolean b) {
		this.estDansLigneFinale = b;
	}

	public boolean getEstDansLigneFinale() {
		return estDansLigneFinale;
	}

	public float getX() {
		return coordonnees[0];
	}

	public float getY() {
		return coordonnees[1];
	}

	public float getZ() {
		return coordonnees[2];
	}

	/*
	 * Permet de comparer deux points entre eux dans l'espace. A Y donne, N est
	 * "plus petit" que M si son X est plus petit et en cas d'egalite, si son Z
	 * est plus petit sinon, ce sont les memes points (on ne compare que des
	 * points situes dans un meme plan). Sinon, ils sont rangés par Y croissant.
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Noeud n) {
		// TODO Auto-generated method stub
		if (this.getY() == n.getY()) {
			if (this.getX() < n.getX()) {
				return -1;
			} else if (this.getX() > n.getX()) {
				return 1;
			} else {
				if (this.getZ() < n.getZ()) {
					return -1;
				} else if (this.getZ() > n.getZ()) {
					return 1;
				} else {
					return 0;
				}
			}
		} else if (this.getY() < n.getY()) {
			return -1;
		} else {
			return 1;
		}
	}
}
