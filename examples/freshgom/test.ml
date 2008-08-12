let y = fun f -> (fun x -> f (x x)) (fun x -> f (x x)) in
let plus = y (fun r -> fun n -> fun m -> match n with
 | O() -> m
 | S(p) -> S (r p m)
end) in
let mult = y (fun r -> fun n -> fun m -> match n with
 | O() -> 0
 | S(p) -> plus m (r p m)
end) in
mult 8 3
;

let y = fun f -> (fun x -> f (x x)) (fun x -> f (x x)) in
let plus = y (fun r -> fun n -> fun m -> match n with
 | O() -> m
 | S(p) -> S (r p m)
end) in
let mult = y (fun r -> fun n -> fun m -> match n with
 | O() -> 0
 | S(p) -> plus m (r p m)
end) in
let fact = y (fun re -> fun n -> match n with
 | O() -> 1
 | S(x) -> mult (S(x)) (re x)
end) in 
fact 4
;

let y = fun f -> (fun x -> f (x x)) (fun x -> f (x x)) in
let plus = y (fun r -> fun n -> fun m -> match n with
 | O() -> m
 | S(p) -> S (r p m)
end) in
let mult = y (fun r -> fun n -> fun m -> match n with
 | O() -> 0
 | S(p) -> plus m (r p m)
end) in
let pow = y (fun r -> fun n -> fun m -> match m with
 | O() -> 1
 | S(p) -> mult n (r n p)
end)
in 
mult 3 (pow 2 3)
;

(* illustration of lazy evaluation: nats = [0,1,.....] *)
let y = fun f -> (fun x -> f (x x)) (fun x -> f (x x)) in
let map = y (fun r -> fun f -> fun l -> 
 match l with 
   | Nil() -> Nil()
   | Cons(x,xs) -> Cons(f x, r f xs)
 end) in
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
let square = fun x -> mult x x in
let nat = y (fun r -> fun n -> Cons(n,r (S(n)))) in
let nats = nat 0 in
let take = y (fun r -> fun n -> fun l -> 
  match l with
    | Cons(x,xs) -> 
        match n with 
          | O() -> Nil()
          | S(m) -> Cons(x,r m xs)
        end
  end) in
take 5 (map square nats)
