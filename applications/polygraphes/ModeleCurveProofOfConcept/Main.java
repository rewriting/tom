package modele;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;

import javax.swing.JFrame;



public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("Polygraphes GUI Alpha 2");
		frame.setSize(500,500);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		Container contentPane = frame.getContentPane();
		Graph mG = new Graph();
		
		//Creation d'une deux Cellules
		DeuxCellules c1 = new DeuxCellules("Cellule1",2,4);
		mG.ajouterElement(c1); //Ajout de la cellule
		
		DeuxCellules c2 = new DeuxCellules("Cellule2",8,2);
		mG.ajouterElement(c2); //Ajout de la cellule
		
		//Note IMPORTANTE : on ajoute les compo JUSTE pour le debuggade
		Composition0 co00 = new Composition0(c1,c2);
		
//		DeuxCellules c3 = new DeuxCellules("Cellule3",8,4);
//		mG.ajouterElement(c3);
//		
//		Composition0 co01 = new Composition0(co00,c3);
//		mG.ajouterElement(co01);
		
		DeuxCellules c4 = new DeuxCellules("Cellule4",10,5);
		mG.ajouterElement(c4);
		
		
		Composition1 co10 = new Composition1(co00,c4);
		mG.ajouterElement(co10);
		
		
//        DeuxCellules c5 = new DeuxCellules("Cellule5",5,5);
//		mG.ajouterElement(c5);
//		
//		Composition0 co02 = new Composition0(co10,c5);
////		
//		DeuxCellules c6 = new DeuxCellules("Cellule6",7,2);
//		mG.ajouterElement(c6);
//
//		DeuxCellules c7 = new DeuxCellules("Cellule7",3,2);
//		mG.ajouterElement(c7);
//		
//		Composition1 co11 = new Composition1(co02,new Composition0(c6,c7));
//
//		DeuxCellules c8 = new DeuxCellules("Cellule8",4,10);
//		mG.ajouterElement(c8);
//		
//		Composition1 co12 = new Composition1(co11,c8);
//		
//		DeuxCellules c9 = new DeuxCellules("Cellule9",3,2);
//		mG.ajouterElement(c9);
//
//		DeuxCellules c10 = new DeuxCellules("Cellule10",4,2);
//		mG.ajouterElement(c10);
//		
//		DeuxCellules c11 = new DeuxCellules("Cellule11",3,2);
//		mG.ajouterElement(c11);
//		
//		Composition0 co03 = new Composition0(c9,c10);
//		Composition0 co04 = new Composition0(co03,c11);
//		
//		Composition1 co13 =  new Composition1(c8,co04);
//		
		contentPane.add(mG);
		frame.setVisible(true);
	}
}
