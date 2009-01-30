<!-- Make a nested list of the section elements in Book1, preserving their original attributes and hierarchy. Inside each section element, include the title of the section and an element that includes the number of figures immediately contained in the section.

Solution in XQuery:

-->

declare function local:section-summary($book-or-section as element())
  as element()*
{
  for $section in $book-or-section
  return
    <section>
       { $section/@* }
       { $section/title }       
       <figcount>         
         { count($section/figure) }
       </figcount>                
       { local:section-summary($section/section) }                      
    </section>
};

<toc>
  {
    for $s in doc("book.xml")/book/section
    return local:section-summary($s)
  }
</toc> 

