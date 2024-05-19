import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class BlockchainCodeGUI {
    private static ArrayList<String> textList = new ArrayList<>();
    private static JPanel textPanel;
    private static JTextArea resultTextArea;

    public void showGUI(String title) {
        // Create a JFrame
        JFrame frame = new JFrame(title);

        // Create a panel to hold components
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        // Create an input field
        JTextField inputField = new JTextField(10);

        // Create an "Add" button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputText = inputField.getText();
                if (isValidInput(inputText)) {
                    textList.add(inputText);
                    inputField.setText("");
                    updateTextPanel();
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter three items separated by ';'");
                }
            }
        });

        // Create a "Show" button
        JButton showButton = new JButton("Show");
        showButton.addActionListener(new ActionListener() {
            boolean isHidden = true;

            public void actionPerformed(ActionEvent e) {
                if (isHidden) {
                    textPanel.setVisible(true);
                    showButton.setText("Hide");
                } else {
                    textPanel.setVisible(false);
                    showButton.setText("Show");
                }
                isHidden = !isHidden;
            }
        });

        // Create a "Hash Check" button
        JButton hashCheckButton = new JButton("Hash Check");
        hashCheckButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkHashes();
            }
        });

        // Create a JPanel to display the added texts in three columns
        textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(0, 3));

        // Add labels to the top of each column
        textPanel.add(new JLabel("Name"));
        textPanel.add(new JLabel("Action"));
        textPanel.add(new JLabel("Times seen"));

        GridBagConstraints gbc = new GridBagConstraints();

        // Add components to the panel using GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(inputField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(addButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(showButton, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(hashCheckButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(textPanel, gbc);

        // Create a JTextArea to display the result
        resultTextArea = new JTextArea(10, 30);
        resultTextArea.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(new JScrollPane(resultTextArea), gbc);

        // Add the panel to the frame
        frame.add(panel);

        // Set frame properties
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400); // Increased height to accommodate the result text area
        frame.setVisible(true);
    }

    private static void updateTextPanel() {
        textPanel.removeAll();
        textPanel.add(new JLabel("Name"));
        textPanel.add(new JLabel("Action"));
        textPanel.add(new JLabel("Times seen"));

        for (String text : textList) {
            String[] tokens = text.split(";");
            if (tokens.length == 3) {
                for (String token : tokens) {
                    JTextField textField = new JTextField(10);
                    textField.setEditable(false);
                    textField.setText(token);
                    textPanel.add(textField);
                }
            }
        }
        textPanel.revalidate();
        textPanel.repaint();
    }

    private static boolean isValidInput(String inputText) {
        String[] tokens = inputText.split(";");
        return tokens.length == 3;
    }

    private static void checkHashes() {
        for (String text : textList) {
            String[] tokens = text.split(";");
            if (tokens.length == 3) {
                BlockData data = new BlockData(tokens[0], tokens[1], tokens[2]);
                try {
                    String result = BlockchainCode.checkHash(data);
                    resultTextArea.append(result + "\n");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
