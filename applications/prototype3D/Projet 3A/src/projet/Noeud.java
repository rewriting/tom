package projet;

import tom.donnees.types.*;

public class Noeud implements Comparable<Noeud> {

	private final float[] coordonnees = new float[3];

	private int profondeurInitiale;

	private int profondeur;

	private boolean estFinal;

	private String position = "";

	private int numeroSequent;

	private Formule formule;

	public Noeud() {
		coordonnees[0] = 0;
		coordonnees[1] = 0;
		coordonnees[2] = 0;
		estFinal = false;
		position = "0";
		numeroSequent = 0;
	}

	public Noeud(float x, float y, float z) {
		coordonnees[0] = x;
		coordonnees[1] = y;
		coordonnees[2] = z;
		estFinal = false;
		position = "0";
		numeroSequent = 0;
		setProfondeurInitiale(1);
		setProfondeur(1);
	}

	public Noeud(float x, float y, float z, int i, int j, String s, int k) {
		coordonnees[0] = x;
		coordonnees[1] = y;
		coordonnees[2] = z;
		estFinal = false;
		position = s;
		numeroSequent = k;
		setProfondeurInitiale(i);
		setProfondeur(j);
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
	 * Permet de comparer deux points entre eux dans l'espace. N est "plus
	 * petit" que M si son X est plus petit et en cas d'egalite, si son Z est
	 * plus petit sinon, ce sont les memes points (on ne compare que des points
	 * situes dans un meme plan) (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Noeud n) {
		// TODO Auto-generated method stub
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
	}
}
