/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2006, INRIA
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
 * Julien Guyon
 *
 **/

package tom.engine.debug;

import java.util.HashSet;

public class TomDebugStructure {
  String[] patternText;
  HashSet watchPatternList;
  int nbPatterns;
  int nbSubjects;
  int[] patternLine;
  String fileName;
  int line;
  String key;
  String type;

  TomDebugStructure(String key, String type, String fileName, int line, int nbPatterns, int nbSubjects, String[] patternText, int[] patternLine){
    this.key = key;
    this.type = type;
    this.fileName = fileName;
    this.line = line;
    this.nbPatterns = nbPatterns;
    this.nbSubjects = nbSubjects;
    this.patternText = patternText;
    this.patternLine = patternLine;
  }

} // Class TomDebugStructure
