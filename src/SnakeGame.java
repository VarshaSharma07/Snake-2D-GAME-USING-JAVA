import javax.swing.*;

public class SnakeGame extends JFrame{

    // make an object for board class
    Board board;


    //create snake game constructor
    SnakeGame(){
        // here we are declare an object of board class
        board =new Board();
        //add board to the frame
        add(board);
        //its pack the frame (means packs the particular parent component to its child component)
        pack();
        //when i used pack function then its mandatory to used setResizable
        //   its resize to the board dimension(here we are adjusted board size to the frame)
        setResizable(false);
        // here i set board to be visible in JFrame
        setVisible(true);

    }

    public static void main(String[] args) {
         // Initialize Snake Game
         // first we create an object SnakeGame class
        SnakeGame snakeGame=new SnakeGame();
    }
}