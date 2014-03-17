/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the Inria nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
//Source file: C:\\document\\codegen\\xquery\\lib\\PathAxe.java

package xquery.lib;


public class PathAxe 
{
  public static final int CHILD = 0;
  public static final int ATTRIBUTE = 1;
  public static final int PARENT = 2;
  public static final int SELF = 3;
  public static final int DESCENDANT = 4;
  public static final int DESCENDANT_OR_SELF = 5;
  protected int axeType;
   
  /**
   * @param type
   * @roseuid 4118049D035F
   */
  public PathAxe(int type) 
  {
	this.axeType = type; 
  }
   
  /**
   * @roseuid 4110B63101E2
   */
  public PathAxe() 
  {
	this.axeType = CHILD; 
  }
   
  /**
   * @return int
   * @roseuid 410E0D9301E8
   */
  public int getAxeType() 
  {
    return axeType;
  }
   
  /**
   * @param type
   * @roseuid 410E0F26016F
   */
  public void setAxeType(int type) 
  {
	this.axeType = type; 
  }
   
  /**
   * @return PathAxe
   * @roseuid 410FA11002AC
   */
  public static PathAxe createChildPathAxe() 
  {
    return new PathAxe(CHILD);
  }
   
  /**
   * @return PathAxe
   * @roseuid 410FA12D029A
   */
  public static PathAxe createSelfPathAxe() 
  {
    return new PathAxe(SELF);
  }
   
  /**
   * @return PathAxe
   * @roseuid 410FA12E0147
   */
  public static PathAxe createParentPathAxe() 
  {
    return new PathAxe(PARENT);
  }
   
  /**
   * @return PathAxe
   * @roseuid 410FA12E03D2
   */
  public static PathAxe createAttributePathAxe() 
  {
    return new PathAxe(ATTRIBUTE);
  }
   
  /**
   * @return PathAxe
   * @roseuid 410FA12F0261
   */
  public static PathAxe createDescendantPathAxe() 
  {
    return new PathAxe(DESCENDANT);
  }
   
  /**
   * @return PathAxe
   * @roseuid 410FA13000DC
   */
  public static PathAxe createDescendantOrSelfPathAxe() 
  {
    return new PathAxe(DESCENDANT_OR_SELF);
  }
}
