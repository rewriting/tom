package parser;

import java.io.DataInputStream;
import parser.rec.types.*;
import parser.rec.RecAdaptor;
import tom.library.sl.*;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;
import java.io.*;
import java.util.*;

public class Main {

  %include { rec/Rec.tom }
  %include { sl.tom }

  public static void main(String[] args) throws VisitFailure {
    try {
      if(args.length<=0) {
        System.out.println("usage: java Main <filename>"); 
      } else {
        // Initialize parser
        RecLexer lexer = new RecLexer(new ANTLRInputStream(new FileInputStream(args[0])));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RecParser parser = new RecParser(tokens);
        // Parse the input expression
        Tree b = (Tree) parser.program().getTree();
        Stm p = (Stm) RecAdaptor.getTerm(b);
        System.out.println(p);
        Main main = new Main();
        main.interp(p);
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  private int lookup(Table table, String key) {
    %match(table) {
      Table(name,value,tail) -> {
        if(key==`name) {
          return `value;
        } else {
          return lookup(`tail,key);
        }
      }
    }
    return 0;
  }

  private void interp(Stm s) {
    Table table = `EmptyTable();
    interpStm(s,table);
  }

  private Table interpStm(Stm s, Table table) {
    %match(s) {
      Assign(name,e1) -> {
        Pair p = interpExp(`e1,table);
        Table newTable = `Table(name,p.getValue(),p.getTable());   
        return newTable;
      }
      
      Seq(s1,s2*) -> { 
        Table newTable = `interpStm(s2,interpStm(s1,table));
        return newTable;
      }
      
      Print(l) -> {
        Table t = interpPrint(`l,table);
        System.out.println();
        return t;
      }
      If(c,s1,s2) -> {
        Pair p = interpExp(`c,table);
        if(p.getValue()==1) {
          return `interpStm(s1,table);
        } else {
          return `interpStm(s2,table);
        }
        
      }    
    }
    return `EmptyTable();
  }

  private Table interpPrint(ExpList list, Table table) {
    %match(list) {
      ExpList(e1,tail*) -> {
        Pair p = `interpExp(e1,table);
        System.out.print(p.getValue());
        System.out.print(" ");
        return interpPrint(`tail*,p.getTable());
      }
    }
    return table;
  }
    
  private Pair interpExp(Exp e, Table table) {
    %match(e) {
      Id(n) -> { return `Pair(lookup(table,n),table); }
      Num(v) -> { return `Pair(v,table); }
      True() -> { return `Pair(1,table); }
      False() -> { return `Pair(0,table); }
      NotExp(e1) -> {
        Pair p = `interpExp(e1,table);
        int v = (p.getValue()==1)?0:1;
        return `Pair(v,p.getTable());
      }
      OpExp(e1,op,e2) -> {
        Pair p1 = `interpExp(e1,table);
        Pair p2 = `interpExp(e2,p1.getTable());
        %match(p1,op,p2) {
          Pair(i1,t1), Plus(), Pair(i2,t2) -> { return `Pair(i1 + i2,t2); }
          Pair(i1,t1), Minus(), Pair(i2,t2) -> { return `Pair(i1 - i2,t2); }
          Pair(i1,t1), Times(), Pair(i2,t2) -> { return `Pair(i1 * i2,t2); }
          Pair(i1,t1), Div(), Pair(i2,t2) -> { return `Pair(i1 / i2,t2); }
          Pair(i1,t1), Equal(), Pair(i2,t2) -> { return `Pair((i1==i2)?1:0,t2); }
        }
      }

      SeqExp(s1,e1) -> {
        Table t = `interpStm(s1,table);
        Pair p = `interpExp(e1,t);
        return p;
      }

    }
    System.out.println("should not be there: " + e);
    return null;      
  }

}
