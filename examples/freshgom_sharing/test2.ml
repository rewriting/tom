let y = fun f -> (fun x -> f (x x)) (fun x -> f (x x)) in
let plus = y (fun r -> fun n -> fun m -> match n with
 | O -> m
 | S(p) -> S (r p m)
end) in
let mult = y (fun r -> fun n -> fun m -> match n with
 | O -> O
 | S(p) -> plus m (r p m)
end) in
let fact = y (fun re -> fun n -> match n with
 | O -> S(O)
 | S(x) -> mult (S(x)) (re x)
end) in 
fact (S(S(S(S(O)))))


