/*
 * Copyright (c) 2004-2011, INPL, INRIA
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
 *  - Neither the name of the INRIA nor the names of its
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
package bytecode;

/* 
 *  Secure class loader that replaces all call to read() by secureRead()
 *  option of java command : java -Djava.system.class.loader=bytecode.SClassLoader
 */

import com.sun.xacml.finder.impl.FilePolicyModule;

public class SClassLoader extends ClassLoader {

  public SClassLoader(ClassLoader parent) {
    super(parent);
  }

  public SClassLoader() {
    super();
  }

  public synchronized Class loadClass(String name) throws ClassNotFoundException{
    if (!(name.startsWith("java.")) && !(name.equals("SecureAccess")) 
        && !(name.startsWith("javax.")) && !(name.startsWith("com."))
        && !(name.startsWith("sun.")) && !(name.startsWith("org."))
        && !(name.equals("bytecode.SecureAccess"))) {
      //Transformer t = new Transformer();
      Transformer2 t = new Transformer2();
      byte[] scode = t.transform(name);
      Class sClass = defineClass(name,scode, 0, scode.length) ;
      return loadClass(name,true);
        }
    else {
      Class sClass = loadClass(name,false);
      return sClass;
    }
  }

}
