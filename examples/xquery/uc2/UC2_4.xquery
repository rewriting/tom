<!-- How many top-level sections are in Book1?

Solution in XQuery: -->

<top_section_count>
 { 
   count(doc("book.xml")/book/section) 
 }
</top_section_count>

