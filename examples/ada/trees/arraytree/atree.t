with System; use System;
with Ada.Text_IO; use Ada.Text_IO;
package body ATree is

 
  %include { int.tom }
  %include { string.tom }
 
  type Node_Range is range 0..1024;

  type BinaryNode is
  record
    value: Integer;
    left, right: Node_Range;
  end record;

  subtype Node_Array_Range is Node_Range range 1..Node_Range'Last;  

  type ArrayTree is array (Node_Array_Range) of BinaryNode;

  Nodes : ArrayTree;

  Last_Node : Node_Range := 0;

  function Allocate_Tree_Node(value:Integer;left,right:Node_Range) return Node_Range is
  begin
    if (Last_Node /= Node_Range'Last) then
      Last_Node := Last_Node + 1;
      nodes(Last_Node).value := value;
      nodes(Last_Node).left := left;
      nodes(Last_Node).right := right;
      return Last_Node;
    else
      return 0;
    end if;
  end Allocate_Tree_Node;

 -----------------------------------------------------
 -- Mapping TOM
 -----------------------------------------------------
 
  %typeterm TomArrayTree {
    implement { Node_Range }  
    is_sort(t) { True } 
    equals(t1,t2) { $t1 = $t2 }
  }
 
  -- This is the classical abstraction for the record type in Ada
  -- Notice the use of default values for some fields
  %op TomArrayTree node(left : TomArrayTree, value:int, right : TomArrayTree) {
    is_fsym(t)        { ($t /= 0) }
    get_slot(left,t) { nodes($t).left }
    get_slot(value,t) { nodes($t).value }
    get_slot(right,t) { nodes($t).right }
    get_default(left) { 0 }
    get_default(right) { 0 }
    make(l,v,r)         { Allocate_Tree_Node($v, $l, $r) } 
  }

  -- This is a slightly different node that must have a son different from null. It cannot be a leaf.
  -- The make operator should have a pre-condition.
  %op TomArrayTree realnode(left : TomArrayTree, value:int, right : TomArrayTree) {
    is_fsym(t)        { (($t /= 0) and then ((nodes($t).left /= 0) or (nodes($t).right /= 0))) }
    get_slot(left,t) { nodes($t).left }
    get_slot(value,t) { nodes($t).value }
    get_slot(right,t) { nodes($t).right }
    get_default(left) { 0 }
    get_default(right) { 0 }
    make(l,v,r)         { Allocate_Tree_Node($v, $l, $r) } 
  }

  -- This provides a view on a record with some specific values for some fields
  %op TomArrayTree leaf(value:int) {
    is_fsym(t)        { (($t /= 0) and then ((nodes($t).left = 0) and (nodes($t).right = 0))) }
    get_slot(value,t) { nodes($t).value }
    make(v)         { Allocate_Tree_Node($v,0,0) } 
  }
  
  -- This provides a view on a the null value for the access to the record
  %op TomArrayTree empty() {
    is_fsym(t)        { $t = 0 }
    make()         { 0 } 
  }
  
 -----------------------------------------------------
 -- Example of use
 -----------------------------------------------------

  procedure print(n : Node_Range) is
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
    n1 : Node_Range := Allocate_Tree_Node(0,0,0); -- standard Ada constructor
    n2 : Node_Range := `node(empty(),1,empty()); -- Tom constructor 
    n3 : Node_Range := `node[value=2]; -- Tom constructor with default values
    n4 : Node_Range := `leaf(3);
  begin
    %match (TomArrayTree n1) {
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
end ATree;
