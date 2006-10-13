package bpel;

import bpel.wfg.types.*;
import aterm.*;
import aterm.pure.*;
import tom.antlrmapper.ATermAST;
import java.io.StringReader;
import java.io.*;
import tom.antlrmapper.ATermAST;
import antlr.collections.*;
import antlr.*;


public class ConditionParser {
  %include {wfg/Wfg.tom}
  %include {Mapping.tom}

  public static Condition getCondition(ATerm t) {
    %match(t) {
      OR(_,(c1,c2)) -> { 
        return `or(getCondition(c1), getCondition(c2));
      }
      AND(_,(c1,c2)) -> { 
        return `and(getCondition(c1), getCondition(c2));
      }
      ID(NodeInfo[text=text],_) -> {
        return `label(text);
      }
    }
    return null;
  }


  public static Condition parse(String cond){
    try{
      CondLexer lexer = new CondLexer(new StringReader(cond));
      CondParser parser = new CondParser(lexer);

      // Parse the input expression
      parser.setASTNodeClass("tom.antlrmapper.ATermAST");
      parser.cond();

      // walk the input
      ATermAST t = (ATermAST) parser.getAST();
    return getCondition(t.genATermFromAST(TokenTable.getTokenMap()));
    }catch(Exception e ){throw new RuntimeException(e); }
  }
}
