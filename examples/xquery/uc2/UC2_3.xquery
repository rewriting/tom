<!-- How many sections are in Book1, and how many figures? -->

<!-- Solution in XQuery: -->

<section_count>{ count(doc("book.xml")//section) }</section_count>, 
<figure_count>{ count(doc("book.xml")//figure) }</figure_count> 
