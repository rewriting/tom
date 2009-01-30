package quine;
class S {
  %include { string.tom }
  public static void main(String[]a) {
    String s=%[package quine;
class S {
  %include { string.tom }
  public static void main(String[]a) {
    String s=;
    %match(s) {
      concString(X*,'s','=;',Y*) -> {
        System.out.println(`X*+"s=%"+'['+s+']'+"%;"+`Y*);
      }
    }
  }
}]%;
    %match(s) {
      concString(X*,'s','=;',Y*) -> {
        System.out.println(`X*+"s=%"+'['+s+']'+"%;"+`Y*);
      }
    }
  }
}
