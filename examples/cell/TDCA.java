/*
 * Copyright (c) 2004-2005, INRIA
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
