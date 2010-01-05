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
package gnu.prolog.term;
/** 32bit Integer term.
  * @author Constantine Plotnikov
  * @version 0.0.1
  */
public class IntegerTerm extends AtomicTerm
{
  // terms from -32k to +32k are cached
  private static IntegerTerm cache[] = new IntegerTerm[64*1024];

  // often used integers
  /** -2 interger term */
  public static final IntegerTerm int_m2 = get(-2);
  /** -1 integer term */
  public static final IntegerTerm int_m1 = get(-1);
  /** 0 integer term */
  public static final IntegerTerm int_0 = get(0);
  /** 1 integer term */
  public static final IntegerTerm int_1 = get(1);
  /** 2 integer term */
  public static final IntegerTerm int_2 = get(2);
  /** 3 integer term */
  public static final IntegerTerm int_3 = get(3);
  /** 4 integer term */
  public static final IntegerTerm int_4 = get(4);


  /** get integer term equal to val
    * @param val value of integer term
    * @return new integer term
    */
  public static IntegerTerm get(int val)
  {
    int idx = val+32*1024;
    IntegerTerm rc;
    if (0 <= idx && idx < 64*1024)
    {
      rc = cache[idx];
      if (rc == null)
      {
        rc = new IntegerTerm(val);
        cache[idx] = rc;
      }
    }
    else
    {
      rc = new IntegerTerm(val);
    }
    return rc;
  }
  
  private static int parseInt(String str)
  {
    int val;
    int sign  = 1;
    try
    {
      if (str.charAt(0) == '-')
      {
        sign = -1;
        str = str.substring(1);
      }
      // if first symbol is 0 do special processing
      if (str.charAt(0) == '0' && str.length()>1 )
      {
        switch (str.charAt(1))
        {
        case 'b':
        case 'B':
          // binary integer
          //System.out.println("binary begin = "+str.substring(2));
          val = Integer.parseInt(str.substring(2),2);
          //System.out.println("binary test end "+val);
          break;
        case 'O':
        case 'o':
          // octal integer
          val = Integer.parseInt(str.substring(2),8);
          break;
        case 'X':
        case 'x':
          // hexadecimal integer
          val = Integer.parseInt(str.substring(2),16);
          break;
        case '\'':
          // character integer
          str = str.substring(2);
          chars: while (true)
          {
            switch (str.charAt(0))
            {
            case '\\':
              switch (str.charAt(1))
              {
              case '\\': val = '\\'; break chars;
              case 'n': val = '\n'; break chars;
              case '\'': val = '\''; break chars;
              case '\"': val = '\"'; break chars;
              case 'b': val = '\b'; break chars;
              case 'a': val = 7; break chars;
              case 'r': val = '\r'; break chars;
              case 't': val = '\t'; break chars;
              case 'v': val = 0x0b; break chars;
              case '\r':
                str = str.substring(str.charAt(2)=='\n'?3:2);
                continue chars;
              case '\n':
                str = str.substring(str.charAt(2)=='\r'?3:2);
                continue chars;
              case 'x':
                val = 0;
                int i = 2;
                while ( str.charAt(i) != '\\' )
                {
                  val = (val * 16 + Character.digit(str.charAt(i),16));
                  i++;
                }
                break chars;
              default:
                val = str.charAt(1);
                break chars;
              }
            default:
              val = str.charAt(0);
              break chars;
            }
          }
          break;
        default:
          throw new IllegalArgumentException("argument should be integer number");
        }
      }
      else
      {
        val = Integer.parseInt(str);
      }
      return val*sign;
    }
    catch(NumberFormatException ex)
    {
      throw new IllegalArgumentException("argument should be integer number");
    }
  }
  /** get integer term using string value
    * @param str value of integer term
    * @return new integer term
    * @throw IllegalArgumentException when val could ne be converted to integer
    */
  public static IntegerTerm get(String str)
  {
    return get(parseInt(str));
  }
  /** get integer term using string value
    * @param str value of integer term
    * @return new integer term
    * @throw IllegalArgumentException when val could ne be converted to integer
    */
  public IntegerTerm(String str)
  {
    this(parseInt(str));
  }
  /** a constructor
    * @param val value of term
    */
  public IntegerTerm(int val) {value = val;}
  /** value of integer */
  public final int value;


  /** get type of term 
    * @return type of term
    */
  public int getTermType()
  {
    return INTEGER;
  }


}

