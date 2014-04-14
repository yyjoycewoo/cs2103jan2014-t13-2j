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
 */

public abstract class Notification implements ActionListener {
	private static int count = 0;
	private static int width = 200;
	private static int fwidth = 300;
	private static int fheight = 125;
	private static int inset_num = 5;
	private static int grid_xy = 0;
	private static int textWidth = 300;
	private static int textHeight = 100;
	private static float lweight_xy = 0f;
	private static float fweight_xy = 1.0f;
	private static String htmlCode = "<html><div WIDTH=%d>%s</div><html>";
	private static String msg = "~~~Reminder! Do now or never!~~~";
	private static String jFrame_name = "Reminder";
	private static String labelHTML = "<HtMl>";
	// pop up disappeared after 2sec
	private static int waitfor = 2000;
	private static int closeBt_inset_top_bot = 1;
	private static int closeBt_inset_left_right = 4;

	/**
	 * @param myownlist
	 * @param taskToDo
	 */
	protected static void popUpNotice(TaskList list) {
		// create and set up the window
		final JFrame frame = new JFrame(jFrame_name);
		frame.setSize(fwidth, fheight);
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
		constraints.gridx = grid_xy;
		constraints.gridy = grid_xy;
		constraints.weightx = fweight_xy;
		constraints.weighty = fweight_xy;
		constraints.insets = new Insets(inset_num, inset_num, inset_num,
				inset_num);
		constraints.fill = GridBagConstraints.BOTH;

		final JLabel textLabel = new JLabel();
		buttonsAction(textLabel, list);

		textLabel.setPreferredSize(new Dimension(textWidth, textHeight));
		textLabel.setOpaque(false);
		frame.getContentPane().add(textLabel, constraints);
		constraints.gridx++;
		constraints.weightx = lweight_xy;
		constraints.weighty = lweight_xy;
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

		closeBt.setMargin(new Insets(closeBt_inset_top_bot,
				closeBt_inset_left_right, closeBt_inset_top_bot,
				closeBt_inset_left_right));
		closeBt.setFocusable(false);
		frame.getContentPane().add(closeBt, constraints);
		constraints.gridx = grid_xy;
		constraints.gridy++;
		constraints.weightx = fweight_xy;
		constraints.weighty = fweight_xy;
		constraints.insets = new Insets(inset_num, inset_num, inset_num,
				inset_num);
		constraints.fill = GridBagConstraints.BOTH;

		JLabel msgLabel = new JLabel(labelHTML + msg);
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
					text = String.format(htmlCode, width,
							list.getListItem(count).toString());
				} else {
					count--;
					text = String.format(htmlCode, width,
							list.getListItem(count).toString());
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
					text = String.format(htmlCode, width,
							list.getListItem(count).toString());

				} else {
					count++;
					// allow text to be within the JLabel border
					text = String.format(htmlCode, width,
							list.getListItem(count).toString());
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
		String text = String.format(htmlCode, width, list.getListItem(0)
				.toString());
		textLabel.setText(text);
		// Centralizing the text on the JLabel
		textLabel.setHorizontalAlignment(JLabel.CENTER);
	}
}
