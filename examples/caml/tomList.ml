
open TomExtension ;;

let rec tom_get_fun_sym_TomList t = "conc" 
and tom_cmp_fun_sym_TomList (t1,t2) = t1 = t2
and tom_terms_equal_TomList (t1,t2) = t1 = t2
and tom_get_head_TomList l = List.hd l
and tom_get_tail_TomList l = List.tl l
and tom_is_empty_TomList l = l = [] ;;

let rec tom_make_empty_conc () = []
and tom_make_insert_conc (e, l) = e::l 
and tom_insert_list_conc (l1,l2)= l1 @ l2
and tom_get_slice_conc (beginning, ending) = 
  if beginning = ending 
  then [] 
  else List.hd(beginning) :: tom_get_slice_conc(List.tl(beginning),ending);;
