%include { caml/metal.tom }
%include { string.tom }
%include { bool.tom }

%typeterm term {
  implement { term }
  is_sort(t) { true }
    equals(s,t) { s = t }
}

%op term var(name:String) {
  is_fsym(t) { match t with Var _ -> true | _ -> false }
    get_slot(name,t) { get_subterm t 0 }
    make(s) { Var(s) }
}

%op term fun(name:String, args:term_list) {
  is_fsym(t) { match t with Fun(_,_) -> true | _ -> false }
    get_slot(name,t) { get_subterm t 0 }
    get_slot(args,t) { get_subterm t 1 }
    make(n,a) { Fun(n,a) }
}

%typeterm term_list {
  implement { term list }
  is_sort(t) { true }
    equals(s,t) { s = t }
}

%oplist term_list args( term* ) {
  is_fsym(t) { true }
  make_empty()  { [] }
  make_insert(e,l) { e::l }
  get_head(l)   { List.hd l }
  get_tail(l)   { List.tl l }
  is_empty(l)   { (l=[]) }
}


%typeterm prop {
  implement { prop }
  is_sort(t) { true }
    equals(s,t) { prop_eq s t }
}

%op prop atomic(name:String, args:term_list) {
  is_fsym(t) { match t with Atomic(_,_) -> true | _ -> false }
  get_slot(name,t) { (match t with Atomic (s,_) -> s | _ -> failwith "Tom bug") }
  get_slot(args,t) { (match t with Atomic (_,s) -> s |_-> failwith "Tom bug") }
    make(s,a) { Atomic(s,a) }
}

%op prop True() {
    is_fsym(t) { match t with True -> true | _ -> false }
    make() { True }
}

%op prop False() {
    is_fsym(t) { match t with False -> true | _ -> false }
    make() { False }
}

%op prop not(prop:prop) {
  is_fsym(t) { match t with Not _ -> true | _ -> false }
  get_slot(prop,t) { (match t with Not s -> s |_-> failwith "Tom bug") }
     make(s) { Not(s) }
}
 
%op prop or(prop1:prop, prop2:prop) {
  is_fsym(t) { match t with Or(_,_) -> true | _ -> false }
    get_slot(prop1,t) { get_subterm t 0 }
    get_slot(prop2,t) { get_subterm t 1 }
    make(s,t) { Or(s,t) }
}
 
%op prop and(prop1:prop, prop2:prop) {
  is_fsym(t) { match t with And(_,_) -> true | _ -> false }
    get_slot(prop1,t) { get_subterm t 0 }
    get_slot(prop2,t) { get_subterm t 1 }
    make(s,t) { And(s,t) }
}

%op prop all(name:String, prop:prop) {
  is_fsym(t) { match t with All(_,_) -> true | _ -> false }
    get_slot(name,t) { get_subterm t 0 }
    get_slot(prop,t) { get_subterm t 1 }
    make(s,t) { All(s,t) }
}

%op prop ex(name:String, prop:prop) {
    is_fsym(t) { match t with Ex(_,_) -> true | _ -> false }
    get_slot(name,t) { get_subterm t 0 }
    get_slot(prop,t) { get_subterm t 1 }
    make(s,t) { Ex(s,t) }
}

%typeterm string_list {
    implement { string list }
    is_sort(t) { true }
    equals(s,t) { s = t }
}

%oplist string_list strings( String* ) {
    is_fsym(t) { true }
    make_empty()  { [] }
    make_insert(e,l) { e::l }
    get_head(l)   { List.hd l }
    get_tail(l)   { List.tl l }
    is_empty(l)   { (l=[]) }
}

%typeterm eq {
    implement { eq }
    is_sort(t) { true }
    equals(s,t) { s = t }
}

%op eq term(lt:term, rt:term) {
  is_fsym(t) { match t with Term _ -> true | _ -> false }
    get_slot(lt,t) { get_subterm t 0 }
    get_slot(rt,t) { get_subterm t 1 }
     make(l,r) { Term(l,r) }
}

%op eq prop(lt:prop, rt:prop) {
  is_fsym(t) { match t with Prop _ -> true | _ -> false }
    get_slot(lt,t) { get_subterm t 0 }
    get_slot(rt,t) { get_subterm t 1 }
     make(l,r) { Prop(l,r) }
}

%typeterm eq_list {
  implement { eq list }
  is_sort(t) { true }
    equals(s,t) { s = t }
}

%oplist eq_list conc_eq( eq* ) {
  is_fsym(t) { true }
    make_empty()  { [] }
    make_insert(e,l) { e::l }
    get_head(l)   { List.hd l }
    get_tail(l)   { List.tl l }
    is_empty(l)   { (l=[]) }
}

%typeterm constr {
    implement { constr }
  is_sort(t) { true }
  equals(s,t) { s = t }
}

%op constr c(s:string_list, e:eq_list, r:eq_list) {
    is_fsym(t) { true }
    get_slot(s,t) { (let s,_,_ = t in s) }
    get_slot(e,t) { (let _,e,_ = t in e) }
    get_slot(r,t) { (let _,_,r = t in r) }
    make(s,t,r) { (s,t,r) }    
}

%typeterm a_prop {
    implement { a_prop }
    is_sort(t)             { true }
    equals(s,t) { let (l,_) = s and (r,_) = t in prop_eq l r }
}

%op a_prop a(prop:prop, ss:constr) {
    is_fsym(t) { true }
    get_slot(prop,t) { (fst t) }
    get_slot(ss,t) { (snd t) }
    make(s,t) { (s,t) }
}

%typeterm prop_list {
  implement { prop_list }
   is_sort(t) { true }
 equals(s,t) { (try List.for_all2 (fun (x,_) (y,_) -> prop_eq x y) s t with Invalid_argument _ -> false) }
}

%oplist prop_list conc( a_prop* ) {
  is_fsym(t) { true }
  make_empty()  { [] }
  make_insert(e,l) { e::l }
  get_head(l)   { List.hd l }
  get_tail(l)   { List.tl l }
  is_empty(l)   { (l=[]) }
}

%typeterm annotation {
    implement { annotation }
  is_sort(t) { true }
    equals(s,t) { s = t }
}

%op annotation an(one:bool,two:bool) {
    is_fsym(t) { true }
    get_slot(one,t) { (fst t) }
    get_slot(two,t) { (snd t) }
    make(s,t) { (s,t) }
}

%typeterm branch {
    implement { branch }
  is_sort(t) { true }
    equals(s,t) { (try List.for_all2 (fun (x,_) (y,_) -> prop_eq x y) (fst s) (fst t) with Invalid_argument _ -> false) }
}

%op branch b (props:prop_list, an:annotation) {
  is_fsym(t) { true }
  get_slot(props,t) { (fst t) }
  get_slot(an,t) { (snd t) }
  make(s,t) { (s,t) }
}

%typeterm tableau {
  implement { branch list }
  is_sort(t) { true }
  equals(s,t) { (try List.for_all2 (fun (b1, _) (b2, _) -> List.for_all2 (fun (x,_) (y,_) -> prop_eq x y) b1 b2) s t with Invalid_argument _ -> false) }
}

%oplist tableau tab( branch* ) {
  is_fsym(t) { true }
  make_empty()  { [] }
  make_insert(e,l) { e::l }
  get_head(l)   { List.hd l }
  get_tail(l)   { List.tl l }
  is_empty(l)   { (l=[]) }
}
