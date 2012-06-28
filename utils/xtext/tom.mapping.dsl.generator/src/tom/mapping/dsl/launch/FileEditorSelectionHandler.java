package tom.mapping.dsl.launch;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;

public class FileEditorSelectionHandler extends AbstractTomMappingHandler {

	@Override
	protected IFile getFilePath(ExecutionEvent event) throws ExecutionException {
		IFile file = null;
		Object showInInput = HandlerUtil.getShowInInput(event);
		if (showInInput == null) {
			showInInput = HandlerUtil.getShowInInput(event);
		}
		if (showInInput instanceof FileEditorInput) {
			final FileEditorInput fileEditorInput = (FileEditorInput) showInInput;
			file = fileEditorInput.getFile();
			return file;
		}
		throw new ExecutionException("error : can't find the path of the .tmap file");
	}
}