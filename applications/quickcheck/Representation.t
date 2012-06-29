import sort.strategy.expr.*;
import sort.types.*;
import sort.types.expr.*;
import tom.library.sl.*;
import java.io.*;

public class Representation {
  %include { sl.tom }
  %include {sort/Sort.tom}
  

  private static String represente_aux(Expr e, String way){
    String res = new String();
    %match(e){
      zero() -> {res += "\"" + way + "\"\n";}
      un() -> {res += "\"" + way + "\"\n";}
      plus(a,b) -> {
        res += "\"" + way + "\"" + "->" + "\"" + way + 0 + "\"\n";
        res += "\"" + way + "\"" + "->" + "\"" + way + 1 + "\"\n";
        res += represente_aux(`a, way + 0);
        res += represente_aux(`b, way + 1);
      }
      mult(a,b) -> {
        res += "\"" + way + "\"" + "->" + "\"" + way + 0 + "\"\n";
        res += "\"" + way + "\"" + "->" + "\"" + way + 1 + "\"\n";
        res += represente_aux(`a, way + 0);
        res += represente_aux(`b, way + 1);
      }
    }
    return res;
  }

  public static void represente(Expr e, String chemin){
    String res = "digraph mon_graphe {\n";
    res += represente_aux(e, "8");
    res += "}\n";
    File fichier = new File(chemin);
    try{
      FileOutputStream graveur = new FileOutputStream(fichier);
      graveur.write(res.getBytes());
      graveur.close(); 
    } catch (java.io.IOException err){
      System.out.println("ecriture fichier impossible");
    }    
  }
  
  private static String represente_aux_hash(Expr e){
    String res = new String();
    %match(e){
      a@zero() -> {res += (`a).hashCode() + "\n";}
      a@un() -> {res += (`a).hashCode() + "\n";}
      p@plus(a,b) -> {
        res += (`p).hashCode() + "->" + (`a).hashCode() + "\n";
        res += (`p).hashCode() + "->" + (`b).hashCode() + "\n";
        res += represente_aux_hash(`a);
        res += represente_aux_hash(`b);
      }
      m@mult(a,b) -> {
        res += (`m).hashCode() + "->" + (`a).hashCode() + "\n";
        res += (`m).hashCode() + "->" + (`b).hashCode() + "\n";
        res += represente_aux_hash(`a);
        res += represente_aux_hash(`b);
      }
    }
    return res;
  }
  
  public static void representeHash(Expr e, String chemin){
    String res = "digraph mon_graphe {\n";
    res += represente_aux_hash(e);
    res += "}\n";
    File fichier = new File(chemin);
    try{
      FileOutputStream graveur = new FileOutputStream(fichier);
      graveur.write(res.getBytes());
      graveur.close(); 
    } catch (java.io.IOException err){
      System.out.println("ecriture fichier impossible");
    }    
  }
  
  %strategy SeeStrategy() extends Fail(){
    visit Strategy {
      Identity() -> {System.out.println("ID");}
      Make_plus(_,_) -> {System.out.println("PLUS");}
    }
  }
  
  public static void representeStrategy(Strategy s){
    try {
      `TopDown(SeeStrategy()).visit(s);
    } catch (VisitFailure e){
      System.out.println("erreur de representation de la strategie");
    }
  }
  
}
