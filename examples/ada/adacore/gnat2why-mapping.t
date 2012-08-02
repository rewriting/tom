with Ada; use Ada;
with Types; use Types;
with Sinfo; use Sinfo;
with Nlists; use Nlists;

package body Gnat2Why.Mapping is

  %include { int.tom }

--------------------------------------
-- Mapping
--------------------------------------
  %typeterm Tom_Node_Kind {
    implement     { Node_Kind }
    is_sort(n)    { True    }
    equals(n1,n2) { n1 = n2 }
  }

  %op Tom_Node_Kind Tom_Identifier() {
      is_fsym(n)  { true } 
      make()      { Node_Kind'Pos(N_Identifier) }
  }

  %op Tom_Node_Kind Tom_Assignment_Statement() {
      is_fsym(n)  { true }
      make()      { Node_Kind'Pos(N_Assignment_Statement) }
  }

  %op Tom_Node_Kind Tom_Op_Add() {
      is_fsym(n)  { true }
      make()      { Node_Kind'Pos(N_Op_Add) }
  }

  %op Tom_Node_Kind Tom_Loop_Statement() {
      is_fsym(n)  { true                            }
      make()      { Node_Kind'Pos(N_Loop_Statement) }
  }

--------------------------------------
  %typeterm Tom_Node_Id {
    implement     { Node_Id }
    is_sort(n)    { True    }
    equals(n1,n2) { n1 = n2 }
  }

  %op Tom_Node_Id Tom_Identifier_Node (n: Tom_Node_Id) {
      make(n)        { n }
      get_slot(n, n) {  n }
  }

  %op Tom_Node_Id Tom_Assignment_Node(n: Tom_Node_Id, name: Tom_Node_Id, expr: Tom_Node_Id) {
      make(n, name, expr) { n }
      get_slot(n, n)      {  n }
      get_slot(name, n)   { Sinfo.Name(Node_Id(n)) }
      get_slot(expr, n)   { Sinfo.Expression(Node_Id(n)) }
  }

  %op Tom_Node_Id Tom_Op_Add_Node(n: Tom_Node_Id, left: Tom_Node_Id, right: Tom_Node_Id) {
      make(n, left, right) { n }
      get_slot(n, n)       {  ( n ) }
      get_slot(left, n)    {   Left_Opnd(Node_Id(n)) }
      get_slot(right, n)   {   Right_Opnd(Node_Id(n)) }
  }

  %op Tom_Node_Id Tom_Loop_Statement_Node(n: Tom_Node_Id, stmts: Tom_List_Node_Id) {
      make(n, stmts)       { n }
      get_slot(n, n)       {  n  }
      get_slot(stmts, n)   {  Statements(Node_Id(n)) }
  }


--------------------------------------
  %typeterm Tom_List_Node_Id {
    implement     { Node_Id }
    equals(l1,l2) { l1 = l2 }
  }

%oplist Tom_List_Node_Id List_Node_Id( Tom_Node_Id* ) {
  is_fsym(l)       { true }
  make_empty()     {  New_List }
  make_insert(n,l) {  Insert_List(Node_Id(n),List_Id(l))}
  get_head(l)      {   First(List_Id(l))  }
  get_tail(l)      {   Last(List_Id(l))    }
  is_empty(l)      { Is_Empty_List(List_Id(l))}
}

function Insert_List (N : Node_Id; L : List_Id) return List_Id is
begin
 Append (Node_Or_Entity_Id (N), L);
 return L;
end Insert_List;

--------------------------------------
-- Instance of Matching 
--------------------------------------
  function Process_Node_Id (Arg : Node_Id) return Node_Id is
  begin
        %match(Arg) {

          Tom_Loop_Statement_Node (n, 
                                   List_Node_Id(_*,
                                                Tom_Assignment_Node (_, x,  Tom_Op_Add_Node (_,x,y)), 
                                                Tom_Assignment_Node (_, !x, Tom_Op_Add_Node (_,x,y)),
                                                _*
                                               )
                                  ) -> { 
                                  return Node_Id(`n);
                                  }

        }
  end Process_Node_Id;

end Gnat2Why.Mapping;
