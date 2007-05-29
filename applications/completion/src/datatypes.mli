type term = Var of string | Fun of string * term list

type term_list = term list

type prop =
    Atomic of string * term_list
  | True | False
  | Not of prop
  | Or of prop * prop
  | And of prop * prop
  | All of string * prop
  | Ex of string * prop

type eq = Term of term * term | Prop of prop * prop

type eq_list = eq list

type string_list = string list

type constr = string_list * eq_list * eq_list

type a_prop = prop * constr

type prop_list = a_prop list

type annotation = bool * bool

type branch = prop_list * annotation

type tableau = branch list

val term_to_string : term -> string

val prop_to_string : prop -> string

val tableau_to_string : tableau -> string

val tableau_list_to_string : tableau list -> string

val rules_to_string : eq list -> string

val subst_term : eq list -> term -> term

val subst : eq list -> prop -> prop

val fresh_var : string -> string

val prop_eq : prop -> prop -> bool
