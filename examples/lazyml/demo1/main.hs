data Nat = Z | S Nat
data NatList = Nil | Cons Nat NatList

instance Show Nat where
  show = show . conv
    where conv Z = 0
          conv (S n) = 1 + (conv n)

go n = Cons n (go (S n))
naturals = go Z

mytake Z l = Nil
mytake (S n) (Cons x xs) = Cons x (mytake n xs)

plus Z     m = m
plus (S p) m = S (plus p m)

mult Z m = Z
mult (S p) m = plus m (mult p m)

pow n Z = (S Z)
pow n (S p) = mult n (pow n p)

foldR z f Nil = z
foldR z f (Cons x xs) = f x (foldR z f xs)

mysum = foldR Z plus 

four = (S (S (S (S Z))))
n256 = pow four four

main = print $ mysum (mytake n256 naturals)
