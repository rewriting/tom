/*
 * Created on 19 janv. 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.parsers.*;


/**
 * @author bertrand.tavernier
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class domtomTest2 {

  %include{dom.tom}

		
  private Document dom;

  public void run() {
    try {		
      System.out.println("Debut des tests ");
        /*Tests sur le document*/
      System.out.println("\nTest de nouveau document :");
      dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      dom.appendChild(dom.createElement("Test"));
      Node root = null;
      %match(TDocument dom) {
        DocumentNode[docElem=r] -> {
          root = r;
        }
      }
      System.out.println("ElementNode : "+root);

        /*Creation d'un document XML :*/
      System.out.println("\nTest de creation : ");


      root.appendChild(`xml(dom,<Fils1 At1="V1" Bt1="V1"> 
                            <SousFils><SousousFils></SousousFils></SousFils>
                            <SousFils2></SousFils2>
                            </Fils1>));

      /*
      root.appendChild(`xml(dom,<Fils1 At1="V2"> 
                            <SousFils><SousousFils></SousousFils></SousFils>
                            <SousFils2></SousFils2>
                            </Fils1>));

      root.appendChild(`xml(dom,<Fils1 At1="V1"> 
                            <SousFils><SousousFils></SousousFils></SousFils>
                            <SousFils2></SousFils2>
                            </Fils1>));
      System.out.println(root);
      */
      /*Matching du document
      System.out.println("\nTest de matching : ");
      %match(TNode root) {
        <_><Fils1 At1=val></Fils1></_> -> {
           System.out.println("Attribut At1 trouve, valeur : "+val);
         }
      }

      System.out.println("\nTest de listmatching avec non-linearite");
      %match(TNode root) {
        <_><_ At1=val></_><_ At1=val></_></_> -> {
           System.out.println("Pattern non lineaire matche avec : "+val);
         }
      }
      */
      System.out.println("\nTest de l'ordre des attributs");
      %match(TNode root) {
              <_><Fils1 Bt1=val At1=val  ></Fils1></_> -> {
             /*<_><Fils1 At1=val Bt1=val  ></Fils1></_> -> {*/
           System.out.println("ordre ok : "+val);
         }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new domtomTest2().run();
		
  }


}
