package main

import (
	"fmt"
	"os"
	tomparser "tom/tomgo/stable/tom/parser/parser"
)

func main() {
	src, _ := os.ReadFile(os.Args[1])
	code, err := tomparser.Parse(string(src), os.Args[1])
	if err != nil {
		fmt.Println("ERR:", err)
		return
	}
	fmt.Println(code)
}
