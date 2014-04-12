package todomato;

import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import org.jfree.ui.BevelArrowIcon;

import net.miginfocom.swing.MigLayout;

/**
 * This class is the JFrame for the GUI of the application.
 * @author Joyce
 *
 */
@SuppressWarnings("serial")
public class TodomatoFrame extends JFrame implements ActionListener {
	private static final String EMPTY_TEXT = "";
	private static final String REDO_COMMAND = "redo";
	private static final String FIND_COMMAND = "find ";
	private static final String DELETE_COMMAND = "delete ";
	private static final String HELP_COMMAND = "help";
	private static final String UNDO_COMMAND = "undo";
	private static final String SORT_PRIORITY_DESC_COMMAND = "sort priority desc";
	private static final String SORT_PRIORITY_ASC_COMMAND = "sort priority asc";
	private static final String SORT_COMPLETE_DESC_COMMAND = "sort complete desc";
	private static final String SORT_COMPLETE_ASC_COMMAND = "sort complete asc";
	private static final String SORT_DATE_DESC_COMMAND = "sort enddate desc";
	private static final String SORT_DATE_ASC_COMMAND = "sort enddate asc";

	private final String UNDO = "undo action key";
	private final String REDO = "redo action key";
	private final String FIND = "search action key";
	private final String DELETE = "delete action key";
	private final String HELP = "help action key";

	protected static final int INDEX_OFFSET = 1;

	private static final String INVALID_INPUT_MSG = "Invalid input: ";
	private static final String SORT_PRIORITY = "Sort by priority";
	private static final String SORT_COMPLETED = "Sort by completed";
	private static final String SORT_DATE = "Sort by date";

	private static Icon downArrow = new BevelArrowIcon(1, false, true);
	private static Icon upArrow = new BevelArrowIcon(0, false, true);

	private TodomatoTable table = new TodomatoTable();      
	private JPanel panel = new JPanel();
	private JTextField txtCommand = new JTextField(20);
	private JLabel lblStatus = new JLabel(" ");
	private JButton btnPriority = new JButton(SORT_PRIORITY, upArrow);
	private JButton btnCompleted = new JButton(SORT_COMPLETED, upArrow);
	private JButton btnStartDate = new JButton(SORT_DATE, upArrow);


	public TodomatoFrame() {
		super("Todomato");
		setSize(680,500);
		setMinimumSize(new Dimension(400, 200));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initDisplay();
		initShortcuts();

		add(panel);
		//pack();
		setVisible(true);
	}


	private void initDisplay() {
		panel.setLayout(new MigLayout("nocache"));
		panel.add(table.getTableDisplay(), "wrap, push, grow");
		panel.add(btnStartDate, "split");
		panel.add(btnPriority);
		panel.add(btnCompleted, "wrap");
		panel.add(txtCommand, "wrap, pushx, growx");
		panel.add(lblStatus);

		updateData(EMPTY_TEXT);

		initTxtCommandAction(); 
		initBtnStartDateAction();
		initBtnPriorityAction();   
		initBtnCompletedAction();  
	}


	private void initBtnStartDateAction() {
		btnStartDate.addActionListener(new ActionListener() {                         
			public void actionPerformed(ActionEvent e)
			{
				String status;
				try {
					if (btnStartDate.getIcon() == upArrow) {
						status = SplitProcessorsHandler.processCommand(SORT_DATE_ASC_COMMAND);
						btnStartDate.setIcon(downArrow);
					} else {
						status = SplitProcessorsHandler.processCommand(SORT_DATE_DESC_COMMAND);
						btnStartDate.setIcon(upArrow);
					}
					assert status != null;
					table.update();
				} catch (InvalidInputException e1) {
					System.out.println(INVALID_INPUT_MSG + e1.getMessage());
				}       
			}
		});
	}


	private void initBtnCompletedAction() {
		btnCompleted.addActionListener(new ActionListener() {                    
			public void actionPerformed(ActionEvent e)
			{
				String status;
				try {
					if (btnCompleted.getIcon() == upArrow) {
						status = SplitProcessorsHandler.processCommand(SORT_COMPLETE_ASC_COMMAND);
						btnCompleted.setIcon(downArrow);
					} else {
						status = SplitProcessorsHandler.processCommand(SORT_COMPLETE_DESC_COMMAND);
						btnCompleted.setIcon(upArrow);
					}
					assert status != null;
					table.update();
				} catch (InvalidInputException e1) {
					System.out.println(INVALID_INPUT_MSG + e1.getMessage());
				}       
			}
		});
	}


	private void initBtnPriorityAction() {
		btnPriority.addActionListener(new ActionListener() {                     
			public void actionPerformed(ActionEvent e)
			{
				String status = null;
				try {
					if (btnPriority.getIcon() == upArrow) {
						status = SplitProcessorsHandler.processCommand(SORT_PRIORITY_ASC_COMMAND);
						btnPriority.setIcon(downArrow);
					} else {
						status = SplitProcessorsHandler.processCommand(SORT_PRIORITY_DESC_COMMAND);
						btnPriority.setIcon(upArrow);                                           
					}
					assert status != null;
					table.update();                                 
				} catch (InvalidInputException e1) {
					System.out.println(INVALID_INPUT_MSG + e1.getMessage());
				}       
			}
		});
	}


	private void initTxtCommandAction() {
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

			table.update();

			txtCommand.setText(EMPTY_TEXT);
			lblStatus.setText(status);
		} catch (InvalidInputException e) {
			txtCommand.setText(EMPTY_TEXT);
			lblStatus.setText(INVALID_INPUT_MSG + e.getMessage());
		}
	}

	private void initShortcuts() {          


		Action undoAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				updateData(UNDO_COMMAND);
			}
		};

		Action redoAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				updateData(REDO_COMMAND);
			}
		};

		Action searchAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				updateData(FIND_COMMAND + txtCommand.getText());
			}
		};

		Action deleteAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int rowIndex = table.rowSelected + INDEX_OFFSET;
				updateData(DELETE_COMMAND + rowIndex);
			}
		};

		Action helpAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				updateData(HELP_COMMAND);
			}
		};

		panel.getActionMap().put(UNDO, undoAction);
		panel.getActionMap().put(REDO, redoAction);
		panel.getActionMap().put(FIND, searchAction);
		panel.getActionMap().put(DELETE, deleteAction);
		panel.getActionMap().put(HELP, helpAction);

		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Z"), UNDO);
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Y"), REDO);
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F2"), FIND);
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), HELP);
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DELETE"), DELETE);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}