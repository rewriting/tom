<!-- Find books in which the name of some element ends with the string "or" and the same element contains the string "Suciu" somewhere in its content. For each such book, return the title and the qualifying element.

Solution in XQuery: -->

for $b in doc("http://bstore1.example.com/bib.xml")//book
let $e := $b/*[contains(string(.), "Suciu") 
               and ends-with(local-name(.), "or")]
where exists($e)
return
    <book>
        { $b/title }
        { $e }
    </book> 

