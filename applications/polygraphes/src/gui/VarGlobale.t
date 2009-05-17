package gui;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class VarGlobale{
	
	public static int filhauteurdefaut = 15;
	public static int modeaffichage=0
	public static Log mLog;
	
	public static void ecritLigne(String str){
		if (mLog == null){
			mLog = new Log("./log_" + getDateTime() + ".txt");
		}
		mLog.ecritLigne("File : " + str);
	}
	
	 private static String getDateTime() {
	      DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	      Date date = new Date();
	      return dateFormat.format(date);
	  }
	 
	 public static void ModeAffichage(String s){
		 if(s.compareTo("")!=0) modeaffichage=Integer.parseInt(s);
	 }
}