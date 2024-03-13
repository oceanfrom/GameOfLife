import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CorePanel extends JPanel implements ActionListener, MouseMotionListener, MouseListener {

    int[][] life;
    int[][] nextLife;
    boolean checked = false;
    boolean startGame = true;
    JFrame dialog;
    Timer timer;
    private long startTime;


    public CorePanel() {
        setBackground(Color.BLACK);
        setLayout(null);

        addMouseListener(this);
        addMouseMotionListener(this);

        timer = new Timer(30, this);
        timer.start();
        startTime = System.currentTimeMillis();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                startGame = true;
                repaint();
            }
        });

        setupKeyBindings();
    }

    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pause");
        actionMap.put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer.isRunning()) {
                    timer.stop();
                    showDialog();
                }
            }
        });


        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "clear");
        actionMap.put("clear", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                showConfirmClearDialog();
            }
        });
    }

    private void showConfirmClearDialog() {
        dialog = new JFrame("Clear Board");
        dialog.setLayout(new BorderLayout());
        JLabel label = new JLabel("Do you want to clear the board?");
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        dialog.add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton yesButton = new JButton("Yes");
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearBoard();
                dialog.dispose();
            }
        });
        buttonPanel.add(yesButton);

        JButton noButton = new JButton("No");
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        buttonPanel.add(noButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void clearBoard() {
        for (int x = 0; x < life.length; x++) {
            for (int y = 0; y < life[0].length; y++) {
                nextLife[x][y] = 0;
            }
        }
        repaint();
    }

    private void showDialog() {
        dialog = new JFrame("Continue Game");
        dialog.setLayout(new BorderLayout());
        JLabel label = new JLabel("Do you want to continue?");
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        dialog.add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton yesButton = new JButton("Yes");
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                timer.start();
            }
        });
        buttonPanel.add(yesButton);

        JButton noButton = new JButton("No");
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                JOptionPane.showMessageDialog(null, "You've been playing this game for " + calculateElapsedTime() + "\nSee you soon!");
                System.exit(0);
            }
        });
        buttonPanel.add(noButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private String calculateElapsedTime() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        long seconds = elapsedTime / 1000;
        long minutes = seconds / 60;
        return minutes + " min " + (seconds % 60) + " sec";
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        grid(g);
        fillingCells();
        coloringOfFieldCells(g);
    }

    private void grid(Graphics g) {
        g.setColor(Color.GRAY);
        int rows = getHeight() / Config.SIZE;
        int cols = getWidth() / Config.SIZE;

        for (int i = 0; i <= rows; i++) {
            g.drawLine(0, i * Config.SIZE, getWidth(), i * Config.SIZE);
        }

        for (int i = 0; i <= cols; i++) {
            g.drawLine(i * Config.SIZE, 0, i * Config.SIZE, getHeight());
        }

        if (startGame) {
            life = new int[cols][rows];
            nextLife = new int[cols][rows];
        }
    }

    private void fillingCells() {
        if (startGame) {
            for (int x = 0; x < life.length; x++) {
                for (int y = 0; y < life[0].length; y++) {
                    if ((int) (Math.random() * 5) == 0) {
                        nextLife[x][y] = 1;
                    } else {
                        nextLife[x][y] = 0;
                    }
                }
            }
            startGame = false;
        }
    }

    private void coloringOfFieldCells(Graphics g) {
        copyArray();
        for (int x = 0; x < life.length; x++) {
            for (int y = 0; y < life[0].length; y++) {
                if (life[x][y] == 1) {
                    float hue = (float) (System.currentTimeMillis() % 10000) / 10000;
                    float saturation = 0.9f;
                    float brightness = 0.9f;
                    Color color = Color.getHSBColor(hue, saturation, brightness);
                    g.setColor(color);
                    g.fillRect(x * Config.SIZE, y * Config.SIZE, Config.SIZE, Config.SIZE);
                }
            }
        }
    }


    private void copyArray() {
        for (int x = 0; x < life.length; x++) {
            System.arraycopy(nextLife[x], 0, life[x], 0, life[0].length);
        }
    }


    private int nearAlive(int x, int y) {
        int alive = 0;
        int xWidth = life.length;
        int yHeight = life[0].length;

        int[][] offsets = {
                {-1, -1}, {0, -1}, {1, -1},
                {-1, 0},           {1, 0},
                {-1, 1},  {0, 1},  {1, 1}
        };

        for (int[] offset : offsets) {
            int nx = (x + offset[0] + xWidth) % xWidth;
            int ny = (y + offset[1] + yHeight) % yHeight;
            alive += life[nx][ny];
        }

        return alive;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        int alive;
        for (int x = 0; x < life.length; x++) {
            for (int y = 0; y < life[0].length; y++) {
                alive = nearAlive(x, y);
                if (alive == 3) {
                    nextLife[x][y] = 1;
                } else if (alive != 2) {
                    nextLife[x][y] = 0;

                }
            }
            repaint();
        }
    }




    public void mouseDragged(MouseEvent e){
        int x = e.getX()/Config.SIZE;
        int y = e.getY()/Config.SIZE;

        if(life[x][y] == 0 && checked){
            nextLife[x][y] =1;}
        else if(life[x][y] == 1 && !checked){
            nextLife[x][y] =0;}

        repaint();
    }

    public void mouseMoved(MouseEvent e){

    }

    public void mouseClicked(MouseEvent e){

    }

    public void mousePressed(MouseEvent e){
        timer.stop();
        int x = e.getX()/Config.SIZE;
        int y = e.getY()/Config.SIZE;
        checked = true;

        if(life[x][y] == 0){
            nextLife[x][y] =1;}
        else if(life[x][y] == 1){
            nextLife[x][y] =0;}

    }

    public void mouseReleased(MouseEvent e){
        checked =false;
        timer.start();
    }

    public void mouseEntered(MouseEvent e){

    }

    public void mouseExited(MouseEvent e){

    }


}
