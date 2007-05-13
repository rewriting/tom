import sort.sortedlist.types.*; 

public class Sort {

  %gom {
    module sortedlist
      imports int
      abstract syntax

      Integers = sorted(int*)

      sorted:make_insert(e,l) {
        %match(l) {
          sorted(head,tail*) -> { 
            if(e >= `head) return `realMake(head,sorted(e,tail*)); 
            else return `realMake(e,realMake(head,tail));
          }
        }
      }
  }

  public static void main(String[] args) {
    Integers l1 = `sorted(7,5,3,1,9);
    Integers l2 = `sorted(8,4,6,2,0);
    Integers l3 = `sorted(l1*,10,l2*);
    System.out.println(l1 + "\n" + l2 + "\n" + l3);
  }
}

