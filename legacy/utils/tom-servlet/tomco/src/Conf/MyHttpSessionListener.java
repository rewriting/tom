package Conf;

import java.io.File;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Classe qui sera appellé à chaque création et destruction d'une session
 * @author cynthia
 *
 */
public class MyHttpSessionListener implements HttpSessionListener{

	@Override
	/**
	 * Methode qui créer le repertoire de la session dans le temporyfile
	 */
	public void sessionCreated(HttpSessionEvent se) {
		File d= detecteDirectory(se);
		if (!(d.exists())) {
			d.mkdirs(); // créer le rep
		}
	}

	@Override
	/**
	 * Methode qui efface le repertoire de la session dans le temporyfile
	 */
	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		File d= detecteDirectory(se);
		deleteDirectory(d);
	}
	
	/**
	 * Methode qui redonne le File qui sera créer dans un cas et supprimer dans l'autre
	 * @param se HttpSessionEvent 
	 * @return File du nom de l'identifiant de la session qui est dans le temporyFile
	 */
	private File detecteDirectory(HttpSessionEvent se){
		// on localise le repertoire temporaire
		File dir = (File)se.getSession().getServletContext().getAttribute("javax.servlet.context.tempdir");
		// on y créer un répertoire avec id de la session
		File d=new File(dir,se.getSession().getId());
		return d;
	}
	
	/**
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

}
