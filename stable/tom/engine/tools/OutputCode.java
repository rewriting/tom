/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.tools;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import tom.platform.OptionManager;

public class OutputCode {
  protected Writer file;
  private OptionManager optionManager;

  private int lineCounter = 1;
  private int singleLine = 0;

  private boolean pretty = false;
  private boolean indent = false;
  private boolean cCode  = false;
  private boolean aCode  = false;
  private boolean goCode = false;
  /* Go only: the end of the output line being written (see goSingleLine) */
  private String lineTail = "";

  public OutputCode(Writer file, OptionManager optionManager) {
    this.file = file;
    this.optionManager = optionManager;
    this.cCode  = ((Boolean)optionManager.getOptionValue("cCode")).booleanValue();
    this.aCode  = ((Boolean)optionManager.getOptionValue("aCode")).booleanValue();
    this.indent = ((Boolean)optionManager.getOptionValue("pCode")).booleanValue() || this.aCode;
    this.pretty = ((Boolean)optionManager.getOptionValue("pretty")).booleanValue();
    Object go = optionManager.getOptionValue("goCode");
    this.goCode = (go instanceof Boolean) && ((Boolean)go).booleanValue();
  }

  public OutputCode(Writer file) {
    this.file = file;
  }

  public void setSingleLine() {
    this.singleLine++;
  }

  public void unsetSingleLine() {
    this.singleLine--;
  }

  public Writer getFile() {
    return file;
  }

  public void writeSpace() throws IOException {
    file.write(' ');
    track(String.valueOf(' '));
  }
  public void writeOpenBrace() throws IOException {
    file.write('(');
    track(String.valueOf('('));
  }
  public void writeCloseBrace() throws IOException {
    file.write(')');
    track(String.valueOf(')'));
  }
  public void writeComa() throws IOException {
    file.write(',');
    track(String.valueOf(','));
  }
  public void writeSemiColon() throws IOException {
    file.write(';');
    track(String.valueOf(';'));
  }
  public void writeUnderscore() throws IOException {
    file.write('_');
    track(String.valueOf('_'));
  }

  public void write(String s) throws IOException {
    try {
      file.write(s);
      track(s);
    } catch (IOException e) {
      System.out.println("write error");
      e.printStackTrace();
    }
  }

  public void write(int n) throws IOException {
    write(Integer.toString(n));
  }

  public void write(int deep,String s) throws IOException {
    indent(deep);
    write(s);
  }

  public void writeln() throws IOException {
    if(pretty) {
      file.write('\n');
    }
  }

  public void writeln(String s) throws IOException {
    write(s);
    writeln();
  }

  public void writeln(int deep,String s) throws IOException {
    write(deep,s);
    writeln();
  }

  public void write(StringBuilder s) throws IOException {
    write(s.toString());
  }

  public void write(int deep,StringBuilder s) throws IOException {
    write(deep,s.toString());
  }

  public void writeln(StringBuilder s) throws IOException {
    writeln(s.toString());
  }

  public void writeln(int deep,StringBuilder s) throws IOException {
    writeln(deep,s.toString());
  }

  public void write(int deep, StringBuilder s, int line, int length) throws IOException {
    write(deep, s.toString(), line, length);
  }

  private String makeSingleLine(String s) {
    if(goCode) {
      s = goSingleLine(lineTail, s);
    }
    s = s.replace('\n', ' ');
    s = s.replace('\r', ' ');
    s = s.replace('\t', ' ');
    return s;
  }

  private static final java.util.Set<String> GO_KEYWORDS = new java.util.HashSet<String>(java.util.Arrays.asList(
        "case", "chan", "const", "default", "defer", "else", "for", "func", "go",
        "goto", "if", "import", "interface", "map", "package", "range", "select",
        "struct", "switch", "type", "var"));

  /*
   * Go code put on a single line (line synchronisation: host code that
   * cannot be written at its source line, e.g. the action of a %match
   * duplicated for each branch of a disjunction) must keep the semicolons
   * that Go inserts at the end of a line: a newline becomes "; " where the
   * Go lexer would insert one (after an identifier, a literal, one of the
   * keywords break/continue/fallthrough/return, ++, --, or a closing
   * bracket), and a space elsewhere. The first line of s continues the
   * output line written so far (lineTail): e.g. a backquote term ending a
   * Go statement is generated before the host code that follows it.
   */
  static String goSingleLine(String before, String s) {
    StringBuilder res = new StringBuilder();
    String[] lines = s.split("\n", -1);
    for(int i=0;i<lines.length;i++) {
      res.append(lines[i]);
      if(i < lines.length-1) {
        // the first line continues the output line written before s
        String line = (i == 0) ? before + lines[i] : lines[i];
        boolean semicolon = goInsertsSemicolon(line);
        if(semicolon) {
          // an explicit ';' follows (e.g. the host code of a mapping,
          // written for a single line): a second one would be an empty
          // declaration, rejected outside a function body
          for(int j=i+1;j<lines.length;j++) {
            String next = lines[j].trim();
            if(next.length() > 0) {
              semicolon = next.charAt(0) != ';';
              break;
            }
          }
        }
        res.append(semicolon ? "; " : " ");
      }
    }
    return res.toString();
  }

  private void track(String s) {
    if(!goCode) {
      return;
    }
    int nl = s.lastIndexOf('\n');
    String t = (nl >= 0) ? s.substring(nl+1) : lineTail + s;
    lineTail = (t.length() > 256) ? t.substring(t.length()-256) : t;
  }

  private static boolean goInsertsSemicolon(String line) {
    String l = line.replaceFirst("\\s+$", "");
    if(l.length() == 0) {
      return false;
    }
    char c = l.charAt(l.length()-1);
    if(c == ')' || c == ']' || c == '}' || c == '"' || c == '\'' || c == '`') {
      return true;
    }
    if(l.endsWith("++") || l.endsWith("--")) {
      return true;
    }
    if(Character.isLetterOrDigit(c) || c == '_') {
      int i = l.length();
      while(i > 0 && (Character.isLetterOrDigit(l.charAt(i-1)) || l.charAt(i-1) == '_')) {
        i--;
      }
      return !GO_KEYWORDS.contains(l.substring(i));
    }
    return false;
  }

  public void write(int deep, String s, int line, int length) throws IOException {
    if(aCode && pretty) {
      String[] lines = s.split("\n", -1);
      if(lines.length==1) { //one line
        String s2 = s.replaceFirst("^\\s+","");
        if(!s2.equals("")) {
          write(deep, s);
        } else {
          indent = true;
        }
      } else { //several lines
        for(int i=0;i<lines.length-1;i++) {
          String ln = lines[i];
          ln = ln.replaceFirst("^\\s+",""); // removes spaces at the beginning of the line
          writeln(deep, ln);
          indent = true;
        }
        String s2 = lines[lines.length-1].replaceFirst("^\\s+","");
        if(!s2.equals("")) {
          write(deep, s2);
          indent = false;
        } else {
          indent = true;
        }
      }
    } else if(!pretty) {
      if(cCode) {
        String s1 = "\n#line "+line+"\n";
        s = s1+s;
        write(s);
        lineCounter+= length;
      } else if(singleLine>0 || lineCounter>line) {
        // put everything on a single line
        length = 0;
        write(makeSingleLine(s));
      } else if(lineCounter <= line) {
        while(lineCounter < line) {
          write("\n");
          lineCounter++;
        }
        write(s);
        lineCounter+= length;
      } 
    } else {
      String[] lines = s.split("\n");
      //System.out.println("s = '" + s + "'");
      //System.out.println("length = " + lines.length);
      if(lines.length==0) {
        write(deep, s.replaceFirst("^\\s+",""));
      } else {
        for (int i=0;i<lines.length;i++) {
          String ln = lines[i];
          ln = ln.replaceFirst("^\\s+",""); // removes spaces at the beginning of the line
          writeln(deep, ln);
        }
      }
    }
  }

  public void close() {
    try {
      file.flush();
      file.close();
    } catch (IOException e) {
      System.out.println("close error");
      e.printStackTrace();
    }
  }

  public String stringDump() {
    try {
      if(file instanceof StringWriter) {
        file.flush();
        return file.toString();
      } else {
        throw new InternalError("OutputCode does not contain any string");
      }
    } catch (IOException e) {
      System.out.println("stringDump error");
      e.printStackTrace();
      return null;
    }
  }

  public void indent(int deep) {
    try {
      if(indent) {
        if(pretty) {
          for(int i=0 ; i<deep ; i++) {
            file.write("    ");
          }
        } else {
          file.write(' ');
        }
      }
    } catch (IOException e) {
      System.out.println("write error");
      e.printStackTrace();
    }
  }

}
