import examplestring.examplestring.types.*;

public class ExampleString{
	%include { sl.tom }

  %gom {
    module ExampleString
    imports
    int String
      
    abstract syntax
  }
 
  public final static void main(String[] args) {
    String s = "abcaabbccabc";
    int a = 0;
    %match(String s) {
      concString(_*,'a',_*) -> { a++; }
    }
  }
}
