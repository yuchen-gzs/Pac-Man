import javax.swing.*;
import java.awt.*;

public class PacManGame extends JFrame {
    private GamePanel gamePanel;
    
    public PacManGame() {
        setTitle("屿宸网络科技工作室·吃豆人");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        gamePanel = new GamePanel();
        add(gamePanel);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        gamePanel.startGame();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PacManGame();
        });
    }
}