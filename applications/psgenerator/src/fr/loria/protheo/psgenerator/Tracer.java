/*
 * Created on 3 août 2004
 *
 * Copyright (c) 2004, Michael Moossen
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
package fr.loria.protheo.psgenerator;

import java.io.PrintStream;

/**
 *  The Tracer class, is a helper for tracing method calls while assertions
 *  are enabled. <br> 
 * 
 *  For activating tracing use the java interpreter -ea flag or some of its 
 *  variations. <br>
 *  
 *  For using it just add the following line of code where you want to register
 *  a trace: <br>
 *       <CENTER><code>assert(Tracer.trace());</code></CENTER><br>
 *  Or<br>
 *       <CENTER><code>assert(Tracer.trace(someData));</code></CENTER><br>
 *  <br>
 * 
 *  And you have also somewhere to specify the output stream, as for example:<br>
 *       <CENTER><code>assert(Tracer.setTraceStream(new PrintStream(new FileOutputStream("psgen.trace"))));</code></CENTER><br>
 *  Or<br>
 *       <CENTER><code>assert(Tracer.setTraceStream(System.err));</code></CENTER><br>
 *  <br>
 *
 *      <p align="left"><font color="#003063"><b>Change Log</b></font>
 *      <table border="0" cellpadding="1">
 *        <tr bordercolor="#FF0000" bgcolor="#CCCCCC" align="center"> 
 *          <th width="107"><strong>Date</strong></th>
 *          <th width="67"><strong>Version</strong></th>
 *          <th width="491"><strong>Description</strong></th>
 *        </tr>
 *        <tr align="center"> 
 *          <td>03 aug. 2004</td>
 *          <td>0</td>
 *          <td align="left"> 
 *            File Creation
 *          </td>
 *        </tr>
 *        <tr bgcolor="#EAEAEA"  align="center"> 
 *          <td>03 aug. 2004</td>
 *          <td>1</td>
 *          <td align="left"> 
 *            Initial Working Version
 *          </td>
 *        </tr>
 *      </table>
 *
 * @author <a href="mailto:moossen@loria.fr">Michael Moossen</a>
 */
public class Tracer {
	/**
	 * Comment for <code>traceStream</code>
	 */
	private static PrintStream traceStream = null;
	
	public static boolean setTraceStream(PrintStream stream) {
		traceStream = stream;
		return true;
	}
	
	/**
	 * @param s
	 */
	public static final boolean trace(Object s) {
		if (traceStream==null) return true;
		try {
			throw new Exception();
		} catch (Exception e) { 
			traceStream.println("TRACE: " + e.getStackTrace()[1]);
			if (s==null) s="null";
			String[] outs = s.toString().split("\n");
			for (int i = 0; i < outs.length; i++) {
				traceStream.println("TRACE-DATA! " + outs[i]);
			}
		}
		return true;
	}
	
	/**
	 * 
	 */
	public static final boolean trace() {
		if (traceStream==null) return true;
		try {
			throw new Exception();
		} catch (Exception e) { 
			traceStream.println("TRACE: " + e.getStackTrace()[1]);
		}
		return true;
	}
}
