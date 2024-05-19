import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ImagePanel extends JFrame {
    private Image backgroundImage;
    private ImageIcon[] animalIcons;
    private String[] animalNames = {
        "Cheetah-running.png", "deer.png", "elephant.png",
        "giraffe.png", "Moose.png", "zebra.png"
    };

    public ImagePanel() {
        // Set up the frame
        setTitle("The wild");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load the background image from the default package
        backgroundImage = new ImageIcon(getClass().getResource("/backgroundWild.jpg")).getImage();

        // Load animal images from the default package
        loadAnimalImages();

        // Create a blank panel with a background image
        JPanel blankPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        blankPanel.setLayout(null); // Use null layout for absolute positioning

        // Add the blank panel to the frame
        add(blankPanel);

        // Add a component listener to ensure the panel size is set before adding images
        blankPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                blankPanel.removeComponentListener(this); // Remove listener after use
                addRandomImages(blankPanel);
            }
        });
    }

    // Method to load animal images
    private void loadAnimalImages() {
        animalIcons = new ImageIcon[animalNames.length];
        for (int i = 0; i < animalNames.length; i++) {
            animalIcons[i] = new ImageIcon(getClass().getResource("/" + animalNames[i]));
        }
    }

    // Method to add random number of clickable images to the panel
    private void addRandomImages(JPanel panel) {
        Random random = new Random();
        int numberOfImages = random.nextInt(6) + 5; // Random number between 1 and 10

        for (int i = 0; i < numberOfImages; i++) {
            String title = "BlockchainCode " + (i + 1); // Unique title for each image
            ImageIcon icon = animalIcons[random.nextInt(animalIcons.length)];

            // Ensure that the panel width and height are greater than the image dimensions
            int panelWidth = panel.getWidth();
            int panelHeight = panel.getHeight();

            int x = Math.max(0, random.nextInt(panelWidth - icon.getIconWidth()));
            int y = Math.max(0, random.nextInt(panelHeight - icon.getIconHeight()));

            ClickableImage image = new ClickableImage(icon, title);
            image.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
            panel.add(image);

            // Create a timer to move the image
            Timer timer = new Timer(1000, new ActionListener() { // Set to 100 milliseconds (0.1 seconds)
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveImage(image, panel);
                }
            });
            timer.start();
        }

        panel.revalidate();
        panel.repaint();
    }

    // Method to move the image by 1 pixel in a random direction
    private void moveImage(JLabel image, JPanel panel) {
        Random random = new Random();
        int direction = random.nextInt(4); // 0 = up, 1 = down, 2 = left, 3 = right

        int x = image.getX();
        int y = image.getY();
        int panelWidth = panel.getWidth();
        int panelHeight = panel.getHeight();

        switch (direction) {
            case 0: // move up
                if (y > 0) {
                    y -= 20;
                }
                break;
            case 1: // move down
                if (y < panelHeight - image.getHeight()) {
                    y += 20;
                }
                break;
            case 2: // move left
                if (x > 0) {
                    x -= 20;
                }
                break;
            case 3: // move right
                if (x < panelWidth - image.getWidth()) {
                    x += 20;
                }
                break;
        }

        image.setLocation(x, y);
    }

    // The ClickableImage class that triggers the BlockchainCodeGUI
    private class ClickableImage extends JLabel implements MouseListener {
        private String title;

        public ClickableImage(ImageIcon icon, String title) {
            super(icon);
            this.title = title;
            this.addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // Show the BlockchainCodeGUI with a unique title
            BlockchainCodeGUI blockchainCodeGUI = new BlockchainCodeGUI();
            blockchainCodeGUI.showGUI(title);
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ImagePanel().setVisible(true);
            }
        });
    }
}
