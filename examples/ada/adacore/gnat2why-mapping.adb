        with Ada; use Ada;
        with Types; use Types;
        with Sinfo; use Sinfo;
        with Nlists; use Nlists;
        
        package body Gnat2Why.Mapping is
        
        function tom_equal_term_int(t1: Integer; t2: Integer) return Boolean is 
        begin 
            return  t1=t2 ; 
        end tom_equal_term_int; 

        function tom_is_sort_int(t: Integer) return Boolean is 
        begin 
            return  True ; 
        end tom_is_sort_int; 

        
        
        
        --------------------------------------
        -- Mapping
        --------------------------------------
        function tom_equal_term_Tom_Node_Kind(n1:  Node_Kind ; n2:  Node_Kind ) return Boolean is 
        begin 
            return  n1 = n2 ; 
        end tom_equal_term_Tom_Node_Kind; 

        function tom_is_sort_Tom_Node_Kind(n:  Node_Kind ) return Boolean is 
        begin 
            return  True    ; 
        end tom_is_sort_Tom_Node_Kind; 

        
        
        --------------------------------------
        function tom_equal_term_Tom_Node_Id(n1:  Node_Id ; n2:  Node_Id ) return Boolean is 
        begin 
            return  n1 = n2 ; 
        end tom_equal_term_Tom_Node_Id; 

        function tom_is_sort_Tom_Node_Id(n:  Node_Id ) return Boolean is 
        begin 
            return  True    ; 
        end tom_is_sort_Tom_Node_Id; 

        function tom_is_fun_sym_Tom_Assignment_Node(t:  Node_Id ) return Boolean is 
        begin 
            return False; 
        end tom_is_fun_sym_Tom_Assignment_Node; 

        function tom_get_slot_Tom_Assignment_Node_n(n:  Node_Id ) return  Node_Id  is 
        begin 
            return   n ; 
        end tom_get_slot_Tom_Assignment_Node_n; 

        function tom_get_slot_Tom_Assignment_Node_name(n:  Node_Id ) return  Node_Id  is 
        begin 
            return  Sinfo.Name(Node_Id(n)) ; 
        end tom_get_slot_Tom_Assignment_Node_name; 

        function tom_get_slot_Tom_Assignment_Node_expr(n:  Node_Id ) return  Node_Id  is 
        begin 
            return  Sinfo.Expression(Node_Id(n)) ; 
        end tom_get_slot_Tom_Assignment_Node_expr; 

        function tom_is_fun_sym_Tom_Op_Add_Node(t:  Node_Id ) return Boolean is 
        begin 
            return False; 
        end tom_is_fun_sym_Tom_Op_Add_Node; 

        function tom_get_slot_Tom_Op_Add_Node_n(n:  Node_Id ) return  Node_Id  is 
        begin 
            return   ( n ) ; 
        end tom_get_slot_Tom_Op_Add_Node_n; 

        function tom_get_slot_Tom_Op_Add_Node_left(n:  Node_Id ) return  Node_Id  is 
        begin 
            return    Left_Opnd(Node_Id(n)) ; 
        end tom_get_slot_Tom_Op_Add_Node_left; 

        function tom_get_slot_Tom_Op_Add_Node_right(n:  Node_Id ) return  Node_Id  is 
        begin 
            return    Right_Opnd(Node_Id(n)) ; 
        end tom_get_slot_Tom_Op_Add_Node_right; 

        function tom_is_fun_sym_Tom_Loop_Statement_Node(t:  Node_Id ) return Boolean is 
        begin 
            return False; 
        end tom_is_fun_sym_Tom_Loop_Statement_Node; 

        function tom_get_slot_Tom_Loop_Statement_Node_n(n:  Node_Id ) return  Node_Id  is 
        begin 
            return   n  ; 
        end tom_get_slot_Tom_Loop_Statement_Node_n; 

        function tom_get_slot_Tom_Loop_Statement_Node_stmts(n:  Node_Id ) return  Node_Id  is 
        begin 
            return   Statements(Node_Id(n)) ; 
        end tom_get_slot_Tom_Loop_Statement_Node_stmts; 

        
        
        
        --------------------------------------
        function tom_equal_term_Tom_List_Node_Id(l1:  Node_Id ; l2:  Node_Id ) return Boolean is 
        begin 
            return  l1 = l2 ; 
        end tom_equal_term_Tom_List_Node_Id; 

        function tom_is_fun_sym_List_Node_Id(l:  Node_Id ) return Boolean is 
        begin 
            return  true ; 
        end tom_is_fun_sym_List_Node_Id; 

        function tom_empty_list_List_Node_Id return  Node_Id  is 
        begin 
            return   New_List ; 
        end tom_empty_list_List_Node_Id; 

        function tom_cons_list_List_Node_Id(n:  Node_Id ; l:  Node_Id ) return  Node_Id  is 
        begin 
            return   Insert_List(Node_Id(n),List_Id(l)); 
        end tom_cons_list_List_Node_Id; 

        function tom_get_head_List_Node_Id_Tom_List_Node_Id(l:  Node_Id ) return  Node_Id  is 
        begin 
            return    First(List_Id(l))  ; 
        end tom_get_head_List_Node_Id_Tom_List_Node_Id; 

        function tom_get_tail_List_Node_Id_Tom_List_Node_Id(l:  Node_Id ) return  Node_Id  is 
        begin 
            return    Last(List_Id(l))    ; 
        end tom_get_tail_List_Node_Id_Tom_List_Node_Id; 

        function tom_is_empty_List_Node_Id_Tom_List_Node_Id(l:  Node_Id ) return Boolean is 
        begin 
            return  Is_Empty_List(List_Id(l)); 
        end tom_is_empty_List_Node_Id_Tom_List_Node_Id; 

        function tom_append_list_List_Node_Id(l1:  Node_Id ; l2:  Node_Id ) return  Node_Id  is 
        begin 
            if tom_is_empty_List_Node_Id_Tom_List_Node_Id(l1) then 
                return l2; 
            elsif tom_is_empty_List_Node_Id_Tom_List_Node_Id(l2) then 
                return l1; 
            elsif tom_is_empty_List_Node_Id_Tom_List_Node_Id(tom_get_tail_List_Node_Id_Tom_List_Node_Id(l1)) then 
                return tom_cons_list_List_Node_Id(tom_get_head_List_Node_Id_Tom_List_Node_Id(l1),l2); 
            else 
                return tom_cons_list_List_Node_Id(tom_get_head_List_Node_Id_Tom_List_Node_Id(l1),tom_append_list_List_Node_Id(tom_get_tail_List_Node_Id_Tom_List_Node_Id(l1),l2)); 
            end if; 
        end tom_append_list_List_Node_Id; 

        function tom_get_slice_List_Node_Id(begining:  Node_Id ; ending:  Node_Id ; tail:  Node_Id ) return  Node_Id  is 
        begin 
            if tom_equal_term_Tom_List_Node_Id(begining,ending) then 
                return tail; 
            else 
                return tom_cons_list_List_Node_Id(tom_get_head_List_Node_Id_Tom_List_Node_Id(begining),tom_get_slice_List_Node_Id(tom_get_tail_List_Node_Id_Tom_List_Node_Id(begining),ending,tail)); 
            end if; 
        end tom_get_slice_List_Node_Id; 

        
        
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
                if True then 
                    if tom_is_fun_sym_Tom_Loop_Statement_Node( Node_Id (Arg)) then 
                        declare tomMatch1_2:  Node_Id :=tom_get_slot_Tom_Loop_Statement_Node_stmts( Node_Id (Arg)); begin 
                            if tom_is_fun_sym_List_Node_Id(tomMatch1_2) then 
                                declare tomMatch1_end_7:  Node_Id :=tomMatch1_2; begin 
                                    loop 
                                        if not (tom_is_empty_List_Node_Id_Tom_List_Node_Id(tomMatch1_end_7)) then 
                                            declare tomMatch1_14:  Node_Id :=tom_get_head_List_Node_Id_Tom_List_Node_Id(tomMatch1_end_7); begin 
                                                if tom_is_fun_sym_Tom_Assignment_Node(tomMatch1_14) then 
                                                    declare tomMatch1_13:  Node_Id :=tom_get_slot_Tom_Assignment_Node_expr(tomMatch1_14); begin 
                                                        declare tom_x:  Node_Id :=tom_get_slot_Tom_Assignment_Node_name(tomMatch1_14); begin 
                                                            if tom_is_fun_sym_Tom_Op_Add_Node(tomMatch1_13) then 
                                                                if tom_equal_term_Tom_Node_Id(tom_x, tom_get_slot_Tom_Op_Add_Node_left(tomMatch1_13)) then 
                                                                    declare tomMatch1_8:  Node_Id :=tom_get_tail_List_Node_Id_Tom_List_Node_Id(tomMatch1_end_7); begin 
                                                                        if not (tom_is_empty_List_Node_Id_Tom_List_Node_Id(tomMatch1_8)) then 
                                                                            declare tomMatch1_22:  Node_Id :=tom_get_head_List_Node_Id_Tom_List_Node_Id(tomMatch1_8); begin 
                                                                                if tom_is_fun_sym_Tom_Assignment_Node(tomMatch1_22) then 
                                                                                    declare tomMatch1_21:  Node_Id :=tom_get_slot_Tom_Assignment_Node_expr(tomMatch1_22); begin 
                                                                                        if tom_is_fun_sym_Tom_Op_Add_Node(tomMatch1_21) then 
                                                                                            if tom_equal_term_Tom_Node_Id(tom_x, tom_get_slot_Tom_Op_Add_Node_left(tomMatch1_21)) then 
                                                                                                if tom_equal_term_Tom_Node_Id(tom_get_slot_Tom_Op_Add_Node_right(tomMatch1_13), tom_get_slot_Tom_Op_Add_Node_right(tomMatch1_21)) then 
                                                                                                    declare tomMatch1_31: Boolean:=False; begin 
                                                                                                        if tom_equal_term_Tom_Node_Id(tom_x, tom_get_slot_Tom_Assignment_Node_name(tomMatch1_22)) then 
                                                                                                            tomMatch1_31:=True; 
                                                                                                        end if; 
                                                                                                        if not (tomMatch1_31) then 
                                                                                                            
                                                                                                            return Node_Id(tom_get_slot_Tom_Loop_Statement_Node_n( Node_Id (Arg)));

                                                                                                        end if; 

                                                                                                    end; 
                                                                                                end if; 
                                                                                            end if; 
                                                                                        end if; 
                                                                                    end; 
                                                                                end if; 
                                                                            end; 
                                                                        end if; 
                                                                    end; 
                                                                end if; 
                                                            end if; 
                                                        end; 
                                                    end; 
                                                end if; 
                                            end; 
                                        end if; 
                                        if tom_is_empty_List_Node_Id_Tom_List_Node_Id(tomMatch1_end_7) then 
                                            tomMatch1_end_7:=tomMatch1_2; 
                                        else 
                                            tomMatch1_end_7:=tom_get_tail_List_Node_Id_Tom_List_Node_Id(tomMatch1_end_7); 
                                        end if; 

                                        exit when not(not (tom_equal_term_Tom_List_Node_Id(tomMatch1_end_7, tomMatch1_2))); 
                                    end loop; 
                                end; 
                            end if; 
                        end; 
                    end if; 
                end if; 


        
        end Process_Node_Id;
        
        end Gnat2Why.Mapping;
