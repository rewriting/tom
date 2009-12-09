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
package gnu.prolog.database;


public class PrologTextLoaderError extends Exception
{
  String file;
  int    line;
  int    column;
  //String message;

  public PrologTextLoaderError(String file,
                               int    line,
                               int    column,
                               String message)
  {
    super(message);
    this.file    = file   ;
    this.line    = line   ;
    this.column  = column ;
    //this.message = message;
  }


  public PrologTextLoaderError(PrologTextLoader loader,
                               String message)
  {
    this(loader.getCurrentFile(), loader.getCurrentLine(), loader.getCurrentColumn(), message);
  }


  public PrologTextLoaderError(PrologTextLoader loader,
                               gnu.prolog.io.ParseException ex)
  {
    this(loader.getCurrentFile(), ex.getLine(), ex.getColumn(), "syntax error");
  }

  public String getFile()
  {
    return file;
  }

  public int getLine()
  {
    return line;
  }

  public int getColumn()
  {
    return column;
  }

  public String toString()
  {
    return file+":"+line+":"+column+": "+getMessage();
  }
}
