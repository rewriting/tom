package term;

import lib.MOFException;
import lib.fun.Fun;
import lib.zip.Zip;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 23/10/12
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
// A simple leaf
public class L extends Term {
    public String name ;

    public L(String n) {
        name = n;
    }

    public String toString() {
        return name;
    }

    public boolean equals(Object o) {
        if(o instanceof L) {
            return name.equals(((L) o).name);
        }
        return false;
    }

    public <Ans,Y> Ans accept(TermVisitor<Y> v, Fun<Zip<Term,Y>,Ans> k) throws MOFException { return v.visit(this, k); }
}
