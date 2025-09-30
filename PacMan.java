import java.awt.*;

public class PacMan {
    private int x, y;
    private int prevX, prevY;
    private int size;
    private char direction = 'R'; // R-右, L-左, U-上, D-下
    private char nextDirection = 'R';
    
    public PacMan(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;
        this.size = size;
    }
    
    public void move() {
        prevX = x;
        prevY = y;
        
        direction = nextDirection;
        
        switch (direction) {
            case 'U':
                y -= size;
                break;
            case 'D':
                y += size;
                break;
            case 'L':
                x -= size;
                break;
            case 'R':
                x += size;
                break;
        }
    }
    
    public void undoMove() {
        x = prevX;
        y = prevY;
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        
        // 绘制吃豆人的身体
        g.fillOval(x, y, size, size);
        
        // 绘制嘴巴
        g.setColor(Color.BLACK);
        int mouthSize = size / 3;
        int mouthX = x + size / 2;
        int mouthY = y + size / 2;
        
        switch (direction) {
            case 'R':
                int[] xPointsR = {mouthX, x + size, x + size};
                int[] yPointsR = {mouthY, y + mouthSize, y + size - mouthSize};
                g.fillPolygon(xPointsR, yPointsR, 3);
                break;
            case 'L':
                int[] xPointsL = {mouthX, x, x};
                int[] yPointsL = {mouthY, y + mouthSize, y + size - mouthSize};
                g.fillPolygon(xPointsL, yPointsL, 3);
                break;
            case 'U':
                int[] xPointsU = {mouthX, x + mouthSize, x + size - mouthSize};
                int[] yPointsU = {mouthY, y, y};
                g.fillPolygon(xPointsU, yPointsU, 3);
                break;
            case 'D':
                int[] xPointsD = {mouthX, x + mouthSize, x + size - mouthSize};
                int[] yPointsD = {mouthY, y + size, y + size};
                g.fillPolygon(xPointsD, yPointsD, 3);
                break;
        }
        
        // 绘制眼睛
        g.setColor(Color.BLACK);
        int eyeSize = size / 8;
        int eyeOffset = size / 4;
        
        switch (direction) {
            case 'R':
                g.fillOval(x + eyeOffset, y + eyeOffset, eyeSize, eyeSize);
                g.fillOval(x + eyeOffset, y + size - eyeOffset - eyeSize, eyeSize, eyeSize);
                break;
            case 'L':
                g.fillOval(x + size - eyeOffset - eyeSize, y + eyeOffset, eyeSize, eyeSize);
                g.fillOval(x + size - eyeOffset - eyeSize, y + size - eyeOffset - eyeSize, eyeSize, eyeSize);
                break;
            case 'U':
                g.fillOval(x + eyeOffset, y + size - eyeOffset - eyeSize, eyeSize, eyeSize);
                g.fillOval(x + size - eyeOffset - eyeSize, y + size - eyeOffset - eyeSize, eyeSize, eyeSize);
                break;
            case 'D':
                g.fillOval(x + eyeOffset, y + eyeOffset, eyeSize, eyeSize);
                g.fillOval(x + size - eyeOffset - eyeSize, y + eyeOffset, eyeSize, eyeSize);
                break;
        }
    }
    
    public void setDirection(char direction) {
        this.nextDirection = direction;
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
    
    public char getDirection() {
        return direction;
    }
}