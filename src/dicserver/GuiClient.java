package dicserver;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetSocketAddress;

import javax.swing.*;

import dicserver.client.Client;
import dicserver.protocol.*;

public class GuiClient {
	public static void main(String[] args) {
		Client client = new Client(new InetSocketAddress("localhost", 51153));
		JFrame frame = new JFrame();
		JButton button = new JButton("click");
		button.setBounds(130, 100, 100, 40);
		JTextField textField = new JTextField();
		JTextArea resultField = new JTextArea();
		JTextField langCodeField = new JTextField();
		resultField.setEditable(false);
		resultField.setLineWrap(true);
		frame.add(button);
		frame.add(textField);
		frame.add(langCodeField);
		frame.add(resultField);
		textField.setToolTipText("translated phrase");
		langCodeField.setToolTipText("language code to transalte to for ex. en");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String phrase = textField.getText();
				String langCode = langCodeField.getText();

				if (phrase.isEmpty() || langCode.isEmpty())
					resultField.setText("missing inputs");
				else if (!Query.isLangCode(langCode))
					resultField.setText("Language Code have to be 2 letters long");
				else
					try {
						String fetched = client.get(phrase, langCode);
						resultField.setText(fetched);
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (QueryException e1) {
						e1.printStackTrace();
					}
			}
		});
		frame.setBounds(400, 100, 500, 100);
		frame.setTitle("Dictionary server PJWSTK, s18543");
		frame.setLayout(new GridLayout());
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
