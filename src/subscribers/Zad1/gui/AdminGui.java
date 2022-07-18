package subscribers.Zad1.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.*;

import subscribers.Zad1.*;

public class AdminGui {

	private static InetSocketAddress serverAddress = new InetSocketAddress("localhost", 50001);

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setBounds(300, 250, 800, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar mb = new JMenuBar();
		JMenu m1 = new JMenu("Options");
		mb.add(m1);
		JMenuItem m11 = new JMenuItem("Manage Topics");
		JMenuItem m22 = new JMenuItem("Serve Info");
		m1.add(m11);
		m1.add(m22);
		JPanel panel = new JPanel();

		JPanel resultPane = new JPanel();
		JTextArea resultText = new JTextArea();
		resultText.setEditable(false);
		JTextArea unameArea = new JTextArea("username");
		resultPane.add(BorderLayout.WEST, resultText);
		resultPane.add(BorderLayout.EAST, unameArea);
		m11.addMouseListener(new MouseListener() {
			AdminClient client = new AdminClient(unameArea.getText(), serverAddress);

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				panel.removeAll();
				JTextArea userInput = new JTextArea("new topic");
				panel.add(BorderLayout.SOUTH, userInput);
				JButton newButton = new JButton("new topic");
				JButton delButton = new JButton("del topic");
				JPanel rightPanel = new JPanel();
				rightPanel.removeAll();
				rightPanel.add(newButton);
				rightPanel.add(delButton);
				rightPanel.setLayout(new GridBagLayout());
				panel.add(rightPanel);
				JList topicList = new JList();

				JScrollPane scrollPane = new JScrollPane(topicList);

				panel.add(BorderLayout.CENTER, scrollPane);
				panel.revalidate();
				frame.repaint();
				try {
					Set<Topic> topics = client.fetchAllTopics();
					topicList.setListData(topics.toArray(new Topic[topics.size()]));

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
				delButton.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mousePressed(MouseEvent e) {
						try {
							CliReceiver rec = client.delTopic((Topic) topicList.getSelectedValuesList().get(0));
							resultText.setText(rec.getStatus().toString());
							resultText.revalidate();
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub

					}
				});
				newButton.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mousePressed(MouseEvent e) {
						Topic tp = new Topic(userInput.getText());
						try {
							CliReceiver rec = client.newTopic(tp);
							resultText.setText(rec.getStatus().toString());
							resultText.revalidate();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub

					}
				});
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		m22.addMouseListener(new MouseListener() {
			AdminClient client = new AdminClient(unameArea.getText(), serverAddress);
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				panel.removeAll();
				JButton sendInfo = new JButton("send new info");
				JTextArea inputArea = new JTextArea("recent news");
				inputArea.setBounds(0, 0, 80, 150);
				JList topicList = new JList();
				JScrollPane scrollPanel = new JScrollPane(topicList);
				panel.add(inputArea);
				panel.add(scrollPanel);
				panel.add(sendInfo);
				panel.revalidate();
				Set<Topic> topics;
				try {
					topics = client.fetchAllTopics();
					topicList.setListData(topics.toArray(new Topic[topics.size()]));
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
				sendInfo.addMouseListener(new MouseListener() {
					
					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mousePressed(MouseEvent e) {
						TreeSet topics = new TreeSet<Topic>(topicList.getSelectedValuesList());
						
						Information info = new Information(inputArea.getText(), topics);
						try {
							CliReceiver rec = client.newInfo(info);
							resultText.setText(rec.getStatus().toString());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					
					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
				panel.revalidate();
				frame.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.add(BorderLayout.NORTH, mb);
		frame.add(BorderLayout.SOUTH, resultPane);
		frame.add(BorderLayout.CENTER, panel);
		frame.revalidate();
	}

}
