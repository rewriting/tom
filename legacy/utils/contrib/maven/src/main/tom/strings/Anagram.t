/*
 * Copyright (c) 2005-2011, INPL, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *      - Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *      - Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *      - Neither the name of the INRIA nor the names of its
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package strings;

public class Anagram {

  %include{ string.tom }

  public final static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage: java Anagram <word1> <word2>");
      System.exit(1);
    }
    Anagram test = new Anagram();
    test.showIsAnagram(args[0],args[1]);
  }

  public boolean isAnagram(String w1, String w2) {
    %match(String w1, String w2) {
      concString(),concString() -> {
        return true;
      }
      concString(l,a1*),concString(b2*,l,a2*) -> {
//         `isAnagram(a1,b2*+a2*);
        return `isAnagram(a1,concString(b2*,a2*));
      }
    }
    return false;
  }

  private void showIsAnagram(String w1, String w2) {
    if(isAnagram(w1,w2)) {
      System.out.println("They are anagrams.");
    } else {
      System.out.println("They aren't anagrams.");
    }
  }
}
