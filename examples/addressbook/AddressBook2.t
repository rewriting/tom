/*
 * Copyright (c) 2004, INRIA
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

package addressbook;

import aterm.*;
import aterm.pure.*;
import addressbook.data.*;
import addressbook.data.types.*;


public class AddressBook2 {
  private Factory factory;
   
  %include { data.tom }
 
  public AddressBook2(Factory factory) {
    this.factory = factory;
  }
 
  public Factory getDataFactory() {
    return factory;
  }

  public final static void main(String[] args) {
    AddressBook2 test = new AddressBook2(new Factory(new PureFactory()));
    test.run();
  }
  
  public void run() {
    PersonList book = generateBook();
    Date today = `date(2003,3,27);
    happyBirthday(book,today);
  }
 
  public void happyBirthday(PersonList book, Date date) {
    %match(PersonList book, Date date) {
      concPerson(_*, person(firstname, _, date(_,month,day)), _*),
        date(_,month,day)   -> {
        System.out.println("Happy birthday " + `firstname);
      }
    }
  }
  
  public PersonList generateBook() {
    return `concPerson(
      person("John","Smith",date(1965,3,27)),
      person("Marie","Muller",date(1986,3,28)),
      person("Paul","Muller",date(2000,1,27))
      );
  }
  
}
