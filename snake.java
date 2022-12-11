

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class snake extends JPanel implements ActionListener, KeyListener {

    int a = 25, w = 25, h = 25;
    int sl = 2;//蛇长度
    int sl2 = 2;//第二条蛇
    int st = 0;//记录游戏状态，0未开始，1正在游戏，2游戏结束，3游戏重置
    int foodX, foodY;
    int[] sX = new int[w * h];
    int[] sY = new int[w * h];
    int[] sX2 = new int[w * h];
    int[] sY2 = new int[w * h];
    int fx = 2;//记录蛇的运动方向
    int fx2 = 1;
    int died = 0;//记录死亡类型
    int graed = 0;//记录分数
    int max = 0;//最高分
    Timer t = new Timer(200, this);


    //初始化数据
    public void makeJFrame() {
        JFrame f = new JFrame();
        f.setTitle("双龙戏珠");
        f.add(this);
        f.setSize(a * w + 200, a * h + 200);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(true);
        t.start();
        f.addKeyListener(this);
        initializeSl();
        initializeSl2();
        //判断两条蛇有没有重叠
        for (int i = 0; i < sl; i++) {
            for (int i1 = 0; i1 < sl2; i1++) {
                if (sX[i] == sX2[i1] && sY[i] == sY[i1]) {
                    sX2[0] = (int) (Math.random() * (w - 2)) * a + 125;
                    sY2[0] = (int) (Math.random() * (h - 2)) * a + 125;
                    i = 0;
                    i1 = 0;
                }
            }
        }
        ProduceFood();
    }

    //初始化蛇1数据
    public void initializeSl() {
        sl = 2;
        sX[0] = (int) (Math.random() * (w - 2)) * a + 125;
        sY[0] = (int) (Math.random() * (h - 2)) * a + 125;
        fx = (int) (Math.random() * 4);
        if (fx == 0) {
            sX[1] = sX[0];
            sY[1] = sY[0] + a;
        } else if (fx == 1) {
            sX[1] = sX[0];
            sY[1] = sY[0] - a;
        } else if (fx == 2) {
            sX[1] = sX[0] + a;
            sY[1] = sY[0];
        } else if (fx == 3) {
            sX[1] = sX[0] - a;
            sY[1] = sY[0];
        }
    }

    //初始化蛇2数据
    public void initializeSl2() {
        sl2 = 2;
        sX2[0] = (int) (Math.random() * (w - 2)) * a + 125;
        sY2[0] = (int) (Math.random() * (h - 2)) * a + 125;
        fx2 = (int) (Math.random() * 4);
        if (fx2 == 0) {
            sX2[1] = sX2[0];
            sY2[1] = sY2[0] + a;
        } else if (fx2 == 1) {
            sX2[1] = sX2[0];
            sY2[1] = sY2[0] - a;
        } else if (fx2 == 2) {
            sX2[1] = sX2[0] + a;
            sY2[1] = sY2[0];
        } else if (fx2 == 3) {
            sX2[1] = sX2[0] - a;
            sY2[1] = sY2[0];
        }
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.gray);
        g.fillRect(100, 100, w * a, h * a);
        //画格子
        g.setColor(Color.black);
        for (int i = 0; i <= w; i++) {
            g.drawLine(100 + a * i, 100, a * i + 100, a * h + 100);
        }
        for (int i = 0; i <= h; i++) {
            g.drawLine(100, a * i + 100, a * w + 100, a * i + 100);
        }
        //画蛇
        g.setColor(Color.YELLOW);
        g.fillRect(sX[0] + 1, sY[0] + 1, a - 1, a - 1);
        g.setColor(Color.RED);
        for (int i = 1; i < sl; i++) {
            g.fillRect(sX[i] + 1, sY[i] + 1, a - 1, a - 1);
        }
        g.setColor(Color.YELLOW);
        g.fillRect(sX2[0] + 1, sY2[0] + 1, a - 1, a - 1);
        g.setColor(Color.blue);
        for (int i = 1; i < sl2; i++) {
            g.fillRect(sX2[i] + 1, sY2[i] + 1, a - 1, a - 1);
        }

        //画食物
        g.setColor(Color.BLACK);
        g.fillOval(foodX, foodY, a, a);
        //画提示内容
        g.setColor(Color.blue);
        g.setFont(new Font("宋体", Font.BOLD, 15));
        g.drawString("你是红蛇，有一条蓝色的蛇在跟你抢吃的，如果你们的头撞到对方或者自己的身体就会嘎。加油吧！！", 50, 30);
        g.setColor(Color.magenta);
        g.drawString("温馨提示：你可以跨过边界", 50, 50);
        if (st == 0 || st == 3) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("宋体", Font.BOLD, 20));
            g.drawString("按下空格键开始游戏", 100, 70);
            g.drawString("吃一个食物得5分,杀死敌人得10分", 100, 90);
        }
        if (st == 1) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("宋体", Font.BOLD, 20));
            g.drawString("按下空格键暂停游戏", 100, 70);
        }
        // 游戏失败，结束
        if (st == 2) {
            g.setColor(Color.RED);
            g.setFont(new Font("宋体", Font.BOLD, 20));
            g.drawString("游戏失败   按下空格键重新开始游戏", 100, 70);
            g.drawString("最终分得分：" + graed, 100, 90);
        }
        g.setColor(Color.BLACK);
        g.setFont(new Font("宋体", Font.BOLD, 20));
        g.drawString("分数：" + graed, w * a - 50, 70);
        g.drawString("最高分数：" + max, w * a - 70, 90);
        if (died == 1) {
            g.setColor(Color.magenta);
            g.setFont(new Font("宋体", Font.BOLD, 20));
            g.drawString("死因：你吃了自己", 300, 90);
        } else if (died == 2) {
            g.setColor(Color.magenta);
            g.setFont(new Font("宋体", Font.BOLD, 20));
            g.drawString("死因：你撞到了敌人", 300, 90);
        }
    }

    //寻找最短运动方式
    public int[] WayFinding(int a, int b) {
        int way[] = {0, 0};
        if (a - foodX < 0) {
            if (Math.abs(a - foodX) <= w * this.a / 2) {
                way[0] = 1;
            } else if (Math.abs(a - foodX) > w * this.a / 2) {
                way[0] = -1;
            }
        } else if (a - foodX > 0) {
            if (Math.abs(a - foodX) <= w * a / 2) {
                way[0] = -1;
            } else if (Math.abs(a - foodX) > w * this.a / 2) {
                way[0] = 1;
            }
        }
        if (b - foodY < 0) {
            if (Math.abs(b - foodY) <= h * this.a / 2) {
                way[1] = 1;
            } else if (Math.abs(b - foodY) > h * this.a / 2) {
                way[1] = -1;
            }
        } else if (b - foodY > 0) {
            if (Math.abs(b - foodY) <= h * this.a / 2) {
                way[1] = -1;
            } else if (Math.abs(b - foodY) > h * this.a / 2) {
                way[1] = 1;
            }
        }
        return way;
    }

    //生成食物
    public void ProduceFood() {
        foodX = (int) (Math.random() * w) * a + 100;
        foodY = (int) (Math.random() * h) * a + 100;
        for (int i = 0; i < sl + sl2; i++) {
            if (i < sl) {
                if (foodX == sX[i] && foodY == sY[i]) {
                    foodX = (int) (Math.random() * w) * a + 100;
                    foodY = (int) (Math.random() * h) * a + 100;
                    i = 0;
                }
            } else {
                if (foodX == sX2[i - sl] && foodY == sY2[i - sl]) {
                    foodX = (int) (Math.random() * w) * a + 100;
                    foodY = (int) (Math.random() * h) * a + 100;
                    i = 0;
                }
            }
        }
    }

    //线程中要执行的内容
    @Override
    public void actionPerformed(ActionEvent e) {
        if (st == 1) {
            //蛇的每一节位置移动到前一节的位置上
            for (int i = sl - 1; i > 0; i--) {
                sX[i] = sX[i - 1];
                sY[i] = sY[i - 1];
            }
            for (int i = sl2 - 1; i > 0; i--) {
                sX2[i] = sX2[i - 1];
                sY2[i] = sY2[i - 1];
            }
            //System.out.println(fx);
            //方向判断，蛇不能倒着走
            if (fx == 0) {
                if (sY[0] - a >= 100) {
                    sY[0] -= a;
                } else {
                    sY[0] = 100 + (h - 1) * a;
                }
            } else if (fx == 1) {
                if (sY[0] + a <= 100 + (h - 1) * a) {
                    sY[0] += a;
                } else {
                    sY[0] = 100;
                }
            } else if (fx == 2) {
                if (sX[0] - a >= 100) {
                    sX[0] -= a;
                } else {
                    sX[0] = 100 + (w - 1) * a;
                }
            } else if (fx == 3) {
                if (sX[0] + a <= 100 + (w - 1) * a) {
                    sX[0] += a;
                } else {
                    sX[0] = 100;
                }
            }
            //蛇2的运动判断
            if (WayFinding(sX2[0], sY2[0])[1] < 0 && fx2 != 1) {
                if (sY2[0] - a >= 100) {
                    sY2[0] -= a;
                    fx2 = 0;
                } else {
                    sY2[0] = 100 + (h - 1) * a;
                }
            }
            //如果食物出现在蛇的运动方向的正后方就得先向旁边转个弯
            else if (WayFinding(sX2[0], sY2[0])[1] < 0 && fx2 == 1) {
                if (sX2[0] - a >= 100) {
                    sX2[0] -= a;
                    fx2 = 2;
                } else {
                    sX2[0] = 100 + (w - 1) * a;
                }
            } else if (WayFinding(sX2[0], sY2[0])[1] > 0 && fx2 != 0) {
                if (sY2[0] + a <= 100 + (h - 1) * a) {
                    sY2[0] += a;
                    fx2 = 1;
                } else {
                    sY2[0] = 100;
                }
            } else if (WayFinding(sX2[0], sY2[0])[1] > 0 && fx2 == 0) {
                if (sX2[0] + a <= 100 + (w - 1) * a) {
                    sX2[0] += a;
                    fx2 = 3;
                } else {
                    sX2[0] = 100;
                }
            } else if (WayFinding(sX2[0], sY2[0])[0] < 0 && fx2 != 3) {
                if (sX2[0] - a >= 100) {
                    sX2[0] -= a;
                    fx2 = 2;
                } else {
                    sX2[0] = 100 + (w - 1) * a;
                }
            } else if (WayFinding(sX2[0], sY2[0])[0] < 0 && fx2 == 3) {
                if (sY2[0] - a >= 100) {
                    sY2[0] -= a;
                    fx2 = 0;
                } else {
                    sY2[0] = 100 + (h - 1) * a;
                }
            } else if (WayFinding(sX2[0], sY2[0])[0] > 0 && fx2 != 2) {
                if (sX2[0] + a <= 100 + (w - 1) * a) {
                    sX2[0] += a;
                    fx2 = 3;
                } else {
                    sX2[0] = 100;
                }
            } else if (WayFinding(sX2[0], sY2[0])[0] > 0 && fx2 == 2) {
                if (sY2[0] + a <= 100 + (h - 1) * a) {
                    sY2[0] += a;
                    fx2 = 1;
                } else {
                    sY2[0] = 100;
                }
            }
            //System.out.println(" x:"+sX2[0]+"  y:"+sY2[0]+"  "+WayFinding(sX2[0],sY2[0])[0]+WayFinding(sX2[0],sY2[0])[1]);
            //System.out.println("fx:"+foodX+" fy:"+foodY);

            //判断是否吃到食物
            if (sX[0] == foodX && sY[0] == foodY) {
                sl++;
                graed += 5;
                if (graed > max) {
                    max = graed;
                }
                sX[sl - 1] = -100;//初始化新长出的身子，把他放到看不见的地方，不然会在屏幕上随机位置闪烁
                sY[sl - 1] = -100;
                foodX = (int) (Math.random() * w) * a + 100;
                foodY = (int) (Math.random() * h) * a + 100;
                ProduceFood();

            }
            if (sX2[0] == foodX && sY2[0] == foodY) {
                sl2++;
                sX2[sl2 - 1] = -100;
                sY2[sl2 - 1] = -100;
                //System.out.println(sl2);
                ProduceFood();
            }

            //判断死不死
            for (int i = 1; i < sl + sl2; i++) {
                if (i < sl) {
                    if (sX[0] == sX[i] && sY[0] == sY[i]) {
                        st = 2;
                        died = 1;
                    }
                } else {
                    if (sX[0] == sX2[i - sl] && sY[0] == sY2[i - sl]) {
                        st = 2;
                        died = 2;
                    }
                }
            }
            for (int i = 1; i < sl + sl2; i++) {
                if (i < sl2) {
                    if (sX2[0] == sX2[i] && sY2[0] == sY2[i]) {
                        initializeSl2();
                        System.out.println("敌人吃到了自己");
                    }
                } else {
                    if (sX2[0] == sX[i - sl2] && sY2[0] == sY[i - sl2]) {
                        initializeSl2();
                        graed += 10;
                        if (graed > max) {
                            max = graed;
                        }
                        System.out.println("敌人撞到了你");
                    }
                }
            }
            //System.out.println("运行ing");
            repaint();
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        //强制重置游戏，调试用
        if (k == KeyEvent.VK_ENTER) {
            initializeSl2();
            initializeSl();
            ProduceFood();
            repaint();
            st = 0;
        }
        if (k == KeyEvent.VK_SPACE) {
            if (st == 0||st==3) {
                st = 1;
            } else if (st == 2) {
                st = 3;
                graed = 0;
                initializeSl2();
                initializeSl();
                ProduceFood();
                repaint();
            } else if (st == 1) {
                st = 0;
            }
            if (died != 0) {
                died = 0;
            }
        }
        if (k == KeyEvent.VK_UP || k == KeyEvent.VK_W) {
            if (fx != 1)
                fx = 0;
        } else if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S) {
            if (fx != 0)
                fx = 1;
        } else if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A) {
            if (fx != 3)
                fx = 2;
        } else if (k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D) {
            if (fx != 2)
                fx = 3;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    public static void main(String[] args) {
        snake s = new snake();
        s.makeJFrame();
    }
}