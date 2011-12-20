/*
 * Gom
 *
 * Copyright (c) 2006-2011, INPL, INRIA
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
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.backend.shared;

import java.io.*;
import java.util.*;

import tom.gom.backend.TemplateClass;
import tom.gom.backend.TemplateHookedClass;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.tools.GomEnvironment;
import tom.platform.OptionManager;

public class AbstractTypeTemplate extends TemplateHookedClass {
  ClassNameList sortList;

  %include { ../../adt/objects/Objects.tom }
  boolean maximalsharing;

  public AbstractTypeTemplate(File tomHomePath,
                              OptionManager manager,
                              List importList,
                              GomClass gomClass,
                              TemplateClass mapping,
                              boolean maximalsharing,
                              GomEnvironment gomEnvironment){
    super(gomClass,manager,tomHomePath,importList,mapping,gomEnvironment);
    this.maximalsharing = maximalsharing;
    %match(gomClass) {
      AbstractTypeClass[SortList=sortList] -> {
        this.sortList = `sortList;
        return;
      }
    }
    throw new GomRuntimeException(
        "Bad argument for AbstractTypeTemplate: " + gomClass);
  }

  public void generateSpec(java.io.Writer writer) throws java.io.IOException {
writer.write(%[ --From generateSpec at AbstractTypeTemplate.t, unnecessary ]%) ;
}


  public void generate(java.io.Writer writer) throws java.io.IOException {

    writer.write(
%[
-- with VisitablePackage; use VisitablePackage;
with SharedObjectFactoryP; use SharedObjectFactoryP;
with SharedObjectP; use SharedObjectP;
]%);

    String implementsInterface;
    if(maximalsharing) {
      implementsInterface = "SharedObjectP.SharedObject with null record";
    } else {
      implementsInterface = "tom.library.sl.Visitable and Cloneable and Comparable";
 
    }
    writer.write(
%[

package @fullClassName()@ is

	type @className()@ is abstract new @implementsInterface@ ; 
]%);

    if(hooks.containsTomCode()) {
      mapping.generate(writer);
    }
    writer.write(
%[
@generateBlock()@
]%);

    if(maximalsharing) {
      writer.write(
%[
  factory : access SharedObjectFactoryP.SharedObjectFactory := new SharedObjectFactoryP.SharedObjectFactory;
]%);
    }

writer.write(
%[
end @fullClassName()@;
]%); // end of class
}

}
