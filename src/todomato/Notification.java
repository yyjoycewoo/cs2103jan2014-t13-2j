//@author A0101324A
package todomato;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 * This class creates a pop up notification to appear on the desktop.
 * 
 */

public abstract class Notification implements ActionListener {
	private static int count = 0, width = 200;

	/**
	 * @param myownlist
	 * @param taskToDo
	 */
	protected static void popUpNotice(TaskList list) {
		String msg = "~~~Reminder! Do now or never!~~~";
		// pop up disappeared after 2sec
		final int waitfor = 2000;
		// create and set up the window
		final JFrame frame = new JFrame("Reminder");

		frame.setSize(300, 125);
		// remove the title bar and border
		frame.setUndecorated(true);
		// change the window location to bottom right corner of the screen
		// size of the screen
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		// height of the task bar
		Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(
				frame.getGraphicsConfiguration());
		frame.setLocation((scrSize.width - frame.getWidth()), (scrSize.height
				- (frame.getHeight()) - toolHeight.bottom));
		frame.setLayout(new GridBagLayout());
		frame.setAlwaysOnTop(true);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;

		final JLabel textLabel = new JLabel();
		buttonsAction(textLabel, list);

		textLabel.setPreferredSize(new Dimension(300, 100));
		textLabel.setOpaque(false);
		frame.getContentPane().add(textLabel, constraints);
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
		frame.getContentPane().add(closeBt, constraints);
		constraints.gridx = 0;
		constraints.gridy++;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;

		JLabel msgLabel = new JLabel("<HtMl>" + msg);
		// Centralizing the text on the JLabel
		msgLabel.setHorizontalAlignment(JLabel.CENTER);
		frame.add(msgLabel, constraints);
		// close window after clicking on cross
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// to make pop up disappear after some time
		new Thread() {
			@Override
			public void run() {
				// when mouse over, it wont disappear until the mouse exits
				mouseActions(waitfor, frame);
			}
		}.start();
	}

	/**
	 * @param waitfor
	 * @param frame
	 */
	protected static void mouseActions(final int waitfor, final JFrame frame) {
		// ensure that the pop up wont disappear when mouse over
		frame.addMouseListener(new MouseAdapter() {
			Timer timer;

			@Override
			public void mouseEntered(MouseEvent event) {
				// cos can only cancel timer for one time only!
				// need new timer every time the mouse enters pop up
				if (timer != null) {
					timer.cancel();
				}
			}

			@Override
			public void mouseExited(MouseEvent event) {
				// If mouse pointer is still within the bounds of the
				// frame, do not set the timer to close the notification
				// window
				Rectangle frameRect = new Rectangle(frame.getLocationOnScreen());
				frameRect.setSize(frame.getWidth(), frame.getHeight());
				Point mousePointer = event.getLocationOnScreen();
				if (!frameRect.contains(mousePointer)) {
					// Otherwise, schedule a timer
					timer = new Timer();
					timer.schedule(new java.util.TimerTask() {
						@Override
						public void run() {
							frame.dispose();
						}
					}, waitfor);
				}
			}

		});
	};

	/**
	 * @param textLabel
	 * @return
	 */
	protected static void buttonsAction(final JLabel textLabel,
			final TaskList list) {
		// allow the next task to be shown after clicking the button
		ActionListener actionListener_prev = new ActionListener() {
			private String text;

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (count <= 0) {
					count = 0;
					// allow text to be within the JLabel border
					text = String.format("<html><div WIDTH=%d>%s</div><html>",
							width, list.getListItem(count).toString());
				} else {
					count--;
					text = String.format("<html><div WIDTH=%d>%s</div><html>",
							width, list.getListItem(count).toString());
				}
				textLabel.setText(text);
			}
		};

		// allow the next task to be shown after clicking the button
		ActionListener actionListener_next = new ActionListener() {
			private String text;

			@Override
			public void actionPerformed(ActionEvent actionEvent) {

				// get the next item in myownlist
				if (count >= (list.getSize() - 1)) {
					count = list.getSize() - 1;
					text = String.format("<html><div WIDTH=%d>%s</div><html>",
							width, list.getListItem(count).toString());

				} else {
					count++;
					// allow text to be within the JLabel border
					text = String.format("<html><div WIDTH=%d>%s</div><html>",
							width, list.getListItem(count).toString());
				}
				textLabel.setText(text);
			}
		};
		putButtonsOnLabel(textLabel, actionListener_prev, actionListener_next,
				list);
	}

	/**
	 * @param textLabel
	 * @param actionListener_prev
	 * @param actionListener_next
	 */
	protected static void putButtonsOnLabel(final JLabel textLabel,
			ActionListener actionListener_prev,
			ActionListener actionListener_next, TaskList list) {

		// implementing buttons on the label
		textLabel.setLayout(new BorderLayout());
		JButton next = new BasicArrowButton(BasicArrowButton.EAST);
		JButton prev = new BasicArrowButton(BasicArrowButton.WEST);
		prev.addActionListener(actionListener_prev);
		next.addActionListener(actionListener_next);
		textLabel.add(prev, BorderLayout.WEST);
		textLabel.add(next, BorderLayout.EAST);
		String text = String.format("<html><div WIDTH=%d>%s</div><html>",
				width, list.getListItem(0).toString());
		textLabel.setText(text);
		// Centralizing the text on the JLabel
		textLabel.setHorizontalAlignment(JLabel.CENTER);
	}
}
