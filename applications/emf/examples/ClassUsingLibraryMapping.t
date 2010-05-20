import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;

import org.eclipse.example.library.*;

import tom.library.sl.*;

class ClassUsingLibraryMapping {

  %include{ librarymapping.tom }

  %include { sl.tom }

  %strategy stratPrintBook() extends Fail() {
    visit Book {
      Book(name, _, _, _) -> { System.out.println(`name); }
    }
  }

  public static void main(String[] args) throws VisitFailure {
    Book b1=`Book("Book 1", 420, ScienceFiction(), null);
    Book b2=`Book("Book 2", 300, Mystery(), null);
    Book b3=`Book("Book 3", 500, Biography(), null);
    Writer w1=`Writer("Writer 1", BookEList(b1,b2));
    Writer w2=`Writer("Writer 2", BookEList(b3));
    b1.setAuthor(w1);
    b2.setAuthor(w1);
    b3.setAuthor(w2);
    Library l = `Library("Library name", WriterEList(w1,w2), BookEList(b1,b2,b3));

    // print all book names from library l
    System.out.println("%match :");
    %match (l) {
      Library(_, _, BookEList(_*, Book(name, _, _, _), _*)) -> {
        System.out.println(`name);
      }
    }

    // same thing using strategies and a containment instrospector
    System.out.println("strategies :");
    `TopDown(Try(stratPrintBook())).visitLight(l, new EcoreContainmentIntrospector());
  }
}
