package tom.library.strategy.mutraveler;

public abstract interface MuVisitable extends jjtraveler.Visitable {

    /**
     * Replaces all childs of any visitable at once, and returns this
     * visitable. 
     */
    public abstract jjtraveler.Visitable setChilds(jjtraveler.Visitable[] childs);

}
