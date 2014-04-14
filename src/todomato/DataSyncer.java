package todomato;

import hirondelle.date4j.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimeZone;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

//@author A0099332Y
/**
 * This class is the process for the Google Sync of the application.
 * To sync with google calendar service. A remote API service is built by A009933Y 
 * to handle the main logic in sync process. DataSyncer hence only need to send 
 * HTTP Post Request and receive the response from the API server. No sync process 
 * is done locally. 
 * 
 * The API service is deployed in Heroku, written in Python, using Flask microframework.
 * 
 * 
 * Sample HTTP Post Request JSON:
 * 
 * {
 * 	"auth": {
 *   	"username": "todomatotest@gmail.com",
 *   	"password": "cs2103todomato",
 *   	"last_sync": "2014-04-14T15:55:55.000+08:00",
 *   	"current_time": "2014-04-14T15:56:25.000+08:00"
 * 	},
 * 	"data": {
 *   	"tasklist": [tasks]
 * 	}
 * }
 * 
 * tasks:
 *     {
 *     "created": "2014-04-14T15:56:22.000+08:00",
 *     "description": "0101",
 *     "edit": "2014-04-15T15:55:39.000+08:00",
 *     "eid": "http://www.google.com/calendar/feeds/qaefsdqnfhv66or3dp2psjko40%40group.
 *     			calendar.google.com/events/i57utfat7p3olsknjl139225o4",
 *     "enddate": "2014-04-14",
 *     "endtime": "20:00:00.000+08:00",
 *     "location": "utown",
 *     "meta": {
 *       "completed": "true",
 *       "id": "-2064010008",
 *       "priority": "LOW",
 *       "timecode": "0101"
 *     },
 *     "startdate": "2014-04-14",
 *    "starttime": "19:00:00.000+08:00"
 *   }
 * 
 * Once receive the response JSON, DataSyncer will reproduce Task Object based on each 
 * task in JSON.
 * 
 * 
 */

public class DataSyncer extends Processor {
	
	private static final String SYNC_ERROR = "Sync Error";
	private static final String SYNC_ERROR_MSG = "Sync Error: please check your data file";
	private static final String AUTHORIZATION_ERROR = "Authorization Error";
	private static final String AUTHORIZATION_ERROR_MSG = "Authorization Error: please check your password and username. Reset with \"setsync <username> <password>\"";
	private static final String ERROR_KEY_IN_JSON = "error";
	private static final int INDEX_OF_END_TIME = 3;
	private static final int INDEX_OF_END_DATE = 2;
	private static final int INDEX_OF_START_TIME = 1;
	private static final int INDEX_OF_START_DATE = 0;
	private static final String DEFAULT_KEY_IN_JSON = "1111";
	private static final String CURRENT_TIME_KEY_IN_JSON = "current_time";
	private static final String LAST_SYNC_TIME_KEY_IN_JSON = "last_sync";
	private static final String PASSWORD_KEY_IN_JSON = "password";
	private static final String USERNAME_KEY_IN_JSON = "username";
	private static final String EMPTY_STRING = "";
	private static final String TIMEZONE_INFO_AND_SECOND = ":00.000+08:00";
	private static final String TIME_FORMAT = "hh:mm";
	private static final String TIMEZONE_INFO = ".000+08:00";
	private static final String TIME_FORMAT_PRECISE = "hh:mm:ss";
	private static final String DATE_TIME_DIVIDER = "T";
	private static final String DATE_FORMAT = "YYYY-MM-DD";
	private static final String NULL_STRING = "null";
	private static final char TIME_CODE_NOT_VALID = '0';
	private static final String TIME_CREATED_KEY_IN_JSON = "created";
	private static final String UPDATE_TIME_KEY_IN_JSON = "edit";
	private static final String END_DATE_KEY_IN_JSON = "enddate";
	private static final String START_DATE_KEY_IN_JSON = "startdate";
	private static final String END_TIME_KEY_IN_JSON = "endtime";
	private static final String START_TIME_KEY_IN_JSON = "starttime";
	private static final String LOCATION_KEY_IN_JSON = "location";
	private static final String DESCRIPTION_KEY_IN_JSON = "description";
	private static final String EVENT_ID_KEY_IN_JSON = "eid";
	private static final String ID_KEY_IN_JSON = "id";
	private static final String PRIORITY_KEY_IN_JSON = "priority";
	private static final String IS_COMPLETED_KEY_IN_JSON = "completed";
	private static final String CONTENT_TYPE = "Content-type";
	private static final String APPLICATION_JSON = "application/json";
	private static final String TASKLIST_KEY_IN_JSON = "tasklist";
	private static final String META_KEY_IN_JSON = "meta";
	private static final String DATA_KEY_IN_JSON = "data";
	private static final String AUTHORIZATION_KEY_IN_JSON = "auth";
	private static final String TIMECODE_KEY_IN_JSON = "timecode";
	private static final String SERVER_URL = "http://todomato-sync.herokuapp.com/todomato/api/v1.0/update";
//	for local test:
//	private static final String SERVER_URL = "http://127.0.0.1:5000/todomato/api/v1.0/update";
	
	TaskList localList;


	public DataSyncer (TaskList localList) {
		this.localList = localList;
	}
	
	/**
	 * method for sync data with google calendar using a cloud server
	 * @param username (google account)
	 * @param password (google account)
	 * @param lastSyncTime
	 * @return TaskList
	 * @throws SyncErrorException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public TaskList sync(String username, String password, DateTime lastSyncTime) throws SyncErrorException, ParseException, IOException {

		JsonObject localJson = prepareData(this.localList, username, password, lastSyncTime);
		JsonObject responseJson = sendRequest(localJson);
		localList = processResponse(responseJson);
		localList.setLastSyncTime(DateTime.now(TimeZone.getDefault()));
		localList.setUserName(username);
		localList.setPassword(password);
		
		return localList;
	}
	
	/**
	 * main function for creating JSON object from local data information. The object will be sent to cloud server.
	 * @param localList
	 * @param username
	 * @param password
	 * @param lastSyncTime
	 * @return
	 */
	private JsonObject prepareData(TaskList localList, String username, String password, DateTime lastSyncTime) {
		
		// create JSON objects for response
		JsonObject localJson = new JsonObject();
		JsonObject auth = new JsonObject();
		JsonObject data = new JsonObject();
		
		// add in header data for the response JSON
		initResponseJson(username, password, lastSyncTime, localJson, auth);
		JsonArray tasklist = createTaskJsonArray(localList);
		
		// add tasklist in response JSON
		data.add(TASKLIST_KEY_IN_JSON, tasklist);
		localJson.add(DATA_KEY_IN_JSON, data);
		return localJson;
	}
	
	/**
	 * main function for sending HTTP request with JSON object that contains Task info
	 * @param localJson
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private JsonObject sendRequest(JsonObject localJson) throws ParseException, IOException {
		
		HttpClient client = HttpClientBuilder.create().build();
		String postUrl = SERVER_URL;
		HttpPost post = new HttpPost(postUrl);
		StringEntity postingString;
		
		postingString = new StringEntity(localJson.toString());
		post.setEntity(postingString);
		post.setHeader(CONTENT_TYPE, APPLICATION_JSON );
		
		HttpResponse response = client.execute(post);
		String json = EntityUtils.toString(response.getEntity());
		JsonParser jsonParser = new JsonParser();
		JsonObject tJson = (JsonObject)jsonParser.parse(json);
		
		return tJson;
		
	}

	/**
	 * main function for turning response (in JSON format) into Tasks and add all tasks into a TaskList
	 * @param responseJson
	 * @return
	 */
	private TaskList processResponse(JsonObject responseJson) throws SyncErrorException {
		
		if (responseJson.has(TASKLIST_KEY_IN_JSON)) {
			// Initialization
			TaskList output = new TaskList();
			JsonArray tasklist = responseJson.getAsJsonArray(TASKLIST_KEY_IN_JSON);
			
			// convert task Json to Task for all jsons in the tasklist
			for(JsonElement t: tasklist) {
				Task task = convertJsonToTask(t);
				output.addToList(task);
			}
			
			return output;
		
		} else if (responseJson.has(ERROR_KEY_IN_JSON)) {
			String errorMessage = responseJson.get(ERROR_KEY_IN_JSON).getAsString();
			if( errorMessage.equals(AUTHORIZATION_ERROR)){
				throw new SyncErrorException(AUTHORIZATION_ERROR_MSG);
			}
			
			if ( errorMessage.equals(SYNC_ERROR)){
				throw new SyncErrorException(SYNC_ERROR_MSG);
			}
			
		}
		return localList;
		
	}
	

	/**
	 * helper function for converting JSON into Task Object
	 * @param json
	 * @return a Task generate from task Json
	 * @throws NumberFormatException
	 */
	private Task convertJsonToTask(JsonElement json) throws NumberFormatException {
		
		JsonObject tJson = json.getAsJsonObject();
		
		JsonObject meta = tJson.get(META_KEY_IN_JSON).getAsJsonObject();
		
		
		// get meta data
		String timeCode = meta.get(TIMECODE_KEY_IN_JSON).getAsString();
		boolean isCompleted = meta.get(IS_COMPLETED_KEY_IN_JSON).getAsBoolean();
		String priorityLevel = meta.get(PRIORITY_KEY_IN_JSON).getAsString();
		JsonElement idJson = meta.get(ID_KEY_IN_JSON);
		
		// get normal data
		String eventId = tJson.get(EVENT_ID_KEY_IN_JSON).getAsString();
		String description = tJson.get(DESCRIPTION_KEY_IN_JSON).getAsString();
		String location = tJson.get(LOCATION_KEY_IN_JSON).getAsString();
		DateTime startTime = stringToTime(tJson.get(START_TIME_KEY_IN_JSON).getAsString());
		DateTime endTime = stringToTime(tJson.get(END_TIME_KEY_IN_JSON).getAsString());
		DateTime startDate = stringToDate(tJson.get(START_DATE_KEY_IN_JSON).getAsString());
		DateTime endDate = stringToDate(tJson.get(END_DATE_KEY_IN_JSON).getAsString());
		DateTime updateTime = stringToDateTime(tJson.get(UPDATE_TIME_KEY_IN_JSON).getAsString());
		DateTime timeCreated = stringToDateTime(tJson.get(TIME_CREATED_KEY_IN_JSON).getAsString());
		
		ArrayList<DateTime> taskTime = createDateTimeForATask(timeCode, startDate, startTime, endDate, endTime);
		Integer id = null;
		
		if (!idJson.isJsonNull()){
			id = Integer.parseInt(idJson.getAsString());
		}
		
		Task task = createATask(isCompleted, priorityLevel, eventId, description,
				location, updateTime, timeCreated, taskTime, id);
		
		return task;
	}

	/**
	 * helper function to create a Task object based on values in JSON
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
		DateTime startDate = taskTime.get(INDEX_OF_START_DATE);
		DateTime startTime = taskTime.get(INDEX_OF_START_TIME);
		DateTime endDate = taskTime.get(INDEX_OF_END_DATE);
		DateTime endTime = taskTime.get(INDEX_OF_END_TIME);
	
		Task task = new Task(description);
		
		if(id != null){
			task.setId(id);
		}
		
		task.setStartTime(startTime);
		task.setEndTime(endTime);
		task.setStartDate(startDate);
		task.setEndDate(endDate);		
		task.setLocation(location);
		
		task.setTimeCreated(timeCreated);
		task.setEventId(eventId);
		task.setUpdateTime(updateTime);
		task.setPriorityLevel(priorityLevel);
		task.setCompleted(isCompleted);
		
		return task;
	}

	
	/**
	 * helper function to validate time attributes from JSON. Sync process from Google Calendar 
	 * may include additional invalid time attributes in JSON. 
	 * @param timeCode
	 * @param startDate
	 * @param startTime
	 * @param endDate
	 * @param endTime
	 * @return a timeArray which set the invalid time attribute to null
	 */
	private ArrayList<DateTime> createDateTimeForATask(String timeCode,
			DateTime startDate, DateTime startTime, DateTime endDate, DateTime endTime) {
		
		ArrayList<DateTime> timeArray = new ArrayList<DateTime>();
		timeArray.add(startDate);
		timeArray.add(startTime);
		timeArray.add(endDate);
		timeArray.add(endTime);

		for(int i = 0; i < timeCode.length(); i++){
			boolean isValidData = timeCode.charAt(i) != TIME_CODE_NOT_VALID;
			if(!isValidData){
				timeArray.set(i, null);
			}
		}
		
		return timeArray;
	}


	/** 
	 * helper function to format DateTime Object based on the data it contains
	 * @param dt
	 * @return YYYY-MM-DDThh:mm:ss.000+08:00 for DateTime Object with all data; 
	 * YYYY-MM-DD with date data only;
	 * hh:mm:00.000+08:00 for DateTime Object with time data only;
	 * Empty String if the Object is null
	 */
	private String formatTime(DateTime dt) {
		boolean datetimeIsNull = dt == null || dt.toString().equals(NULL_STRING);
		if (datetimeIsNull) {
			return EMPTY_STRING;
		} else if (dt.hasYearMonthDay() && isTime(dt)){
			return dt.format(DATE_FORMAT) + DATE_TIME_DIVIDER + dt.format(TIME_FORMAT_PRECISE) + TIMEZONE_INFO;
		} else if (!dt.hasYearMonthDay() && isTime(dt)){
			return dt.format(TIME_FORMAT) + TIMEZONE_INFO_AND_SECOND;
		} else if (!isTime(dt) && dt.hasYearMonthDay()){
			return dt.format(DATE_FORMAT);
		} else {
			return EMPTY_STRING;
		}
	}
	
	
	/** 
	 * helper function to check if a DateTime Object has Hour and Minute ONLY
	 * @param dt
	 * @return true if the object has Hour and Minute ONLY
	 */
	private boolean isTime(DateTime dt) {
		// check if dt is time hh:mm
		if (dt.getHour() != null && dt.getMinute()!= null){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * helper function to convert a string to DateTime Object
	 * @param s
	 * @return a DateTime Object with Year, Month, Day, Hour, Minute, Second
	 */
	private DateTime stringToDateTime(String s) {
		DateTime dt = new DateTime(s.substring(0,19));
		return dt;
	}
	
	/**
	 * helper function to convert a string to DateTime Object
	 * @param s
	 * @return a DateTime Object with Year, Month, Day ONLY
	 */
	private DateTime stringToDate(String s) {
		DateTime d = null;
		if (!s.isEmpty()){
			d = new DateTime(s);
		}
		
		return d;
	}
	
	/**
	 * helper function to convert a string to DateTime Object
	 * @param s
	 * @return a DateTime Object with Hour, Minute, Second ONLY
	 */
	private DateTime stringToTime(String s) {
		DateTime t = null;
		if (!s.isEmpty()){
			t = new DateTime(s.substring(0, 5));
		}
		
		return t;
	}
	
	/**
	 * helper function to check if a string is null string or is null
	 * @param s
	 * @return true if the string is not a null string or is not a null object
	 */
	private boolean notNullString(String s) {
		boolean isNullString = (s == null || s.equals(NULL_STRING));
		return !isNullString;
	}

	

	/**
	 * helper function to convert the local TaskList into JSON format
	 * @param localList
	 * @return JsonArray that contains tasks in Json
	 */
	private JsonArray createTaskJsonArray(TaskList localList) {
		ArrayList<Task> list = localList.getList();
		JsonArray tasklist = new JsonArray();
		for (Task t: list) {
			JsonObject tJson = convertTaskToJson(t);
			tasklist.add(tJson);
		}

		return tasklist;
	}

	/**
	 * helper function to initialize the JSON that is going to be send to the server
	 * @param username
	 * @param password
	 * @param lastSyncTime
	 * @param localJson
	 * @param auth
	 */
	private void initResponseJson(String username, String password,
			DateTime lastSyncTime, JsonObject localJson, JsonObject auth) {
		auth.addProperty(USERNAME_KEY_IN_JSON, username);
		auth.addProperty(PASSWORD_KEY_IN_JSON, password);
		auth.addProperty(LAST_SYNC_TIME_KEY_IN_JSON, formatTime(lastSyncTime));
		auth.addProperty(CURRENT_TIME_KEY_IN_JSON, formatTime(DateTime.now(TimeZone.getDefault())));
		localJson.add(AUTHORIZATION_KEY_IN_JSON, auth);
	}

	/**
	 * helper function to create an JSON object from the local Task
	 * @param t
	 * @return JsonObject that made from the task
	 */
	private JsonObject convertTaskToJson(Task t) {
		
		JsonObject tJson = new JsonObject();
		
		// if the task has never been updated, set its updateTime to its createTime
		if(t.getUpdateTime() == null) {
			t.setUpdateTime(t.getTimeCreated());
		}
		
		// Get local Task data
		String id = Integer.toString(t.getId());
		String description = t.getDescription();
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
		
		// write meta data into JSON object
		JsonObject meta = new JsonObject();
		meta.addProperty(ID_KEY_IN_JSON, id);
		meta.addProperty(TIMECODE_KEY_IN_JSON, timecode);
		meta.addProperty(PRIORITY_KEY_IN_JSON, priority);
		meta.addProperty(IS_COMPLETED_KEY_IN_JSON, completed);
		tJson.add(META_KEY_IN_JSON, meta);
		
		// write basic data into JSON object
		if (notNullString(description)){
			tJson.addProperty(DESCRIPTION_KEY_IN_JSON, description);
		}
		
		if (notNullString(starttime)){
			tJson.addProperty(START_TIME_KEY_IN_JSON, starttime);
		}
		
		if (notNullString(startdate)){
			tJson.addProperty(START_DATE_KEY_IN_JSON, startdate);
		}
		
		if (notNullString(enddate)){
			tJson.addProperty(END_DATE_KEY_IN_JSON, enddate);
		}
		
		if (notNullString(endtime)){
			tJson.addProperty(END_TIME_KEY_IN_JSON, endtime);
		}
		
		if (notNullString(created)){
			tJson.addProperty(TIME_CREATED_KEY_IN_JSON, created);
		}
		
		if (notNullString(location)){
			tJson.addProperty(LOCATION_KEY_IN_JSON, location);
		}else{
			tJson.addProperty(LOCATION_KEY_IN_JSON, EMPTY_STRING);
		}
		
		if (notNullString(eid)){
			tJson.addProperty(EVENT_ID_KEY_IN_JSON, eid);
		}
		
		if (notNullString(edit)){
			tJson.addProperty(UPDATE_TIME_KEY_IN_JSON, edit);
		}

		return tJson;
	}
	
	
	/**
	 * helper function to generate the timeCode for each Task based on the existence of its time attribute
	 * timeCode records the existence of a Task's time attribute (startDate, startTime, endDate, endTime)
	 * It is a four digit number that corresponds to four time attribute in the order of 
	 * (startDate, startTime, endDate, endTime)
	 * @param t
	 * @return timeCode in following format:
	 * 		"dddd"
	 * 		
	 * 		d: a binary digit of 0 or 1
	 * 		0: time attribute does not exist
	 * 		1: time attribute exists
	 * 		
	 * 		eg.
	 * 		A Task object has a timeCode of "1011"
	 * 		The Task object has startDate, endDate, endTime but not startTime
	 */
	private String generateTimeCode(Task t) {
		String timeCode = DEFAULT_KEY_IN_JSON;
		char[] timeCodeChars = timeCode.toCharArray();
		
		if(t.getStartDate() == null){
			timeCodeChars[INDEX_OF_START_DATE] = TIME_CODE_NOT_VALID;
		}
		
		if(t.getStartTime() == null){
			timeCodeChars[INDEX_OF_START_TIME] = TIME_CODE_NOT_VALID;
		}
		
		if(t.getEndDate() == null){
			timeCodeChars[INDEX_OF_END_DATE] = TIME_CODE_NOT_VALID;
		}
		
		if(t.getEndTime() == null){
			timeCodeChars[INDEX_OF_END_TIME] = TIME_CODE_NOT_VALID;
		}
		
		timeCode = String.valueOf(timeCodeChars);
				
		return timeCode;
	}
}
