package ppeditor;

public class Main {

  public static void main(String[] args){

  String texte1="Première ligne";
  String texte2="Troisième ligne\nEt quatrième si tout va bien";
  PPCursor aCursor = new PPCursor(0,0);

  aCursor.write(texte1);
  aCursor.setPosition(new PPTextPosition(2,0));
  aCursor.write(texte2);

  aCursor.dump("./Test.txt");
  }
}
