<!-- Prepare a (nested) table of contents for Book1, listing all the sections and their titles. Preserve the original attributes of each <section> element, if any. -->

<!-- Solution in XQuery: -->

declare function local:toc($book-or-section as element()) as element()*
{
    for $section in $book-or-section/section
    return
      <section>
         { $section/@* , $section/title , local:toc($section) }                 
      </section>
};

<toc>
   {
     for $s in doc("book.xml")/book return local:toc($s)
   }
</toc> 
