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
 * 	TwoDimCellularAutomaton.java
 * 	par Blaise Potard et Emmanuel Hainry
 *
 * 	classe abstraite que doivent etendre les automates cellulaires 2D.
 * 	la fonction qui gere les transitions est ici.
 */

package cell;

public abstract class TwoDimCellularAutomaton {

	Matrice neighbourhood;
	
	public TwoDimCellularAutomaton() {
	 int[][] blankNeighbourhood = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
	 neighbourhood = new Matrice(3, 3, blankNeighbourhood);
	}

	public abstract int nextGeneration(Matrice Neighbourhood);
	public abstract Matrice init();

	public void nextGenerationConfig(Matrice config, Matrice interm) {
		// transition de la configuration totale vers la configuration totale suivante.
		// Remarque : le tableau contenant la configuration conserve sa taille.
		// -> bords consideres eternellement blancs
		// -> effets de bords
		//System.out.println("[TwoDimCellularAutomaton]\tnextGenerationConfig(..)");
	int height = config.nblignes;
	int width = config.nbcols;
	int a, b, j, k;
	for (a=0; a<height; a++) {
		for (b=0; b<width; b++) {
			
			for (j=0; j<3; j++) {
				for (k=0; k<3; k++) {
					if (a+j < 1 || a+j > height || b+k < 1 || b+k > width) {
						neighbourhood.matrice[j][k] = 0;
					} else {
						neighbourhood.matrice[j][k] = config.matrice[a+j-1][b+k-1];
					}
				}
			}
			
			interm.matrice[a][b] = nextGeneration(neighbourhood);
		}
	}
	}

	public void nextGenerationConfigAdapt(Matrice config, Matrice interm) {
		// transition de la configuration totale vers la configuration totale suivante.
		// Remarque : le tableau contenant la configuration grandit avec l'AC.
		//System.out.println("[TwoDimCellularAutomaton]\tnextGenerationConfigAdapt(..)");
		config.grandit();
		interm.grandit();
		int height = config.nblignes;
		int width = config.nbcols;
		int a, b, j, k;
		for (a=0; a<height; a++) {
			for (b=0; b<width; b++) {
				for (j=0; j<3; j++) {
					for (k=0; k<3; k++) {
						if (a+j < 1 || a+j > height || b+k < 1 || b+k > width) {
							neighbourhood.matrice[j][k] = 0;
						} else {
							neighbourhood.matrice[j][k] = config.matrice[a+j-1][b+k-1];
						}
					}
				}
				interm.matrice[a][b] = nextGeneration(neighbourhood);
			}
		}
		//	config.rapetisse();
		interm.rapetisse();
	}
}
