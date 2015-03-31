/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the Inria nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.lang.*;

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

public class Rss extends HttpServlet {
  
  %include{adt/tnode/TNode.tom}
  %include{boolean.tom}

  private XmlTools xtools;

  private TNodeFactory getTNodeFactory() {
    return xtools.getTNodeFactory();
  }

  public void init() {
    xtools = new XmlTools();
    xtools.setDeletingWhiteSpaceNodes(true);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    try{
      TNode rss = (TNode)xtools.convertXMLToATerm(request.getParameter("url"));
      rss = rss.getDocElem();
      System.out.println(rss);
      response.setContentType("text/html; charset=utf-8");
      TNodeList body = `concTNode();
      if(request.getParameter("max").length() == 0) {
        body = viewFilter(out,rss,request.getParameter("filter"));
      } else {
        body = viewFilterPrice(out,rss,request.getParameter("filter"),Float.parseFloat(request.getParameter("max")));
      }
      printPage(out,"Simple RSS viewer",body);
    } catch(Exception e) {
      out.println("Erreur lors de la lecture du flux");
      out.println(e);
    }
    out.close();
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    response.setContentType("text/html; charset=utf-8");
    printPage(out,"Test",`concTNode(#TEXT("Simple RSS viewer - test")));
    out.close();
  }

  private void printPage(PrintWriter out, String title, TNodeList body) {
    printPage(out,title,body,"text/html; charset=utf-8");
  }

  private void printPage(PrintWriter out, String title, TNodeList body, String charset) {
    out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
    // <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
    TNode html = `xml(<html><head><title>#TEXT(title)</title><meta http-equiv="Content-type" content=charset/></head><body>#TEXT("\n") body*</body></html>);
    xtools.writeXMLFileFromATerm(out,html);
  }

  private TNodeList viewFilter(PrintWriter out, TNode rss, String filter) {
    return viewFilterPrice(out,rss,filter,-1);
  }

  private TNodeList viewFilterPrice(PrintWriter out, TNode rss, String filter, float max) {
    TNodeList body = `concTNode();
    %match(TNode rss) {
      <rss><channel><title>title@#TEXT(_)</title></channel></rss> -> {
        body = `concTNode(body*,<h1>title</h1>,#TEXT("\n"));
      }
      <rss><channel><item>elements*</item></channel></rss> -> {
        String link = "";;
        TNode title = `EmptyNode();
        TNode description = `EmptyNode();
        boolean match = false;
        %match(TNodeList elements) {
          (_*,<title>ttl@#TEXT(t)</title>,_*) -> {
            title = `ttl;
            boolean match1 = false;
            boolean match2 = false;
            String tmpTitle = `t.toLowerCase();
            String tmpFilter = filter.toLowerCase();
            %match(String tmpTitle, String tmpFilter) {
              (_*,word*,_*),(word*) -> {
              match1 = true;
              }
            }
            if(max == -1) {
              // no price limit
              match2 = true;
            } else {
              %match(String tmpTitle) {
                (word*,_*) when isPrice(word,true,max) -> {
                  match2 = true;
                }
                (_*,word*,_*) when isPrice(word,false,max) -> {
                  match2 = true;
                }
              }
            }
            match = match1 && match2;
          }
          (_*,<link>#TEXT(l)</link>,_*) -> {
            link = `l;
          }
          (_*,<description>d</description>,_*) -> {
            description = `d;
          }
        }
        if(match) {
          if(link != "") {
            body = `concTNode(body*,<h2><a href=link>title</a></h2>);
          } else {
            body = `concTNode(body*,<h2>title</h2>);
          }
          if(!description.isEmptyNode()) {
            body = `concTNode(body*,#TEXT("\n"),<p>description</p>);
          }
          body = `concTNode(body*,#TEXT("\n"));
        }
      }
    }
    return body;
  }

  private boolean isPrice(String word, boolean begin, float max) {
    boolean result;
    if(begin) {
      result = word.matches("^[0-9][0-9]*([\\.,][0-9]*)?[ ]*(euro[s]?|\\€)");
    } else {
      if(result = word.matches("[^0-9\\.,][0-9][0-9]*([\\.,][0-9]*)?[ ]*(euro[s]?|\\€)")) {
        word = word.substring(1,word.length());
      }
    }
    word = word.trim();
    if(result) {
      //System.out.println("word : "+word);
      float p = -1;
      if(word.indexOf('e') != -1) {
        p = Float.parseFloat(word.substring(0,word.indexOf('e')));
      } else {
        p = Float.parseFloat(word.substring(0,word.indexOf('€')));
      }
      if(p > max) {
        result = false;
      }
    }
    return result;
  }

}
