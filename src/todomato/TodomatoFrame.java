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
	private static final String SORT_PRIORITY_DESC_COMMAND = "sort priority desc";
	private static final String SORT_PRIORITY_ASC_COMMAND = "sort priority asc";
	private static final String SORT_COMPLETE_DESC_COMMAND = "sort complete desc";
	private static final String SORT_COMPLETE_ASC_COMMAND = "sort complete asc";
	protected static final String SORT_DATE_DESC_COMMAND = "sort date desc";
	protected static final String SORT_DATE_ASC_COMMAND = "sort date asc";
	protected static final String SORT_DESC_DESC_COMMAND = "sort description desc";
	protected static final String SORT_DESC_ASC_COMMAND = "sort description asc";
	
	protected static final int INDEX_OFFSET = 1;
	
	private static final String INVALID_INPUT_MSG = "Invalid input: ";
	private static final String SORT_PRIORITY = "Sort by priority";
	private static final String SORT_COMPLETED = "Sort by completed";
	private static final String SORT_DATE = "Sort by date";
	private static final String SORT_DESC = "Sort by description";

	private static Icon downArrow = new BevelArrowIcon(1, false, true);
	private static Icon upArrow = new BevelArrowIcon(0, false, true);
	
	private TodomatoTable table = new TodomatoTable();	
	private JPanel panel = new JPanel();
	private JTextField txtCommand = new JTextField(20);
	private JLabel lblStatus = new JLabel(" ");
	private JButton btnPriority = new JButton(SORT_PRIORITY, upArrow);
	private JButton btnCompleted = new JButton(SORT_COMPLETED, upArrow);
	private JButton btnDate = new JButton(SORT_DATE, upArrow);
	private JButton btnDesc = new JButton(SORT_DESC, upArrow);


	public TodomatoFrame() {
		super("Todomato");
		super.setDefaultLookAndFeelDecorated(true);
		setSize(600,500);
		setMinimumSize(new Dimension(370, 200));
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
		panel.add(btnDesc, "split");
		panel.add(btnDate);
		panel.add(btnPriority);
		//panel.add(btnPriority, "split");
		panel.add(btnCompleted, "wrap");
		panel.add(txtCommand, "wrap, pushx, growx");
		panel.add(lblStatus);

		updateData("");

		initTxtCommandAction(); 
		initBtnDateAction();
		initBtnDescAction();
		initBtnPriorityAction();   
		initBtnCompletedAction();  
	}


	private void initBtnDescAction() {
		btnDesc.addActionListener(new ActionListener() {			 
            public void actionPerformed(ActionEvent e)
            {
				String status;
				try {
					if (btnDesc.getIcon() == upArrow) {
						status = SplitProcessorsHandler.processCommand(SORT_DESC_ASC_COMMAND);
						btnDesc.setIcon(downArrow);
					} else {
						status = SplitProcessorsHandler.processCommand(SORT_DESC_DESC_COMMAND);
						btnDesc.setIcon(upArrow);
					}
					assert status != null;
					table.update();
				} catch (InvalidInputException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
            }
        });
	}


	private void initBtnDateAction() {
		btnDate.addActionListener(new ActionListener() {			 
            public void actionPerformed(ActionEvent e)
            {
				String status;
				try {
					if (btnDate.getIcon() == upArrow) {
						status = SplitProcessorsHandler.processCommand(SORT_DATE_ASC_COMMAND);
						btnDate.setIcon(downArrow);
					} else {
						status = SplitProcessorsHandler.processCommand(SORT_DATE_DESC_COMMAND);
						btnDate.setIcon(upArrow);
					}
					assert status != null;
					table.update();
				} catch (InvalidInputException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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
					// TODO Auto-generated catch block
					e1.printStackTrace();
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
					// TODO Auto-generated catch block
					e1.printStackTrace();
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
			
			txtCommand.setText("");
			lblStatus.setText(status);
		} catch (InvalidInputException e) {
			txtCommand.setText("");
			lblStatus.setText(INVALID_INPUT_MSG + e.getMessage());
		}
	}

	private void initShortcuts() {		
		String UNDO = "undo action key";
		String REDO = "redo action key";
		String FIND = "search action key";
		String DELETE = "delete action key";

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
				updateData("find " + txtCommand.getText());
			}
		};
		
		Action deleteAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int rowIndex = table.rowSelected + INDEX_OFFSET;
				updateData("delete " + rowIndex);
			}
		};

		panel.getActionMap().put(UNDO, undoAction);
		panel.getActionMap().put(REDO, redoAction);
		panel.getActionMap().put(FIND, searchAction);
		panel.getActionMap().put(DELETE, deleteAction);

		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Z"), UNDO);
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Y"), REDO);
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F2"), FIND);
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DELETE"), DELETE);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
