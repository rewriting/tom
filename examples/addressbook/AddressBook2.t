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
