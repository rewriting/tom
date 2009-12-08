package mi3.mapping;

public interface IListMapping<C,D> extends IMapping {

    public boolean isEmpty(C l);

    public C makeEmpty();

    public C makeInsert(D o, C l);

    public D getHead(C l);

    public C getTail(C l);
    
}
