" Vim syntax file
" tom:         Java + TOM
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
syn region  tomMetaQuoteEscape start="@" end="@" contained 
syn region  tomMetaQuote	start="%\["  end="\]%" contains=tomMetaQuoteEscape
syn match   tomExternal         "%include"
syn match   tomOperator         "%op"
syn match   tomOperator         "%oplist"
syn match   tomOperator         "%oparray"
syn match   tomMatch            "%match"
syn match   tomRule             "%rule"
syn match   tomStrat            "%strategy"
syn match   tomType             "%typeterm"
syn match   tomType             "%typelist"
syn match   tomType             "%typeint"
syn match   tomType             "%typearray"
syn match   tomType             "%subtype"
syn match   tomArrow            "->"
syn match   tomArrow            "<:"
syn match   tomMake             "`.*;"
syn match   tomMake             "`"
syn keyword tomOpkey            implement is_sort get_fun_sym get_subterm cmp_fun_sym fsym
syn keyword tomOpkey            equals get_head get_tail is_empty get_element is_fsym 
syn keyword tomOpkey            get_size make_empty
syn keyword tomOpkey            make_insert make_append
syn keyword tomOpkey            get_slot get_default
syn keyword tomOpkey            make
syn keyword tomOpkey            visit

syn cluster javaTop add=tomMetaQuote

" Define the default highlighting.
" For version 5.7 and earlier: only when not done already
" For version 5.8 and later: only when an item doesn't have highlighting yet
if version >= 508 || !exists("did_tom_syn_inits")
  if version < 508
    let did_tom_syn_inits = 1
    command -nargs=+ HiLink hi link <args>
  else
    command -nargs=+ HiLink hi def link <args>
  endif
  HiLink tomExternal   Include
  HiLink tomOperator   javaScopeDecl
  HiLink tomMatch      javaScopeDecl
  HiLink tomStrat      javaScopeDecl
  HiLink tomRule       javaScopeDecl
  HiLink tomType       Type
  HiLink tomArrow      Statement
  HiLink tomMake       Special
  HiLink tomOpkey      Statement
  HiLink tomMetaQuote  String 
  HiLink tomMetaQuoteEscape Special
  delcommand HiLink
endif

let b:current_syntax = "tom"
