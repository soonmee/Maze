package model;

import java.awt.*;
import java.util.ArrayList;

/**
 * 迷宫地图
 */
public class Map extends Thread {

    public Integer[][] map;
    public Point local = new Point(1, 1);
    public Point end = createEndPoint();
    public Point temp;

    public Map() {
        map = new Integer[Const.WIDTH][Const.HEIGHT];
        //将地图置为未初始化值
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (i == 0 || i == Const.WIDTH || j == 0 || j == Const.HEIGHT) {
                    //边界置为墙
                    map[i][j] = Const.WALL;
                } else if (i % 2 == 1) {
                    map[i][j] = Const.BLANK;
                } else {
                    map[i][j] = Const.WALL;
                }
            }
        }


        createMap();
    }

    /**
     * 生成地图
     */
    private void createMap() {

        //初始化
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (i == 0 || i == map.length - 1 || j == 0 || j == map[0].length - 1) {
                    map[i][j] = Const.WALL;
                } else {
                    if (i % 2 == 1 && j % 2 == 1) {
                        map[i][j] = Const.BLANK;
                    } else {
                        map[i][j] = Const.NOTINI;
                    }
                }
            }
        }


        pointSet(map, 1, 1);
        map[local.x][local.y] = Const.MAN;
        map[end.x][end.y] = Const.END;

        //处理未初始化地区
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j].equals(Const.WAY)) {
                    map[i][j] = Const.BLANK;
                }
                if (map[i][j].equals(Const.NOTINI)) {
                    map[i][j] = Const.WALL;
                }
            }
        }

    }

    /**
     * dfs创建地图路径
     */
    private void pointSet(Integer[][] map, Integer x, Integer y) {

        map[x][y] = Const.WAY;

        ArrayList<Integer> direction = new ArrayList<>();
        direction.add(Const.UP);
        direction.add(Const.DOWN);
        direction.add(Const.LEFT);
        direction.add(Const.RIGHT);

        while (!direction.isEmpty()) {
            Integer integer = direction.get((int) (Math.random() * direction.size()));
            if (integer.equals(Const.UP)) {
                if (map[x][y - 1].equals(Const.NOTINI) && map[x][y - 2].equals(Const.BLANK)) {
                    map[x][y - 1] = Const.BLANK;
                    pointSet(map, x, y - 2);
                }
            }
            if (integer.equals(Const.DOWN)) {
                if (map[x][y + 1].equals(Const.NOTINI) && map[x][y + 2].equals(Const.BLANK)) {
                    map[x][y + 1] = Const.BLANK;
                    pointSet(map, x, y + 2);
                }
            }
            if (integer.equals(Const.LEFT)) {
                if (map[x - 1][y].equals(Const.NOTINI) && map[x - 2][y].equals(Const.BLANK)) {
                    map[x - 1][y] = Const.BLANK;
                    pointSet(map, x - 2, y);
                }
            }
            if (integer.equals(Const.RIGHT)) {
                if (map[x + 1][y].equals(Const.NOTINI) && map[x + 2][y].equals(Const.BLANK)) {
                    map[x + 1][y] = Const.BLANK;
                    pointSet(map, x + 2, y);
                }
            }
            direction.remove(integer);
        }
    }

    /**
     * 生成终点
     */
    public Point createEndPoint() {
        int x = 0, y = 0;
        int n = (int) (Math.random() * 4) + 1;
        switch (n) {
            case 1:
            case 2:
                x = ((int) (Math.random() * 2)) * (Const.WIDTH - 1);
                y = (int) (Math.random() * (Const.HEIGHT - 1));
                if ((y & 1) == 0) {
                    y++;
                }
                break;
            case 3:
            case 4:
                y = ((int) (Math.random() * 2)) * (Const.HEIGHT - 1);
                x = (int) (Math.random() * (Const.WIDTH - 1));
                if ((x & 1) == 0) {
                    x++;
                }
                break;
            default:
                break;
        }

        return new Point(x, y);
    }


    /**
     * 寻找正确路径
     */
    public void findWay() {
        for (int i = map.length - 1; i >= 0; i--) {
            for (int j = map[0].length - 1; j >= 0; j--) {
                if (map[i][j].equals(Const.ANS)) {
                    map[i][j] = Const.BLANK;
                }
            }
        }

        dfs(temp = new Point(local));

        for (int i = map.length - 1; i >= 0; i--) {
            for (int j = map[0].length - 1; j >= 0; j--) {
                if (map[i][j].equals(Const.DEATH)) {
                    map[i][j] = Const.BLANK;
                }
            }
        }

        map[local.x][local.y] = Const.MAN;
    }

    public boolean dfs(Point point) {

        int x = point.x;
        int y = point.y;
        Integer temp = Const.BLANK;

        if (point.equals(end)) {
            map[x][y] = Const.END;
            return true;
        }
        map[x][y] = Const.ANS;

        if (map[x][y - 1].equals(temp) || map[x][y - 1].equals(Const.END)) {
            point.setLocation(x, y - 1);
            if (dfs(point)) {
                return true;
            }
        }
        if (map[x][y + 1].equals(temp) || map[x][y + 1].equals(Const.END)) {
            point.setLocation(x, y + 1);
            if (dfs(point)) {
                return true;
            }
        }
        if (map[x - 1][y].equals(temp) || map[x - 1][y].equals(Const.END)) {
            point.setLocation(x - 1, y);
            if (dfs(point)) {
                return true;
            }
        }
        if (map[x + 1][y].equals(temp) || map[x + 1][y].equals(Const.END)) {
            point.setLocation(x + 1, y);
            if (dfs(point)) {
                return true;
            }
        }

        map[x][y] = Const.DEATH;
        return false;
    }

    /**
     * 自动走到出口
     */
    public void goOut(Point point) {
        Integer temp = Const.ANS;
        int x = point.x;
        int y = point.y;
        int cnt;

        while (!point.equals(end)) {
            map[x][y] = Const.BLANK;
            y = point.y;
            x = point.x;
            map[x][y] = Const.MAN;
            cnt = 0;

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                continue;
            }

            if (map[x][y - 1].equals(temp)) {
                point.setLocation(x, y - 1);
                cnt++;
            }
            if (map[x][y + 1].equals(temp)) {
                point.setLocation(x, y + 1);
                cnt++;
            }
            if (map[x - 1][y].equals(temp)) {
                point.setLocation(x - 1, y);
                cnt++;
            }
            if (map[x + 1][y].equals(temp)) {
                point.setLocation(x + 1, y);
                cnt++;
            }
            if (cnt == 0) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void run() {
        goOut(local);
    }

    /**
     * 打印地图
     */
    public void printMap(Graphics graphics) {

        Color color = graphics.getColor();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j].equals(Const.WALL)) {
                    //墙
                    graphics.setColor(Color.black);
                    graphics.fillRect(i * Const.Size, j * Const.Size, Const.Size, Const.Size);
                    graphics.setColor(Color.white);
                    graphics.drawRect(i * Const.Size, j * Const.Size, Const.Size, Const.Size);
                } else if (map[i][j].equals(Const.END)) {
                    //终点
                    graphics.setColor(Color.green);
                    graphics.fillRect(i * Const.Size, j * Const.Size, Const.Size, Const.Size);
                } else if (map[i][j].equals(Const.MAN)) {
                    //人物
                    graphics.setColor(Color.RED);
                    graphics.fillRect(i * Const.Size, j * Const.Size, Const.Size, Const.Size);
                } else if (map[i][j].equals(Const.ANS)) {
                    //出路
                    graphics.setColor(Color.orange);
                    graphics.fillRect(i * Const.Size, j * Const.Size, Const.Size, Const.Size);
                } else {
                    //路
                    graphics.setColor(Color.white);
                    graphics.fillRect(i * Const.Size, j * Const.Size, Const.Size, Const.Size);
                }
            }
        }

        graphics.setColor(color);
    }
}
