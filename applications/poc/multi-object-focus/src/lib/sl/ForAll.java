package lib.sl;

import lib.Fun;
import lib.MOFException;
import lib.Zip;
import tom.library.sl.Visitable;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 08/11/12
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */

public class ForAll extends Visitor<Visitable,Visitable> {
    public <Ans,T> Visitable visitZK(final Zip<T,Visitable> z, Fun<Zip<Visitable,Visitable>,Fun<Zip<Visitable,Visitable>,Ans>> k, Fun<Fun<Zip<Visitable,Visitable>,Ans>>) throws MOFException {
        final Visitable   x         = z.focus;
        final Visitable[] childrens = x.getChildren();
        final int         length    = childrens.length;

        if (length == 0) mk.apply(Zip.unit(x));
        else k.apply(Zip.child(x,0)).apply( new Fun<Visitable,Visitable>() { public Visitable apply(Visitable y) {

             )

        for(int i= 0; i < childrens.length; i++) {
            final int j = i;
            Zip<T,Visitable> zyj = Zip.mkZip(new Fun<Visitable, T>() { public T apply(Visitable y) throws MOFException {
                return z.replace(x.setChildAt(j,y)).run();
            }}
                    , childrens[i]
            ) ;
            childrens[i] = k.apply(zyj);
        }

        return x.setChildren(childrens);
    }
}
