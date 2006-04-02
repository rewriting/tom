" Vim syntax file
" Language:     Gom
" Filenames:    *.gom
" Maintainers:  Emmanuel Hainry   <Emmanuel.Hainry@loria.fr>
" URL:          http://tom-server.loria.fr/confluence/display/Tom/tips
" Last modified: Mar 28, 2006  10:20

" For version 5.x: Clear all syntax items
if version < 600
  syntax clear
endif

" Gom is case sensitive.
syn case match

syn keyword gomPreproc imports module abstract syntax

syn match gomComment /\/\/.*$/
syn region gomComment start=/\/\*/ end=/\*\//

syn match gomBackquote "`"
syn match gomPipe     "|"
syn match gomArrow    "->" 
syn match gomOperator "%match" 

syn region gomType start="\w" end=/=/me=s-1 oneline
syn region gomBrackets start="{" end="}" contains=gomArrow,gomOperator,gomBackquote

syn keyword gomHook make make_before make_insert 
syn region gomType start=":"ms=e+1 end=/[),]/me=s-1 contains=ALLBUT,gomHook

highlight link gomPreproc   Preproc
highlight link gomType      Type
highlight link gomStatement Statement
highlight link gomComment   Comment
highlight link gomStruct    Structure
highlight link gomPipe      Operator
highlight link gomOperator  Operator
highlight link gomArrow     Operator
highlight link gomBackQuote PreProc
highlight link gomHook      Function

let b:current_syntax = "gom"

" vim: ts=8
