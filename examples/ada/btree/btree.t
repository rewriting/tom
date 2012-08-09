with System; use System;
with Ada.Text_IO; use Ada.Text_IO;
package body BTree is

 
  %include { int.tom }
  %include { string.tom }
 
  type BinaryTree is
  record
    value: Integer;
    left, right: access BinaryTree;
  end record;


 -----------------------------------------------------
 -- Mapping TOM
 -----------------------------------------------------
 
  %typeterm TomBinaryTree {
    implement { access BinaryTree }  
    is_sort(t) { True } 
    equals(t1,t2) { $t1.all = $t2.all }
  }
 
  %op TomBinaryTree node(left : TomBinaryTree, value:int, right : TomBinaryTree) {
    is_fsym(t)        { $t /= null }
    get_slot(left,t) { $t.all.left}
    get_slot(value,t) { $t.all.value}
    get_slot(right,t) { $t.all.right}
    get_default(left) { null }
    get_default(right) { null }
    make(l,v,r)         { new BinaryTree'($v, $l, $r) } 
  }

  %op TomBinaryTree empty() {
    is_fsym(t)        { $t = null }
    make()         { null } 
  }
  
 -----------------------------------------------------
 -- Example of use
 -----------------------------------------------------

  procedure main is
    n1 : access BinaryTree := new BinaryTree'(0,null,null);
    n2 : access BinaryTree := `node(empty(),1,empty());
    n3 : access BinaryTree := `node[value=2];
  begin
   null;
  end Main;
end BTree;
