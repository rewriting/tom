let Z = (fun z -> fun s -> z) in
let S = fun n -> (fun z -> fun s -> n (s z) s) in
let iter = fun u -> fun v -> fun n -> n u v in
let plus = fun n -> fun m -> (fun z -> fun s -> n (m z s) s) in
let pair = fun x -> fun y -> fun proj -> proj x y in
let first = fun p -> p (fun x -> fun y -> x) in
let second = fun p -> p (fun x -> fun y -> y) in
let pred = fun n -> 
  first (iter (pair Z Z) (fun p -> pair (second p) (S (second p))) n) in
let true = fun x -> fun y -> x in
let false = fun x -> fun y -> y in
let ite = fun test -> fun v1 -> fun v2 -> test v1 v2 in
let zerotest = fun n -> n true (fun x -> false) in
let Y = fun f -> (fun x -> f (x x)) (fun x -> f (x x)) in
let natrec = fun u -> fun v -> fun n -> 
  iter (pair Z u) 
  (fun p-> (let a = first p in let b = second p in (pair (S a) (v a b)))) n in
let mult = fun n -> fun m -> iter Z (plus n) m in
let fact = natrec (S Z) (fun p -> fun r -> mult (S p) r)
in
  second (fact (S (S (S Z))))


