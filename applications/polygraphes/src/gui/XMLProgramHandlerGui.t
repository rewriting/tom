package gui;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import adt.polygraphicprogramgui.types.*;
import adt.polygraphicprogramgui.types.onepath.*;
import adt.polygraphicprogramgui.types.twopath.*;
import tom.library.sl.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

//constructs the specific part of each program with strategies corresponding to rule and the function that tries them all
public class XMLProgramHandlerGui {
  %include { ../adt/polygraphicprogramgui/PolygraphicProgramgui.tom }
  %include { sl.tom }
  
  	//quite long but core of this part
	public static String makeRuleStrategy(String filename){
		
		JFrame frame = new JFrame();
		frame.setTitle("Polygraphes GUI Alpha 2");
		frame.setSize(500,500);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		//tp=XMLhandlerGui.RepartitionFilsTwoPath(tp);
		Container contentPane = frame.getContentPane();
		JTabbedPane jtp = new JTabbedPane();
		contentPane.add(jtp);
		Graph g = new Graph();
		
		
		String strategy="";
		int n=0;
		ArrayList<OneCell> types=new ArrayList<OneCell>();
		ArrayList<TwoPath> constructors=new ArrayList<TwoPath>();
		ArrayList<ThreePath> structureRules=new ArrayList<ThreePath>();
		try{
			//we load the xml file
			Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename);
			Element e = dom.getDocumentElement();
			NodeList childs=e.getChildNodes();
			for (int i = 0; i < childs.getLength(); i++) {
				Node child = childs.item(i);
				//first we get the types and the corresponding constructors and store them in the collections we instantiated in the beginning
				if(child.getNodeName().equals("Type")){
					NodeList typeNodes=child.getChildNodes();
					for (int j = 0; j < typeNodes.getLength(); j++) {
						Node typeNode = typeNodes.item(j);
						if(typeNode.getNodeName().equals("OnePath")){
							types.add((OneCell)XMLhandlerGui.makeOnePath(typeNode));
						}
						if(typeNode.getNodeName().equals("Constructor")){
							NodeList constructorNodes=typeNode.getChildNodes();
							for (int k = 0; k < constructorNodes.getLength(); k++) {
								Node constructorNode = constructorNodes.item(k);
								if(!constructorNode.getNodeName().equals("#text")){
									TwoPath tp = XMLhandlerGui.makeTwoPath(constructorNode);
									constructors.add(tp);
									/*System.out.println("******* CIRCUIT *******");
									System.out.println("");
									System.out.println(" |      |     | \n" +
											           "----  ----  ----\n" +
											           "|C1|  |C2|  |C3|\n" +
											           "----  ----  ----\n" +
											           " |      |     | \n"+
											           " |      |     | \n"+
											           "---------   ----\n" +
											           "|   C4  |   |C5|\n" +
											           "---------   ----\n" +
											           "    |        |  \n");
									
									System.out.println("********** INFORMATION **********");
									System.out.println("LARGEUR ESTIME DU CIRCUIT : (100+4+100+4+100)=308");
									System.out.println("HAUTEUR ESTIME DU CIRCUIT : (90+4+90)=184");
									System.out.println("********** REEL **********");
									System.out.println("LARGEUR DU CIRCUIT : "+tp.getLargeur());
									System.out.println("HAUTEUR DU CIRCUIT : "+tp.getHauteur());
									System.out.println("********** GENERAL STATUS **********");
									tp.affiche();
									System.out.println("********** EOF **********");
									*/
								    
									ajoutfenetre(g,tp);
									jtp.addTab("Constructor N°"+i, g);
									g= new Graph();
									//System.out.println("CONTROLE NB ELEMENT :"+g.getComponentCount());
									frame.setVisible(true);
									
									
								}
							}
						}
					}
				}
				//then we get the rules and construct the corresponding strategies
				if(child.getNodeName().equals("Function")){
					NodeList functionNodes=child.getChildNodes();
					for (int j = 0; j < functionNodes.getLength(); j++) {
						Node functionNode = functionNodes.item(j);
						if(functionNode.getNodeName().equals("Rule")){
							NodeList ruleNodes=functionNode.getChildNodes();
							for (int k = 0; k < ruleNodes.getLength(); k++) {
								Node ruleNode = ruleNodes.item(k);
								if(!ruleNode.getNodeName().equals("#text")){
									ThreePath tp = XMLhandlerGui.makeThreeCell(ruleNode);
									
									JTabbedPane jtp2 = new JTabbedPane();
									jtp.addTab("Function N°"+i,jtp2);
									ajoutfenetre(g,tp.source());
									jtp2.addTab("Source", g);
									g= new Graph();
									ajoutfenetre(g,tp.target());
									jtp2.addTab("Target", g);
									g= new Graph();
									frame.setVisible(true);
									String rule=makeRule(tp)+"\n";
									
									
			 strategy+=%[	%strategy ApplyRules@n@() extends Identity(){ 
		visit TwoPath {
			@rule@
		}
	}
  	]%;
			 n++;
								}
							}
						}
					}
				}
			}
			
			// generation of the structure rules
			for (Iterator<TwoPath> iterator = constructors.iterator(); iterator.hasNext();) {
				TwoPath constructor =  iterator.next();
				ArrayList<ThreePath> constructorRules=StructureRuleHandlergui.makeStructureRules(constructor,types);
				for (Iterator<ThreePath> iterator2 = constructorRules.iterator(); iterator2.hasNext();) {
					ThreePath rule = iterator2.next();
					structureRules.add(rule);
				}
			}
			//construction of the strategies corresponding to the strategy rules
			for (Iterator<ThreePath> iterator = structureRules.iterator(); iterator.hasNext();) {
				ThreePath rulePath =  iterator.next();
				String rule=makeRule(rulePath)+"\n";
			 strategy+=%[	%strategy ApplyRules@n@() extends Identity(){ 
		visit TwoPath {
			@rule@
		}
	}
  	]%;
			 n++;
			}
			//construction of the function that applies all the strategies on a 2-Path
			String evalStrategy="ApplyRules0()";
			//String evalStrategy="RepeatId(TopDown(ApplyRules0())),MakeLog()";
			for(int i=1;i<n;i++){
				//adds each rule strategy in the global visit
				evalStrategy+=",ApplyRules"+i+"()";
				//evalStrategy+=",TopDown(ApplyRules"+i+"()),MakeLog()";
			}
			strategy+=%[	public static TwoPath eval(TwoPath myPath){
		try{
			System.out.println("BEFORE");
			myPath.print();
			//System.out.println("LOG");
			//double initTime= System.currentTimeMillis();
			myPath=(TwoPath) `InnermostId(ChoiceId(Gravity(),Normalize(),@evalStrategy@)).visitLight(myPath);
			//myPath=(TwoPath) `RepeatId(Sequence(RepeatId(TopDown(Gravity())),RepeatId(TopDown(Normalize())),RepeatId(Sequence(@evalStrategy@,Print())))).visitLight(myPath);
			//myPath=(TwoPath) `RepeatId(Sequence(RepeatId(TopDown(Gravity())),RepeatId(TopDown(Normalize())),RepeatId(Sequence(@evalStrategy@)))).visit(myPath);
			System.out.println("\nRESULT");
			myPath.print();
			//System.out.println("Time : " +(System.currentTimeMillis()-initTime)/1000+" s");
			return myPath;
		}
		catch(VisitFailure e) {
			throw new RuntimeException("strange term: " + myPath);
			}
			}
			]%;
		}
		catch(Exception e){ e.printStackTrace();}
		System.out.println("");
		
		
		//we return all the rule strategies and also the computation functon "eval(TwoPath)"
		return strategy;
		
	}
	
	public static void ajoutfenetre(Graph g,TwoPath path){
		%match (TwoPath path){
			TwoId(onepath) -> {;}
			TwoCell(name,source,target,type,id,x,y,hauteur,largeur) -> { g.ajouterElement(path); }
			TwoC0(head,tail*) -> {ajoutfenetre(g,`head); ajoutfenetre(g,`tail);}
			TwoC1(head,tail*) -> {ajoutfenetre(g,`head); ajoutfenetre(g,`tail);}
		}
	}
	
	//transforms the rule source in a pattern and its target as the resulting action
	public static String makeRule(ThreePath rule){
		TwoPath source=rule.getSource();
		TwoPath target=rule.getTarget();
		source=formatRule(source);
		target=formatRule(target);
		String sourceString=source.toString();
		String targetString=target.toString();
		int i=0;
		//we replace the ruleAux Cells by "Xi" in the patterns and actions
		while(sourceString.contains("ruleAux")){//cautious, infinite loop risk there
			char indexSource=sourceString.charAt(sourceString.indexOf("ruleAux")+7);
			char indexTarget=targetString.charAt(targetString.indexOf("ruleAux")+7);
			sourceString=sourceString.replaceFirst("TwoCell\\(\"ruleAux[^\"]\",[^,]+,[^,]+,[^\\)]+\\),0\\)", "X"+indexSource+"*");
			targetString=targetString.replaceFirst("TwoCell\\(\"ruleAux[^\"]\",[^,]+,[^,]+,[^\\)]+\\),0\\)", "X"+indexTarget+"*");
			i++;}
		int j=0;
		//we replace id=0 in the constructors of the action part by a call to a function that will return a new different id to each new TwoCell
		while(sourceString.contains("(),0")){
			sourceString=sourceString.replaceFirst("\\(\\),0", "(),id"+j++);}
		targetString=targetString.replaceAll("\\(\\),0", "(),setID()");
		//we add a condition to verify that each connection Xi is not empty
		String condition="";
		if(i>0){
			i--;
			condition="if(`X"+i+"!=`TwoId(Id())";
			while(i>0){
				i--;
				condition+="&&`X"+i+"!=`TwoId(Id())";
			}
			condition+=")";
		}
		//we add Y* at the tail of the patterns, otherwise we dont capture every pattern
		if(source.isConsTwoC1()){
			sourceString=sourceString.subSequence(0, sourceString.length()-1)+",Y*)";}
		else{
			sourceString="TwoC1("+sourceString+",Y*)";}
		if(target.isConsTwoC1()){
			targetString=targetString.subSequence(0, targetString.length()-1)+",Y*)";}
		else{
			targetString="TwoC1("+targetString+",Y*)";}
		return sourceString+ "-> {"+condition+"return `"+targetString+";}";
	}
	
	//Adds a "ruleAux" cell on each incoming wire, allows to know the input connection structure for the rule pattern and actions
	public static TwoPath formatRule(TwoPath path){
		try{
			TwoPath source=`TwoId(path.source());
			TwoPath ruleAux=(TwoPath) `RepeatId(TopDown(ruleAux(0))).visitLight(source);
			path=`TwoC1(ruleAux,path);
			//we normalize the rule also
			path=(TwoPath) `RepeatId(Sequence(TopDown(Normalize()),TopDown(Gravity()))).visitLight(path);
		}
		catch(VisitFailure e) {
			throw new RuntimeException("strange term: "+path);
		}
		return path;
	}

	//adds the ruleAux cells
	%strategy ruleAux(int i) extends Identity(){ 
		visit TwoPath {
			TwoId(OneC0(head,tail*)) -> { System.out.print(".");return `TwoC0(TwoId(head),TwoId(tail*));}
			TwoId(OneCell(name,x,y,hauteur,largeur)) -> {return `TwoCell("ruleAux"+(i++),Id(),OneCell(name,x,y,hauteur,largeur),Constructor(),0,0,0,90,100);} 
		} 
	}


	//returns the program name from the xml file root tag name field
	public static String getProgramName(String filename){
		String name="polygraphicprogram";
		try{		
			Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename);
			Element e = dom.getDocumentElement();
			name=e.getAttribute("Name");
		}
		catch(Exception e){ e.printStackTrace();}
		return name;
	}	

  //this normalize strategy is slightly different from the one we use in the rest of the project
  %strategy Normalize() extends Identity(){ 
	  visit TwoPath {
  		TwoC1(TwoC0(head1*,TwoC1(top*),tail1*),TwoC0(head2*,bottom*,tail2*)) -> {if(`head1*.target()==`head2*.source()&&`top*.target()==`bottom*.source()){System.out.print(".");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top*,bottom*),TwoC1(tail1*,tail2*));}} 
  		TwoC1(TwoC0(head1*,top@TwoCell(_,_,_,_,_,_,_,_,_),tail1*),TwoC0(head2*,bottom*,tail2*))-> {if(`head1*.target()==`head2*.source()&&`top.target()==`bottom*.source()){System.out.print(".");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top,bottom*),TwoC1(tail1*,tail2*));}} 
  	  	TwoC1(TwoC0(head1*,top@TwoId(_),tail1*),TwoC0(head2*,bottom*,tail2*))-> {if(`head1*.target()==`head2*.source()&&`top.target()==`bottom*.source()){System.out.print(".");return `TwoC0(TwoC1(head1*,head2*),TwoC1(top,bottom*),TwoC1(tail1*,tail2*));}} 
  	  	TwoC1(TwoC0(head*,TwoC1(top*),tail*),bottom*) -> {if(`top*.target()==`bottom*.source()){System.out.print(".");return `TwoC0(head*,TwoC1(top*,bottom*),tail*);}} 
  	  	TwoC1(TwoC0(head*,top@TwoCell(_,_,_,_,_,_,_,_,_),tail*),bottom*) -> {if(`top.target()==`bottom*.source()){System.out.print(".");return `TwoC0(head*,TwoC1(top,bottom*),tail*);}} 
  	  	TwoC1(TwoC0(head*,top@TwoId(_),tail*),bottom*) -> {if(`top.target()==`bottom*.source()){System.out.print(".");return `TwoC0(head*,TwoC1(top,bottom*),tail*);}} 
  	  	TwoC1(head*,TwoC0(topleft*,top*,topright*),TwoC0(left*,f@TwoCell(_,_,_,Function(),_,_,_,_,_),right*),tail*) -> {
  	  		if(`topleft*.target()==`left*.source()&&`top.target()==`f.source()){
  	  			TwoPath myNewPath=`TwoId(Id());
  	  			if(`head*!=`TwoId(Id())){myNewPath= `TwoC1(head*,TwoC0(TwoC1(topleft*,left*),TwoC1(top*,f),TwoC1(topright*,right*)),tail*);}
  	  			else{myNewPath= `TwoC1(TwoC0(TwoC1(topleft*,left*),TwoC1(top*,f),TwoC1(topright*,right*)),tail*);}
  	  			if(myNewPath!=`TwoId(Id())){
  	  				System.out.print(".");
  	  				return myNewPath;
  	  			}
  	  		}
  	  	}
  	  	//the following pattern is used only to normalize rules, we dont use it in the compiled program version
 	  	TwoC1(head*,TwoC0(topleft*,top*,topright*),TwoC0(left*,TwoC1(topf*,f@TwoCell(_,_,_,_,_,_,_,_,_),right*)),tail*) -> {
 	  		if(`topleft*.target()==`left*.source()&&`top.target()==`topf.source()){
  	  			TwoPath myNewPath=`TwoId(Id());
  	  			if(`head*!=`TwoId(Id())){myNewPath= `TwoC1(head*,TwoC0(TwoC1(topleft*,left*),TwoC1(top*,topf*,f),TwoC1(topright*,right*)),tail*);}
  	  			else{myNewPath= `TwoC1(TwoC0(TwoC1(topleft*,left*),TwoC1(top*,topf*,f),TwoC1(topright*,right*)),tail*);}
  	  			if(myNewPath!=`TwoId(Id())){
  	  				System.out.print(".");
  	  				return myNewPath;
  	  			}
  	  		}
  	  	}
  	  	TwoC1(head*,top,TwoC0(left*,X,right*),tail*) -> {if(`left*.source()==`Id()&&`right*.source()==`Id()&&`X.source()==`top.target()){	 
  	  		TwoPath myNewPath=`TwoId(Id());
  	  		if(`head*!=`TwoId(Id())){myNewPath=`TwoC1(head*,TwoC0(left*,TwoC1(top,X),right*),tail*);}else{myNewPath=`TwoC1(TwoC0(left*,TwoC1(top,X),right*),tail*);}
  	  			if(myNewPath!=`TwoId(Id())){
  	  				System.out.print(".");
  	  				return myNewPath;}
  	  		}
  	  	}
  	  	p@TwoC1(head*,top@TwoC0(X*),down@TwoC0(Y*),f@TwoCell(_,_,_,Function(),_,_,_,_,_),tail*) -> {
  	  		int sourcelength=`f.sourcesize();
  	  		TwoPath myNewPath=`TwoId(Id());
  	  		int index=0;
  	  		if(sourcelength!=`down.length()){break;}
  	  			TwoPath[] topArray=toArray((TwoC0)`top);
  	  			TwoPath[] downArray=toArray((TwoC0)`down);
  	  				for(int i=0;i<sourcelength;i++){
  	  					int downsourcelength=downArray[i].sourcesize();
  	  					TwoPath topPart=`TwoId(Id());
  	  						for(int j=index;j<downsourcelength+index;j++){
  	  							try{TwoPath newC0 = (TwoPath)topArray[j];
  	  								if(j==index){topPart=newC0;}
  	  								else {topPart=`TwoC0(topPart,newC0);}
  	  							}catch (Exception e){//case with only constructors at the source, with a duplication cell for instance, then we do not do anything hence the following return
  	  								return `p;
  	  							}
  	  						}
  	  						index+=downsourcelength;
  	  						if(topPart.target()==downArray[i].source()){
  	  							TwoPath newC1=`TwoC1(topPart,downArray[i]);
  	  							if(i==0){myNewPath=`newC1;}
  	  							else {myNewPath=`TwoC0(myNewPath,newC1);}
  	  						}  	  			
  	  				}
  	  				if(myNewPath!=`TwoId(Id())){
  	  					if(`head!=`TwoId(Id())){
  	  						myNewPath=`TwoC1(head,myNewPath,f,tail);}
  	  					else{myNewPath=`TwoC1(myNewPath,f,tail);}}
  	  				if(myNewPath!=`TwoId(Id())){
  	  					System.out.print(".");
  	  					return myNewPath;}
  	  	}
  	  
  	  	//different things, transform TwoId of OneC0 in TwoC0 of TwoId
  	  	TwoId(OneC0(head,tail*)) -> { System.out.print(".");return `TwoC0(TwoId(head),TwoId(tail*)); } 
  	  	//absorbs the twoIds
  	  	TwoC1(head*,t@TwoId(_),TwoId(_),tail*) -> { if(`head!=`TwoId(Id())){return `TwoC1(head,t,tail);}else{return `TwoC1(t,tail);}}
 	 } 
}

  	//strictly the same but it has to be define locally because the tom compiler turns it into a private method
  	%strategy Gravity() extends Identity(){ 
  		visit TwoPath {
  			TwoC1(head*,f@TwoCell(_,_,_,Constructor(),_,_,_,_,_),g@TwoId(_),tail*)->{
  				if(`f.target()==`g.source()){
  					if(`head*==`TwoId(Id())){
  						if(`tail*==`TwoId(Id())){return `TwoC1(TwoId(f.source()),f);}
  						return `TwoC1(TwoId(f.source()),f,tail*);
  					}
  					if(`tail*==`TwoId(Id())){return `TwoC1(head*,TwoId(f.source()),f);}
  					System.out.print(".");
  					return `TwoC1(head*,TwoId(f.source()),f,tail*);
  				}
  			}
  			TwoC1(head*,TwoC0(head1*,f@TwoCell(_,_,_,Constructor(),_,_,_,_,_),tail1*),TwoC0(head2*,g@TwoId(_),tail2*),tail*) -> { 
  				if(`head1*.target()==`head2*.source()&&`tail1*.target()==`tail2*.source()&&`f.target()==`g.source()){																								
  					if(`head*==`TwoId(Id())){
  						if(`tail*==`TwoId(Id())){return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*));}
  						System.out.print(".");
  						return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*),tail*);
  					}
  					if(`tail*==`TwoId(Id())){return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*));}
  					System.out.print(".");
  					return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),TwoC0(head2*,f,tail2*),tail*);
  				}
  			}
  			TwoC1(head*,f@TwoCell(_,_,_,Constructor(),_,_,_,_,_),TwoC0(head2*,g@TwoId(_),tail2*),tail*) -> { 
  				if(`f.target()==`g.source()){
  					if(`head*==`TwoId(Id())){
  						if(`tail*==`TwoId(Id())){return `TwoC0(head2*,f,tail2*);}
  						System.out.print(".");
  						return `TwoC1(TwoC0(head2*,f,tail2*),tail*);
  					}
  					if(`tail*==`TwoId(Id())){return `TwoC1(head*,TwoC0(head2*,f,tail2*));}
  					System.out.print(".");
  					return `TwoC1(head*,TwoC0(head2*,f,tail2*),tail*);
  				}
  			}
  			TwoC1(head*,TwoC0(head1*,f@TwoCell(_,_,_,Constructor(),_,_,_,_,_),tail1*),g@TwoId(_),tail*) -> { 
  				if(`f.target()==`g.source()){
  					if(`head*==`TwoId(Id())){
  						if(`tail*==`TwoId(Id())){
  							System.out.print(".");
  							return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),f);
  						}
  						System.out.print(".");return `TwoC1(TwoC0(head1*,TwoId(f.source()),tail1*),f,tail*);
  					}
  					if(`tail*==`TwoId(Id())){
  						System.out.print(".");
  						return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),f);
  					}
  					System.out.print(".");return `TwoC1(head*,TwoC0(head1*,TwoId(f.source()),tail1*),f,tail*);
  				}
  			}
  		} 
  	}

	//function that extends the toArray method for twoc0s
	public static TwoPath[] toArray(TwoC0 twoc0) {
    int size = twoc0.length();
    TwoPath[] array = new TwoPath[size];
    int i=0;
    if(twoc0 instanceof ConsTwoC0) {
      TwoPath cur = twoc0;
      while(cur instanceof ConsTwoC0) {
        TwoPath elem = ((ConsTwoC0)cur).getHeadTwoC0();
        array[i] = elem;
        i++;
        cur = ((ConsTwoC0)cur).getTailTwoC0();
        
      }
      array[i] = cur;
    }
    return array;
  }
	
}
