import java.awt.*;
import java.util.Random;

public class Ghost {
    private int x, y;
    private int size;
    private Color color;
    private char direction;
    private Random random;
    private int moveCounter = 0;
    
    public Ghost(int x, int y, int size, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
        this.random = new Random();
        this.direction = getRandomDirection();
    }
    
    public void move(GameMap gameMap) {
        moveCounter++;
        
        // 每隔一定步数改变方向，增加随机性
        if (moveCounter % 20 == 0 || isBlocked(gameMap)) {
            direction = getRandomDirection();
            moveCounter = 0;
        }
        
        int newX = x;
        int newY = y;
        
        switch (direction) {
            case 'U':
                newY -= size;
                break;
            case 'D':
                newY += size;
                break;
            case 'L':
                newX -= size;
                break;
            case 'R':
                newX += size;
                break;
        }
        
        // 检查新位置是否有效
        if (!gameMap.isWall(newX, newY)) {
            x = newX;
            y = newY;
        } else {
            // 如果撞墙，立即改变方向
            direction = getRandomDirection();
        }
    }
    
    private boolean isBlocked(GameMap gameMap) {
        int newX = x;
        int newY = y;
        
        switch (direction) {
            case 'U':
                newY -= size;
                break;
            case 'D':
                newY += size;
                break;
            case 'L':
                newX -= size;
                break;
            case 'R':
                newX += size;
                break;
        }
        
        return gameMap.isWall(newX, newY);
    }
    
    private char getRandomDirection() {
        char[] directions = {'U', 'D', 'L', 'R'};
        return directions[random.nextInt(directions.length)];
    }
    
    public void draw(Graphics g) {
        // 绘制幽灵身体
        g.setColor(color);
        g.fillOval(x, y, size, size);
        
        // 绘制幽灵底部的波浪形状
        g.fillRect(x, y + size / 2, size, size / 2);
        
        // 绘制底部的锯齿
        g.setColor(Color.BLACK);
        int zigzagWidth = size / 5;
        for (int i = 0; i < 5; i++) {
            int[] xPoints = {x + i * zigzagWidth, x + i * zigzagWidth + zigzagWidth / 2, x + (i + 1) * zigzagWidth};
            int[] yPoints = {y + size, y + size - zigzagWidth / 2, y + size};
            g.fillPolygon(xPoints, yPoints, 3);
        }
        
        // 绘制眼睛
        g.setColor(Color.WHITE);
        int eyeSize = size / 6;
        int eyeOffset = size / 4;
        g.fillOval(x + eyeOffset, y + eyeOffset, eyeSize, eyeSize);
        g.fillOval(x + size - eyeOffset - eyeSize, y + eyeOffset, eyeSize, eyeSize);
        
        // 绘制瞳孔
        g.setColor(Color.BLACK);
        int pupilSize = eyeSize / 2;
        int pupilOffset = eyeSize / 4;
        
        // 根据移动方向调整瞳孔位置
        int leftPupilX = x + eyeOffset + pupilOffset;
        int rightPupilX = x + size - eyeOffset - eyeSize + pupilOffset;
        int pupilY = y + eyeOffset + pupilOffset;
        
        switch (direction) {
            case 'L':
                leftPupilX -= pupilOffset / 2;
                rightPupilX -= pupilOffset / 2;
                break;
            case 'R':
                leftPupilX += pupilOffset / 2;
                rightPupilX += pupilOffset / 2;
                break;
            case 'U':
                pupilY -= pupilOffset / 2;
                break;
            case 'D':
                pupilY += pupilOffset / 2;
                break;
        }
        
        g.fillOval(leftPupilX, pupilY, pupilSize, pupilSize);
        g.fillOval(rightPupilX, pupilY, pupilSize, pupilSize);
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getSize() {
        return size;
    }
    
    public Color getColor() {
        return color;
    }
}