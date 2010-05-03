import teststring.teststring.types.*;
public class TestString{
	%include { sl.tom }

  %gom {
    module TestString
    imports
    int String
      
    abstract syntax
  }
 
  public final static void main(String[] args) {
    String s = "abcaabbccabc";
    int a = 0;
    %match(String s) {
      (_*,'a',_*) -> { a++; }
    }
  }
}
