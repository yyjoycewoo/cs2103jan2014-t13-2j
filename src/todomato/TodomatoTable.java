package todomato;

import hirondelle.date4j.DateTime;

import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

//@author A0120766H
/**
 * This class consists of a table that is designed to display a user's
 * TaskList. It color codes based on priority and completion status,
 * and sets the width of each column.
 *
 */
public class TodomatoTable extends JTable {     
	private static final String DATE_LOCALE = "US";
	private static final String DATE_FORMAT = "MMM DD YYYY";
	private static final String EMPTY_TASK_LIST_DESC = "(Empty)";
	private static final String EMPTY_TASK_LIST_INDEX = "0";
	//constants for column names
	private static final String INDEX_HEADER = "Index";
	private static final String DESC_HEADER = "Description";
	private static final String STARTTIME_HEADER = "Start Time";
	private static final String ENDTIME_HEADER = "End Time";
	private static final String STARTDATE_HEADER = "Start Date";
	private static final String ENDDATE_HEADER = "End Date";
	private static final String LOCATION_HEADER = "Location";
	private static final String RECUR_HEADER = "Recur";

	//constants for preferred column widths
	private static final int INDEX_COLUNM_WIDTH = 10;
	private static final int DESC_COLUNM_WIDTH = 185;
	private static final int STARTTIME_COLUNM_WIDTH = 60;
	private static final int ENDTIME_COLUNM_WIDTH = 60;
	private static final int STARTDATE_COLUNM_WIDTH = 70;
	private static final int ENDDATE_COLUNM_WIDTH = 70;
	private static final int LOCATION_COLUNM_WIDTH = 90;
	private static final int RECUR_COLUNM_WIDTH = 10;

	//constants for minimum column widths
	private static final int INDEX_MIN_WIDTH = 10;
	private static final int DESC_MIN_WIDTH = 50;
	private static final int STARTTIME_MIN_WIDTH = 50;
	private static final int ENDTIME_MIN_WIDTH = 50;
	private static final int STARTDATE_MIN_WIDTH = 60;
	private static final int ENDDATE_MIN_WIDTH = 60;
	private static final int LOCATION_MIN_WIDTH = 50;
	private static final int RECUR_MIN_WIDTH = 10;

	//constants for column indices
	private static final int INDEX_COLUNM_INDEX = 0;
	private static final int DESC_COLUNM_INDEX = 1;
	private static final int STARTTIME_COLUNM_INDEX = 2;
	private static final int ENDTIME_COLUNM_INDEX = 3;
	private static final int STARTDATE_COLUNM_INDEX = 4;
	private static final int ENDDATE_COLUNM_INDEX = 5;
	private static final int LOCATION_COLUNM_INDEX = 6;
	private static final int RECUR_COLUNM_INDEX = 7;

	//constants for background colour for the rows
	private static final Object PRIORITY_HIGH = "HIGH";
	private static final Object PRIORITY_MEDIUM = "MEDIUM";

	private static final int NUM_COLS = 5;

	private JTable table;
	private JScrollPane tableDisplay;
	static Object[][] data;
	int rowSelected;
	TaskList list = Processor.getDisplayList();

	/**
	 * Create a new TodomatoTable.
	 */
	public TodomatoTable() {
		String[] columnNames = {INDEX_HEADER, DESC_HEADER,
				STARTTIME_HEADER, ENDTIME_HEADER, STARTDATE_HEADER,
				ENDDATE_HEADER, LOCATION_HEADER, RECUR_HEADER};

		data = loadData(Processor.getDisplayList());

		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			public Class getColumnClass(int column) {
				Object value = this.getValueAt(0,column); 
				return (value==null?Object.class:value.getClass());
				//return getValueAt(0, column).getClass();
			}

		};

		table = createData(model);
		addListListener();
		setColumnWidths();
		tableDisplay = new JScrollPane(table);
	}

	private JTable createData(DefaultTableModel model) {
		JTable table = new JTable(model) {
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);

				//  Color row based on priority and completed
				if (!isRowSelected(row)) {
					c.setBackground(getBackground());
					int modelRow = convertRowIndexToModel(row);

					int index = Integer.parseInt( (String) getModel().getValueAt(modelRow, INDEX_COLUNM_INDEX));
					colorRows(c, index);
				}
				return c;
			}

			private void colorRows(Component c, int index) {
				//if index is within bounds of the list
				if (index != 0 && index <= list.getSize()) {
					Task item = list.getListItem(index - 1);
					String priority = item.getPriorityLevel();
					
					if (PRIORITY_HIGH.equals(priority)) {
						c.setBackground(Color.RED);
					}
					
					if (PRIORITY_MEDIUM.equals(priority)) {
						c.setBackground(Color.YELLOW);
					}

					if (item.getCompleted()) {
						c.setBackground(Color.GRAY);                                                
					}
				}
			}
		};

		table.setAutoCreateRowSorter(true);
		return table;
	}

	private void addListListener() {
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						rowSelected = table.getSelectedRow();
					}
				}               
				);
	}

	private void setColumnWidths() {
		boolean canAutoCreateColumnsFromModel = false;
		table.getColumnModel().getColumn(INDEX_COLUNM_INDEX).setPreferredWidth(INDEX_COLUNM_WIDTH);
		table.getColumnModel().getColumn(DESC_COLUNM_INDEX).setPreferredWidth(DESC_COLUNM_WIDTH);               
		table.getColumnModel().getColumn(STARTTIME_COLUNM_INDEX).setPreferredWidth(STARTTIME_COLUNM_WIDTH);
		table.getColumnModel().getColumn(ENDTIME_COLUNM_INDEX).setPreferredWidth(ENDTIME_COLUNM_WIDTH);
		table.getColumnModel().getColumn(STARTDATE_COLUNM_INDEX).setPreferredWidth(STARTDATE_COLUNM_WIDTH);
		table.getColumnModel().getColumn(ENDDATE_COLUNM_INDEX).setPreferredWidth(ENDDATE_COLUNM_WIDTH);
		table.getColumnModel().getColumn(LOCATION_COLUNM_INDEX).setPreferredWidth(LOCATION_COLUNM_WIDTH);
		table.getColumnModel().getColumn(RECUR_COLUNM_INDEX).setPreferredWidth(RECUR_COLUNM_WIDTH);

		table.getColumnModel().getColumn(INDEX_COLUNM_INDEX).setMinWidth(INDEX_MIN_WIDTH);
		table.getColumnModel().getColumn(DESC_COLUNM_INDEX).setMinWidth(DESC_MIN_WIDTH);                
		table.getColumnModel().getColumn(STARTTIME_COLUNM_INDEX).setMinWidth(STARTTIME_MIN_WIDTH);
		table.getColumnModel().getColumn(ENDTIME_COLUNM_INDEX).setMinWidth(ENDTIME_MIN_WIDTH);
		table.getColumnModel().getColumn(STARTDATE_COLUNM_INDEX).setMinWidth(STARTDATE_MIN_WIDTH);
		table.getColumnModel().getColumn(ENDDATE_COLUNM_INDEX).setPreferredWidth(ENDDATE_MIN_WIDTH);
		table.getColumnModel().getColumn(LOCATION_COLUNM_INDEX).setMinWidth(LOCATION_MIN_WIDTH);
		table.getColumnModel().getColumn(RECUR_COLUNM_INDEX).setPreferredWidth(RECUR_MIN_WIDTH);

		table.setAutoCreateColumnsFromModel(canAutoCreateColumnsFromModel);
	}

	private static Object[][] loadData(TaskList l) {        
		Object[][] list = new Object[1][8];
		if (l.getSize() == 0) {
			for (int i=0; i<=NUM_COLS; i++) {
				list[0][i] = "";
			}                               
			list[0][INDEX_COLUNM_INDEX] = EMPTY_TASK_LIST_INDEX;
			list[0][DESC_COLUNM_INDEX] = EMPTY_TASK_LIST_DESC;
		} else {
			list = new Object[l.getSize()][8];
			for (int i = 0; i < l.getSize(); i++) {
				list[i][INDEX_COLUNM_INDEX] = String.valueOf(i+1);
				list[i][DESC_COLUNM_INDEX] = checkNull(l.getListItem(i).getDescription());
				list[i][STARTTIME_COLUNM_INDEX] = checkNull(l.getListItem(i).getStartTime());
				list[i][ENDTIME_COLUNM_INDEX] = checkNull(l.getListItem(i).getEndTime());
				list[i][STARTDATE_COLUNM_INDEX] = checkNullDate(l.getListItem(i).getStartDate());  
				list[i][ENDDATE_COLUNM_INDEX] = checkNullDate(l.getListItem(i).getEndDate());                             
				list[i][LOCATION_COLUNM_INDEX] = checkNull(l.getListItem(i).getLocation());                           
				list[i][RECUR_COLUNM_INDEX] = checkNull(l.getListItem(i).getRecurrencePeriod());
			}
		}
		return list;
	}

	private static Object checkNullDate(DateTime date) {
		if (date == null) {
			return "";
		} else {
			return date.format(DATE_FORMAT, new Locale(DATE_LOCALE));
		}
	}

	private static Object checkNull(Object item) {
		if (item == null) {
			return "";
		} else {
			return item;
		}
	}

	public void update() {
		data = loadData(Processor.getDisplayList());
		table.setModel(new CustModel(data));
	}

	class CustModel extends AbstractTableModel {
		private String[] columnNames = {INDEX_HEADER, DESC_HEADER,
				STARTTIME_HEADER, ENDTIME_HEADER, STARTDATE_HEADER,
				ENDDATE_HEADER, LOCATION_HEADER, RECUR_HEADER};
		private Object[][] data = loadData(list);

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

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public static Object[][] getData() {
		return data;
	}

	public JScrollPane getTableDisplay() {
		return tableDisplay;
	}

	public void setTableDisplay(JScrollPane tableDisplay) {
		this.tableDisplay = tableDisplay;
	}
}