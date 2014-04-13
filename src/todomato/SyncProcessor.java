package todomato;

//@author A0099332Y
public class SyncProcessor extends Processor {

	private static final String NO_USERNAME_PW = "Set username and password with \"setsync <username> <password>\"";
	private static final String SYNC_COMPLETE = "Sync completed";
	public static String processSync() {
		if ((list.getUserName() == null && list.getPassword() == null) ||
		(list.getUserName().equals("null") && list.getPassword().equals("null"))){
			return NO_USERNAME_PW;
		}
		
		String username = list.getUserName();
		String password = list.getPassword();
		
		DataSyncer Syncer = new DataSyncer(list);
		TaskList newList = Syncer.sync(username, password, list.getLastSyncTime());
		list = newList;
		fileHandler.updateFile(list);
		displayList.deepCopy(list);
		
		return SYNC_COMPLETE;

	}
}