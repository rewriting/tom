let rec fact n = 
  if eq n 0 then 1 else times n (fact (minus n 1)) end
in (fact 15)
;;

(fun x -> fun y -> x) 1 2
;;

((plus 1) (plus 2 3))
;;

let first x y = x in first 2 3
;;

let f x = if eq x 2 then 1 else plus x 1 end 
in f 3
;;

callcc (fun k -> throw k 1) 
;;

let f x k = throw k 1 in
let g = f 2 in
(callcc g)
;;

let f x k = throw k 1 in
(callcc (f 2))
;;

(times (minus 4 (plus 3 2)) 2)
;;

(print 1; print 2)

;;

let pair x y p = p x y 
in let fst p = p (fun x -> fun y -> x) 
in let snd p = p (fun x -> fun y -> y) 
in let gen n = callcc (fun k ->
  (let rec aux x =  
     (callcc (fun j -> throw k (pair x j)); aux (plus x 1))
  in aux n)
)
in let p = gen 0 
in let n = fst p
in let k = snd p
in (print n; if eq n 20 then () else throw k () end)
;;

(* -> tail recursive fib *)
let rec fib n = 
  if eq n 0 then 0 else if eq n 1 then 1 else plus (fib (minus n 1)) (fib (minus n 2)) end end
in (fib 10)
;;

(* prolog-like callcc use : enumerate elts > 3 in a list *)
callcc (fun error ->
   let case s f g = s f g
in let inl x f g = f x
in let inr x f g = g x
in let pair x y p = p x y
in let fst p = p (fun x -> fun y -> x)
in let snd p = p (fun x -> fun y -> y)
in let nil = inr ()
in let cons x l = inl (pair x l)
in let isnil l = case l (fun x -> false) (fun x -> true)
in let iscons l = case l (fun x -> true) (fun x -> false)
in let head l = case l fst (fun x -> throw error ())
in let tail l = case l snd (fun x -> throw error ())
in let none = inr ()
in let some = inl
in let isnone m = case m (fun x -> false) (fun x -> true)
in let issome m = case m (fun x -> true) (fun x -> false)
in let getsome m = case m (fun x -> x) (fun x -> throw error ())
in let rec iter f l =  
  if (isnil l) then () else (f (head l); iter f (tail l)) end
in let rec find pred l = 
  callcc (fun k ->
    (iter (fun x -> if pred x then (callcc (fun kk -> throw k (some (pair x kk)))) else () end) l;
    none)
  )
in let seq b e =  
  let rec aux n = 
    if (gt n e) then nil else cons n (aux (plus n 1)) end
  in aux b
in let l = seq 0 20
in let m = find (fun x-> or (eq x 2) (and (gt x 5) (not (gt x 15)))) l
in if issome m then let xk = getsome m in (print (fst xk); throw (snd xk) ()) else () end 
)
;;

(* mutually recursive functions *)
let pair x y p = p x y 
in let fst p = p (fun x -> fun y -> x) 
in let snd p = p (fun x -> fun y -> y) 
in let rec oddeven = fun freeze -> pair 
 (fun n -> if eq n 0 then false else if eq n 1 then true else snd (oddeven ()) (minus n 1) end end) 
 (fun n -> if eq n 0 then true else if eq n 1 then false else fst (oddeven ()) (minus n 1) end end) 
in let odd = fst (oddeven ())
in let even = snd (oddeven ())
in (print (odd 9); print (odd 8); print (even 46); print (even 99))
;;

(* tom generators *)

callcc 
(fun error ->
  let pair x y p = p x y in 
  let fst p = p (fun x -> fun y -> x) in
  let snd p = p (fun x -> fun y -> y) 
  in
  let f k =
    let k1 = callcc (fun kk -> throw k (pair 1 kk)) in
    let k2 = callcc (fun kk -> throw k1 (pair 2 kk)) in
    let k3 =  callcc (fun kk -> throw k2 (pair 3 kk)) in
      throw error () 
  in
  let rec next kk un =
    let p = callcc(fun k -> throw kk k) in
    let n = fst p in
    let kk1 = snd p in
      pair n (next kk1)
  in
  let gen un =
    let p = callcc (fun kk -> f kk) in
      pair (fst p) (next (snd p))
  in
  
  let rec main g =
    let p = g () in
    let n = fst p in
    let gg = snd p in
      (print n; main gg)
  in
  main gen
)
