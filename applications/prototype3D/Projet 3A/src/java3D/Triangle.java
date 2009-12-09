package java3D;

import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleArray;

import fenetre.Interface;

import projet.Noeud;

public class Triangle {
	
	private Noeud[] sommets = new Noeud[3];
	
	private Repere repere;
	
	public Triangle() {
		for (int i=0; i<3; i++) {
			sommets[i] = new Noeud();
		}
		repere = new Repere();
	}
	
	public Triangle(Noeud n1, Noeud n2, Noeud n3, Repere r) {
		sommets[0] = n1;
		sommets[1] = n2;
		sommets[2] = n3;
		repere = r;
	}
	
	public Shape3D creerTriangle(String s, int k, boolean deriv) {
		TriangleArray triangle = new TriangleArray(3,
				LineArray.COORDINATES | LineArray.COLOR_3);
		triangle.setCoordinate(0, sommets[0].getCoordonnees());
		triangle.setCoordinate(1, sommets[1].getCoordonnees());
		triangle.setCoordinate(2, sommets[2].getCoordonnees());
		for (int i = 0; i < 3; i++) {
			if (Repere.estSelectionne(sommets[0]) && k == Interface.getPositionSequent()) {
				triangle.setColor(i, repere.COULEUR);
			} else {
				if (deriv) {
					triangle.setColor(i, repere.COULEUR_DERIV);
				} else if (s.equals("or")) {
					triangle.setColor(i, repere.COULEUR_OR);
				} else {
					triangle.setColor(i, repere.COULEUR_AND);
				}
			}
		}
		return new Shape3D(triangle);
	}
	
}
