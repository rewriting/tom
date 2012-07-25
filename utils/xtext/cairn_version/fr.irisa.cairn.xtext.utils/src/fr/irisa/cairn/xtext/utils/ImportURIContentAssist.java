package fr.irisa.cairn.xtext.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.ui.editor.contentassist.AbstractContentProposalProvider;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.util.Strings;

/**
 * A tool for assisting user when searching an URI in a XText generated editor
 * for example. Visible files are sharing the same defined extension.
 * 
 * @author antoine
 * 
 */
public class ImportURIContentAssist {
	private final String resource;
	private final AbstractContentProposalProvider provider;
	private final FilenameFilter filter;
	private final String name;
	private final static String DEFAULT_NAME = "importURI";
	/**
	 * The eclipse workspace.
	 */
	private static final IWorkspace workspace = ResourcesPlugin.getWorkspace();

	/**
	 * The eclipse workspace's root
	 */
	private static final IWorkspaceRoot root = workspace.getRoot();

	public ImportURIContentAssist(String ext, String resource,
			AbstractContentProposalProvider provider, String name) {
		this.resource = resource;
		this.provider = provider;
		this.filter = new Filter(ext);
		this.name = name;
	}

	public ImportURIContentAssist(String ext, String resource,
			AbstractContentProposalProvider provider) {
		this(ext, resource, provider, DEFAULT_NAME);
	}

	public void complete(EObject model, Assignment assignment,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {

		String current = (String) model.eGet(model.eClass()
				.getEStructuralFeature(name));

		String contentRelative = current.replace(resource, "/");
		if (current.contains(resource)) {
			String path = root.getLocation().toOSString() + contentRelative;
			boolean backtracked = false;
			if (!new File(path).exists()) {
				String backtrack = "";
				String[] previous = path.split("/");
				for (int i = 0; i < previous.length - 1; ++i) {
					backtrack += previous[i] + "/";
				}
				path = backtrack;
				backtracked = true;
			}
			List<String> contents;
			if (!backtracked && new File(path).isDirectory()) {
				path += "/";
				if (!current.endsWith("/"))
					current += "/";
				contents = getContents(new File(path), "");
			} else
				contents = getContents(new File(path),
						Strings.lastToken(current, "/"));
			for (int i = 0; i < contents.size(); ++i) {
				String dir = "";
				String c = contents.get(i);
				if (isDirectory(path + c))
					dir = "/";

				ICompletionProposal proposal = provider
						.createCompletionProposal("\"" + current + c + dir,
								current + c + dir, getImage(new File(current
										+ c)), context);
				acceptor.accept(proposal);
			}
		}
		provider.completeRuleCall(((RuleCall) assignment.getTerminal()),
				context, acceptor);
	}

	private boolean isDirectory(String s) {
		return new File(s).isDirectory();
	}

	private List<String> getContents(File e, String prefix) {
		List<String> contents = new ArrayList<String>();
		for (String c : e.list(filter)) {
			if (c.startsWith(prefix)) {
				contents.add(findRemainingContent(c, prefix));
			}

		}
		return contents;
	}

	private String findRemainingContent(String content, String prefix) {
		String[] split = content.split(prefix);
		String remaining = split[1];
		for (int i = 2; i < split.length; ++i) {
			if (split[i].length() > 0)
				remaining += prefix + split[i];
		}
		return remaining;
	}

	private Image getImage(Object o) {
		return provider.getLabelProvider().getImage(o);
	}

	private static class Filter implements FilenameFilter {
		private String ext;

		public Filter(String ext) {
			super();
			this.ext = ext;
		}

		public boolean accept(File dir, String name) {
			boolean isDir = new File(dir.getPath() + "/" + name).isDirectory();
			return name.endsWith(ext) || isDir && !name.startsWith(".");
		}

	}

}