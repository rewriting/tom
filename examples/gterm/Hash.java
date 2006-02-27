public class Hash {
	public static int stringHashFunction(String name, int arity) {
    int a, b, c;
    /* Set up the internal state */
    a = b = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    /*------------------------------------- handle the last 11 bytes */
    int len = name.length();
    if (len >= 12) {
      return stringHashFunction2(name,arity);
    }
    c = arity + 1;
    c += len;
    switch (len) {
      case 11:
        c += (name.charAt(10) << 24);
      case 10:
        c += (name.charAt(9) << 16);
      case 9:
        c += (name.charAt(8) << 8);
        /* the first byte of c is reserved for the length */
      case 8:
        b += (name.charAt(7) << 24);
      case 7:
        b += (name.charAt(6) << 16);
      case 6:
        b += (name.charAt(5) << 8);
      case 5:
        b += name.charAt(4);
      case 4:
        a += (name.charAt(3) << 24);
      case 3:
        a += (name.charAt(2) << 16);
      case 2:
        a += (name.charAt(1) << 8);
      case 1:
        a += name.charAt(0);
        /* case 0: nothing left to add */
    }
    // mix(a,b,c);

    a -= b;
    a -= c;
    a ^= (c >> 13);
    b -= c;
    b -= a;
    b ^= (a << 8);
    c -= a;
    c -= b;
    c ^= (b >> 13);
    a -= b;
    a -= c;
    a ^= (c >> 12);
    b -= c;
    b -= a;
    b ^= (a << 16);
    c -= a;
    c -= b;
    c ^= (b >> 5);
    a -= b;
    a -= c;
    a ^= (c >> 3);
    b -= c;
    b -= a;
    b ^= (a << 10);
    c -= a;
    c -= b;
    c ^= (b >> 15);

    return c;
  }

  private static int stringHashFunction2(String name, int arity) {
    int offset = 0;
    int count = name.length();
    char[] source = new char[count];

    offset = 0;
    name.getChars(0, count, source, 0);
    int a, b, c;
    /* Set up the internal state */
    int len = count;
    a = b = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    c = arity + 1; // to avoid collison
    /*------------------------------------- handle the last 11 bytes */
    int k = offset;

    while (len >= 12) {
      a += (source[k + 0] + (source[k + 1] << 8) + (source[k + 2] << 16) + (source[k + 3] << 24));
      b += (source[k + 4] + (source[k + 5] << 8) + (source[k + 6] << 16) + (source[k + 7] << 24));
      c += (source[k + 8] + (source[k + 9] << 8) + (source[k + 10] << 16) + (source[k + 11] << 24));
      // mix(a,b,c);
      a -= b;
      a -= c;
      a ^= (c >> 13);
      b -= c;
      b -= a;
      b ^= (a << 8);
      c -= a;
      c -= b;
      c ^= (b >> 13);
      a -= b;
      a -= c;
      a ^= (c >> 12);
      b -= c;
      b -= a;
      b ^= (a << 16);
      c -= a;
      c -= b;
      c ^= (b >> 5);
      a -= b;
      a -= c;
      a ^= (c >> 3);
      b -= c;
      b -= a;
      b ^= (a << 10);
      c -= a;
      c -= b;
      c ^= (b >> 15);

      k += 12;
      len -= 12;
    }
    /*---------------------------------------- handle most of the key */
    c += count;
    switch (len) {
      case 11:
        c += (source[k + 10] << 24);
      case 10:
        c += (source[k + 9] << 16);
      case 9:
        c += (source[k + 8] << 8);
        /* the first byte of c is reserved for the length */
      case 8:
        b += (source[k + 7] << 24);
      case 7:
        b += (source[k + 6] << 16);
      case 6:
        b += (source[k + 5] << 8);
      case 5:
        b += source[k + 4];
      case 4:
        a += (source[k + 3] << 24);
      case 3:
        a += (source[k + 2] << 16);
      case 2:
        a += (source[k + 1] << 8);
      case 1:
        a += source[k + 0];
        /* case 0: nothing left to add */
    }
    // mix(a,b,c);
    a -= b;
    a -= c;
    a ^= (c >> 13);
    b -= c;
    b -= a;
    b ^= (a << 8);
    c -= a;
    c -= b;
    c ^= (b >> 13);
    a -= b;
    a -= c;
    a ^= (c >> 12);
    b -= c;
    b -= a;
    b ^= (a << 16);
    c -= a;
    c -= b;
    c ^= (b >> 5);
    a -= b;
    a -= c;
    a ^= (c >> 3);
    b -= c;
    b -= a;
    b ^= (a << 10);
    c -= a;
    c -= b;
    c ^= (b >> 15);

    //System.out.println("static doobs_hashFunctionAFun = " + c + ": " + name);
    return c;
  }



}
