<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="fonctionIndex.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="java.util.Random"%>
<%@page import="java.util.Date"%><html
	xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
<head>
<title>Online TOM Compiler</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="language" content="fr-FR" />
<meta name="description" content="Compiler Online TOM" />
<meta name="keywords" content="Online, Compiler, Tom, Loria" />
<link rel="icon" type="image/png" href="image/logoSite.png" />
<style type="text/css" title="StyleParDefaut" media="screen">
@import url(./styleIndex.css);
</style>
<script type="text/javascript" src="numeroteLigne.js">
</script>
</head>

<body>
<h1 class="title">Compiler Online TOM and Java</h1>


<ul id="nav">
	<li><a
		href="http://tom.loria.fr/wiki/index.php5/Documentation:Support"
		title="atteindre les supports de cours">Support de cours</a></li>
	<li><a href="http://tom.loria.fr/wiki/index.php5/Documentation"
		title="aller à la documentation de tom">Documentation TOM</a></li>
	<li><a href="<%=RedonneURL(request)%>Help.html" title="aller à l'aide">Aide</a></li>

</ul>

<%-- ######### formulaire de saisie ############ --%>
<div class="content">
<div id="formulairebouton" class="formulaire">
<form method="post" action="FormulaireServlet" id="form">
<div>
<b>Code Source: </b><br/>
<%=ErreurScript(request, "l_erreursScript")%>
<span id="controls" style="display: none;">
Numéros de ligne <input type="checkbox" id="numline" />
</span>
<textarea name="script" id="script" cols="55" rows="20"><%=SessionTextArea(request, "le_script")%></textarea>
<br/>
<input id="run" type="submit" name="bouton"
	value="<%=ConfString.butRun%>" /> <input type="submit" name="bouton"
	value="<%=ConfString.butReset%>" />
</div>
</form>
</div>
<%-- ######### Resultat du formulaire  ############ --%>
<div class="Retour_Serveur">Id de la session : <%=idSession(request)%>
<textarea id="ResultatBash" name="ResultatBash" cols="60" rows="<%= tailleTextArea(!isHidden(request, "objetCacher")) %>"
	readonly="readonly"> <%=RedonneString(request, "le_scriptBash")%> </textarea>
<br />
<br />
<%
	if (!isHidden(request, "objetCacher")) {
%> <object
	codetype="application/java" classid="java:AppletExecutionTom"
	codebase="applet" standby="Veuillez patienter..." width="400"
	height="270">
	<param name="<%=ConfString.nameClass%>"
		value="<%=RedonneString(request, "le_nomClasse")%>" />
	<param name="cache_archive"
		value="<%=RedonneURL(request)%>JarAppletServlet,<%=RedonneURL(request)%>applet/tom-runtime.jar" />
	<param name="cache_version" value="0.0.0.<%=new Date().getTime()%>" />
	<ul id="pbApplet">
		<li>Si votre système d'exploitation est basé sur Debian (par exemple Ubuntu) : tapez dans un terminal
		"sudo apt-get install sun-java6-plugin"</li>
		<li>Sinon Télécharger <a
			href="http://www.javasoft.com/products/plugin/"
			title="download plugin java">le plugin java pour votre navigateur</a></li>
		<li>Ou bien allez voir ce <a
			href="http://www.java.com/fr/download/help/enable_browser.xml"
			title="depannage">lien pour dépanner si une applet ne s'exécute pas</a></li>
	</ul>
</object>
<form method="post" action="FormRServlet">
	<div>
	<input type="submit" name="boutonApplet"
	value="<%=ConfString.butStop%>" /> 
	<a id="presentationBouton" href="<%=RedonneURL(request)%>JarAppletServlet"
		title="Enregistrer les fichiers crées"><%=ConfString.butSave%></a>
	</div>    
	
</form>

<%
	}
%>
</div>
</div>

<hr />
<%-- ######### Information vers Webmaster ############ --%>
<div id="webmaster">
<p class="mail">Webmaster Email: <a
	href="mailto:Pierre-Etienne.Moreau@loria.fr">Pierre-Etienne.Moreau@loria.fr</a></p>
<p class="copyright">&copy; licence GPL 2009 - Equipe PAREO - LORIA.</p>
<p class="valide">Valid <a
	href="http://validator.w3.org/check/referer"
	title="Check page validity">XHTML 1.0</a> and <a
	href="http://jigsaw.w3.org/css-validator/validator?uri=<%=RedonneURL(request)%>miseEnPage.css"
	title="Check CSS Style Validity">CSS2</a> Page.</p>
</div>


</body>
</html>