package gui;

import gui.*;
import java.io.File;
import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class MainGUI{

	public static String programPath;
	
	public static void main (String args[]) {
	  
		programPath=args[0];
		XMLProgramHandlerGui.makeRuleStrategy(programPath);
	}
}