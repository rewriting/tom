" Vim indent file
" Language:    Gom
" Maintainer:  Emmanuel Hainry <Emmanuel.Hainry@loria.fr>
" URL:         http://tom-server.loria.fr/confluence/display/Tom/tips
" Last modified: Mar 29, 2006  19:55
" new version fixes 2 problems:
" 	* de-indent when a Structure definition ends
" 	* not de-indent too much for closing brackets

" Only load this indent file when no other was loaded.
if exists("b:did_indent")
  finish
endif
let b:did_indent = 1

" setlocal expandtab
syntax enable

setlocal indentexpr=GetGomIndent()
setlocal indentkeys+=0\|,0\{,0\},o,O

" Comment formatting
if (has("comments"))
	setlocal comments=sr:/*,mb:*,ex:*/,://
	setlocal fo=croq
endif

if exists("*GetGomIndent")
	  finish
endif

function GetGomIndent()
	let lnum = v:lnum
	let pnum = prevnonblank(lnum - 1)
	if lnum == 0
		return 0
	endif
	let ind = indent(lnum)
	let pind= indent(pnum)
	let line = getline(lnum)
	let pline= getline(pnum)
	let re_fistpipe = '^\s*|'
	if line =~ re_fistpipe
		if pline =~ re_fistpipe
			return pind
		elseif pline =~ '='
			return match(pline, '=')
		else
			return ind + &sw
		endif
	elseif pline =~ re_fistpipe
		return 0
	endif
	if pline =~ '{[^}]*$'
		return pind + &sw
	endif
	if line =~ '^\s*}'
		return pind - &sw
	endif
	if line =~ '[ 	/]*\*'
		return ind
	endif
	return pind
endfunction

