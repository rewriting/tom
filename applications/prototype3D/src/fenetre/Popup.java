package fenetre;

/*
 * Popup class.
 * 
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
 * Nancy, France.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * To have a copy of the GNU General Public License, please see <http://www.gnu.org/licenses/>.
 *
 * Thomas Boudin e-mail: Thomas.Boudin@mines.inpl-nancy.fr
 *
 */

import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Popup extends JFrame{
	
	private final static long serialVersionUID = 1L;

	private final static boolean RIGHT_TO_LEFT = false;
	
	private JPanel jContentPane = null;
	
	private JTextField answer = null;
	
	private JTextArea liste = null;
	
	private JButton quit = null;
	
	private Popup pop = null;
	
	private String reponse = "";
	
	private String listeCouple = "";
	
	private int taille = 0;
	
	/**
	 * This is the default constructor
	 */
	public Popup() {
		super();
		pop = this;
		initialize("Popup");
	}
	
	public Popup(String rep) {
		super();
		pop = this;
		reponse = rep;
		initialize("Answer");
	}
	
	public Popup(String rep, String lc, int i) {
		super();
		pop = this;
		reponse = rep;
		listeCouple = lc;
		taille = i;
		initialize("Answer");
	}
	
	public void fermerPopup() {
		pop.setVisible(false);
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(String titre) {
		this.setSize(200, 150 + 10 * taille);
		this.setContentPane(getJContentPane());
		this.setTitle(titre);
	}
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (RIGHT_TO_LEFT) {
			jContentPane
					.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		/*
		 * Permet de placer les elements dans la fenetre d'affichage; autorise
		 * le redimensionnement de la fenetre tout en gardant une place
		 * coherente des elements constitutifs
		 */
		if (jContentPane == null) {
			jContentPane = new JPanel();
			GridBagLayout grid = new GridBagLayout();
			GridBagConstraints contraintes = new GridBagConstraints();
			jContentPane.setLayout(grid);
			contraintes = new GridBagConstraints();
			contraintes.insets = new Insets(3, 3, 3, 3);
			
			contraintes.anchor = GridBagConstraints.NORTH;
			contraintes.fill = GridBagConstraints.HORIZONTAL;
			contraintes.weighty = 1;
			contraintes.weightx = 1;
			contraintes.gridx = 1;
			contraintes.gridy = 1;
			contraintes.gridheight = 1;
			contraintes.gridwidth = 1;
			
			JComponent comp = getAnswer();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);
			
			contraintes.gridy = 2;
			comp = getListe();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);
			
			contraintes.gridy = 3;
			comp = getQuit();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);
			
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes answer
	 * 
	 * @return javax.swing.JTextField
	 */

	private JTextField getAnswer() {
		if (answer == null) {
			answer = new JTextField();
			answer.setText(reponse);
			answer.setEditable(false);
			answer.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				}
			});
		}
		return answer;
	}
	
	/**
	 * This method initializes liste
	 * 
	 * @return javax.swing.JTextField
	 */

	private JTextArea getListe() {
		if (liste == null) {
			liste = new JTextArea();
			liste.setText(listeCouple);
			liste.setEditable(false);
		}
		return liste;
	}
	
	/**
	 * This method initializes quit
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getQuit() {
		if (quit == null) {
			quit = new JButton();
			quit.setText("Quit");
			quit.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					pop.setVisible(false);
				}
			});
		}
		return quit;
	}

}
