/*
 * 	Reglages.java
 *	par Blaise Potard et Emmanuel Hainry
 *
 *	Contient les valeurs de reglages choisis.
 */

package Cell;

class Reglages
{
    int delai, style;
    int generationDebut;
    int generationFin;
    int generationActuelle;
    boolean grid, dd, td;
    boolean lecture, pause;
    int mode = 1;
    static final int COULEUR = 1;
    static final int DONNEES = 0;

	Reglages()
	{
		delai = 1995;
		generationDebut = 1;
		generationActuelle = generationDebut;
		generationFin = 10;
		grid = false;
		style = COULEUR;
		dd = true;
		td = false;
		lecture = false;
		pause = false;
	}
}
