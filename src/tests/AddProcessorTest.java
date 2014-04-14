package tests;

import static org.junit.Assert.*;

import java.util.TimeZone;

import hirondelle.date4j.DateTime;

import org.junit.Test;

import todomato.AddProcessor;
import todomato.InvalidInputException;
import todomato.Task;

//@author A0096620E
public class AddProcessorTest {

	/*This is a test case to test for locations for more than one words*/
	@Test
	public void testAddTimeDateLoc() throws InvalidInputException {
		Task testTask = null;
		Integer startHour = 14;
		Integer endHour = 16;
		Integer month = 3;
		Integer day = 22;
		String test = "dinner with parents at 2pm March 22 until 4pm in utown starbucks";
		testTask = AddProcessor.parseTask(test);
		assertEquals("", testTask.getDescription(), "dinner with parents");
		assertEquals("", testTask.getStartTime().getHour(), startHour);
		assertEquals("", testTask.getEndTime().getHour(), endHour);
		assertEquals("", testTask.getStartDate().getDay(), day);
		assertEquals("", testTask.getStartDate().getMonth(), month);
		assertEquals("", testTask.getLocation(), "utown starbucks");
	}
	
	/*This is a test case to test for locations of one word*/
	@Test
	public void testAddTimeDateLocOneWord() throws InvalidInputException {
		Task testTask = null;
		Integer hour = 0;
		Integer month = 3;
		Integer day = 15;
		String test = "dinner with parents at 0am on March 15 in utown";
		testTask = AddProcessor.parseTask(test);
		assertEquals("", testTask.getDescription(), "dinner with parents");
		assertEquals("", testTask.getStartTime().getHour(), hour);
		assertEquals("", testTask.getEndDate().getDay(), day);
		assertEquals("", testTask.getEndDate().getMonth(), month);
		assertEquals("", testTask.getLocation(), "utown");
	}
	
	/*Tests boundary case of 12pm for the Time Parser
	 *Tests at keyword that has time and date
	 *Tests until keyword that has time and date */
	@Test
	public void testAddDateTime() throws InvalidInputException {
		Task testTask = null;
		String test = "project at 12pm dec 4 until 11am dec 31";
		Integer day = 31;
		Integer month = 12;
		Integer startHour = 12;
		Integer endHour = 11;
		testTask = AddProcessor.parseTask(test);
		assertEquals("", "project", testTask.getDescription());
		assertEquals("", month, testTask.getStartDate().getMonth());
		assertEquals("", (Integer) 4 , testTask.getStartDate().getDay());
		assertEquals("", month, testTask.getEndDate().getMonth());
		assertEquals("", day, testTask.getEndDate().getDay());
		assertEquals("", startHour, testTask.getStartTime().getHour());
		assertEquals("", endHour, testTask.getEndTime().getHour());
	}
	/* This is a boundary case of Jan 1  for the Date Parser
	 * Tests description with keywords enclosed in ""*/
	@Test
	public void testAddDateJan1() throws InvalidInputException {
		Task testTask= null;
		String test = "\"grab lunch on the way to school\" on 1 Jan at 09:00";
		testTask = AddProcessor.parseTask(test);
		assertEquals("", testTask.getDescription(), "grab lunch on the way to school");
		assertEquals("", testTask.getStartTime(), new DateTime("09:00"));
		assertEquals("",testTask.getEndDate(), new DateTime("2014-01-01"));
	}	
	
	/* Tests boundary case of Dec 31  for the Date Parser
	 * Tests parameter weekly for recurrence
	 * Tests @ location keyword */
	@Test
	public void testAddDateDec31() throws InvalidInputException {
		Task testTask= null;
		String test = "dinner with parents at 1900 Dec 31 recur weekly priority 2 @food court";
		testTask = AddProcessor.parseTask(test);
		assertEquals("", testTask.getLocation(), "food court");
		assertEquals("", testTask.getPriorityLevel(), "MEDIUM");
		assertEquals("", testTask.getRecurrencePeriod(), 7);
		assertEquals("", testTask.getDescription(), "dinner with parents");
		assertEquals("", testTask.getEndDate(), new DateTime("2014-12-31"));
		assertEquals("", testTask.getEndDate(), new DateTime("2014-12-31"));
	}	
	
	/* Tests for "today" for date
	 * Tests for shortcut ! for priority
	 * Tests daily for recurrence*/
	@Test
	public void testAddDateToday() throws InvalidInputException {
		Task testTask = null;
		String test = "lunch with parents on today at 11am !high recur daily";
		testTask = AddProcessor.parseTask(test);
		assertEquals("", testTask.getDescription(), "lunch with parents");
		assertEquals("", testTask.getStartTime(), new DateTime("11:00"));
		assertEquals("", testTask.getPriorityLevel(), "HIGH");
		assertEquals("", testTask.getRecurrencePeriod(), 1);
		assertEquals("", testTask.getEndDate(), DateTime.today(TimeZone.getDefault()));
	}
	

	public void testAddDateTomorrow() throws InvalidInputException {
		Task testTask = null;
		String test = "dinner with parents at 7pm until 2100 tomorrow";
		testTask = AddProcessor.parseTask(test);
		assertEquals("", testTask.getDescription(), "dinner with parents");
		assertEquals("", testTask.getStartDate(), DateTime.today(TimeZone.getDefault()).plusDays(1));
		assertEquals("", testTask.getStartTime(), new DateTime("19:00:00"));
		assertEquals("", testTask.getEndTime(), new DateTime("21:00:00"));
		assertEquals("", testTask.getEndDate(), DateTime.today(TimeZone.getDefault()).plusDays(1));
	}
	
}