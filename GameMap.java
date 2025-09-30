import java.awt.*;

/**
 * 游戏地图类
 * 负责管理迷宫布局、豆子、大豆子和碰撞检测
 */
public class GameMap {
    private final int BOARD_WIDTH;
    private final int BOARD_HEIGHT;
    private final int UNIT_SIZE;
    private final int ROWS;
    private final int COLS;
    
    // 地图布局 (0=空地, 1=墙, 2=豆子, 3=大豆子)
    private int[][] map;
    private boolean[][] dots;
    private boolean[][] powerPellets;
    
    // 简化的地图布局
    private final int[][] MAZE_LAYOUT = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,2,2,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,2,2,1},
        {1,3,1,1,1,1,2,1,1,1,2,1,1,2,1,1,1,2,1,1,1,1,3,1},
        {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1,2,1,1,1,1,2,1,1,2,1,1,1,1,2,1,1,2,1,1,1,1,2,1},
        {1,2,2,2,2,2,2,1,1,2,2,2,2,2,2,1,1,2,2,2,2,2,2,1},
        {1,1,1,1,1,1,2,1,1,1,1,1,0,1,1,1,1,1,2,1,1,1,1,1},
        {0,0,0,0,0,1,2,1,1,0,0,0,0,0,0,0,1,1,2,1,0,0,0,0},
        {1,1,1,1,1,1,2,1,1,0,1,1,0,0,1,1,0,1,2,1,1,1,1,1},
        {0,0,0,0,0,0,2,0,0,0,1,0,0,0,0,1,0,0,2,0,0,0,0,0},
        {1,1,1,1,1,1,2,1,1,0,1,0,0,0,0,1,0,1,2,1,1,1,1,1},
        {0,0,0,0,0,1,2,1,1,0,1,1,1,1,1,1,0,1,2,1,0,0,0,0},
        {1,1,1,1,1,1,2,1,1,0,0,0,0,0,0,0,0,1,2,1,1,1,1,1},
        {1,2,2,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,2,2,1},
        {1,2,1,1,1,1,2,1,1,1,2,1,1,2,1,1,1,2,1,1,1,1,2,1},
        {1,3,2,2,1,1,2,2,2,2,2,2,2,2,2,2,2,2,1,1,2,2,3,1},
        {1,1,1,2,1,1,2,1,1,2,1,1,1,1,2,1,1,2,1,1,2,1,1,1},
        {1,2,2,2,2,2,2,1,1,2,2,2,2,2,2,1,1,2,2,2,2,2,2,1},
        {1,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,1},
        {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };
    
    public GameMap(int boardWidth, int boardHeight, int unitSize) {
        this.BOARD_WIDTH = boardWidth;
        this.BOARD_HEIGHT = boardHeight;
        this.UNIT_SIZE = unitSize;
        this.ROWS = MAZE_LAYOUT.length;
        this.COLS = MAZE_LAYOUT[0].length;
        
        initializeMap();
    }
    
    private void initializeMap() {
        map = new int[ROWS][COLS];
        dots = new boolean[ROWS][COLS];
        powerPellets = new boolean[ROWS][COLS];
        
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                map[row][col] = MAZE_LAYOUT[row][col];
                dots[row][col] = (MAZE_LAYOUT[row][col] == 2);
                powerPellets[row][col] = (MAZE_LAYOUT[row][col] == 3);
            }
        }
    }
    
    public void draw(Graphics g) {
        // 绘制墙壁
        g.setColor(Color.BLUE);
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (map[row][col] == 1) {
                    g.fillRect(col * UNIT_SIZE, row * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                }
            }
        }
        
        // 绘制豆子
        g.setColor(Color.YELLOW);
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (dots[row][col]) {
                    int x = col * UNIT_SIZE + UNIT_SIZE / 2;
                    int y = row * UNIT_SIZE + UNIT_SIZE / 2;
                    g.fillOval(x - 2, y - 2, 4, 4);
                }
            }
        }
        
        // 绘制大豆子
        g.setColor(Color.WHITE);
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (powerPellets[row][col]) {
                    int x = col * UNIT_SIZE + UNIT_SIZE / 2;
                    int y = row * UNIT_SIZE + UNIT_SIZE / 2;
                    g.fillOval(x - 6, y - 6, 12, 12);
                }
            }
        }
    }
    
    public boolean isWall(int x, int y) {
        int col = x / UNIT_SIZE;
        int row = y / UNIT_SIZE;
        
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return true;
        }
        
        return map[row][col] == 1;
    }
    
    public boolean eatDot(int x, int y) {
        int col = x / UNIT_SIZE;
        int row = y / UNIT_SIZE;
        
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS && dots[row][col]) {
            dots[row][col] = false;
            return true;
        }
        return false;
    }
    
    public boolean eatPowerPellet(int x, int y) {
        int col = x / UNIT_SIZE;
        int row = y / UNIT_SIZE;
        
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS && powerPellets[row][col]) {
            powerPellets[row][col] = false;
            return true;
        }
        return false;
    }
    
    public boolean allDotsEaten() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (dots[row][col] || powerPellets[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void resetDots() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                dots[row][col] = (MAZE_LAYOUT[row][col] == 2);
                powerPellets[row][col] = (MAZE_LAYOUT[row][col] == 3);
            }
        }
    }
    
    // 获取吃豆人起始位置
    public int getPacManStartX() {
        return 12 * UNIT_SIZE;
    }
    
    public int getPacManStartY() {
        return 18 * UNIT_SIZE;
    }
    
    // 获取幽灵起始位置
    public Point[] getGhostStartPositions() {
        return new Point[] {
            new Point(12 * UNIT_SIZE, 9 * UNIT_SIZE),
            new Point(11 * UNIT_SIZE, 9 * UNIT_SIZE),
            new Point(13 * UNIT_SIZE, 9 * UNIT_SIZE),
            new Point(12 * UNIT_SIZE, 8 * UNIT_SIZE)
        };
    }
    
    public int getUnitSize() {
        return UNIT_SIZE;
    }
    
    public int getRows() {
        return ROWS;
    }
    
    public int getCols() {
        return COLS;
    }
}