  %include { tree/tree.tom }
  
  /*private Tree makeBlack(Tree t) {
    if(!t.getcolor().isB()) {
      t = node(B(),t.getlhs(),t.getvalue(),t.getrhs()); 
    }
    return t;
  }*/

  rule boolean rbtree.MyTree1.member(Tree t, Element x) {
    %match(Tree t) {
      emptyTree() /*<< t*/ -> { return false; }
      node(_,a,y,b) /*<< t*/ -> {
        int cmp = comparator.compare(x,y); // <- problematic example : we only keep expressions and not statements
        if(cmp < 0) {
          return member(a,x);
        } else if(cmp == 0) {
          return true;
        } else {
          return member(b,x);
        }
      }
    }
    return false;
  }

  rule int rbtree.MyTree1.card(Tree t) {
//    %match(Tree t) {
      emptyTree() << t -> { return 0; }
      node(_,a,_,b) << t -> { return 1 + card(a) + card(b); }
//    }
    return 0;
  }
  
  rule Tree rbtree.MyTree1.ins(Tree t, Element x) {
//    %match(Tree t) {
      emptyTree() << t -> { return node(R(),t,x,t); }

      node(color,a,y,b) << t -> {
        int cmp = comparator.compare(x,y);
        if(cmp < 0 ) {
          return balance(color,ins(a,x),y,b);
        } else if(cmp == 0) {
          return t;
        } else {
          return balance(color,a,y,ins(b,x));
        }
      }
//    }
    return null;
  }

  rule Tree rbtree.MyTree1.balance(Color color, Tree lhs, Element elt, Tree rhs) {
    %match(Color color, Tree lhs, Element elt, Tree rhs) { // plus facile de laisser la construction match possible dans ce type de cas
      B(), node(R(),node(R(),a,x,b),y,c), z, d /* (B() << t) && (node(R(),node(R(),a,x,b),y,c) << t) && (z << t) && (d << t) */ -> { return node(R(),node(B(),a,x,b),y,node(B(),c,z,d)); }
      B(), node(R(),a,x,node(R(),b,y,c)), z, d -> { return node(R(),node(B(),a,x,b),y,node(B(),c,z,d)); }
      B(), a, x, node(R(),node(R(),b,y,c),z,d) -> { return node(R(),node(B(),a,x,b),y,node(B(),c,z,d)); }
      B(), a, x, node(R(),b,y,node(R(),c,z,d)) -> { return node(R(),node(B(),a,x,b),y,node(B(),c,z,d)); }
    }
      // no balancing necessary
    return node(color,lhs,elt,rhs);
  }
  
  rule Tree rbtree.MyTree1.balance2(Color color, Tree lhs, Element elt, Tree rhs) {
    %match(Color color, Tree lhs, Element elt, Tree rhs) { // plus facile de laisser la construction match possible dans ce type de cas
        // color flip
      B(), node(R(),a@node(R(),_,_,_),x,b), y, node(R(),c,z,d) -> {
        return node(R(),node(B(),a,x,b),y,node(B(),c,z,d));
      }
      B(), node(R(),a,x,b@node(R(),_,_,_)), y, node(R(),c,z,d) -> {
        return node(R(),node(B(),a,x,b),y,node(B(),c,z,d));
      }
      B(), node(R(),a,x,b), y, node(R(),c@node(R(),_,_,_),z,d) -> {
        return node(R(),node(B(),a,x,b),y,node(B(),c,z,d));
      }
      B(), node(R(),a,x,b), y, node(R(),c,z,d@node(R(),_,_,_)) -> {
        return node(R(),node(B(),a,x,b),y,node(B(),c,z,d));
      }
        // single rotations
      B(), node(R(),a@node(R(),_,_,_),x,b), y, c -> { return node(B(),a,x,node(R(),b,y,c)); }
      B(), a, x, node(R(),b,y,c@node(R(),_,_,_)) -> { return node(B(),node(R(),a,x,b),y,c); }
        // double rotations
      B(), node(R(),a,x,node(R(),b,y,c)), z, d -> {
        return node(B(),node(R(),a,x,b),y,node(R(),c,z,d));
      }
      B(), a, x, node(R(),node(R(),b,y,c),z,d) -> {
        return node(B(),node(R(),a,x,b),y,node(R(),c,z,d));
      }
    }
      // no balancing necessary
    return node(color,lhs,elt,rhs);
  }
  
}
