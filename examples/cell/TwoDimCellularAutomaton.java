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

public void nextGenerationConfig(Matrice config, Matrice interm) {
// transition de la configuration totale vers la configuration totale suivante.
// Remarque : le tableau contenant la configuration conserve sa taille.
// -> bords consideres eternellement blancs
// -> effets de bords
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
}
