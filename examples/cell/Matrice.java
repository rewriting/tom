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
 *	Matrice.java
 *	par Blaise Potard et Emmanuel Hainry
 *
 *	une matrice est un tableau et ses 2 dimensions.
 */

package cell;

class Matrice
{
    int nblignes;
    int nbcols;
    int[][] matrice;

    Matrice(int nbl, int nbc)
    {
	    nblignes = nbl;
	    nbcols = nbc;
	    matrice = new int[nbl][nbc];
    }

    Matrice(int[][] matrix)
    {
	    matrice = matrix;
	    nblignes = matrix.length;
	    nbcols = matrix[0].length;
    }
    
    Matrice(int nbl, int nbc, int[][] matrix) //deprecated: hazardous 
    // ne plus utiliser cette fonction.
    {
	    nblignes = nbl;
	    nbcols = nbc;
	    matrice = matrix;
    }
	
	void grandit() {
		int[][] m2 = new int[nblignes+2][nbcols+2];
		for (int i=0; i<nblignes; i++) {
			for (int j=0; j<nbcols; j++) {
				m2[i+1][j+1] = matrice[i][j];
			}
		}
		matrice = m2;
		nblignes = nblignes + 2;
		nbcols = nbcols + 2;
	}

	void rapetisse() {
		int i;
		boolean b = true;
		// La premiere colonne est-elle vide ?
		for (i=0; i<nblignes; i++) {
			b = b && (matrice[i][0]==0);
		}
		if (b) {
			nbcols -- ;
			int [][] m2 = new int[nblignes][nbcols];
			for (int j=0; j<nblignes; j++) {
				for (int k=0; k<nbcols; k++) {
					m2[j][k] = matrice[j][k+1];
				}
			}
			matrice = m2;
			//System.out.println("[Matrice]\trapetisse premiere colonne");
		}

		//la derniere colonne est-elle vide ?
		b = true;
		for (i=0; i<nblignes; i++) {
			b = b && (matrice[i][nbcols-1]==0);
		}
		if (b) {
			nbcols -- ;
			int [][] m2 = new int[nblignes][nbcols];
			for (int j=0; j<nblignes; j++) {
				for (int k=0; k<nbcols; k++) {
					m2[j][k] = matrice[j][k];
				}
			}
			matrice = m2;
			//System.out.println("[Matrice]\trapetisse derniere colonne");
		}

		//la premiere ligne est-elle vide ?
		b = true;
		for (i=0; i<nbcols; i++) {
			b = b && (matrice[0][i] == 0);
		} if (b) {
			nblignes -- ;
			int [][] m2 = new int[nblignes][nbcols];
			for (i=0; i<nblignes; i++) {
				m2[i] = matrice[i+1];
			}
			matrice = m2;
			//System.out.println("[Matrice]\trapetisse premiere ligne");
		}

		//la derniere ligne est-elle vide ?
		b = true;
		for (i=0; i<nbcols; i++) {
			b = b && (matrice[nblignes-1][i] == 0);
		} if (b) {
			nblignes -- ;
			int [][] m2 = new int[nblignes][nbcols];
			for (i=0; i<nblignes; i++) {
				m2[i] = matrice[i];
			}
			matrice = m2;
			//System.out.println("[Matrice]\trapetisse derniere ligne");
		}
	}
}
