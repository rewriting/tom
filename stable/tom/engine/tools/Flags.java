/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
			     Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom.tools;

import java.util.*;

public class Flags {

  public static boolean pretty = false; // Synchronize TL code and source code
  
  public static boolean verbose = false;
  
  public static boolean printOutput = true; // to print the output

  public static boolean atermStat = false; // to print some statistics about ATerms

  public static boolean jCode = true;  // generate Java
  public static boolean cCode = false; // generate C
  public static boolean eCode = false; // generate Eiffel

  public static boolean version = false; // to print the version number

  public static boolean doParse   = true; // parse a *.t file
  public static boolean doExpand  = true; // expand the AST
  public static boolean doCheck   = true; // type check the AST
  public static boolean doCompile = true; // compile the AST

  public static boolean intermediate = false; // generate intermediate files


  public static boolean supportedGoto  = true; //true; // if the target language has gotos
  public static boolean supportedBlock = true; // if the target language has blocks

  public static boolean noWarning = false; // print warning error messages

  public static boolean demo = false; // run demonstration interface

  public static boolean findErrors = false; // true if an error is found

  public static boolean doVerify = true; // verify during parsing
  public static boolean strictType = true; // no universal type
  public static boolean genDecl = true; // generate declarations
  
}
