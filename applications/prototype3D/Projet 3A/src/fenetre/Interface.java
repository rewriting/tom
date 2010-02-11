package fenetre;

import tom.Sequent;
import tom.Couple;
import java3D.Repere;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;

/*
 * Exemple : (A\/(A\/False))/\(True\/False)
 * Exemple 2 : (B\/(C\/False))/\(True\/False),Neg(A)\/(A/\A)
 * Exemple 3 : (A\/(True/\(False\/A)))/\((A\/(True/\False))/\(A\/(True\/False)))
 * Exemple 4 : True\/(A/\True),(A\/(A\/False))/\(True\/False),(A/\(True/\(False\/A)))/\((A\/(True/\False))/\(A\/(True\/False)))
 * Exemple 5 : (A\/True)\/(A/\False)
 * Exemple 6 : (A\/True)\/(((A\/((A\/False)\/True))\/False)/\A)
 */

public class Interface extends JFrame {

	private static final long serialVersionUID = 1L;

	private final static boolean RIGHT_TO_LEFT = false;

	private static Interface inter;

	private JPanel jContentPane = null;

	private JTextField sequent = null;

	private JTextField subSequent = null;

	private JTextField consigne = null;

	private JTextField couple = null;

	private JButton consigne2 = null;

	private JButton and = null;

	private JButton or = null;

	private JButton True = null;

	private JButton False = null;

	private JButton and2 = null;

	private JButton or2 = null;

	private JButton True2 = null;

	private JButton False2 = null;

	private JButton seeSequent = null;

	private JButton previousSequent = null;

	private JButton nextSequent = null;

	private JButton leftSon = null;

	private JButton rightSon = null;

	private JButton back = null;

	private JButton send = null;

	private JButton genererDerivation = null;

	private JButton findProof = null;

	private JButton checkProof = null;

	private JButton close = null;

	private JButton quit = null;

	private static LinkedList<Repere> listeRepere = new LinkedList<Repere>(); // @jve:decl-index=0:

	private static LinkedList<String> listeSubSequent = new LinkedList<String>(); // @jve:decl-index=0:

	private static LinkedList<Integer> listeNumeroSequent = new LinkedList<Integer>(); // @jve:decl-index=0:

	private static String placeSubSequent = ""; // @jve:decl-index=0:

	private static String[] listeSequent;

	private static String[] listeCouple;

	private static boolean derivation = false;

	private static boolean parcourirSequentVisible = false;

	private static boolean coupleVisible = false;

	private static boolean verifierPreuve = false;

	/**
	 * This is the default constructor
	 */
	public Interface() {
		super();
		inter = this;
		listeNumeroSequent.add(-1);
		initialize();
	}

	public static String getPlaceSubSequent() {
		return placeSubSequent;
	}

	public static int getNumeroSequent() {
		return listeNumeroSequent.getLast();
	}

	public static String[] getListeSequent() {
		return listeSequent;
	}

	public static String[] getListeCouple() {
		return listeCouple;
	}

	public static LinkedList<Repere> getListeRepere() {
		return listeRepere;
	}

	public static boolean getDerivation() {
		return derivation;
	}

	public static boolean getVerifierPreuve() {
		return verifierPreuve;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(1200, 200);
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
			/*
			 * Rentrer un sequent
			 */
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

			contraintes.gridx = 2;
			contraintes.weightx = 10;
			contraintes.gridwidth = 3;
			comp = getSequent();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 1;
			contraintes.gridy = 2;
			contraintes.gridwidth = 1;
			contraintes.weightx = 1;
			comp = getOr();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 2;
			comp = getAnd();
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

			/*
			 * Parcourir les sequents
			 */
			contraintes.gridx = 5;
			contraintes.gridy = 1;
			contraintes.gridwidth = 1;
			contraintes.weightx = 1;
			comp = getSeeSequent();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 6;
			contraintes.gridy = 1;
			contraintes.weightx = 10;
			contraintes.gridwidth = 4;
			comp = getSubSequent();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridwidth = 1;
			contraintes.weightx = 1;
			contraintes.gridx = 5;
			contraintes.gridy = 2;
			comp = getPreviousSequent();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 6;
			comp = getNextSequent();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 7;
			comp = getLeftSon();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 8;
			comp = getRightSon();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 9;
			comp = getBack();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			/*
			 * Generer les arbres et/ou les preuves
			 */
			contraintes.gridx = 1;
			contraintes.gridy = 3;
			comp = getSend();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 2;
			comp = getGenererDerivation();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 3;
			comp = getFindProof();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 4;
			comp = getCheckProof();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 1;
			contraintes.gridy = 4;
			comp = getClose();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 3;
			comp = getQuit();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			/*
			 * Creer liste couple pour verification/recherche de preuves
			 */
			contraintes.gridwidth = 1;
			contraintes.weightx = 1;
			contraintes.gridx = 5;
			contraintes.gridy = 3;
			comp = getConsigne2();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 6;
			contraintes.gridy = 3;
			contraintes.weightx = 10;
			contraintes.gridwidth = 4;
			comp = getCouple();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.weightx = 1;
			contraintes.gridwidth = 1;
			contraintes.gridx = 5;
			contraintes.gridy = 4;
			comp = getAnd2();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 6;
			comp = getOr2();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 7;
			comp = getTrue2();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 8;
			comp = getFalse2();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);
		}
		return jContentPane;
	}
	
	/*
	 * Verifie l'entree de l'utilisateur : entree non-vide, pas d'espace, etc.
	 */
	public String verifierEntree(String s) {
		String resultat = "";
		if (s.length() > 0) {
			resultat = s.replace(" ", "");
		}
		return resultat;
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
					/*
					 * Pour rentrer les sequents, il faut respecter les regles
					 * suivantes : separer les sequents par des virgules; ne pas
					 * mettre d'espaces; ne pas mettre de parenthèses inutiles
					 * (ex: ne pas entourer un sequent de parenthèses)
					 */
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
	 * This method initializes and2
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getAnd2() {
		if (and2 == null) {
			and2 = new JButton();
			and2.setText("/\\");
			and2.setVisible(false);
			and2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					couple.setText(couple.getText() + "/\\");
				}
			});
		}
		return and2;
	}

	/**
	 * This method initializes or2
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getOr2() {
		if (or2 == null) {
			or2 = new JButton();
			or2.setText("\\/");
			or2.setVisible(false);
			or2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					couple.setText(couple.getText() + "\\/");
				}
			});
		}
		return or2;
	}

	/**
	 * This method initializes True2
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getTrue2() {
		if (True2 == null) {
			True2 = new JButton();
			True2.setText("True");
			True2.setVisible(false);
			True2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					couple.setText(couple.getText() + "True");
				}
			});
		}
		return True2;
	}

	/**
	 * This method initializes False2
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getFalse2() {
		if (False2 == null) {
			False2 = new JButton();
			False2.setText("False");
			False2.setVisible(false);
			False2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					couple.setText(couple.getText() + "False");
				}
			});
		}
		return False2;
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
					/*
					 * On rend visible/invisible les boutons correspondant
					 */
					parcourirSequentVisible = !parcourirSequentVisible;
					previousSequent.setVisible(parcourirSequentVisible);
					nextSequent.setVisible(parcourirSequentVisible);
					leftSon.setVisible(parcourirSequentVisible);
					rightSon.setVisible(parcourirSequentVisible);
					back.setVisible(parcourirSequentVisible);
					subSequent.setVisible(parcourirSequentVisible);
					/*
					 * On met a jour le subsequent selectionne
					 */
					if (parcourirSequentVisible) {
						if (verifierEntree(sequent.getText()).length() > 0) {
							sequent.setText(verifierEntree(sequent.getText()));
						}
						listeSequent = sequent.getText().split(",");
						subSequent.setText(listeSequent[0]);
						listeSubSequent.add(listeSequent[0]);
						listeNumeroSequent.add(0);
					} else {
						subSequent.setText("");
						listeNumeroSequent.clear();
						listeNumeroSequent.add(-1);
						listeSubSequent.clear();
					}
					placeSubSequent = "";
					if (!listeRepere.isEmpty()) {
						listeRepere.getLast().actualiser();
					}
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
			previousSequent.setVisible(false);
			previousSequent
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							if (listeNumeroSequent.getLast() > 0) {
								listeNumeroSequent.add(listeNumeroSequent
										.getLast() - 1);
								subSequent
										.setText(listeSequent[listeNumeroSequent
												.getLast()]);
								listeSubSequent
										.add(listeSequent[listeNumeroSequent
												.getLast()]);
								placeSubSequent = "";
								if (!listeRepere.isEmpty()) {
									listeRepere.getLast().actualiser();
								}
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
			nextSequent.setVisible(false);
			nextSequent.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (listeNumeroSequent.getLast() >= 0) {
						if (listeNumeroSequent.getLast() < listeSequent.length - 1) {
							listeNumeroSequent
									.add(listeNumeroSequent.getLast() + 1);
							subSequent.setText(listeSequent[listeNumeroSequent
									.getLast()]);
							listeSubSequent.add(listeSequent[listeNumeroSequent
									.getLast()]);
							placeSubSequent = "";
							if (!listeRepere.isEmpty()) {
								listeRepere.getLast().actualiser();
							}
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
			leftSon.setVisible(false);
			leftSon.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					listeSubSequent.add(subSequent.getText());
					subSequent.setText(redirigerSelection(subSequent.getText(),
							"left"));
					listeNumeroSequent.add(listeNumeroSequent.getLast());
					placeSubSequent = placeSubSequent + "0";
					if (!listeRepere.isEmpty()) {
						listeRepere.getLast().actualiser();
					}
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
			rightSon.setVisible(false);
			rightSon.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					listeSubSequent.add(subSequent.getText());
					subSequent.setText(redirigerSelection(subSequent.getText(),
							"right"));
					listeNumeroSequent.add(listeNumeroSequent.getLast());
					placeSubSequent = placeSubSequent + "1";
					if (!listeRepere.isEmpty()) {
						listeRepere.getLast().actualiser();
					}
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
			back.setVisible(false);
			back.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (!listeSubSequent.isEmpty()) {
						subSequent.setText(listeSubSequent.removeLast());
						listeNumeroSequent.removeLast();
						if (placeSubSequent.length() > 0) {
							placeSubSequent = placeSubSequent.substring(0,
									placeSubSequent.length() - 1);
						}
						if (!listeRepere.isEmpty()) {
							listeRepere.getLast().actualiser();
						}
						/*
						 * Dans le cas ou on revient au debut, on recache tous
						 * les boutons
						 */
						if (listeSubSequent.isEmpty()) {
							parcourirSequentVisible = !parcourirSequentVisible;
							previousSequent.setVisible(parcourirSequentVisible);
							nextSequent.setVisible(parcourirSequentVisible);
							leftSon.setVisible(parcourirSequentVisible);
							rightSon.setVisible(parcourirSequentVisible);
							back.setVisible(parcourirSequentVisible);
							subSequent.setVisible(parcourirSequentVisible);
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
			subSequent.setEditable(false);
			subSequent.setVisible(false);
			subSequent.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				}
			});
		}
		return subSequent;
	}

	/**
	 * This method initializes consigne2
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getConsigne2() {
		if (consigne2 == null) {
			consigne2 = new JButton();
			consigne2.setText("Enter Couples");
			consigne2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					/*
					 * On rend visible/invisible les boutons correspondant
					 */
					coupleVisible = !coupleVisible;
					couple.setVisible(coupleVisible);
					and2.setVisible(coupleVisible);
					or2.setVisible(coupleVisible);
					True2.setVisible(coupleVisible);
					False2.setVisible(coupleVisible);
				}
			});
		}
		return consigne2;
	}

	/**
	 * This method initializes couple
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCouple() {
		if (couple == null) {
			couple = new JTextField();
			couple.setVisible(false);
			couple.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					/*
					 * Pour rentrer les couples, il faut respecter les regles
					 * suivantes : separer les couples par des virgules ;
					 * separer les deux formules d'un couple par un espace ;
					 */
				}
			});
		}
		return couple;
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
					if (verifierEntree(sequent.getText()).length() > 0) {
						sequent.setText(verifierEntree(sequent.getText()));
					}
					listeSequent = sequent.getText().split(",");
					listeRepere.add(new Repere());
					Sequent.main(listeSequent, listeRepere.getLast());
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
			genererDerivation
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							derivation = true;
							if (verifierEntree(sequent.getText()).length() > 0) {
								sequent.setText(verifierEntree(sequent.getText()));
							}
							listeSequent = sequent.getText().split(",");
							listeRepere.add(new Repere());
							Sequent.main(listeSequent, listeRepere.getLast());
						}
					});
		}
		return genererDerivation;
	}

	/**
	 * This method initializes findProof
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getFindProof() {
		if (findProof == null) {
			findProof = new JButton();
			findProof.setText("Find Proof");
			findProof.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (!listeRepere.isEmpty()) {
						if (Sequent.trouverPreuve()) {
							System.out.println("Couples : ");
							for (Couple c : Sequent.getListeCouple()) {
								if (c.getF1().isTrue()) {
									System.out.println(c.getF1() + ",");
								} else if (c.getF2().isTrue()) {
									System.out.println(c.getF2() + ",");
								} else {
									System.out.println(c.getF1() + " "
											+ c.getF2() + ",");
								}
								c.dessinerCouple();
							}
							listeRepere.getLast().rajouterCouple();
							System.out.println("Il y a une preuve.");
						} else {
							System.out.println("Il n'y a pas de preuves.");
						}
					}
				}
			});
		}
		return findProof;
	}

	/**
	 * This method initializes checkProof
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getCheckProof() {
		if (checkProof == null) {
			checkProof = new JButton();
			checkProof.setText("Check Proof");
			checkProof.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					/*
					 * On met a jour la liste des couples retenus
					 */
					if (verifierEntree(couple.getText()).length() > 0) {
						couple.setText(verifierEntree(couple.getText()));
					}
					listeCouple = couple.getText().split(",");
					if (!listeRepere.isEmpty()) {
						System.out.println("Est-ce une preuve ? "
								+ Sequent.verifierPreuve(listeCouple));
					}
				}
			});
		}
		return checkProof;
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
					for (Repere rep : listeRepere) {
						rep.setVisible(false);
					}
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
			case 'N':
				if (s1.length() == 6) {
					return s1;
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
			default:
				if (s1.length() == 1) {
					return s1;
				}
				break;
			}
		}
		return "ERROR";
	}
}
