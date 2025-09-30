import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {
    private final int BOARD_WIDTH = 600;
    private final int BOARD_HEIGHT = 600;
    private final int UNIT_SIZE = 25;
    private final int GAME_UNITS = (BOARD_WIDTH * BOARD_HEIGHT) / UNIT_SIZE;
    private final int DELAY = 100;
    
    private PacMan pacman;
    private ArrayList<Ghost> ghosts;
    private GameMap gameMap;
    private GameState gameState;
    private Timer timer;
    private boolean running = false;
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        
        gameMap = new GameMap(BOARD_WIDTH, BOARD_HEIGHT, UNIT_SIZE);
        gameState = new GameState();
        pacman = new PacMan(gameMap.getPacManStartX(), gameMap.getPacManStartY(), UNIT_SIZE);
        
        ghosts = new ArrayList<>();
        Point[] ghostPositions = gameMap.getGhostStartPositions();
        ghosts.add(new Ghost(ghostPositions[0].x, ghostPositions[0].y, UNIT_SIZE, Color.RED));
        ghosts.add(new Ghost(ghostPositions[1].x, ghostPositions[1].y, UNIT_SIZE, Color.PINK));
        ghosts.add(new Ghost(ghostPositions[2].x, ghostPositions[2].y, UNIT_SIZE, Color.CYAN));
        ghosts.add(new Ghost(ghostPositions[3].x, ghostPositions[3].y, UNIT_SIZE, Color.ORANGE));
    }
    
    public void startGame() {
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw(Graphics g) {
        if (running) {
            gameMap.draw(g);
            pacman.draw(g);
            
            for (Ghost ghost : ghosts) {
                ghost.draw(g);
            }
            
            // 显示游戏状态信息
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("分数: " + gameState.getScore(), 10, 20);
            g.drawString("生命: " + gameState.getLives(), 10, 40);
            g.drawString("关卡: " + gameState.getLevel(), 10, 60);
        } else {
            gameOver(g);
        }
    }
    
    public void move() {
        pacman.move();
        
        for (Ghost ghost : ghosts) {
            ghost.move(gameMap);
        }
    }
    
    public void checkFood() {
        if (gameMap.eatDot(pacman.getX(), pacman.getY())) {
            gameState.addDotScore();
        }
        
        if (gameMap.eatPowerPellet(pacman.getX(), pacman.getY())) {
            gameState.addPowerPelletScore();
            // 让幽灵变弱的逻辑可以在这里添加
        }
    }
    
    public void checkCollisions() {
        // 检查墙壁碰撞
        if (gameMap.isWall(pacman.getX(), pacman.getY())) {
            pacman.undoMove();
        }
        
        // 检查幽灵碰撞
        for (Ghost ghost : ghosts) {
            if (pacman.getX() == ghost.getX() && pacman.getY() == ghost.getY()) {
                gameState.loseLife();
                if (gameState.getLives() <= 0) {
                    running = false;
                    gameState.setGameOver(true);
                } else {
                    // 重置位置
                    resetPositions();
                }
            }
        }
        
        // 检查是否吃完所有豆子
        if (gameMap.allDotsEaten()) {
            gameState.nextLevel();
            gameMap.resetDots();
            resetPositions();
            JOptionPane.showMessageDialog(this, "恭喜过关！进入第 " + gameState.getLevel() + " 关");
        }
    }
    
    private void resetPositions() {
        pacman = new PacMan(gameMap.getPacManStartX(), gameMap.getPacManStartY(), UNIT_SIZE);
        
        ghosts.clear();
        Point[] ghostPositions = gameMap.getGhostStartPositions();
        ghosts.add(new Ghost(ghostPositions[0].x, ghostPositions[0].y, UNIT_SIZE, Color.RED));
        ghosts.add(new Ghost(ghostPositions[1].x, ghostPositions[1].y, UNIT_SIZE, Color.PINK));
        ghosts.add(new Ghost(ghostPositions[2].x, ghostPositions[2].y, UNIT_SIZE, Color.CYAN));
        ghosts.add(new Ghost(ghostPositions[3].x, ghostPositions[3].y, UNIT_SIZE, Color.ORANGE));
    }
    
    public void gameOver(Graphics g) {
        // 分数
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        String scoreText = "分数: " + gameState.getScore();
        g.drawString(scoreText, (BOARD_WIDTH - metrics1.stringWidth(scoreText)) / 2, 
                    g.getFont().getSize());
        
        // 游戏结束文本
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("游戏结束", (BOARD_WIDTH - metrics2.stringWidth("游戏结束")) / 2, 
                    BOARD_HEIGHT / 2);
        
        // 重新开始提示
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("按空格键重新开始", (BOARD_WIDTH - metrics3.stringWidth("按空格键重新开始")) / 2, 
                    BOARD_HEIGHT / 2 + 50);
    }
    
    public void newGame() {
        gameState.resetGame();
        gameMap.resetDots();
        resetPositions();
        running = true;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }
    
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    pacman.setDirection('L');
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    pacman.setDirection('R');
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    pacman.setDirection('U');
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    pacman.setDirection('D');
                    break;
                case KeyEvent.VK_SPACE:
                    if (!running) {
                        newGame();
                    }
                    break;
            }
        }
    }
}