
<%-- ######### Liste des imports ############ --%>
<%@ page import="Conf.ConfString" %>


<%-- ######### Liste des noms des noms des attributs ############ --%>
<%
        String lang = "Language";
        int _TIME_OUTdeb = 3;
        int _TIME_OUTfin = 10;
        int _TIME_OUTpas = 2;
        boolean defauttimeout = false;
        HttpSession client = request.getSession();

%>

<%-- ######### methode de conservation de la saisie ############ --%>
<%!

// Méthodes
    /**
     * Méthode qui redonne la valeur liée à un objet html
     */
    private String getParam(HttpServletRequest request, String param) {

        if (request.getParameter(param) == null) {
            return "";
        } else {
            return request.getParameter(param);
        }
    }

    /**
     * Méthode qui permet de notifier à l'utilisateur l'id de la session
     */
    private String idSession(HttpServletRequest request) {
        return "<i id=\"couleurvert\">" + request.getSession().getId() + "</i><br/>";
    }

    /**
     * Méthode qui permet de sauvegarder ce qui a été sélectionner précédemment
     * sans revenir à la valeur par défaut dans un menu déroulant
     */
    private String SessionSelected(HttpServletRequest request, String name, String def) {
        String resultat = def;
        Object val = request.getSession().getAttribute(name);
        if (val != null) {
            resultat = val.toString();
        }
        return resultat;
    }

    /**
     * Méthode pour empécher de traiter un morceau du formulaire qui devrait
     * être rempli
     */
    private String ErreurScript(HttpServletRequest request, String name) {
        String resultat = "<br/>";
        Object val = request.getSession().getAttribute(name);
        if (val != null) {
            resultat = "<i id=\"couleured\">" + val.toString() + "</i><br/>";
        }
        return resultat;
    }

    /**
     * Méthode qui permet de sauvegarder ce qui a été écrit précédemment
     * sans perdre en cas de rechargement de la page
     */
    private String SessionTextArea(HttpServletRequest request, String name) {
        String resultat =ConfString.CodeInitial2 ;
        Object val = request.getSession().getAttribute(name);
        if (val != null) {
            resultat = val.toString();
        }
        return resultat;
    }
    
    /**
     * Méthode qui permet de sauvegarder ce qui a été écrit précédemment
     * sans perdre en cas de rechargement de la page
     */
    private String RedonneString(HttpServletRequest request, String name) {
        String resultat = "";
        Object val = request.getSession().getAttribute(name);
        if (val != null) {
            resultat = val.toString();
        }
        return resultat;
    }
    
    private int tailleTextArea(boolean condition){
    	int taille =4;
    	if (!condition){
    		taille =22;
    	}
    	return taille;
    }

    /**
     * Méthode qui cache les objets name tant qu'il n'y a pas possibilité de les
     * utiliser à cause de la condition
     * cond=true signifie qu'on peut arcacher
     */
    private boolean isHidden(HttpServletRequest request, String cond) {
        String resultat = ConfString.hidden;
        Object val = request.getSession().getAttribute(cond);
        Boolean valeur = new Boolean(true);
        if (val != null) {
            valeur = new Boolean(val.toString());
        }
        return valeur;
    }
    
    /**
     * Méthode qui redonne l'url du site qui est exécuté
     */
    private String RedonneURL(HttpServletRequest request){
    	StringBuffer urlLong=request.getRequestURL();
    	// on enléve index.jsp de l'url ou on enléve la partie derrière le dernier /
    	int index=urlLong.lastIndexOf("/");
    	String urlCourt = urlLong.substring(0,index+1);
    	
    	return urlCourt;
    }

%>