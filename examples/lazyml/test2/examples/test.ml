let pair = fun x -> fun y -> fun f -> f x y in
  let f0 = fun x -> pair x x in
    let f1 = fun y -> f0 (f0 y) in
      let f2 = fun y -> f1 (f1 y) in
        let f3 = fun y -> f2 (f2 y) in
          let f4 = fun y -> f3 (f3 y) in
              f4 (fun z -> z)
