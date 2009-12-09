package fenetre;

import tom.Sequent;
import java3D.Repere;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;

/*
 * Exemple : (A\/(A\/False))/\(True\/False)
 * Exemple 2 : True\/(A/\True),(A\/(A\/False))/\(True\/False)
 * Exemple 3 : (A\/(True/\(False\/A)))/\((A\/(True/\False))/\(A\/(True\/False)))
 * Exemple 4 : True\/(A/\True),(A\/(A\/False))/\(True\/False),(A/\(True/\(False\/A)))/\((A\/(True/\False))/\(A\/(True\/False)))
 * Exemple 5 : (A\/True)\/(A/\False)
 * Exemple 6 : (A\/True)\/(((A\/((A\/False)\/True))\/False)/\A)
 */

public class Interface extends JFrame {

	private static final long serialVersionUID = 1L;

	private static Interface inter;

	private JPanel jContentPane = null;

	final static boolean RIGHT_TO_LEFT = false;

	private JTextField sequent = null;

	private JTextField subSequent = null;

	private JTextField consigne = null;

	private JButton and = null;

	private JButton or = null;

	private JButton True = null;

	private JButton False = null;

	private JButton seeSequent = null;

	private JButton previousSequent = null;

	private JButton nextSequent = null;

	private JButton leftSon = null;

	private JButton rightSon = null;

	private JButton back = null;

	private JButton send = null;
	
	private JButton genererDerivation = null;

	private JButton close = null;

	private JButton quit = null;

	private static LinkedList<Repere> listeRepere = new LinkedList<Repere>(); // @jve:decl-index=0:

	private static String placeSubSequent = ""; // @jve:decl-index=0:

	private static String[] listeSequent;

	private static int positionSequent = -1;

	private static LinkedList<String> subSequentPrecedent = new LinkedList<String>(); // @jve:decl-index=0:

	private static boolean derivation = false;
	
	/**
	 * This is the default constructor
	 */
	public Interface() {
		super();
		inter = this;
		initialize();
	}

	public static String getPlaceSubSequent() {
		return placeSubSequent;
	}

	public static int getPositionSequent() {
		return positionSequent;
	}

	public static String[] getListeSequent() {
		return listeSequent;
	}

	public static LinkedList<Repere> getListeRepere() {
		return listeRepere;
	}
	
	public static boolean getDerivation() {
		return derivation;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(750, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("Prototype");
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
		 * Permet de placer les elements dans la fenetre d'affichage; autorise le
		 * redimensionnement de la fenetre tout en gardant une place coherente
		 * des elements constitutifs
		 */
		if (jContentPane == null) {
			jContentPane = new JPanel();
			GridBagLayout grid = new GridBagLayout();
			GridBagConstraints contraintes = new GridBagConstraints();
			jContentPane.setLayout(grid);
			contraintes = new GridBagConstraints();
			contraintes.insets = new Insets(7, 7, 7, 7);

			contraintes.anchor = GridBagConstraints.NORTH;
			contraintes.fill = GridBagConstraints.HORIZONTAL;
			contraintes.weighty = 1;
			contraintes.weightx = 1;
			contraintes.gridx = 1;
			contraintes.gridy = 1;
			contraintes.gridheight = 1;
			contraintes.gridwidth = 1;
			JComponent comp = getConsigne();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.weightx = 20;
			contraintes.gridx = 2;
			contraintes.gridwidth = 10;
			comp = getSequent();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.insets = new Insets(3, 3, 3, 3);
			contraintes.fill = GridBagConstraints.NONE;
			contraintes.weightx = 1;
			contraintes.gridx = 1;
			contraintes.gridy = 2;
			contraintes.gridwidth = 1;
			comp = getAnd();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 2;
			comp = getOr();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 3;
			comp = getTrue();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 4;
			comp = getFalse();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 1;
			contraintes.gridy = 3;
			comp = getSeeSequent();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.fill = GridBagConstraints.HORIZONTAL;
			contraintes.gridx = 2;
			contraintes.gridwidth = 10;
			comp = getSubSequent();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.fill = GridBagConstraints.NONE;
			contraintes.gridx = 1;
			contraintes.gridy = 4;
			contraintes.gridwidth = 1;
			comp = getPreviousSequent();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 2;
			comp = getNextSequent();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 3;
			comp = getLeftSon();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 4;
			comp = getRightSon();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 5;
			comp = getBack();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.fill = GridBagConstraints.NONE;
			contraintes.anchor = GridBagConstraints.CENTER;
			contraintes.gridx = 1;
			contraintes.gridy = 5;
			contraintes.gridwidth = 2;
			comp = getSend();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);
			
			contraintes.gridx = 3;
			comp = getGenererDerivation();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 5;
			comp = getClose();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 6;
			comp = getQuit();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);
		}
		return jContentPane;
	}

	/**
	 * This method initializes consigne
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getConsigne() {
		if (consigne == null) {
			consigne = new JTextField();
			consigne.setText("Enter your sequent :");
			consigne.setEditable(false);
			consigne.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				}
			});
		}
		return consigne;
	}

	/**
	 * This method initializes sequent
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getSequent() {
		if (sequent == null) {
			sequent = new JTextField();
			sequent.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				}
			});
		}
		return sequent;
	}

	/**
	 * This method initializes and
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getAnd() {
		if (and == null) {
			and = new JButton();
			and.setText("/\\");
			and.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					sequent.setText(sequent.getText() + "/\\");
				}
			});
		}
		return and;
	}

	/**
	 * This method initializes or
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getOr() {
		if (or == null) {
			or = new JButton();
			or.setText("\\/");
			or.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					sequent.setText(sequent.getText() + "\\/");
				}
			});
		}
		return or;
	}

	/**
	 * This method initializes True
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getTrue() {
		if (True == null) {
			True = new JButton();
			True.setText("True");
			True.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					sequent.setText(sequent.getText() + "True");
				}
			});
		}
		return True;
	}

	/**
	 * This method initializes False
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getFalse() {
		if (False == null) {
			False = new JButton();
			False.setText("False");
			False.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					sequent.setText(sequent.getText() + "False");
				}
			});
		}
		return False;
	}

	/**
	 * This method initializes seeSequent
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getSeeSequent() {
		if (seeSequent == null) {
			seeSequent = new JButton();
			seeSequent.setText("See Sequent");
			seeSequent.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					sequent.setText(sequent.getText().replace(" ", ""));
					listeSequent = sequent.getText().split(",");
					subSequent.setText(listeSequent[0]);
					subSequentPrecedent.add(listeSequent[0]);
					positionSequent = 0;
					placeSubSequent = "";
				}
			});
		}
		return seeSequent;
	}

	/**
	 * This method initializes previousSequent
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getPreviousSequent() {
		if (previousSequent == null) {
			previousSequent = new JButton();
			previousSequent.setText("Previous Sequent");
			previousSequent
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							if (positionSequent > 0) {
								positionSequent--;
								subSequent
										.setText(listeSequent[positionSequent]);
								subSequentPrecedent
										.add(listeSequent[positionSequent]);
								placeSubSequent = "";
							}
						}
					});
		}
		return previousSequent;
	}

	/**
	 * This method initializes nextSequent
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getNextSequent() {
		if (nextSequent == null) {
			nextSequent = new JButton();
			nextSequent.setText("Next Sequent");
			nextSequent.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (positionSequent >= 0) {
						if (positionSequent < listeSequent.length - 1) {
							positionSequent++;
							subSequent.setText(listeSequent[positionSequent]);
							subSequentPrecedent
									.add(listeSequent[positionSequent]);
							placeSubSequent = "";
						}
					}
				}
			});
		}
		return nextSequent;
	}

	/**
	 * This method initializes leftSon
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getLeftSon() {
		if (leftSon == null) {
			leftSon = new JButton();
			leftSon.setText("Left Son");
			leftSon.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					subSequentPrecedent.add(subSequent.getText());
					subSequent.setText(redirigerSelection(subSequent.getText(),
							"left"));
					placeSubSequent = placeSubSequent + "0";
				}
			});
		}
		return leftSon;
	}

	/**
	 * This method initializes rightSon
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getRightSon() {
		if (rightSon == null) {
			rightSon = new JButton();
			rightSon.setText("Right Son");
			rightSon.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					subSequentPrecedent.add(subSequent.getText());
					subSequent.setText(redirigerSelection(subSequent.getText(),
							"right"));
					placeSubSequent = placeSubSequent + "1";
					//if (!listeRepere.isEmpty()) {
						//listeRepere.getFirst().actualiser();
						// listeRepere.getFirst().dessiner(listeRepere.getFirst());
					//}
				}
			});
		}
		return rightSon;
	}

	/**
	 * This method initializes back
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getBack() {
		if (back == null) {
			back = new JButton();
			back.setText("Back");
			back.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (!subSequentPrecedent.isEmpty()) {
						subSequent.setText(subSequentPrecedent.pollLast());
						if (placeSubSequent.length() > 0) {
							placeSubSequent = placeSubSequent.substring(0,
									placeSubSequent.length() - 1);
						}
					}
				}
			});
		}
		return back;
	}

	/**
	 * This method initializes subSequent
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getSubSequent() {
		if (subSequent == null) {
			subSequent = new JTextField();
			subSequent.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				}
			});
		}
		return subSequent;
	}

	/**
	 * This method initializes send
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getSend() {
		if (send == null) {
			send = new JButton();
			send.setText("Generate Graph");
			send.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					derivation = false;
					sequent.setText(sequent.getText().replace(" ", ""));
					listeSequent = sequent.getText().split(",");
					listeRepere.add(new Repere());
					Sequent.main(listeSequent, listeRepere.getLast());
					listeRepere.getLast().setCamera();
				}
			});
		}
		return send;
	}
	
	/**
	 * This method initializes genererDerivation
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getGenererDerivation() {
		if (genererDerivation == null) {
			genererDerivation = new JButton();
			genererDerivation.setText("Generate Graph Derivation");
			genererDerivation.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					derivation  = true;
					sequent.setText(sequent.getText().replace(" ", ""));
					listeSequent = sequent.getText().split(",");
					listeRepere.add(new Repere());
					Sequent.main(listeSequent, listeRepere.getLast());
					listeRepere.getLast().setCamera();
				}
			});
		}
		return genererDerivation;
	}

	/**
	 * This method initializes close
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getClose() {
		if (close == null) {
			close = new JButton();
			close.setText("Close 3D windows");
			close.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (!listeRepere.isEmpty()) {
						listeRepere.pollLast().fermerFenetre();
					}
				}
			});
		}
		return close;
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
					inter.setVisible(false);
				}
			});
		}
		return quit;
	}

	public String redirigerSelection(String s1, String s2) {
		/*
		 * Meme principe que pour la decomposition de la formule
		 * (tom/Sequent.java decomposerFormule) mais seul l'affichage de la
		 * partie droite ou gauche est ici utile
		 */
		int compteur = 0;
		for (int i = 0; i < s1.length(); i++) {
			switch (s1.charAt(i)) {
			case '(':
				compteur++;
				break;
			case ')':
				compteur--;
				break;
			case '\\':
				if (compteur == 0) {
					String mot1 = "";
					String mot2 = "";
					if ((s1.startsWith("("))
							&& s1.substring(i - 2, i - 1).equals(")")) {
						mot1 = s1.substring(1, i - 1);
					} else {
						mot1 = s1.substring(0, i);
					}
					if ((s1.substring(i + 2).startsWith("("))
							&& s1.endsWith(")")) {
						mot2 = s1.substring(i + 3, s1.length() - 1);
					} else {
						mot2 = s1.substring(i + 2, s1.length());
					}
					if (s2.equals("left")) {
						return mot1;
					} else {
						return mot2;
					}
				}
				break;
			case '/':
				if (compteur == 0) {
					String mot1 = "";
					String mot2 = "";
					if ((s1.startsWith("("))
							&& s1.substring(i - 2, i - 1).equals(")")) {
						mot1 = s1.substring(1, i - 1);
					} else {
						mot1 = s1.substring(0, i - 1);
					}
					if ((s1.substring(i + 2).startsWith("("))
							&& s1.endsWith(")")) {
						mot2 = s1.substring(i + 3, s1.length() - 1);
					} else {
						mot2 = s1.substring(i + 2, s1.length());
					}
					if (s2.equals("left")) {
						return mot1;
					} else {
						return mot2;
					}
				}
				break;
			case 'T':
				if (s1.length() == 4) {
					return s1;
				}
				break;
			case 'F':
				if (s1.length() == 5) {
					return s1;
				}
				break;
			case 'A':
				if (s1.length() == 1) {
					return s1;
				}
				break;
			default:
				break;
			}
		}
		return "ERROR";
	}
}
