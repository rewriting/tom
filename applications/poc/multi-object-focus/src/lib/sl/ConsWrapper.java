package lib.sl;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 16/11/12
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
import tom.library.sl.Visitable;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class ConsWrapper implements Visitable {
    private Visitable   x;
    private Visitable[] cachedChildren = null;
    private Visitable   empty          = null;


    public Visitable getTerm() { return x; }

    public String toString() {
        return x.toString();
    }

    public Visitable[] getCachedChildren()
    { if (cachedChildren == null)
      {
          ArrayList<Visitable> children = new ArrayList<Visitable>(10);
          Visitable tail  = x;
          int       pos   = 0;

          while (tail.getChildCount() == 2)
          {
              children.add(tail.getChildAt(0));
              tail = tail.getChildAt(1);
          }

          if (tail.getChildCount() == 0) empty = tail;
          else throw new RuntimeException("ConsWrapper: " + tail.toString() + " should be empty");

//          System.out.println("<getChildren>" + children.toString() + "</getChildren>");
          return children.toArray(new Visitable[0]);
      }
      return cachedChildren ;
    }

    public static Visitable unwrap(Visitable y) {
        if (y instanceof ConsWrapper) { return ((ConsWrapper) y).getTerm(); }
        else return y;
    }

    private ConsWrapper(Visitable term) {
       x = term;
    }

    public int getChildCount() {
        return getCachedChildren().length;
    }

    public Visitable getChildAt(int i) {
        return getCachedChildren()[i];
    }

    public Visitable setChildAt(int i, Visitable y) {
        int       pos   = 0;
        Visitable tail  = x;


        // must contains the Heads from position 0 to i (not included)
        Visitable[] upToi = new Visitable[i];


        // We store in upToi all the Cons from pos = 0 to i (included)
        for (pos = 0; pos < i; pos++)
        {
            upToi[pos] = tail.getChildAt(0);
            tail       = tail.getChildAt(1);
        }

        // No that we have the Cons at position i, we set y as the head, tail unchanged.
        tail = tail.setChildAt(0, y);

        // We have to rebuild the term: i.e. applying recursively all the Cons we stored.
        for (pos = i - 1; pos >= 0; pos--)
        {
            tail = x.setChildren(new Visitable[]{upToi[pos], tail});
        }

        //System.out.println("<setChildAt><At>" + i + "</At><Value>" + tail.toString() + "</Value></setChildAt>");
        return tail;
    }

    public Visitable[] getChildren() {
        return getCachedChildren().clone();
    }

    public Visitable setChildren(Visitable[] children) {
        if (x.getChildCount() == 0) throw new RuntimeException("ConsWrapper: can not build non empty conc from empty ones");

        Visitable res  = empty;
        for (int pos = children.length - 1; pos >= 0; pos--)
            res = x.setChildren(new Visitable[] { children[pos] , res });

//        System.out.println("<setChildren>" + res + "</setChildren>");
        return res;
    }

    public static Visitable mk(Visitable x) {
        //System.out.print("<ConsWrapper-input>" + x.toString() + "</ConsWrapper-input>");
        try { Field f = x.getClass().getDeclaredField("symbolName");
              f.setAccessible(true);
              Object v = f.get(x);
              if (v instanceof String)
              {  String s = (String)v;
                 if (s.startsWith("Cons")) return new ConsWrapper(x);
                 else {
                        //System.out.print("<NotConsBut>" + s + "</NotConsBut>");
                        return x;
                      }
              }
              else {
                     //System.out.print("<NotStringBut>" + v.getClass().getName() + "</NotStringBut>");
                     return x;
                   }
        }
        catch (NoSuchFieldException   e)
        {
            //System.out.print("<NoSuchFieldException>" + x.getClass().getName() + "</NoSuchFieldException>");
            return x;
        }
        catch (IllegalAccessException e)
        {
            //System.out.print("<IllegalAccessException/>");
            return x;
        }

    }
}
