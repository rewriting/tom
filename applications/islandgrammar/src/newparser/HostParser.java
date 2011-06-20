/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2011, INPL, INRIA
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Gregoire Brenon, Nabil Dhiba, and Hichem Mokrani (Mines de Nancy)
 *
 **/

package newparser;

import org.antlr.runtime.tree.*;
import org.antlr.runtime.*;

  /** 
   * <p>This class aims at switching between the various parsers by looking
   * for keywords.</p> <p>The HostParser can be used in several ways depending
   * on when you want the parsing to stop. In fact, there are three
   * constructors.  You can have HostParser stop when reaching the EOF. You can
   * alternatively set a custom 'EOF' (second constructor). You can also use
   * HostParser and start counting the levels of each block by using an
   * OpenToken and CloseToken.</p>
   */

public class HostParser {

  /*
   * Definition of the keyword classes used in the rest
   */

  /** 
   * This is a special Keyword to watch something but do nothing. It is useful
   * to count pattern levels (see DoubleKeyword).
   */
  public class Watcher extends Keyword {
    public Watcher(String pattern) {
      this.pattern = pattern;
    }

    public void action() {}
  } //Watcher

  /** 
   * This keyword doesn't trigger any parser but instead make the HostParser
   * stop.
   */
  public class ExitKeyword extends Keyword {
    public ExitKeyword() {
      this.pattern = String.valueOf((char) CharStream.EOF);
    }
    public ExitKeyword(String pattern) {
      this.pattern = pattern;
    }
    protected void action() throws org.antlr.runtime.RecognitionException {
      packHostContent();
      alive = false;
      hostContent.setLength(0);
    }
  } //ExitKeyword

  /** The 'match' keyword to look for Tom's %match.*/
  public class MatchConstruct extends Keyword {
    public MatchConstruct() {
      this.pattern = "%match";
    }
    protected void action() throws org.antlr.runtime.RecognitionException {
      packHostContent();
      miniTomLexer lexer = new miniTomLexer(input);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      miniTomParser parser = new miniTomParser(tokens);
      arbre.addChild((Tree) parser.matchconstruct().getTree());
    }
  } //MatchConstruct

  /** The 'typeterm' keyword to look for Tom's %typeterm.*/
  public class TypeTermConstruct extends Keyword {
    public TypeTermConstruct() {
      this.pattern = "%typeterm";
    }
    protected void action() throws org.antlr.runtime.RecognitionException {
      packHostContent();
      miniTomLexer lexer = new miniTomLexer(input);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      miniTomParser parser = new miniTomParser(tokens);
      arbre.addChild((Tree) parser.typetermconstruct().getTree());
    }
  } //TypeTermConstruct

  /** 
   * A special keyword that 'eats' the inside of a comment to make it disappear
   * from the host content (preventing HostParser to see commented constructs
   * like %match).
   */
  public class Comment extends Keyword {
    public Comment() {
      this.pattern = "/*";
    }
    protected void action() throws org.antlr.runtime.RecognitionException {
      savedContent.setLength(0);
      boolean found = false;
      while(!found) {
        if((char) input.LA(1) == '*' ) {
          found = (char) input.LA(2) == '/';
        }
        input.consume();
      }
      input.consume();
    }
  } //Comment

  /** 
   * The same as Comment, but acting on one line comments (thus skipping line
   * like "// %typeterm").
   */
  public class OLComment extends Keyword {
    public OLComment() {
      this.pattern = "//";
    }
    protected void action() throws org.antlr.runtime.RecognitionException {
      savedContent.setLength(0);
      while((char) input.LA(1) != '\n') {
        input.consume();
      }
      input.consume();
    }
  } //OLComment

  /** 
   * This class extends the mechanism provided by the comment Keywords to the
   * host content between double quotes, like what is interpreted as strings in
   * Java and most of languages (so that writing "%match" inside the host
   * content - with the characters " - won't trigger the MatchConstruct
   * keyword).
   */
  public class HostString extends Keyword {
    public HostString() {
      this.pattern = "\"";
    }
    protected void action() throws org.antlr.runtime.RecognitionException {
      while((char) input.LA(1) != '"') {
        savedContent.append((char) input.LA(1));
        input.consume();
      }
      savedContent.append((char) input.LA(1));
      input.consume();
    }
  } //HostString

  /** Same as HostString, but with the simple quotes.*/
  public class HostCharacter extends Keyword {
    public HostCharacter() {
      this.pattern = "'";
    }
    protected void action() throws org.antlr.runtime.RecognitionException {
      while((char) input.LA(1) != '\'') {
        savedContent.append((char) input.LA(1));
        input.consume();
      }
      savedContent.append((char) input.LA(1));
      input.consume();
    }
  } //HostCharacter

  /** 
   * This Keyword is a tool to use two Keywords at the same time with a 'level'
   * to count the number of { and } for instance (but it can be used with any
   * pattern you like).
   */
  public class DoubleKeyword extends Keyword {
    private Keyword openKeyword;
    private Keyword closeKeyword;
    private int level = 0;
    protected String pattern = null;

    public DoubleKeyword(Keyword openKeyword, Keyword closeKeyword) {
      this.openKeyword = openKeyword;
      this.closeKeyword = closeKeyword;
    }

    public boolean take(char c) {
      boolean oAnswer = openKeyword.take(c);
      boolean cAnswer = closeKeyword.take(c);
      matched = openKeyword.isReady() || closeKeyword.isReady();
      return oAnswer || cAnswer;
    }

    protected void action() throws org.antlr.runtime.RecognitionException {
      if(openKeyword.isReady()) {
        level++;
        openKeyword.reset();
      } else if (level > 0) {
        level--;
        closeKeyword.reset();
      } else {
        closeKeyword.action();
        closeKeyword.reset();
      }
    }

  } //DoubleKeyword

  /*
   ** Fields definition
   */

  /** The input file that going to be parsed by the program.*/
  private CharStream input;

  /**
   * A flag to control the getTree while loop - set to false to end the
   * parsing.
   */
  private boolean alive;/* whether the getTree() routine should go on or not.*/

  /**
   * A buffer to store characters before deciding whether they're host content
   * or not.
   */
  private StringBuffer savedContent;

  /** 
   * A buffer to store characters that aren't dealt with (the 'host' content).
   */
  private StringBuffer hostContent;

  /** 
   * The tree that we'll be returning to represent the file once the parsing is
   * over.
   */
  private Tree arbre;

  /** This array will contain the Keywords to look for.*/
  private Keyword[] keywords;


  /** 
   * Default constructor to create a HostParser stopping when it finds EOF.
   * @param input a CharStream to read the data from
   */
  public HostParser(CharStream input) {
    this.input = input;
    this.alive = true;
    this.savedContent = new StringBuffer();
    this.hostContent = new StringBuffer();
    this.arbre = new CommonTree(new CommonToken(1,"HostBlock"));
    this.keywords = new Keyword[] {
      (new ExitKeyword()),
        (new MatchConstruct()),
        (new TypeTermConstruct()),
        /*      (new Comment()),
                (new OLComment()),*/
        (new HostString()),
        (new HostCharacter())
    };
  }

  /** 
   * Stops when encountering an arbitrary keyword.
   * @param input a CharStream to read the data from 
   * @param StopToken the keyword that will act as a EOF for this instance of
   * HostParser
   */
  public HostParser(CharStream input, String StopToken) {
    this(input);
    keywords[0] = new ExitKeyword(StopToken);
  }

  /**
   * Stops when encountering an arbitrary keyword at imbrication level 0. Each
   * time OpenToken is encountered the level is incremented, and decremented
   * when CloseToken  is found. This acts as parenthesis matching system.
   * @param input a CharStream to read the data from
   * @param OpenToken the keyword that will act as an open bracket
   * @param StopToken the keyword that will act as a close bracket
   */
  public HostParser(CharStream input, String OpenToken, String StopToken) {
    this(input); keywords[0] = new DoubleKeyword(new Watcher(OpenToken), new
        ExitKeyword(StopToken)); }

  /** 
   * getTree() is the 'main' function of this class : it reads the input and
   * produces the corresponding Tree.
   * @return an ANTLR Tree representing the program described in the input.
   */
  public Tree getTree() {
    while (alive) {
      char read = (char) input.LA(1);
      boolean found = false;
      for(Keyword k : keywords) {
        /* Warning ! Logic is lazy in Java ! had we written 'found ||
           keywords…', keywords left wouldn't be fed 'read' as soon as one of
           them was ok with it.*/
        found =  k.take(read) || found;
      }
      savedContent.append(read);
      input.consume();
      if(!found) {
        /* No keyword has accepted the character 'read'. This means we are no
         * longer recognizing a patten, so what we've found so far is actually
         * host content, and should be stored with the rest in the
         * hostContent.*/
        hostContent.append(savedContent);
        savedContent.setLength(0); /* 'empty' the savedContent buffer by
                                      setting its length to 0.*/
      }
      else {
        /* A keyword can be ready only if it has accepted last character read,
         * so this is the  case only if 'found'.*/
        for(Keyword k : keywords) {
          if(k.isReady()) {
            /* This test should fail for all but at most one keyword.
             * Otherwise, it means that we've triggered several keywords at the
             * same time. This is probably unwanted, as the action triggered
             * will most likely modify the input stream, meaning that the other
             * keyword's action will take place on an unknown context. This
             * will be the case for instance if you have to keywords watching
             * respectively 'abc' and 'bc'. To allow such things, it would be
             * good to allow reg. exp. based patterns (and 'bc' would then
             * become '[^a]bc' and wouldn't conflict with 'abc' anymore).*/
            try {
              k.action();
              k.reset();
            } catch (Exception e) {e.printStackTrace();}
          }
        }
      }
    }
    return arbre;
  }

  /** 
   * A private function, consists in storing the hostContent String on the tree
   * “arbre” as a leaf, by converting it to a Token.
   */
  private void packHostContent() {
    savedContent.setLength(0);
    if(hostContent.length() > 0) {
      CommonToken tokenizedHostContent = new CommonToken(1,("<"+hostContent.toString()+">"));
      CommonTree treedHostContent = new CommonTree(tokenizedHostContent);
      arbre.addChild(treedHostContent);
      hostContent.setLength(0);
    } 
  }

} //HostParesr

