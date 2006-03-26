import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import aterm.*;
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

  private XmlTools xtools;
  private Tools tools;
  private TNode menu;

  private Hashtable menus;

  private TNodeFactory getTNodeFactory() {
    return xtools.getTNodeFactory();
  }

  public Menu(TNode menu,Tools tools,XmlTools xtools){
    this.menu = menu;
    this.tools = tools;
    this.xtools = xtools;
    menus = new Hashtable();
    
    VisitableVisitor duplicateLinks = new DuplicateLinks();
    VisitableVisitor addSubsectionsTag = new AddSubsectionsTag();
    try {
      menu = (TNode)MuTraveler.init(`BottomUp(RepeatId(duplicateLinks))).visit(menu);
      menu = (TNode)MuTraveler.init(`BottomUp(addSubsectionsTag)).visit(menu);

      // Find all leaf nodes
      Collection leaves = new HashSet();
      try {
        VisitableVisitor getleaves = new FindLeaves();
        MuTraveler.init(`BottomUp(getleaves)).visit(menu);
        leaves = ((FindLeaves)getleaves).getLeaves();
      } catch (VisitFailure e) {
        System.out.println("Failed to get leaves" + menu);
      }
      
      Iterator it = leaves.iterator();
      while(it.hasNext()) {
        Position p = (Position)it.next();
        
        VisitableVisitor s1 = new S1();
        S2 s2 = new S2();
        VisitableVisitor eqPos = new EqPos(p);
        VisitableVisitor subPos = new SubPos(p);

        VisitableVisitor xmastree = `mu(MuVar("x"),
                                        All(IfThenElse(eqPos,s2,IfThenElse(subPos,MuVar("x"),s1))));
        
        try {
          TNode output = (TNode)MuTraveler.init(xmastree).visit(menu);
          TNode tmp = switchLang(s2.getLink());
          TNodeList outList = `concTNode(output,tmp);
          
          writeMenu(Translator.IN_ENGLISH,s2.getLink(),outList);
          writeMenu(Translator.IN_FRENCH,s2.getLink(),outList);
     
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
   * Build a menu given his langage and is keyName, put it in the hastable
   */
  private void writeMenu(String lang, String link, TNodeList outList) throws VisitFailure {
    VisitableVisitor ruleId = new RewriteSystemId(lang);
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
  class DuplicateLinks extends TNodeVisitableFwd {
    public DuplicateLinks() {
      super(`Identity());
    }  
    public TNode visit_TNode(TNode arg) throws VisitFailure {
      %match(TNode arg) {
        <section>(titles*,link@<link>#TEXT(ln)</link>,a*,<section>(sub1*,anc@<anchor></anchor>,sub2*)</section>,b*)</section> -> {
          if(!hasLink(`sub2)) {
            return arg = `xml(<section>titles* link a*<section>sub1* anc <link>#TEXT(ln)</link> sub2*</section>b*</section>);
          }
        }
        <menu>(a*,<root>#TEXT(ln)</root>,b*,<section>(sub1*,anc@<anchor></anchor>,sub2*)</section>,c*)</menu> -> {
          if(!hasLink(`sub2)) {
            return arg = `xml(<menu>a* <root>#TEXT(ln)</root> b* <section>sub1* anc <link>#TEXT(ln)</link> sub2*</section> c*</menu>);
          }
        }
      }
      return arg;
    }
  }

  /**
   * Root subsections by a subsections tag
   */
  class AddSubsectionsTag extends TNodeVisitableFwd {
    public AddSubsectionsTag() {
      super(`Identity());
    }  
    public TNode visit_TNode(TNode arg) throws VisitFailure {
      %match(TNode arg) {
        <section>(titles*,link@<link>#TEXT(ln)</link>,sub*)</section> -> {
          if(hasSectionTag(`sub)) {
            return arg = `xml(<section>titles* link <sections>sub*</sections></section>);
          }
        }
      }
      return arg;
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
  class RewriteSystemId extends TNodeVisitableFwd {
    private String lang;
    public RewriteSystemId(String lang) {
      super(`Identity());
      this.lang = lang;
    }  
    public TNode visit_TNode(TNode arg) throws VisitFailure {
      %match(TNode arg) {
        <section>tag@<(title_fr|title_en)>title</(title_fr|title_en)>s*<link>#TEXT(ln)</link>sub*</section> -> {
          if((`tag.getName()).endsWith(lang)&&!hasAnchor(`s)) {
            String link = `ln+"_"+lang+".html";
            return arg = `xml(<li><a href=link>title</a>sub*</li>);
          }
        }
        <section>tag@<(title_fr|title_en)>title</(title_fr|title_en)><anchor>#TEXT(an)</anchor><link>#TEXT(ln)</link>sub*</section> -> {
          if((`tag.getName()).endsWith(lang)) {
            String link = `ln+"_"+lang+".html"+`an;
            return arg = `xml(<li><a href=link>title</a>sub*</li>);
          }
        }
        <sections>(sub*)</sections> -> {
          return arg = `xml(<ul>sub*</ul>);
        }
        <root></root> -> {
          return arg = `xml(#TEXT(""));
        }
      }
      return arg;
    }
    public TNodeList visit_TNodeList(TNodeList arg) throws VisitFailure {
      %match(TNodeList arg) {
        (_*,<menu>(sub*)</menu>,footer*) -> {
          TNode logo = `xml(<p><a href="http://www.loria.fr/"><img src="images/loria.gif" alt="LORIA" /></a></p>);
          TNode list = `xml(<ul>sub*</ul>);
          TNodeList res = `concTNode(logo,list);
          return `concTNode(res*,footer*);
        }
      }
      return arg;
    }
  }

  /*********************************************************************************************************************/

  /**
   * Prune the sub-trees that we don't need
   */
  class S1 extends TNodeVisitableFwd {
    public S1() {
      super(`Identity());
    }
    public TNode visit_TNode(TNode arg) throws VisitFailure { 
      %match(TNode arg) {
        <section>(titles*,link@<link>#TEXT(ln)</link>,sub*)</section> -> {
          return `xml(<section>titles* link</section>);
        }
      }
      return arg;
    }
  }

  /**
   * Extract the link reference related to the leaf
   */
  class S2 extends TNodeVisitableFwd {
    private String link;
    public S2() {
      super(`Identity());
      link="";
    }
    public String getLink() {
      return link;
    }
    public TNode visit_TNode(TNode arg) throws VisitFailure { 
      MuTraveler.getPosition(this);
      %match(TNode arg) {
        <section>(t*,l@<anchor>#TEXT(ln)</anchor>,sub*)</section> -> {
          VisitableVisitor s1 = new S1();
          TNodeList tList = (TNodeList)MuTraveler.init(`BottomUp(s1)).visit(`sub);
          link = `ln;
          return `xml(<section>t* l tList*</section>);
        }
        <section>(t*,l@<link>#TEXT(ln)</link>,sub*)</section> -> {
          if(!hasAnchor(`t)) {
            VisitableVisitor s1 = new S1();
            TNodeList tList = (TNodeList)MuTraveler.init(`BottomUp(s1)).visit(`sub);
            link = `ln;
            return `xml(<section>t* l tList*</section>);
          }
        }
      }
      return arg;
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
   * Collect leafs (menu entries)
   */
  class FindLeaves extends TNodeVisitableFwd {
    Collection bag;
    public FindLeaves() {
      super(`Identity());
      bag = new HashSet();
    }
    public Collection getLeaves() {
      return bag;
    }
    public TNode visit_TNode(TNode arg) throws VisitFailure { 
      %match(TNode arg) {
        <section></section> -> { bag.add(MuTraveler.getPosition(this));}
      }
      return arg;
    }
  }

  /*********************************************************************************************************************/

  class EqPos extends TNodeVisitableFwd {
    Position p;
    public EqPos(Position p) {
      super(`Fail());
      this.p = p;
    }
    public TNode visit_TNode(TNode arg) throws VisitFailure { 
      if (MuTraveler.getPosition(this).equals(p)) {
        return arg;
      } else {
        return (TNode)`Fail().visit(arg);
      }
    }
    public TNodeList visit_TNodeList(TNodeList arg) throws VisitFailure { 
      if (MuTraveler.getPosition(this).equals(p)) {
        return arg;
      } else {
        return (TNodeList)`Fail().visit(arg);
      }
    }
  }
  class SubPos extends TNodeVisitableFwd {
    Position p;
    public SubPos(Position p) {
      super(`Fail());
      this.p = p;
    }
    public TNode visit_TNode(TNode arg) throws VisitFailure { 
      if (MuTraveler.getPosition(this).isPrefix(p)) {
        return arg;
      } else {
        return (TNode)`Fail().visit(arg);
      }
    }
    public TNodeList visit_TNodeList(TNodeList arg) throws VisitFailure { 
      if (MuTraveler.getPosition(this).isPrefix(p)) {
        return arg;
      } else {
        return (TNodeList)`Fail().visit(arg);
      }
    }
  }

}
