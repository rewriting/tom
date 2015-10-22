/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the Inria nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
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


import maingxx.m.*;
import maingxx.m.types.*;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TestGxx {
  %include { maingxx/m/m.tom }
	public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(Maingxx.class.getName());
	}
 
  @Before
  public void setUp() {
  }

  Object run(Object t) {

	try{
		Class<?> c = Class.forName("Maingxx");
		Method[] allMethods = c.getDeclaredMethods();
		Pattern p = Pattern.compile(".*mainStrat.*");
		Pattern encodeToMetalevel = Pattern.compile(".*encode.*");
		Pattern decodeFromMetalevel = Pattern.compile(".*decode.*");
		List<Method> matchingMethod = new ArrayList<Method>();
		List<Method> encodeMethod = new ArrayList<Method>();
		List<Method> decodeMethod = new ArrayList<Method>();
		for(Method m: allMethods) {
			if(p.matcher(m.getName()).matches()){
				matchingMethod.add(m);
			}	
			if(encodeToMetalevel.matcher(m.getName()).matches()){
				encodeMethod.add(m);
			}
			if(decodeFromMetalevel.matcher(m.getName()).matches()){
				decodeMethod.add(m);
			}
		}
		
		try{
			matchingMethod.get(0).setAccessible(true);
			Object res = t;
			if(!encodeMethod.isEmpty()) {
				encodeMethod.get(0).setAccessible(true);
				decodeMethod.get(0).setAccessible(true);
				res = encodeMethod.get(0).invoke(null, encodeMethod.get(0).getParameterTypes()[0].cast(res));			
				res = matchingMethod.get(0).invoke(null, matchingMethod.get(0).getParameterTypes()[0].cast(res));
				res = decodeMethod.get(0).invoke(null, decodeMethod.get(0).getParameterTypes()[0].cast(res));
				encodeMethod.get(0).setAccessible(false);
				decodeMethod.get(0).setAccessible(false);
				matchingMethod.get(0).setAccessible(false);
				return res;
			}
			else {	
				res = matchingMethod.get(0).invoke(null, matchingMethod.get(0).getParameterTypes()[0].cast(res));
				matchingMethod.get(0).setAccessible(false);
				return res;
			}
		}
		catch (Exception e){
			 e.printStackTrace();
	 		 return null;
		}

	}
	catch (ClassNotFoundException e) {
	    e.printStackTrace();
	    return null;
	    
	}

  }

  @Test
  public void testGxx1() {
    assertEquals("mainStrat(g(a)) should be a()",
                 `a(), run(`g(a())));
  }
  
  @Test
  public void testGxx2() {
    assertEquals("mainStrat(g(g(a))) should be a()",
                 `a(), run(`g(g(a()))));
  }

  @Test
  public void testGxx3() {
    assertEquals("mainStrat(g(f(g(a),g(b)))) should be f(a,b)",
                 `f(a(),b()), run(`g(f(g(a()),g(b())))));
  }

}
