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
;
S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(O))))))))))))))))))))))))
;
let y = fun f -> (fun x -> f (x x)) (fun x -> f (x x)) in
let plus = y (fun r -> fun n -> fun m -> match n with
 | O -> m
 | S(p) -> S (r p m)
end) in
let mult = y (fun r -> fun n -> fun m -> match n with
 | O -> O
 | S(p) -> plus m (r p m)
end) in
let two = S(S(O)) in
let three = plus two (S(O)) in
let height = mult two (mult two two) in
mult height three
;
let y = fun f -> (fun x -> f (x x)) (fun x -> f (x x)) in
let plus = y (fun r -> fun n -> fun m -> match n with
 | O -> m
 | S(p) -> S (r p m)
end) in
let mult = y (fun r -> fun n -> fun m -> match n with
 | O -> O
 | S(p) -> plus m (r p m)
end) in
let pow = y (fun r -> fun n -> fun m -> match m with
 | O -> S(O)
 | S(p) -> mult n (r n p)
end)
in 
let three = (S(S(S(O)))) in
mult three (pow (S(S(O))) three)


