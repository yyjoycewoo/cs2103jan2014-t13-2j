package todomato;


//@author A0120766H
/** 
 * This class contains methods to process undo commands by the user.
 * It updates the user's lists of tasks, and saves it to disk.
 *
 */
public class RedoProcessor extends Processor {
	 private static final String NO_CHANGES_TO_REDO_MSG = "No changes to redo";
	 private static final String SUCCESS_MSG = "Last action redone";

		/**
		 * Undo last action, if possible
		 * @author Joyce
		 * @return Status message telling user if there were changes to redo
		 */
		public static String processRedo() {
			if (!redoList.isEmpty()) {
				undoList.push(list);
				
				list = redoList.pop();	
				fileHandler.updateFile(list);

				displayList = list;
				return SUCCESS_MSG;
			} else {
				return NO_CHANGES_TO_REDO_MSG;
			}
		}

 }
