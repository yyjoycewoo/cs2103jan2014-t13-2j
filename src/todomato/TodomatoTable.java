package todomato;

import hirondelle.date4j.DateTime;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

public class TodomatoTable extends JPanel {
	private static final int PRIORITY_COLUNM_INDEX = 6;
	private JTable table;
	private JScrollPane tableDisplay;
	static Object[][] data;
	
	public TodomatoTable() {
		String[] columnNames = {"Index", "Description", "Start Time", " End Time", "Date", "Location", "Priority", "Completed"};
		data = loadData(Processor.getDisplayList());

		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			public Class getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}
		};

		table = createData(model);
		tableDisplay = new JScrollPane(table);
	}


	private JTable createData(DefaultTableModel model) {
		JTable table = new JTable(model) {
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);

				//  Color row based on a cell value
				if (!isRowSelected(row)) {
					c.setBackground(getBackground());
					int modelRow = convertRowIndexToModel(row);
					String type = (String)getModel().getValueAt(modelRow, PRIORITY_COLUNM_INDEX);
					if ("HIGH".equals(type)) {
						c.setBackground(Color.RED);
					}
					if ("MEDIUM".equals(type)) {
						c.setBackground(Color.YELLOW);
					}
				}
				return c;
			}
		};

		//table.changeSelection(0, 0, false, false);
        //table.setAutoCreateRowSorter(true);
		return table;
	}
	
	private static Object[][] loadData(TaskDTList l) {        
		Object[][] list = new Object[1][8];
		if (l.getSize() == 0) {
			for (int i=0; i<=7; i++) {
				list[0][i] = "";
			}				
			list[0][0] = "0";
			list[0][1] = "(Empty)";
		} else {
			list = new Object[l.getSize()][8];
			for (int i = 0; i < l.getSize(); i++) {
				list[i][0] = String.valueOf(i+1);
				list[i][1] = checkNull(l.getListItem(i).getDescription());
				list[i][2] = checkNull(l.getListItem(i).getStartTime());
				list[i][3] = checkNull(l.getListItem(i).getEndTime());
				list[i][4] = checkNullDate(l.getListItem(i).getDate());				
				list[i][5] = checkNull(l.getListItem(i).getLocation());
				list[i][6] = checkNull(l.getListItem(i).getPriorityLevel());
				if (l.getListItem(i).getCompleted()) {
					list[i][7] = "Y";
				} else {
					list[i][7] = "N";
				}
			}
		}
		return list;
	}
	
	private static Object checkNullDate(DateTime date) {
		if (date == null) {
			return "";
		} else {
			return date.format("DD-MM-YYYY");
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
		table.setAutoCreateRowSorter(true);
	}
	
	class CustModel extends AbstractTableModel {
		private String[] columnNames = {"Index", "Description", "Start Time", " End Time", "Date", "Location", "Priority", "Completed"};
		private Object[][] data = loadData(Processor.getDisplayList());

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