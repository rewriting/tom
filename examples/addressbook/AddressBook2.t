import aterm.*;
import aterm.pure.*;
import adt.address.data.*;
import adt.address.data.types.*;


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
    Date today = `date(2003,11,6);
    happyBirthday(book,today);
  }
 
  public void happyBirthday(PersonList book, Date date) {
    %match(PersonList book, Date date) {
      concPerson(_*, person(fn, ln, date(y,m,d)), _*),
        date(_,m,d) -> {
        System.out.println("Happy birthday " + fn + " " + ln);
      }
    }
  }
  
  public PersonList generateBook() {
    return `concPerson(
      person("John","Smith",date(1965,9,18)),
      person("Marie","Muller",date(1986,9,6)),
      person("Paul","Muller",date(2000,11,6))
      );
  }
  
}
