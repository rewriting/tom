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

import  java.io.FilterReader;
import  java.io.Reader;
import  java.io.StringReader;
import  gnu.prolog.term.Term;
import  gnu.prolog.io.parser.gen.TermParser;
import  gnu.prolog.io.parser.ReaderCharStream;

public class TermReader extends FilterReader
{
  private static OperatorSet defaultOperatorSet = new OperatorSet();
  TermParser parser;

  public TermReader(Reader r, int line, int col)
  {
    super(r);
    parser = new TermParser(r, line,col);
  }

  public TermReader(Reader r)
  {
    this(r, 1, 1);
  }

  public Term readTerm(ReadOptions options) throws ParseException
  {
    try
    {
      return parser.readTerm(options);
    }
    catch(gnu.prolog.io.parser.gen.ParseException ex)
    {
      throw new ParseException(ex);
    }
  }

  public static Term stringToTerm(ReadOptions options, String str) throws ParseException
  {
    StringReader srd = new StringReader(str);
    TermReader trd = new TermReader(srd);
    return trd.readTermEof(options);
  }
  
  public static Term stringToTerm(String str) throws ParseException
  {
    StringReader srd = new StringReader(str);
    TermReader trd = new TermReader(srd);
    return trd.readTermEof();
  }

  public Term readTermEof(ReadOptions options) throws ParseException
  {
    try
    {
      return parser.readTermEof(options);
    }
    catch(gnu.prolog.io.parser.gen.ParseException ex)
    {
      throw new ParseException(ex);
    }
  }



  public Term readTerm(OperatorSet set) throws ParseException
  {
    ReadOptions options = new ReadOptions();
    options.operatorSet = set;
    return readTerm(options);
  }

  public Term readTermEof(OperatorSet set) throws ParseException
  {
    ReadOptions options = new ReadOptions();
    options.operatorSet = set;
    return readTermEof(options);
  }

  public Term readTerm() throws ParseException
  {
    return readTerm(defaultOperatorSet);
  }

  public Term readTermEof() throws ParseException
  {
    return readTermEof(defaultOperatorSet);
  }

  public int getCurrentLine()
  {
    return parser.getCurrentLine();
  }

  public int getCurrentColumn()
  {
    return parser.getCurrentColumn();
  }

}


