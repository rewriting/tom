/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 *	Etat.java
 *	par Blaise Potard et Emmanuel Hainry
 *
 *	Dessin de l'etat global de l'automate cellulaire 2D.
 */

package cell;

import java.awt.*;
import javax.swing.*;

class Etat extends JPanel {
  private static final long serialVersionUID = 1L;
  
	Matrice matr;
	Color fore, back;
	Color rose, rouge, bistre, bleu, lavande, jaune, jaunepale, bleu1, bleu2, bleu3, bleu4, jaune1, jaune2, jaune3, jaune4, vert1, vert2, vert3, vert4;
	Reglages reg;
	int x0;
	int y0;
	float scaleX;
	float scaleY;
	int nblignes, nbcols;
	int largeur, hauteur;

	Etat(Matrice matrix, Reglages r) {
		matr = matrix;
		fore = new Color(245, 245, 235);
		back = Color.white;
		rose = new Color(255, 155, 155);
		rouge = new Color(255, 0, 0);
		bleu = new Color(0, 0, 255);
		lavande = new Color(155, 155, 255); //bleu pale
		jaune = new Color(255, 255, 0);
		jaunepale = new Color(255, 255, 165);
		bistre = new Color(128, 16, 16);
		bleu1 = new Color(180, 180, 255);
		bleu2 = new Color(120, 120, 255);
		bleu3 = new Color(60, 60, 255);
		bleu4 = new Color(0, 0, 255);
		jaune1 = new Color(255, 255, 180);
		jaune2 = new Color(255, 255, 120);
		jaune3 = new Color(255, 255, 60);
		jaune4 = new Color(255, 255, 0);
		vert1 = new Color(180, 255, 180);
		vert2 = new Color(120, 255, 120);
		vert3 = new Color(60, 255, 60);
		vert4 = new Color(0, 255, 0);
		reg = r;
	}

	Etat() {
	}

	public void dessine() {
		affiche(getGraphics());
	}

	public void ChangeEtat(Matrice matrix) {
		matr = matrix;
	}

	public void paintComponent(Graphics g) {
		affiche(g);
	}

	private void dessinePoint(Graphics g, int x, int y, Color c) {
		int x1 = (int)(x * scaleX);
		int y1 = (int)(y * scaleY);
		int x2 = (int)((x + 1) * scaleX);
		int y2 = (int)((y + 1) * scaleY);
		g.setColor(c);
		g.fillRect(x0 + x1, hauteur - (y0 + y2), x2 - x1, y2 - y1);
	}

	private void dessineCadre(Graphics g, int x, int y, Color c) {
		int x1 = (int)(x * scaleX);
		int y1 = (int)(y * scaleY);
		int x2 = (int)((x + 1) * scaleX);
		int y2 = (int)((y + 1) * scaleY);
		g.setColor(c);
		g.drawRect(x0 + x1, hauteur - (y0 + y2), x2 - x1, y2 - y1);
	}

	private void dessineLigne(Graphics g, int i1, int j1, int i2, int j2,
	        Color c) {
		int x1 = (int)(i1 * scaleX);
		int y1 = (int)(j1 * scaleY);
		int x2 = (int)(i2 * scaleX);
		int y2 = (int)(j2 * scaleY);
		g.setColor(c);
		g.drawLine(x0 + x1, hauteur - (y0 + y1), x0 + x2, hauteur - (y0 + y2));
	}
        private void dessineDemiLigne(Graphics g, int i1, int j1, int i2, int j2,
	        Color c) {
        int x1 = (int)((i1 * scaleX)/2);
		int y1 = (int)((j1 * scaleY)/2);
		int x2 = (int)((i2 * scaleX)/2);
		int y2 = (int)((j2 * scaleY)/2);
		g.setColor(c);
		g.drawLine(x0 + x1, hauteur - (y0 + y1), x0 + x2, hauteur - (y0 + y2));
        }
	private void nettoie(Graphics g, Color c) {
		largeur = getSize().width;
		hauteur = getSize().height;
		g.setColor(c);
		g.fillRect(0, 0, largeur, y0);
		g.fillRect(0, 0, x0, hauteur);
		g.fillRect((int)(x0 + scaleX * nbcols), 0, largeur, hauteur);
		g.fillRect(0, (int)(y0 + scaleY * nblignes), largeur, hauteur);
	}

	public void affiche(Graphics g) {
		int i, j;

		nblignes = matr.nblignes;
		nbcols = matr.nbcols;
		largeur = getSize().width;
		hauteur = getSize().height;

		/* pour obtenir un resultat (a peu pres) harmonieux,
		 * on essaie de garder des pixels de même dimensions horizontales
		 * et verticales */

		scaleX = (float) largeur / nbcols;
		scaleY = (float) hauteur / nblignes;

			if (scaleX > scaleY)
				scaleX = scaleY;
			else
				scaleY = scaleX;
				
		x0 = (int)((largeur - scaleX * nbcols) / 2);
		y0 = (int)((hauteur - scaleY * nblignes) / 2);

		nettoie(g, back);
		if (reg.style == Reglages.DONNEES) {
			for (i = 0; i < nblignes; i++) {
				for (j = 0; j < nbcols; j++) {
					CaseEtat a = new CaseEtat(matr.matrice[i][j]);
					switch (a.donnee) {
					case 0:
						dessinePoint(g, j, i, back);
						break;
					case 1:
						dessinePoint(g, j, i, rouge);
						break;
					case 2:
						dessinePoint(g, j, i, rouge);
						break;
					case 3:
						dessinePoint(g, j, i, bleu);
						break;
					case 4:
						dessinePoint(g, j, i, lavande);
						break;
					case 5:
						dessinePoint(g, j, i, jaune);
						break;
					case 6:
						dessinePoint(g, j, i, jaunepale);
						break;
					case 7:
						dessinePoint(g, j, i, bistre);
						break;
					case 8:
						dessinePoint(g, j, i, bleu1);
						break;
					case 9:
						dessinePoint(g, j, i, bleu2);
						break;
					case 10:
						dessinePoint(g, j, i, bleu3);
						break;
					case 11:
						dessinePoint(g, j, i, bleu4);
						break;
					case 12:
						dessinePoint(g, j, i, jaune1);
						break;
					case 13:
						dessinePoint(g, j, i, jaune2);
						break;
					case 14:
						dessinePoint(g, j, i, jaune3);
						break;
					case 15 :
						dessinePoint(g, j, i, jaune4);
						break;
					case 16 :
						dessinePoint(g, j, i, vert1);
						break;
					case 17 :
						dessinePoint(g, j, i, vert2);
						break;
					case 18 :
						dessinePoint(g, j, i, vert3);
						break;
					case 19 :
						dessinePoint(g, j, i, vert4);
						break;
					default:
						dessinePoint(g, j, i, fore);
					}
					if (reg.grid) {
						dessineCadre(g, j, i, Color.black);
					}
				}
                          }
		}
		if (reg.style == Reglages.COULEUR) {
			for (i = 0; i < nblignes; i++) {
				for (j = 0; j < nbcols; j++) {
                    CaseEtat a = new CaseEtat(matr.matrice[i][j]);
					switch (a.couleur) {
					case 0:
						dessinePoint(g, j, i, back);
						break;
					case 1:
						dessinePoint(g, j, i, rose);
						break;
					case 2:
						dessinePoint(g, j, i, rouge);
						break;
					case 3:
						dessinePoint(g, j, i, bleu);
						break;
					case 4:
						dessinePoint(g, j, i, lavande);
						break;
					case 5:
						dessinePoint(g, j, i, jaune);
						break;
					case 6:
						dessinePoint(g, j, i, jaunepale);
						break;
					case 7:
						dessinePoint(g, j, i, bistre);
						break;
					case 8:
						dessinePoint(g, j, i, bleu1);
						break;
					case 9:
						dessinePoint(g, j, i, bleu2);
						break;
					case 10:
						dessinePoint(g, j, i, bleu3);
						break;
					case 11:
						dessinePoint(g, j, i, bleu4);
						break;
					case 12:
						dessinePoint(g, j, i, jaune1);
						break;
					case 13:
						dessinePoint(g, j, i, jaune2);
						break;
					case 14:
						dessinePoint(g, j, i, jaune3);
						break;
					case 15 :
						dessinePoint(g, j, i, jaune4);
						break;
					case 16 :
						dessinePoint(g, j, i, vert1);
						break;
					case 17 :
						dessinePoint(g, j, i, vert2);
						break;
					case 18 :
						dessinePoint(g, j, i, vert3);
						break;
					case 19 :
						dessinePoint(g, j, i, vert4);
						break;
					default:
						dessinePoint(g, j, i, fore);
					}
					if (reg.grid) {
						dessineCadre(g, j, i, Color.black);
					}
				}
			}

		}
		Toolkit.getDefaultToolkit().sync();
	}
}
class CaseEtat {
        int entree ;
        int sortie;
        int donnee;
        int couleur;

	CaseEtat(int etat) {
		entree = etat >> 16;
		sortie = etat >> 24;
		donnee = Math.min(1, etat);
        	couleur = ((short) etat) % 16;
	}
}
