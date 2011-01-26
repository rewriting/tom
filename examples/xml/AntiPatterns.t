/*
 * Copyright (c) 2004-2011, INPL, INRIA
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
 * 	- Neither the name of the INRIA nor the names of its
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

package xml;

import java.io.File;

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

public class AntiPatterns {

	%include{ adt/tnode/TNode.tom }

	private XmlTools xtools;

	public static void main (String args[]) {
		AntiPatterns person = new AntiPatterns();
				
		String fileSep = System.getProperty("file.separator");
		// for launching from eclipse  		
		String path = System.getProperty("user.dir") + fileSep
			+ "workspace" + fileSep
			+ "jtom" + fileSep
			+ "examples" + fileSep
			+ "build" + fileSep
			+ "xml" + fileSep
			+ "person.xml";		
		
		File file = new File(path);
		System.out.println("path:" + path);
		if (!file.exists()){
			path = "xml/person.xml";
		}		
		person.run(path);
	}

	private void run(String filename){
		
		xtools = new XmlTools();
		TNode term = (TNode)xtools.convertXMLToTNode(filename);

		TNode result = run(term.getDocElem());
	}

	private TNode run(TNode subject) {
		System.out.println("Not normal persons:");
		%match(TNode subject) {
			<Persons>
					t@!<Person></Person>
			</Persons> -> {				
				xtools.printXMLFromTNode(`t);
				System.out.println("");
			}
		}
		
		System.out.println("Not normal persons that have age=21:");
		%match(TNode subject) {
			<Persons>
					t@!<Person Age="21"></Person>
			</Persons> -> {				
				xtools.printXMLFromTNode(`t);
				System.out.println("");
			}
		}
		
		System.out.println("Not normal persons or normal persons that have age=21:");
		%match(TNode subject) {
			<Persons>
					t@!<Person Age=!"21"></Person>
			</Persons> -> {				
				xtools.printXMLFromTNode(`t);
				System.out.println("");
			}
		}
		return subject;
	}

}

