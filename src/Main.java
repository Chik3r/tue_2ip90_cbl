import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Runnable guiCreator = new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
//                frame.setLayout(null);
                frame.setSize(1024, 768);
                frame.setResizable(false);

                new JPanel(true);
                CustomPanel cPanel = new CustomPanel();
                frame.add(cPanel);

                frame.setVisible(true);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                while (true)
                cPanel.paintImmediately(0, 0, 1024, 768);
//                cPanel.repaint();
            }
        };

        SwingUtilities.invokeLater(guiCreator);
    }
}

class CustomPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawString("Hello world!", 20, 20);
        double v = Math.sin(System.currentTimeMillis() / 1000.0) + 1;
        g.fillRect(0, 0, (int) (v * 100), (int) (v * 100));

        // System.out.println("Drawn");
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }
}