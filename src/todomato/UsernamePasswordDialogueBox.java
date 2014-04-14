package todomato;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

//@author A0120766H-unused
//depreciated with a CLI usable command
/**
 * This class generates a dialogue box to ask the
 * user for his/her username and password.
 */
public class UsernamePasswordDialogueBox extends JFrame{
	//constants for MigLayout formatting
	private static final String SUBMIT_BUTTON_LAYOUT = "span, align center";
	private static final String PASSWORD_TEXTBOX_LAYOUT = "wrap, growx";
	private static final String USERNAME_TEXTBOX_LAYOUT = "wrap, pushx, growx";
	private static final String MIG_LAYOUT = "nocache";
	
	private static final int GUI_HEIGHT = 500;
	private static final int GUI_WIDTH = 680;
	
	private static final String SUBMIT_BUTTON_TEXT = "Submit";
	private static final int PASSWORD_TEXTBOX_SIZE = 20;
	private static final int USERNAME_TEXTBOX_SIZE = 20;
	private static final String PASSWORD_LABEL = "Pasword: ";
	private static final String USERNAME_LABEL = "Username: ";
	
	private JPanel panel = new JPanel();
	private JLabel lblUsername = new JLabel(USERNAME_LABEL);
	private JLabel lblPassword = new JLabel(PASSWORD_LABEL);
	private JTextField txtUsername = new JTextField(USERNAME_TEXTBOX_SIZE);
	private JTextField txtPassword = new JTextField(PASSWORD_TEXTBOX_SIZE);
	private JButton button = new JButton(SUBMIT_BUTTON_TEXT);
	private String username;
	private String password;
	
	/**
	 * Create a new UsernamePasswordDialogueBox
	 */
	public UsernamePasswordDialogueBox() {
		super("Log in");
		setSize(GUI_WIDTH, GUI_HEIGHT);
		setLocationRelativeTo(null);

		panel.setLayout(new MigLayout(MIG_LAYOUT));
		panel.add(lblUsername);
		panel.add(txtUsername, USERNAME_TEXTBOX_LAYOUT);
		panel.add(lblPassword);
		panel.add(txtPassword, PASSWORD_TEXTBOX_LAYOUT);
		panel.add(button, SUBMIT_BUTTON_LAYOUT);
		
		initButtonAction();
		
		add(panel);
		pack();
		setVisible(true);
	}
	
	private void initButtonAction() {
		button.addActionListener(new ActionListener() {			 
            public void actionPerformed(ActionEvent e) {
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