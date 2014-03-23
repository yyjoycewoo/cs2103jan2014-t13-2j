package todomato;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;

/**
 * This class is the JFrame for the GUI of the application.
 * @author Joyce
 *
 */
@SuppressWarnings("serial")
public class TodomatoFrame extends JFrame implements ActionListener {
	private static final String INVALID_INPUT_MSG = "Invalid input: ";

	private static String[] columnNames = {"Index", "Description", "Start Time", " End Time", "Date", "Location", "Priority"};
	private static Object[][] data = loadData(Processor.getList());
	static JTable table = new JTable(data, columnNames);
	JScrollPane tableDisplay = new JScrollPane(table);
	private JPanel panel = new JPanel();
	private JTextField txtCommand = new JTextField(20);
	private JLabel lblStatus = new JLabel(" ");
	//private JList<TaskDT> listTasks = new JList<TaskDT>(loadTasks(Processor.getList()));


	public TodomatoFrame() {
		super("Todomato");
		setSize(600,480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initDisplay();
		initShortcuts();
		add(panel);
		pack();
		setVisible(true);
	}


	private void initShortcuts() {
		String UNDO = "undo action key";
		String REDO = "redo action key";
		String FIND = "search action key";

		Action undoAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				updateData("undo");
			}
		};

		Action redoAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				updateData("redo");
			}
		};
		
		Action searchAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				
				updateData("find" + txtCommand.getText());
				System.out.println(txtCommand.getText());
			}
		};


		panel.getActionMap().put(UNDO, undoAction);
		panel.getActionMap().put(REDO, redoAction);
		panel.getActionMap().put(FIND, searchAction);

		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Z"), UNDO);
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Y"), REDO);
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F2"), FIND);

	}


	class CustModel extends AbstractTableModel {
		private String[] columnNames = {"Index", "Description", "Start Time", " End Time", "Date", "Location", "Priority"};
		private Object[][] data = loadData(Processor.getList());

		public CustModel(Object[][] data) {
			this.data = data;
		}
		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			if (getColumnCount() == 0 || getRowCount() == 0) {
				return null;
			}
			return data[row][col];
		}

	}

	private void initDisplay() {
		panel.setLayout(new MigLayout("nocache"));
		panel.add(tableDisplay, "wrap,push, grow");
		panel.add(txtCommand, "wrap, pushx, growx");
		panel.add(lblStatus);

		txtCommand.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateData(txtCommand.getText());
			}
		});
	}


	private void updateData(String command) {
		String status;
		try {
			status = SplitProcessorsHandler.processCommand(command);
			assert status != null;
			//listTasks.setListData(loadTasks(Processor.getList()));
			data = loadData(Processor.getDisplayList());
			table.setModel(new CustModel(data));
			table.setAutoCreateRowSorter(true);
			txtCommand.setText("");
			lblStatus.setText(status);
		} catch (InvalidInputException e) {
			txtCommand.setText("");
			lblStatus.setText(INVALID_INPUT_MSG + e.getMessage());
		}
	}


	private static Object[][] loadData(TaskDTList l) {
		Object[][] list = new Object[1][7];
		if (l.getSize() == 0) {
			list[0][0] = 0;
			list[0][1] = "(Empty)";
		} else {
			list = new Object[l.getSize()][7];
			for (int i = 0; i < l.getSize(); i++) {
				list[i][0] = i+1;
				list[i][1] = l.getListItem(i).getDescription();
				list[i][2] = l.getListItem(i).getStartTime();
				list[i][3] = l.getListItem(i).getEndTime();
				if (l.getListItem(i).getDate() != null) {
					list[i][4] = l.getListItem(i).getDate().format("DD-MM-YYYY");
				}
				list[i][5] = l.getListItem(i).getLocation();
				list[i][6] = l.getListItem(i).getPriorityLevel();
			}
		}
		return list;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
