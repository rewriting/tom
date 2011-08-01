package tom.engine.treeprinter;

import tom.engine.adt.code.types.*;
import tom.engine.adt.cst.types.*;

import tom.platform.OptionManager;
import tom.engine.TomMessage;
import tom.engine.TomStreamManager;
import tom.engine.tools.TomGenericPlugin;
import tom.library.utils.Viewer;
import tom.library.sl.Visitable;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * This plugin print a representation of current term as previous plugin left it.
 * This term (actually arg[0] from setArgs) may be null or a instance of
 * tom.library.sl.Visitable (most of the time a Code).
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
 */
public class TreePrinterPlugin extends TomGenericPlugin {

  %include {sl.tom}
  //%include {../adt/code/Code.tom}
  //%include {../adt/cst/CST.tom}
  %include {../adt/tomsignature/TomSignature.tom}

  /**
   * stores configuration.
   */
  private class TreePrinterPluginConf {
    
    Map<Integer, TreePrinterPluginConfItem> conf =
      new HashMap<Integer, TreePrinterPluginConfItem>();

    TreePrinterPluginConf(String pluginconfstring) {
      String[] items = pluginconfstring.split("\\|");

      for(String item : items){
        String[] instancenumbandconf = item.split(":");
        conf.put(new Integer(instancenumbandconf[0].trim()),
                new TreePrinterPluginConfItem(instancenumbandconf[1]));
      }
    }

    TreePrinterPluginConfItem getConf(int instnumb) {
      TreePrinterPluginConfItem res = conf.get(new Integer(instnumb));
      return (res!=null)?res:new TreePrinterPluginConfItem();
    }
  }

  private class TreePrinterPluginConfItem {

    private List<String> requestedtrueoptions = new ArrayList<String>();
    private List<String> requestedfalseoptions = new ArrayList<String>();
    private String info = "";

    TreePrinterPluginConfItem(){;}

    /**
     * confstring like :
     * ((!)?booleanoptionname)('+'((!)?booleanoptionname))* ',' string
     */
    TreePrinterPluginConfItem(String confstring) {

      String[] optionsandinfo = confstring.split(",");
      if(optionsandinfo.length>=2){
        info=optionsandinfo[1];
      }

      String[] requestedoptions = optionsandinfo[0].split("\\+");
      for(String requestedoption : requestedoptions){
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
        for(String requestedtrueoption : requestedtrueoptions){
          if(!((Boolean)getOptionManager().getOptionValue(requestedtrueoption))
              .booleanValue() ) {
            return false;
          }
        }

        for(String requestedfalseoption : requestedfalseoptions){
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

  

  private static int nextRunCallNumber = 1;
  private static TreePrinterPluginConf conf = null;

  public TreePrinterPlugin() {
    super("astprinterplugin");
  }

  protected Visitable visitable;

  /**
   * overriden to accept a null arg[0] and anything visitable
   */
  @Override
  public void setArgs(Object[] arg) {

    if ( (arg[0]==null ||  arg[0] instanceof Visitable)
        && (arg[1] instanceof TomStreamManager) ) {
     
      visitable = (Visitable)arg[0];

      streamManager = (TomStreamManager)arg[1];
    } else {
      TomMessage.error(getLogger(),null,0,TomMessage.invalidPluginArgument,
         "AstPrinterPlugin", "[Visitable null, TomStreamManager]",
         getArgumentArrayString(arg));
    }
  }

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
        "\n== "+ instanceConf.getInfo()
        +" ====================================================");
     
     if(visitable!=null) {

       Visitable oneLined = null;
       try {
          oneLined = `BottomUp(toSingleLineTargetLanguage()).visit(visitable);
       } catch (tom.library.sl.VisitFailure e) {
         System.err.println("VisitFailure Exception"); //XXX handle cleanly
       }
      
      Viewer.toTree(oneLined);

      //System.out.println(oneLined);

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


  private static String formatTargetLanguageString(String s) {
    s = s.replaceAll("\n", "\\\\n");
    s = s.replaceAll("\r", "\\\\r");
    s = s.replaceAll("\t", "\\\\t");
    return "["+s+"]";
  }

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

    visit gt_Block {
      HOSTBLOCK(code, x, y) -> {
        return `HOSTBLOCK(formatTargetLanguageString(code), x, y);
      }
    }
  }
}
