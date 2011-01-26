/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package xml.rss;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import tom.engine.*;

public class MatchGenerator {
  
  %include{adt/tnode/TNode.tom}
  %include{boolean.tom}

  private static XmlTools xtools = null;
  private static String rssFilename = null;
  private static String workDir = null;
    
  private static final String fileSep = System.getProperty("file.separator");
  
  private static final String MATCH_TEMPLATE_FILENAME = "Filter"; 
  
  public MatchGenerator(){	  
	  xtools = new XmlTools();
	   
	  // for launching from eclipse  		
	  rssFilename = System.getProperty("user.dir") + fileSep
		+ "workspace" + fileSep
		+ "jtom" + fileSep
		+ "examples" + fileSep
		+ "build" + fileSep
		+ "xml" + fileSep
		+ "rss.xml";		
	
	  File file = new File(rssFilename);	  
	  if (!file.exists()){
		  rssFilename = "xml/rss.xml";
	  }	 
  }  
  
  public static void main(String[] args) throws Exception{
	  
	  //String pattern = args[0];
	  String pattern = "<item><title>#TEXT(\"Many casualties in Baghdad blast\")</title></item>";
	  //String pattern = "<item><title>(_*,#TEXT(\"Baghdad\"),_*)</title></item>";
	  String className = getUniqueFileName(MATCH_TEMPLATE_FILENAME,pattern);
	  workDir = args[0];	  
	  	  
	  MatchGenerator mg = new MatchGenerator();
	  System.out.println("path:" + rssFilename);
	  
	  TNode term = (TNode)xtools.convertXMLToTNode(rssFilename);
	  TNode result = mg.applyPattern(pattern,term.getDocElem(), className);
	  
	  xtools.printXMLFromTNode(result);
  }
  
  private TNode applyPattern(String pattern, TNode subject, String className)
  throws Exception{
	  // TODO - if the file exists, just load it and call the method	  
	  Writer writer = new BufferedWriter(new FileWriter(workDir + fileSep + className+".t"));
	  // generate the .t  
	  generate(writer,className,pattern);	  
	  // generate .java
	  System.setProperty("tom.home",System.getenv("TOM_HOME"));
	  int tomResult = Tom.exec(getTomParams(className));
	  System.out.println("Tom returned: " + tomResult);
	  // compile class	  
	  int javacResult = com.sun.tools.javac.Main.compile(new String[] {
//	            "-classpath", "bin",
//	            "-d", "/temp/dynacode_classes",
			  workDir + fileSep + className +".java"});	  
	  System.out.println("Java returned: " + javacResult);
	  // loadClass
	  Class cls = ClassLoader.getSystemClassLoader().loadClass("xml." + className);
	  IMatchXML matchCls = (IMatchXML)cls.newInstance();
	  return matchCls.match(subject);
  }
  
  private String[] getTomParams(String className){
	  
	  File xmlFile = new File(System.getenv("TOM_HOME") + fileSep + "Tom.xml");
	  if(!xmlFile.exists()) {
		  System.out.println("Failed to get canonical path for "+xmlFile);
		  System.exit(0);
	  }
	  
	  String file_path = workDir + fileSep + className+".t";
	  File output = new File(file_path);
	  if (!output.exists()){
		  System.out.println("Failed to get canonical path for "+output.getPath());
		  System.exit(0);
	  }

	  ArrayList<String> tomParams = new ArrayList<String>();
	  
	  tomParams.add("-X");
	  tomParams.add(xmlFile.getPath());
	  tomParams.add("--optimize");
	  tomParams.add("--optimize2");
	  tomParams.add("--output");
	  tomParams.add(workDir + fileSep + className+".java");
	  tomParams.add(file_path);
	  	  
	  return (String[])tomParams.toArray(new String[tomParams.size()]);
  }
  
  private static String getUniqueFileName(String classname, String pattern){
	  
	  String result = "";
	  
	  for (int i=0; i < pattern.length(); i++){
		  int numericValue = Character.getNumericValue(pattern.charAt(i));
		  result += numericValue > 0 ? "" + numericValue: "m" + (-1 * numericValue); 
	  }
	  
	  return classname + result;
  }

  public static void generate(Writer writer, String className, String pattern)
  throws IOException {
	  
	  writer.write(			  
%[
package xml;  

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

public class @className@ implements IMatchXML{
  
  %include{adt/tnode/TNode.tom}
  %include{ sl.tom }	
  
  static TNodeList retList = `concTNode();
  
  public TNode match(TNode subject){	  	  
	  `TopDown(MatchXml()).apply(subject);
	  return `xml(<Result>retList*</Result>);
  }
  
  %strategy MatchXml() extends `Identity() {
	  visit TNode{
		  t@@@pattern@ ->{
			  retList = `concTNode(retList*, t);			
		  }
	  }
  }  
  
}

]%);
      writer.flush();
      writer.close();
	  
  }

}
