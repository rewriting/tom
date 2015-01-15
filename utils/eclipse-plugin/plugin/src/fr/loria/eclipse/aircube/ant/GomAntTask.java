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

import tom.gom.*;
import java.io.File;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class GomAntTask extends Task
{

    public GomAntTask()
    {
        str_package = "";
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

    public void execute()
        throws BuildException
    {
        try
        {
            System.out.println("Compiling " + gomFile + "...");
            String str_command = "--javagen -n "+str_factory;
            if(options != null && !options.equals(""))
                str_command = str_command + " " + options.trim();
            if(str_command.indexOf("-i") < 0)
                str_command = str_command + " -i " + gomFile;

            if(str_command.indexOf("-o") < 0)
                str_command = str_command + " -o " + location;

            if(str_command.indexOf("-p") < 0 && !str_package.equals(""))
            {
                str_command = str_command + " -p " + str_package;
            }
            String cmd[] = split(str_command);
            Gom.main(cmd);
            System.out.println("Finished...");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("it does not work");
            throw new BuildException("Gom generation failed");
        }
    }

    public void setFile(File file)
    {
        gomFile = file.getAbsolutePath();
    }

    public void setFilename(String name)
    {
        gomFile = name;
    }

    public void setPackage(String str_pack)
    {
        str_package = str_pack;
    }

    public void setOptions(String options)
    {
        this.options = options;
    }

    public void setBasedir(File destinationDir)
    {
        location = destinationDir.getAbsolutePath();
    }

    public void setFactory(String str_factory) {
		this.str_factory = str_factory;
	}

    private String gomFile;
    private String options;
    private String location;
    private String str_package;
    private String str_factory;
    
} //class GomAntTask