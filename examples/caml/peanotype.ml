exception Erreur ;;

type mlpeano =
    Zero 
  | Suc of mlpeano ;;

let fzero = "zero"
and fsuc = "suc" ;;

let get_sym = function
    Zero  -> fzero
  | Suc _ -> fsuc ;;

let get_sub n = function
    Zero  -> raise Erreur
  | Suc s -> if n = 0 then s else raise Erreur ;;

let suc n = Suc n ;;

let rec string_of_peano = function
    Zero -> "Zero"
  | Suc n -> "Suc(" ^ (string_of_peano n) ^")"
