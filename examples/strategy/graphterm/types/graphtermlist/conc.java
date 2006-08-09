
package strategy.graphterm.types.graphtermlist;

public abstract class conc extends strategy.graphterm.types.GraphTermList {


  public int length() {
    int count = 0;
    if(this instanceof strategy.graphterm.types.graphtermlist.Consconc) {
      strategy.graphterm.types.GraphTermList tl = ((strategy.graphterm.types.graphtermlist.Consconc)this).getTailconc();
      if (tl instanceof conc) {
        return 1+((conc)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

 public strategy.graphterm.types.GraphTerm[] toArray() {
    strategy.graphterm.types.GraphTerm[] array;
    if(this instanceof strategy.graphterm.types.graphtermlist.Consconc) {
      strategy.graphterm.types.GraphTerm h = ((strategy.graphterm.types.graphtermlist.Consconc)this).getHeadconc();
      strategy.graphterm.types.GraphTermList tl = ((strategy.graphterm.types.graphtermlist.Consconc)this).getTailconc();
      if (tl instanceof conc) {
        strategy.graphterm.types.GraphTerm[] tailArray =((conc)tl).toArray();
        array = new strategy.graphterm.types.GraphTerm[1+tailArray.length];
        array[0]=h;     
        for(int i =0;i<tailArray.length;i++){
          array[i+1]=tailArray[i];
        }
      } else {
        array = new strategy.graphterm.types.GraphTerm[1];
        array[0]=h;  
      }
    } else {
        array = new strategy.graphterm.types.GraphTerm[0];
    }
    return array;
  }

  public strategy.graphterm.types.GraphTermList reverse() {
    if(this instanceof strategy.graphterm.types.graphtermlist.Consconc) {
      strategy.graphterm.types.GraphTermList cur = this;
      strategy.graphterm.types.GraphTermList rev = strategy.graphterm.types.graphtermlist.Emptyconc.make();
      while(cur instanceof strategy.graphterm.types.graphtermlist.Consconc) {
        rev = strategy.graphterm.types.graphtermlist.Consconc.make(((strategy.graphterm.types.graphtermlist.Consconc)cur).getHeadconc(),rev);
        cur = ((strategy.graphterm.types.graphtermlist.Consconc)cur).getTailconc();
      }
      return rev;
    } else {
      return this;
    }
  }


}
