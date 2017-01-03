/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2016-2017, Universite de Lorraine, Inria
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.parser;

import java.io.*;
import java.util.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import tom.platform.OptionManager;
import tom.engine.TomStreamManager;
import tom.engine.exception.TomIncludeException;
import tom.engine.TomMessage;

import tom.library.sl.*;

import tom.engine.adt.code.types.*;
import tom.engine.adt.cst.types.*;

public class TomParserTool {
  %include {sl.tom}
  %include {../adt/tomsignature/TomSignature.tom}

  private static Logger logger = Logger.getLogger("tom.engine.parser.TomParserTool");
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  private TomStreamManager streamManager;
  private OptionManager optionManager;

  public TomParserTool(TomStreamManager streamManager, OptionManager optionManager) {
    this.streamManager = streamManager;
    this.optionManager = optionManager;
  }

  public TomStreamManager getStreamManager() {
    return this.streamManager;
  }
  public OptionManager getOptionManager() {
    return this.optionManager;
  }

  /*
   * search file corresponding to an include
   */
  public String searchIncludeFile(String currentFileName, String filename, int lineNumber) throws TomIncludeException {

    String includeName = `filename.trim();
    includeName = includeName.replace('/',File.separatorChar);
    includeName = includeName.replace('\\',File.separatorChar);
    if(includeName.equals("")) {
      throw new TomIncludeException(TomMessage.missingIncludedFile,
        new Object[]{currentFileName, lineNumber});
    }

    File file = new File(includeName);
    if(file.isAbsolute()) {
      try {
        file = file.getCanonicalFile();
      } catch (IOException e) {
        System.out.println("IO Exception when computing included file");
        e.printStackTrace();
      }

      if(!file.exists()) {
        file = null;
      }
    } else {
      /* StreamManager shall find it */

      //System.out.println("currentFileName: " + currentFileName);
      //System.out.println("includeName: " + includeName);
      file = getStreamManager().findFile(new File(currentFileName).getParentFile(), includeName);
    }
    if(file == null) {
      throw new TomIncludeException(TomMessage.includedFileNotFound,
          new Object[]{
            filename, 
            currentFileName, 
            lineNumber,
            currentFileName});
    }

    String res = "";
    try {
      res = file.getCanonicalPath();
    } catch (IOException e) {
      TomMessage.finer(getLogger(), null, 0, TomMessage.failGetCanonicalPath,file.getPath());
    }
    return res;
  }

  /*
   * parse a Gom construct
   */
  public String parseGomFile(String gomCode, int initialGomLine, String[] userOpts) throws TomIncludeException {
    String res = "";
    //System.out.println("gomCode: " + gomCode);
    String currentFileName = getStreamManager().getInputFileName();

    File config_xml = null;
    ArrayList<String> parameters = new ArrayList<String>();
    try {
      String tom_home = System.getProperty("tom.home");
      if(tom_home != null) {
        config_xml = new File(tom_home,"Gom.xml");
      } else {
        // for the eclipse plugin for example
        String tom_xml_filename = ((String)getOptionManager().getOptionValue("X"));
        config_xml = new File(new File(tom_xml_filename).getParentFile(),"Gom.xml");
        // pass all the received parameters to gom in the case that it will call tom
        java.util.List<File> imp = getStreamManager().getUserImportList();
        for(File f:imp) {
          parameters.add("--import");
          parameters.add(f.getCanonicalPath());
        }
      }
      config_xml = config_xml.getCanonicalFile();
    } catch (IOException e) {
      TomMessage.finer(getLogger(), null, 0, TomMessage.failGetCanonicalPath,config_xml.getPath());
    }

    String destDir = getStreamManager().getDestDir().getPath();
    String packageName = getStreamManager().getPackagePath().replace(File.separatorChar, '.');
    String inputFileNameWithoutExtension = getStreamManager().getRawFileName().toLowerCase();
    String subPackageName = "";
    if(packageName.equals("")) {
      subPackageName = inputFileNameWithoutExtension;
    } else {
      subPackageName = packageName + "." + inputFileNameWithoutExtension;
    }

    parameters.add("-X");
    parameters.add(config_xml.getPath());
    parameters.add("--destdir");
    parameters.add(destDir);
    parameters.add("--package");
    parameters.add(subPackageName);
    if(getOptionManager().getOptionValue("wall")==Boolean.TRUE) {
      parameters.add("--wall");
    }
    if(getOptionManager().getOptionValue("intermediate")==Boolean.TRUE) {
      parameters.add("--intermediate");
    }
    if(Boolean.TRUE == getOptionManager().getOptionValue("optimize")) {
      parameters.add("--optimize");
    }
    if(Boolean.TRUE == getOptionManager().getOptionValue("optimize2")) {
      parameters.add("--optimize2");
    }
    if(Boolean.TRUE == getOptionManager().getOptionValue("newtyper")) {
      parameters.add("--newtyper");
    }
    if(Boolean.TRUE == getOptionManager().getOptionValue("newparser")) {
      parameters.add("--newparser");
    }
    parameters.add("--intermediateName");
    parameters.add(getStreamManager().getRawFileName()+".t.gom");
    if(getOptionManager().getOptionValue("verbose")==Boolean.TRUE) {
      parameters.add("--verbose");
    }
    /* treat user supplied options */
    //textCode = t.getText();
    //if(textCode.length() > 6) {
    //  String[] userOpts = textCode.substring(5,textCode.length()-1).split("\\s+");
      for(int i=0; i < userOpts.length; i++) {
        parameters.add(userOpts[i]);
      }
    //}

    final File tmpFile;
    try {
      tmpFile = File.createTempFile("tmp", ".gom", null).getCanonicalFile();
      parameters.add(tmpFile.getPath());
    } catch (IOException e) {
      TomMessage.error(getLogger(), null, 0, TomMessage.ioExceptionTempGom,e.getMessage());
      e.printStackTrace();
      return res;
    }

    TomMessage.fine(getLogger(), null, 0, TomMessage.writingExceptionTempGom,tmpFile.getPath());

    try {
      Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpFile)));
      writer.write(new String(gomCode.getBytes("UTF-8")));
      writer.flush();
      writer.close();
    } catch (IOException e) {
      TomMessage.error(getLogger(), null, 0, TomMessage.writingFailureTempGom,e.getMessage());
      return res;
    }

    /* Prepare arguments */
    Object[] preparams = parameters.toArray();
    String[] params = new String[preparams.length];
    for (int i = 0; i < preparams.length; i++) {
      params[i] = (String)preparams[i];
    }

    int errorCode = 1;
    Map<String,String> informationTracker = new HashMap<String,String>();
    informationTracker.put(tom.engine.tools.TomGenericPlugin.KEY_LAST_GEN_MAPPING,null);

    informationTracker.put("gomBegin",""+initialGomLine);
    informationTracker.put("inputFileName",getStreamManager().getInputFileName());

    try {
      // Call tom.gom.Gom.exec(params,informationTracker) using reflexivity, to
      // avoid a build time dependency between tom and gom
      errorCode = ((Integer) Class.forName("tom.gom.Gom")
          .getMethod("exec", new Class[] {params.getClass(), Map.class})
          .invoke(null, new Object[] {params, informationTracker}))
        .intValue();
    } catch (ClassNotFoundException cnfe) {
      TomMessage.error(getLogger(), currentFileName, initialGomLine, TomMessage.gomInitFailure,currentFileName,Integer.valueOf(initialGomLine), cnfe);
    } catch (NoSuchMethodException nsme) {
      TomMessage.error(getLogger(), currentFileName, initialGomLine, TomMessage.gomInitFailure,currentFileName,Integer.valueOf(initialGomLine), nsme);
    } catch (InvocationTargetException ite) {
      TomMessage.error(getLogger(), currentFileName, initialGomLine, TomMessage.gomInitFailure,currentFileName,Integer.valueOf(initialGomLine), ite);
    } catch (IllegalAccessException iae) {
      TomMessage.error(getLogger(), currentFileName, initialGomLine, TomMessage.gomInitFailure,currentFileName,Integer.valueOf(initialGomLine), iae);
    }
    tmpFile.deleteOnExit();
    if(errorCode != 0) {
      TomMessage.error(getLogger(), currentFileName, initialGomLine, TomMessage.gomFailure,currentFileName,Integer.valueOf(initialGomLine));
      return res;
    }
    String generatedMapping = (String)informationTracker.get(tom.engine.tools.TomGenericPlugin.KEY_LAST_GEN_MAPPING);

    // Simulate the inclusion of generated Tom file
    /*
     * We shall not need to test the validity of the generatedMapping file name
     * as gom returned <> 0 if it is not valid
     *
     * Anyway, for an empty gom signature, no files are generated
     */
    if(generatedMapping != null) {
      res = generatedMapping;
    }

    return res;
  }

  public void printTree(Visitable tree) {
    if(tree!=null) {
      try {
        tree = `BottomUp(ToSingleLineTargetLanguage()).visit(tree);
        tom.library.utils.Viewer.toTree(tree);
      } catch (tom.library.sl.VisitFailure e) {
        System.err.println("VisitFailure Exception"); //XXX handle cleanly
      }
    } else {
      System.out.println("Nothing to print (tree is null)");
    }
  }

  /**
   * Change every hostCode block so it's on a single line.
   * Make printed tree more easily readable.
   */
  %strategy ToSingleLineTargetLanguage() extends Identity() {
    visit TargetLanguage {
      TL[Code=code, Start=start, End=end] -> {
        return `TL(formatTargetLanguageString(code), start, end);
      }
      ITL[Code=code] -> {
        return `ITL(formatTargetLanguageString(code));
      }
      Comment[Code=code] -> {
        return `Comment(formatTargetLanguageString(code));
      }
    }
  }

  /**
   * Usefull to print every hostCode on a single line in tree.
   * Improve readability.
   */
  private static String formatTargetLanguageString(String s) {
    s = s.replaceAll("\n", "\\\\n");
    s = s.replaceAll("\r", "\\\\r");
    s = s.replaceAll("\t", "\\\\t");
    return "["+s+"]";
  }


  public String metaEncodeCode(String code) {
		/*
			 System.out.println("before: '" + code + "'");
			 for(int i=0 ; i<code.length() ; i++) {
			 System.out.print((int)code.charAt(i));
			 System.out.print(" ");
			 }
			 System.out.println();
		 */
		char bs = '\\';
		StringBuilder sb = new StringBuilder((int)1.5*code.length());
		for(int i=0 ; i<code.length() ; i++) {
			char c = code.charAt(i);
			switch(c) {
				case '\n':
					sb.append(bs);
					sb.append('n');
					break;
				case '\r':
					sb.append(bs);
					sb.append('r');
					break;
				case '\t':
					sb.append(bs);
					sb.append('t');
					break;
				case '\"':
				case '\\':
					sb.append(bs);
					sb.append(c);
					break;
				default:
					sb.append(c);
			}
		}
    //System.out.println("sb = '" + sb + "'");
		sb.insert(0,'\"');
		sb.append('\"');
		return sb.toString();
  }


}


