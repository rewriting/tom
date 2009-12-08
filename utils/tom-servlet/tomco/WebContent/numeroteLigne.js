/**
 * Cette méthode permet d'ajouter le bon nombre de blanc avant le texte. 
 * En effet si il y a 100 lignes il y aura des blanc en plus que si il y a 9 lignes
 * @param str la ligne 
 * @param num le nombre de blanc
 * @return
 */
function repeatString(str, num){
 var returnStr='';
 for(k=1; k<=num; k++){
  returnStr+=str;
 }
 return returnStr;
}

function lineNumbers(checkbox){
 page=document.getElementById('script');
//On sauvegarde la position de la scrollbar pour firefox qui ne se replace pas automatiquement contrairement aux autres navigateurs.
 posY=page.scrollTop;
 posX=page.scrollLeft;
//On découpe le champ texte par ligne et on le stock dans un tableau.
 array=page.value.split("\n");
//on conserve la place du curseur
 start=page.selectionStart;
 end=page.selectionEnd;
 //on calcule la taille du texte
 maxLength=String(array.length).length;
// on place les blancs et on regarde où est placé le curseur
//On rajoute/enlève le numéro pour chaque ligne du champ.
 tot=0;
 diff=0;
 for(j=0; j<array.length; j++){
  if(checkbox.checked==true) {
	  if(tot<=start){
		  diff+=maxLength+1;
	  }
	  tot+=array[j].length+1;
	  array[j]=String(j+1)+repeatString(' ', maxLength+1-String(j+1).length)+array[j];
  } else {
	  if(tot<=start){
		  diff-=maxLength+1;
	  }
	  tot+=array[j].length+1;
	  array[j]=array[j].substring(maxLength+1, array[j].length);
  }
 }
 page.value=array.join("\n");
 // on replace le curseur
 page.focus();
 page.setSelectionRange(start+diff, end+diff); 
//On replace la scrollbar de Firefox.
 page.scrollTop=posY;
 page.scrollLeft=posX;
}
window.onload=function (){
// On affiche la checkbox.
 document.getElementById('controls').style.display='inline';
 numline=document.getElementById('numline');
 numline.onclick = function() {
	 lineNumbers(numline);
 };
 form=document.getElementById('form');
 form.onsubmit = function() {
	 if(numline.checked==true){
		 numline.checked=false;
		 lineNumbers(numline);
	 }
 };
 page=document.getElementById('script');
 // si il y a un bouton de pressé ou un lettre de taper on enléve les numéros
 page.onkeypress=function (event){
  var key=window.event?window.event.keyCode:event.keyCode;
  if(numline.checked==true){
   numline.checked=false;
   lineNumbers(numline);
   return true;
  }
 };
};
