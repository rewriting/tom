package mi2.mapping;

import java.util.List;

/**
 * @author nick-vintila
 * @date Jun 23, 2009  9:06:06 PM
 */
public interface List_MappingI<Domain,Codomain> {

    boolean isSym(Object t);

    boolean isEmpty(Codomain l);

    Codomain makeEmpty();

    Codomain makeInsert(Domain o, Codomain l);

    Domain getHead(Codomain l);

    Codomain getTail(Codomain l);
}
