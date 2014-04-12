package todomato;
import hirondelle.date4j.DateTime;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.TimeZone;






import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;






import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * This class is the process for the Google Sync of the application.
 * @author A0099332Y
 *
 */

public class DataSyncer extends Processor {
	
	TaskList localList;
	static String SERVER_URL = "http://todomato-sync.herokuapp.com/todomato/api/v1.0/update";
	//static String SERVER_URL = "http://127.0.0.1:5000/todomato/api/v1.0/update";

	
	public DataSyncer (TaskList localList) {
		this.localList = localList;
	}
			
	public TaskList sync(String username, String password, DateTime lastsync) {
		JsonObject localJson = prepareData(this.localList, username, password, lastsync);
		JsonObject responseJson = sendRequest(localJson);
		
		localList = processResponse(responseJson);
		localList.setLastSyncTime(DateTime.now(TimeZone.getDefault()));
		localList.setUserName(username);
		localList.setPassword(password);
		return localList;
	}
	
	private JsonObject sendRequest(JsonObject localJson) {
		
		HttpClient client = HttpClientBuilder.create().build();
		String postUrl = SERVER_URL;
		HttpPost post = new HttpPost(postUrl);
		StringEntity postingString;
		
		try {
			postingString = new StringEntity(localJson.toString());
			post.setEntity(postingString);
			post.setHeader("Content-type", "application/json");
			
			try {
				HttpResponse response = client.execute(post);
				String json = EntityUtils.toString(response.getEntity());
				JsonParser jsonParser = new JsonParser();
				System.out.println(json);
				JsonObject tJson = (JsonObject)jsonParser.parse(json);
				
				return tJson;
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return localJson;
		
		
	}

	private TaskList processResponse(JsonObject responseJson) {
		
		// Initialization
		TaskList output = new TaskList();
		JsonArray tasklist = responseJson.getAsJsonArray("tasklist");
		
		// convert task Json to Task for all jsons in the tasklist
		for(JsonElement t: tasklist) {
			Task task = convertJsonToTask(t);
			output.addToList(task);
		}
		return output;
	}

	/**
	 * @param t
	 * @return a Task generate from task Json
	 * @throws NumberFormatException
	 */
	private Task convertJsonToTask(JsonElement t) throws NumberFormatException {
		
		JsonObject tJson = t.getAsJsonObject();
		
		JsonObject meta = tJson.get("meta").getAsJsonObject();
		
		
		// meta data
		// timeCode = '1111', means all data is valid
		String timeCode = meta.get("timecode").getAsString();
		boolean isCompleted = meta.get("completed").getAsBoolean();
		String priorityLevel = meta.get("priority").getAsString();
		JsonElement idJson = meta.get("id");
		
		// normal data
		String eventId = tJson.get("eid").getAsString();
		String description = tJson.get("description").getAsString();
		String location = tJson.get("location").getAsString();
		ArrayList<DateTime> startDTList = stringToDateAndTime(tJson.get("starttime").getAsString());
		ArrayList<DateTime> endDTList = stringToDateAndTime(tJson.get("endtime").getAsString());
		DateTime updateTime = stringToDateTime(tJson.get("edit").getAsString());
		DateTime timeCreated = stringToDateTime(tJson.get("created").getAsString());
		
		ArrayList<DateTime> taskTime = createDateTimeForATask(timeCode, startDTList, endDTList);
		Integer id = null;
		
		if (!idJson.isJsonNull()){
			id = Integer.parseInt(idJson.getAsString());
		}
		
		
		Task task = createATask(isCompleted, priorityLevel, eventId, description,
				location, updateTime, timeCreated, taskTime, id);
		System.out.println("==========");
		System.out.println("timecode:" + timeCode);
		System.out.println(task);
		
		
		return task;
	}

	/**
	 * @param isCompleted
	 * @param priorityLevel
	 * @param eventId
	 * @param description
	 * @param location
	 * @param updateTime
	 * @param timeCreated
	 * @param taskTime
	 * @param id
	 * @return
	 */
	private Task createATask(boolean isCompleted, String priorityLevel,
			String eventId, String description, String location,
			DateTime updateTime, DateTime timeCreated,
			ArrayList<DateTime> taskTime, Integer id) {
		DateTime startDate = taskTime.get(0);
		DateTime startTime = taskTime.get(1);
		DateTime endDate = taskTime.get(2);
		DateTime endTime = taskTime.get(3);
	
		Task task = new Task(description);
		task.setStartTime(startTime);
		task.setEndTime(endTime);
		task.setStartDate(startDate);
		task.setEndDate(endDate);		
		task.setLocation(location);
		
		if(id != null){
			task.setId(id);
		}
		
		task.setTimeCreated(timeCreated);
		task.setEventId(eventId);
		task.setUpdateTime(updateTime);
		task.setPriorityLevel(priorityLevel);
		task.setCompleted(isCompleted);
		
		
		return task;
	}

	private ArrayList<DateTime> createDateTimeForATask(String timeCode,
			ArrayList<DateTime> startDTList, ArrayList<DateTime> endDTList) {
		// TODO Auto-generated method stub
		
		ArrayList<DateTime> timeArray = prepareTimeArray(startDTList, endDTList);
		
		for(int i = 0; i < timeCode.length(); i++){
			boolean isValidData = timeCode.charAt(i) != '0';
			if(!isValidData){
				timeArray.set(i, null);
			}
		}
		
		return timeArray;
	}

	/**
	 * @param startDTList
	 * @param endDTList
	 * @return 
	 */
	private ArrayList<DateTime> prepareTimeArray(ArrayList<DateTime> startDTList,
			ArrayList<DateTime> endDTList) {
		DateTime startDate = startDTList.get(0);
		DateTime startTime = startDTList.get(2);
		DateTime endDate = endDTList.get(0);
		DateTime endTime = endDTList.get(2);
		
		ArrayList<DateTime> time = new ArrayList<DateTime>();
		time.add(startDate);
		time.add(startTime);
		time.add(endDate);
		time.add(endTime);
		
		return time;
	}

	private String formatTime(DateTime dt) {
		
		
		
		if (dt == null || dt.toString().equals("null")  ) {
			return "";
		} else if (dt.hasYearMonthDay() && isTime(dt)){
			return dt.format("YYYY-MM-DD") + "T" + dt.format("hh:mm:ss") + ".000+08:00";
		} else if (!dt.hasYearMonthDay() && isTime(dt)){
			return dt.format("hh:mm") + ":00.000+08:00";
		} else if (!isTime(dt) && dt.hasYearMonthDay()){
			return dt.format("YYYY-MM-DD");
		} else {
			return "";
		}
	}
	
	private boolean isTime(DateTime dt) {
		// check if dt is time hh:mm
		if (dt.getHour() != null && dt.getMinute()!= null){
			return true;
		} else {
			return false;
		}
	}

	private DateTime stringToDateTime(String s) {
		DateTime dt = new DateTime(s.substring(0,19));
		return dt;
	}
	
	private ArrayList<DateTime> stringToDateAndTime(String s) {
		ArrayList<DateTime> datetimeList = new ArrayList<DateTime>();
		DateTime t = null;
		DateTime d = null;
		DateTime ts = null;
		if(s.contains("T")){
			d = new DateTime(s.substring(0,10));
			t = new DateTime(s.substring(11,19));
			ts = new DateTime(s.substring(11,16));
		}
		if (s.contains("-")){
			d = new DateTime(s.substring(0,10));
		}
		if (s.contains(":")) {
			t = new DateTime(s.substring(0,8));
			ts = new DateTime(s.substring(0,5));
		}
		datetimeList.add(d);
		datetimeList.add(t);
		datetimeList.add(ts);
		return datetimeList;
	}
	
	
	private boolean notNullString(String s) {
		boolean isNullString = (s == null || s.equals("null"));
		return !isNullString;
	}

	private JsonObject prepareData(TaskList localList, String username, String password, DateTime lastsync) {
		
		// create Json objects for response
		JsonObject localJson = new JsonObject();
		JsonObject auth = new JsonObject();
		JsonObject data = new JsonObject();
		
		// add in header datas for the response Json
		initResponseJson(username, password, lastsync, localJson, auth);
		JsonArray tasklist = createTaskJsonArray(localList);
		
		// add tasklist in response Json
		data.add("tasklist", tasklist);
		localJson.add("data", data);
		
		System.out.println(auth);
		
		return localJson;
	}

	/**
	 * @param localList
	 * @return JsonArray that contains tasks in Json
	 */
	private JsonArray createTaskJsonArray(TaskList localList) {
		ArrayList<Task> list = localList.getList();
		JsonArray tasklist = new JsonArray();
		
		for (Task t: list) {
			JsonObject tJson = convertTaskToJson(t);
			tasklist.add(tJson);
			System.out.println(tJson);
		}
		
		
		
		return tasklist;
	}

	/**
	 * @param username
	 * @param password
	 * @param lastsync
	 * @param localJson
	 * @param auth
	 */
	private void initResponseJson(String username, String password,
			DateTime lastsync, JsonObject localJson, JsonObject auth) {
		auth.addProperty("username", username);
		auth.addProperty("password", password);
		auth.addProperty("last_sync", formatTime(lastsync));
		localJson.add("auth", auth);
	}

	/**
	 * @param t
	 * @return JsonObject that made from the task
	 */
	private JsonObject convertTaskToJson(Task t) {
		JsonObject tJson = new JsonObject();
		
		String id = Integer.toString(t.getId());
		String description = t.getDescription();
		
		
		
		if(t.getUpdateTime() == null) {
			t.setUpdateTime(t.getTimeCreated());
		}
		
		
		
		String starttime = formatTime(t.getStartTime());
		String startdate = formatTime(t.getStartDate());
		String endtime = formatTime(t.getEndTime());
		String enddate = formatTime(t.getEndDate());
		String created = formatTime(t.getTimeCreated());
		String edit = formatTime(t.getUpdateTime());
		String location = t.getLocation();
		String eid = t.getEventId();
		String completed  = t.getCompleted().toString();
		String timecode = generateTimeCode(t);
		String priority = t.getPriorityLevel();
		
		JsonObject meta = new JsonObject();
		meta.addProperty("id", id);
		meta.addProperty("timecode", timecode);
		meta.addProperty("priority", priority);
		meta.addProperty("completed", completed);
		
		tJson.add("meta", meta);
		

		if (notNullString(description)){
			tJson.addProperty("description", description);
		}
		
		if (notNullString(starttime)){
			tJson.addProperty("starttime", starttime);
		}
		
		if (notNullString(startdate)){
			tJson.addProperty("startdate", startdate);
		}
		
		if (notNullString(enddate)){
			tJson.addProperty("enddate", enddate);
		}
		
		if (notNullString(endtime)){
			tJson.addProperty("endtime", endtime);
		}
		
		if (notNullString(created)){
			tJson.addProperty("created", created);
		}
		
		if (notNullString(location)){
			tJson.addProperty("location", location);
		}else{
			tJson.addProperty("location", "");
		}
		
		if (notNullString(eid)){
			tJson.addProperty("eid", eid);
		}
		
		if (notNullString(edit)){
			tJson.addProperty("edit", edit);
		}

		return tJson;
	}
	

	private String generateTimeCode(Task t) {
		String timeCode = "1111";
		char[] timeCodeChars = timeCode.toCharArray();
		
		if(t.getStartDate() == null){
			timeCodeChars[0] = '0';
		}
		
		if(t.getStartTime() == null){
			timeCodeChars[1] = '0';
		}
		
		if(t.getEndDate() == null){
			timeCodeChars[2] = '0';
		}
		
		if(t.getEndTime() == null){
			timeCodeChars[3] = '0';
		}
		
		timeCode = String.valueOf(timeCodeChars);
				
		return timeCode;
	}
}
