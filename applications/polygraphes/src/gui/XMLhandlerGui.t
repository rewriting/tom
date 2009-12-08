package gui;

import org.w3c.dom.*;

import java.io.File;
import adt.polygraphicprogramgui.types.*;
import adt.polygraphicprogramgui.types.onepath.*;
import adt.polygraphicprogramgui.types.twopath.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

//provides all the functions to transform terms in xml and vice-versa

public class XMLhandlerGui {
  %include{ dom.tom }
  %include { ../adt/polygraphicprogramgui/PolygraphicProgramgui.tom }
  %include { sl.tom }

  // save a String in a new File
  public static void save(String fileContent,File file) throws IOException {
		PrintWriter printWriter = new PrintWriter(new FileOutputStream(
				file));
		printWriter.print(fileContent);
		printWriter.flush();
		printWriter.close();
	}

  //get the text from a text file
  public static String load(String filepath) throws IOException {
		FileChannel channel = new FileInputStream(new File(filepath)).getChannel();
		try {
			ByteBuffer b = ByteBuffer.allocate((int) channel.size());
			channel.read(b);
			return new String(b.array());
		} finally {
			channel.close();
		}
	}


  // make a 1-path term from its xml description
  public static OnePath makeOnePath(Node node){
	  String nodeName =node.getNodeName();
	  if(nodeName.equals("OnePath")){
		  NodeList nodeChilds=node.getChildNodes();
		  for (int i = 0; i < nodeChilds.getLength(); i++) {
			  Node nodeChild=nodeChilds.item(i);
			  if(!nodeChild.getNodeName().equals("#text")){return makeOnePath(nodeChild);}
		  }
	  }
	  if(nodeName.equals("OneCell")){
		  NamedNodeMap attributes=node.getAttributes();
		  String name=attributes.getNamedItem("Name").getNodeValue();
		  return `OneCell(name,0,0,VarGlobale.filhauteurdefaut,9);
	  }
	  if(nodeName.equals("OneC0")){
		  NodeList oneC0s=node.getChildNodes();
		  OnePath res=`Id();
		  for (int j = oneC0s.getLength()-1; j > 0; j--) {
			  Node oneC0Element = oneC0s.item(j);
			  if(!oneC0Element.getNodeName().contains("#text")){
				  //res=`OneC0(makeOnePath(oneC0Element),res);
				  
				  OnePath res2=res;
				  OnePath res3 = makeOnePath(oneC0Element);
				  
				  res= gestionOneC0(res3,res2);
			  }				
		  }
		  return res;
	  }
	  NodeList childs=node.getChildNodes();
	  for (int i = 0; i < childs.getLength(); i++) {
		  Node child = childs.item(i);
		  if(!child.getNodeName().equals("#text")){
			  return makeOnePath(child);}
	  }
	  return `Id();
  }

  // make a 2-path term from its xml description
  public static TwoPath makeTwoPath(Node node){
		
	  String nodeName =node.getNodeName();
	  if(nodeName.equals("TwoPath")){
		  NodeList nodeChilds=node.getChildNodes();
		  for (int i = 0; i < nodeChilds.getLength(); i++) {
			  Node nodeChild=nodeChilds.item(i);
			  if(!nodeChild.getNodeName().equals("#text")){return makeTwoPath(nodeChild);}
		  }
	  }
	  if(nodeName.equals("TwoId")){
		  NodeList nodeChilds=node.getChildNodes();
		  for (int i = 0; i < nodeChilds.getLength(); i++) {
			  Node nodeChild=nodeChilds.item(i);
			  if(!nodeChild.getNodeName().equals("#text")){
				  TwoPath ti = `TwoId(makeOnePath(nodeChild));		  
				  return ti;
			  }
		  }
	  }
	  if(nodeName.equals("TwoCell")){
		  NamedNodeMap attributes=node.getAttributes();
		  String name=attributes.getNamedItem("Name").getNodeValue();
		  String type=attributes.getNamedItem("Type").getNodeValue();
		  CellType celltype=null;
		  if(type.equals("Function")){celltype=`Function();}
		  if(type.equals("Constructor")){celltype=`Constructor();}
		  NodeList io=node.getChildNodes();
		  OnePath source=`Id();
		  OnePath target=`Id();
		  for (int j = 0; j < io.getLength(); j++) {
			  Node ioChild=io.item(j);
			  String ioName =ioChild.getNodeName();
			  if(ioName.equals("Source")){source=makeOnePath(ioChild);}
			  if(ioName.equals("Target")){target=makeOnePath(ioChild);}
		  }
		  
		  TwoPath res = `TwoCell(name,source,target,celltype,0,0,0,50,80);
		  res=RepartitionFilsTwoCell((TwoCell)res);
		  return res;
	  }
	  if(nodeName.equals("TwoC0")){
		  NodeList twoC0s=node.getChildNodes();
		  TwoPath res=`TwoId(Id());
		  for (int j = twoC0s.getLength()-1; j >0; j--) {
			  Node twoC0Element = twoC0s.item(j);
			  if(!twoC0Element.getNodeName().contains("#text")){
				  TwoPath res2=res;
				  TwoPath res3 = makeTwoPath(twoC0Element);
				  res= gestionTwoC0(res3,res2);
				  
			  }	
		  }
		  return res;
	  }
	  if(nodeName.equals("TwoC1")){
		  NodeList twoC1s=node.getChildNodes();
		  TwoPath res=`TwoId(Id());
		  for (int j = twoC1s.getLength()-1; j >0; j--) {
			  Node twoC1Element = twoC1s.item(j);
			  if(!twoC1Element.getNodeName().contains("#text")){
				  if(res==`TwoId(Id())){res=`makeTwoPath(twoC1Element);}
				  else{
					  TwoPath res2=res;
					  TwoPath res3 = makeTwoPath(twoC1Element);
					  System.out.println("TwoC1 :(");
					  VarGlobale.ecritLigne("TwoC1 :(");
					  System.out.print(" res2:");VarGlobale.ecritLigne(" res2:");res2.affiche();
					  System.out.print(" res3:");  VarGlobale.ecritLigne(" res3:");res3.affiche();
					  System.out.println(")");
					  VarGlobale.ecritLigne(")");
					  res= gestionTwoC1(res3,res2);
				  }
			  }
		  }
		  return res;
	  }
	  NodeList childs=node.getChildNodes();
	  for (int i = 0; i < childs.getLength(); i++) {
		  Node child = childs.item(i);
		  if(!child.getNodeName().equals("#text")){
			  return makeTwoPath(child);
		  }	
	  }
	  return `TwoId(Id());
  }

//make a 3-path term from its xml description
  public static ThreePath makeThreeCell(Node node){
	  NamedNodeMap attributes=node.getAttributes();
	  String name=attributes.getNamedItem("Name").getNodeValue();
	  String type=attributes.getNamedItem("Type").getNodeValue();
	  CellType celltype=null;
	  if(type.equals("Function")){celltype=`Function();}
	  if(type.equals("Constructor")){celltype=`Constructor();}//never used
	  NodeList io=node.getChildNodes();
	  TwoPath source=`TwoId(Id());
	  TwoPath target=`TwoId(Id());
	  for (int j = 0; j < io.getLength(); j++) {
		  Node ioChild=io.item(j);
		  String ioName =ioChild.getNodeName();
		  if(ioName.equals("Source")){source=makeTwoPath(ioChild);}
		  if(ioName.equals("Target")){target=makeTwoPath(ioChild);}
	  }
	  return `ThreeCell(name,source,target,celltype);
  }
  
	//Set of functions converting polygraphic terms in strings based on the xml format we chose
	//return the xml description of a 2-Path
	public static String twoPath2XML(TwoPath path){
		%match (TwoPath path){
			TwoId(onepath) -> {return "<TwoPath>\n<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n</TwoPath>\n";}
			TwoCell(name,source,target,type,id,_,_,_,_) -> { return "<TwoPath>\n<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n</TwoPath>\n"; }
			TwoC0(head,tail*) -> {return "<TwoPath>\n<TwoC0>\n"+twoC02XML(`head)+twoC02XML(`tail)+"</TwoC0>\n</TwoPath>\n";}
			TwoC1(head,tail*) -> {return "<TwoPath>\n<TwoC1>\n"+twoC12XML(`head)+twoC12XML(`tail)+"</TwoC1>\n</TwoPath>\n";}
		}
		return "";
	}
	
	//sub-function of the previous one to handle twoC0s
	public static String twoC02XML(TwoPath path){
	%match (TwoPath path){
					TwoId(onepath) -> {return "<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n";}
					TwoCell(name,source,target,type,id,_,_,_,_) -> { return "<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n"; }
					TwoC0(head,tail*) -> {return twoC02XML(`head)+twoC02XML(`tail);}
					TwoC1(head,tail*) -> {return "<TwoC1>\n"+twoC12XML(`head)+twoC12XML(`tail)+"</TwoC1>\n";}
		}
	return "";
	}
	//the same for twoC1s
	public static String twoC12XML(TwoPath path){
		%match (TwoPath path){
			TwoId(onepath) -> {return "<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n";}
			TwoCell(name,source,target,type,id,_,_,_,_) -> { return "<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n"; }
			TwoC0(head,tail*) -> {return "<TwoC0>\n"+twoC02XML(`head)+twoC02XML(`tail)+"</TwoC0>\n";}
			TwoC1(head,tail*) -> {return twoC12XML(`head)+twoC12XML(`tail);}
		}
		return "";
	}
	//same idea with 1-paths
	public static String onePath2XML(OnePath path){
		%match (OnePath path){
			Id() -> {return "<OnePath>\n<Id></Id>\n</OnePath>\n";}
			OneCell(name,_,_,_,_) -> { return "<OnePath>\n<OneCell Name=\""+`name+"\"></OneCell>\n</OnePath>\n"; }
			OneC0(head,tail*)->{ return "<OnePath>\n<OneC0>\n"+oneC02XML(`head)+oneC02XML(`tail)+"</OneC0>\n</OnePath>\n";}
		}
		return "";
	}
	//and for OneC0s
	public static String oneC02XML(OnePath path){
		%match (OnePath path){
			Id() -> {return "<Id></Id>\n";}
			OneCell(name,_,_,_,_) -> { return "<OneCell Name=\""+`name+"\"></OneCell>\n"; }
			OneC0(head,tail*)->{ return oneC02XML(`head)+oneC02XML(`tail);}
		}
		return "";
	}
	
	//return the xml description of a 3-Cell
	public static String threePath2XML(ThreePath path){
		if(path instanceof ThreePath){
			return "<ThreeCell Name=\""+path.getName()+"\" Type=\""+path.getType().toString().replace("()","")+"\">\n<Source>\n"+twoPath2XML(path.getSource())+"</Source>\n<Target>\n"+twoPath2XML(path.getTarget())+"</Target>\n</ThreeCell>\n";
		}
		else {System.out.println("this is not a ThreeCell !"+path);return null;} 
	}
	
	/*
	 * Methode David : Permet de decaler sur l'axe des Y les coordonner de l'element cible (path) d'un taille c
	 */
	public static OnePath decalageY(OnePath path,int c){
		%match (OnePath path){
			Id() -> {return `Id();}
			OneCell(name,x,y,hauteur,largeur) -> {return `OneCell(name,x,y+c,hauteur,largeur); }
		 	OneC0 (head,tail*) -> {return `OneC0(decalageY(head,c),decalageY(tail*,c));}
		}
		return path;
	}
	
	/*
	 * Methode David : Permet de decaler sur l'axe des Y les coordonner de l'element cible (path) d'un taille c
	 */
	public static TwoPath decalageY(TwoPath path ,int c){
		%match (TwoPath path){
			TwoId(onepath) -> {return `TwoId(decalageY(onepath,c));}
			TwoCell(name,source,target,type,id,x,y,hauteur,largeur) -> { return `TwoCell(name,decalageY(source,c),decalageY(target,c),type,id,x,y+c,hauteur,largeur); }
			TwoC0(head,tail*) -> {return `TwoC0(decalageY(head,c),decalageY(tail*,c));}
			TwoC1(head,tail*) -> {return `TwoC1(decalageY(head,c),decalageY(tail*,c));}
		}
		return path;
	}
	
	/*
	 * Methode David : Permet de decaler sur l'axe des X les coordonner de l'element cible (path) d'un taille c
	 */
	public static OnePath decalageX(OnePath path,int c){
		%match (OnePath path){
			Id() -> {return `Id();}
			OneCell(name,x,y,hauteur,largeur) -> {return `OneCell(name,x+c,y,hauteur,largeur); }
		 	OneC0 (head,tail*) -> {return `OneC0(decalageX(head,c),decalageX(tail*,c));}
		}
		return path;
	}
	
	/*
	 * Methode David : Permet de decaler sur l'axe des Y les coordonner de l'element cible (path) d'un taille c
	 */
	public static TwoPath decalageX(TwoPath path ,int c){
		%match (TwoPath path){
			TwoId(onepath) -> {return `TwoId(decalageX(onepath,c));}
			TwoCell(name,source,target,type,id,x,y,hauteur,largeur) -> { return `TwoCell(name,decalageX(source,c),decalageX(target,c),type,id,x+c,y,hauteur,largeur); }
			TwoC0(head,tail*) -> {return `TwoC0(decalageX(head,c),decalageX(tail*,c));}
			TwoC1(head,tail*) -> {return `TwoC1(decalageX(head,c),decalageX(tail*,c));}
		}
		return path;
	}
	
	/*
	 * On  recupere ici la structure qu'on cherche a modifier
	 * de facon a recupere par la liste par tranche 
	 * Ex : 
	 * 	  |         |
	 *  ------    ------
	 *  | C1 |    | C2 |
	 *  ------    ------
	 *    |         |
	 *  ---------------
	 *  |      C3     |
	 *  ---------------
	 * 	  |         |
	 *  ------    ------
	 *  | C4 |    | C5 |
	 *  ------    ------
	 *    |         |
	 *    
	 *  On recupere d'abord C1, et C2, puis C3 puis C4 et C5
	 */
	public static ArrayList<TwoPath> niveauC1(TwoPath tc1){
		ArrayList<TwoPath> res = new ArrayList<TwoPath>();
		%match (TwoPath tc1){
			TwoId(onepath) -> {res.add(tc1);}
			TwoCell(_,_,_,_,_,_,_,_,_) -> { res.add(tc1); }
			TwoC0(head,tail*) -> {res.add(tc1);}
			TwoC1(head,tail*) -> {res.addAll(niveauC1(`head)); res.addAll(niveauC1(`tail*));}
		}
		return res;
	}
	
	/*
	 * Methode qui permet de modifier uniquement la partie basse d'un TwoC1 dans le cadre de la construction d'un TwoC1
	 * Ex, si on doit modifier un TwoC1 comme celui ci:
	 * 	  |         |
	 *  ------    ------
	 *  | C1 |    | C2 |
	 *  ------    ------
	 *    |         |
	 *  ---------------
	 *  |      C3     |
	 *  ---------------
	 * 	  |         |
	 *  ------    ------
	 *  | C4 |    | C5 |
	 *  ------    ------
	 *    |         |
	 *   Alors il faut modifier uniquement C4 et C5 et conservé le reste du TwoC1
	 *   Pour cela il faut utilisé une méthode qui permet de determiner combien d'element de la liste sont consommé
	 */
	public static TwoPath completudemode_1(TwoPath tp,TwoPath sol,int n){
		ArrayList<TwoPath> tmp = getListeTwoPath(sol);
		%match (TwoPath tp){
			TwoId(onepath) -> { return tmp.get(n);}
			TwoCell(_,_,_,_,_,_,_,_,_) -> { return tmp.get(n); }
			TwoC0(head,tail*) -> {return `TwoC0(completudemode_1(head,sol,n),completudemode_1(tail*,sol,n+consommation(head)));}
			TwoC1(head*,tail) -> {return `TwoC1(head*,completudemode_1(tail,sol,n));}
		}
		return sol;
	}
	
	/*
	 * Methode qui compte le nombre de cellule qu'il faut modifier
	 */
	public static int consommation(TwoPath tp){
		%match (TwoPath tp){
			TwoId(onepath) -> { return 1;}
			TwoCell(_,_,_,_,_,_,_,_,_) -> { return 1; }
			TwoC0(head,tail*) -> {return consommation(`head)+consommation(`tail*);}
			TwoC1(head*,tail) -> {return consommation(`tail);}
		}
		return 0;
	}
	
	/*
	 * Methode qui cree des TwoC1 (et s'occupe de mettre correctement les fils)
	 * Mode 0 = On modifie le bas (amodifier) par rapport au haut (modele)
 	 * Mode 1 = On modifie le haut (amodifier) par rapport au bas (modele)
	 */
	public static TwoPath gestionC1(TwoPath amodifier,TwoPath modele,int mode){
		
		// creation de la liste des elements a modifier
		ArrayList<TwoPath> listeTwoPath_amodifier;
		if(mode==0) listeTwoPath_amodifier = getListeTwoPathHaut(amodifier);
		else listeTwoPath_amodifier = getListeTwoPathBas(amodifier);
		
		ArrayList<TwoPath> listeTwoPath_retour = new ArrayList<TwoPath>();
		
		// creation de la liste des fils de l'element a modifier
		ArrayList<OnePath> listeOnePath_amodifier;
		if(mode==0) listeOnePath_amodifier = getListeOnePathSource(amodifier);
		else listeOnePath_amodifier = getListeOnePathTarget(amodifier);
		
		// creation de la liste des fils de l'element modele
		ArrayList<OnePath> listeOnePath_modele;
		if(mode==0) listeOnePath_modele = getListeOnePathTarget(modele);
		else listeOnePath_modele = getListeOnePathSource(modele);
		
		ArrayList<OnePath> listeOnePath_tmp  = new ArrayList<OnePath>();
		
		Iterator it_listeOnePath_amodifier = listeOnePath_amodifier.iterator();
		Iterator it_listeOnePath_modele = listeOnePath_modele.iterator();
		
		// On cree les nouveaux OneCell a partir de données du modele et de amodifier
		// pour chaque fil du modele, on modifie chaque fil a modifier
		// Cette methode ignore les ID
		while(it_listeOnePath_amodifier.hasNext() && it_listeOnePath_modele.hasNext()){
			OnePath c2 = (OnePath)it_listeOnePath_modele.next();
			if(c2 instanceof OneCell){
				OnePath c1 = (OnePath)it_listeOnePath_amodifier.next();
				if(c1 instanceof OneCell && c2 instanceof OneCell) listeOnePath_tmp.add(`OneCell(c1.getName(),c2.getx(),c1.gety(),c1.gethauteur(),c1.getlargeur()));
				else listeOnePath_tmp.add(`Id());
			}
		}
		
		Iterator it_listeOnePath_retour = listeOnePath_tmp.iterator();
		Iterator it_listeTwoPath_amodifier = listeTwoPath_amodifier.iterator();
		int newx = -1;
		
		//On modifie maintenant les cellules
		while(it_listeTwoPath_amodifier.hasNext()){
			
			//pour chaque cellule
			TwoPath tp = (TwoPath)it_listeTwoPath_amodifier.next();
			int nb=-1;
			int newlargeur = -1;
			if(mode==0) nb=tp.sourcesize();
			else nb=tp.targetsize();
			
			ArrayList<OnePath> tmp = new ArrayList<OnePath>();
			System.out.println(listeOnePath_tmp);
			VarGlobale.ecritLigne(listeOnePath_tmp.toString());
			// on prend les fils de la liste modele pour les mettre dans la cellule
			for(int i=0;i<nb;i++){
				OnePath tmpoc = listeOnePath_tmp.get(0);
				if(tmpoc instanceof OneCell){
					// cas particulier : en Mode 0 (ie on modifie bas par rapport a haut)
					// Lorsque c'est le premier fil, on la cellule sera déplacé jusqu'a la position du fil
					if (i==0 && mode==0 && newx!=-1) newx=tmpoc.getx();
					// cas particulier : en Mode 1 (ie on modifie haut par rapport a bas)
					// La cellule prendra en largeur la position du dernier fil
					if (i==nb-1 && mode==1) newlargeur=tp.getLargeur();
					// cas particulier : 
					// En mode 0, la largeur de la nouvelle cellule sera la position du dernier fil
					// En mode 1, Si la position du dernier fil est plus loin que la largeur de la cellule, alors modifier la largeur en consequence
					if ((i==nb-1 && mode==0) ||( i==nb-1 && mode==1 && ((tmpoc.getx())>(newx+tp.getLargeur())))) newlargeur=tmpoc.getx()-newx+4;				
					tmp.add(tmpoc);
				} else tmp.add(`Id());
				
				listeOnePath_tmp.remove(0);
			}
			if(!it_listeTwoPath_amodifier.hasNext()) newlargeur=modele.getLargeur()-newx;
			System.out.println("new : "+newx+" "+newlargeur);
			VarGlobale.ecritLigne("new : "+newx+" "+newlargeur);
			
			// En fonction du type de cellule, on reconstruit la cellule avec les nouveaux fils selon le mode prévu
			if(tp instanceof TwoCell){
				if(mode==0){
					if(newx!=-1) listeTwoPath_retour.add(`TwoCell(tp.getName(),genererOneC0(tmp),tp.target(),tp.getType(),tp.getID(),newx,tp.getY(),tp.getHauteur(),newlargeur));
					else listeTwoPath_retour.add(`TwoCell(tp.getName(),genererOneC0(tmp),tp.target(),tp.getType(),tp.getID(),tp.getX(),tp.getY(),tp.getHauteur(),newlargeur));
				}else{
					if(newx!=-1) listeTwoPath_retour.add(`TwoCell(tp.getName(),tp.source(),genererOneC0(tmp),tp.getType(),tp.getID(),newx,tp.getY(),tp.getHauteur(),newlargeur));
					else listeTwoPath_retour.add(`TwoCell(tp.getName(),tp.source(),genererOneC0(tmp),tp.getType(),tp.getID(),tp.getX(),tp.getY(),tp.getHauteur(),newlargeur));
				}
			}
			else if (tp instanceof TwoId){
				listeTwoPath_retour.add(`TwoId(genererOneC0(tmp)));
			}
			if(mode==0) newx=listeTwoPath_retour.get(listeTwoPath_retour.size()-1).getX()+newlargeur+4;	
			else newx=listeTwoPath_retour.get(listeTwoPath_retour.size()-1).getX()+listeTwoPath_retour.get(listeTwoPath_retour.size()-1).getLargeur()+4;
		}
		// on genere la tranche
		TwoPath res = genererTwoC0(listeTwoPath_retour,mode);
		System.out.println("res : "+res);
		VarGlobale.ecritLigne("res : "+res);
		
		// en mode 1, il peut y avoir le cas particulier, notamment lorsque la partie a modifier
		// est un TwoC1, il faut alors modifier la tranche basse MAIS aussi conserve le haut
		if(mode==1){
			res = completudemode_1(amodifier,res,0);
		}
		
		System.out.println("res : "+res);
		VarGlobale.ecritLigne("res : "+res);
		return res;
	}
	

	/*
	 * Methode David : Permet d'agrandir la hauteur de l'element cible (path) d'une taille c dans le cadre de la construction d'un TwoC0
	 */
	public static TwoPath allongerFils(TwoPath path ,int c){
		%match (TwoPath path){
			TwoId(onepath) -> {return `TwoId(allongerFils(onepath,c));}
			TwoCell(name,source,target,type,id,x,y,hauteur,largeur) -> { return `TwoCell(name,source,allongerFils(target,c),type,id,x,y,hauteur,largeur); }
			TwoC0(head,tail*) -> {return `TwoC0(allongerFils(head,c),allongerFils(tail*,c));}
			TwoC1(head*,tail) -> {return `TwoC1(head*,allongerFils(tail,c));}
		}
		return path;
	}
	
	public static OnePath allongerFils(OnePath path ,int c){
		%match (OnePath path){
			Id() -> {;}
			OneCell(name,x,y,hauteur,largeur) -> { return `OneCell(name,x,y,hauteur+c,largeur);}
		 	OneC0 (head,tail*) -> {return `OneC0(allongerFils(head,c),allongerFils(tail*,c));}
		}
		return path;
	}
	
	/*
	 * Methode David : Retourne sous forme d'une liste un TwoPath
	 * Permet d'avoir de maniere detourner une liste de OnePath
	 * WARNING : Cette methode ne recupere que le bas d'un TwoC1
	 * 
	 * Exemple si l'element cible est :
	 *         " |      |     | "
	 *	       "----  ----  ----" 
	 *	       "|C1|  |C2|  |C3|" 
	 *	       "----  ----  ----"
	 *		   " |      |     | "
	 *		   " |      |     | "
	 *         "---------   ----"
	 *		   "|   C4  |   |C5|"
	 *	       "---------   ----"
	 *		   "    |         | "
	 * 
	 * On ne recuperera que C4 et C5
	 */
	public static ArrayList<TwoPath> getListeTwoPathBas(TwoPath path){
		ArrayList<TwoPath> liste = new ArrayList<TwoPath>();
		%match (TwoPath path){
			TwoId(onepath) -> { liste.add(path);}
			TwoCell(_,_,_,_,_,_,_,_,_) -> { liste.add(path); }
			TwoC0(head,tail*) -> { liste.addAll(getListeTwoPathBas(`head)); liste.addAll(getListeTwoPathBas(`tail*));}
			TwoC1(head*,tail) -> { liste.addAll(getListeTwoPathBas(`tail));}
		}
		return liste;
	}
	
	/*
	 * Methode David : Retourne sous forme d'une liste un TwoPath
	 * Permet d'avoir de maniere detourner une liste de OnePath
	 */
	public static ArrayList<TwoPath> getListeTwoPath(TwoPath path){
		ArrayList<TwoPath> liste = new ArrayList<TwoPath>();
		%match (TwoPath path){
			TwoId(onepath) -> { liste.add(path);}
			TwoCell(_,_,_,_,_,_,_,_,_) -> { liste.add(path); }
			TwoC0(head,tail*) -> { liste.addAll(getListeTwoPath(`head)); liste.addAll(getListeTwoPath(`tail*));}
			TwoC1(head,tail*) -> { liste.addAll(getListeTwoPath(`head)); liste.addAll(getListeTwoPath(`tail*));}
		}
		return liste;
	}
	
	/*
	 * Methode David : Retourne sous forme d'une liste un TwoPath
	 * Permet d'avoir de maniere detourner une liste de OnePath
	 * ATTENTION : Ne recupere que les parties hautes
	 */
	public static ArrayList<TwoPath> getListeTwoPathHaut(TwoPath path){
		ArrayList<TwoPath> liste = new ArrayList<TwoPath>();
		%match (TwoPath path){
			TwoId(onepath) -> { liste.add(path);}
			TwoCell(_,_,_,_,_,_,_,_,_) -> { liste.add(path); }
			TwoC0(head,tail*) -> { liste.addAll(getListeTwoPathBas(`head)); liste.addAll(getListeTwoPathBas(`tail*));}
			TwoC1(head,tail*) -> { liste.addAll(getListeTwoPathBas(`head));}
		}
		return liste;
	}
	
	/*
	 * Methode David : Recuperer sous forme de liste la source
	 */
	public static ArrayList<OnePath> getListeOnePathSource(TwoPath path){
		ArrayList<OnePath> liste = new ArrayList<OnePath>();
		%match (TwoPath path){
			TwoId(onepath) -> { liste.addAll(getListeOnePath(`onepath));}
			TwoCell(_,source,_,_,_,_,_,_,_) -> { liste.addAll(getListeOnePath(`source)); }
			TwoC0(head,tail*) -> { liste.addAll(getListeOnePathSource(`head)); liste.addAll(getListeOnePathSource(`tail*));}
			TwoC1(head,tail*) -> { liste.addAll(getListeOnePathSource(`head));}
		}
		return liste;
	}
	
	/*
	 * Methode David : Recuperer sous forme de liste le target
	 */
	public static ArrayList<OnePath> getListeOnePathTarget(TwoPath path){
		ArrayList<OnePath> liste = new ArrayList<OnePath>();
		%match (TwoPath path){
			TwoId(onepath) -> { liste.addAll(getListeOnePath(`onepath));}
			TwoCell(_,_,target,_,_,_,_,_,_) -> { liste.addAll(getListeOnePath(`target)); }
			TwoC0(head,tail*) -> { liste.addAll(getListeOnePathTarget(`head)); liste.addAll(getListeOnePathTarget(`tail*));}
			TwoC1(head*,tail) -> { liste.addAll(getListeOnePathTarget(`tail));}
		}
		return liste;
	}
	
	/*
	 * Methode David : Recuperer sous forme de liste les OneCell
	 */
	public static ArrayList<OnePath> getListeOnePath(OnePath path){
		ArrayList<OnePath> liste = new ArrayList<OnePath>();
		%match (OnePath path){
			Id() -> {liste.add(path);}
			OneCell(_,_,_,_,_) -> { liste.add((OneCell)path); }
		 	OneC0 (head,tail*) -> { liste.addAll(getListeOnePath(`head)); liste.addAll(getListeOnePath(`tail*));}
		}
		return liste;
	}
	
	/*
	 * Methode qui repartie les fils de la cellule de maniere constante
	 * A relié a la methode : RepartitionFilsTwoCell
	 */
	public static TwoPath RepartitionFilsTwoPath(TwoPath path){
		%match (TwoPath path){
			TwoId(onepath) -> { return(path);}
			TwoCell(_,_,_,_,_,_,_,_,_) -> { return RepartitionFilsTwoCell((TwoCell)path); }
			TwoC0(head,tail*) -> { return `TwoC0(RepartitionFilsTwoPath(head),RepartitionFilsTwoPath(tail*)) ;}
			TwoC1(head,tail*) -> { return `TwoC1(RepartitionFilsTwoPath(head),RepartitionFilsTwoPath(tail*)) ;}
		}
		return path;
	}
	
	/*
	 * Methode qui repartie les fils de la cellule de maniere constante mais seulement le bas de la cellule
	 * * A relié a la methode : RepartitionFilsTwoCellBas
	 */
	public static TwoPath RepartitionFilsTwoPathBas(TwoPath path){
		%match (TwoPath path){
			TwoId(onepath) -> { return(path);}
			TwoCell(_,_,_,_,_,_,_,_,_) -> { return RepartitionFilsTwoCellBas((TwoCell)path); }
			TwoC0(head,tail*) -> { return `TwoC0(RepartitionFilsTwoPathBas(head),RepartitionFilsTwoPathBas(tail*)) ;}
			TwoC1(head,tail*) -> { return `TwoC1(RepartitionFilsTwoPathBas(head),RepartitionFilsTwoPathBas(tail*)) ;}
		}
		return path;
	}
	
	
	/*
	 * Methode qui repartie les fils de la cellule de maniere constante mais seulement le bas de la cellule
	 */
	public static TwoCell RepartitionFilsTwoCellBas(TwoCell path){
		ArrayList<OnePath> listebas = getListeOnePathTarget(path);
		Iterator it2 = listebas.iterator();
		ArrayList<OnePath> target=new ArrayList<OnePath>();
		int tsize = path.targetsize()+1;
		int i=1;
		int j=1;
		while(it2.hasNext()){
			int espacebas = path.getlargeur()/tsize;
			OnePath oc = (OnePath)it2.next();
			int tmp = j*espacebas; //Calcul ici parce que tom utilise * pour autre chose
			if(oc instanceof OneCell) target.add(`OneCell(oc.getName(),path.getx()+tmp,path.gety()+path.getHauteur()-oc.getHauteur(),oc.gethauteur(),oc.getlargeur()));
			else target.add(`Id());
			j++;
		}
		TwoPath res = `TwoCell(path.getName(),path.source(),genererOneC0(target),path.getType(),path.getID(),path.getx(),path.gety(),path.gethauteur(),path.getlargeur());
		return (TwoCell)res;
	}
	
	/*
	 * Methode qui repartie les fils de la cellule de maniere constante
	 */
	public static TwoCell RepartitionFilsTwoCell(TwoCell path){
		ArrayList<OnePath> listehaut = getListeOnePathSource(path);
		ArrayList<OnePath> listebas = getListeOnePathTarget(path);
		Iterator it = listehaut.iterator();
		Iterator it2 = listebas.iterator();
		ArrayList<OnePath> source=new ArrayList<OnePath>();
		ArrayList<OnePath> target=new ArrayList<OnePath>();
		int ssize = path.sourcesize()+1;
		int tsize = path.targetsize()+1;
		int i=1;
		int j=1;
		while(it.hasNext()){
			int espacehaut = path.getlargeur()/ssize;
			OnePath oc = (OnePath)it.next();
			int tmp = i*espacehaut; //Calcul ici parce que tom utilise * pour autre chose
			if(oc instanceof OneCell) source.add(`OneCell(oc.getName(),path.getx()+tmp,path.gety(),oc.gethauteur(),oc.getlargeur()));
			else source.add(`Id());
			i++;
		}
		while(it2.hasNext()){
			int espacebas = path.getlargeur()/tsize;
			OnePath oc = (OnePath)it2.next();
			int tmp = j*espacebas; //Calcul ici parce que tom utilise * pour autre chose
			if(oc instanceof OneCell) target.add(`OneCell(oc.getName(),path.getx()+tmp,path.gety()+path.getHauteur()-oc.getHauteur(),oc.gethauteur(),oc.getlargeur()));
			else target.add(`Id());
			j++;
		}
		TwoPath res = `TwoCell(path.getName(),genererOneC0(source),genererOneC0(target),path.getType(),path.getID(),path.getx(),path.gety(),path.gethauteur(),path.getlargeur());
		return (TwoCell)res;
	}
	
	/*
	 * Methode David : Methode qui organise et cree les TwoC1
	 * Emploi les methodes : GestionC1, NiveauC1, Completuremode_1
	 * 
	 */
	public static TwoPath gestionTwoC1(TwoPath haut, TwoPath bas){
		int taillehaut = haut.getLargeur();
		int taillebas = bas.getLargeur();
		// si c'est le bas qu'il faut modifier
		if(taillehaut>=taillebas){
			%match (TwoPath bas){
				// cas particulier ou il faut modifier un TwoC1, il faut alors modifier chaque tranche de celui ci
				TwoC1(head,tail*) -> {
					ArrayList<TwoPath> listeniveau = niveauC1(bas); //on recupere alors le haut de la partie basse
					Iterator it = listeniveau.iterator();
					TwoPath tptmp1 = haut;
					//on modifie progressivement depuis le début et on redescent progressivement dans le TwoC1
					while(it.hasNext()){
						TwoPath tptmp2 = (TwoPath)it.next();
						tptmp1 = `TwoC1(tptmp1,decalageY(gestionC1(tptmp2,tptmp1,0),haut.getHauteur()));
						tptmp1 = RepartitionFilsTwoPathBas(tptmp1);
					}
					return tptmp1;
				}
			}
			bas=gestionC1(bas, haut, 0);
		}else{
			haut=gestionC1(haut,bas,1);
		}
		bas=decalageY(bas,haut.getHauteur());
		return `TwoC1(haut,bas);
	}
	
	/*
	 * Methode qui s'occupe d'organiser et de creer des TwoC0 (utilise la methode allongeFils)
	 */
	public static TwoPath gestionTwoC0(TwoPath gauche, TwoPath droite){	
		int taillegauche = gauche.getHauteur();
		int tailledroite = droite.getHauteur();
		if(taillegauche>=tailledroite){
			droite=allongerFils(droite,gauche.getHauteur()-droite.getHauteur());
		}else{
			gauche=allongerFils(gauche,droite.getHauteur()-gauche.getHauteur());
		}
		droite = decalageX(droite,gauche.getLargeur()+4);
		return `TwoC0(gauche,droite);
	}
	
	public static OnePath gestionOneC0(OnePath gauche, OnePath droite){
		int taillegauche = gauche.getHauteur();
		int tailledroite = droite.getHauteur();
		if(taillegauche>=tailledroite){
			droite=allongerFils(droite,gauche.getHauteur()-droite.getHauteur());
		}else{
			gauche=allongerFils(gauche,droite.getHauteur()-gauche.getHauteur());
		}
		droite = decalageX(droite,gauche.getLargeur()+4);
		System.out.println("gestionOneC0 : "+gauche+" "+droite);
		VarGlobale.ecritLigne("gestionOneC0 : "+gauche+" "+droite);
		return `OneC0(gauche,droite);
	}
	
	
	
	/*
	 * Methode David : Permet de generer depuis une liste un TwoC0 
	 * Dans le but d'être compatible avec une construction classique de TwoPath
	 */
	public static TwoPath genererTwoC0(ArrayList<TwoPath> array,int mode){
		if(array.size()==1) return array.get(0);
		else {
			TwoPath first = array.get(0);
			array.remove(0);
			TwoPath last = genererTwoC0(array,mode);
			//if(last instanceof TwoCell) last=`TwoCell(last.getName(),last.source(),last.target(),last.getType(),last.getID(),first.getX()+first.getLargeur()+4,last.gety(),last.gethauteur(),last.getlargeur());					
			//else if(last instanceof TwoId) last=`TwoId(last.source()); //decalageX(last.source(),last.source().getX()-first.getLargeur()+4)
			return `TwoC0(first,last);
		}
	}
	
	public static OnePath genererOneC0(ArrayList<OnePath> array){
		if(array.size()==0) return `Id();
		else if(array.size()==1) return array.get(0);
		else {
			OnePath first = array.get(0);
			array.remove(0);
			OnePath last = genererOneC0(array);
			return `OneC0(first,last);
		}
	}
}