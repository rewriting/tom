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
}
	
