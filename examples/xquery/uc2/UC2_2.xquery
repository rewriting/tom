<!-- Prepare a (flat) figure list for Book1, listing all the figures and their titles. Preserve the original attributes of each <figure> element, if any. -->

<!-- Solution in XQuery: -->

<figlist>
  {
    for $f in doc("book.xml")//figure
    return
        <figure>
            { $f/@* }
            { $f/title }
        </figure>
  }
</figlist> 
