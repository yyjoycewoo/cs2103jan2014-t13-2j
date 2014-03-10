package todomato;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class TodomatoFrame  {
	private JFrame window = new JFrame("Todomato");
	private JPanel p = new JPanel();
	
	private JButton btnCommand = new JButton("Submit");
	private JTextField txtCommand = new JTextField(20);
	private JList<Task> listTasks = new JList<Task>(loadTasks(Processor.getList()));
	
	public TodomatoFrame() {
	}
	
	private void createAndShowGUI() {
        //Create and set up the window.
		p.setLayout(new MigLayout());
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    
		p.add(new JScrollPane(listTasks), "wrap, push, grow");
	    p.add(txtCommand, "split2, pushx, growx");
	    p.add(btnCommand);
	    
	    window.add(p);

        //Display the window.
	    window.pack();
	    window.setVisible(true);
		
	}
	
	public void display() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
            public void run() {
                createAndShowGUI();
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
}
