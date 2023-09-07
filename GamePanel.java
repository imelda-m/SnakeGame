import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int screen_width = 600;
    static final int screen_height = 600;
    static final int unit_size = 30; //how big do we want the objects in the screen
    static final int game_units = (screen_width * screen_height)/unit_size;
    static final int delay = 75; // the heigher the nr the slower the game
    //arrays to hold the coorditates for the body parts of the snake
    final int x[] = new int[game_units];
    final int y[] = new int[game_units];
    int bodyparts = 6;
    int apple_eaten;
    int applex; int appley;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(screen_width,screen_height));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapter());
        startGame();


    }
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(delay,this);// how fast the game is runnig
        timer.start();

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g){
        if (running){
            for (int i = 0; i < screen_height/unit_size; i++) {
                g.drawLine(i*unit_size,0,i*unit_size,screen_height); //draws vertical lines
                g.drawLine(0,i*unit_size,screen_width,i*unit_size);
                //how big do we want the items to be (one unit)
            }
            g.setColor(Color.red);
            g.fillOval(applex,appley,unit_size,unit_size);

            for (int i = 0; i < bodyparts; i++) {
                if(i == 0){
                   // g.setColor(Color.green);
                    g.setColor(Color.CYAN);
                    g.fillRect(x[i],y[i],unit_size,unit_size);
                }
                else{
                  //  g.setColor(new Color(45,180,0));
                    g.setColor(Color.BLUE);
                    g.fillRect(x[i],y[i],unit_size,unit_size);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("PT Serif", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score:" + apple_eaten,(screen_width - metrics.stringWidth("Score:" + apple_eaten))/2,g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }
    public void newApple(){
        applex = random.nextInt((int)(screen_width/unit_size))*unit_size; //to get one of the units
        appley = random.nextInt((int)(screen_height/unit_size))*unit_size;

    }
    public void  move(){
        for (int i = bodyparts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];

        }
        switch (direction){
            case 'U':
                y[0] = y[0] - unit_size; //up
                break;
            case 'D':
                y[0] = y[0] + unit_size;//down
                break;
            case 'L':
                x[0] = x[0] - unit_size;//left
                break;
            case 'R':
                x[0] = x[0] + unit_size;//right
                break;
        }

    }
    public void checkApple(){
        if ((x[0] == applex) && (y[0] == appley)){
            bodyparts++;
            apple_eaten++;
            newApple();
        }

    }
    public void checkCollisions(){
        //checks if head collides with body
        for (int i = bodyparts; i > 0 ; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        //check if head touches  left border
        if(x[0] < 0){
            running = false;
        }
        //check if head touches  right border
        if(x[0] > screen_width){
            running = false;
        }
        //check if head touches top border
        if(y[0] < 0){
            running = false;
        }
        //check if head touches bottom border
        if(y[0] > screen_height){
            running = false;
        }
        if(!running){
            timer.stop();
        }


    }
    public void gameOver(Graphics g){
        //game over text
        g.setColor(Color.red);
        g.setFont(new Font("PT Serif", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over",(screen_width - metrics1.stringWidth("Game Over"))/2,screen_height/2); //will put the string in the center of the frame
        //score
        g.setColor(Color.red);
        g.setFont(new Font("PT Serif", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score:" + apple_eaten,(screen_width - metrics2.stringWidth("Score:" + apple_eaten))/2,g.getFont().getSize());
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();

    }

    public class myKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }

        }
    }

}
