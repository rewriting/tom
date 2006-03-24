package meta;

public class GenHello {
  static String version = "v12";
  public static void main(String[] args) {
    String prg="";

    prg+="public class Hello {\n";
    prg+="\tpublic static void main(String[] args) {\n";
    prg+="\t\tSystem.out.println(\"Hello\\n\\tWorld\t"+version+"\");\n";
    prg+="\t}\n";
    prg+="}\n";

    System.out.println(prg);


    String prg2=%[
public class Hello {
  public static void main(String[] args) {
    System.out.println("Hello\n\tWorld      @version@");
  }
}
      ]%;
    
    System.out.println(prg2);
  }
}
