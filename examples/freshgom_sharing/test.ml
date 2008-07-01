let zero = (fun z -> fun s -> z) in
let succ = fun n -> (fun z -> fun s -> n (s z) s) in
let iter = fun u -> fun v -> fun n -> n u v in
let plus = fun n -> fun m -> (fun z -> fun s -> n (m z s) s) in
let pair = fun x -> fun y -> fun proj -> proj x y in
let first = fun p -> p (fun x -> fun y -> x) in
let second = fun p -> p (fun x -> fun y -> y) in
let pred = fun n -> 
  first (iter (pair zero zero) (fun p -> pair (second p) (succ (second p))) n) in
let true = fun x -> fun y -> x in
let false = fun x -> fun y -> y in
let ite = fun test -> fun v1 -> fun v2 -> test v1 v2 in
let zerotest = fun n -> n true (fun x -> false) in
let y = fun f -> (fun x -> f (x x)) (fun x -> f (x x)) in
let natrec = fun u -> fun v -> fun n -> 
  iter (pair zero u) 
  (fun p-> (let a = first p in let b = second p in (pair (succ a) (v a b)))) n in
let mult = fun n -> fun m -> iter zero (plus n) m in
let fact = natrec (succ zero) (fun p -> fun r -> mult (succ p) r)
in
second (fact (succ (succ (succ zero)))) 

;

(* test alpha equivalence between 6 and previous value *)

let zero = (fun x -> fun f -> x) in
let succ = fun n -> (fun a -> fun b -> n (b a) b) in
succ (succ (succ (succ (succ (succ zero)))))

;

(* test alpha equivalence failure in different terms *)

let zero = (fun x -> fun f -> x) in
let succ = fun n -> (fun a -> fun b -> n (b a) b) in
  succ (succ (succ (succ zero)))


