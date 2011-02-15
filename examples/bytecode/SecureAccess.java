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

import com.sun.xacml.*;
import com.sun.xacml.ctx.*;
import com.sun.xacml.finder.*;
import com.sun.xacml.finder.impl.*;

import java.util.*;
import java.io.*;


public class SecureAccess {


  public SecureAccess(){
    System.out.println("Creation of a secure access");
  }

  public int sread(String filename){
    System.out.println("Secured reading request: ");
    File f = new File(filename);    try{
      if(enforcePolicy(filename)){
        FileReader r = new FileReader(filename);
        return r.read(); 
      }
    }catch(Exception e){
      e.printStackTrace();
    }
    //error code for security policy deny 
    return -2;
  }

  public boolean enforcePolicy (String file) throws Exception{

    // setup the policy finder 
    FilePolicyModule policies = new FilePolicyModule();
    policies.addPolicy("bytecode/policy.xml");
    PolicyFinder policyFinder = new PolicyFinder();
    Set<FilePolicyModule> policyModules = new HashSet<FilePolicyModule>();
    policyModules.add(policies);
    policyFinder.setModules(policyModules);

    // module to provide the current date & time
    CurrentEnvModule envModule = new CurrentEnvModule();

    // setup the attribute finder
    AttributeFinder attrFinder = new AttributeFinder();
    List<CurrentEnvModule> attrModules = new ArrayList<CurrentEnvModule>();
    attrModules.add(envModule);
    attrFinder.setModules(attrModules);

    // create the PDP
    PDP pdp = new PDP(new PDPConfig(attrFinder, policyFinder, null));

    //replace in the request model [filename] vc static void main(String[] args) throws IOException { 

    BufferedReader in = new BufferedReader(new FileReader("bytecode/requestModel.xml")); 
    BufferedWriter out = new BufferedWriter(new FileWriter(file+"Request.xml")); 
    String s;
    while ((s = in.readLine()) != null){
      out.write(s.replaceAll("filename",file)); 
      out.newLine();
    }
    in.close(); 
    out.close(); 

    // now work on the request
    RequestCtx request = RequestCtx.getInstance(new FileInputStream(file+"Request.xml"));
    ResponseCtx response = pdp.evaluate(request);
    // we print out the result
    response.encode(System.out);
    Iterator results = response.getResults().iterator();
    //if one result is deny, return false
    while(results.hasNext()){
      if(((Result)results.next()).getDecision()!=Result.DECISION_PERMIT) return false; 
    }
    return true; 
    }

  }
