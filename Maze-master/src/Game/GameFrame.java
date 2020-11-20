package Game;

import model.Const;
import model.Map;
import util.SwingUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;

/**
 * 程序主窗口
 */
public class GameFrame extends JFrame {

    public GameFrame m = this;
    public Map map = new Map();
    public static Point origin = new Point();
    public HashSet<Integer> consts = new HashSet<Integer>();

    public GameFrame() throws HeadlessException {

        this.setUndecorated(true);
        this.setVisible(true);
        this.setSize(Const.WIDTH * Const.Size, (Const.HEIGHT) * Const.Size);
        this.setTitle("MAZE");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        consts.add(Const.BLANK);
        consts.add(Const.ANS);
        consts.add(Const.END);

        new PaintThread().start();

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                map.map[map.local.x][map.local.y] = Const.BLANK;

                int x = map.local.x;
                int y = map.local.y;

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (y > 0 && consts.contains(map.map[x][y - 1])) {
                        map.local.setLocation(x, y - 1);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (y < Const.HEIGHT - 1 && consts.contains(map.map[x][y + 1])) {
                        map.local.setLocation(x, y + 1);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (x > 0 && consts.contains(map.map[x - 1][y])) {
                        map.local.setLocation(x - 1, y);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (x < Const.WIDTH - 1 && consts.contains(map.map[x + 1][y])) {
                        map.local.setLocation(x + 1, y);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                    map.findWay();
                } else if (e.getKeyCode() == KeyEvent.VK_A) {
                    map.local = new Point(map.end);
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                } else if (e.getKeyCode() == KeyEvent.VK_Z) {
                    if (map.getState() != Thread.State.RUNNABLE){
                        map.start();
                    }
                }
                map.map[map.local.x][map.local.y] = Const.MAN;
                if (map.local.equals(map.end)) {
                    JOptionPane.showMessageDialog(null, "you win !", "", JOptionPane.WARNING_MESSAGE);
                    map = new Map();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        /* 按下（mousePressed 不是点击，而是鼠标被按下没有抬起） */
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 当鼠标按下的时候获得窗口当前的位置
                origin.x = e.getX();
                origin.y = e.getY();
            }
        });
        // 拖动（mouseDragged 指的不是鼠标在窗口中移动，而是用鼠标拖动）
        this.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                // 当鼠标拖动时获取窗口当前位置
                Point p = m.getLocation();
                // 设置窗口的位置
                // 窗口当前的位置 + 鼠标当前在窗口的位置 - 鼠标按下的时候在窗口的位置
                m.setLocation(p.x + e.getX() - origin.x, p.y + e.getY() - origin.y);
            }
        });
    }


    private Image off = null;

    @Override
    public void paint(Graphics g) {
        if (off == null) {
            off = this.createImage((Const.WIDTH + 2) * Const.Size, (Const.HEIGHT + 1) * Const.Size + 40);
        }
        Graphics graphics = off.getGraphics();

        ////////////////////////
        Color color = graphics.getColor();
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, SwingUtil.getScreen().width, SwingUtil.getScreen().height);
        map.printMap(graphics);

        ////////////////////////
        g.drawImage(off, 0, 0, null);
        graphics.dispose();
    }

    class PaintThread extends Thread {
        @Override
        public void run() {
            while (true) {
                repaint();
            }
        }

    }

    public static void main(String[] args) {
        GameFrame gameFrame = new GameFrame();
    }
}
