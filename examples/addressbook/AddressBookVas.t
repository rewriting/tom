package AddressBook;

import aterm.*;
import aterm.pure.*;
import AddressBook.person.*;
import AddressBook.person.types.*;

public class AddressBookVas {
  private Factory factory;

  %vas {
      //
      // extension of adt syntax
      //
    module person
      imports int str
      
    public
      sorts Date Person PersonList

    abstract syntax
      date( day:Int, month:Int, year:Int ) -> Date
      person(firstname:String,lastname:String, birthdate:Date) -> Person
      concPerson( Person* ) -> PersonList
      bob -> Person
      alice() -> Person
  }
 
  public AddressBookVas(Factory factory) {
    this.factory = factory;
  }
 
  public Factory getPersonFactory() {
    return factory;
  }

  public final static void main(String[] args) {
    AddressBookVas test = new AddressBookVas(new Factory(new PureFactory()));
    test.run();
  }
  
  public void run() {
    PersonList book = generateBook();
    Date today = `date(2003,3,27);
    happyBirthday(book,today);
  }
 
  public void happyBirthday(PersonList book, Date date) {
    %match(PersonList book, Date date) {
      concPerson(_*, person(firstname, _, date(year,month,day)), _*),
        date(_,month,day)   -> {
        System.out.println("Happy birthday " + `firstname);
      }
    }
  }
  
  public PersonList generateBook() {
    return `concPerson(
      person("John","Smith",date(1965,3,27)),
      person("Marie","Muller",date(1986,3,26)),
      person("Paul","Muller",date(2000,1,27))
      );
  }
  
}
