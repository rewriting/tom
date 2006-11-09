/*
 * Copyright (c) 2004-2006, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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
package bytecode;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SecureAccess {

  public void sread(String filename){
    String fichier = filename;
    File f = new File(fichier);  
    if(!f.isHidden()) {
      // we open the file if it is not hidden
      System.out.println("Open file");
      FileInputStream fis;
      try {
        fis = new FileInputStream(f);
        BufferedInputStream bis = new BufferedInputStream(fis);  
        DataInputStream dis = new DataInputStream(bis); 
        BufferedReader d=new BufferedReader(new InputStreamReader(dis));
        String record = null;  
        try {  
          while ( (record=d.readLine()) != null ) {  
            System.out.println(record);
          }  
        } catch (IOException e) {  
          e.printStackTrace();
        }  
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }  
    } else {
      System.out.println("a hidden file cannot be read!");
    }
  }

  public void sreadF(String fileName){
    File f = new File(fileName);  
    if(!f.isHidden()) {
      try {
        FileReader fr=new FileReader(f);
        fr.read();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
