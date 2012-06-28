package tom.mapping.dsl.launch;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class PackageExplorerSelectionHandler extends AbstractTomMappingHandler {

	@Override
	protected IFile getFilePath(ExecutionEvent event) throws ExecutionException {
		IFile file = null;
		final ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
		if (currentSelection instanceof IStructuredSelection) {
			final IStructuredSelection sel = (IStructuredSelection) currentSelection;
			final Object selElem = sel.getFirstElement();
			if (selElem instanceof IFile) {
				file = (IFile) selElem;
				return file;
			}
		}
		throw new ExecutionException("error : can't find the path of the .tmap file");
	}
}