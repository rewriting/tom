package smoke;

// Tom+Java twin of ../unused_subject.t: compiled by legacy/, its output is
// ../expected.txt (TestJavaTwins).

public class Twin {
  %include { string.tom }
  %include { int.tom }

  public static void main(String[] args) {
    String s = "a";
    int n = 0;
    %match(String s) {
      _ -> { n++; }
      a@b -> { n += 10; }
    }
    int i = 3;
    %match(int i) {
      x -> {
        int k = 4;
        %match(int x, int k) {
          _, _ -> { n += 100; }
        }
      }
    }
    int j = 5;
    %match {
      y << int j && _ << int y -> { n += 1000; }
    }
    System.out.println(n);
  }
}
