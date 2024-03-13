import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Game of Life");
        setSize(Config.WIDTH, Config.HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new WelcomePanel(this));
        setVisible(true);
    }

    public void startLifePanel() {
        getContentPane().removeAll();
        getContentPane().add(new CorePanel());
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        new MainWindow();
    }

}