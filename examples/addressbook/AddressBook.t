package AddressBook;

import aterm.*;
import aterm.pure.*;
import adt.address.data.*;
import adt.address.data.types.*;

import java.util.Iterator;
import java.util.HashSet;

public class AddressBook {
  private Factory factory;

  private HashSet book;
  
  %include { data.tom }
  
  public AddressBook(Factory factory) {
    this.factory = factory;
  }

  public Factory getDataFactory() {
    return factory;
  }

  public final static void main(String[] args) {
    AddressBook test = new AddressBook(new Factory(new PureFactory()));
    test.run();
    System.out.println("res = " + test.checkHappyBirthday());
  }
  
  public void run() {
    book = new HashSet();
    generatePerson(book);
    
    Iterator it = book.iterator();
    while(it.hasNext()) {
      Person p = (Person) it.next();
      happyBirthday(p, `date(2004,3,27));
    }
  }

  public void happyBirthday(Person p, Date today) {
    %match(Person p, Date today) {
      person(firstname, _ ,date(year,month1,day1)),
      date(_,month2,day2) -> {
        if(month1==month2 && day1==day2) {
          System.out.println("Happy Birthday " + firstname );
        }  			  
      }
    }
  }
  
  public void generatePerson(HashSet set) {
    set.add(`person("John","Smith",date(1965,3,27)));
    set.add(`person("Marie","Muller",date(1986,3,26)));
    set.add(`person("Paul","Muller",date(2000,1,27)));
	}

  public boolean checkHappyBirthday() {
    book = new HashSet();
    generatePerson(book);
    boolean res = true;
    Iterator it = book.iterator();
    while(it.hasNext()) {
      Person p = (Person) it.next();
      Date d = p.getBirthdate();
      %match(Person p, Date d) {
        person(firstname, _ ,date(year,month1,day1)), date(_,month2,day2) -> {
            //res = res && (month1==month2) && (day1==day2);
        }  			  
      }
    }
    return res;
  }

}

