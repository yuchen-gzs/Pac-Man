/**
 * 游戏状态管理类
 * 负责管理分数、生命值、关卡等游戏状态信息
 */
public class GameState {
    private int score;
    private int lives;
    private int level;
    private int highScore;
    private boolean gameOver;
    private boolean gameWon;
    
    // 游戏常量
    private static final int INITIAL_LIVES = 3;
    private static final int DOT_SCORE = 10;
    private static final int POWER_PELLET_SCORE = 50;
    private static final int GHOST_SCORE = 200;
    private static final int BONUS_LIFE_SCORE = 10000;
    
    public GameState() {
        initializeGame();
    }
    
    private void initializeGame() {
        score = 0;
        lives = INITIAL_LIVES;
        level = 1;
        gameOver = false;
        gameWon = false;
        // 这里可以从文件读取最高分
        highScore = 0;
    }
    
    public void addScore(int points) {
        int oldScore = score;
        score += points;
        
        // 检查是否获得额外生命
        if (oldScore < BONUS_LIFE_SCORE && score >= BONUS_LIFE_SCORE) {
            addLife();
        }
        
        // 更新最高分
        if (score > highScore) {
            highScore = score;
        }
    }
    
    public void addDotScore() {
        addScore(DOT_SCORE);
    }
    
    public void addPowerPelletScore() {
        addScore(POWER_PELLET_SCORE);
    }
    
    public void addGhostScore() {
        addScore(GHOST_SCORE);
    }
    
    public void addLife() {
        lives++;
    }
    
    public void loseLife() {
        lives--;
        if (lives <= 0) {
            gameOver = true;
        }
    }
    
    public void nextLevel() {
        level++;
        // 可以在这里增加难度，比如增加幽灵速度等
    }
    
    public void resetGame() {
        initializeGame();
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    public boolean isGameWon() {
        return gameWon;
    }
    
    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }
    
    public int getScore() {
        return score;
    }
    
    public int getLives() {
        return lives;
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getHighScore() {
        return highScore;
    }
    
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
    
    // 获取当前游戏状态的字符串表示
    public String getStatusString() {
        return String.format("分数: %d | 生命: %d | 关卡: %d | 最高分: %d", 
                           score, lives, level, highScore);
    }
    
    // 检查是否创造新纪录
    public boolean isNewHighScore() {
        return score == highScore && score > 0;
    }
    
    // 获取生命值显示字符串
    public String getLivesDisplay() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lives; i++) {
            sb.append("♥ ");
        }
        return sb.toString();
    }
    
    // 保存游戏状态到文件（可选功能）
    public void saveGameState() {
        // 这里可以实现保存游戏状态到文件的功能
        // 比如保存最高分、当前关卡等
    }
    
    // 从文件加载游戏状态（可选功能）
    public void loadGameState() {
        // 这里可以实现从文件加载游戏状态的功能
        // 比如加载最高分记录等
    }
}