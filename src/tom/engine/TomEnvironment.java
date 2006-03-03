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
 *
 **/

package tom.engine;


import aterm.pure.PureFactory;
import aterm.pure.SingletonFactory;
import tom.engine.adt.tomsignature.TomSignatureFactory;
import tom.engine.tools.ASTFactory;
import tom.platform.adt.platformoption.PlatformOptionFactory;

/**
 * The TomEnvironment class is a singleton for accessing all used factory
 * AST, TomSignature and PlatformOption
 */
public class TomEnvironment {
  /**
   * Part of the Singleton pattern.
   * The unique instance of the TomEnvironment
   */
  private static TomEnvironment instance;

  /** the factories */
  private ASTFactory astFactory;
  private TomSignatureFactory   tomSignatureFactory;
  private PlatformOptionFactory platformOptionFactory;
  
  /**
   * A private constructor method to defeat instantiation
   */
  private TomEnvironment() {
    PureFactory pure = SingletonFactory.getInstance();
    tomSignatureFactory = TomSignatureFactory.getInstance(pure);
    platformOptionFactory = PlatformOptionFactory.getInstance(pure);
    astFactory = new ASTFactory(tomSignatureFactory);
  }

  /**
   * Part of the Singleton pattern, get the instance or create it.
   * @return the instance of the TomEnvironment
   */
  public static TomEnvironment getInstance() {
    if(instance == null) {
      instance = new TomEnvironment();
    }
    return instance;
  }

  /**
   * Accessor for astFactory
   * @return the environments astFactory
   */
  public ASTFactory getAstFactory() {
    return astFactory;
  }

  /**
   * Accessor for tomSignatureFactory
   * @return the environments tomSignatureFactory
   */
  public TomSignatureFactory getTomSignatureFactory() {
    return tomSignatureFactory;
  }

  /**
   * Accessor for platformOptionFactory
   * @return the environments platformOptionFactory
   */
  public PlatformOptionFactory getPlatformOptionFactory() {
    return platformOptionFactory;
  }

} // class TomEnvironment
