import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
    public class WelcomePanel extends JPanel implements KeyListener {

        private MainWindow mainWindow;


        public WelcomePanel(MainWindow mainWindow) {
            this.mainWindow = mainWindow;
            setBackground(Color.BLACK);
            setLayout(new BorderLayout());

            // Descriptive variable names
            JLabel startLabel = new JLabel("Press 'S' to start");
            JLabel pauseLabel = new JLabel("Press 'P' to pause/exit");
            JLabel clearLabel = new JLabel("Press 'R' to clear"); // New label for clear

            // Set foreground color and alignment
            startLabel.setForeground(Color.WHITE);
            startLabel.setHorizontalAlignment(SwingConstants.CENTER);
            startLabel.setVerticalAlignment(SwingConstants.CENTER);
            pauseLabel.setForeground(Color.WHITE);
            pauseLabel.setHorizontalAlignment(SwingConstants.CENTER);
            pauseLabel.setVerticalAlignment(SwingConstants.CENTER);
            clearLabel.setForeground(Color.WHITE); // Set color for clear label
            clearLabel.setHorizontalAlignment(SwingConstants.CENTER);
            clearLabel.setVerticalAlignment(SwingConstants.CENTER); // Position below startLabel

            // Add labels to the panel
            add(startLabel, BorderLayout.CENTER);
            add(pauseLabel, BorderLayout.WEST);
            add(clearLabel, BorderLayout.EAST); // Add clear label to the east

            setFocusable(true);
            addKeyListener(this);
        }


        @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
            mainWindow.startLifePanel();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
