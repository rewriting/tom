/*
 * 	TDCA.java
 *	par Blaise Potard et Emmanuel Hainry
 *
 * 	Main class
 */

package cell;

public class TDCA {
        static ZoneBoutons zb;
        static Reglages reglages;
        static TwoDimCellularAutomaton automate;
        Matrice config;
        Matrice interm, tmp;
        static TDCA auto;
        static Affichage affiche;

        public static void main(String[] args) {
		//automate = new Parabole(); // L'automate et sa fonction de transition
		automate = new LangtonSelf(); // automate de Langton
                zb = new ZoneBoutons();
                reglages = zb.r;
                auto = new TDCA();
                affiche = new Affichage();
                Fenetre f = new Fenetre(affiche);
                f.setSize(600, 400);
                f.setVisible(true);
        }
        
        void init() {
		/*int[][] temp = {	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 
					{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 2, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 2, 4, 6, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 2, 8, 8, 8, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
					{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};*/
		int[][] temp = { {0, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0}, 
				 {2, 1, 7, 0, 1, 4, 0, 1, 4, 2, 0, 0, 0, 0, 0},
				 {2, 0, 2, 2, 2, 2, 2, 2, 0, 2, 0, 0, 0, 0, 0},
				 {2, 7, 2, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0},
				 {2, 1, 2, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0},
				 {2, 0, 2, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0},
				 {2, 7, 2, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0},
				 {2, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 0},
				 {2, 0, 7, 1, 0, 7, 1, 0, 7, 1, 1, 1, 1, 1, 2},
				 {0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0},
				 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0} };
		int[][] bigone = new int[200][200];
		int i, j;
		for (i=0; i< 11; i++) {
			for (j=0; j<15; j++) {
				//System.out.println(temp[i][j]);
				bigone[110+i][90+j] = temp[i][j];
			}
		}
		config = new Matrice(bigone);
		interm = new Matrice(200,200);
        }

        public Matrice Generation(int depart) {
        		init();
        		int i;
        		for (i=1; i<depart; i++) {
        			//automate.nextGenerationConfig(config, interm);
        			//config = interm;
        			Suivant();
        		}
                return config;
        }

        public Matrice Suivant() {
                automate.nextGenerationConfig(config, interm);
                tmp=interm; interm=config; config=tmp;
								//config = interm;
             	return interm;
        }
}
