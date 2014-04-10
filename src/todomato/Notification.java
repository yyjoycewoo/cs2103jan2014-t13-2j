/**
 * 
 */
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

//@author A0101324A
/**
 * This class creates a pop up notification to appear on the desktop.
 * 
 * @source from
 *         http://www.javacodegeeks.com/2012/10/create-new-message-notification
 *         -pop-up.html
 */

public abstract class Notification extends Popup implements ActionListener {
	private static int count = 0, width = 200;

	/**
	 * @param taskToDo
	 */
	protected static void popUpNotice() {
		String msg = "~~~Reminder! Do now or never!~~~";
		// pop up disappeared after 3sec
		final int waitfor = 3000;
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
		buttonsAction(textLabel);

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
				// ensure that the pop up wont disppear when mouse over
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
						Rectangle frameRect = new Rectangle(frame
								.getLocationOnScreen());
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
		}.start();
	}

	/**
	 * @param textLabel
	 * @return
	 */
	protected static void buttonsAction(final JLabel textLabel) {
		// allow the next task to be shown after clicking the button
		ActionListener actionListener_prev = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// get the prev item in myownlist
				if (count == 0) {
					count = Popup.myownlist.getSize() - 1;
					// allow text to be within the JLabel border
					String text = String.format(
							"<html><div WIDTH=%d>%s</div><html>", width,
							Popup.myownlist.getListItem(count).toString());
					textLabel.setText(text);

				} else {
					count--;
					// allow text to be within the JLabel border
					String text = String.format(
							"<html><div WIDTH=%d>%s</div><html>", width,
							Popup.myownlist.getListItem(count).toString());
					textLabel.setText(text);
				}
			}
		};
		// allow the next task to be shown after clicking the button
		ActionListener actionListener_next = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// get the next item in myownlist
				if (count == (Popup.myownlist.getSize())) {
					// allow text to be within the JLabel border
					String text = String.format(
							"<html><div WIDTH=%d>%s</div><html>", width,
							Popup.myownlist.getListItem(count - 1).toString());
					textLabel.setText(text);
					count = 0;
				}
				// allow text to be within the JLabel border
				String text = String.format(
						"<html><div WIDTH=%d>%s</div><html>", width,
						Popup.myownlist.getListItem(count).toString());
				textLabel.setText(text);
				count++;
			}
		};
		// implementing buttons on the label
		textLabel.setLayout(new BorderLayout());
		JButton next = new BasicArrowButton(BasicArrowButton.EAST);
		JButton prev = new BasicArrowButton(BasicArrowButton.WEST);
		prev.addActionListener(actionListener_prev);
		next.addActionListener(actionListener_next);
		textLabel.add(prev, BorderLayout.WEST);
		textLabel.add(next, BorderLayout.EAST);
		textLabel.setText(Popup.myownlist.getListItem(0).toString());
		// Centralizing the text on the JLabel
		textLabel.setHorizontalAlignment(JLabel.CENTER);
		count = 1;
	}
}
