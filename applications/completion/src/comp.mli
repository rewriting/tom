exception Res of Datatypes.tableau list

exception Res_eq_list of Datatypes.eq_list

exception Not_match

val pmatch : Datatypes.eq list -> Datatypes.eq_list

val unify : Datatypes.eq list -> Datatypes.eq_list

val rewrite :
  Datatypes.eq ->
  Datatypes.eq_list -> Datatypes.prop -> Datatypes.prop * Datatypes.eq_list

exception Solved

val closure : Datatypes.tableau -> unit

val tableau :
  Datatypes.eq list -> Datatypes.branch list -> Datatypes.branch list list

val unify_max : Datatypes.tableau -> Datatypes.tableau list

val sort_result :
  Datatypes.prop ->
  ((Datatypes.prop * 'a) list * 'b) list list ->
  ((Datatypes.prop * 'c list) list * (bool * bool)) list list

exception Res_tab of Datatypes.tableau

val clean : Datatypes.tableau list -> Datatypes.tableau list

val critic : Datatypes.eq -> Datatypes.eq -> 
  Datatypes.tableau * Datatypes.tableau 

val get_rules : Datatypes.tableau list -> Datatypes.eq list
