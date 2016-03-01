/*
 * Gom
 *
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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
 * Antoine Reilles       e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.tools.ant;

import org.apache.tools.ant.taskdefs.Java;

/**
 * Compiles GOM source files. This task can take the following
 * arguments:
 * <ul>
 * <li>config</li>
 * <li>srcdir</li>
 * <li>destdir</li>
 * <li>optimize</li>
 * <li>optimize2</li>
 * <li>inlineplus</li>
 * <li>newtyper</li>
 * <li>newparser</li>
 * <li>package</li>
 * <li>options</li>
 * <li>failonerror</li>
 * <li>fork</li>
 * <li>termgraph</li>
 * <li>termpointer</li>
 * <li>withCongruenceStrategies</li>
 * <li>fresh</li>
 * <li></li>
 * </ul>
 * Of these arguments, the <b>srcdir</b> and <b>destdir</b> are
 * required.
 * <b>config</b> has to be set if the Gom.xml file can't be found in
 * <b>tom.home</b>.<p>
 *
*/

public class GomTask extends GomCommonTask {

  private boolean newtyper = false;
  private boolean newparser = false;
  private boolean optimize = false;
  private boolean optimize2 = false;
  private boolean inlineplus = false;

  protected String toPattern() {
    return "\\1\\L\\3" + protectedFileSeparator + "\\3.tom";
  }

  protected String defaultConfigName() {
    return "Gom.xml";
  }

  /**
   * If true, compiles with new typer enabled.
   * @param flag if true compile with new typer
   */
  public void setNewtyper(boolean newtyper) {
    this.newtyper = newtyper;
  }

  public boolean getNewtyper() {
    return newtyper;
  }

  /**
   * If true, compiles with new parser enabled.
   * @param flag if true compile with new parser
   */
  public void setNewparser(boolean newparser) {
    this.newparser = newparser;
  }

  public boolean getNewparser() {
    return newparser;
  }

  /**
   * If true, compiles with optimization enabled.
   * @param optimize if true compile with optimization level-1 enabled
   */
  public void setOptimize(boolean optimize) {
    this.optimize = optimize;
  }

  public boolean getOptimize() {
    return optimize;
  }

  /**
   * If true, compiles with optimization level-2 enabled.
   * @param optimize if true compile with optimization level-2 enabled
   */
  public void setOptimize2(boolean optimize) {
    this.optimize2 = optimize;
  }

  public boolean getOptimize2() {
    return optimize2;
  }

  /**
   * If true, compiles with inlining
   * @param inlineplus if true compile with inlining
   */
  public void setInlineplus(boolean flag) {
    this.inlineplus = flag;
  }

  public boolean getInlineplus() {
    return inlineplus;
  }

  protected void processAdditionalOptions(Java runner) {
    if(newtyper) {
      javaRunner.createArg().setValue("--newtyper");
    }
    if(newparser) {
      javaRunner.createArg().setValue("--newparser");
    }
    if(optimize) {
      javaRunner.createArg().setValue("--optimize");
    }
    if(optimize2) {
      javaRunner.createArg().setValue("--optimize2");
    }
    if(inlineplus) {
      javaRunner.createArg().setValue("--inlineplus");
    }
  }
}
