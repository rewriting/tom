%include{ TNode.tom }
{
  `XML(<A>
          <B/>
        </A>);
  
  `XML(<A at1="foo" at2=x at3=dd("text")/>);
  /*
  ``xml(<IntegerList/>);
  ``xml(<IntegerList> integers* <Integer>#TEXT(s)</Integer>
        </IntegerList>);
  ``xml(<IntegerList> integers* <Int>#TEXT(s)</Int>
        </IntegerList>);
  ``swapElements(<IntegerList attr*>X1* n2 n1 X2*</IntegerList>);
  */
  ``toto
}
 
