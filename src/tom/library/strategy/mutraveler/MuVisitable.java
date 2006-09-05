package tom.library.strategy.mutraveler;

public interface MuVisitable extends jjtraveler.Visitable {

    /**
     * Replaces all childs of any visitable at once, and returns this
     * visitable. 
     */
    public jjtraveler.Visitable setChilds(jjtraveler.Visitable[] childs);

    /**
     * Returns the list of (non builtin) Visitable childs
     */
    public jjtraveler.Visitable[] getChilds();
}
