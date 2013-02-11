/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 Inria
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 * Julien Guyon						e-mail: Julien.guyon@loria.fr
 * 
 **/

package fr.loria.eclipse.gom.properties;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import fr.loria.eclipse.aircube.core.properties.*;
import fr.loria.eclipse.jtom.JtomPlugin;

/**
 * @author julien
 *
 */
public class GOMFilePropertyPage extends AircubePropertyPage {

	private DirectoryDialog destdir;
	private Button		
		debug,
		verbose,
		verbosedebug,
		intermediate,
		wall; 
//		generator;
		
	private Text packageName, destDirText;
	private PathPropertyEditor includePathEditor;

	/* (non-Javadoc)
	 * @see fr.loria.eclipse.aircube.core.properties.AircubePropertyPage#analyseCommandLine(java.lang.String)
	 */
	protected boolean analyseCommandLine(String command) {
		/*usage :
        gom [options] file [... file_n]
options :
        -X <file>:      Defines an alternate XML configuration file
*        --debug | -vv:  Display debugging info
        --destdir <dir> | -d:   Specify where to place generated files
*        --verbose | -v: Display compilation information
*        --intermediate | -i:    Generate intermediate files
*        --verbosedebug | -vvv:  Display even more debugging info
*        --wall | -W:    Print warning
*        --generator <type> | -g:        Select Generator
        --import <path> | -I:   Path for include
*        --package <packageName> | -p:   Specify package name (optional)
*        --version | -V: Print version
*        --help | -h:    Show this help*/
		String statusMessage = "", option="";
		if(command.equals("")) {
			showStatus(command, 0);
			return true;
		}
		String options[] = command.split(" ");
		int warningLevel =0;
		
		for (int i = 0; i<options.length;i++) {
			option = options[i];
			if(option.equals("")) {
				statusMessage = "Avoid extra white spaces!!";
				warningLevel = 1;
				break;
			} else if(option.equals("--help") || option.equals("--version") || option.equals("-V")) {
				statusMessage = option+" is not supported: Help and version is available from Preference page";
				warningLevel = 1;
				break;
			} else if(option.equals("--verbose") || option.equals("-v")) {
			} else if(option.equals("--verbosedebug") || option.equals("-vvv")) {
			} else if(option.equals("--debug") || option.equals("-vv")) {
			} else if(option.equals("--visitable") || option.equals("-t")) {
			} else if(option.equals("--wall") || option.equals("-W")) {
			} else if(option.equals("--destdir") || option.equals("-d")) {
				i++;
			} else if(option.equals("--name") || option.equals("-n")) {
				i++;
			} else if(option.equals("--package") || option.equals("-p")) {
				i++;
			} else if(option.equals("--generator") || option.equals("-g")) {
				i++;	
			/*} else if(option.equals("--import") || option.equals("-m")) {
				i++;*/
//			} else if(option.equals("--output") || option.equals("-o")) {
//				statusMessage = "--output not yet manage";
//				warningLevel =2;
//				break;
			} else if(option.equals("--intermediate") || option.equals("-i")) {
//			} else if(option.equals("--nogeneration")) {
			} else {
				statusMessage = option+" : Unknown option";
				warningLevel =2;
				break;
			}
		}
		return showStatus(statusMessage, warningLevel);
	}

	private void createDestDir(Composite commandGenerationComposite) {
		Composite destDirCompo = new Composite(commandGenerationComposite, SWT.NONE);
		GridLayout destDirLayout = new GridLayout(2, false);
		destDirCompo.setLayout(destDirLayout);
    destDirText = new Text(destDirCompo, SWT.BORDER);
    destDirText.addModifyListener(new ModifyListener() {
    	public void modifyText(ModifyEvent e) {
    		updateGeneratedCommandLineText(generateCommand());
     	}
    });
    destdir = new DirectoryDialog(destDirCompo.getShell());
    destdir.setText("Select destination directory");
    Button open = new Button(destDirCompo, SWT.PUSH);
    open.setText("select Destination Directory");
    open.addSelectionListener(new SelectionAdapter() {
    	public void widgetSelected(SelectionEvent e) {
    		String selectedDir = destdir.open();
    		destDirText.setText(selectedDir);
     	}
    });
	}
	/* (non-Javadoc)
	 * @see fr.loria.eclipse.aircube.core.properties.AircubePropertyPage#createCustomCommandComposite(org.eclipse.swt.widgets.Composite)
	 */
	protected void createCustomCommandComposite(Composite commandGenerationComposite) {
		Composite optionsComposite = new Composite(commandGenerationComposite, SWT.BORDER);
		GridLayout optionsCompositeGridLayout = new GridLayout ();
		optionsCompositeGridLayout.numColumns = 2;
		optionsComposite.setLayout(optionsCompositeGridLayout);
		
		
		GridData optionsCompositeGridData = new GridData();
		optionsCompositeGridData.horizontalSpan = 2;
		optionsComposite.setLayoutData(optionsCompositeGridData);
		
		debug = new Button(optionsComposite, SWT.CHECK);
		debug.setText("debug");
		debug.setToolTipText(JtomPlugin.getResourceString("debugGOM.hoover"));
		debug.addSelectionListener(new generatedCommandSelectionAdapter());
		
		verbose = new Button(optionsComposite, SWT.CHECK);
		verbose.setText("verbose");
		verbose.setToolTipText(JtomPlugin.getResourceString("verboseGOM.hoover"));
		verbose.addSelectionListener(new generatedCommandSelectionAdapter());
		
		verbosedebug = new Button(optionsComposite, SWT.CHECK);
		verbosedebug.setText("verbosedebug");
		verbosedebug.setToolTipText(JtomPlugin.getResourceString("verbosedebugGOM.hoover"));
		verbosedebug.addSelectionListener(new generatedCommandSelectionAdapter());		
		
		intermediate = new Button(optionsComposite, SWT.CHECK);
		intermediate.setText("intermediate");
		intermediate.setToolTipText(JtomPlugin.getResourceString("intermediateGOM.hoover"));
		intermediate.addSelectionListener(new generatedCommandSelectionAdapter());
		
		wall = new Button(optionsComposite, SWT.CHECK);
		wall.setText("wall");
		wall.setToolTipText(JtomPlugin.getResourceString("wallGOM.hoover"));
		wall.addSelectionListener(new generatedCommandSelectionAdapter());		
		
//		generator = new Button(optionsComposite, SWT.CHECK);
//		generator.setText("generator");
//		generator.setToolTipText(VasPlugin.getResourceString("generatorGOM.hoover"));
//		generator.addSelectionListener(new generatedCommandSelectionAdapter());
		
		Composite nameComposite = new Composite(commandGenerationComposite, SWT.BORDER);
		GridData nameCompositeData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		nameCompositeData.horizontalSpan = 2;
		nameComposite.setLayoutData(nameCompositeData);
		
		GridLayout nameLayout = new GridLayout();
		nameLayout.numColumns = 2;
		nameComposite.setLayout(nameLayout);
				
//		Label APINameLabel = new Label(nameComposite, SWT.NONE);
//		APINameLabel.setText("Name");
//		APINameLabel.setToolTipText(VasPlugin.getResourceString("name.hoover"));
//		apiName = new Text(nameComposite, SWT.BORDER);
//		apiName.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent e) {
//				updateGeneratedCommandLineText(generateCommand());
//			}
//		});
		Label packageOptionLabel = new Label(nameComposite, SWT.NONE);
		packageOptionLabel.setText("Package");
		packageOptionLabel.setToolTipText(JtomPlugin.getResourceString("package.hoover"));
		packageName = new Text(nameComposite, SWT.BORDER);
		packageName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateGeneratedCommandLineText(generateCommand());
			}
		});
		GridData packageGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);

		packageName.setLayoutData(packageGridData);
		//apiName.setLayoutData(nameGridData);
		
		 // PathEditor
		Composite pathComposite = new Composite(commandGenerationComposite, SWT.NONE);
		GridLayout pathLayout = new GridLayout(2, false);
		pathComposite.setLayout(pathLayout);
		
		includePathEditor = new PathPropertyEditor("List of included path location to search", "Choose",
				pathComposite);
		includePathEditor.getListControl(pathComposite).addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					updateGeneratedCommandLineText(generateCommand());
				}
			}
		);
		createDestDir(commandGenerationComposite);
		createGeneratedCommandLineText();
	}

	/* (non-Javadoc)
	 * @see fr.loria.eclipse.aircube.core.properties.AircubePropertyPage#refreshGeneratedCommandFromString(java.lang.String)
	 */
	protected void refreshGeneratedCommandFromString(String command) {
		String options[] = command.split(" ");
		String pathList = "", option ="";
		for (int i = 0; i<options.length;i++) {
			option = options[i];
			if(option.equals("")) {
				// does not matter				
			} else if(option.equals("--debug") || option.equals("-vv")) {
				debug.setSelection(true);
			} else if(option.equals("--verbose") || option.equals("-v")) {
				verbose.setSelection(true);
			} else if(option.equals("--verbosedebug") || option.equals("-vvv")) {
				verbosedebug.setSelection(true);	
			} else if(option.equals("--wall")) {
				wall.setSelection(true);
			} else if(option.equals("--intermediate") || option.equals("-i")) {
				intermediate.setSelection(true);
//			} else if(option.equals("--generator")) {
//				generator.setSelection(true);
			} else if(option.equals("--package") || option.equals("-p")) {
				packageName.setText(options[++i]);
//			} else if(option.equals("--name") || option.equals("-n")) {
//				apiName.setText(options[++i]);
			} else if(option.equals("--import") || option.equals("-I")) {
				pathList += options[++i]+ File.pathSeparator + "\n\r";
			} else if(option.equals("--destdir")) {
				destDirText.setText(options[++i]);
			}
		}
		includePathEditor.doLoad(pathList);
		
	}

	/* (non-Javadoc)
	 * @see fr.loria.eclipse.aircube.core.properties.AircubePropertyPage#generateCommand()
	 */
	protected String generateCommand() {
		String res = "";
		
		if(debug.getSelection()) {
			res+= " --debug";
		}
		if(verbose.getSelection()) {
			res+= " --verbose";
		}
		if(verbosedebug.getSelection()) {
			res+= " --verbosedebug";
		}
		if(intermediate.getSelection()) {
			res+= " --intermediate";
		}
//		if(generator.getSelection()) {
//			res+= " --generator";
//		}
		if(wall.getSelection()) {
			res+= " --wall";
		}
		if(!packageName.getText().equals("")) {
			res+= " --package "+packageName.getText();
		}
//		if(!apiName.getText().equals("")) {
//			res+= " --name "+apiName.getText();
//		}
		if(!destDirText.getText().equals("")) {
			res+= " --destdir "+destDirText.getText();
		}
		/*String pathList[] = includePathEditor.getArrayValue();
		for (int i=0;i<pathList.length;i++) {
			res += " --import "+pathList[i];
		}*/
		return res.equals("")?res:res.substring(1);
	}
	
} // Class GOMFilePropertyPage
