/*
 * Copyright (c) 2004-2010, Universite de Lorraine, Inria
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
package tutorial;

import aterm.*;
import aterm.pure.*;
public class PersonSubtype {
  ATermFactory factory;

  public PersonSubtype(ATermFactory factory) {
    this.factory = factory;
  }
  // ------------------------------------------------------------
  public static class Person {

    String firstname;
    String lastname;

    public Person(String firstname, String lastname) {
      this.firstname = firstname;
      this.lastname = lastname;
    }

    public String getFirstname() {
      return firstname;
    }

    public String getLastname() {
      return lastname;
    }

  }

  public static class Woman extends Person {
    String firstname;
    String lastname;
    String lastsinglename;

    public Woman(String firstname, String lastname, String lastsinglename) {
      super(firstname,lastname);
      this.lastsinglename = lastsinglename;

        System.out.println("woman"+ this.lastname + " " + this.lastsinglename +
            " " + this.firstname + " is married.");
    }

    public String getFirstname() {
      return firstname;
    }

    public String getLastsinglename() {
      return lastsinglename;
    }

    public String getLastname() {
      return lastname;
    }
  }

    // ------------------------------------------------------------
  
  %include { string.tom }
  %typeterm Person {
    implement { Person }
    is_sort(t) { $t instanceof Person }
    //equals(t0,t1) { $t0 == $t1 }
  }

  %typeterm Woman extends Person {
    implement { Woman }
    is_sort(t) { $t instanceof Woman }
    //equals(t0,t1) { $t0 == $t1 }
  }

    // ------------------------------------------------------------
  
  %op Person person(firstname:String, lastname:String) {
    is_fsym(t) { $t instanceof Person }
    get_slot(firstname,t) { $t.getFirstname() }
    get_slot(lastname,t) { $t.getLastname() }
    make(t0, t1) {  new Person($t0, $t1) } 
  }

  %op Woman woman(firstname:String, lastname:String, lastsinglename:String) {
    is_fsym(t) { $t instanceof Woman}
    get_slot(firstname,t) { $t.getFirstname() }
    get_slot(lastname,t) { $t.getLastname() }
    get_slot(lastsinglename,t) { $t.getLastsinglename() }
    make(t0, t1, t2) {  new Woman($t0, $t1, $t2) } 
  }
    // ------------------------------------------------------------

  public void print(Person p) {
    //Person p = `person("machin","chose");
    //Person p = `woman("machin","chose","truc");
    %match(p) {
      /* Comment beacuse of compilation of examples with old typer
      woman(first,last,lastsingle)-> {
        if (`last != `lastsingle) {
        System.out.println("woman"+ `last + " " + `first + " is married.");
        }
      }
      */
      person(first,last) -> {
        System.out.println("person "+ `last + " " + `first);
      }
    }
  }

  public final static void main(String[] args) {
    PersonSubtype test = new PersonSubtype(new PureFactory());
    Woman p = `woman("machin","chose","truc");
    test.print(p);
  }
}

