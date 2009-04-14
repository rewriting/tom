package gui;

import gui.*;
import java.io.File;

public class MainGUI{

	public static String programPath;
	
	public static void main (String args[]) {
		programPath=args[0];
		XMLProgramHandlerGui.makeRuleStrategy(programPath);
	}
}