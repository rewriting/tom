import java.util.Arrays;
import java.util.LinkedList;

public class Mapping {

  %include{ util/LinkedList.tom }

  public static void main(String[] args) {
    LinkedList<Integer> l = new LinkedList(Arrays.asList(1,2,3,1,4,3,1,1));
    System.out.println("list = " + l);

    %match(LinkedList l) {
      (_*,x,_*) -> { System.out.println("interate: " + `x); }
      (_*,x,_*,x,_*) -> { System.out.println("appears twice: " + `x); }
    }
    %match(LinkedList l, LinkedList l) {
      (_*,x,_*), !(_*,x,_*,x,_*) -> { System.out.println("appears only once: " + `x); }
    }
  }
}

