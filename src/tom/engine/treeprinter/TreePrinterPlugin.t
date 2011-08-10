package tom.engine.treeprinter;

import tom.engine.adt.code.types.*;
import tom.engine.adt.cst.types.*;

import tom.platform.OptionManager;
import tom.engine.TomMessage;
import tom.engine.TomStreamManager;
import tom.engine.tools.TomGenericPlugin;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import tom.library.utils.Viewer;
import tom.library.sl.Visitable;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * This plugin print a representation of current term as previous plugin left
 * it.
 * This term (actually arg[0] from setArgs) may be null (in such case will
 * just say so) or a instance of any kind of tom.library.sl.Visitable
 * (most of the time a Code).
 *
 * Configuration is done throw xml.
 * A string option named "treeprinterconf" is requested.
 *
 * It's syntax is :
 *
 * (
 *   runCallNumber
 *   ':'
 *   (optionRequestedToBeTrueName | '!' optionRequestedToBeFalseName)
 *   ('+' optionRequestedToBeTrueName | '!' optionRequestedToBeFalseName))*
 *   ','
 *   descriptionOfPrintedTree
 * )
 * (
 *  '|'
 *  ( and so on...)
 * )*
 *
 * These are valid treeprinterconf values :
 * "1:printfirst,FirstTree"
 * "1:printfirst,FirstTree | 2:printsecond,SecondTree"
 * "1:printTrees+!finalOnly,SomeTree | 2:printTrees,AnotherOne"
 * "1:ast+!np,OldParserAST | 2:cst+np,NewParserCST | 3:ast+np,NewParserAST"
 *
 * Remark : This configuration string is pretty sensitive to small changes
 * (it's easy to crash), but this plugin is of no interest for "real" use of
 * the compiler, so it's probably not such a big problem.
 */
public class TreePrinterPlugin extends TomGenericPlugin {

  %include {sl.tom}
  %include {../adt/tomsignature/TomSignature.tom}
  
  public static final String DECLARED_OPTIONS = 
    "<options>" +
    "<boolean name='printcst' altName='cst' description='print post-parsing cst (only with new parser)' value='false'/>" +
    "<boolean name='printast' altName='ast' description='print post-parsing ast' value='false'/>" +
    "<string name='treeprinterconf' altName=''" +
    "        description='defines how and when tree should be printed to stdout (default value is probably just fine)'" +
    "        value='1:printast+!newparser,OldParserAST | 2:printcst+newparser,NewParserCST | 3:printast+newparser,NewParserAST'" +
    "        attrName='treeprinterconf'/>" +
    "</options>";
  
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TreePrinterPlugin.DECLARED_OPTIONS);
  }

  private static int nextRunCallNumber = 1;
  private static TreePrinterPluginConf conf = null;

  protected Visitable visitable;
  
  public TreePrinterPlugin() {
    super("astprinterplugin");
  }

  /**
   * overriden to accept a null arg[0] and anything visitable
   */
  @Override
  public void setArgs(Object[] arg) {

    if( (arg[0]==null || arg[0] instanceof Visitable)
        && (arg[1] instanceof TomStreamManager) ) {
     
      visitable = (Visitable)arg[0];
      streamManager = (TomStreamManager)arg[1];
    } else {
      TomMessage.error(getLogger(),null,0,TomMessage.invalidPluginArgument,
         "AstPrinterPlugin", "[Visitable or null, TomStreamManager]",
         getArgumentArrayString(arg));
    }
  }

  /**
   * Run's behaviour may change each time it's called.
   * Each execution have a different value of thisRunCallNumber and behaviour
   * will change according to that number.
   */
  @Override
  public void run(Map informationTracker) {

    int thisRunCallNumber = nextRunCallNumber;
    nextRunCallNumber++;

    // this is done here and not in constructor to be able to access
    // optionManager
    if(conf==null) {
       conf = new TreePrinterPluginConf(
              (String)getOptionManager().getOptionValue("treeprinterconf"));
    }

  TreePrinterPluginConfItem instanceConf = conf.getConf(thisRunCallNumber);
    
   if(instanceConf.isActive()) {
      System.out.println(
        "\n== "+ instanceConf.getInfo() +
	" ====================================================");
     
     if(visitable!=null) {
       try {
          Visitable oneLined = `BottomUp(toSingleLineTargetLanguage()).visit(visitable);
	  Viewer.toTree(oneLined);
       } catch (tom.library.sl.VisitFailure e) {
         System.err.println("VisitFailure Exception"); //XXX handle cleanly
       }
     } else {
       System.out.println("Nothing to print (this tree is null)");
     }

      System.out.println(
        "== /"+ instanceConf.getInfo()
        +" ===================================================");

    }
  }

  @Override 
  public Object[] getArgs() {
    return new Object[]{visitable, streamManager};
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

  /**
   * Change every hostCode block so it's on a single line.
   * Make printed tree more easily readable.
   */
  %strategy toSingleLineTargetLanguage() extends Identity() {
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
  /*
    visit gt_Block {
      HOSTBLOCK(e, x, y) -> {
        return `HOSTBLOCK(formatTargetLanguageString(code), x, y);
      }
    }
  */
  }

  /**
   * stores configuration.
   */
  private class TreePrinterPluginConf {
   
    Map<Integer, TreePrinterPluginConfItem> mapconf =
      new HashMap<Integer, TreePrinterPluginConfItem>();

    public TreePrinterPluginConf(String pluginconfstring) {
      String[] items = pluginconfstring.split("\\|");

      for(String item : items) {
        String[] instancenumbandconf = item.split(":");
        mapconf.put(new Integer(instancenumbandconf[0].trim()),
                new TreePrinterPluginConfItem(instancenumbandconf[1]));
      }
    }

    TreePrinterPluginConfItem getConf(int instnumb) {
      TreePrinterPluginConfItem res = mapconf.get(new Integer(instnumb));
      return (res!=null)?res:new TreePrinterPluginConfItem();
    }
  }

  private class TreePrinterPluginConfItem {

    private List<String> requestedtrueoptions = new ArrayList<String>();
    private List<String> requestedfalseoptions = new ArrayList<String>();
    private String info = "";

    public TreePrinterPluginConfItem(){;}

    /**
     * confstring like :
     * ((!)?booleanoptionname)('+'((!)?booleanoptionname))* ',' string
     */
    public TreePrinterPluginConfItem(String confstring) {

      String[] optionsandinfo = confstring.split(",");
      if(optionsandinfo.length>=2) {
        info=optionsandinfo[1];
      }

      String[] requestedoptions = optionsandinfo[0].split("\\+");
      for(String requestedoption : requestedoptions) {
        requestedoption = requestedoption.trim();
        
        if(requestedoption.startsWith("!")) {
          requestedoption = requestedoption.substring(1);
          requestedfalseoptions.add(requestedoption.trim());
        } else {
          requestedtrueoptions.add(requestedoption);
        }
      }
    }

    boolean isActive() {
      if(requestedtrueoptions.size()==0 && requestedfalseoptions.size()==0) {
        return true;
      } else {
        for(String requestedtrueoption : requestedtrueoptions) {
          if(!((Boolean)getOptionManager().getOptionValue(requestedtrueoption))
              .booleanValue() ) {
            return false;
          }
        }

        for(String requestedfalseoption : requestedfalseoptions) {
          if(((Boolean)getOptionManager().getOptionValue(requestedfalseoption))
              .booleanValue() ) {
            return false;
          }
        }
      }

      return true;
    }

    String getInfo() {
      return info;
    }

    // May help for debug
    @Override
    public String toString() {
       String res = "";
       res+= "=====================\n";
       res+= "requested true :";
       for(String to : requestedtrueoptions)
         res+=" "+to;
       res+= "\nrequested false :";
       for(String fo : requestedfalseoptions)
         res+=" "+fo;
       res+= "\ninfo string : "+info+"\n" ;
       res+= "=====================";


       return res;
    }
  }
}
