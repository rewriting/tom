/* Generated by TOM (version 2.1 source - Under development): Do not edit this file */package jtom.checker;

import java.util.logging.*;

import aterm.*;
import jtom.*;
import jtom.adt.tomsignature.types.*;
import tom.platform.adt.platformoption.types.*;
import tom.platform.*;

/**
 * The TomSyntaxChecker plugin.
 */
public class TomSyntaxChecker extends TomChecker {

  /* Generated by TOM (version 2.1 source - Under development): Do not edit this file *//* Generated by TOM (version 2.1 source - Under development): Do not edit this file *//*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/  /* Generated by TOM (version 2.1 source - Under development): Do not edit this file *//*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/     /*  * old definition of String %typeterm String {   implement           { String }   get_fun_sym(t)      { t }   cmp_fun_sym(s1,s2)  { s1.equals(s2) }   get_subterm(t, n)   { null }   equals(t1,t2)       { t1.equals(t2) } } */ /* Generated by TOM (version 2.1 source - Under development): Do not edit this file *//*  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  */   /* Generated by TOM (version 2.1 source - Under development): Do not edit this file *//*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/   /* Generated by TOM (version 2.1 source - Under development): Do not edit this file *//*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/   /* Generated by TOM (version 2.1 source - Under development): Do not edit this file *//*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/     /* Generated by TOM (version 2.1 source - Under development): Do not edit this file */ 


  /** the declared options string */
  public static final String DECLARED_OPTIONS = "<options><boolean name='NoSyntaxCheck' altName='' description='Do not perform syntax checking' value='false'/></options>";
  
  /**
   * inherited from OptionOwner interface (plugin) 
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomSyntaxChecker.DECLARED_OPTIONS);
  }
  
  /** Constructor */
  public TomSyntaxChecker() {
    super("TomSyntaxChecker");
  }


  public RuntimeAlert run() {
    RuntimeAlert result = new RuntimeAlert();
    if(isActivated()) {
      strictType = !getOptionBooleanValue("lazyType");
      //int errorsAtStart = getStatusHandler().nbOfErrors();
      //int warningsAtStart = getStatusHandler().nbOfWarnings();
      long startChrono = System.currentTimeMillis();
      try {
        // clean up internals
        reinit();
        // perform analyse
        checkSyntax((TomTerm)getWorkingTerm());
        // verbose
        getLogger().log(Level.INFO, "TomSyntaxCheckingPhase",
                        new Integer((int)(System.currentTimeMillis()-startChrono)));      
        //printAlertMessage(errorsAtStart, warningsAtStart);
      } catch (Exception e) {
        getLogger().log(Level.SEVERE, "ExceptionMessage",
                        new Object[]{getClass().getName(),
                                     getStreamManager().getInputFile().getName(),
                                     e.getMessage() });
        e.printStackTrace();
        //result.addError();
      }
    } else {
      // syntax checker desactivated
      getLogger().log(Level.INFO, "SyntaxCheckerInactivated");
    }
    return result;
  }
  
  private boolean isActivated() {
    return !getOptionBooleanValue("NoSyntaxCheck");
  }

} // class TomSyntaxChecker
