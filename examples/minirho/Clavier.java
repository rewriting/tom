// Clavier.java
// Premiere version developpee par Didier Ferment: 
//                 objet a instancier systematiquement
// Modification par Cyrille Randrianamaro
//                 possibilite de ne pas instancier
// Puis Changement de nom pour TP algo UE1 MIAS: G. Richomme
// 29 janvier 2002-12 février 2002: G. Richomme (avec aide D. Ferment)
//         Modifications des commentaires. 
//         suppression de la possibilité d'instancier la classe.
//         disparition de l'attribut instance
//         mise en private de la fonction initialise
//         ajout de la méthode lireLigne
//         tentative de flushTotal mais Probleme.
//         mise en private des flush() car inutile pour etudiants
// Création javadoc:
// javadoc -d DocClavier -nodeprecated -nodeprecatedlist -nohelp -notree 
//           -noindex -nonavbar Clavier.java
package minirho;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Cette classe a pour but de fournir des outils de base pour la 
 * saisie de valeurs de certains type de base. <BR><BR>
 * Elle a été initialement écrite pour la mise en place des TPs de
 * JAVA dans les modules d'initiation à la programmation de DEUG MIAS 
 * première année a l'université de Picardie Jules Verne. <BR><BR>
 * Toutes les méthodes proposées dans la classe Clavier 
 * sont des méthodes de classe (seules méthodes vues au premier semestre).
 * La manière d'utiliser ces méthodes 
 *   est illustrée par l'exemple suivant&nbsp;:
 * <pre>
 *    int valeur ;                   // déclaration de la variable 
 *    ...
 *    valeur = Clavier.lireInt();    // saisie de la valeur
 *    ...
 * </pre>
 * <BR>
 * <b>Avertissement 1</b>. La classe Clavier doit, à priori, 
 *    être présente dans le 
 *    répertoire d'utilisation 
 *    à moins que (<i>Aspect Avancés (hors programme première année)</i>)
 *   le répertoire dans lequel elle est présente 
 *   ne soit précisé dans la variable d'environnement CLASSPATH
 *   ou dans l'option -classpath des commandes.<bR><BR>
 * <b>Avertissement 2</b>. La classe ne fonctionne qu'à partir de la 
 *     version 1.2 de JAVA.<bR><bR>
 * <b>Avertissement 3</b>.  Toutes les méthodes (à l'exception de lireLigne)
 *        proposées dans cette classe 
 *        ne tiennent pas compte des séparateurs 
 *        (espace, tabulation, retour chariot = passage à la ligne) 
 *        pour récupérer de l'information. (C'est ce qui se passe dans 
 *        beaucoup d'autres langages de programmation 
 *        (par exemple langage C, langage Pascal)).<br>
 *    On appelle <b>entité</b> toute suite de caractères consécutifs
 *    différents de l'espace, de la tabulation ou du retour chariot.<br><br>
 * <b>Informations pour ceux qui cherchent à comprendre</b> 
 * (<i>Aspects Avancés (hors programme première année)</i>).
 *  <ul>
 *  <li> Lorsque des caractères sont tapés au clavier, ils sont
 *       au fur et à mesure stockés dans un coin de la mémoire 
 *       appelé <i>tampon</i> (ou <i>buffer</i>).</li>
 *  <li> La manière de remplissage du tampon permet de corriger la frappe 
 *        tant qu'on n'a pas fait de "retour chariot" (touche Entrée).</li>
 *  <li> Lorsqu'on demande une information à l'utilisateur, on cherche cette
 *        information dans le tampon, quitte à attendre qu'il se remplisse.</li>
 *  <li> La saisie au clavier ou l'analyse de l'information cherchée peut 
 *          générer des erreurs (de type IOExceptions en JAVA).
 *          Elles sont gérées au minimum (on sort du programme).</li>
 *  </ul>
 */

/* Remarque. 
    Les commentaires du code présente des aspects avancés de programmation 
    (hors programme première année) */
public final class Clavier {

    /* L'objet buffer: l'initialisation à la valeur null permet de 
     * savoir plus tard que l'objet n'a pas été instancié.
     */
    private static BufferedReader bufIn = null;   

    /* L'objet pour analyser le buffer. On parle d'analyseur lexical. */
    private static StringTokenizer st = null;  

    /**
     * Le fait de mettre en private le constructeur rend 
     * la classe non instanciable. <BR>
     * Il n'y a qu'un clavier!
     */
    private Clavier() {}


    /**
     * Initialise le buffer.<bR>
     * Les données sont saisies par défaut au clavier 
     * (souvent appelé entrée standard et noté stdin)
     * Cette action (l'allocation) n'est exécutée qu'une fois. <BR>
     * Cette fonction est appelée lors de chaque lecture (via la fonction
     * read().
     *
     */
    private static void initialise() {
      if (bufIn == null)
        bufIn= new BufferedReader(new InputStreamReader(System.in));
      }

    /* Lecture d'une information dans le buffer */
    private static void read()  {
	if (bufIn == null)   // Ce test ne peut être vrai qu'une fois
	    initialise();    // définition du buffer 
	try {          
	    String s = bufIn.readLine();    // Lecture d'une ligne du buffer
	    st = new StringTokenizer(s);    // Instanciation objet d'analyse
	} catch (IOException e) {
	    System.err.println("read" + " " + e.getMessage());
	    System.exit(2); // Une erreur s'est produite, on sort du programme.
	}
    }



    /**
     * vide le tampon (buffer), en supprimant les caractères tapés 
     * jusqu'a cette instruction. <br>
     * Entre deux demandes d'information, l'utilisateur peut tapoter sur le 
     * clavier. En faisant appel a flush() avant la saisie d'une information,
     * cela permet de ne pas tenir compte de ce "tapotage".
     * BUG RECENSE. Ne vire pas ce qui reste dans le buffer de
     *     plus bas niveau stdin.
     */
    private static void flushTotal() {
        st = null;
	bufIn = null ;
    }

    /**
     * vide partiellement le tampon (buffer), 
     * en supprimant les caractères tapés non utilisés sur la dernière ligne. 
     * <br>
     * Pour éviter de saisir des informations tapées malencontreusement.
     */
    private static void flush() {
        st = null;
    }

    /** analyse la prochaine entité dans le buffer comme une constante
     * de type int (entier). <BR>
     * Les espaces, tabulations et sauts de lignes sont "passés".
     * La lecture attend l'arrivée d'autres caractères dans le buffer. <br>
     * @return    le nombre lu de type int
     * @exception NumberFormatException
     * erreur si la prochaine entité n'est pas de type int.
     */
    public static int lireInt()  {
	if (st == null)
	    read();
	while (! st.hasMoreTokens())
	    read();
	String ss = st.nextToken();
	int i = Integer.parseInt(ss);
	return(i);
    }

    /** analyse la prochaine entité dans le buffer comme une constante
     * de type long (entier). <BR>
     * Les espaces, tabulations et sauts de lignes sont "passés".
     * La lecture attend l'arrivée d'autres caractères dans le buffer. <br>
     * @return    le nombre lu de type long
     * @exception NumberFormatException
     * erreur si la prochaine entité n'est pas de type long
     */
    public static long lireLong()  {
	if (st == null)
	    read();
	while (! st.hasMoreTokens())
	    read();
	String ss = st.nextToken();
	long i = Long.parseLong(ss);
	return(i);
    }

    /** analyse la prochaine entité dans le buffer comme une constante
     * de type float (réel flottant). <BR>
     * Les espaces, tabulations et sauts de lignes sont "passés".
     * La lecture attend l'arrivée d'autres caractères dans le buffer. <br>
     * @return    le nombre lu de type float
     * @exception NumberFormatException
     * erreur si la prochaine entité n'est pas de type float
     */
    public static float lireFloat()  {
	if (st == null)
	    read();
	while (! st.hasMoreTokens())
	    read();
	String ss = st.nextToken();
	float f = Float.parseFloat(ss);
	return(f);
    }

    /** analyse la prochaine entité dans le buffer comme une constante
     * de type double (réel flottant en double précision). <BR>
     * Les espaces, tabulations et sauts de lignes sont "passés".
     * La lecture attend l'arrivée d'autres caractères dans le buffer. <br>
     * @return    le nombre lu de type double
     * @exception NumberFormatException
     * erreur si la prochaine entité n'est pas de type double
     */
    public static double lireDouble()  {
	if (st == null)
	    read();
	while (! st.hasMoreTokens())
	    read();
	String ss = st.nextToken();
	double f = Double.parseDouble(ss);
	return(f);
    }

    /** analyse la prochaine entité dans le buffer comme une constante
     * de type chaine de caractères. <BR>
     * Les espaces, tabulations et sauts de lignes sont "passés".
     * La lecture attend l'arrivée d'autres caractères dans le buffer. <br>
     * En particulier, on ne peut pas saisir une chaîne vide, ou
     *    une chaîne avec des espaces.
     * @return    la chaine de caractères lue de type String
     */
    public static String lireString()  {
	if (st == null)
	    read();
	while (! st.hasMoreTokens())
	    read();
	return(st.nextToken());
    }

    /** Retourne la chaîne composée de tous les caractères rencontrés
     * jusqu'au prochain retour chariot. <BR>
     * Permet de saisir une chaîne vide, ou
     *    une chaîne avec des espaces.
     * Si une information (par exemple un entier) a déjà été récupérée, 
     *     ne donne que la fin de la ligne (à partir du premier caractère, 
     *     après l'information, différent de l'espace et de la tabulation).
     * @return    la chaine de caractères lue de type String
     */
    public static String lireLigne()  {
	String s = "" ;
	if ((st == null) || (!st.hasMoreTokens()))
	{
	    if(bufIn == null) 
		initialise() ;
	    try{
		s = bufIn.readLine() ;
	    } catch (IOException e) {
		System.err.println("lireString" + " " + e.getMessage());
		System.exit(2); // Une erreur s'est produite, on sort du programme.
	    }
	    return s ;
	}
	else
	{
	    System.out.println("Autre cas") ;
	    return(st.nextToken(System.getProperty("line.separator")));
	}
    }
}
