package AddressBook;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import aterm.*;
import aterm.pure.*;
import AddressBook.data.*;
import AddressBook.data.types.*;

import java.util.Iterator;
import java.util.HashSet;

public class TestAddressBook extends TestCase {
  private AddressBook test;
  private HashSet book;
  private Factory factory;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestAddressBook.class));
	}

  public void setUp() {
    factory = new Factory(new PureFactory());
    test = new AddressBook(factory);
    book = new HashSet();
    test.generatePerson(book);
  }

  %include { data.tom }
  public Factory getDataFactory() {
    return factory;
  }

  public void testBirthdate() {
    Iterator it = book.iterator();
    while(it.hasNext()) {
      Person p = (Person) it.next();
      Date d = p.getBirthdate();
      %match(Person p, Date d) {
        person(firstname, _ ,date(year,month1,day1)), date(_,month2,day2) -> {
           assertTrue((month1==month2) && (day1==day2));
         }  			  
      }
    }
  }
    
}

