/*
 * Created on 15 sept. 2004
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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 *  The ReturnCodeHandler class
 *
 *      <p align="left"><font color="#003063"><b>Change Log</b></font>
 *      <table border="0" cellpadding="1">
 *        <tr bordercolor="#FF0000" bgcolor="#CCCCCC" align="center"> 
 *          <th width="107"><strong>Date</strong></th>
 *          <th width="67"><strong>Version</strong></th>
 *          <th width="491"><strong>Description</strong></th>
 *        </tr>
 *        <tr align="center"> 
 *          <td>15 sept. 2004</td>
 *          <td>0</td>
 *          <td align="left"> 
 *            File Creation
 *          </td>
 *        </tr>
 *        <tr bgcolor="#EAEAEA"  align="center"> 
 *          <td>15 sept. 2004</td>
 *          <td>0.1</td>
 *          <td align="left"> 
 *            Initial Working Version
 *          </td>
 *        </tr>
 *      </table>
 *
 * @author <a href="mailto:moossen@loria.fr">Michael Moossen</a>
 */
public class ReturnCodeHandler {
	public static ReturnCodeHandler BAIL_OUT_OPTION = new ReturnCodeHandler(1, "bail out option execution successful");
	public static ReturnCodeHandler NO_ERROR = new ReturnCodeHandler(0, "execution successful");
	public static ReturnCodeHandler INCORRECT_USAGE = new ReturnCodeHandler(-1, "incorrect usage");
	public static ReturnCodeHandler UNKNOWN_ERROR = new ReturnCodeHandler(-2, "unknown error");
	public static ReturnCodeHandler RESOURCEFILE_NOT_FOUND = new ReturnCodeHandler(-3, "resource file not found");	
	public static ReturnCodeHandler WRONG_RESOURCE_INFO = new ReturnCodeHandler(-4, "wrong resource info");	

	private int id;
	private String desc;
	
	private ReturnCodeHandler(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}
	
	public int getId() {
		return id;
	}
	public String getDesc() {
		return desc;
	}
	private static String newTabSpace(int chars, int spaces) {
		String ret = "";
		for(int i=0; i<spaces-chars; i++) {
			ret += " ";
		}
		return ret;
	}
	public String toString() {
		String sp = newTabSpace(String.valueOf(id).length(), 3);
		return sp + id +newTabSpace(String.valueOf(id).length(), 8-sp.length())+desc;
	}
	public static List getList() {
		List ret = new Vector();
		ret.add(BAIL_OUT_OPTION);
		ret.add(NO_ERROR);
		ret.add(INCORRECT_USAGE);
		ret.add(UNKNOWN_ERROR);
		ret.add(RESOURCEFILE_NOT_FOUND);
		ret.add(WRONG_RESOURCE_INFO);
		return ret;
	}
	public static String getSummary() {
		List list = getList();
		String ret = "code"+newTabSpace(4, 8)+"description"+System.getProperty("line.separator");
		for (Iterator it = list.iterator(); it.hasNext();) {
			ReturnCodeHandler handler = (ReturnCodeHandler) it.next();
			ret += handler.toString()+System.getProperty("line.separator");
		}
		return ret;
	}
}
