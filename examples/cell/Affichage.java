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
 *	Affichage.java
 *	par Blaise Potard et Emmanuel Hainry
 *
 *	gestion de l'affichage
 */

package cell;

import java.awt.event.*;
import javax.swing.*;

class Affichage implements ActionListener {
  Timer timer;
  TDCA td;
  Etat e;
  int iteration;
  int debut, fin;
  Reglages reg;
  JProgressBar pb;

	Affichage() {
    reg = TDCA.reglages;
    pb = TDCA.zb.progression;
    td = TDCA.auto;
    //  fin = reg.generationFin;
    debut = reg.generationDebut;
    timer = new Timer(reg.delai, this);
    Matrice m;
    m = td.Generation(reg.generationDebut);
    //  iteration = debut;
    e = new Etat(m, reg);
    timer.start();
  }
  
	public void actionPerformed(ActionEvent ae ) {
    timer.setDelay(reg.delai);
		if (reg.lecture) {
        reg.generationActuelle++;
        pb.setMinimum(reg.generationDebut);
        pb.setMaximum(reg.generationFin);
        e.dessine();
        e.ChangeEtat(td.Suivant()); // passage a l'etat suivant
			if (reg.generationActuelle >= reg.generationFin)
          reg.lecture = false;
        debut = reg.generationActuelle; // eonnant non ?
		} else {
			if (reg.generationDebut != debut && !reg.pause) {
            e.ChangeEtat(td.Generation(reg.generationDebut));
            debut  = reg.generationDebut;
           }
      }
    pb.setValue(reg.generationActuelle);
    pb.setString(new Integer(reg.generationActuelle).toString());
    pb.setStringPainted(true);
  }
  
  public void redessine() {
      e.dessine();
    }
}
