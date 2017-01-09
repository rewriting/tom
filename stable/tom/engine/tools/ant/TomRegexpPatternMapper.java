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
 * Antoine Reilles        e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.engine.tools.ant;

import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.*;
import org.apache.tools.ant.util.regexp.RegexpMatcher;
import org.apache.tools.ant.util.regexp.RegexpMatcherFactory;

/**
 * Implementation of FileNameMapper that does regular expression
 * replacements. 
 * Extends the apache one with \\L syntax to insert \n in
 * lowercase
 */
public class TomRegexpPatternMapper extends RegexpPatternMapper implements FileNameMapper {

  public TomRegexpPatternMapper() throws BuildException {
    super();
  }

  /**
   * Replace all backreferences in the to pattern with the matched
   * groups of the source.
   */
  protected String replaceReferences(String source) {
    boolean lowercase=false;
    Vector v = reg.getGroups(source);

    result.setLength(0);
    for (int i = 0; i < to.length; i++) {
      if (to[i] == '\\') {
        if (++i < to.length) {
          int value = Character.digit(to[i], 10);
          if (value > -1) {
            String repl = (String) v.elementAt(value);
            if (lowercase) {
              repl = repl.toLowerCase();
            } 
            result.append(repl);
            lowercase = false;
          } else {
            if (to[i] == 'L') {
              lowercase=true;
            } else {
              result.append(to[i]);
            }
          }
        } else {
          result.append('\\');
        }
      } else {
        result.append(to[i]);
      }
    }
    return result.substring(0);
  }

}
