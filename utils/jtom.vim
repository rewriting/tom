" Vim syntax file
" jtom:         Java + TOM
" Maintainer:   Antoine Reilles <reilles@loria.fr>
" Original:     Comes from javacc.vim

" For version 5.x: Clear all syntax items
" For version 6.x: Quit when a syntax file was already loaded
if version < 600
  syntax clear
elseif exists("b:current_syntax")
  finish
endif

" Uses java.vim, and adds things for TOM constructs

" source the java.vim file
if version < 600
   so <sfile>:p:h/java.vim
else
   runtime! syntax/java.vim
   unlet b:current_syntax
endif

" remove javaError
syn clear javaError
syn match javaError "[\\]"
syn match javaError "<<<\|\.\.\|=>\|<>\|||=\|&&=\|\*\/"

" tom keyword definitions
syn match   tomExternal         "%include"
syn match   tomOperator         "%op"
syn match   tomOperator         "%oplist"
syn match   tomOperator         "%oparray"
syn match   tomMatch            "%match"
syn match   tomRule             "%rule"
syn match   tomType             "%typeterm"
syn match   tomType             "%typelist"
syn match   tomType             "%typeint"
syn match   tomType             "%typearray"
syn match   tomArrow            "->"
syn match   tomMake             "`.*;"
syn keyword tomOpkey            implement get_fun_sym get_subterm cmp_fun_sym fsym
syn keyword tomOpkey            equals get_head get_tail is_empty get_element is_fsym 
syn keyword tomOpkey            get_size make_empty
syn keyword tomOpkey            make_insert make_append
syn keyword tomOpkey            get_slot
syn keyword tomOpkey            make

" Define the default highlighting.
" For version 5.7 and earlier: only when not done already
" For version 5.8 and later: only when an item doesn't have highlighting yet
if version >= 508 || !exists("did_jtom_syn_inits")
  if version < 508
    let did_jtom_syn_inits = 1
    command -nargs=+ HiLink hi link <args>
  else
    command -nargs=+ HiLink hi def link <args>
  endif
  HiLink tomExternal   javaScopeDecl
  HiLink tomOperator   javaScopeDecl
  HiLink tomMatch      javaScopeDecl
  HiLink tomRule       javaScopeDecl
  HiLink tomType       Type
  HiLink tomArrow      Statement
  HiLink tomMake       Statement
  HiLink tomOpkey      Statement
  delcommand HiLink
endif

let b:current_syntax = "jtom"
