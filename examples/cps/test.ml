(fun x -> fun y -> x) 1 2
;
((plus 1) (plus 2 3))
;
let first = fun x -> fun y -> x in first 2 3
;
let f = fun x -> if eq x 2 then 1 else plus x 1 end 
in f 3
;
eq (fun x -> x) (fun y -> y)
;
callcc (fun k -> throw k 1) 
;
let g = fun x -> fun k ->
  if eq x 1 then plus x 1 else throw k 99 end
in let f = fun x -> callcc (fun k -> plus (g x k) 1)
in f 1

