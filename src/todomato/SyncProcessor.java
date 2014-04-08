package todomato;

public class SyncProcessor extends Processor {

	public static String processSync() {
		if (list.getUserName() == null && list.getPassword() == null) {
			new UsernamePasswordDialogueBox();
		}
		
		return "syncing...";
	}
}