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
    public Visitable x;

    private ConsWrapper(Visitable term) {
       x = term;
    }

    public int getChildCount() {
        Visitable tail  = x;
        int       count = 0;
        while (tail.getChildCount() == 2)
        { count += 1;
          tail = tail.getChildAt(2);
        }
        System.out.println("<getChildCount>" + (count + tail.getChildCount()) + "</getChildCount>");
        return count + tail.getChildCount();
    }

    public Visitable getChildAt(int i) {
        Visitable tail  = x;
        for (int pos = 0; pos < i; pos++) tail = tail.getChildAt(1);
        System.out.println("<getChildAt><At>" + i + "</At><Value>" + tail.getChildAt(0).toString() + "</Value></getChildAt>");
        return tail.getChildAt(0);
    }

    public Visitable setChildAt(int i, Visitable y) {
        int       pos   = 0;
        Visitable tail  = x;


        // must contains the Cons from position 0 to i (not included)
        Visitable[] upToi = new Visitable[i];


        // We store in upToi all the Cons from pos = 0 to i (included)
        for (pos = 0; pos < i; pos++)
        {
            upToi[pos] = tail;
            tail       = tail.getChildAt(1);
        }

        // No that we have the Cons at position i, we set y as the head, tail unchanged.
        tail = tail.setChildAt(0, y);

        // We have to rebuild the term: i.e. applying recursively all the Cons we stored.
        for (pos = i - 1; pos >= 0; pos--)
        {
            tail = upToi[pos].setChildren(new Visitable[] { upToi[pos].getChildAt(0)  , tail });
        }

        System.out.println("<setChildAt><At>" + i + "</At><Value>" + tail.toString() + "</Value></setChildAt>");
        return tail;
    }

    public Visitable[] getChildren() {
        ArrayList<Visitable> children = new ArrayList<Visitable>(10);
        Visitable tail  = x;
        int       pos   = 0;
        while (tail.getChildCount() == 2)
        { children.add(tail.getChildAt(0));
          tail = tail.getChildAt(1);
        }
        System.out.println("<getChildren>" + children.toString() + "</getChildren>");
        return children.toArray(new Visitable[0]);
    }

    public Visitable setChildren(Visitable[] children) {
        int       pos   = 0;
        Visitable tail  = x;

        // must contains the Cons from position 0 to i (not included)
        Visitable[] spine = new Visitable[children.length];

        // We store in upToi all the Cons from pos = 0 to i (included)
        for (pos = 0; pos < children.length - 1 ; pos++)
        {   spine[pos] = tail;
            tail       = tail.getChildAt(1);
        }

        // Tail is now empty

        // We have to rebuild the term: i.e. applying recursively all the Cons we stored.
        for (pos = children.length - 1 ; pos >= 0 ; pos--)
            tail = spine[pos].setChildren(new Visitable[] { children[pos]  , tail });

        System.out.println("<setChildren>" + tail + "</setChildren>");
        return tail;
    }

    public static Visitable mk(Visitable x) {
        System.out.print("<ConsWrapper-input>" + x.toString() + "</ConsWrapper-input>");
        try { Field f = x.getClass().getDeclaredField("symbolName");
              f.setAccessible(true);
              Object v = f.get(x);
              if (v instanceof String)
              {  String s = (String)v;
                 if (s.startsWith("Cons")) return new ConsWrapper(x);
                 else { System.out.print("<NotConsBut>" + s + "</NotConsBut>");
                        return x;
                      }
              }
              else { System.out.print("<NotStringBut>" + v.getClass().getName() + "</NotStringBut>");
                     return x;
                   }
        }
        catch (NoSuchFieldException   e) { System.out.print("<NoSuchFieldException>" + x.getClass().getName() + "</NoSuchFieldException>");  return x; }
        catch (IllegalAccessException e) { System.out.print("<IllegalAccessException/>");return x; }

    }
}
