import java.awt.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class tictactoe_gui {
    static boolean compPlayer = false;
    static int mode = -1;
    static char p1 = ' ';
    public static void main(String[] args) {
        // start game
        Common_Frame c = new Common_Frame();
        c.build();
    }
}



class Play extends Common_Frame{

    public char grid[][] = new char[3][3]; 
    int random;
    int single;
    char current_player;
    char player_1, player_2;
    ImageIcon blank, x, o, win_x, win_o;
    ArrayList <Integer> available = new ArrayList<>(9);
    JFrame pframe;
    JLabel label;
    Random r;
    String txt, spaces;
    
    public Play(boolean mode_true, int mode, char p1){
        // Game play screen

        pframe = new JFrame("TIC-TAC-TOE");
        pframe.setSize(SIZE_X, SIZE_Y);
        pframe.setBackground(Color.WHITE);
        pframe.setLocationRelativeTo(null); // window in middle of screen
        pframe.setVisible(true);
        pframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        r = new Random(); // for randomizing number

        // initialising the ImageIcons
        blank = new ImageIcon( getClass().getResource("tic-tac-toe_images/blank.png") , "Blank");
        x = new ImageIcon(getClass().getResource("tic-tac-toe_images/x.png") , "x");
        o = new ImageIcon(getClass().getResource("tic-tac-toe_images/o.png") , "o");
        win_o = new ImageIcon(getClass().getResource("tic-tac-toe_images/win_o.png") , "win_o");
        win_x = new ImageIcon(getClass().getResource("tic-tac-toe_images/win_x.png") , "win_x");
        

        JPanel play_panel = new JPanel(new GridLayout(3,3));
        // create Grid add grid to panel
        for (int i = 0; i < 9; i++) {
            Grid[i] = new JButton(blank);
            play_panel.add(Grid[i]);
            Grid[i].addActionListener(this);
        }

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                grid[i][j] = ' ';
            }
        }

        //p2 : for EXIT buttons
        Exit = new JButton("Exit");
        Exit.setPreferredSize(new Dimension(70,35));
        Exit.setBackground(Color.WHITE);
        Exit.setForeground(Color.BLACK);
        Exit.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 20));
        Exit.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.black));
        Exit.addActionListener(this);

        
        int q = r.nextInt(100);
        // set current player by random:
        if (q%2 == 0) 
            current_player = 'x';
        else 
            current_player = 'o';

            q = r.nextInt(100);
        if ((q%2 == 0 && p1 == ' ') || p1 == 'x'){
            player_1 = 'x'; player_2 = 'o';
        }   
        else {
            player_1 = 'o'; player_2 = 'x';
        } 

        /* indicate current player's turn and have exit button on top right corner*/
        spaces = "                                  ";
        txt  = current_player+"'s turn" + spaces;
        label = new JLabel(txt);
        label.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 20));
        JPanel p2 = new JPanel();
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.CENTER);
        p2.setLayout(fl);
        p2.add(label);
        fl.setAlignment(FlowLayout.RIGHT);
        p2.setLayout(fl);
        p2.add(Exit);
               
        // set available spots
        for (int i = 0; i < 9; i++) available.add(i);

        // adding Panels to frame
        pframe.add(play_panel, BorderLayout.CENTER);
        pframe.add(p2, BorderLayout.NORTH);

        // to start game
        if (mode_true){
            single = 1;
            // player 2 is ai
            switch(mode){
                case 0:{ // easy mode; 85% random placement, 15% best move
                    random = 85; break;
                } 
                case 1:{ // medium mode; 60% random placement, 40% best move
                    random = 60; break;
                }
                case 2:{ // hard mode; 25% random placement, 75% best move
                    random = 25; break;
                }
                case 3:{ // impossible mode; 100% best move
                    random = 0; break;
                }
            }
            // call to start the play
            ai_gamePlay();
        }
        else
        {
            single = 0;
        }
    }

    void delay(long tym)
    {
        try{
            TimeUnit.MILLISECONDS.sleep(tym);
        }
        catch(Exception E)
        {
            System.out.println("Error " + E.getMessage());
        }
    }

    void ai_gamePlay(){
        if (current_player == player_2){
            // depending on difficulty call bestMove
            int diff = r.nextInt(100) - 1;
            if (diff >= random)  bestMove();
            else randomizedMove();
        }
    }


    void randomizedMove()
    {
        // randomly pick a number from available and place the symbol in that spot
        int spot = available.get(r.nextInt(available.size()));
        delay(500);
        place(player_2, spot);
        available.remove((Integer)spot);
    }

    void bestMove(){
        int bestScore = -1000;;
        int score;
        int bestMove[] = new int[2];
        // we know that the player is the computer - maximising player
        for (int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if (grid[i][j] == ' '){
                    grid[i][j] = player_2;
                    score = minimax(grid, false, 0, -1000, 1000);
                    grid[i][j] = ' ';
                    if (score > bestScore){
                        // since we need to choose maximising path for computer
                        bestScore = score;
                        bestMove[0] = i; bestMove[1] = j;
                    }
                }
            }
        }
        delay(500);
        // after best move identified, place token in spot
        place(player_2, 3*bestMove[0] + bestMove[1]);
    }

    private int minimax(char grid[][], boolean isMax, int depth, int alpha, int beta)
    {
        // check if winner
        //if no winner upon calling winner function
        
        char result[] = winner().toCharArray();
        if (result[0] != ' '){
            if (result[0] == player_2)
                return 10;
            else if (result[0] == player_1)
                return -10;
            else if (result[0] == 't')
                return 0;
        }
           
        // if at maximising, next turn we will figure optimum move of opposition and vice versa
        int moveScore;

        if (isMax){
            // choose player as the computer
            moveScore = -1000;
            for (int p = 0; p <3; p++){
                for(int q = 0; q < 3; q++){
                    // checking if spot is occupied or not
                    if (grid[p][q] == ' '){
                        grid[p][q] = player_2;
                        int score_local = minimax(grid, !isMax, depth+1, alpha, beta);
                        grid[p][q] = ' ';
                        moveScore = Math.max(score_local, moveScore);
                        alpha = Math.max(score_local, alpha);
                        if (beta <= alpha){
                            return moveScore;
                        }
                    }
                }
            }
            return moveScore;
        }
        else{
            // choose player as the human
            moveScore = 1000;
            for (int p = 0; p < 3; p++){
                for(int q = 0; q < 3; q++){
                    // checking if spot is occupied or not
                    if (grid[p][q] == ' '){
                        grid[p][q] = player_1;
                        int score_local = minimax(grid, !isMax, depth+1, alpha, beta);
                        grid[p][q] = ' ';
                        moveScore = Math.min(score_local, moveScore);
                        beta = Math.min(score_local, beta);
                        if (beta <= alpha){
                            return moveScore;
                        }
                    }
                }
            }
            return moveScore;
        }
    }


    public String winner(){
        // 3 rows match
        // 3 columns match
        // 2 diagonals match

        char t[] = {' ', ' ', ' '};

        char[] d = checkDiag();
        if (d[0] == ' '){
            // no diagonal winner
            char[] r = checkRow();
            if (r[0] == ' '){
                // no row winner
                char[] c = checkColumn();
                if (c[0] != ' ')
                    // if there is colun winner, return c
                    return new String(c);
            }
            else
                // there is row winner, return r
                return new String(r);
        }
        else
            // there is diagonal winner
            return new String(d);  
        
        if ((9-available.size()) == 0){
            // if it has reached terminal state with no winner
            // tied
            t[0] = 't';
        }

        return new String(t);
    }

    
    private char[] checkRow(){
        
        char details[] = {' ', ' ', ' '};
        for (int i = 0; i < 3; i++){
            char letter = grid[i][0];
            
            if (letter == grid[i][1] && letter == grid[i][2] && letter != ' '){
                details[0] = letter;
                details[1] = 'R';
                details[2] = (char)i;
                break;
            }
        }
        return details;
    }

    private char[] checkColumn(){
        char details[] = {' ', ' ', ' '};
        for (int j = 0; j < 3; j++){
            char letter = grid[0][j];
            
            if (letter == grid[1][j] && letter == grid[2][j] && letter != ' '){
                details[0] = letter;
                details[1] = 'C';
                details[2] = (char)j;
                break;
            }
        }
        return details;
    }

    private char[] checkDiag(){
        char details[] = {' ', ' ', ' '};
        char letter1, letter2;
        letter1 = grid[0][0];
        letter2 = grid[0][2];
        if (letter1 == grid[1][1] && letter1 == grid[2][2] && letter1 != ' '){
            details[0] = letter1;
            details[1] = 'M'; // M stands for Major Diagonal
            details[2] = '0';
        }
        else if (letter2 == grid[1][1]  && letter2 == grid[2][0] && letter2 != ' '){
            details[0] = letter2;
            details[1] = 'N'; // N stands for Non-Major (minor) Diagonal
            details[2] = '2';
        }
        return details;
    }

    public void place(char player, int i){
        // place symbol of player in Grid[i] then next person's turn
        int p, q;
        if (i < 3)
        { p = 0; q = i;}
        else if (i < 6)
        {
                p = 1; q = i - 3;
        }
        else
        {
        p = 2; q = i - 6;
        }
        grid[p][q] = player;

        if (current_player == 'x'){
            Grid[i].setIcon(x);
        }
        else 
        {
            Grid[i].setIcon(o);
        }
        // update available table
        available.remove((Integer)i);

        //check winner
        char win[] = new char[3];
        String s = winner();
        win = s.toCharArray();
        if (win[0] != ' '){
            // if there is a winner:
            current_player = ' ';
            // call winner change
            winning_change(win);
        }      
        else{
            if (current_player == 'x')
                current_player = 'o';
            else
                current_player = 'x';
            txt  = current_player+"'s turn" + spaces;
            label.setText(txt);
            if (single == 1){
                ai_gamePlay();
            } 
        }
    }

    public void winning_change(char[] win){
        // highlight winning player grid
        ImageIcon temp;
        int t = win[2]; // i.e row or column number

        if (win[0] != ' ' & win[0]!= 't')
        {
            if (win[0] == 'x'){
                temp = win_x;
                if (player_1 == 'x') winning_player = "Player 1 (X) WINS!!!";
                else winning_player = "Player 2 (X) WINS!!!";
            }
            else{
                temp = win_o;
                if (player_1 == 'o') winning_player = "Player 1 (O) WINS!!!";
                else winning_player = "Player 2 (O) WINS!!!";
            }
        
            if (win[1] == 'R'){
                Grid[3*t].setIcon(temp);
                Grid[3*t + 1].setIcon(temp);
                Grid[3*t + 2].setIcon(temp);
            }
            else if (win[1] == 'C'){
                Grid[t].setIcon(temp);
                Grid[3*1 + t].setIcon(temp);
                Grid[3*2 + t].setIcon(temp);
            }
            else if (win[1] == 'M'){
                Grid[0].setIcon(temp);
                Grid[4].setIcon(temp);
                Grid[8].setIcon(temp);
            }
            else if (win[1] == 'N')
            {
                Grid[2].setIcon(temp);
                Grid[4].setIcon(temp);
                Grid[6].setIcon(temp);
            }
        }       
        else
            winning_player = "It is a TIE!"; // else it is a tie

        Jop(2); // to show victory dialog box
        // destroy pframe here
        pframe.removeAll();
        pframe.setVisible(false); //you can't see me!
        pframe.dispose(); //Destroy the JFrame object
    }

    // this method will override original method which is not ideal hence must put this option in the parent class
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == Exit){
            //close frame
            System.exit(0);
        }
        else
        {
            // must check if empty, allow only then, if not empty, wait till empty spot is clicked
            String desc = ((ImageIcon)((JButton)e.getSource()).getIcon()).getDescription();
            if(desc.equals(blank.getDescription())){
                for (int i = 0; i < 9; i++){
                    if (e.getSource() == Grid[i]){
                        place(current_player, i);
                        break;
                    }
                }
            }
        }
    }
}

class Common_Frame implements ActionListener{

    /* Create main Game-play frame */

    // defining buttons, frames, panels, and constants
    JFrame frame;
    JPanel p, pA;
    JButton Start, Players[], Modes[], Play_again, Exit, Home;
    JButton Grid[] = new JButton[9];
    int SIZE_X, SIZE_Y, OFFSET_X, OFFSET_Y;
    String winning_player;
    
    public Common_Frame()
    {
        SIZE_X = SIZE_Y = 620; OFFSET_X = OFFSET_Y = 20;    
    }

    public void build()
    {
        // builds main menu 

        frame = new JFrame("TIC-TAC-TOE");
        frame.setSize(SIZE_X, SIZE_Y);  
        
        Players = new JButton[2];
        Modes = new JButton[4];

        Start = new JButton("START");
        Exit = new JButton("EXIT");
        Home = new JButton("Home");
        Play_again = new JButton("PLAY AGAIN");
        Players[0] = new JButton("Single Player");
        Players[1] = new JButton("Double Player");
        Modes[0] = new JButton("Easy");
        Modes[1] = new JButton("Medium");
        Modes[2] = new JButton("Hard");
        Modes[3] = new JButton("Impossible");

        p = new JPanel();
        p.setLayout(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(OFFSET_X, OFFSET_Y, OFFSET_X, OFFSET_Y));

        pA = new JPanel();
        pA.setBackground(Color.WHITE);
        
        // for Exit button and Home button, default dimensions and positions on screen
        Exit.setBackground(Color.WHITE);
        Exit.setForeground(Color.BLACK);
        Exit.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 20));
        Exit.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.black));

        Home.setBackground(Color.WHITE);
        Home.setForeground(Color.BLACK);
        Home.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 20));
        Home.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.black));

        frame.setBackground(Color.WHITE);
        frame.setLocationRelativeTo(null); // window in middle of screen
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Home_Screen();
    }

    public void closef()
    {
        if (frame != null)
        {
            frame.setVisible(false); //you can't see me!
            frame.dispose(); //Destroy the JFrame object
        }
    }

    public void Home_Screen()
    {   
        // Using panels and gridbagLayout to align all the elements on frame
        p.setLayout(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(OFFSET_X, OFFSET_Y, OFFSET_X, OFFSET_Y));
        pA.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel heading = new JLabel("Tic-Tac-Toe", SwingConstants.CENTER);
        heading.setForeground(Color.BLACK);
        heading.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 30));
        gbc.insets = new Insets(-100, 0, 0, 0);
        p.add(heading, gbc);
        JPanel HomePanel = new JPanel(new GridBagLayout());
        HomePanel.setBackground(Color.WHITE);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.VERTICAL;        
        Font  f  = new Font(Font.SANS_SERIF,  Font.BOLD, 20);
        
        Start.setBackground(Color.WHITE);
        Start.setForeground(Color.BLACK);
        Start.setFont(f);
        Start.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.black));
                
        //Exit Button already has above format
        Start.setPreferredSize(new Dimension(70,40));
        Exit.setPreferredSize(new Dimension(70,40));
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.ipady = 10;
        gbc.ipadx = 40;
        HomePanel.add(Start, gbc);
        gbc.ipadx = 41;
        HomePanel.add(Exit, gbc);

        gbc.gridheight = 1;
        gbc.gridwidth = 1; 
        gbc.gridy = 3; 
        gbc.gridx = 0; 
        gbc.weighty = 0.5;    
   

        p.add(HomePanel, gbc);
        frame.add(p);

        Start.addActionListener(this);
        Exit.addActionListener(this);
    }
    
    public void Player_Screen()
    {
        // recreate frame for player screen
        frame.remove(p);
        p.removeAll();
        p.repaint();
        p.revalidate();
        
        
        GridBagConstraints gbc = new GridBagConstraints();

        // Using panels and gridbag layout to align elements on frame    
        JPanel p1 = new JPanel(new GridBagLayout());
        //pA = new JPanel();
        p1.setBackground(Color.WHITE);
        pA.setBackground(Color.WHITE);

        //p1
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.ipadx = 30;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 0;
        
        Font  f  = new Font(Font.SANS_SERIF,  Font.BOLD, 20);
        for (int i = 0 ; i <= 1; i ++)
        {
            Players[i].setBackground(Color.WHITE);
            Players[i].setForeground(Color.BLACK);
            Players[i].setFont(f);
            Players[i].setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.black));
            p1.add(Players[i], gbc);
            gbc.gridy += 1;
        }
      
        gbc.gridheight = 2;
        gbc.gridwidth = 1; 
        gbc.gridy = 1; 
        gbc.gridx = 1; 
        gbc.ipady = 0;
        gbc.ipadx = 0;   
    
        p.add(p1, gbc);

        //pA : for HOME and EXIT buttons
        Home.setPreferredSize(new Dimension(70,35));
        Exit.setPreferredSize(new Dimension(70,35));
        Exit.setText("Exit");
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.RIGHT);
        pA.setLayout(fl);
        pA.add(Home); pA.add(Exit);

        // adding Panels to frame
        frame.add(p, BorderLayout.CENTER);
        p.revalidate();
        frame.add(pA, BorderLayout.NORTH);

        Players[0].addActionListener(this); Players[1].addActionListener(this);
        Exit.addActionListener(this);
        Home.addActionListener(this);
    }

    public void Mode_Screen()
    {
        p.setLayout(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(OFFSET_X, OFFSET_Y, OFFSET_X, OFFSET_Y));
        GridBagConstraints gbc = new GridBagConstraints();
            
        JPanel p1 = new JPanel(new GridBagLayout());
        pA = new JPanel();
        p1.setBackground(Color.WHITE);
        pA.setBackground(Color.WHITE);

        //p1
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.ipadx = 30;
        gbc.ipady = 10;
        gbc.gridx = 1;
        gbc.gridy = 0;
        
        Font  f  = new Font(Font.SANS_SERIF,  Font.BOLD, 20);
        for (int i = 0 ; i <= 3; i ++)
        {
            Modes[i].setBackground(Color.WHITE);
            Modes[i].setForeground(Color.BLACK);
            Modes[i].setFont(f);
            Modes[i].setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.black));
            p1.add(Modes[i], gbc);
            gbc.gridy += 1;
            Modes[i].addActionListener(this);
        }
      
        gbc.gridheight = 2;
        gbc.gridwidth = 1; 
        gbc.gridy = 1; 
        gbc.gridx = 1; 
        gbc.ipady = 0;
        gbc.ipadx = 0;   
    
        p.add(p1, gbc);

        //pA : for HOME and EXIT buttons
        Home.setPreferredSize(new Dimension(70,35));
        Exit.setPreferredSize(new Dimension(70,35));
        Exit.setText("Exit");
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.RIGHT);
        pA.setLayout(fl);
        pA.add(Home); pA.add(Exit);

        // adding Panels to frame
        frame.add(p, BorderLayout.CENTER);
        frame.add(pA, BorderLayout.NORTH);
        frame.revalidate();

        Exit.addActionListener(this);
        Home.addActionListener(this);
    }

    public void removePanels()
    {
        // remove panels on Frame to add elements based on desired menu page
        if (frame != null)
        {
        frame.remove(p); frame.remove(pA);
        frame.repaint();
        frame.revalidate();}
        if (p != null){
        p.removeAll();
        p.repaint();
        p.revalidate();}
        if (pA != null){
        pA.removeAll();
        pA.repaint();
        pA.revalidate();}
    }

    public void actionPerformed(ActionEvent e){

        if (e.getSource() == Exit){
            //close frame
            System.exit(0);
        }
        else if(e.getSource() == Home){
            // display goes back to home screen
            //removePanels();
            closef();
            build();
        }
        else if (e.getSource() == Start) {
                Player_Screen();
        }
        else if(e.getSource() == Players[0]){
                // if human vs ai chosen
                Jop(1);// pop up asking player 1 if X or O
                removePanels();
                tictactoe_gui.compPlayer = true;
                Mode_Screen();
            }
            else if(e.getSource() == Players[1]){
                // if human vs human chosen

                Jop(1);// pop up asking player 1 if X or O
                removePanels();

                closef();
                tictactoe_gui.compPlayer = false;
                tictactoe_gui.mode = -1;
                // play gets called
                new Play(false, -1, tictactoe_gui.p1);
            }
            else if(checkMode(e)){
                removePanels();
                // checks the current mode and calls play accordingly
            }
        
    }

    boolean checkMode(ActionEvent e){
        // create play object according to mode chosen
        
        if (e.getSource() == Modes[0]){
            closef();
            tictactoe_gui.mode = 0;
            new Play(true, 0, tictactoe_gui.p1);
        }
        else if (e.getSource() == Modes[1]){
            closef();
            tictactoe_gui.mode = 1;
            new Play(true, 1, tictactoe_gui.p1);
        }
        else if (e.getSource() == Modes[2]){
            closef();
            tictactoe_gui.mode = 2;
            new Play(true, 2, tictactoe_gui.p1);
        }
        else if (e.getSource() == Modes[3]){
            closef();
            tictactoe_gui.mode = 3;
            new Play(true, 3, tictactoe_gui.p1);
        }
        else {
            return false;
        }

        return true;
    }
    
    public void Jop(int i)
    {
        if (i == 1)
        {
            Object[] options = {"X","O"};

            int n = JOptionPane.showOptionDialog(frame,
            "Choose X or O", 
            "Player 1",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,     //do not use a custom Icon
            options,  // the titles of buttons
            options[0]); // default button title

            if (n == JOptionPane.YES_OPTION) tictactoe_gui.p1 = 'x';
            else if (n == JOptionPane.NO_OPTION) tictactoe_gui.p1 = 'o';
            else tictactoe_gui.p1 = ' ';
        }
        else if (i == 2)
        {
            Object[] options = {"Play Again", "Exit"};
            int n = JOptionPane.showOptionDialog(frame,
            winning_player, 
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,     // do not use a custom Icon
            options,  // the titles of buttons
            options[1]); // default button title

            if (n == JOptionPane.YES_OPTION){
                System.out.println("Play again " + tictactoe_gui.p1 + " " + tictactoe_gui.compPlayer + " " + tictactoe_gui.mode);// play in same mode again.
                // close the current frame and call the new Play window 
                new Play(tictactoe_gui.compPlayer, tictactoe_gui.mode, tictactoe_gui.p1);
            } 
            else System.exit(0); // go to exit;
        }
        else if (i == 3)
        {
            Object[] options = {"Yes", "No"};
            int n = JOptionPane.showOptionDialog(frame,
            "Are you sure you want to exit?", 
            "Exit Game?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,     //do not use a custom Icon
            options,  //the titles of buttons
            options[1]); //default button title

            if (n != JOptionPane.YES_OPTION) JOptionPane.getRootFrame().dispose();  //dont exit: closes dialog
            else System.exit(0);// exit;
        }

    }
}
