/**
 * 
 */
package todomato;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;

//@author A0101324A

/**
 * 
 * 
 */
public class Scheduling {
	/**
	 * @return Scheduler
	 * @throws SchedulerException
	 *             Get the tasks checking started
	 */
	protected static Scheduler schedulingTasks() throws SchedulerException {
		// Grab the Scheduler instance from the Factory
		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
		Scheduler scheduler = schedFact.getScheduler();
		// and start it off
		scheduler.start();
		schedulingNotification(scheduler);
		return scheduler;
	}

	/*
	 * Enable the quartz scheduler to keep running the program to check the
	 * notify time of the tasks before the program exits More efficient than a
	 * "while" loop
	 */

	protected static void schedulingNotification(Scheduler sche) {
		// define the job and tie it to KeepChecking class
		JobDetail job = newJob(KeepChecking.class).withIdentity("myJob",
				"group").build();

		// Trigger the job to run now, and then every 40 seconds
		Trigger trigger = newTrigger()
				.withIdentity("myTrigger", "group")
				.startNow()
				.withSchedule(
						simpleSchedule().withIntervalInSeconds(40)
								.repeatForever()).build();

		// Tell quartz to schedule the job using trigger
		try {
			sche.scheduleJob(job, trigger);

		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
