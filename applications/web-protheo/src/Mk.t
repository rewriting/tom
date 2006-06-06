import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import aterm.*;
import java.util.*;
import java.io.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Identity;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;

public class Mk {

  %include{adt/tnode/TNode.tom}
  %include{mutraveler.tom}

  public static String contentDir;
  public static String w3Dir;
  public static String bibDir;
  public static String tmpDir;

  private Tools tools;
  private static final XmlTools xtools = new XmlTools();

  private static Menu menu;
  private static Members members;
  private static Publi publi;

  private static TNode skeleton;

  public static void main (String args[]) {
    Mk mk = new Mk();
  }

  public Mk() {
    tools = new Tools();

    TNode conf = xtools.convertXMLToTNode("config.xml");
    if(!(conf == null)) {
      conf = tools.removeComments(conf.getDocElem());
      %match(TNode conf) {
        <conf><contentDir>#TEXT(d)</contentDir></conf> -> { contentDir = `d; }
        <conf><w3Dir>#TEXT(d)</w3Dir></conf> -> { w3Dir = `d; }
        <conf><bibDir>#TEXT(d)</bibDir></conf> -> { bibDir = `d; }
        <conf><tmpDir>#TEXT(d)</tmpDir></conf> -> { tmpDir = `d; }
      }
    } else {
      System.err.println("config.xml doesn't exists.");
      System.exit(1);
    }

    // Removes first tag <!DOCTYPE...> as well.
    TNode mn = xtools.convertXMLToTNode(contentDir +"menu.xml");
    if(!(mn == null)) {
      mn = tools.removeComments(mn.getDocElem());
    } else {
      System.err.println(contentDir +"menu.xml doesn't exists.");
      System.exit(1);
    }
    TNode mb = xtools.convertXMLToTNode(contentDir + "members.xml");
    if(!(mb == null)) {
      mb = tools.removeComments(mb.getDocElem());
    } else {
      System.err.println(contentDir +"members.xml doesn't exists.");
      System.exit(1);
    }
    TNode bib = xtools.convertXMLToTNode(bibDir + "complete.xml");
    if(!(bib == null)) {
      bib = tools.removeComments(bib.getDocElem());
    } else {
      System.err.println(contentDir +"complete.xml doesn't exists.");
      System.exit(1);
    }

    skeleton = xtools.convertXMLToTNode(contentDir + "skeleton.html");
    if(skeleton == null) {
      System.err.println(contentDir +"skeleton.xml doesn't exists.");
      System.exit(1);
    }

    // Tests if all authors are well patched
    // Patch patch = new Patch(mb,bib);

    menu = new Menu(mn,tools,xtools);
    publi = new Publi(mb,bib,tools,xtools);
    members = new Members(mb,xtools);

    VisitableVisitor genHTML = `GenHTML();

    try{
      MuTraveler.init(`BottomUp(genHTML)).visit(`mn);
    } catch (VisitFailure e){
      System.out.println("VisitFailure");
    }
  }

  /*********************************************************************************************************************/
  /**
   * For each link in the menu, write its corresponding HTML page if the link is a standalone page
   */
  %strategy GenHTML() extends `Identity(){
    visit TNode{
      <link>#TEXT(lk)</link> -> {
        writeHTML(`lk,Translator.IN_ENGLISH);
        writeHTML(`lk,Translator.IN_FRENCH);
      }
    }
  }

  public static void writeHTML(String link, String lang) {
    TNode content;
    TNodeList menuHTML = menu.getContent(link+"_"+lang);
    if(menuHTML == null) {
      throw new RuntimeException("Pas de menu pour : "+link+"_"+lang);
    }
    try{    
      // Get HTML page content
      if(link.equals("members")) {
        content = members.getContent(lang);
      }
      else if(link.equals("publi")) {
        content = publi.getContent(lang);
      }
      // If french file do not exist
      else if((lang == Translator.IN_FRENCH) && (!new File(contentDir + link + "_fr.body").exists())) {
        content = xtools.convertXMLToTNode(contentDir + link + "_" + Translator.IN_ENGLISH + ".body");
        if(!(content == null)) {
          content = content.getDocElem();
        } else {
          System.err.println(contentDir + link + "_" + lang +".body doesn't exists.");
          content = `xml(#TEXT("empty page"));
        }
      }
      // General case
      else {
        content = xtools.convertXMLToTNode(contentDir + link + "_" + lang + ".body");
        if(!(content == null)) {
          content = content.getDocElem();
        } else {
          System.err.println(contentDir + link + "_" + lang +".body doesn't exists.");
          content = `xml(#TEXT("empty page"));
        }
      }

      String title;
      if(lang.equals(Translator.IN_FRENCH)) {
        title = "Le projet PROTHEO";
      }
      else{
        title = "The PROTHEO Project Home Page";
      }
      // Build HTML page
      VisitableVisitor ruleId = `IncludeContent(content,menuHTML,link,title);
      TNode p = (TNode)MuTraveler.init(`BottomUp(ruleId)).visit(skeleton);
      // Write HTML page
      OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(w3Dir + link+ "_"+lang+".html"),"UTF-8");
      xtools.writeXMLFileFromTNode(writer,p);
      writer.close();
    }
    catch(Exception e) {
      e.printStackTrace();
      System.err.println("An error occurs when writing " + link + " file.");
    }
  }

  %strategy IncludeContent(content:TNode,menu:TNodeList,link:String,title:String) extends `Identity(){

    visit TNode {
      /* we can't do pattern matching on namespaces yet
         <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">(c*)</html> ->{
         if lang.equals(Translator.FRENCH)
         return `xml(<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr">(c*)</html>);           
         }*/
      <div id="replace_me_title_header"/> ->{
        return `xml(<title>#TEXT("Protheo::") #TEXT(link)</title>);
      }
      <div id="replace_me_title"/> ->{
        return `xml(<h1>#TEXT(title)</h1>);
      }
      <div id="replace_me_menu"/> ->{
        return `xml(<div id="menu">menu*</div>);
      }
      <div id="replace_me_content"/> ->{
        return content;
      }
    }
  }
}
