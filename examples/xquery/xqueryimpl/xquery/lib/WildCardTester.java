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
//Source file: C:\\document\\codegen\\xquery\\lib\\WildCardTester.java

package xquery.lib;

import org.w3c.dom.*;

import xquery.lib.data.type.*;
import xquery.lib.data.*;

public class WildCardTester extends NodeTester 
{
   private int type;
   private String name;
   public static final int STAR = 0;
   public static final int PREFIX_STAR = 1;
   public static final int STAR_POSTFIX = 2;
   
   /**
    * @roseuid 4110B63201DA
    */
   public WildCardTester() 
   {
    
   }
   
   /**
    * @param type
    * @param name
    * @roseuid 410FAA5303A6
    */
   public WildCardTester(int type, int name) 
   {
    
   }
   
   /**
    * @param type
    * @roseuid 410FAA3A0350
    */
   public WildCardTester(int type) 
   {
    
   }
   
   /**
    * @return xquery.lib.data.type.String
    * @roseuid 410E138503A9
    */
   protected String getName() 
   {
    return null;
   }
   
   /**
    * @return int
    * @roseuid 410E13B50037
    */
   protected int getWildCardType() 
   {
    return 0;
   }
   
   /**
    * @param item
    * @return boolean
    * @roseuid 410F9E6D0161
    */
   public boolean doTest(Item item) 
   {
    return true;
   }
   
   /**
    * @param node
    * @return boolean
    * @roseuid 410FA69B0068
    */
   public boolean doTest(Node node) 
   {
    return true;
   }
   
   /**
    * @return WildCardTester
    * @roseuid 410FA7FE0004
    */
   public static WildCardTester createStarWildCard() 
   {
    return null;
   }
   
   /**
    * @param name
    * @return WildCardTester
    * @roseuid 410FA81903B0
    */
   public static WildCardTester createPrefixWildCard(String name) 
   {
    return null;
   }
   
   /**
    * @param name
    * @return WildCardTester
    * @roseuid 410FA84E02C6
    */
   public static WildCardTester createPostfixWildCard(String name) 
   {
    return null;
   }
}
