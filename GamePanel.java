//package Games.SnakeGames;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
//import java.util.Timer;
public class GamePanel extends JPanel implements ActionListener{
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;    //higher the unit size higher the square boxes 
    static final int GAME_UNIT  = (SCREEN_HEIGHT*SCREEN_WIDTH)/UNIT_SIZE;
    static final int DELAY = 75;    //for snake speed
    final int x[]= new int[GAME_UNIT];//game_unit = 14400
    final int y[]= new int[GAME_UNIT];
    int bodyParts = 6; //six body parts on the snake in beggining
    int applesEaten=0;
    int appleX;
    int appleY;
    //apples gonna appear the randomly 
    char direction = 'R';   //direction of snake set to right in beginning of game
    boolean running = false;    //beggining value 
    Timer timer;
    Random random ; 
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(new Color(51,204,255));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        start_game();
    }
    public void start_game(){
        New_Apple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(running){
            /*

            //use this loop to draw line accross x and y axis in game panel
            for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++)  
            {
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);//to create line || to y axix
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);//create lines parallel to x axis
            }

            */
            g.setColor(new Color(255,102,102)); //apples color
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);   //draw apple randomly 

            //drawing the snake 
            for(int i=0;i<bodyParts;i++){
                if(i==0){   //i=0 means its head of snake
                    g.setColor(Color.RED);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{   //means body part of the snake
                    g.setColor(new Color(153,0,0)); //rgb value for very dark red
                    //below line is for multicolor snake try it :)
                    //g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(new Color(0,0,153));
            g.setFont(new Font("SansSerif Bold",Font.ITALIC,30));
            //fontmatrix is used to get text at center of the panel
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score : "+ applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score : "+ applesEaten))/2 , g.getFont().getSize());
            //x co-ordinate =  (SCREEN_WIDTH - metrics.stringWidth("Score : "+ applesEaten))/2
            //y co-ordinate =  g.getFont().getSize()
        }
        else{
            gameover(g);
        }
    }
    public void New_Apple(){
        //to generate new apple for snake within each box randomly 
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        for(int i=bodyParts;i>0;i--)//to iterate through whole body of snake
        {
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        switch(direction){  //to change the direction of snake 
            case 'U':
                y[0]=y[0] - UNIT_SIZE;  //x[0] and y[0] for moving head of the snake
                break;
            case 'D':
                y[0]= y[0] + UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
        }
    }
    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            New_Apple();
        }
    }
    public void CheckCollisions(){
        for(int i=bodyParts;i>0;i--){
            if((x[0]==x[i]) && (y[0]==y[i])){   //if this condition is true means head collided with its own body
                running = false;
            }
        }

        //checking if head touches left border
        if(x[0]<0){
            running = false;
        }
        //checking if head toches right border 
        if(x[0]>SCREEN_WIDTH){
            running = false;
        }
        //check if head toches top 
        if(y[0] < 0){
            running = false;
        }
        //for head touches bottom
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if(!running)//if running is false
        {
            timer.stop();
        }
    }
    public void gameover(Graphics g){
        //displays gameover 
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif Bold",Font.BOLD,85));
        //fontmatrix is used to align text (like here to center of the panel
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics1.stringWidth("GAME OVER"))/2 , SCREEN_HEIGHT/2);
        
        //displaying score 
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif Bold",Font.BOLD,40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score : "+ applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score : "+ applesEaten))/2 , g.getFont().getSize());
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkApple();
            CheckCollisions();
        }
        repaint();
    }

    //inner class
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT :
                    if(direction != 'R')   
                    //to limit direction switch to not greater than 90 deg (eg. if direction is up dont move to down 
                    {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT : 
                    if(direction != 'L')  
                    {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP : 
                    if(direction != 'D')  
                    {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN : 
                    if(direction != 'U')  
                    {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
