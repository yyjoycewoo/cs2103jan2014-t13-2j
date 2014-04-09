package todomato;

import hirondelle.date4j.DateTime;

import java.awt.Dimension;
import java.awt.event.*;
import java.util.Locale;
import java.util.TimeZone;

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
	private static final String SORT_DATE_DESC_COMMAND = "sort date desc";
	private static final String SORT_DATE_ASC_COMMAND = "sort date asc";
	
	protected static final int INDEX_OFFSET = 1;
	private static final int numDaysInAWeek = 7;
	
	private static final String INVALID_INPUT_MSG = "Invalid input: ";
	private static final String SORT_PRIORITY = "Sort by priority";
	private static final String SORT_COMPLETED = "Sort by completed";
	private static final String SORT_DATE = "Sort by date";

	private static Icon downArrow = new BevelArrowIcon(1, false, true);
	private static Icon upArrow = new BevelArrowIcon(0, false, true);
	private static Icon leftArrow = new ArrowIcon(SwingConstants.LEFT);
	private static Icon rightArrow = new ArrowIcon(SwingConstants.RIGHT);
	
	private TodomatoTable table = new TodomatoTable();	
	private JPanel panel = new JPanel();
	private JTextField txtCommand = new JTextField(20);
	private JLabel lblStatus = new JLabel(" ");
	private JButton btnPriority = new JButton(SORT_PRIORITY, upArrow);
	private JButton btnCompleted = new JButton(SORT_COMPLETED, upArrow);
	private JButton btnDate = new JButton(SORT_DATE, upArrow);
	private JButton btnLeft = new JButton(leftArrow);
	private JButton btnRight = new JButton(rightArrow);
	private JButton btnToday = new JButton("Today");
	private JButton btnViewPeriod = new JButton("All");
	JLabel lblDate = new JLabel();
	
	private DateTime currDate = DateTime.now(TimeZone.getDefault());
	private DateTime viewDate;
	private DateTime startDate;
	private DateTime endDate;
	private String currDaysViewed;
	private int currWeekday;
	
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
		panel.add(btnLeft, "split, center");
		panel.add(lblDate);
		panel.add(btnRight, "wrap");
		
		panel.add(table.getTableDisplay(), "wrap, push, grow");
		
		panel.add(btnDate, "split");
		panel.add(btnPriority);
		panel.add(btnCompleted);
		panel.add(btnToday, "gapleft push");
		panel.add(btnViewPeriod, "wrap");
		
		panel.add(txtCommand, "wrap, pushx, growx");
		panel.add(lblStatus);

		initWeeklyView();
		
		initTxtCommandAction(); 
		initBtnDateAction();
		initBtnPriorityAction();   
		initBtnCompletedAction();
		initViewPeriodAction();

		updateData("");
	}


	private void initViewPeriodAction() {
		btnViewPeriod.addActionListener(new ActionListener() {			 
            public void actionPerformed(ActionEvent e) {
            	if (btnViewPeriod.getText().equals("All")) {
            		btnViewPeriod.setText("By week");
            		lblDate.setText("Displaying all tasks");
            		btnLeft.setVisible(false);
            		btnRight.setVisible(false);

            		DisplayProcessor.displayBetweenDates(null, null);
            		table.update();
            		
            	} else if (btnViewPeriod.getText().equals("By week")) {
            		btnViewPeriod.setText("By day");
            		viewDate = currDate;
            		lblDate.setText(getViewWeek());  
            		btnLeft.setVisible(true);
            		btnRight.setVisible(true);      
            		
            	} else if (btnViewPeriod.getText().equals("By day")) {
            		btnViewPeriod.setText("All");
            		viewDate = currDate;
            		lblDate.setText(getViewDay());  
            		btnLeft.setVisible(true);
            		btnRight.setVisible(true);          
            	}
            }
        });
	}

	private void initWeeklyView() {
		viewDate = currDate;
		lblDate.setText(getViewWeek());

		initBtnRightAction();
		initBtnLeftAction();
		initBtnTodayAction();
	}


	private void initBtnTodayAction() {
		btnToday.addActionListener(new ActionListener() {			 
            public void actionPerformed(ActionEvent e) {
        		lblDate.setText(currDate.format("MMM DD, YYYY", new Locale("US")));
        		btnLeft.setVisible(false);
        		btnRight.setVisible(false);
        		
            	viewDate = currDate;
        		getViewDay();
            }
        });
	}

	protected String getViewDay() {		
		DisplayProcessor.displayBetweenDates(viewDate, viewDate);
		table.update();
		
		currDaysViewed = viewDate.format("MMM DD, YYYY", new Locale("US"));
		
		return currDaysViewed;
	}


	private String getViewWeek() {		
		currWeekday = viewDate.getWeekDay();
		startDate = viewDate.minusDays(numDaysInAWeek - currWeekday);
		endDate = viewDate.plusDays(currWeekday - INDEX_OFFSET);
		currDaysViewed = startDate.format("MMM DD, YYYY", new Locale("US"));
		currDaysViewed += " - ";
		currDaysViewed += endDate.format("MMM DD, YYYY", new Locale("US"));
		
		DisplayProcessor.displayBetweenDates(startDate, endDate);
		table.update();
		
		return currDaysViewed;
	}

	private void initBtnRightAction() {
		btnRight.addActionListener(new ActionListener() {			 
            public void actionPerformed(ActionEvent e) {
            	//currently viewing by day
            	if (btnViewPeriod.getText().equals("All")) {
                	viewDate = viewDate.plusDays(1);
            		lblDate.setText(getViewDay());   
            	} else if (btnViewPeriod.getText().equals("By day")) {
            		//currently viewing by week
	            	viewDate = viewDate.plusDays(numDaysInAWeek);
	        		lblDate.setText(getViewWeek());
            	}
            }
        });
	}

	private void initBtnLeftAction() {
		btnLeft.addActionListener(new ActionListener() {			 
            public void actionPerformed(ActionEvent e) {
            	//currently viewing by day
            	if (btnViewPeriod.getText().equals("All")) {
                	viewDate = viewDate.minusDays(1);
            		lblDate.setText(getViewDay());            	
            	} else if (btnViewPeriod.getText().equals("By day")) {
            		//currently viewing by week
	            	viewDate = viewDate.minusDays(numDaysInAWeek);
	        		lblDate.setText(getViewWeek());
            	}
            }
        });
	}

	private void initBtnDateAction() {
		btnDate.addActionListener(new ActionListener() {			 
            public void actionPerformed(ActionEvent e) {
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
            public void actionPerformed(ActionEvent e) {
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
            public void actionPerformed(ActionEvent e) {
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
			
			getViewWeek();
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
