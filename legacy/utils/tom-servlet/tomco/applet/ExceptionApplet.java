/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Classe qui indique que l'on ne pourra pas démarrer l'applet correctement car
 * au moment de faire <param name > dans la jsp, il y a eu une erreur avec
 * ConfString.getNameClass(). On a récupérer un nom de class qui était
 * inexistant ce qui s'est traduit par le retour d'un nom null.
 * 
 * @author cynthia
 */
public class ExceptionApplet extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2867159506917070684L;

	public ExceptionApplet() {
		super("L'applet n'a pu être démarré correctement: param class NULL");
	}

}
