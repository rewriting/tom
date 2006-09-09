/*
 * Gom
 *
 * Copyright (C) 2006 INRIA
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

package tom.gom.backend;

import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;

public abstract class TemplateHookedClass extends TemplateClass {
  protected HookList hooks;

  public TemplateHookedClass(ClassName className,HookList hooks) {
    super(className);
    this.hooks = hooks;
  }

  %include { ../adt/objects/Objects.tom}

  protected String generateBlock() {
    StringBuffer res = new StringBuffer();
    %match(HookList hooks) {
      concHook(L1*,BlockHook(code),L2*) -> {
        //remove brackets
        res.append(`code.substring(1,`code.length()-1)+"\n");
        hooks = `concHook(L1*,L2*);
      }
    }
    return res.toString();
  }

  protected String generateImport() {
    StringBuffer res = new StringBuffer();
    %match(HookList hooks) {
      concHook(L1*,ImportHook(code),L2*) -> {
        //remove brackets
        res.append(`code.substring(1,`code.length()-1)+"\n");
        hooks = `concHook(L1*,L2*);
      }
    }
    return res.toString();
  }

  protected String generateInterface() {
    StringBuffer res = new StringBuffer();
    %match(HookList hooks) {
      concHook(L1*,InterfaceHook(code),L2*) -> {
        //remove brackets
        res.append(","+`code.substring(1,`code.length()-1).replaceAll("\n",""));
        hooks = `concHook(L1*,L2*);
      }
    }
    return res.toString();
  }
}
