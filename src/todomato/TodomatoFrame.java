package todomato;

import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

/**
 * This class is the JFrame for the GUI of the application.
 * @author Joyce
 *
 */
@SuppressWarnings("serial")
public class TodomatoFrame extends JFrame implements ActionListener {
	private static final String INVALID_INPUT_MSG = "Invalid input: ";
	
	private TodomatoTable table = new TodomatoTable();
	
	private JPanel panel = new JPanel();
	private JTextField txtCommand = new JTextField(20);
	private JLabel lblStatus = new JLabel(" ");


	public TodomatoFrame() {
		super("Todomato");
		super.setDefaultLookAndFeelDecorated(true);
		setSize(700,500);
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

		panel.getActionMap().put(UNDO, undoAction);
		panel.getActionMap().put(REDO, redoAction);
		panel.getActionMap().put(FIND, searchAction);

		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Z"), UNDO);
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Y"), REDO);
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F2"), FIND);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
