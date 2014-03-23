/**
 * 
 */
package todomato;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * This class creates a pop up notification to appear on the desktop.
 * 
 * @author Hao Eng
 * @source from
 *         http://www.javacodegeeks.com/2012/10/create-new-message-notification
 *         -pop-up.html
 */
public class Notification {

	/**
	 * @param taskToDo
	 */
	protected static void popUpNotice(String taskToDo) {
		String msg = "~~~Reminder! Do now or never!~~~";
		// pop up disappeared after 7sec
		final int waitfor = 7000;
		// create and set up the window
		final JFrame frame = new JFrame("Reminder");
		// change the window location to bottom right corner of the screen
		// size of the screen
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		// height of the task bar
		Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(
				frame.getGraphicsConfiguration());
		frame.setSize(300, 125);
		frame.setLocation((scrSize.width - frame.getWidth()), (scrSize.height
				- frame.getHeight() - toolHeight.bottom));
		frame.setUndecorated(true);
		frame.setLayout(new GridBagLayout());
		frame.setAlwaysOnTop(true);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;

		JLabel textLabel = new JLabel(taskToDo, SwingConstants.CENTER);
		textLabel.setPreferredSize(new Dimension(300, 100));
		// frame.getContentPane().add(textLabel, BorderLayout.CENTER);
		textLabel.setOpaque(false);
		frame.add(textLabel, constraints);
		constraints.gridx++;
		constraints.weightx = 0f;
		constraints.weighty = 0f;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.NORTH;

		@SuppressWarnings("serial")
		// allow closing of button
		JButton closeBt = new JButton(new AbstractAction("X") {
			@Override
			public void actionPerformed(final ActionEvent e) {
				frame.dispose();
			}
		});

		closeBt.setMargin(new Insets(1, 4, 1, 4));
		closeBt.setFocusable(false);
		frame.add(closeBt, constraints);
		constraints.gridx = 0;
		constraints.gridy++;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;

		JLabel msgLabel = new JLabel("<HtMl>" + msg);
		frame.add(msgLabel, constraints);
		// close window after clicking on cross
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// to make pop up disappear after some time
		new Thread() {
			@Override
			public void run() {
				try {
					// time after which pop up will be disappeared
					Thread.sleep(waitfor);
					frame.dispose();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
}
