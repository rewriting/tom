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
 * TDCA.java
 * par Blaise Potard et Emmanuel Hainry
 *
 * Main class
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
  boolean adapt = true;

  public static void main(String[] args) {
    //automate = new Parabole(); // L'automate et sa fonction de transition
    automate = new LangtonSelf(); // automate de Langton
    zb = new ZoneBoutons();
    reglages = zb.r;
    auto = new TDCA();

    if (args.length >= 1 && args[0].equals("bench")) {
      long startChrono  = System.currentTimeMillis();
      auto.Generation(1000);
      long stopChrono  = System.currentTimeMillis();
      System.out.println("gen(1000) in " + (stopChrono-startChrono) + " ms");
    } else {
      affiche = new Affichage();
      Fenetre f = new Fenetre(affiche);
      f.setSize(600, 400);
      f.setVisible(true);
    }
  }

  void init() {
    config = automate.init();
    interm = new Matrice(config.nblignes, config.nbcols);
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
    if (!adapt) {
      automate.nextGenerationConfig(config, interm);
    } else {
      automate.nextGenerationConfigAdapt(config, interm);
    }
    //tmp=interm; interm=config; config=tmp;
    //config = interm;
    config = new Matrice(interm.matrice);
    return interm;
  }
}
