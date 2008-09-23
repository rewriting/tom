let rec fact = fun n -> 
  if eq n 0 then 1 else times n (fact (minus n 1)) end
in (fact 15)
;;

(fun x -> fun y -> x) 1 2
;;

((plus 1) (plus 2 3))
;;

let first = fun x -> fun y -> x in first 2 3
;;

let f = fun x -> if eq x 2 then 1 else plus x 1 end 
in f 3
;;

(*
eq (fun x -> x) (fun y -> y)
;;
*)

callcc (fun k -> throw k 1) 
;;

let g = fun x -> fun k ->
  if eq x 1 then plus x 1 else throw k 99 end
in let f = fun x -> callcc (fun k -> plus (g x k) 1)
in f 1
;;

let f = fun x -> fun k -> throw k 1 in
let g = f 2 in
(callcc g)
;;

let f = fun x -> fun k -> throw k 1 in
(callcc (f 2))
;;

(times (minus 4 (plus 3 2)) 2)
;;

(print 1; print 2)

;;

let pair = fun x -> fun y -> fun p -> p x y 
in let fst = fun p -> p (fun x -> fun y -> x) 
in let snd = fun p -> p (fun x -> fun y -> y) 
in let gen = fun n -> callcc (fun k ->
  (let rec aux = fun x -> 
     (callcc (fun j -> throw k (pair x j)); aux (plus x 1))
  in aux n)
)
in let p = gen 0 
in let n = fst p
in let k = snd p
in (print n; if eq n 20 then () else throw k () end)
;;

(* -> tail recursive fib *)
let rec fib = fun n ->
  if eq n 0 then 0 else if eq n 1 then 1 else plus (fib (minus n 1)) (fib (minus
  n 2)) end end
in (fib 10)
;;

(* prolog-like callcc use : enumerate elts > 3 in a list *)
callcc (fun error ->
   let case = fun s -> fun f -> fun g -> s f g
in let inl = fun x -> fun f -> fun g -> f x
in let inr = fun x -> fun f -> fun g -> g x
in let pair = fun x -> fun y -> fun p -> p x y
in let fst = fun p -> p (fun x -> fun y -> x)
in let snd = fun p -> p (fun x -> fun y -> y)
in let nil = inr ()
in let cons = fun x -> fun l -> inl (pair x l)
in let isnil = fun l -> case l (fun x -> false) (fun x -> true)
in let iscons = fun l -> case l (fun x -> true) (fun x -> false)
in let head = fun l -> case l fst (fun x -> throw error ())
in let tail = fun l -> case l snd (fun x -> throw error ())
in let none = inr ()
in let some = inl
in let isnone = fun m -> case m (fun x -> false) (fun x -> true)
in let issome = fun m -> case m (fun x -> true) (fun x -> false)
in let getsome = fun m -> case m (fun x -> x) (fun x -> throw error ())
in let rec iter = fun f -> fun l -> 
  if (isnil l) 
  then () 
  else (f (head l); iter f (tail l))
  end
in let rec find = fun pred -> fun l -> 
  callcc (fun k ->
    (iter (fun x -> if pred x then (callcc (fun kk -> throw k (some (pair x kk)))) else () end) l;
    none)
  )
in let l = (cons 2 (cons 4 (cons 7 (cons 3 (cons 4 (cons 5 (cons 1 (cons 6 nil))))))))
in let m = find (fun x-> gt x 3) l
in if issome m then let xk = getsome m in (print (fst xk); throw (snd xk) ()) else () end 
)



