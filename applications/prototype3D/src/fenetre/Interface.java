package fenetre;

/*
 * Interface class.
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

import tom.Sequent;
import tom.Couple;
import java3D.Point;
import java3D.Repere;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import projet.Noeud;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.JList;

public class Interface extends JFrame {

	private final static long serialVersionUID = 1L;

	private final static boolean RIGHT_TO_LEFT = false;

	private static Interface inter;

	private JPanel jContentPane = null;

	private JTextField sequent = null;

	private JTextField subSequent = null;

	private JTextField consigne = null;

	private JTextField couple = null;

	private JButton selectCouple = null;

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

	private JButton nextNode = null;

	private JButton previousNode = null;

	private JButton validateNode = null;

	private JButton validateCouple = null;

	private JButton clearCouple = null;

	private JButton genererDerivation = null;

	private JButton findProof = null;

	private JButton checkProof = null;

	private JButton close = null;

	private JButton quit = null;

	private static LinkedList<Repere> listeRepere = new LinkedList<Repere>(); // @jve:decl-index=0:

	private static LinkedList<String> listeSubSequent = new LinkedList<String>(); // @jve:decl-index=0:

	private static TreeSet<Noeud> listeNoeud = new TreeSet<Noeud>(); // @jve:decl-index=0:

	private static LinkedList<Integer> listeNumeroSequent = new LinkedList<Integer>(); // @jve:decl-index=0:

	private static LinkedList<Integer> listeNumeroPoint = new LinkedList<Integer>(); // @jve:decl-index=0:

	private static LinkedList<Couple> listeCouple = new LinkedList<Couple>(); // @jve:decl-index=0:

	private static LinkedList<Popup> listePopup = new LinkedList<Popup>();

	private static Noeud[] coupleTemporaire = new Noeud[2];

	private static String placeSubSequent = ""; // @jve:decl-index=0:

	private static String[] listeSequent;

	private static int numeroNode = 0;

	private static int nombreNoeudCouple = 0;

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

	public static LinkedList<Couple> getListeCouple() {
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

	public static void setListeNoeud(TreeSet<Noeud> l) {
		listeNoeud = l;
	}

	public static TreeSet<Noeud> getListeNoeud() {
		return listeNoeud;
	}

	public static int getNumeroNode() {
		return numeroNode;
	}

	public static LinkedList<Integer> getListeNumeroPoints() {
		return listeNumeroPoint;
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
			contraintes.gridx = 5;
			contraintes.gridy = 3;
			comp = getSelectCouple();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 6;
			contraintes.gridy = 3;
			contraintes.gridwidth = 4;
			comp = getCouple();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridwidth = 1;
			contraintes.gridx = 5;
			contraintes.gridy = 4;
			comp = getValidateNode();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 6;
			comp = getValidateCouple();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 7;
			comp = getPreviousNode();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 8;
			comp = getNextNode();
			grid.setConstraints(comp, contraintes);
			jContentPane.add(comp);

			contraintes.gridx = 9;
			comp = getClearCouple();
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
		String temp2 = "";
		if (s.length() > 0) {
			resultat = s.replace(" ", "");
			try {
				boolean termine = false;
				String temp = resultat;
				/*
				 * Liste des caracteres qui seront automatiquement supprimes de
				 * l'entree utilisateur lors de la generation du graphe
				 */
				Pattern p = Pattern.compile("!|:|;|§|ù|µ|£|<|>|%");
				Matcher m;
				while (!termine) {
					m = p.matcher(resultat);
					if (m.find()) {
						temp = resultat;
						temp2 = resultat;
						resultat = temp.replace(temp.substring(m.start(), m
								.end()), "");
					} else {
						termine = true;
					}
					if (resultat.length() <= 0 || temp2.equals(resultat)) {
						termine = true;
					}
				}
			} catch (PatternSyntaxException pse) {
				System.err.println("Error : " + pse);
			}
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
					 * Pour rentrer les sequents, il faut respecter certaines
					 * regles typographiques. Se referer au Guide d'Utilisation
					 * pour plus de precision
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
	 * This method initializes selectCouple
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getSelectCouple() {
		if (selectCouple == null) {
			selectCouple = new JButton();
			selectCouple.setText("Enter Couples");
			selectCouple.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					/*
					 * On rend visible/invisible les boutons correspondant
					 */
					coupleVisible = !coupleVisible;
					couple.setVisible(coupleVisible);
					previousNode.setVisible(coupleVisible);
					nextNode.setVisible(coupleVisible);
					validateNode.setVisible(coupleVisible);
					validateCouple.setVisible(coupleVisible);
					clearCouple.setVisible(coupleVisible);
					/*
					 * On reinitialise le choix des couples
					 */
					nombreNoeudCouple = 0;
					numeroNode = 0;
					coupleTemporaire[0] = null;
					coupleTemporaire[1] = null;
					listeCouple.clear();
					listeNumeroPoint.clear();
					if (coupleVisible && !listeRepere.isEmpty()) {
						couple.setText(listeRepere.getLast().getPoint(
								listeRepere.getLast().getListePoints(), 1)
								.getN().getFormule()
								+ "");
						if (numeroNode < listeRepere.getLast().getListePoints()
								.size()) {
							numeroNode++;
						}
						Point p = listeRepere.getLast().getPoint(
								listeRepere.getLast().getListePoints(),
								numeroNode);
						if (numeroNode > 1) {
							numeroNode--;
						}
						p = listeRepere.getLast().getPoint(
								listeRepere.getLast().getListePoints(),
								numeroNode);
						couple.setText(p.getN().getFormule() + "");
						listeRepere.getLast().actualiserPoints();
					}
					if (!listeRepere.isEmpty()) {
						listeRepere.getLast().effacerCouple();
						listeRepere.getLast().actualiserPoints();
					}
				}
			});
		}
		return selectCouple;
	}

	/**
	 * This method initializes previousNode
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getPreviousNode() {
		if (previousNode == null) {
			previousNode = new JButton();
			previousNode.setText("Previous Formula");
			previousNode.setVisible(false);
			previousNode.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (!listeRepere.isEmpty()) {
						if (numeroNode > 1) {
							numeroNode--;
						}
						Point p = listeRepere.getLast().getPoint(
								listeRepere.getLast().getListePoints(),
								numeroNode);
						couple.setText(p.getN().getFormule() + "");
						listeRepere.getLast().actualiserPoints();
					}
				}
			});
		}
		return previousNode;
	}

	/**
	 * This method initializes nextNode
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getNextNode() {
		if (nextNode == null) {
			nextNode = new JButton();
			nextNode.setText("Next Formula");
			nextNode.setVisible(false);
			nextNode.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (!listeRepere.isEmpty()) {
						if (numeroNode < listeRepere.getLast().getListePoints()
								.size()) {
							numeroNode++;
						}
						Point p = listeRepere.getLast().getPoint(
								listeRepere.getLast().getListePoints(),
								numeroNode);
						couple.setText(p.getN().getFormule() + "");
						listeRepere.getLast().actualiserPoints();
					}
				}
			});
		}
		return nextNode;
	}

	/**
	 * This method initializes validateNode
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getValidateNode() {
		if (validateNode == null) {
			validateNode = new JButton();
			validateNode.setText("Validate Formula");
			validateNode.setVisible(false);
			validateNode.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (!listeRepere.isEmpty()) {
						listeNumeroPoint.add(numeroNode);
						if (nombreNoeudCouple == 2) {
							nombreNoeudCouple = 1;
						}
						coupleTemporaire[nombreNoeudCouple] = listeRepere
								.getLast().getPoint(
										listeRepere.getLast().getListePoints(),
										numeroNode).getN();
						if (nombreNoeudCouple < 2) {
							nombreNoeudCouple++;
						}
						/*
						 * Si le noeud en question contient 'True', cela forme
						 * un couple a lui tout seul
						 */
						if (listeRepere.getLast().getPoint(
								listeRepere.getLast().getListePoints(),
								numeroNode).getN().getFormule().isTrue()) {
							Couple temp = new Couple(listeRepere.getLast()
									.getPoint(
											listeRepere.getLast()
													.getListePoints(),
											numeroNode).getN(), listeRepere
									.getLast());
							listeCouple.add(temp);
							nombreNoeudCouple = 0;
							for (Couple c : listeCouple) {
								c.dessinerCouple();
							}
							listeRepere.getLast().rajouterCouple();
						}
					}
				}
			});
		}
		return validateNode;
	}

	/**
	 * This method initializes validateCouple
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getValidateCouple() {
		if (validateCouple == null) {
			validateCouple = new JButton();
			validateCouple.setText("Validate Couple");
			validateCouple.setVisible(false);
			validateCouple
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							if (!listeRepere.isEmpty()
									&& nombreNoeudCouple == 2) {
								Couple temp = new Couple(coupleTemporaire[0],
										coupleTemporaire[1], listeRepere
												.getLast());
								listeCouple.add(temp);
								nombreNoeudCouple = 0;
								for (Couple c : listeCouple) {
									c.dessinerCouple();
								}
								listeRepere.getLast().rajouterCouple();

							}
						}
					});
		}
		return validateCouple;
	}

	/**
	 * This method initializes clearCouple
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private JButton getClearCouple() {
		if (clearCouple == null) {
			clearCouple = new JButton();
			clearCouple.setText("Clear Formulas");
			clearCouple.setVisible(false);
			clearCouple.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (!listeRepere.isEmpty()) {
						while (nombreNoeudCouple > 0) {
							listeNumeroPoint.removeLast();
							nombreNoeudCouple--;
						}
						coupleTemporaire = new Noeud[2];
						listeRepere.getLast().actualiserPoints();
					}
				}
			});
		}
		return clearCouple;
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
			couple.setEditable(false);
			couple.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
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
								sequent.setText(verifierEntree(sequent
										.getText()));
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
							String s = "Couples : \n";
							int steps = 0;
							for (Couple c : Sequent.getListeCouple()) {
								steps++;
								if (c.getF1().isTrue()) {
									s = s + c.getF1() + ", \n";
								} else if (c.getF2().isTrue()) {
									s = s + c.getF2() + ", \n";
								} else {
									s = s + c.getF1() + " " + c.getF2()
											+ ", \n";
								}
								c.dessinerCouple();
							}
							listeRepere.getLast().rajouterCouple();
							Popup p = new Popup("There's a proof", s, steps);
							p.setVisible(true);
							listePopup.add(p);
						} else {
							Popup p = new Popup("There's no proof");
							p.setVisible(true);
							listePopup.add(p);
						}
						Sequent.supprimerCouple();
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
					if (!listeRepere.isEmpty() && !listeCouple.isEmpty()) {
						String s = "Couples : \n";
						int steps = 0;
						for (Couple c : listeCouple) {
							steps++;
							if (c.getF1().isTrue()) {
								s = s + c.getF1() + ", \n";
							} else if (c.getF2().isTrue()) {
								s = s + c.getF2() + ", \n";
							} else {
								s = s + c.getF1() + " " + c.getF2() + ", \n";
							}
						}
						if (Sequent.verifierPreuve(listeCouple)) {
							Popup p = new Popup("That's a proof", s, steps);
							p.setVisible(true);
							listePopup.add(p);
						} else {
							Popup p = new Popup("That's not a proof");
							p.setVisible(true);
							listePopup.add(p);
						}
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
					/*
					 * On ferme toutes les fenêtres
					 */
					inter.setVisible(false);
					for (Repere rep : listeRepere) {
						rep.fermerFenetre();
					}
					for (Popup pop : listePopup) {
						pop.fermerPopup();
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
		 * partie droite ou gauche de la selection est ici utile
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
