package main

import (
	"fmt"
	"os"
	"path/filepath"
	"strings"
	"tom/tomgo/stable/tom"
	"tom/tomgo/stable/tom/desugarer"
	"tom/tomgo/stable/tom/parser"
	tomparser "tom/tomgo/stable/tom/parser/parser"
	"tom/tomgo/stable/tom/starter"
	"tom/tomgo/stable/tom/typer"
)

func main() {
	tomparser.IncludeSearchPath = []string{
		"/Users/pem/github/tom/utils/eclipse-plugin/plugin/include/java",
		"/Users/pem/github/tom/utils/eclipse-plugin/plugin/include",
	}
	input := os.Args[1]
	st, err := tom.Run(tom.State{Filename: input}, starter.Run, parser.Run, desugarer.Run, typer.Run)
	if err != nil {
		fmt.Println("ERR:", err)
		return
	}
	out := fmt.Sprintf("%v", st.Code)
	out = strings.ReplaceAll(out, input, "__INPUT__")
	out = strings.ReplaceAll(out, filepath.Dir(input), "__DIR__")
	fmt.Print(out)
}
