/*
  
    TOM - To One Matching Compiler

    Copyright (c) 2004-2015, Inria Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr
	  Julien Guyon			e-mail: Julien.Guyon@loria.fr
	  Bertrand Tavernier (CRIL Technology)
	
*/

package fr.loria.eclipse.aircube.ant;

import java.io.*;
import java.util.Vector;
import tom.engine.Tom;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class TomAntTask extends Task {

    private String tomFile;
    private String options;
    private String location;
    public TomAntTask()
    {
    }

    private String[] split(String str)
    {
        try
        {
            String res[] = new String[0];
            int begin = 0;
            int end = 0;
            Vector<String> list = new Vector<String>();
            while(end < str.length()) 
            {
                while(end < str.length() && str.charAt(end) != ' ') 
                    end++;
                list.add(str.substring(begin, end));
                begin = ++end;
            }
            return (String[])list.toArray(res);
        }
        catch(Exception x)
        {
            return new String[0];
        }
    }

    public void execute() throws BuildException {
        try {
            System.out.println("Compiling " + tomFile + "...");
            String str_opts = tomFile + " --verbose";
            if(options != null)
                str_opts = str_opts + " " + options.trim();
            if(str_opts.indexOf(" -I") < 0)
                str_opts = str_opts + " -I " + location;
            String opt[] = split(str_opts);
            Tom.main(opt);
            System.out.println("Finished...");
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("it does not work");
            throw new BuildException("Tom generation failed");
        }
    }

    public void setFile(File file) {
        tomFile = file.getAbsolutePath();
    }

    public void setFilename(String name) {
        tomFile = name;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public void setDestdir(File destinationDir) {
        String str_package = getPackage();
        location = destinationDir.getAbsolutePath() + File.separator + (str_package == null ? "" : str_package);
    }

    private String getPackage() {
        String packageName = null;
        try {
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(tomFile)));
            do {
                String line = reader.readLine();
                if(line == null)
                    break;
                if(packageName == null) {
                    int index = line.indexOf("package");
                    if(index != -1) {
                        index += 7;
                        int end = line.indexOf(';', index);
                        if(end != -1) {
                            packageName = line.substring(index, end);
                            packageName = packageName.trim();
                        }
                    }
                }
            } while(packageName == null);
        } catch(Exception exception) { }
        if(packageName != null)
            packageName = packageName.replace('.', File.separatorChar);
        return packageName;
    }
    
} //class TomAntTask