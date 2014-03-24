package todomato;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

	class ColorRenderer extends JLabel implements TableCellRenderer
	{
		public ColorRenderer()
		{
			setOpaque(true);
		}
		public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column)
		{

			if (value != null) setText(value.toString());
			if(isSelected)
			{
				setBackground(table.getSelectionBackground());
				setForeground(table.getSelectionForeground());
			}
			else
			{
				setBackground(table.getBackground());
				setForeground(table.getForeground());

				Object cellValue = table.getModel().getValueAt(row, column);

				if (cellValue != null && cellValue.toString().equals("HIGH")) {
					System.out.println(cellValue);
					setBackground(Color.RED);
				} else if (cellValue != null && cellValue.toString().equals("MEDIUM")) {
					System.out.println(cellValue);
					setBackground(Color.YELLOW);
				}

			}
			return this;
		}
	}