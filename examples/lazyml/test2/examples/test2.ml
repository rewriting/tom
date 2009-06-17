(let false = (fun x -> (fun y -> x)) in (let true = (fun x -> (fun y -> y)) in
(let case = (fun s -> (fun f -> (fun g -> ((s f) g)))) in (let inl = (fun x ->
  (fun f -> (fun g -> (f x)))) in (let inr = (fun x -> (fun f -> (fun g -> (g
  x)))) in (let pair = (fun x -> (fun y -> (fun p -> ((p x) y)))) in (let fst =
    (fun p -> (p (fun x -> (fun y -> x)))) in (let snd = (fun p -> (p (fun x ->
      (fun y -> y)))) in (let nil = (inr (fun x -> x)) in (let cons = (fun x ->
        (fun l -> (inl ((pair x) l)))) in (let isnil = (fun l -> (((case l) (fun
          x -> false)) (fun x -> true))) in (let iscons = (fun l -> (((case l)
          (fun x -> true)) (fun x -> false))) in (let none = (inr (fun x -> x))
          in (let some = inl in (let isnone = (fun m -> (((case m) (fun x ->
            false)) (fun x -> true))) in (let issome = (fun m -> (((case m) (fun
              x -> true)) (fun x -> false))) in issome))))))))))))))))
