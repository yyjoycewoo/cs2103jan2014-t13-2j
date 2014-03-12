package todomato;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class TodomatoFrame extends JFrame implements ActionListener {
	private static final String INVALID_INPUT_MSG = "Invalid input: ";

	private JPanel p = new JPanel();
	
	private JTextField txtCommand = new JTextField(20);
	private JList<Task> listTasks = new JList<Task>(loadTasks(Controller.getList()));
	private JLabel lblStatus = new JLabel(" ");
	
	public TodomatoFrame() {
		//create and set up the window
		super("Todomato");
		//setSize only to have window display as centered; does not actually set the size
		setSize(600,480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initDisplay();
		add(p);
        
		//display the window
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
					String status = CmdHandler.processCommand(txtCommand.getText());
					//SplitProcessorsHandler.processCommand(txtCommand.getText());
					listTasks.setListData(loadTasks(Controller.getList()));
					txtCommand.setText("");
					lblStatus.setText(status);
					
				} catch (InvalidInputException e1) {
					txtCommand.setText("");
					lblStatus.setText(INVALID_INPUT_MSG + e1.getMessage());				
				}	    		
	    	}
	    });
	}
	
	private Task[] loadTasks(TaskList l) {
		Task[] list = {new Task("You currently have no tasks.")};
		if (l.getSize() == 0) {
			return list;
		}
		else {
			list = new Task[l.getSize()];
			for (int i=0; i<l.getSize(); i++) {
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
