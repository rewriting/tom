import aterm.*;
import aterm.pure.*;
import adt.address.*;

import java.util.Iterator;
import java.util.HashSet;

public class AddressBook {
  private DataFactory factory;

  private HashSet book;
  
  %include { adt/address/data.tom }
 
  public AddressBook(DataFactory factory) {
    this.factory = factory;
  }

  public DataFactory getDataFactory() {
    return factory;
  }

  public final static void main(String[] args) {
    AddressBook test = new AddressBook(new DataFactory(new PureFactory()));
    test.run();
  }
  
  public void run() {
    book = new HashSet();
    generatePerson(book);
    
    Iterator it = book.iterator();
    while(it.hasNext()) {
    	Person p = (Person) it.next();
    	happyBirthday(p, `date(2003,9,18));
    }
  }

  public void happyBirthday(Person p, Date today) {
    %match(Person p, Date today) {
        person(firstname,lastname,date(year,month,day)),
        date(_,month,day) -> {
        
        System.out.println("Happy Birthday " + firstname );  			  
      }
    }
    
    
 }
  
  public void generatePerson(HashSet set) {
	set.add(`person("John","Smith",date(1965,9,18)));
	set.add(`person("Marie","Muller",date(1986,10,19)));
	set.add(`person("Paul","Muller",date(2000,9,20)));
	}

}

