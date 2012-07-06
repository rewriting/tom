package definitions;

/**
 *
 * @author hubert
 */
class Constructor {

  private Algebraic caller;
  private Typable[] fields;

  Constructor(Algebraic caller, Typable[] fields) {
    this.caller = caller;
    this.fields = fields;
  }

  int getDimentionMax() {
    int res = 0;
    for (int i = 0; i < fields.length; i++) {
      res = Math.max(res, fields[i].getDimention());
    }
    return res;
  }
  
  int getSize(){
    return fields.length;
  }
  
  private boolean isRec(){
    for (int i = 0; i < fields.length; i++) {
      Typable field = fields[i];
      if(field.dependsOn(caller)){
        return true;
      }
    }
    return false;
  }
  
  int distanceToReachLeaf(){
    throw new UnsupportedOperationException("not yet implemented");
  }
}