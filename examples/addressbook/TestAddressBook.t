package AddressBook;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import aterm.*;
import aterm.pure.*;
import adt.address.data.*;
import adt.address.data.types.*;

public class TestAddressBook extends TestCase {
  private AddressBook test;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestAddressBook.class));
	}

  public void setUp() {
    this.test = new AddressBook(new Factory(new PureFactory()));
  }

  public void testBirthdate() {
    assertTrue(test.checkHappyBirthday());
  }
    
}

