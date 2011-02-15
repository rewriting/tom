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
 * 	ZoneBoutons.java
 *	par Blaise Potard et Emmanuel Hainry
 *
 *	structure de la zone de la fenetre contenant les boutons.
 */

package cell;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

class ZoneBoutons extends JPanel
{
    private static final long serialVersionUID = 1L;
  
    Reglages r;
    //JTextField genDeb, genFin;
    JSpinner genDeb, genFin;
    JProgressBar progression;

    ZoneBoutons()
    {
    	ImageIcon playIcon = new ImageIcon("cell/play.gif");
    	ImageIcon stopIcon = new ImageIcon("cell/stop.gif");
    	ImageIcon pauseIcon = new ImageIcon("cell/pause.gif");
	r = new Reglages();;
	JSlider choixDelai;
	JComboBox choixFigure;
	JCheckBox grid;
	JPanel p1, p2;
	JLabel vitEtiq, genDebL, genFinL;
	JButton lecture, stop, pause; //valid

	setLayout(new BorderLayout());

	p1 = new JPanel();
	p1.setLayout(new BorderLayout());

	vitEtiq = new JLabel("Vitesse");
	p1.add(vitEtiq, BorderLayout.NORTH);

	choixDelai = new JSlider(1, 100, 2);
	choixDelai.addChangeListener(new SliderListener());
	p1.add(choixDelai, BorderLayout.CENTER);

	String[] listeFigure = { "Donnees", "Couleurs"};
	choixFigure = new JComboBox(listeFigure);
	choixFigure.addActionListener(new StyleListener(choixFigure));
	p1.add(choixFigure, BorderLayout.SOUTH);
	add(p1, BorderLayout.NORTH);


	p2 = new JPanel();
	p2.setLayout(new GridBagLayout());

	GridBagConstraints gbc = new GridBagConstraints();
	gbc.gridwidth = 1;
	gbc.gridheight = 1;
	gbc.insets=new Insets(5,5,5,5);

	lecture = new JButton(playIcon);
	lecture.setToolTipText("Lecture");
	lecture.addActionListener(new PlayListener());
	p2.add(lecture, gbc);

	pause = new JButton(pauseIcon);
	pause.setToolTipText("Pause");
	pause.addActionListener(new PauseListener());
	p2.add(pause, gbc);

	gbc.gridwidth=GridBagConstraints.REMAINDER;
	stop = new JButton(stopIcon);
	stop.setToolTipText("Stop");
	stop.addActionListener(new StopListener());
	p2.add(stop, gbc);

	gbc.gridwidth=GridBagConstraints.REMAINDER;
	progression = new JProgressBar();
	progression.setMinimum(r.generationDebut);
	progression.setMaximum(r.generationFin);
	progression.setValue(r.generationActuelle);
	progression.setString(new Integer(r.generationActuelle).toString());
	progression.setStringPainted(true);
	p2.add(progression, gbc);

	gbc.gridwidth=1;
	genDebL = new JLabel("Debut");
	p2.add(genDebL, gbc);

	gbc.gridwidth=GridBagConstraints.REMAINDER;
	//genDeb = new JTextField((new Integer(r.generationDebut)).toString(), 3);
	SpinnerModel ModelDebut = new SpinnerNumberModel(1, 1, 99990, 1);
	// should the max be MAX_INT or is this value safe enough?
	genDeb = new JSpinner(ModelDebut);
	genDeb.setValue(new Integer(r.generationDebut));
	p2.add(genDeb, gbc);

	gbc.gridwidth=1;
	genFinL = new JLabel("Fin");
	p2.add(genFinL, gbc);

	gbc.gridwidth=GridBagConstraints.REMAINDER;
	//genFin = new JTextField((new Integer(r.generationFin)).toString(), 3);
	SpinnerModel ModelFin = new SpinnerNumberModel(1, 1, 99990, 1);
	genFin = new JSpinner(ModelFin);
	genFin.setValue(new Integer(r.generationFin));
	p2.add(genFin, gbc);

	add(p2, BorderLayout.CENTER);

	JPanel sud = new JPanel(new BorderLayout());
	grid = new JCheckBox("Quadrillage", r.grid);
	grid.setMnemonic(KeyEvent.VK_Q);
	grid.addItemListener(new CheckBoxListener());
        sud.add(grid, BorderLayout.NORTH);
	add(sud, BorderLayout.SOUTH);

    }

    class CheckBoxListener implements ItemListener
    {
        public void itemStateChanged(ItemEvent e)
       	{
            if (e.getStateChange() == ItemEvent.DESELECTED)
            	r.grid=false;
            else
            	r.grid=true;
	}
    }

    class PauseListener implements ActionListener
    {
	public void actionPerformed(ActionEvent e)
	{
	    r.pause = true;
	    r.lecture = false;
	}
    }

      class StopListener implements ActionListener
    {
	public void actionPerformed(ActionEvent e)
	{
    r.pause = false;
    r.lecture = false;
    r.generationActuelle = r.generationDebut;
	}
    }


    class PlayListener implements ActionListener
    {
	public void actionPerformed(ActionEvent e)
	{
		if (!r.lecture && !r.pause) // etat stop
		{

   		  //int debut = (new Integer(genDeb.getText())).intValue();
          //int fin = (new Integer(genFin.getText())).intValue();
          int debut = ((Integer) genDeb.getValue()).intValue();
          int fin = ((Integer) genFin.getValue()).intValue();
          //System.out.println("[ZoneBoutons]\tLecture de " + debut + " a " + fin);
	      if ( debut > fin || debut < 1)
	 	  {
		      JOptionPane erreur = new JOptionPane();
		      JOptionPane.showMessageDialog(null, "La generation de debut ou de fin est erronee.", "Erreur", JOptionPane.ERROR_MESSAGE);
		  }
	      else
	      {
			r.generationDebut = debut;
			r.generationFin = fin;
	    	r.generationActuelle = debut;
	    	r.lecture = true;
	      }
	    }

	    if (!r.lecture && r.pause) // etat  pause
	    {
	    	r.lecture = true;
	    	r.pause = false;
	    }
	}
    }

    class StyleListener implements ActionListener
    {
	JComboBox j;
	public StyleListener(JComboBox j)
	{
	    this.j = j;
	    j.setSelectedIndex(r.style);
	}
	public void actionPerformed(ActionEvent e)
	{
	    r.style = j.getSelectedIndex();
	    TDCA.affiche.redessine();
	}
    }

    class SliderListener implements ChangeListener
    {
	public void stateChanged(ChangeEvent e)
	{
	    JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting())
		{
		    r.delai = (2000 - ((int) source.getValue()*20));
		}
	}
    }
    
}
