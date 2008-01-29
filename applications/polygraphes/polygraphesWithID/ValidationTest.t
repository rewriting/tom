package tools;


import myprogram.testprogramv2.polygraph.types.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import myprogram.TestProgramv2;
import compiler.XMLhandler;
import java.io.File;
import java.io.IOException;

//load the tests, run the testing program on source paths and compare the results to the expected ones
public class ValidationTest{

	%include { myprogram/testprogramv2/polygraph/Polygraph.tom }
		
	public static void main(String[] args) {
		String testSetPath="";
		if(args.length!=1){
			System.out.println("you must indicate the path of the test set xml file");
			}
		else{
			if(args[0].substring(args[0].length()-4, args[0].length()).equals(".xml")){
				testSetPath=args[0];
		//runs the test
		String log=runValidationTest(testSetPath);
		//print the log
		System.out.println(log);
			}
			else{
			System.out.println("the first and only argument must be an xml file i.e. with the extension .xml");}
		}
	}
	
	
	public static String runValidationTest(String testSetPath){
		boolean successfulTest=true;
		int counter=1;
		String log="";
		try{
			//loads the xml file
			Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(testSetPath);
			Element e = dom.getDocumentElement();
			NodeList childs=e.getChildNodes();
			for (int i = 0; i < childs.getLength(); i++) {
				//runs the test one after another
				Node child = childs.item(i);
				if(child.getNodeName().equals("Test")){
					NodeList testNodes=child.getChildNodes();
					//gets the description of the text
					String testDescription=child.getAttributes().getNamedItem("name").toString();
					TwoPath source=`TwoId(Id());
					TwoPath target=`TwoId(Id());
					for (int j = 0; j < testNodes.getLength(); j++) {
						Node testNode = testNodes.item(j);
						if(testNode.getNodeName().equals("Source")){
							//gets the source
							source=TestProgramv2.makeTwoPath(testNode);
						}
						if(testNode.getNodeName().equals("Target")){
							//gets the expected target
							target=TestProgramv2.makeTwoPath(testNode);
						}
					}
					//runs the program on the source and gets the result
					TwoPath computedTarget=TestProgramv2.eval(source);
					log+="Test #"+(counter++)+" "+testDescription;
					//comparison of the expected target and the computed target
					if(isEqual(target,computedTarget)){log+=" SUCCESS\n";}
					else{successfulTest=false;log+=" FAILED\n";
					log+="expected : "+target.prettyPrint()+"\n";
					log+="computed : "+computedTarget.prettyPrint()+"\n";
					}
				}
			}
		}catch(Exception e){ e.printStackTrace();}
		//the test is successful if every test is successful
		if(successfulTest){log+="\nTEST SUCCESSFUL";}
		else{log+="\nTEST FAILED";}
		return log;
	}

	//test the equality but dont take ids into account
	public static boolean isEqual(TwoPath t1, TwoPath t2){
		if(t1==t2){return true;}
		%match (t1,t2){
			TwoCell(name,target,source,type,_),TwoCell(name,target,source,type,_) -> { return true; }
			TwoC0(left1,right1*),TwoC0(left2,right2*) -> { return isEqual(`left1,`left2)&isEqual(`right1,`right2);}
			TwoC1(up1,down1*),TwoC1(up2,down2*) -> { return isEqual(`up1,`up2)&isEqual(`down1,`down2);}
		}
		return false;
	}

}