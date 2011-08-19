package tom.engine.newparser.converter;

import java.util.Map;
import java.util.Collection;
import java.util.LinkedList;

import tom.engine.Tom;
import tom.engine.TomMessage;
import tom.engine.TomStreamManager;
import tom.engine.exception.TomRuntimeException;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.adt.code.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.cst.types.*;

/**
 * This plugin transform newparser's result (CST) into a Code (AST)
 * compatible with other plugins in the compilation chain.
 */
public class TreeFormatterPlugin extends TomGenericPlugin {

  %include { sl.tom }
  %include { util/types/Collection.tom }
  %include { ../../adt/cst/CST.tom }

  protected CstProgram cst;

  public TreeFormatterPlugin() {
    super("TreeFormatterPlugin");
  }

  public void setArgs(Object[] arg) {
    if ( arg[0] instanceof Code && arg[1] instanceof TomStreamManager ) {
      term = (Code)arg[0];
      streamManager = (TomStreamManager)arg[1];
    } else
    if (arg[0] instanceof CstProgram && arg[1] instanceof TomStreamManager ) {
      cst = (CstProgram)arg[0];
      term = null;
      streamManager = (TomStreamManager)arg[1];
    } else {
      TomMessage.error(getLogger(),null,0,TomMessage.invalidPluginArgument,
         "AstPrinterPlugin", "[Code or null, TomStreamManager]",
         getArgumentArrayString(arg));
    }
  }

  @Override
  public void run(Map informationTracker){
    
    boolean pluginIsOn = 
     ((Boolean)getOptionManager().getOptionValue("newparser")).booleanValue();

    if(pluginIsOn) {

      try {
      cst = `BottomUp(toAST()).visit(cst);
      } catch (tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException (
          "tom.engine.treeformatter.TreeFormatter.run strategy "+
          "failure on "+`cst);
      }

      %match(cst) {
        wrappedCodeList(codeList) -> {
          term = `Tom(codeList);
        }
      }
    }
  }

  %strategy toAST() extends Identity() {
    visit CstBlock {
      /*HOSTBLOCK(code,
                CsTextPosition(startLine, startColumn),
                CsTextPosition(endLine, endColumn)
               ) -> {
        return `wrappedCode(TargetLanguageToCode(TL(code,
                  TextPosition(startLine, startColumn),
                  TextPosition(endLine, endColumn))));
      }*/

      Cst_MatchConstruct[] -> {
        return `wrappedCode(
                  InstructionToCode(Match(concConstraintInstruction(),
                                        concOption()))
                );
      }
    }

    visit CstProgram {
      Cst_Program(blocks) -> {
        Collection<Code> c = new LinkedList<Code>();
        `TopDown(collectCodes(c)).visit(`blocks);
        
        CodeList res = `concCode();
        for(Code code : c){
          res = `concCode(res*, code);
        }
        
        return `wrappedCodeList(res);
      }
    }
  }

  %strategy collectCodes(Collection c) extends Identity() {
    visit Code {
      x -> { c.add(`x); }
    }
  }

}
