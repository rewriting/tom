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
 
  -- This is the classical abstraction for the record type in Ada
  -- Notice the use of default values for some fields
  %op TomBinaryTree node(left : TomBinaryTree, value:int, right : TomBinaryTree) {
    is_fsym(t)        { ($t /= null) }
    get_slot(left,t) { $t.all.left }
    get_slot(value,t) { $t.all.value }
    get_slot(right,t) { $t.all.right }
    get_default(left) { null }
    get_default(right) { null }
    make(l,v,r)         { new BinaryTree'($v, $l, $r) } 
  }

  -- This is a slightly different node that must have a son different from null. It cannot be a leaf.
  -- The make operator should have a pre-condition.
  %op TomBinaryTree realnode(left : TomBinaryTree, value:int, right : TomBinaryTree) {
    is_fsym(t)        { (($t /= null) and then (($t.left /= null) or ($t.right /= null))) }
    get_slot(left,t) { $t.all.left }
    get_slot(value,t) { $t.all.value }
    get_slot(right,t) { $t.all.right }
    get_default(left) { null }
    get_default(right) { null }
    make(l,v,r)         { new BinaryTree'($v, $l, $r) } 
  }

  -- This provides a view on a record with some specific values for some fields
  %op TomBinaryTree leaf(value:int) {
    is_fsym(t)        { (($t /= null) and then (($t.left = null) and ($t.right = null))) }
    get_slot(value,t) { $t.all.value }
    make(v)         { new BinaryTree'($v,null,null) } 
  }
  
  -- This provides a view on a the null value for the access to the record
  %op TomBinaryTree empty() {
    is_fsym(t)        { $t = null }
    make()         { null } 
  }
  
 -----------------------------------------------------
 -- Example of use
 -----------------------------------------------------

  procedure print(n : access BinaryTree) is
  begin
    %match (n) {
      empty() -> {
        put_line( "empty" );
      }
      node(g,v,d) -> {
        print(`g);
        put_line( "This node has value " & Integer'image(`v));
        print(`d);
      }
    }
  end print;

  procedure main is
    n1 : access BinaryTree := new BinaryTree'(0,null,null); -- standard Ada constructor
    n2 : access BinaryTree := `node(empty(),1,empty()); -- Tom constructor 
    n3 : access BinaryTree := `node[value=2]; -- Tom constructor with default values
    n4 : access BinaryTree := `leaf(3);
  begin
    %match (TomBinaryTree n1) {
      node(empty(),v,empty()) -> {
        put_line( "This node has value " & Integer'image(`v));
      }
    }
    print( n2 );      
    %match (n3) {
      leaf(v) -> {
        put_line( "This leaf has value " & Integer'image(`v));
      }
    }
  end Main;
end BTree;
