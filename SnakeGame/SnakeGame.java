import javax.swing.*;   //extended package

public class SnakeGame extends JFrame {

    SnakeGame(){
        super("SnakeGame");     //must be first stmt
       add(new Board());     //creating panel
       pack();              //to refresh javaframe
        setVisible(true);    //to create frame
        setSize(300,300);          
       setLocationRelativeTo(null);                 //to set location of frame on the screen

    }
    public static void main(String[] args) {
    new SnakeGame();
      
    }
}
