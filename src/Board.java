import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class Board extends JPanel implements ActionListener {
    //here we declared all the dimension related to board (width and height)
    int B_HEIGHT=400;
    int B_WIDTH=400;
    //here i declared maxi size(total area) of board i.e. 400*400=1600
    int MAX_DOTS=1600;
    //one dot size =10
    int DOT_SIZE=10;
    // dots means initial size of snake is 3
    int DOTS;
    // here i declared coordinates(position) of snake i.e. x,y
    int x[]=new int[MAX_DOTS];
    int y[]=new int[MAX_DOTS];
    //create x and y coordinates for apple
    int apple_x;
    int apple_y;
    // create Image object
    Image body, head, apple;
    //create timer object
    Timer timer;
    //delay is used to defined speed of snake(gap between timer and actual time)(t=0->1)
    // 0.3 sec we are delaying speed of snake(every 0.3 sec timer got incremented)
    int DELAY=300;
    //create four direction variable(ek direction ko true rakha, remaining false)/ declare snake direction
    boolean leftDirection=true;
    boolean rightDirection=false;
    boolean upDirection=false;
    boolean downDirection=false;

    //declaring inGame
    boolean inGame=true;


    // make constructor of board
    Board(){
        // create TAdapter object to follow the key Listener, when we touched
        TAdapter tAdapter=new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
        //here i am going to set preferred size of board(inside provide dimension of board)
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        //inside i am using background color black of board
        setBackground(Color.BLACK);
        //call the initialize game method
        initGame();
        // call the load images method
        loadImages();
        // this is listening the action
        timer=new Timer(DELAY, this);
        timer.start();
    }

    //Initialize game (here i initialized coordinates of game)
    public void initGame(){
        // dots means initial size of snake is 3
        DOTS=3;
        //initialize snake position (i-> 0 to 3)
        // here i am initialize snake position in x and y = 50
        x[0]=250;
        y[0]=250;

        for(int i=0; i<DOTS; i++){
            // when snake is running in x-axis direction
            x[i]=x[0]+DOT_SIZE*i;
            y[i]=y[0];
        }

        //call apple's position(using generate random value)
        locateApple();
    }
    // Load images from resources folder to images object
    // create a method
    public void loadImages(){
        // for body(dot) image
        //when we load(images daal di) images from resources folder, it should be a image icon
        ImageIcon bodyIcon=new ImageIcon("src/resources/dot.png");
        //here we get(millega) image from bodyIcon, its draw at the particular position
        body = bodyIcon.getImage();
        //  for head image
        ImageIcon headIcon=new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();
        // for apple image
        ImageIcon appleIcon=new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();
    }
    //draw images at snakes and apple's position
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        doDrawing(g);
    }
    //draw new method for image to do the drawing
    public void doDrawing(Graphics g){
        if(inGame){
            //basically used drawImage method to draw the image...here specify parameter of apple
            // observer means board object(this is the JPanel)
            g.drawImage(apple, apple_x, apple_y,this);
            //drawImages for the body, head
            for(int i=0; i<DOTS; i++){
                // if i==0 that means we need to draw head image(i means head)
                if(i==0){
                    g.drawImage(head, x[0], y[0], this);
                }
                //this is for body image draw the particular position
                else{
                    g.drawImage(body, x[i], y[i], this);
                }

            }
        }
        else{
            gameOver(g);
            //timer must be stop
            timer.stop();
        }
    }

    //randomize apple's position
    public void locateApple(){
        //Math.random() generates the random number (0 to 1)*39->[0, 39]   , DOT_SIZE=10
        apple_x= ((int)(Math.random()*39))*DOT_SIZE;
        apple_y= ((int)(Math.random()*39))*DOT_SIZE;
    }
    //check collisions with border and body
    public void checkCollision(){
        //collision with body
        for(int i=1; i<DOTS; i++){
            if(i>4 && x[0]==x[i] && y[0]==y[i]){
                inGame=false;
            }
        }
        //collision with border
        if(x[0]<0){
            inGame=false;
        }
        if(x[0]>=B_WIDTH){
            inGame=false;
        }
        if(y[0]<0){
            inGame=false;
        }
        if(y[0]>=B_HEIGHT){
            inGame=false;
        }

    }
    //
    public void gameOver(Graphics g){
        String msg="Game Over";
        //calculate score(HERE 3 is initial snake dot)
        int score=(DOTS-3)*100;
        String scoremsg="Score:"+ Integer.toString(score);
        Font small=new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fontMetrics=getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - fontMetrics.stringWidth(msg)) / 2, B_HEIGHT / 4);
        g.drawString(scoremsg, (B_WIDTH - fontMetrics.stringWidth(scoremsg)) / 2, 3 * (B_HEIGHT / 4));


    }
    //when we used the action event, we need to perform(call) the move() function
    @Override
    public void actionPerformed(ActionEvent actionEvent){
//        if timer is increment then we need to check is collision or not
        if(inGame){
            checkApple();
            checkCollision();
            move();
        }

//        here we recall again the function
        repaint();
    }

    //make snake move
    public void move(){
        //all of the previous dots moves same position
        for(int i = DOTS-1; i>0; i--){
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        //we need to change the position of the head, according to the direction(snake is going to move)
        // if head is moving left then x(decreases means -ve),if head is moving right then x(increases means +ve),if head is going up then y(decreases), if head is going down then y(increases)
        if(leftDirection){
            x[0]-=DOT_SIZE;
        }
        if(rightDirection){
            x[0]+=DOT_SIZE;
        }
        if(upDirection){
            y[0]-=DOT_SIZE;
        }
        if(downDirection){
            y[0]+=DOT_SIZE;
        }
    }
    //make snake eat food
    public void checkApple(){
        //check if head of snake is equal to position of apple is true, then length of snake must be increased and random location of apple is new
        if(apple_x==x[0]&& apple_y==y[0]){
            DOTS++;
            locateApple();
        }
    }


    //implement controls
    private class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key=keyEvent.getKeyCode();
//            when we clicked any particular(ex: left) direction, then snake move that(left) diretion, should not moved right direction(false)
            if(key==KeyEvent.VK_LEFT && !rightDirection){
                leftDirection=true;
                upDirection=false;
                downDirection=false;
            }
            if(key==KeyEvent.VK_RIGHT && !leftDirection){
                rightDirection=true;
                upDirection=false;
                downDirection=false;
            }
            if(key==KeyEvent.VK_UP && !downDirection){
                upDirection=true;
                leftDirection=false;
                rightDirection=false;
            }
            if(key==KeyEvent.VK_DOWN && !upDirection){
                leftDirection=false;
                rightDirection=false;
                downDirection=true;
            }

        }
    }
}
