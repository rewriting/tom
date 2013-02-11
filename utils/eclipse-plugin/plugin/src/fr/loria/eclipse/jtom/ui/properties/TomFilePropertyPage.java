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

package fr.loria.eclipse.jtom.ui.properties;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.IWorkbenchThemeConstants;
import org.eclipse.ui.themes.ITheme;

import fr.loria.eclipse.aircube.core.properties.AircubePropertyPage;
import fr.loria.eclipse.aircube.core.properties.PathPropertyEditor;
import fr.loria.eclipse.jtom.JtomPlugin;


/**
 * @author julien
 *
 */
public class TomFilePropertyPage extends AircubePropertyPage {
	
/*
 * Tom usage :
        tom [options] input[.t] [... input[.t]]
options :
        -X <file>:      Defines an alternate XML configuration file
        --cCode | -c:   Generate C code
        --camlCode:     Generate Caml code
        --camlSemantics:        Verify with caml semantics for match
        --compile:      Compiler (activated by default)
        --destdir <dir> | -d:   Specify where to place generated files
        --eclipse:      Sets Eclipse mode
        --encoding <charset> | -e:      Specify the character encoding
        --expand:       Expander (activated by default)
        --help | -h:    Print this help
        --import <path> | -I:   Path for %include
        --intermediate | -i:    Generate intermediate files
        --jCode | -j:   Generate Java code
        --lazyType | -l:        Use universal type
        --noDeclaration | -D:   Do not generate code for declarations
        --noOutput:     Do not generate code
        --noReduce:     Do not simplify extracted constraints (depends on --veri
fy)
        --noStatic:     Generate non static functions
        --noSyntaxCheck:        Do not perform syntax checking
        --noTypeCheck:  Do not perform type checking
        --optimize | -O:        Optimized generated code
        --optimize2 | -O2:      Optimized generated code
        --output <file> | -o:   Set output file name
        --parse:        Parser (activated by default)
        --pretty | -p:  Generate readable code
        --prettyPIL | -pil:     PrettyPrint IL
        --protected:    Generate protected functions
        --stamp:        Use stamp to simulate concrete ADT
        --verbose | -v: Set verbose mode on
        --verify:       Verify correctness of match compilation
        --version | -V: Print version
        --wall | -W:    Print warning

 */	
	
	private Text destDirText;
	private PathPropertyEditor includePathEditor;
	private DirectoryDialog destdir;
	private Button //c, java, caml, 
	    intermediate,
	    noOutput, 
	    noDeclaration, 
	    protectedCode,
		compile, 
		noSyntaxCheck, 
		noTypeCheck, 
		wall, 
		lazyType, 
		pretty,
		prettyPIL,
		noStatic, 
		debug, 
		memory,
		verify, 
		optimize,
//		autoDispatch,
//		nogen, 
		optimize2,
		verbose;
	private Combo targetLanguage;
	private Composite debugGroup,languageGroup, commandGenerationComposite;
	private CTabFolder optionFolder;
	private CTabItem item;
	
	private static String JAVA = JtomPlugin.getResourceString("java.hoover"), 
						 C    = JtomPlugin.getResourceString("c.hoover"), 
						 CAML = JtomPlugin.getResourceString("caml.hoover");
	
	protected boolean checkResource() {
		IResource resource = (IResource) getElement();
		String tomExt = JtomPlugin.getDefault().getPreferenceStore().getString(JtomPlugin.TOM_EXTENSION_PREFERENCE);
		if (!(resource.getType() == IResource.FILE) || 
				! ( ((IFile)resource).getFileExtension().equals(tomExt)) ) {
			return false;
		}
		return true;
	}
	
	protected Control showBadResourceCheckedDisplay(Composite parent) {
		noDefaultAndApplyButton();
		Label label = new Label(parent, SWT.LEFT);
		label.setText("Not a TOM file: Use the extension `."+JtomPlugin.getDefault().getPreferenceStore().getString(JtomPlugin.TOM_EXTENSION_PREFERENCE)+"`");
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		return label;
	}

	protected boolean analyseCommandLine(String command) {
		String statusMessage = "", option="";
		if(command.equals("")) {
			showStatus(command, 0);
			return true;
		}
		String options[] = command.split(" ");
		int warningLevel =0;
		boolean javaCode = true, noStatic = false;
	
		for (int i = 0; i<options.length;i++) {
			option = options[i];
			if(option.equals("")) {
				statusMessage = "Avoid extra white spaces!!";
				warningLevel = 1;
				break;
			} else if(option.equals("--help") || option.equals("-h")) {
				statusMessage = option+" : not supported option";
				warningLevel =2;
				break;
			} else if(option.equals("--version") || option.equals("-V")) {
				statusMessage = "Version is available from Tom/Versions Preference Page";
				warningLevel = 1;
				break;
			} else if(option.equals("--cCode") || option.equals("-c")) {
				javaCode = false;
			} else if(option.equals("--camlCode")) {
				javaCode = false;
			} else if(option.equals("--intermediate") || option.equals("-i")) {
//			} else if(option.equals("--nogeneration")) {
			} else if(option.equals("--verbose")) {
			} else if(option.equals("--noOutput")) {
			} else if(option.equals("--output") || option.equals("-o")) {
				i++;
			} else if(option.equals("--noDeclaration")) {
			} else if(option.equals("--compile")) {
			} else if(option.equals("--noSyntaxCheck")) {
			} else if(option.equals("--noTypeCheck")) {
			} else if(option.equals("--Wall")) {
			} else if(option.equals("--noWarning")) {
			} else if(option.equals("--lazyType") || option.equals("-l")) {
			} else if(option.equals("--optimize") || option.equals("-O")) {
			} else if(option.equals("--autoDispatch") || option.equals("-ad")) {	
			} else if(option.equals("--optimize2") || option.equals("-O2")){
			} else if(option.equals("--verbose") || option.equals("-v")) {
			} else if(option.equals("--import") || option.equals("-I")) {
				i++;
			} else if(option.equals("--destdir") || option.equals("-d")) {
				i++;
			} else if(option.equals("--pretty")) {
			} else if(option.equals("--prettyPIL")) {	
			} else if(option.equals("--protected")) {
				protectedCode.setSelection(true);
			} else if(option.equals("--noStatic")) {
				noStatic = true;
			} else if(option.equals("--debug")) {
			} else if(option.equals("--memory")) {
			} else if(option.equals("--verify")) {
				verify.setSelection(true);
			} else if(option.equals("--optimize")) {
				optimize.setSelection(true);
			} else {
				statusMessage = option+" : Unknown option";
				warningLevel =2;
				break;
			}
		}
		if(!javaCode && !noStatic) {
		  statusMessage = "Static option is only available for Java code";
			warningLevel = 1;
		}
		return showStatus(statusMessage, warningLevel);

	}

	/**
	 * @param commandGenerationComposite
	 */
	protected void createCustomCommandComposite(Composite commandGenerationComposite) {
		languageGroup = new Composite(commandGenerationComposite, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		languageGroup.setLayout(gridLayout);
		Label targetLanguageLbl = new Label(languageGroup,SWT.NONE);
		targetLanguageLbl.setText("Target language : ");
			
		targetLanguage = new Combo(languageGroup,SWT.READ_ONLY | SWT.V_SCROLL);
		targetLanguage.add(JAVA);
		targetLanguage.add(C);
		targetLanguage.add(CAML);
		targetLanguage.select(targetLanguage.indexOf(JAVA));
		targetLanguage.addSelectionListener(new targetLanguageSelectionAdapter());
		targetLanguage.addSelectionListener(new generatedCommandSelectionAdapter());

		
		optionFolder = new CTabFolder(commandGenerationComposite, SWT.TOP | SWT.BORDER);
		
		/* To get beautiful tabs like in eclipse main editor */
		
		ITheme theme = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme();	    
	    ColorRegistry colorRegistry = theme.getColorRegistry();
		
		optionFolder.setSimple(false);
        optionFolder.setSelectionForeground(colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_TEXT_COLOR));
        optionFolder.setSelectionBackground(
                        new Color[] {
                                colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_START),
                                colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_END) },
                        new int[] { theme.getInt(IWorkbenchThemeConstants.ACTIVE_TAB_PERCENT) },
                        theme.getBoolean(IWorkbenchThemeConstants.ACTIVE_TAB_VERTICAL));
			
		
		Composite optionsComposite = new Composite(optionFolder, SWT.NONE);
		GridLayout optionsCompositeGridLayout = new GridLayout ();
		optionsComposite.setLayout(optionsCompositeGridLayout);
		optionsCompositeGridLayout.numColumns = 2;
		
		GridData optionsCompositeGridData = new GridData();
		optionsCompositeGridData.horizontalSpan = 2;
		optionsComposite.setLayoutData(optionsCompositeGridData);
		
		intermediate = new Button(optionsComposite, SWT.CHECK);
		intermediate.setText("intermediate");
		intermediate.setToolTipText(JtomPlugin.getResourceString("intermediate.hoover"));
		intermediate.addSelectionListener(new generatedCommandSelectionAdapter());
		
		noOutput = new Button(optionsComposite, SWT.CHECK);
		noOutput.setText("noOutput");
		noOutput.setToolTipText(JtomPlugin.getResourceString("noOutput.hoover"));
		noOutput.addSelectionListener(new generatedCommandSelectionAdapter());
		
		noDeclaration = new Button(optionsComposite, SWT.CHECK);
		noDeclaration.setText("noDeclaration");
		noDeclaration.setToolTipText(JtomPlugin.getResourceString("noDeclaration.hoover"));
		noDeclaration.addSelectionListener(new generatedCommandSelectionAdapter());
		
		compile = new Button(optionsComposite, SWT.CHECK);
		compile.setText("compile");
		compile.setToolTipText(JtomPlugin.getResourceString("compile.hoover"));
		compile.addSelectionListener(new generatedCommandSelectionAdapter());
		
		noSyntaxCheck = new Button(optionsComposite, SWT.CHECK);
		noSyntaxCheck.setText("noSyntaxCheck");
		noSyntaxCheck.setToolTipText(JtomPlugin.getResourceString("noSyntaxCheck.hoover"));
		noSyntaxCheck.addSelectionListener(new generatedCommandSelectionAdapter());
		
		noTypeCheck = new Button(optionsComposite, SWT.CHECK);
		noTypeCheck.setText("noTypeCheck");
		noTypeCheck.setToolTipText(JtomPlugin.getResourceString("noTypeCheck.hoover"));
		noTypeCheck.addSelectionListener(new generatedCommandSelectionAdapter());
		
		wall = new Button(optionsComposite, SWT.CHECK);
		wall.setText("wall");
		wall.setToolTipText(JtomPlugin.getResourceString("wall.hoover"));
		wall.addSelectionListener(new generatedCommandSelectionAdapter());
		
		lazyType = new Button(optionsComposite, SWT.CHECK);
		lazyType.setText("lazyType");
		lazyType.setToolTipText(JtomPlugin.getResourceString("lazyType.hoover"));
		lazyType.addSelectionListener(new generatedCommandSelectionAdapter());
		
		pretty = new Button(optionsComposite, SWT.CHECK);
		pretty.setText("pretty");
		pretty.setToolTipText(JtomPlugin.getResourceString("pretty.hoover"));
		pretty.addSelectionListener(new generatedCommandSelectionAdapter());
		
		prettyPIL = new Button(optionsComposite, SWT.CHECK);
		prettyPIL.setText("prettyPIL");
		prettyPIL.setToolTipText(JtomPlugin.getResourceString("prettyPIL.hoover"));
		prettyPIL.addSelectionListener(new generatedCommandSelectionAdapter());		
		
		protectedCode = new Button(optionsComposite, SWT.CHECK);
		protectedCode.setText("protected");
		protectedCode.setToolTipText(JtomPlugin.getResourceString("protected.hoover"));
		protectedCode.addSelectionListener(new generatedCommandSelectionAdapter());
		
		noStatic = new Button(optionsComposite, SWT.CHECK);
		noStatic.setText("noStatic");
		noStatic.setToolTipText(JtomPlugin.getResourceString("noStatic.hoover"));
		noStatic.addSelectionListener(new generatedCommandSelectionAdapter());
		
		verify = new Button(optionsComposite, SWT.CHECK);
		verify.setText("verify");
		verify.setToolTipText(JtomPlugin.getResourceString("verify.hoover"));
		verify.addSelectionListener(new generatedCommandSelectionAdapter());
		
		optimize = new Button(optionsComposite, SWT.CHECK);
		optimize.setText("optimize");
		optimize.setToolTipText(JtomPlugin.getResourceString("optimize.hoover"));
		optimize.addSelectionListener(new generatedCommandSelectionAdapter());
				
		optimize2 = new Button(optionsComposite, SWT.CHECK);
		optimize2.setText("optimize2");
		optimize2.setToolTipText(JtomPlugin.getResourceString("optimize2.hoover"));
		optimize2.addSelectionListener(new generatedCommandSelectionAdapter());
		
//		autoDispatch = new Button(optionsComposite, SWT.CHECK);
//		autoDispatch.setText("autoDispatch");
//		autoDispatch.setToolTipText(JtomPlugin.getResourceString("autoDispatch.hoover"));
//		autoDispatch.addSelectionListener(new generatedCommandSelectionAdapter());
		
//		nogen = new Button(optionsComposite, SWT.CHECK);
//		nogen.setText("nogeneration");
//		nogen.setToolTipText(JtomPlugin.getResourceString("nogen.hoover"));
//		nogen.addSelectionListener(new generatedCommandSelectionAdapter());
		
		verbose = new Button(optionsComposite, SWT.CHECK);
		verbose.setText("verbose");
		verbose.setToolTipText(JtomPlugin.getResourceString("verbose.hoover"));
		verbose.addSelectionListener(new generatedCommandSelectionAdapter());
		
		item = new CTabItem(optionFolder,SWT.NONE);
		item.setText("Options");
		item.setControl(optionsComposite);
		
		  // PathEditor
		Composite pathComposite = new Composite(optionFolder, SWT.NONE);
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
		
		item = new CTabItem(optionFolder,SWT.NONE);
		item.setText("Include path");
		item.setControl(pathComposite);
		
		debugGroup = new Composite(optionFolder, SWT.NONE);
		GridLayout gridLayout2 = new GridLayout();
		debugGroup.setLayout(gridLayout2);
		debugGroup.setLayoutData (new GridData (GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		debugGroup.setEnabled(false);
		debug = new Button(debugGroup, SWT.CHECK);
		debug.setText("generate debug macro");
		debug.setToolTipText(JtomPlugin.getResourceString("debug.hoover"));
		debug.addSelectionListener(new generatedCommandSelectionAdapter());
		debug.setEnabled(false);
		memory = new Button(debugGroup, SWT.CHECK);
		memory.setText("Manage memory");
		memory.setToolTipText(JtomPlugin.getResourceString("memory.hoover"));
		memory.addSelectionListener(new generatedCommandSelectionAdapter());
		memory.setEnabled(false);

		item = new CTabItem(optionFolder,SWT.NONE);
		item.setText("Debug");
		item.setControl(debugGroup);
		
		
		createDestDir(commandGenerationComposite);
		
		
		// Separator
		/*
		Label sep = new Label(commandGenerationComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData sepGridData = new GridData();
		sepGridData.horizontalSpan = 2;
		sep.setLayoutData(sepGridData);*/
		
		// Generated command line
		createGeneratedCommandLineText();
		optionFolder.setSelection(0);
	}

	private void createDestDir(Composite commandGenerationComposite) {
	Composite composite = new Composite(commandGenerationComposite,SWT.NONE);
	GridLayout layout = new GridLayout ();
	composite.setLayout(layout);
	layout.numColumns = 2;
	
    destDirText = new Text(composite, SWT.BORDER);
    destDirText.addModifyListener(new ModifyListener() {
    	public void modifyText(ModifyEvent e) {
    		updateGeneratedCommandLineText(generateCommand());
     	}
    });
    destdir = new DirectoryDialog(composite.getShell(),SWT.SAVE);
 
    destdir.setFilterPath(((IResource)getElement()).getProject().getLocation().toOSString());
   
    destdir.setText("Select destination directory");
    
    Button open = new Button(composite, SWT.PUSH);
    open.setText("Select Destination Directory");
    open.addSelectionListener(new SelectionAdapter() {
    	public void widgetSelected(SelectionEvent e) {
    		String selectedDir = destdir.open();
    		// selectedDir is null when an error occurs or when the dialog was cancelled
    		if (selectedDir != null) {
    		    destDirText.setText(selectedDir);
    		}
     	}
    });
	}



	class customCommandSelectionAdapter extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			
		}
	}
	
	class targetLanguageSelectionAdapter extends SelectionAdapter  {
		public void widgetSelected(SelectionEvent e) {
		    
		    noStatic.setEnabled(!targetLanguage.getText().equals(C) &&
		                             !targetLanguage.getText().equals(CAML));
		     	        
			/*if (e.getSource() == c || e.getSource() == caml) {
			    noStatic.setEnabled(false);
			} else {
			    noStatic.setEnabled(true);
			}*/
			updateGeneratedCommandLineText(generateCommand());
		}
	}
	
	/**
	 * @param resource
	 */
	protected void refreshCustomCommand(IResource resource) {
		commandGenerationComposite.setVisible(false);
		customCommandLineText.setVisible(true);
		try{
			QualifiedName qName = new QualifiedName(JtomPlugin.CUSTOM_COMMAND_PROPERTY, resource.getLocation().toString());
			String command = resource.getPersistentProperty(qName);
			if(command == null) {
				customCommandLineText.setText("");
				oldCustomCommandLine = "";
			} else {
				customCommandLineText.setText(command);
				oldCustomCommandLine = command;
			}
		} catch (CoreException e) {
			System.out.println("Exception catched in TomFilePropertyPage::refreshCustomCommand "+e.getMessage());
		}

		
	}
	
	protected void refreshGeneratedCommandFromString(String command) { 
		String options[] = command.split(" ");
		boolean javaCode = true;
		String pathList = "";
		for (int i = 0; i<options.length;i++) {
			if(options[i].equals("")) {
				// does not matter
			} else if(options[i].equals("--cCode")) {
			    targetLanguage.select(targetLanguage.indexOf(C));
				//c.setSelection(true);
				noStatic.setEnabled(false);
				//java.setSelection(false);
				javaCode = false;
			} else if(options[i].equals("--camlCode")) {
			    targetLanguage.select(targetLanguage.indexOf(CAML));
				//caml.setSelection(true);
				noStatic.setEnabled(false);
				//java.setSelection(false);
				javaCode = false;
			} else if(options[i].equals("--intermediate")) {
				intermediate.setSelection(true);
			} else if(options[i].equals("--noOutput")) {
				noOutput.setSelection(true);
			} else if(options[i].equals("--noDeclaration")) {
				noDeclaration.setSelection(true);
			} else if(options[i].equals("--compile")) {
				compile.setSelection(true);
			} else if(options[i].equals("--noSyntaxCheck")) {
				noSyntaxCheck.setSelection(true);
			} else if(options[i].equals("--noTypeCheck")) {
				noTypeCheck.setSelection(true);	
			} else if(options[i].equals("--wall")) {
				wall.setSelection(true);
			} else if(options[i].equals("--lazyType")) {
				lazyType.setSelection(true);
			} else if(options[i].equals("--import")) {
				pathList += options[++i]+ File.pathSeparator + "\n\r";
			} else if(options[i].equals("--destdir")) {
				destDirText.setText(options[++i]);
			} else if(options[i].equals("--pretty")) {
				pretty.setSelection(true);
			} else if(options[i].equals("--prettyPIL")) {
				prettyPIL.setSelection(true);	
			} else if(options[i].equals("--protected")) {
				protectedCode.setSelection(true);
			} else if(options[i].equals("--noStatic")) {
				noStatic.setSelection(true);
			} else if(options[i].equals("--debug")) {
				debug.setSelection(true);
			} else if(options[i].equals("--memory")) {
				memory.setSelection(true);
			} else if(options[i].equals("--verify")) {
				verify.setSelection(true);
			} else if(options[i].equals("--optimize")) {
				optimize.setSelection(true);
			}
			else if(options[i].equals("--optimize2")) {
				optimize2.setSelection(true);
//			} else if(options[i].equals("--autoDispatch")) {
//				autoDispatch.setSelection(true);				
//			} else if(options[i].equals("--nogeneration")) {
//					nogen.setSelection(true);
			} else if(options[i].equals("--verbose")) {
				verbose.setSelection(true);
			}
		}
		if(javaCode) {
			targetLanguage.select(targetLanguage.indexOf(JAVA));
			//java.setSelection(true);
			//noStatic.setEnabled(true);
		}
		includePathEditor.doLoad(pathList);
	}
	
	/**
	 * 
	 */
	protected void createGroups(Composite parent) {
	    GridLayout gridLayout = new GridLayout ();
		/*languageGroup = new Group(parent, SWT.BORDER);
		languageGroup.setLayout(gridLayout);
		gridLayout.numColumns = 1;
		languageGroup.setLayoutData (new GridData (GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		languageGroup.setText ("Target language");
		*/
		debugGroup = new Group(parent, SWT.BORDER);
		GridLayout gridLayout2 = new GridLayout ();
		debugGroup.setLayout(gridLayout2);
		gridLayout.numColumns = 1;
		debugGroup.setLayoutData (new GridData (GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		debugGroup.setEnabled(false);
	}

	
	protected String generateCommand() {
		String res = "";
		if(targetLanguage.getText().equals(C)){
			res+= " --cCode";
		} else if(targetLanguage.getText().equals(CAML)){
			res+= " --camlCode";
		}	    
		/*    
		if(c.getSelection()) {
			res+= " --cCode";
		}
		if(java.getSelection()) {
		}
		if(caml.getSelection()) {
			res+= " --camlCode";
		}*/
		if(intermediate.getSelection()) {
			res+= " --intermediate";
		}
		if(noOutput.getSelection()) {
			res+= " --noOutput";
		}
		if(noDeclaration.getSelection()) {
			res+= " --noDeclaration";
		}
		if(compile.getSelection()) {
			res+= " --compile";
		}
		if(noSyntaxCheck.getSelection()) {
			res+= " --noSyntaxCheck";
		}
		if(noTypeCheck.getSelection()) {
			res+= " --noTypeCheck";
		}
		if(wall.getSelection()) {
			res+= " --wall";
		}
		if(lazyType.getSelection()) {
			res+= " --lazyType";
		}
		if(pretty.getSelection()) {
			res+= " --pretty";
		}
		if(prettyPIL.getSelection()) {
			res+= " --prettyPIL";
		}
		if(protectedCode.getSelection()) {
			res+= " --protected";
		}
		if(noStatic.isEnabled() && noStatic.getSelection()) {
			res+= " --noStatic";
		}
		if(debug.getSelection()) {
			res+= " --debug";
		}
		if(memory.getSelection()) {
			res+= " --memory";
		}
		if(verify.getSelection()) {
			res+= " --verify";
		}
		if(optimize.getSelection()) {
			res+= " --optimize";
		}
		if(optimize2.getSelection()) {
			res+= " --optimize2";
		}
//		if(autoDispatch.getSelection()) {
//			res+= " --autoDispatch";
//		}		
//		if(nogen.getSelection()) {
//			res+= " --nogeneration";
//		}
		if(verbose.getSelection()) {
			res+= " --verbose";
		}
		if(!destDirText.getText().equals("")) {
			res+= " --destdir "+destDirText.getText();
		}
		String pathList[] = includePathEditor.getArrayValue();
		for (int i=0;i<pathList.length;i++) {
			res += " --import "+pathList[i];
		}
		return res.equals("")?res:res.substring(1);
	}

} // Class TomFilePropertyPage