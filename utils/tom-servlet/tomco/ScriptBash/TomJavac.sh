#! /bin/bash
# Script qui construit tout les fichiers nécessaires en mode pas à pas et qui
#spécifie toute erreur
################# Variable d'environnement tom ###############################
export TOM_HOME=/home/Application/tom
export PATH=${PATH}:${TOM_HOME}/bin
export CLASSPATH=${TOM_HOME}/lib/tom-runtime-full.jar:${CLASSPATH}
################################################################################
SCRIPT_FOLDER="$(cd $(dirname $0); pwd)/../WebContent/applet:${CLASSPATH}"
echo "${SCRIPT_FOLDER}"
cd $1

if [ $# != 2 ]; then
	echo "Usage $0 <chemin> <nom_fichier>"
    exit
fi

if [[ !(-f $2.t) ]]; then
	echo "Fichier $2 inexistant"
    exit
fi

echo "Lancement de la commande : tom "$2".t"
tom $2.t

if [[ !(-f $2.java) ]]; then
	echo "Fichier $2.java non cree"
    exit
fi

echo " Lancement de la commande : javac "$2".java"
javac -cp "${SCRIPT_FOLDER}" $2.java 

if [[ !(-f $2.class) ]]; then
	echo "Fichier $2.class non cree"
	exit
fi

jar cf etu.jar `ls` 

echo " CONSTRUCTION FICHIER END "
