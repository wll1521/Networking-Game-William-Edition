package netGame;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.LineBorder;
import netGame.GamePanel;
public class Main {
    public static void main(String[] args) {
        // Create the main frame or 'window'
        JFrame frame = new JFrame("Split Frame Example");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create 2 panels
        JPanel gamePanel = new GamePanel();
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBorder(new LineBorder(Color.BLACK));

        // Creates chat components
        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false); // Player can't edit
        chatArea.setLineWrap(true);  // Wraps text to fit the area
        chatArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        JTextField chatInput = new JTextField();

        chatInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = chatInput.getText();
                if (!message.isEmpty()) {
                    chatArea.append("You: " + message + "\n");
                    chatInput.setText("");
                    gamePanel.requestFocusInWindow();  //Refocuses back to game after input
                }
            }
        });

        // Adds components to chatPanel
        chatPanel.add(scrollPane, BorderLayout.CENTER);
        chatPanel.add(chatInput, BorderLayout.SOUTH);

        // Size the panels and add to window
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(chatPanel, BorderLayout.EAST);
        int gameWidth = (int)(frame.getWidth()*0.7);
        int chatWidth = (int)(frame.getWidth()*0.3);
        gamePanel.setPreferredSize(new Dimension(gameWidth, frame.getHeight()));
        chatPanel.setPreferredSize(new Dimension(chatWidth, frame.getHeight()));
        frame.revalidate();

        chatPanel.setBackground(Color.decode("#47FD8D"));

        frame.pack();
        frame.revalidate();
        frame.repaint();

        // Auto-resizing to maintain the 70/30 split
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newFrameWidth = frame.getWidth();
                int newFrameHeight = frame.getHeight();

                // Update bounds to maintain 70/30 split
                gamePanel.setBounds(0, 0, (int) (newFrameWidth * 0.7), newFrameHeight);
                chatPanel.setBounds((int) (newFrameWidth * 0.7), 0, (int) (newFrameWidth * 0.3), newFrameHeight);
            }
        });
        frame.setVisible(true);
    }
}
