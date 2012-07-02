package teststruct;

import definitions.Field;
import definitions.Type;

public class Main {

  public static void main(String[] args) {
    Type tree = Type.declare();
    Field[] tab = {new Field(tree, true)};
    tree.addConstructor(tab);
  }
}
