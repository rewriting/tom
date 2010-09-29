package gui;

import java.io.*;
import java.text.*;
import java.util.*;

public class Log {
	private String nomFichier;

	public Log(String n) {
		nomFichier = n;
	}

	public void ecritLigne(String ligne) {
		try {
			FileWriter f = new FileWriter(nomFichier, true);
			BufferedWriter bf = new BufferedWriter(f);
			Calendar c = Calendar.getInstance();
			Date maintenant = c.getTime();
			String datelog = DateFormat.getDateTimeInstance(DateFormat.SHORT,
					DateFormat.MEDIUM, Locale.FRANCE).format(maintenant);
			bf.write("[" + datelog + "]: " + ligne);
			bf.newLine();
			bf.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void ecritLigne(String entete, String info) {
		ecritLigne(entete + " > " + info);
	}
}