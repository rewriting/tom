/*
 *	Affichage.java
 *	par Blaise Potard et Emmanuel Hainry
 *
 *	gestion de l'affichage
 */

package Cell;

import java.awt.event.*;
import javax.swing.*;

class Affichage implements ActionListener
{
  Timer timer;
  TDCA td;
  Etat e;
  int iteration;
  int debut, fin;
  Reglages reg;
  JProgressBar pb;
  Affichage()
  {
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
  
  public void actionPerformed(ActionEvent ae )
  {
    timer.setDelay(reg.delai);
    if (reg.lecture)
      {
	reg.generationActuelle++;
	pb.setMinimum(reg.generationDebut);
	pb.setMaximum(reg.generationFin);
	e.dessine();
	e.ChangeEtat(td.Suivant()); // passage à l'état suivant
	if (reg.generationActuelle >= reg.generationFin)
	  reg.lecture = false;
	debut = reg.generationActuelle; // étonnant non ?
      }
    else
      {
	if (reg.generationDebut != debut && !reg.pause)
	{
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
	
