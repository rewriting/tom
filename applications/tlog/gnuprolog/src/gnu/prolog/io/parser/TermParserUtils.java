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
package gnu.prolog.io.parser;
import gnu.prolog.io.parser.gen.*;
import gnu.prolog.io.*;
import gnu.prolog.term.*;

public class TermParserUtils
{

  public final static int EOF_TOKEN = TermParserConstants.EOF;
  public static String convertQuotedString(String str, char quote)
  {
    int i,n = str.length();
    if (str.charAt(0) != quote || str.charAt(n-1) != quote)
    {
      return null;
    }
    char ch = 0;
    str = str.substring(1,n-1);
    n -= 2;
    StringBuffer bu = new StringBuffer();
    chars: for (i=0;i<n;i++)
    {
      char buch = str.charAt(i);
      switch (buch)
      {
      case '\\':
        buch = str.charAt(++i);
        switch (buch)
        {
        case 'a': ch = '\u0007'; break;
        case 'b': ch = '\b'; break;
        case 'f': ch = '\f'; break;
        case 'n': ch = '\n'; break;
        case 't': ch = '\t'; break;
        case 'v': ch = '\u000b'; break;
        case 'r': ch = '\r'; break;
        case '\r':
          buch = str.charAt(i+1);
          if (buch == '\n')
          {
            i++;
          }
        case '\n':
          continue chars;
        case 'x':
          ch = 0;
          while ( (buch = str.charAt(++i)) != '\\' )
          {
            ch = (char)(ch * 16 + Character.digit(buch,16));
            // @@@ rethow parse error if not digit
          }
        default:
          buch = str.charAt(i);
          if (buch >= '0' && buch < '8')
          {
            ch = 0;
            while ( (buch = str.charAt(i)) != '\\' )
            {
              ch = (char)(ch * 8 + Character.digit(buch,8));
              ++i;
            }
          }
          else
          {
            ch = buch;
          }
        }
        break;
      default:
        if (buch == quote)
        {
          ch = str.charAt(++i); // shoud be = quote too
          if (ch != quote)
          {
            // @@@ rethow parse error
          }
        }
        ch = buch;
      }
      bu.append(ch);
    }
    return bu.toString();
  }
  public static char convertQuotedChar(String str,char quote)
  {
    int i = 0;
    char buch = str.charAt(i),ch =0;
    switch (buch)
    {
    case '\\':
      buch = str.charAt(++i);
      switch (buch)
      {
      case 'a': ch = '\u0007'; break;
      case 'b': ch = '\b'; break;
      case 'f': ch = '\f'; break;
      case 'n': ch = '\n'; break;
      case 't': ch = '\t'; break;
      case 'v': ch = '\u000b'; break;
      case 'r': ch = '\r'; break;
      case 'x':
        ch = 0;
        while ( (buch = str.charAt(++i)) != '\\' )
        {
          ch = (char)(ch * 16 + Character.digit(buch,16));
          // @@@ rethow parse error if not digit
        }
      default:
        ch = 0;
        while ( (buch = str.charAt(++i)) != '\\' )
        {
          ch = (char)(ch * 8 + Character.digit(buch,8));
          // @@@ rethow parse error if not digit
        }
      }
      break;
    default:
      if (buch == quote)
      {
        ch = str.charAt(++i); // shoud be = quote too
        if (ch != quote)
        {
          // @@@ rethow parse error if not digit
        }
      }
      ch = buch;
    }
    return ch;
  }
}

