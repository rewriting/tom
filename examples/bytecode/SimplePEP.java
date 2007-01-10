/*
 * Copyright (c) 2004-2006, INRIA
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


import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.sun.xacml.EvaluationCtx;
import com.sun.xacml.attr.AnyURIAttribute;
import com.sun.xacml.attr.RFC822NameAttribute;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.ctx.Result;
import com.sun.xacml.ctx.Subject;


public class SimplePEP {

  public static HashSet setupSubjects() throws URISyntaxException {
    HashSet attributes = new HashSet();

    // setup the id and value for the requesting subject
    URI subjectId =
      new URI("urn:oasis:names:tc:xacml:1.0:subject:subject-id");
    RFC822NameAttribute value =
      new RFC822NameAttribute("seth@users.example.com");

    // create the subject section with two attributes, the first with
    // the subject's identity...
    attributes.add(new Attribute(subjectId, null, null, value));
    // ...and the second with the subject's group membership
    attributes.add(new Attribute(new URI("group"),
          "admin@users.example.com", null,
          new StringAttribute("developers")));

    // bundle the attributes in a Subject with the default category
    HashSet subjects = new HashSet();
    subjects.add(new Subject(attributes));

    return subjects;
  }

  public static Set setupResource(String file) throws URISyntaxException {
    HashSet resource = new HashSet();

    // the resource being requested
    //AnyURIAttribute value =
    //    new AnyURIAttribute(new URI("http://server.example.com/"));
    AnyURIAttribute value = new AnyURIAttribute (new URI (file));

    // create the resource using a standard, required identifier for
    // the resource being requested
    resource.add(new Attribute(new URI(EvaluationCtx.RESOURCE_ID),
          null, null, value));

    return resource;
  }

  public static Set setupAction() throws URISyntaxException {
    HashSet action = new HashSet();

    // this is a standard URI that can optionally be used to specify
    // the action being requested
    URI actionId =
      new URI("urn:oasis:names:tc:xacml:1.0:action:action-id");

    // create the action
    action.add(new Attribute(actionId, null, null,
          new StringAttribute("commit")));

    return action;
  }


  public void enforcePolicy (String file){
    //RequestCtx instance represents an XACML Request 
    //The Request constructor takes four inputs, 
    //which represent the four categories of Attributes in a Request (Subject, Resource, Action, Environment)
    HashSet subjects=null;
    try {
      subjects = setupSubjects();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HashSet resourceAttrs = null;
    try {
      resourceAttrs = (HashSet) setupResource(file);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HashSet actionAttrs = null;
    try {
      actionAttrs = (HashSet) setupAction();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    HashSet environmentAttrs = new HashSet();

    RequestCtx request = new RequestCtx(subjects, resourceAttrs,
        actionAttrs, environmentAttrs);

    //pass request to PDP:
    SimplePDP myPDP = new SimplePDP();
    //ResponseCtx represents an XACML Response. It is a collection of Results. 
    //A Result contains the Decision, a status code, and optionally a message or Obligations. 
    ResponseCtx response = myPDP.evaluate(request);
    Set rs = response.getResults();
    Iterator it = rs.iterator();
    Result r =(Result)it.next();
    System.out.println("Decision : "+r.getDecision());
  }

}
