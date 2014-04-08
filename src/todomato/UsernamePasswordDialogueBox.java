package todomato;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;


public class UsernamePasswordDialogueBox extends JFrame{
	private JPanel panel = new JPanel();
	private JLabel lblUsername = new JLabel("Username: ");
	private JLabel lblPassword = new JLabel("Pasword: ");
	private JTextField txtUsername = new JTextField(20);
	private JTextField txtPassword = new JTextField(20);
	private JButton button = new JButton("Submit");
	private String username;
	private String password;
	
	public UsernamePasswordDialogueBox() {
		super("Log in");
		setSize(680,500);
		setLocationRelativeTo(null);

		panel.setLayout(new MigLayout("nocache"));
		panel.add(lblUsername);
		panel.add(txtUsername, "wrap, pushx, growx");
		panel.add(lblPassword);
		panel.add(txtPassword, "wrap, growx");
		panel.add(button, "span, align center");
		
		initButtonAction();
		
		add(panel);
		pack();
		setVisible(true);
	}
	
	private void initButtonAction() {
		button.addActionListener(new ActionListener() {			 
            public void actionPerformed(ActionEvent e)
            {
            	setUsername(txtUsername.getText());
            	setPassword(txtPassword.getText());
            	
    			Processor.getList().setUserNameAndPassword(getUsername(), getPassword());
            	setVisible(false);
            }
        });
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}