

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.PrintStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Classe qui affiche les données qui pourrait se retrouver dans le terminal
 * @author cynthia
 */
public class Console extends JPanel{
	

    /**
	 * 
	 */
	private static final long serialVersionUID = -3593483356378384430L;

	public Console() {
        jbInit();
    }

    /**
     * Methode qui fais l'affichage de la console
     */
    public void jbInit(){
    	
        Font f = new Font("Helvetica", Font.PLAIN, 12);
        JTextPane retour= new JTextPane();
        retour.setBackground(Color.WHITE);
        retour.setFont(f);
        retour.setEditable(false);

        JScrollPane ascenseur = new JScrollPane(retour);
        ascenseur.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        ascenseur.setPreferredSize(new Dimension(370, 250));
        ascenseur.setMinimumSize(new Dimension(10, 10));

        PrintStream pso =new PrintStream(new JTextAreaSystemOut(retour,false));
        PrintStream pse =new PrintStream(new JTextAreaSystemOut(retour,true));
        System.setOut(pso);
        System.setErr(pse);
        ascenseur.getViewport().add(retour);
        this.add(ascenseur,BorderLayout.CENTER);
    }


}
