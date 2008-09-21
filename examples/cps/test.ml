let rec fact = fun n -> 
  if eq n 0 then 1 else times n (fact (minus n 1)) end
in (fact 5)
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

eq (fun x -> x) (fun y -> y)
;;

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

