package main

import "fmt"

// Hook bodies containing braces (Go composite literals) are parsed by
// the island grammar as nested blocks: $var must still be substituted,
// and make_empty() { []*N{} } must still be recognised.
type N struct {
  name string
  kids []*N
}

%typeterm N {
  implement { *N }
  is_sort(t) { $t != nil }
  equals(a,b) { $a == $b }
}

%typeterm NL {
  implement { []*N }
  is_sort(t) { $t != nil }
  equals(a,b) { len($a) == len($b) }
}

%op N leaf() {
  is_fsym(t) { $t.name == "leaf" }
  make() { &N{name: "leaf"} }
}

%op N node(l:N, r:N) {
  is_fsym(t) { $t.name == "node" }
  get_slot(l,t) { $t.kids[0] }
  get_slot(r,t) { $t.kids[1] }
  make(a,b) { &N{name: "node", kids: []*N{$a, $b}} }
}

%oplist NL ns(N*) {
  is_fsym(t) { $t != nil }
  get_head(l) { $l[0] }
  get_tail(l) { $l[1:] }
  is_empty(l) { len($l) == 0 }
  make_empty() { []*N{} }
  make_insert(e,l) { append([]*N{$e}, $l...) }
}

func size(n *N) int {
  %match(n) {
    leaf() -> { return 1 }
    node(l, r) -> { return size(`l) + size(`r) }
  }
  return 0
}

func main() {
  t := `node(node(leaf(), leaf()), leaf())
  l := `ns(leaf(), t, leaf())
  fmt.Println(size(t), len(l), l[1] == t)
}
