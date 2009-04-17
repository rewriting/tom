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
		  return `OneCell(name,0,0,20,1);
	  }
	  if(nodeName.equals("OneC0")){
		  NodeList oneC0s=node.getChildNodes();
		  OnePath res=`Id();
		  for (int j = oneC0s.getLength()-1; j > 0; j--) {
			  Node oneC0Element = oneC0s.item(j);
			  if(!oneC0Element.getNodeName().contains("#text")){
				  res=`OneC0(makeOnePath(oneC0Element),res);
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
			  if(!nodeChild.getNodeName().equals("#text")){return `TwoId(makeOnePath(nodeChild));}
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
		  TwoPath res = `TwoCell(name,source,target,celltype,0,0,0,90,100);
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
				  /*System.out.println("TwoC0 :(");
				  System.out.print(" res2:");res2.affiche();
				  System.out.print(" res3:");res3.affiche();
				  System.out.println(")");*/
				  if(res2 instanceof TwoId) ;
				  else res2=decalageX(res2,res3.getLargeur()+4);
				  res=`TwoC0(res2,res3);
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
					  /*System.out.println("TwoC1 :(");
					  System.out.print(" res2:");res2.affiche();
					  System.out.print(" res3:");res3.affiche();
					  System.out.println(")");*/
					  if(res2 instanceof TwoId){
						  res=`TwoC1(res3,res2);
					  }
					  else{
						  
						  res= gestionTwoC1(res3,res2);
					  }
						
					  
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
			TwoCell(name,source,target,type,id,x,y,hauteur,largeur) -> { return "<TwoPath>\n<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n</TwoPath>\n"; }
			TwoC0(head,tail*) -> {return "<TwoPath>\n<TwoC0>\n"+twoC02XML(`head)+twoC02XML(`tail)+"</TwoC0>\n</TwoPath>\n";}
			TwoC1(head,tail*) -> {return "<TwoPath>\n<TwoC1>\n"+twoC12XML(`head)+twoC12XML(`tail)+"</TwoC1>\n</TwoPath>\n";}
		}
		return "";
	}
	
	/*public static Structure twoPath2Graph(TwoPath path){
		%match (TwoPath path){
			TwoId(onepath) -> {return null;}
			TwoCell(name,source,target,type,id) -> { return new DeuxCellules(name,twoPath2Graph(`source.sourcesize()),twoPath2Graph(`target.targetsize())); }
			TwoC0(head,tail*) -> {return new Composition0(`head,`tail);}
			TwoC1(head,tail*) -> {return new Composition1(`head,`tail);}
		}
		return null;
	}*/
	
	//sub-function of the previous one to handle twoC0s
	public static String twoC02XML(TwoPath path){
	%match (TwoPath path){
					TwoId(onepath) -> {return "<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n";}
					TwoCell(name,source,target,type,id,x,y,hauteur,largeur) -> { return "<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n"; }
					TwoC0(head,tail*) -> {return twoC02XML(`head)+twoC02XML(`tail);}
					TwoC1(head,tail*) -> {return "<TwoC1>\n"+twoC12XML(`head)+twoC12XML(`tail)+"</TwoC1>\n";}
		}
	return "";
	}
	//the same for twoC1s
	public static String twoC12XML(TwoPath path){
		%match (TwoPath path){
			TwoId(onepath) -> {return "<TwoId>\n"+onePath2XML(`onepath)+"</TwoId>\n";}
			TwoCell(name,source,target,type,id,x,y,hauteur,largeur) -> { return "<TwoCell Name=\""+`name+"\" Type=\""+`type.toString().replace("()","")+"\">\n<Source>\n"+onePath2XML(`source)+"</Source>\n<Target>\n"+onePath2XML(`target)+"</Target>\n</TwoCell>\n"; }
			TwoC0(head,tail*) -> {return "<TwoC0>\n"+twoC02XML(`head)+twoC02XML(`tail)+"</TwoC0>\n";}
			TwoC1(head,tail*) -> {return twoC12XML(`head)+twoC12XML(`tail);}
		}
		return "";
	}
	//same idea with 1-paths
	public static String onePath2XML(OnePath path){
		%match (OnePath path){
			Id() -> {return "<OnePath>\n<Id></Id>\n</OnePath>\n";}
			OneCell(name,x,y,hauteur,largeur) -> { return "<OnePath>\n<OneCell Name=\""+`name+"\"></OneCell>\n</OnePath>\n"; }
			OneC0(head,tail*)->{ return "<OnePath>\n<OneC0>\n"+oneC02XML(`head)+oneC02XML(`tail)+"</OneC0>\n</OnePath>\n";}
		}
		return "";
	}
	//and for OneC0s
	public static String oneC02XML(OnePath path){
		%match (OnePath path){
			Id() -> {return "<Id></Id>\n";}
			OneCell(name,x,y,hauteur,largeur) -> { return "<OneCell Name=\""+`name+"\"></OneCell>\n"; }
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
			TwoId(onepath) -> {return path;}
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
			TwoId(onepath) -> {return path;}
			TwoCell(name,source,target,type,id,x,y,hauteur,largeur) -> { return `TwoCell(name,decalageX(source,c),decalageX(target,c),type,id,x+c,y,hauteur,largeur); }
			TwoC0(head,tail*) -> {return `TwoC0(decalageX(head,c),decalageX(tail*,c));}
			TwoC1(head,tail*) -> {return `TwoC1(decalageX(head,c),decalageX(tail*,c));}
		}
		return path;
	}
	
	/*
	 * Methode David : Permet d'agrandir la largeur de l'element cible (path) d'une taille c
	 */
	public static TwoPath setLargeur(TwoPath path ,int c){
		%match (TwoPath path){
			TwoId(onepath) -> {return path;}
			TwoCell(name,source,target,type,id,x,y,hauteur,largeur) -> { return `TwoCell(name,source,target,type,id,x,y,hauteur,c); }
			TwoC0(head,tail*) -> {return `TwoC0(decalageX(head,c),decalageX(tail*,c));}
			TwoC1(head,tail*) -> {return `TwoC1(decalageX(head,c),decalageX(tail*,c));}
		}
		return path;
	}
	
	/*
	 * Methode David : Permet d'agrandir la hauteur de l'element cible (path) d'une taille c
	 */
	public static TwoPath setHauteur(TwoPath path ,int c){
		%match (TwoPath path){
			TwoId(onepath) -> {return path;}
			TwoCell(name,source,target,type,id,x,y,hauteur,largeur) -> { return `TwoCell(name,source,target,type,id,x,y,c,largeur); }
			TwoC0(head,tail*) -> {return `TwoC0(decalageX(head,c),decalageX(tail*,c));}
			TwoC1(head,tail*) -> {return `TwoC1(decalageX(head,c),decalageX(tail*,c));}
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
			TwoCell(name,source,target,type,id,x,y,hauteur,largeur) -> { liste.add(path); }
			TwoC0(head,tail*) -> { liste.addAll(getListeTwoPathBas(`head)); liste.addAll(getListeTwoPathBas(`tail*));}
			TwoC1(head*,tail) -> { liste.addAll(getListeTwoPathBas(`tail));}
		}
		return liste;
	}
	
	/*
	 * Methode David : Retourne sous forme d'une liste un TwoPath
	 * Permet d'avoir de maniere detourner une liste de OnePath
	 * WARNING : Cette methode ne recupere que le haut d'un TwoC1
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
	 * On ne recuperera que C1, C2 et C3
	 */
	public static ArrayList<TwoPath> getListeTwoPathHaut(TwoPath path){
		ArrayList<TwoPath> liste = new ArrayList<TwoPath>();
		%match (TwoPath path){
			TwoId(onepath) -> { liste.add(path);}
			TwoCell(name,source,target,type,id,x,y,hauteur,largeur) -> { liste.add(path); }
			TwoC0(head,tail*) -> { liste.addAll(getListeTwoPathHaut(`head)); liste.addAll(getListeTwoPathHaut(`tail*));}
			TwoC1(head,tail*) -> { liste.addAll(getListeTwoPathHaut(`head));}
		}
		return liste;
	}
	
	/*
	 * Methode David : Recuperer sous forme de liste la source
	 */
	public static ArrayList<OneCell> getListeOneCellSource(TwoPath path){
		ArrayList<OneCell> liste = new ArrayList<OneCell>();
		%match (TwoPath path){
			TwoId(onepath) -> { liste.addAll(getListeOneCell(`onepath));}
			TwoCell(name,source,target,type,id,x,y,hauteur,largeur) -> { liste.addAll(getListeOneCell(`source)); }
			TwoC0(head,tail*) -> { liste.addAll(getListeOneCellTarget(`head)); liste.addAll(getListeOneCellTarget(`tail*));}
			TwoC1(head,tail*) -> { liste.addAll(getListeOneCellTarget(`head));}
		}
		return liste;
	}
	
	/*
	 * Methode David : Recuperer sous forme de liste le target
	 */
	public static ArrayList<OneCell> getListeOneCellTarget(TwoPath path){
		ArrayList<OneCell> liste = new ArrayList<OneCell>();
		%match (TwoPath path){
			TwoId(onepath) -> { liste.addAll(getListeOneCell(`onepath));}
			TwoCell(name,source,target,type,id,x,y,hauteur,largeur) -> { liste.addAll(getListeOneCell(`target)); }
			TwoC0(head,tail*) -> { liste.addAll(getListeOneCellTarget(`head)); liste.addAll(getListeOneCellTarget(`tail*));}
			TwoC1(head*,tail) -> { liste.addAll(getListeOneCellTarget(`tail));}
		}
		return liste;
	}
	
	/*
	 * Methode David : Recuperer sous forme de liste les OneCell
	 */
	public static ArrayList<OneCell> getListeOneCell(OnePath path){
		ArrayList<OneCell> liste = new ArrayList<OneCell>();
		%match (OnePath path){
			Id() -> {;}
			OneCell(name,x,y,hauteur,largeur) -> { liste.add((OneCell)path); }
		 	OneC0 (head,tail*) -> {liste.addAll(getListeOneCell(`head)); liste.addAll(getListeOneCell(`tail*));}
		}
		return liste;
	}
	
	public static TwoPath RepartitionFilsTwoPath(TwoPath path){
		%match (TwoPath path){
			TwoId(onepath) -> { return(path);}
			TwoCell(name,source,target,type,id,x,y,hauteur,largeur) -> { return RepartitionFilsTwoCell((TwoCell)path); }
			TwoC0(head,tail*) -> { return `TwoC0(RepartitionFilsTwoPath(head),RepartitionFilsTwoPath(tail*)) ;}
			TwoC1(head,tail*) -> { return `TwoC1(RepartitionFilsTwoPath(head),RepartitionFilsTwoPath(tail*)) ;}
		}
		return path;
	}
	
	public static TwoCell RepartitionFilsTwoCell(TwoCell path){
		ArrayList<OneCell> listehaut = getListeOneCellSource(path);
		ArrayList<OneCell> listebas = getListeOneCellTarget(path);
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
			OneCell oc = (OneCell)it.next();
			int tmp = i*espacehaut; //Calcul ici parce que tom utilise * pour autre chose
			source.add(`OneCell(oc.getName(),path.getx()+tmp,path.gety(),oc.gethauteur(),oc.getlargeur()));
			i++;
		}
		while(it2.hasNext()){
			int espacebas = path.getlargeur()/tsize;
			OneCell oc = (OneCell)it2.next();
			int tmp = j*espacebas; //Calcul ici parce que tom utilise * pour autre chose
			target.add(`OneCell(oc.getName(),path.getx()+tmp,path.gety()+path.getHauteur()-20,oc.gethauteur(),oc.getlargeur()));
			j++;
		}
		TwoPath res = `TwoCell(path.getName(),genererOneC0(source),genererOneC0(target),path.getType(),path.getID(),path.getx(),path.gety(),path.gethauteur(),path.getlargeur());
		return (TwoCell)res;
	}
	
	/*
	 * Methode David : Methode qui organise les TwoC1
	 * Ici, on cherche a donner une taille pour les elements du bas en fonction
	 * des OnePath du niveau
	 */
	public static TwoPath gestionTwoC1(TwoPath haut, TwoPath bas){
		//Init, ie recuperer la liste des elements de chaque TwoPath
		ArrayList<TwoPath> listehaut = new ArrayList<TwoPath>();
		ArrayList<TwoPath> listebas = new ArrayList<TwoPath>();
		System.out.println(haut);
		System.out.println();
		System.out.println(bas);
		if(haut instanceof TwoC0) listehaut=getListeTwoPathBas(haut.reverse()); //on recupere que les elements bas du haut du C1
		else listehaut=getListeTwoPathBas(haut);
		if(bas instanceof TwoC0) listebas=getListeTwoPathHaut(bas.reverse()); //on recupere que les elements haut du bas du C1
		else listebas=getListeTwoPathHaut(bas);
		int taille; //variable qui permettra de connaitre la largeur de chaque sous element bas
		Iterator it = listehaut.iterator();
		Iterator it2 = listebas.iterator();
		ArrayList<TwoPath> listeC1bas=new ArrayList<TwoPath>();
		TwoPath tp1=null;
		int a=0; // nb OnePath d'en haut
		int b=0; // nb OnePath d'en bas
		int taillemax=-1;
		while(it.hasNext() && it2.hasNext()){
			
			
			// 1er tour : On recupere les valeurs des elements
			// Autre tour : CF PLUS TARD (non implementer)
			if(a<=b) tp1 = (TwoPath)it.next();
			if(a<=b) a = tp1.targetsize();
			else a=a-b;
			
			TwoPath tp2 = (TwoPath)it2.next();
			b = tp2.sourcesize();
			
			if(!it2.hasNext()) taillemax+=haut.getLargeur();
			
			ArrayList<OneCell> listeoc1 = getListeOneCellTarget(tp1); 
			ArrayList<OneCell> listeoc2 = getListeOneCellSource(tp2); 
			System.out.println(listeoc1+" "+listeoc2);
			ArrayList<OnePath> listeocret = new ArrayList<OnePath>();
			Iterator itoc1 = listeoc1.iterator();
			Iterator itoc2 = listeoc2.iterator();
			taille=tp2.getLargeur();  //val par def = 100;
			if(a>=b){
				//Cas : Si on a plus de OneC0 qu'il n'en faut pour l'element d'en bas
				
				int tmp=b;
				while(itoc1.hasNext() && itoc2.hasNext() && tmp!=0){
					OneCell c1 = (OneCell)itoc1.next();
					OneCell c2 = (OneCell)itoc2.next();
					listeocret.add(`OneCell(c2.getName(),c1.getx(),c2.gety(),c2.gethauteur(),c2.getlargeur()));			
					tmp--;
				}
				
			}else if(a<b){
				// CAS : Si on a pas assez de OneC0, changer de sous element dans la liste d'en haut
				int tmp=a;
				while(itoc1.hasNext() && itoc2.hasNext() && tmp!=0){
					
					OneCell c1 = (OneCell)itoc1.next();
					OneCell c2 = (OneCell)itoc2.next();
					listeocret.add(`OneCell(c2.getName(),c1.getx(),c2.gety(),c2.gethauteur(),c2.getlargeur()));
					tmp--;
				}
				while(a<b && it.hasNext()){
					tp1 = (TwoPath)it.next();
					a += tp1.targetsize(); //ajouter la largeur du sous element
					listeoc1 = getListeOneCellTarget(tp1);
					itoc1 = listeoc1.iterator();
					tmp=b-a+1;
					while(itoc1.hasNext() && itoc2.hasNext() && tmp!=0){
						OneCell c1 = (OneCell)itoc1.next();
						OneCell c2 = (OneCell)itoc2.next();
						listeocret.add(`OneCell(c2.getName(),c1.getx(),c2.gety(),c2.gethauteur(),c2.getlargeur()));
						tmp--;
						if(!itoc2.hasNext()) taille=c1.getx()+4;
					}
				}
			}
			if(it2.hasNext()) taillemax-=taille;
			else{
				System.out.println(tp2);
				taille=taillemax-4;
			}
			if(tp2 instanceof TwoCell) listeC1bas.add(`TwoCell(tp2.getName(),genererOneC0(listeocret),tp2.target(),tp2.getType(),tp2.getID(),tp2.getX(),tp2.getY(),tp2.getHauteur(),taille));
			else listeC1bas.add(`TwoId(genererOneC0(listeocret)));
		}
		
		TwoPath tmp=genererTwoC0(listeC1bas);
		tmp = decalageY(tmp,haut.getHauteur());
		
		return `TwoC1(haut,tmp);
	}
	
	/*
	 * Methode David : Permet de generer depuis une liste un TwoC0 
	 * Dans le but d'être compatible avec une construction classique de TwoPath
	 */
	public static TwoPath genererTwoC0(ArrayList<TwoPath> array){
		if(array.size()==1) return array.get(0);
		else {
			TwoPath first = array.get(0);
			array.remove(0);
			TwoPath last = genererTwoC0(array);
			if(last instanceof TwoCell) last=`TwoCell(last.getName(),last.source(),decalageX(last.target(),first.getLargeur()+4),last.getType(),last.getID(),first.getLargeur()+4,last.gety(),last.gethauteur(),last.getlargeur());
			else last=`TwoId(decalageX(last.source(),first.getLargeur()+4));
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