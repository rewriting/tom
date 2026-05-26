package main

import (
	"fmt"
	"os"
	"tom/tomgo/stable/library/tomast"
	tomparser "tom/tomgo/stable/tom/parser/parser"
)

func main() {
	src, _ := os.ReadFile(os.Args[1])
	r, err := tomparser.ParseAll(string(src), os.Args[1])
	if err != nil {
		fmt.Println("ERR:", err)
		return
	}
	fmt.Printf("Sorts: %d, Symbols: %d\n", len(r.Sorts), len(r.Symbols))
	for name, sym := range r.Symbols {
		s := sym.(*tomast.SymbolTomSymbol)
		opts, ok := s.Options.(*tomast.ConcOptionOptionList)
		if !ok {
			continue
		}
		fmt.Printf("  %-15s : %d options\n", name, len(opts.Slots))
	}
}
