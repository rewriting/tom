import aterm.*;
import aterm.pure.*;
import adt.address.*;


public class AddressBook2 {
  private DataFactory factory;
   
  %include { adt/address/data.tom }
 
  public AddressBook2(DataFactory factory) {
    this.factory = factory;
  }

  public DataFactory getDataFactory() {
    return factory;
  }

  public final static void main(String[] args) {
    AddressBook2 test = new AddressBook2(new DataFactory(new PureFactory()));
    test.run();
  }
  
  public void run() {
    PersonList book = generateBook();
    Date today = `date(2003,9,6);
    happyBirthday(book,today);
  }
 
  public void happyBirthday(PersonList book, Date date) {
    %match(PersonList book, Date date) {
      concPerson(X1*, person(fn, ln, date(y,m,d)), X2*),
        date(_,m,d) -> {
        System.out.println("Happy birthday " + fn + " " + ln);
      }
    }
  }
  
  public PersonList generateBook() {
    return `concPerson(
      person("John","Smith",date(1965,9,18)),
      person("Marie","Muller",date(1986,10,19)),
      person("Paul","Muller",date(2000,11,6))
      );
  }
  
}
