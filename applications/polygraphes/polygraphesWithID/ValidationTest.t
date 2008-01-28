package tools;


import myprogram.testprogramv2.polygraph.types.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import myprogram.TestProgramv2;
import tools.XMLhandler;
import java.io.File;
import java.io.IOException;

//load the tests, run the testing program on source paths and compare the results to the expected ones
public class ValidationTest{

	%include { myprogram/testprogramv2/polygraph/Polygraph.tom }

	public static String testSetPath="/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/polygraphesWithID/testset.xml";
		
	public static void main(String[] args) {
		//runs the test
		String log=runValidationTest();
		//save the test log into a file
		try{
		XMLhandler.save(log,new File("/Users/aurelien/polygraphWorkspace/PolygraphesApp/polygraphes/polygraphesWithID/test.log"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String runValidationTest(){
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
		%match (t1,t2){
			TwoCell(name,target,source,type,_),TwoCell(name,target,source,type,_) -> { return true; }
			TwoId(t),TwoId(t) -> { return true;}
			TwoC0(left1,right1*),TwoC0(left2,right2*) -> { return isEqual(`left1,`left2)&isEqual(`right1,`right2);}
			TwoC1(up1,down1*),TwoC1(up2,down2*) -> { return isEqual(`up1,`up2)&isEqual(`down1,`down2);}
		}
		return false;
	}

}