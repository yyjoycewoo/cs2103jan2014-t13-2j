package todomato;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

public class HelpProcessor {
	private static final String HELP_TEXT = 
			"1. Adding task: \n" +
			"\t add <desc> at <starttime> <startdate> in <location> \n\n" +
			"2. Updating task: \n" +
			"\t update <index> <field> <detail> \n" +
			"Note: \n" +
			"- Append \'\\\' to end of <detail> when updating description or location \n" +
			"- <detail> is not required when updating completion status \n\n" +
			"3. Deleting task \n" +
			"\t delete <index> \n\n" +
			"Refer to user guide for more details.";
	
	public static void processHelp() {
		JFrame frame= new JFrame("Help");
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("nocache"));
		JTextArea helpText = new JTextArea(HELP_TEXT);
		
		frame.add(panel);
		panel.add(helpText, "wrap, push, grow");
		frame.setSize(500,400);
		frame.setMinimumSize(new Dimension(400, 300));
		frame.setVisible(true);
	}
}
