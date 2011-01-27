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

package addressbook;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

import addressbook.data.*;
import addressbook.data.types.*;

import java.util.Iterator;
import java.util.HashSet;

public class TestAddressBook {
  private AddressBook1 test;
  private HashSet<Person> book;

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestAddressBook.class.getName());
  }

  @Before
  public void setUp() {
    test = new AddressBook1();
    book = new HashSet<Person>();
    AddressBook1.generatePerson(book);
  }

  %include { data/Data.tom }

  @Test
  public void testBirthdate() {
    Iterator it = book.iterator();
    while(it.hasNext()) {
      Person p = (Person) it.next();
      Date d = p.getbirthdate();
      %match(p, d) {
        person(_, _ ,date(_,month1,day1)), date(_,month2,day2) -> {
           Assert.assertTrue((`month1==`month2) && (`day1==`day2));
         }
      }
    }
  }

  @Test
  public void testAddressBook2() {
    PersonList personList = AddressBook2.generateBook();
    Date today = `date(2003,3,27);
    String name = AddressBook2.happyBirthday(personList,today);
    Assert.assertNotNull(name);
    Assert.assertEquals("John",name);
  }
}
