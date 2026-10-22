package fr.loria.eclipse.jtom.mappinggenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;


public class MGClassLoader extends URLClassLoader {

  public MGClassLoader(URL[] urls) {
    super(urls);
  }

  public Class<?> getClassObj(File file) throws ClassNotFoundException, FileNotFoundException, IOException{
    // TODO - check the cast ... 
    byte[] classBytes = new byte[(int)file.length()];
    new FileInputStream(file).read(classBytes);
    return defineClass(null,classBytes,0,classBytes.length);
  }
}
