/*
 * Created on 11 août 2004
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
package fr.loria.protheo.psgenerator.generator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.loria.protheo.psgenerator.ReturnCodeHandler;
import fr.loria.protheo.psgenerator.Tracer;
import fr.loria.protheo.psgenerator.representation.Program;

/**
 *  The GeneratorManager class
 *
 *      <p align="left"><font color="#003063"><b>Change Log</b></font>
 *      <table border="0" cellpadding="1">
 *        <tr bordercolor="#FF0000" bgcolor="#CCCCCC" align="center"> 
 *          <th width="107"><strong>Date</strong></th>
 *          <th width="67"><strong>Version</strong></th>
 *          <th width="491"><strong>Description</strong></th>
 *        </tr>
 *        <tr align="center"> 
 *          <td>11 août 2004</td>
 *          <td>0</td>
 *          <td align="left"> 
 *            File Creation
 *          </td>
 *        </tr>
 *        <tr bgcolor="#EAEAEA"  align="center"> 
 *          <td>11 août 2004</td>
 *          <td>0.1</td>
 *          <td align="left"> 
 *            Initial Working Version
 *          </td>
 *        </tr>
 *        <tr align="center"> 
 *          <td>15 sept 2004</td>
 *          <td>0.2</td>
 *          <td align="left"> 
 *            Added optionNames and fileExtensions in properties file
 *          </td>
 *        </tr>
 *      </table>
 *
 * @author <a href="mailto:moossen@loria.fr">Michael Moossen</a>
 */
public class GeneratorManager {
	private static final String GENERATORS_BUNDLE_NAME = "fr.loria.protheo.psgenerator.generator.generators";

	private static class GeneratorData {
		public final Class generator;
		public final String fileExtension;
		public final String optionName;
		
		public GeneratorData(Class generator, String fileExtension, String optionName) {
			this.generator = generator;
			this.fileExtension = fileExtension;
			this.optionName = optionName;
		}
	}
	
	private static Map generators = Collections.checkedMap(new HashMap(), Class.class, GeneratorData.class);
	
	public static void registerGenerators() {
		assert(Tracer.trace());
		ResourceBundle bundle = null;
		try {
			bundle = ResourceBundle.getBundle(GENERATORS_BUNDLE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(ReturnCodeHandler.RESOURCEFILE_NOT_FOUND.getId());
		} 		
		
		for (Enumeration e = bundle.getKeys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			Matcher m = Pattern.compile("generator\\.([^\\.]+)\\.classname").matcher(key);
			if (m.matches()) {
				try {
					String genClassName = bundle.getString(key);
					Class generator = Class.forName(genClassName);
					String optionName = m.group(1);
					String fileExtension=bundle.getString("generator."+optionName+".fileextension");
					generators.put(generator, new GeneratorData(generator, fileExtension, optionName));
				} catch (Exception ex) {
					ex.printStackTrace();
					System.exit(ReturnCodeHandler.WRONG_RESOURCE_INFO.getId());
				}
			}
		}		
	}
	
	public static List generators() {
		assert(Tracer.trace());
		return Collections.unmodifiableList(new ArrayList(generators.keySet()));
	}

	public static String getFileExtension(Class generator) {
		assert(Tracer.trace());
		return ((GeneratorData)generators.get(generator)).fileExtension;
	}

	public static String getOptionName(Class generator) {
		assert(Tracer.trace());
		return ((GeneratorData)generators.get(generator)).optionName;
	}

	public static String[] getOptionNames() {
		assert(Tracer.trace());
		String[] optionNames = new String[generators.size()];
		int count = 0;
		for (Iterator it = generators.values().iterator(); it.hasNext();) {
			GeneratorData data = (GeneratorData) it.next();
			optionNames[count] = data.optionName;
			count++;
		}
		return optionNames;
	}

	public static AbstractGenerator getNewGenerator(String option, Program program) {
		assert(Tracer.trace());
		for (Iterator it = generators.values().iterator(); it.hasNext();) {
			GeneratorData data = (GeneratorData) it.next();
			if (data.optionName.equals(option)) {
				try {
					Constructor constructor = data.generator.getConstructor(new Class[] { Program.class });
					return (AbstractGenerator) constructor.newInstance(new Object[] { program });
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}
}
