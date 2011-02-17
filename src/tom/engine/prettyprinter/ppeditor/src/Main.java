package ppeditor;

public class Main {

  public static void main(String[] args){

  PPCursor aCursor = new PPCursor(0,0);
  String text="";
  aCursor.setPosition(new PPTextPosition(0,0));
  aCursor.write(text);

  /*Step one: writing were there was nothing before (insertion mode)*/
  String text1="Première ligne";
  String text2="Troisième ligne\n\nEt cinquième si tout va bien";

  aCursor.write(text1);
  aCursor.setPosition(new PPTextPosition(2,2));
  aCursor.write(text2);

  /*Step two: writing inside of a line (insertion mode)*/
  String text3="Septième ligne";
  String text4="// Insertion entre \"Sept\" et \"ième ligne\" //";

  aCursor.setPosition(new PPTextPosition(6,0));
  aCursor.write(text3);
  aCursor.setPosition(new PPTextPosition(6,4));
  aCursor.write(text4);

  /*Step three: writing inside of a line, part 2 (erase mode)*/
  String text5="Neuvième ligne";
  String text6="x";
  String text7="// Efface le mot \"ligne\" //";

  aCursor.setPosition(new PPTextPosition(8,0));
  aCursor.write(text5);
  aCursor.setInsertion(false);
  aCursor.setPosition(new PPTextPosition(8,3));
  aCursor.write(text6);
  aCursor.setPosition(new PPTextPosition(8,9));
  aCursor.write(text7);
  
  aCursor.dump("./Test.txt");
  }
}
