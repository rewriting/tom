<!-- In the document "prices.xml", find the minimum price for each book, in the form of a "minprice" element with the book title as its title attribute.

Solution in XQuery: -->

<results>
  {
    let $doc := doc("prices.xml")
    for $t in distinct-values($doc//book/title)
    let $p := $doc//book[title = $t]/price
    return
      <minprice title="{ $t }">
        <price>{ min($p) }</price>
      </minprice>
  }
</results> 

