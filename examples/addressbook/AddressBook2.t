import aterm.*;
import aterm.pure.*;
import adt.address.*;


public class AddressBook2 {
  private DataFactory factory;

  private PersonList book;
  
  %include { adt/address/data.tom }
 
  public AddressBook2(DataFactory factory) {
    this.factory = factory;
    generateBook();
  }

  public DataFactory getDataFactory() {
    return factory;
  }

  public final static void main(String[] args) {
    AddressBook2 test = new AddressBook2(new DataFactory(new PureFactory()));
    test.run();
  }
  
  public void run() {
    	removeDouble();
  }

  public void removeDouble() {
  	%match(PersonList book) {
  		concPerson(X1*, p@person(fn, ln, date(y,m,d)), X2*, person(fn, ln, date(y,m,d)), X3*) -> {
    			System.out.println("Removing " + fn + " " + ln);
    			book = `concPerson(X1*, p, X2*, X3*); 
    	}
    	emptyPersonList -> {
    		System.out.println("Empty book");
    	}
  	}
 }
  
  public void generateBook() {
 		book = factory.makePersonList();
		book = book.insert(`person("John","Smith",date(1965,9,18)));
		book = book.insert(`person("Marie","Muller",date(1986,10,19)));
		book = book.insert(`person("Paul","Muller",date(2000,9,20)));
		book = book.insert(`person("Marie","Muller",date(1986,10,19)));
  }
  
}