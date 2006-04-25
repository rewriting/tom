import tom.library.xml.*;

import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import java.util.*;
import java.io.*;

import java.lang.reflect.Array;
import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Identity;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;

public class Publi {

  %include{mutraveler.tom}
  %include{adt/tnode/TNode.tom}

  private final static int firstYearPubli = 1992;

  protected SortedMap authors, alumni, members;
  private TNode bibN, membersN;
  private XmlTools xtools;
  private Tools tools;
  private TNodeFactory getTNodeFactory() {
    return xtools.getTNodeFactory();
  }

  public Publi(TNode membersN,TNode bibN,Tools tools,XmlTools xtools){
    this.tools = tools;
    this.xtools = xtools;
    this.bibN = bibN;
    this.membersN = membersN;
    authors = new TreeMap();
    alumni = new TreeMap();
    members = new TreeMap();
    // Put members in a java structure
    VisitableVisitor getMembers = new GetMembers();
    VisitableVisitor parseBib = new ParseBib();
    try{
      MuTraveler.init(`InnermostId(getMembers)).visit(membersN);//put all members in members.xml to variable 'members'
      MuTraveler.init(`InnermostId(parseBib)).visit(bibN);//parse bib
    } catch (VisitFailure e) {
      System.err.println("Visit failed");
    }
  }

  /**
   * Get publi body page 
   * @param lang the language
   */
  public TNode getContent(String lang){
    TNodeList years = processYears(lang);
    TNodeList authorsList = processArray(authors,lang);
    TNodeList alumniList = processArray(alumni,lang);
    TNodeList result = `concTNode(<h2>#TEXT("Publications")</h2>);
    %match(String lang){
      "fr" -> {
        //result = `concTNode(result*,#TEXT("\n"),<div id="year"><h3>#TEXT("Publications triées par année:")</h3>years*</div>);
        result = `concTNode(result*,#TEXT("\n"),<div id="year"><h3>#TEXT("Publications tri\u00E9es par ann\u00E9e:")</h3>years*</div>);
        //result = `concTNode(result*,#TEXT("\n"),<div id="author"><h3>#TEXT("Publications triées par auteur:")</h3>authorsList*</div>);
        result = `concTNode(result*,#TEXT("\n"),<div id="author"><h3>#TEXT("Publications tri\u00E9es par auteur:")</h3>authorsList*</div>);
        result = `concTNode(result*,#TEXT("\n"),<div id="alumni"><h3>#TEXT("Anciens membres:")</h3>alumniList*</div>);
      }
      "en" -> {
        result = `concTNode(result*,#TEXT("\n"),<div id="year"><h3>#TEXT("Publications sorted by year:")</h3>years*</div>);
        result = `concTNode(result*,#TEXT("\n"),<div id="author"><h3>#TEXT("Publications sorted by author:")</h3>authorsList*</div>);
        result = `concTNode(result*,#TEXT("\n"),<div id="alumni"><h3>#TEXT("Former members:")</h3>alumniList*</div>);
      }
    }
    return `xml(<div id="content">result*</div>);
  }

  /**
   * Return HTML code for years and for each year write a HTML page
   */
  private TNodeList processYears(String lang){
    Calendar rightNow = Calendar.getInstance();
    int currentYear = rightNow.get(Calendar.YEAR);
    TNodeList result = `concTNode();
    String ref,year;
    for(int i=firstYearPubli ; i<=currentYear ; i++) {
      ref = Mk.bibDir + i + ".html";
      year = String.valueOf(i) + " ";
      result = `concTNode(result*,#TEXT("\n"),<a href=ref>#TEXT(year)</a>);
      // New line every 4 dates printed
      if(((i-firstYearPubli-1) % 4) == 0) {
        result = `concTNode(result*,<br />);
      }
      // Generates bibfile only once
      if (lang.equals(Translator.IN_ENGLISH)){
        bib2html(String.valueOf(i),"-c 'year="+ String.valueOf(i)+ "'");
      }
    }
    return result;
  }

  /**
   * Return HTML code for authors and for each author write a HTML page
   */
  private TNodeList processArray(SortedMap authorsList,String lang){
    Collection values = authorsList.values();
    Iterator i = values.iterator();
    TNodeList result = `concTNode();
    Author author;
    String first,last,id,ref;
    while(i.hasNext()) {
      author = (Author)i.next();
      first = author.getFirstname();
      last = author.getLastname();
      id = first + "." + last;
      id = tools.removeAccents(id);
      ref = Mk.bibDir + id + ".html";
      result = `concTNode(result*,#TEXT("\n"),<a href=ref>#TEXT(first) #TEXT(" ") #TEXT(last)</a>,<br />);
      // Generates bibfile only once
      if (lang.equals(Translator.IN_ENGLISH)){
        bib2html(id,"-c 'author:\""+ last + ", " + first + "\"'");
      }
    }
    return result;
  }

  private void bib2html(String id, String condition){
    Runtime run = Runtime.getRuntime();
      StringBuffer body = new StringBuffer();//patch for -encoding
    try {
      String[] bib2bib = {"/bin/sh", "-c","bib2bib -oc " + Mk.tmpDir + "cite" + id +" -ob " + Mk.tmpDir + id +".bib " + condition + " " + Mk.bibDir + "complete.bib"};
      //System.out.println(bib2bib[2]);
      Process proc = run.exec(bib2bib);
      proc.waitFor();

      //test if citefile is empty
      File cite = new File(Mk.tmpDir + "cite" + id);
      if (!(cite.length()==0)){
        //String[] bibtex2html = {"/bin/sh", "-c","bibtex2html -t \"Publications::" + id.replace('.',' ') + "\" -css biblio.css -o " + Mk.w3Dir +  Mk.bibDir + id + " -citefile " + Mk.tmpDir + "cite" + id + " " + Mk.tmpDir + id +".bib"};
        String[] bibtex2html = {"/bin/sh", "-c","bibtex2html -nodoc -o " + Mk.w3Dir +  "biblio/" + id + " -citefile " + Mk.tmpDir + "cite" + id + " " + Mk.tmpDir + id +".bib"};
        //System.out.println(bibtex2html[2]);
        proc = run.exec(bibtex2html);
        proc.waitFor();

        /*patch en attendant d'avoir -encoding dans bibtex2html */
        InputStreamReader reader = new InputStreamReader(new FileInputStream(Mk.w3Dir +  "biblio/" + id +".html"),"UTF-8");
        int i = 0;
        while(i != -1) {
          i = reader.read();
          body.append((char)i);
        }
        reader.close();
      }

      OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(Mk.w3Dir +  "biblio/" + id + ".html"),"UTF-8");
      writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xml:lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\"> <head>  <meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\"/>   <link href=\"protheo.css\" rel=\"stylesheet\" title=\"Classic\" type=\"text/css\"/>  <title>Protheo::publi</title> </head> <body>  <h2>" + id.replace('.',' ') + "</h2>" + body.toString() + "</body></html>");
      writer.close();
      /*********************************************************/
    } catch(Exception e) {
      System.err.println("An error occurs when executing bib2html.");
      e.printStackTrace();
    }
  }

  class ParseBib extends TNodeVisitableFwd {
    public ParseBib() {
      super(`Identity());
    }  
    
    public TNode visit_TNode(TNode arg) throws VisitFailure {
      %match(TNode arg) {
        <author>#TEXT(a)</author> -> {
          // Must be surrouded by blanks (don't split name 'Brand' for instance)
          String authArray[] = `a.split(" and ");
          // For each author
          String last,first;
          for (int i=0 ; i<(Array.getLength(authArray)) ; i++) {
            String current_a[] = authArray[i].split(",");
            last = current_a[0].trim();
            first = "";
            // If firstname not empty
            if (Array.getLength(current_a) > 1){
              first = current_a[1].trim();
            }
            // If author belongs to members.xml
            if(members.containsKey(last + first)) {
              boolean isAlumni = ((Author)members.get(last + first)).getStatus().equalsIgnoreCase("Ancien Membre");
              //check is author already added
              if (isAlumni){
                // Alumni
                if(!alumni.containsKey(last + first)) {
                  Author auth = new Author(first,last);
                  alumni.put(last + first,auth);
                }
              }
              else{
                // Author
                if(!authors.containsKey(last + first)) {
                  Author auth = new Author(first,last);
                  authors.put(last + first,auth);
                }
              }
            } else {
              //System.err.println("Bad author : " + last + " " + first);
            }
          }
        }
      }
      return arg;
    }
  }
  
  class GetMembers extends TNodeVisitableFwd {
    public GetMembers() {
      super(`Identity());
    }
    public TNode visit_TNode(TNode arg) throws VisitFailure {
      %match(TNode arg) {
        <person><firstname>#TEXT(fn)</firstname><lastname>#TEXT(ln)</lastname><status>#TEXT(s)</status></person> -> {
          members.put(`ln + `fn, new Author(`fn,`ln,`s));
        }
      }  
      return arg;
    }
  }

}
