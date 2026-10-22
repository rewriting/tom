Le site web se déploit sur un conteneur tomcat sur un serveur Apache 
Pour cela, on utilise une archive war ((Web Application Archive) 

Le dossier a été créer sous eclipse qui peut générer un war automatiquement en 
faisant clique droit sur le dossier du projet puis export WAR File.
Ici, il n'a été fourni que les sources, aucun fichier de conf ou binaire. 
En cas de soucis, je répondrai volontier par mail: cynthia.florentin54@gmail.com
Il faudra en particulier faire attention à ce que le dossier applet retrouve un 
dossier de sortie pour ces .class dans WebContent. 

On peut aussi faire en ligne de commande le fichier war: 
Pour commencer, ouvrez un terminal et déplacez vous à la racine du projet
d'application web. Dans notre cas de l'architecture eclipse, il faut se déplacer 
dans WebContent (le répertoire qui contient le WEB-INF). 
Il suffit ensuite de tapper la ligne de commande suivante :
jar -cf mon_application.war .

Pour le déploiement, on peut suivre ce lien : 
http://wiki.mediabox.fr/tutoriaux/java/deployer_application_web

Pour plus d'information sur le code voir les sources et pour l'explication de 
la construction voir le pdf associé au projet qui sera surement rempli de faute
d'orthographe et je m'en excuse. 