import java.util.Iterator;
import java.util.HashSet;
import java.util.Date;
import myadt.*;

public class AddressBook3 {
  private HashSet book;
 
 %typeint
 
 %typeterm Date{
  implement { Date }
  get_fun_sym(t) {null}
  cmp_fun_sym(s1,s2) { false}
  get_subterm(t,n) {null}
}

%op Date date(year:int, month:int, day:int) {
  fsym {}
  is_fsym(t) { t instanceof Date }
  get_slot(year,t) { t.getYear()}
  get_slot(month,t) { t.getMonth()}
  get_slot(day,t) { t.getDate() }
  make(t0, t1, t2) { new Date(t0,t1,t2)}
}

%typeterm Person{
  implement { Person}
  get_fun_sym(t) {null}
  cmp_fun_sym(s1,s2) { false}
  get_subterm(t,n) {null}
  equals(t1,t2) {t1.equals(t2)}
}

%op Person person(firstname:String, lastname:String, birthdate:Date) {
  fsym {}
  is_fsym(t) { t instanceof Person}
  get_slot(firstname,t) { t.getFirstName()}
  get_slot(lastname,t) { t.getLastName()}
  get_slot(birthdate,t) { t.getBirthdate()}
  make(t0, t1, t2) { new Person(t0,t1,t2)}
}

  public final static void main(String[] args) {
    AddressBook3 test = new AddressBook3();
    test.run();
  }
  
  public void run() {
    book = new HashSet();
    generatePerson(book);
    
    Iterator it = book.iterator();
    while(it.hasNext()) {
    	Person p = (Person) it.next();
    	System.out.println("Testing "+p.getFirstName());
    	happyBirthday(p, `date(2003,9,20));
    }
  }

  public void happyBirthday(Person p, Date today) { 
  	%match(Person p, Date today, Person p) {
  		f@person(firstname,lastname,date(year,month,day)), date(_,month,day), f -> {
    			System.out.println("Happy Birthday " + firstname );  			  
    	}
  	}
 }
  
  public void generatePerson(HashSet set) {
  	System.out.println("...");
		set.add(`person("John","Smith",date(1965,9,18)));
		set.add(`person("Marie","Muller",date(1986,10,19)));
		set.add(`person("Paul","Muller",date(2000,9,20)));
  }
  
}

