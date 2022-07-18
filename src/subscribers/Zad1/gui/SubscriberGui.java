package subscribers.Zad1.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.*;

import javax.swing.*;

import subscribers.Zad1.*;

public class SubscriberGui {
	public static void main(String args[]) {
		JFrame frame = new JFrame("Subscriber Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		InetSocketAddress serverAddress = new InetSocketAddress("localhost", 50001);
		SubscriberClient client = new SubscriberClient("john", serverAddress);

		JMenuBar mb = new JMenuBar();
		JMenu m1 = new JMenu("Options");
		mb.add(m1);
		JMenuItem m11 = new JMenuItem("Subscribe");
		JMenuItem m22 = new JMenuItem("View info");
		m1.add(m11);
		m1.add(m22);
		JPanel panel = new JPanel();
		JTextArea resultText = new JTextArea();
		resultText.setEditable(false);
		// subscribe tab
		m11.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
				ArrayList<Topic> topics = new ArrayList<Topic>();

				try {
					topics = new ArrayList<Topic>(client.fetchAllTopics());
				} catch (ClosedChannelException e1) {
					resultText.setText("no server availble");
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					resultText.setText("no data do fetch");
					e1.printStackTrace();
				}
				JList<Topic> jList = new JList<Topic>(topics.toArray(new Topic[topics.size()]));
				JScrollPane scrollPane = new JScrollPane(jList);
				JButton subButton = new JButton("subscribe");

				JTextArea userTA = new JTextArea("username");
				
				subButton.setToolTipText("click to subscribe topics");
				
				subButton.addMouseListener(new MouseListener() {

					@Override
					public void mouseClicked(MouseEvent e) {
						client.setUserName(userTA.getText());
						Set<Topic> selected = new TreeSet<Topic>(jList.getSelectedValuesList());
						try {
							String res = client.subscribeTopics(selected).getStatus().toString();
							resultText.setText(res);
							
							scrollPane.revalidate();
							scrollPane.repaint();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					@Override
					public void mousePressed(MouseEvent e) {

					}

					@Override
					public void mouseReleased(MouseEvent e) {

					}

					@Override
					public void mouseEntered(MouseEvent e) {

					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub

					}

				});
				JButton unsubButt = new JButton("unsubscribe");
				unsubButt.addMouseListener(new MouseListener() {

					@Override
					public void mouseClicked(MouseEvent e) {
						client.setUserName(userTA.getText());
						try {
							resultText.setText(client.unsubscribeMe().getStatus().toString());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					@Override
					public void mousePressed(MouseEvent e) {}

					@Override
					public void mouseReleased(MouseEvent e) {}

					@Override
					public void mouseEntered(MouseEvent e) {}

					@Override
					public void mouseExited(MouseEvent e) {}
					
				});
				JPanel rightPanel = new JPanel();
				panel.removeAll();
				rightPanel.add(subButton);
				rightPanel.add(unsubButt);
				rightPanel.add(userTA);

				rightPanel.setLayout(new GridLayout(3, 0));
				panel.add(BorderLayout.CENTER, scrollPane);
				panel.add(BorderLayout.EAST, rightPanel);
				frame.revalidate();
				frame.repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});
		// view info tab
		m22.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				resultText.setText("");
				JTextArea infoArea = new JTextArea();
				infoArea.setEditable(false);
				JScrollPane scrollPane = new JScrollPane(infoArea);
				JButton infoButton = new JButton("refresh");
				JPanel rightPanel = new JPanel();
				JTextArea userTA = new JTextArea("username");
				infoButton.setToolTipText("click to subscribe topics");

				infoButton.addMouseListener(new MouseListener() {

					@Override
					public void mouseClicked(MouseEvent e) {

						try {
							client.setUserName(userTA.getText());
							CliReceiver rec = client.fetchNews();
							switch (rec.getStatus()) {
							case OK:
								Set<Information> infos = rec.justInformations();
								infos.forEach((i) -> infoArea.append(i.formatedDate() + "\n" + i.getText() + "\n"));
								scrollPane.revalidate();
								frame.revalidate();
								break;
							default:
								resultText.setText(rec.getStatus().toString());

							}

						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}

					@Override
					public void mousePressed(MouseEvent e) {
					}

					@Override
					public void mouseReleased(MouseEvent e) {
					}

					@Override
					public void mouseEntered(MouseEvent e) {
					}

					@Override
					public void mouseExited(MouseEvent e) {
					}

				});

				rightPanel.add(infoButton);
				rightPanel.add(userTA);
				rightPanel.setLayout(new GridLayout(2, 0));
				panel.removeAll();
				panel.add(BorderLayout.CENTER, scrollPane);
				panel.add(BorderLayout.EAST, rightPanel);
				frame.revalidate();
				
				frame.repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});

		// Text Area at the Center

		// Adding Components to the frame.

		frame.getContentPane().add(BorderLayout.NORTH, mb);
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.getContentPane().add(BorderLayout.SOUTH, resultText);
		frame.setVisible(true);

	}
	// public
}
