
package todomato;

import java.io.IOException;
import org.apache.http.ParseException;

//@author A0099332Y
/**
 * This is the class that handles google sync function
 *
 */
public class SyncProcessor extends Processor {

	private static final String NO_USERNAME_PW = "Set username and password with \"setsync <username> <password>\"";
	private static final String SYNC_COMPLETE = "Sync completed";
	private static final String CONNECTION_ERROR = "Connection Error: Please check your Internet Connection";
	private static final String SYNC_ERROR = "Sync Error: Oops, A Unknown Sync Error has occurred!";
	
	public static String processSync() {
		
		// Prepare google login credentials
		if ((list.getUserName() == null && list.getPassword() == null) ||
		(list.getUserName().equals("null") && list.getPassword().equals("null"))){
			return NO_USERNAME_PW;
		}
		String username = list.getUserName();
		String password = list.getPassword();
		
		// Sync and replace the local Tasklist
		DataSyncer Syncer = new DataSyncer(list);
		TaskList newList;
		try {
			newList = Syncer.sync(username, password, list.getLastSyncTime());
		} catch (ParseException e) {
			return SYNC_ERROR;
		} catch (SyncErrorException e) {
			return e.getMessage();
		} catch (IOException e) {
			return CONNECTION_ERROR;
		}
		list = newList;
		fileHandler.updateFile(list);
		displayList.deepCopy(list);
		
		return SYNC_COMPLETE;

	}
}