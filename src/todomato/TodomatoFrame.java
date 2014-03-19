package todomato;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

/**
 * This class is the JFrame for the GUI of the application.
 * @author Joyce
 *
 */
@SuppressWarnings("serial")
public class TodomatoFrame extends JFrame implements ActionListener {
	private static final String INVALID_INPUT_MSG = "Invalid input: ";

	private JPanel p = new JPanel();

	private JTextField txtCommand = new JTextField(20);
	private JList<TaskDT> listTasks = new JList<TaskDT>(
			loadTasks(Processor.getList()));
	private JLabel lblStatus = new JLabel(" ");

	/**
	 * This constructor creates a JFrame for the application Todomato.
	 */
	public TodomatoFrame() {
		// create and set up the window
		super("Todomato");
		// setSize only to have window display as centered; does not actually
		// set the size
		setSize(600, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		initDisplay();
		add(p);

		// display the window
		pack();
		setVisible(true);
	}

	private void initDisplay() {
		p.setLayout(new MigLayout("nocache"));

		p.add(new JScrollPane(listTasks), "wrap, push, grow");
		p.add(txtCommand, "wrap, pushx, growx");
		p.add(lblStatus);

		txtCommand.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String status = SplitProcessorsHandler
							.processCommand(txtCommand.getText());
					assert status != null;
					
					listTasks.setListData(loadTasks(Processor.getList()));
					txtCommand.setText("");
					lblStatus.setText(status);

				} catch (InvalidInputException e1) {
					txtCommand.setText("");
					lblStatus.setText(INVALID_INPUT_MSG + e1.getMessage());
				}
			}
		});
	}

	private TaskDT[] loadTasks(TaskDTList l) {
		TaskDT[] list = { new TaskDT("You currently have no tasks.") };
		if (l.getSize() == 0) {
			return list;
		} else {
			list = new TaskDT[l.getSize()];
			for (int i = 0; i < l.getSize(); i++) {
				list[i] = l.getListItem(i);
			}
		}
		return list;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
