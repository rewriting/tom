/*	
 * 	ZoneBoutons.java
 *	par Blaise Potard et Emmanuel Hainry
 *
 *	structure de la zone de la fenetre contenant les boutons.
 */

package Cell;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

class ZoneBoutons extends JPanel
{
    Reglages r;
    JTextField genDeb, genFin;
    JProgressBar progression;

    ZoneBoutons()
    {
    	ImageIcon playIcon = new ImageIcon("Cell/play.gif");
    	ImageIcon stopIcon = new ImageIcon("Cell/stop.gif");
    	ImageIcon pauseIcon = new ImageIcon("Cell/pause.gif");
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

	String[] listeFigure = { "Données", "Couleurs"};
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
	genDebL = new JLabel("Début");
	p2.add(genDebL, gbc);

	gbc.gridwidth=GridBagConstraints.REMAINDER;
	genDeb = new JTextField((new Integer(r.generationDebut)).toString(), 3);
	p2.add(genDeb, gbc);

	gbc.gridwidth=1;
	genFinL = new JLabel("Fin");
	p2.add(genFinL, gbc);

	gbc.gridwidth=GridBagConstraints.REMAINDER;
	genFin = new JTextField((new Integer(r.generationFin)).toString(), 3);
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

   		  int debut = (new Integer(genDeb.getText())).intValue();
          int fin = (new Integer(genFin.getText())).intValue();
	      if ( debut > fin || debut < 1)
	 	  {
		      JOptionPane erreur = new JOptionPane();
		      JOptionPane.showMessageDialog(null, "La génération de début ou de fin est erronée.", "Erreur", JOptionPane.ERROR_MESSAGE);
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

