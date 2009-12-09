/* GNU Prolog for Java
 * Copyright (C) 1997-1999  Constantine Plotnikov
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA. The text ol license can be also found 
 * at http://www.gnu.org/copyleft/lgpl.html
 */
package gnu.prolog.io;
import java.io.*;
import java.util.*;
import gnu.prolog.term.*;

/** This class is intendent for printing terms.
  * @author Constantine A. Plotnikov.
  * @version 0.0.1
  */

public class TermWriter extends PrintWriter
{
  // static variables
  private static final CompoundTermTag numbervarsTag = CompoundTermTag.get("$VAR",1);
  private static final CompoundTermTag listTag = CompoundTermTag.get(".",2);
  private static final CompoundTermTag curly1Tag = CompoundTermTag.get("{}",1);
  private static final OperatorSet     defaultOperatorSet = new OperatorSet();
  private static final WriteOptions    defaultWrteOptions = new WriteOptions();
  static {
    defaultWrteOptions.ignoreOps = false;
    defaultWrteOptions.operatorSet = defaultOperatorSet;
    defaultWrteOptions.quoted = true;
    defaultWrteOptions.numbervars = false;
  }

  /** convert term passed as argument to string 
    * @param term a term to convert
    * @return String representation of the term
    */
  public static String toString(Term term)
  {
    try
    {
      StringWriter sout = new StringWriter();
      TermWriter tout = new TermWriter(sout);
      tout.print(term);
      tout.flush();
      return sout.toString();
    }
    catch(Exception ex)
    {
      throw new IllegalArgumentException("BAD TERM: "+ex.toString());
    }
  }


  /** convert term passed as argument to string 
    * @param term a term to convert
    * @return String representation of the term
    */
  public static String toString(Term term, WriteOptions options)
  {
    try
    {
      StringWriter sout = new StringWriter();
      TermWriter tout = new TermWriter(sout);
      tout.print(options, term);
      tout.flush();
      return sout.toString();
    }
    catch(Exception ex)
    {
      throw new IllegalArgumentException("BAD TERM: "+ex.toString());
    }
  }

  /** create term writer over other writer.
    * @param w underlying writer
    */
  public TermWriter(Writer w)
  {
    super(w);
  }

  /** print term using specified write options
    * @param options write options
    * @param term term to print
    */
  public void print(WriteOptions options, Term term)
  {
    displayTerm(options, 1200, term);
  }

  /** print term using default write options
    * @param term term to print
    */
  public void print(Term term)
  {
    print((WriteOptions)defaultWrteOptions.clone(),term);
  }

  /** print term using default write options and specified operator set
    * @param opSet operator set to use
    * @param term term to print
    */
  public void print(OperatorSet opSet, Term term)
  {
    WriteOptions options = new WriteOptions();
    options.ignoreOps = false;
    options.operatorSet = opSet;
    options.quoted = true;
    options.numbervars = false;
    print(options,term);
  }

  /** display term
    * @param options current write options
    * @param priority priority of nearest operation, this variable is only
    *                 defined if ignoreOps is false.
    * @param term term to write
    */
  private void displayTerm(WriteOptions options, int priority, Term term)
  {
    term = term.dereference();
    if (term == null)
    {
      print("<<NULL>>");
      //term = new VariableTerm(); // cretate anonimous variable
    }
    else
    {
      term = term.dereference();
    }
    if (term instanceof AtomTerm)
    {
      boolean isOp = isOperator(options.operatorSet, (AtomTerm)term) && options.quoted;
      if (isOp)
      {
        print("("); 
      }
      displayAtom(options, (AtomTerm)term);
      if (isOp)
      {
        print(")"); 
      }
    }
    else if (term instanceof IntegerTerm)
    {
      displayInteger(options, (IntegerTerm)term);
    }
    else if (term instanceof FloatTerm)
    {
      displayFloat(options, (FloatTerm)term);
    }
    else if (term instanceof CompoundTerm)
    {
      displayCompound(options, priority, (CompoundTerm)term);
    }
    else if (term instanceof VariableTerm)
    {
      displayVariable(options, (VariableTerm)term);
    }
  }

  /** display compound term
    * @param options current write options
    * @param priority priority of nearest operation, this variable is only
    *                 defined if ignoreOps is false.
    * @param term compound term to write
    */
  private void displayCompound(WriteOptions options, int priority, CompoundTerm term)
  {
    // check for numbervars
    if (options.numbervars &&
        term.tag == numbervarsTag &&
        term.args[0] instanceof IntegerTerm)
    {
      int n  = ((IntegerTerm)term.args[0]).value;
      print((char)(n%26 + 'A'));
      print(n/26);
      return;
    }
    if (!options.ignoreOps)
    {
      if (term.tag == listTag) // if list term
      {
        print('[');
        displayList(options,term);
        print(']');
        return;
      }
      if (term.tag == curly1Tag) // if {}/1 term
      {
        print('{');
        displayTerm(options, 1201, term.args[0]);
        print('}');
        return;
      }
      //operators
      Operator op = options.operatorSet.getOperatorForTag(term.tag);
      if (op != Operator.nonOperator)
      {
        if (op.priority > priority)
        {
          print('(');
        }
        switch (op.specifier)
        {
        case Operator.FX :
          displayAtom(options, term.tag.functor);
          print(" ");
          displayTerm(options, op.priority-1, term.args[0]);
          break;
        case Operator.FY :
          displayAtom(options, term.tag.functor);
          print(" ");
          displayTerm(options, op.priority, term.args[0]);
          break;
        case Operator.XFX:
          displayTerm(options, op.priority-1, term.args[0]);
          print(" ");
          displayAtom(options, term.tag.functor);
          print(" ");
          displayTerm(options, op.priority-1, term.args[1]);
          break;
        case Operator.XFY:
          displayTerm(options, op.priority-1, term.args[0]);
          print(" ");
          displayAtom(options, term.tag.functor);
          print(" ");
          displayTerm(options, op.priority, term.args[1]);
          break;
        case Operator.YFX:
          displayTerm(options, op.priority, term.args[0]);
          print(" ");
          displayAtom(options, term.tag.functor);
          print(" ");
          displayTerm(options, op.priority-1, term.args[1]);
          break;
        case Operator.XF :
          displayTerm(options, op.priority-1, term.args[0]);
          print(" ");
          displayAtom(options, term.tag.functor);
          break;
        case Operator.YF :
          displayTerm(options, op.priority, term.args[0]);
          print(" ");
          displayAtom(options, term.tag.functor);
          break;
        default:
          throw new IllegalArgumentException("Wrong operator specifier = "+op.specifier);
        }

        if (op.priority > priority)
        {
          print(')');
        }
        return;
      }
    }

    // canonical form term
    displayAtom(options, term.tag.functor);
    print('(');
    for (int i=0; i < term.args.length; i++)
    {
      if (i>0)
      {
        print(",");
      }
      displayTerm(options, 999, term.args[i]);
    }
    print(')');
  }

  /** display list
    * @param options current write options
    * @param term list term to write
    */
  private void displayList(WriteOptions options, CompoundTerm term)
  {
    displayTerm(options, 999, term.args[0]);
    Term tail = term.args[1];
    if (tail!=null)
    {
      tail = tail.dereference();
    }
    if (tail == AtomTerm.emptyList)
    {
      //do nothing
    }
    else if (tail instanceof CompoundTerm && ((CompoundTerm)tail).tag == listTag)

    {
      print(",");
      displayList(options, (CompoundTerm)tail);
    }
    else 
    {
      print("|");
      displayTerm(options, 999, tail);
    }
  }

  /** display float term
    * @param options current write options
    * @param term fload term to write
    */
  private void displayFloat(WriteOptions options, FloatTerm term)
  {
    if (options.quoted)
    {
      print(term.value); // check later if it is enough
    }
    else
    {
      print(term.value);
    }
  }

  /** display integer term
    * @param options current write options
    * @param term integer term to write
    */
  private void displayInteger(WriteOptions options, IntegerTerm term)
  {
    print(term.value);
  }

  /** display variable term
    * @param options current write options
    * @param variable variable to display
    */
  private void displayVariable(WriteOptions options, VariableTerm term)
  {
    if (options.variable2name == null)
    {
      options.variable2name = new HashMap();
    }
    String name = (String)options.variable2name.get(term);
    if (name == null)
    {
      int n = options.numberOfVariables++;
      name = ("_"+(char)(n % 26 + 'A'))+(n / 26);
      options.variable2name.put(term,name);
    }
    print(name);
  }

  private static boolean isOperator(OperatorSet set, AtomTerm term)
  {
    Operator fxOp = set.lookupFx(term.value);
    Operator xfOp = set.lookupXf(term.value);
    return fxOp != Operator.nonOperator || fxOp != Operator.nonOperator;
  }


  /** display atom.
    * @param options current write options
    * @param atom atom to display
    */
  private void displayAtom(WriteOptions options, AtomTerm atom)
  {
    if (options.quoted)
    {
      String value = atom.value;
      print(needBeQuoted(value)?getSingleQuoted(value):value);
    }
    else
    {
      print(atom.value);
    }
  }

  /** get single quoted string.
    * @param s string to quote
    * @return single quoted string
    */
  private static String getSingleQuoted(String s)
  {
    StringBuffer buf = new StringBuffer(s.length()+6);
    buf.append('\'');
    int i, n = s.length();
    for (i=0; i<n; i++)
    {
      appendQuotedChar(buf,s.charAt(i),'\'');
    }
    buf.append('\'');
    return buf.toString();
  }

  /** check if the string is needed to be quoted .
    * @param s string to test
    * @return true if string need to quoted in displayq
    */
  private static boolean needBeQuoted(String s)
  {
    char ch = s.charAt(0);
    if (isSoloChar(ch))
    {
      // solo char need to be quoted if  length of atom >1 or it is %
      return s.length() != 1 || ch == '%';
    }
    else if (isGraphicsChar(ch))
    {
      // graphics need to be quoted if if not all chars are graphics
      int i,n=s.length();
      if (n >= 2 && s.charAt(0) == '/' && s.charAt(1) == '*')
      {
        return true;
      }
      for (i=1;i<n;i++)
      {
        if (!isGraphicsChar(s.charAt(i)))
        {
          return true;
        }
      }
      return false;
    }
    else if (isAtomStartChar(ch))
    {
      // identifier need to be quoted if if not all chars are alphanmeric
      int i,n=s.length();
      for (i=1;i<n;i++)
      {
        if (!isAtomChar(s.charAt(i)))
        {
          return true;
        }
      }
      return false;
    }
    else
    {
      return true;
    }
  }

  /** check if character is solo char.
    * @param c character to test
    * @return true if character is solo char
    */
  private static boolean isSoloChar(char c)
  {
    switch (c)
    {
    case ';': case '!': case '(': case ')': case ',':
    case '[': case ']': case '{': case '}': case '%':
      return true;
    default:
      return false;
    }
  }

  /** check if character is graphics char.
    * @param ch character to test
    * @return true if character is graphics char
    */
  private static boolean isGraphicsChar(char ch)
  {
    switch (ch)
    {
    case '#': case '$': case '&': case '*': case '+': case '-':
    case '.': case '/': case ':': case '<': case '=': case '>':
    case '?': case '@': case '^': case '~':
      return true;
    default:
      return false;
    }

  }

  /** check if character is valid start of atom.
    * @param c character to test
    * @return true if character is valid start of atom.
    */
  private static boolean isAtomStartChar(char c)
  {
    return 'a' <= c && c <= 'z';
  }

  /** check if character is valid continuation of atom.
    * @param c character to test
    * @return true if character is valid continuation of atom.
    */
  private static boolean isAtomChar(char c)
  {
    return ('a' <= c && c <= 'z') ||
           ('A' <= c && c <= 'Z') ||
           ('0' <= c && c <= '9') ||
           (c == '_');
  }

  /** append quoted char to string buffer.
    * @param buf buffer to which character is added
    * @param ch character to add
    * @param quote a quote of string
    */
  private static void appendQuotedChar(StringBuffer buf, char ch, char quote)
  {
    if (ch == quote) // if quote append "\" quote
    {
      buf.append('\\');
      buf.append(quote);
    }
    else if(ch < ' ' || ch >= 127) // if control character or non ascii
    {
      buf.append('\\');
      switch (ch)
      {
      case '\u0007': buf.append('a'); break;
      case '\b'    : buf.append('b'); break;
      case '\f'    : buf.append('f'); break;
      case '\n'    : buf.append('n'); break;
      case '\t'    : buf.append('t'); break;
      case '\u000b': buf.append('v'); break;
      case '\r'    : buf.append('r'); break;
      default:
        buf.append('x');
        buf.append(Integer.toHexString(ch));
        buf.append('\\');
      }
    }
    else
    {
      switch (ch)
      {
      case '\\':
        buf.append("\\");
      default:
        buf.append(ch);
      }
    }
  }
}
