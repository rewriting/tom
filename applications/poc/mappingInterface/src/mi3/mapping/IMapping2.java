package mi3.mapping;

public interface IMapping2<C, D0, D1> extends IMapping {
  C make(D0 t1, D1 t2);
  D0 get0(C c);
  D1 get1(C c);
}


