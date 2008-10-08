(* test alpha equivalence *)
fun x -> ((fun y -> fun x -> x y) x)
;
(* should be equivalent to the normal form of the previous one *)
fun x -> fun y -> (y x)
;
(* quicksort *)
let rec le = fun n -> fun m ->
  match n with
    | O() -> True()
    | S(x) -> 
        match m with
          | O() -> False()
          | S(y) -> le x y
        end
  end in
let rec filter = fun p -> fun l ->
  match l with
    | Nil() -> Nil()
    | Cons(x,xs) -> 
        match (p x) with
          | True() -> Cons(x,filter p xs)
          | False() -> filter p xs
        end
  end in
let not = fun b ->
  match b with
    | True() -> False()
    | False() -> True()
  end in
let rec append = fun a -> fun b ->
  match a with
    | Nil() -> b
    | Cons(x,xs) -> Cons(x,append xs b)
  end in
let rec quicksort = fun l ->
  match l with
    | Nil() -> Nil()
    | Cons(x,xs) -> 
        let right = filter (le x) xs in
        let left  = filter (fun y -> (not (le x y))) xs in
        append (quicksort left) Cons(x,(quicksort right))
  end in
quicksort Cons(6,Cons(3,Cons(2,Cons(2,Cons(5,Cons(1,Nil()))))))
;

(* Y combinator *)
let y = fun f -> (fun x -> f (x x)) (fun x -> f (x x)) in
let plus = y (fun r -> fun n -> fun m -> 
 match n with
   | O() -> m
   | S(p) -> S (r p m)
  end) in
let mult = y (fun r -> fun n -> fun m -> 
 match n with
   | O() -> 0
   | S(p) -> plus m (r p m)
  end) in
mult 8 3
;
let rec plus = fun n -> fun m -> 
  match n with
   | O() -> m
   | S(p) -> S (plus p m)
  end in
let rec mult = fun n -> fun m -> 
  match n with
   | O() -> 0
   | S(p) -> plus m (mult p m)
  end in
mult 8 3
;

let rec plus = fun n -> fun m -> 
  match n with
   | O() -> m
   | S(p) -> S (plus p m)
  end in
let rec mult = fun n -> fun m -> 
  match n with
   | O() -> 0
   | S(p) -> plus m (mult p m)
  end in
let rec fact = fun n -> 
  match n with
   | O() -> 1
   | S(x) -> mult (S(x)) (fact x)
  end in 
fact 4
;

let rec plus = fun n -> fun m -> 
  match n with
   | O() -> m
   | S(p) -> S (plus p m)
  end in
let rec mult = fun n -> fun m -> 
  match n with
   | O() -> 0
   | S(p) -> plus m (mult p m)
  end in
let rec pow = fun n -> fun m -> 
  match m with
   | O() -> 1
   | S(p) -> mult n (pow n p)
  end in 
mult 3 (pow 2 3)
;

(* illustration of lazy evaluation: nats = [0,1,.....] *)
let rec map = fun f -> fun l -> 
 match l with 
   | Nil() -> Nil()
   | Cons(x,xs) -> Cons(f x, map f xs)
 end in
let rec plus = fun n -> fun m -> 
 match n with
  | O() -> m
  | S(p) -> S (plus p m)
 end in
let rec mult = fun n -> fun m -> 
 match n with
  | O() -> 0
  | S(p) -> plus m (mult p m)
 end in
let square = fun x -> mult x x in
let rec nat = fun n -> Cons(n,nat (S(n))) in
let nats = nat 0 in
let rec take = fun n -> fun l -> 
  match l with
    | Cons(x,xs) -> 
        match n with 
          | O() -> Nil()
          | S(m) -> Cons(x,take m xs)
        end
  end in
take 5 (map square nats)
;

(* mutually recursive functions *)
let fst = fun p -> match p with Pair(x,y) -> x end in
let snd = fun p -> match p with Pair(x,y) -> y end in
let rec oddeven = Pair(
     (fun n -> match n with | O() -> False() | S(O()) -> True() | S(m) -> snd oddeven m end),
     (fun n -> match n with | O() -> True() | S(O()) -> False() | S(m) -> fst oddeven m end))
in 
let odd =  fst oddeven in
let even = snd oddeven in
odd S(S(S(S(S(O())))))





