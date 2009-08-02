package Servlet;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Conf.ConfString;

/**
 *	Classe qui exécute le traitement des requêtes clientes 
 * Le formulaire est constitué de 2 bouton, un textarea qui contient le code.
 * Il est sur le coté gauche de la page web
 * @author cynthia
 */
@SuppressWarnings("serial")
public class FormulaireServlet extends HttpServlet {

	/** 
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		// vérification de l'encodage
		String encoding = request.getCharacterEncoding();
		if (encoding == null || !encoding.equals("UTF-8")) {
			request.setCharacterEncoding("UTF-8");				
		}

		// 1 extraction des paramètres
		String script = request.getParameter("script");
		String button = request.getParameter("bouton");

		// 2 validation des paramètres
		if (button == null){
			return ;
		}

		if (script == null){
			return ;
		}

		String erreursScript = "";
		if (button.equals(ConfString.butRun)) {
			if ("".equals(script)) {
				erreursScript = (ConfString.ScriptVide);
			}
		}

		// 3 méthodes métier
		// requête dans une base de donnée mais on en a pas besoin ...

		// ------ Creation des variables-----
		HttpSession s = request.getSession();
		// utile pour la méthode n°1
		//String bash = ConfString.ScriptBash;
		String scriptBash = "";
		// récup le nom de la session pour créer le dossier
		// On  cherche à localiser le répertoire temporaire
		File dir = (File) getServletContext().getAttribute("javax.servlet.context.tempdir");
		String nameRepertoire = dir.getAbsolutePath()+File.separatorChar+s.getId();

		// 4 envoi vers la vue
		s.setAttribute("le_script", script);
		s.setAttribute("l_erreursScript", erreursScript);

		// on efface les fichiers
		deleteDirectory(new File(nameRepertoire));

		if (button.equals(ConfString.butReset)) {
			s.removeAttribute("le_script");
			s.removeAttribute("l_erreursScript");
			s.removeAttribute("le_scriptBash");
			s.removeAttribute("objetCacher");
			s.removeAttribute("le_nomClasse");
		} else { // fin action butReset
			if (button.equals(ConfString.butRun)) {
				if (!("".equals(script))) {

					/* ----------------Première partie
                     on construit les éléments de fonctionnement du script*/
					s.setAttribute("l_erreursScript", erreursScript);
					// on récup le contenu pour en faire un fichier
					String nameFile = creationFichier(nameRepertoire, script);
					// on vérifie qu'on a pu créer le fichier
					if (nameFile.equals("")) {
						s.setAttribute("l_erreursScript", ConfString.nomInvalide);
						s.removeAttribute("le_scriptBash");
						s.removeAttribute("objetCacher");
						s.removeAttribute("le_nomClasse");
					} else {
						scriptBash=scriptExecution(nameRepertoire, nameFile);
						// on envoit ce qui est afficher ds le textArea Resultat
						s.setAttribute("le_scriptBash", scriptBash);

						/* ----------------Deuxième partie
                     on construit les éléments de l'applet */
						// Test pour savoir si le fichier .class a été créer
						if (isCreateJar(nameRepertoire)){
							s.setAttribute("objetCacher",false);
							s.setAttribute("le_nomClasse", nameFile);
						}else{
							s.removeAttribute("objetCacher");
							s.removeAttribute("le_nomClasse");
						}

					}// fin de la creation du fichier
				}// fin du test sur le script vide
			}
		}// fin des actions des différents boutons
		response.sendRedirect("index.jsp");
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/** 
	 * Handles the HTTP <code>GET</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		processRequest(request, response);
	}

	/** 
	 * Handles the HTTP <code>POST</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		processRequest(request, response);
	}

	/** 
	 * Returns a short description of the servlet.
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Action suite au remplissage du formulaire";
	}// </editor-fold>

	/**
	 * Méthode qui donne un nom au fichier en fonction de ce qui a été entrer
	 * dans le textArea de code source.
	 * On cherche le groupe (1) qui se trouve entre ^public class (1) {$
	 */
	private String nameFile(String valeur) {
		String resultat = "";
		Pattern p = Pattern.compile(ConfString.regex);
		Matcher m = p.matcher(valeur);
		if (m.find()) {
			resultat = m.group(1);
		}
		return resultat;
	}

	/**
	 * Méthode qui créer un fichier .t avec le bon nom en fonction du contenu du
	 * textArea de code source.
	 * Il créer le répertoire si nécessaire.
	 * Cette méthode en profite pour redonner le nom du fichier
	 */
	private String creationFichier(String parent, String contenu) {
		String res = nameFile(contenu);
		// si il n'y a pas de nom de fichier, on ne le créer pas
		if (!(res.equals(""))) {
			String nameFile = res + ".t";
			if (!nameFile.equals(".t")) {
				try {
					File parentFile = new File(parent);
					if (!(parentFile.exists())) {
						parentFile.mkdirs(); // créer le rep
					}
					String nf=File.separatorChar+nameFile;
					PrintWriter out = new PrintWriter(new BufferedWriter
							(new FileWriter(parentFile+nf)));
					out.write(contenu);
					out.flush();
					out.close();
				} catch (IOException ex) {
					res="";
				}
			}
		}
		return res;
	}

	/**
	 * Méthode qui vérifie que le fichier .class a été créer
	 */
	private boolean isCreateJar(String path){
		File fileclass = new File(path+File.separatorChar+"etu.jar");
		return fileclass.exists();
	}

	/**
	 * Methode qui efface les fichiers de la session
	 * Supprimer un répertoire non-vide
	 * @param path chemin du repertoire
	 * @return true si tout c'est bien passé, sinon false
	 */
	private boolean deleteDirectory(File path) { 
		boolean resultat = true; 
		if( path.exists() ) { 
			File[] files = path.listFiles(); 
			for(int i=0; i<files.length; i++) { 
				if(files[i].isDirectory()) { 
					resultat &= deleteDirectory(files[i]); 
				}else { 
					resultat &= files[i].delete(); 
				} 
			} 
		} 
		resultat &= path.delete(); 
		return( resultat ); 
	} 

	/**
	 * Méthode qui fais l'execution d'une méthode et renvoie la sortie
	 * @param cmd une commande
	 * @return l'affichage de la sortie
	 */
	private String toStringExec(String[] cmd){
		StringBuffer sb=new StringBuffer();
		StringBuffer sb2=new StringBuffer();
		int tailleSb2init=sb2.length();
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			//on récupère le flux de sortie du programme en input
			InputStream in = p.getInputStream();
			StringBuilder buildIn=new StringBuilder();
			char cIn = (char) in.read();
			while (cIn != (char) -1) {
				buildIn.append(cIn);
				cIn = (char) in.read();
			}
			String responseIn = new String(buildIn);
			sb.append(responseIn);
			/*BufferedInputStream bis = new BufferedInputStream(p.getInputStream());
			byte i;
			while((i=(byte) bis.read()) > 0){
				sb.append(String.valueOf((char)i));
			}*/
			//on récupère le flux de sortie du programme en error
			InputStream err = p.getErrorStream();
			StringBuilder buildErr = new StringBuilder();
			char c = (char) err.read();
			while (c != (char) -1) {
				buildErr.append(c);
				c = (char) err.read();
			}
			String responseErr = new String(buildErr);
			sb2.append(responseErr);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (sb2.length()!=tailleSb2init){
			sb.append("---- ERREUR ----\n"+sb2.toString());
		}
		return sb.toString();
	}

	/**
	 * Méthode qui remplace l'exécution du fichier bash de départ
	 * Cette méthode fais tout le déroulement du fichier bash par java
	 */
	private String scriptExecution(String arg1,String arg2){
		StringBuffer sb = new StringBuffer();
		File fileTom = new File(arg1+File.separatorChar+arg2+".t");
		// on teste si le fichier .t existe 
		if (fileTom.exists()){
			// dans ce cas on exécute la méthode tom
			sb.append("Lancement de la commande : tom "+arg2+".t\n");
			String[] cmdTom= new String[]{"tom","-d",arg1,arg1+File.separatorChar+arg2+".t"};
			sb.append(toStringExec(cmdTom));
			File fileJava = new File(arg1+"/"+arg2+".java");
			// on teste que le fichier . java a été créer par la méthode précédente
			if (fileJava.exists()){
				// dans ce cas on exécute la méthode javac
				sb.append(" Lancement de la commande : javac "+arg2+".java\n");
				String classpath=System.getenv().get("CLASSPATH");
				String dossierApplet=getServletContext().getRealPath("")+File.separatorChar+"applet";
				String[] cmdJavac= new String[]{"javac","-cp",dossierApplet+":"
						+classpath,"-sourcepath",arg1,"-d",arg1,arg1+
						File.separatorChar+arg2+".java"};
				sb.append(toStringExec(cmdJavac));
				File fileClass = new File(arg1+File.separatorChar+arg2+".class");
				// on teste que le fichier .class a été créer
				if (fileClass.exists()){
					File tmpSession=new File(arg1);
					// on teste le repertoire où sont stockée les fichiers
					if (tmpSession.isDirectory()){
						// on exécute la méthode pour créer le jar
						sb.append(" Création du jar\n");
						String[] find=tmpSession.list();
						String[] cmdJar= new String[3+(find.length*3)];
						cmdJar[0]="jar";
						cmdJar[1]="cf";
						cmdJar[2]=arg1+File.separatorChar+"etu.jar";
						int j=3;
						for (int i = 0; i < find.length; i++) {
								cmdJar[(j++)]="-C";
								cmdJar[(j++)]=arg1;
								cmdJar[(j++)]=find[i];
						}
						sb.append(toStringExec(cmdJar));
						File fileJar=new File(arg1+File.separatorChar+"etu.jar");
						// teste que le jar a été créer
						if (fileJar.exists()) {
							sb.append(" CONSTRUCTION FICHIER END\n");
						} else {
							sb.append("Jar non créé\n");
						}
					}else{
						sb.append("Le repertoire où sont stockée les fichiers sur le serveur est introuvable\n");
					}
				}else{
					sb.append("Fichier "+arg2+".class non cree\n");
				}
			}else{
				sb.append("Fichier "+arg2+".java non cree\n");
			}
		}else{
			sb.append("Fichier "+arg2+".t inexistant\n");
		}
		return sb.toString();
	}
}
