<!-- In the document "books.xml", find all section or chapter titles that contain the word "XML", regardless of the level of nesting.

Solution in XQuery: -->

<results>
  {
    for $t in doc("books.xml")//(chapter | section)/title
    where contains($t/text(), "XML")
    return $t
  }
</results> 

