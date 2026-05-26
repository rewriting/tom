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
	repo := "/Users/pem/github/tom"
	tomparser.IncludeSearchPath = []string{
		filepath.Join(repo, "utils/eclipse-plugin/plugin/include/java"),
		filepath.Join(repo, "utils/eclipse-plugin/plugin/include"),
		filepath.Join(repo, "tomgo/tests/share/tom-mappings"),
		filepath.Join(repo, "tomgo/tests/share/tom-mappings/gom"),
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
