import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import java.util.*;
import java.io.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.Identity;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;


public class Menu {

  %include{mutraveler.tom}
  %include{adt/tnode/TNode.tom}

  %typeterm Position {
    implement {tom.library.strategy.mutraveler.Position}
  }

  private XmlTools xtools;
  private Tools tools;
  private TNode menu;
  private Collection globalLeaves = new HashSet();
  private String globalS2Link = "";
  private Hashtable menus;

  public Menu(TNode menu,Tools tools,XmlTools xtools){
    this.menu = menu;
    this.tools = tools;
    this.xtools = xtools;
    menus = new Hashtable();

    VisitableVisitor duplicateLinks = `DuplicateLinks();
    VisitableVisitor addSubsectionsTag = `AddSubsectionsTag();
    try {
      menu = (TNode)MuTraveler.init(`BottomUp(RepeatId(duplicateLinks))).visit(menu);
      menu = (TNode)MuTraveler.init(`BottomUp(addSubsectionsTag)).visit(menu);

      try {
        VisitableVisitor findLeaves = `FindLeaves();
        MuTraveler.init(`BottomUp(findLeaves)).visit(menu);
      } catch (VisitFailure e) {
        System.out.println("Failed to get leaves" + menu);
      }

      Iterator it = globalLeaves.iterator();
      while(it.hasNext()) {
        Position p = (Position)it.next();

        VisitableVisitor s1 = `S1();
        VisitableVisitor s2 = `S2();
        VisitableVisitor eqPos = `EqPos(p);
        VisitableVisitor subPos = `SubPos(p);

        VisitableVisitor xmastree = `mu(MuVar("x"),
            All(IfThenElse(eqPos,s2,IfThenElse(subPos,MuVar("x"),s1))));

        try {
          TNode output = (TNode)MuTraveler.init(xmastree).visit(menu);
          TNode tmp = switchLang(globalS2Link);
          TNodeList outList = `concTNode(output,tmp);

          writeMenu(Translator.IN_ENGLISH,globalS2Link,outList);
          writeMenu(Translator.IN_FRENCH,globalS2Link,outList);

        } catch (VisitFailure e) {
          System.out.println("reduction failed on: " + menu);
        }
      }

    } catch (VisitFailure e) {
      System.err.println("reduction failed");
    }    
  }

  public TNodeList getContent(String key) {
    return (TNodeList)menus.get(key);
  }

  /**
   * Build a menu given its language and its keyName, put it in the hastable
   */
  private void writeMenu(String lang, String link, TNodeList outList) throws VisitFailure {
    VisitableVisitor ruleId = `RewriteSystemId(lang);
    TNodeList outList2 = (TNodeList)MuTraveler.init(`BottomUp(ruleId)).visit(outList);
    menus.put(link+"_"+lang,outList2);
  }

  /**
   * Generate the fr :: en link
   */
  private TNode switchLang(String menuKey) {
    String frLnk = menuKey+"_"+Translator.IN_FRENCH+".html";
    String fr = Translator.IN_FRENCH;
    String enLnk = menuKey+"_"+Translator.IN_ENGLISH+".html";
    String en = Translator.IN_ENGLISH;
    return `xml(<p><a href=frLnk>#TEXT(fr)</a>#TEXT(" :: ")<a href=enLnk>#TEXT(en)</a></p>);
  }

  /*********************************************************************************************************************/

  /**
   * Put the section link name in all its subnode child
   */
  %strategy DuplicateLinks() extends `Identity() {

    visit TNode {
      <section>(titles*,link@<link>#TEXT(ln)</link>,a*,<section>(sub1*,anc@<anchor></anchor>,sub2*)</section>,b*)</section> -> {
        if(!hasLink(`sub2)) {
          return `xml(<section>titles* link a*<section>sub1* anc <link>#TEXT(ln)</link> sub2*</section>b*</section>);
        }
      }
      <menu>(a*,<root>#TEXT(ln)</root>,b*,<section>(sub1*,anc@<anchor></anchor>,sub2*)</section>,c*)</menu> -> {
        if(!hasLink(`sub2)) {
          return `xml(<menu>a* <root>#TEXT(ln)</root> b* <section>sub1* anc <link>#TEXT(ln)</link> sub2*</section> c*</menu>);
        }
      }
    }
  }

  /**
   * Root subsections by a subsections tag
   */
  %strategy AddSubsectionsTag() extends `Identity() {

    visit TNode {
      <section>(titles*,link@<link>#TEXT(ln)</link>,sub*)</section> -> {
        if(hasSectionTag(`sub)) {
          return `xml(<section>titles* link <sections>sub*</sections></section>);
        }
      }
    }
  }

  private boolean hasLink(TNodeList sub) {
    %match(TNodeList sub) {
      (_*,<link></link>,_*) -> { 
        return true;
      }
    }
    return false;
  }

  private boolean hasSectionTag(TNodeList sub) {
    %match(TNodeList sub) {
      (_*,<section></section>,_*) -> { 
        return true;
      }
    }
    return false;
  }

  /*********************************************************************************************************************/

  /**
   * Translate XML into xHTML
   */
  %strategy RewriteSystemId(lang:String) extends `Identity() {

    visit TNode {
      <section>tag@<(title_fr|title_en)>title</(title_fr|title_en)>s*<link>#TEXT(ln)</link>sub*</section> -> {
        if((`tag.getName()).endsWith(lang)&&!hasAnchor(`s)) {
          String link = `ln+"_"+lang+".html";
          return `xml(<li><a href=link>title</a>sub*</li>);
        }
      }
      <section>tag@<(title_fr|title_en)>title</(title_fr|title_en)><anchor>#TEXT(an)</anchor><link>#TEXT(ln)</link>sub*</section> -> {
        if((`tag.getName()).endsWith(lang)) {
          String link = `ln+"_"+lang+".html"+`an;
          return `xml(<li><a href=link>title</a>sub*</li>);
        }
      }
      <sections>(sub*)</sections> -> {
        return `xml(<ul>sub*</ul>);
      }
      <root></root> -> {
        return `xml(#TEXT(""));
      }
    }

    visit TNodeList {
      (_*,<menu>(sub*)</menu>,footer*) -> {
        TNode logo = `xml(<p><a href="http://www.loria.fr/"><img src="images/loria.gif" alt="LORIA" /></a></p>);
        TNode list = `xml(<ul>sub*</ul>);
        TNodeList res = `concTNode(logo,list);
        return `concTNode(res*,footer*);
      }
    }
  }

  /*********************************************************************************************************************/

  /**
   * Prune useless subtrees
   */
  %strategy S1() extends `Identity() {

    visit TNode {
      <section>(titles*,link@<link>#TEXT(ln)</link>,sub*)</section> -> {
        return `xml(<section>titles* link</section>);
      }
    }
  }

  /**
   * Extract the link reference related to the leaf
   */
  %strategy S2() extends `Identity() {

    visit TNode {
      <section>(t*,l@<anchor>#TEXT(ln)</anchor>,sub*)</section> -> {
        VisitableVisitor s1 = `S1();
        TNodeList tList = (TNodeList)MuTraveler.init(`BottomUp(s1)).visit(`sub);
        globalS2Link = `ln;
        return `xml(<section>t* l tList*</section>);
      }
      <section>(t*,l@<link>#TEXT(ln)</link>,sub*)</section> -> {
        if(!hasAnchor(`t)) {
          VisitableVisitor s1 = `S1();
          TNodeList tList = (TNodeList)MuTraveler.init(`BottomUp(s1)).visit(`sub);
          globalS2Link = `ln;
          return `xml(<section>t* l tList*</section>);
        }
      }
    }
  }

  private boolean hasAnchor(TNodeList sub) {
    %match(TNodeList sub) {
      (_*,<anchor></anchor>,_*) -> { 
        return true;
      }
    }
    return false;
  }

  /**
   * Collect leaves (menu entries)
   */
  %strategy FindLeaves() extends `Identity() { 

    visit TNode {
      <section></section> -> { globalLeaves.add(MuTraveler.getPosition(this));}
    }
  }

  /*********************************************************************************************************************/

  %strategy EqPos(p:Position) extends `Fail() {

    visit TNode {
      arg -> {
        if (MuTraveler.getPosition(this).equals(p)) {
          return `arg;
        }
      }
    }
    visit TNodeList {
      arg -> {
        if (MuTraveler.getPosition(this).equals(p)) {
          return `arg;
        } 
      }
    }
  }

  %strategy SubPos(p:Position) extends `Fail() {

    visit TNode {
      arg -> {
        if (MuTraveler.getPosition(this).isPrefix(p)) {
          return `arg;
        } 
      }
    }
    visit TNodeList {
      arg -> {
        if (MuTraveler.getPosition(this).isPrefix(p)) {
          return `arg;
        } 
      }
    }
  }
}

