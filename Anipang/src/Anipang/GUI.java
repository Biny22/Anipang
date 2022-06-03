package Anipang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

class GUI extends JFrame{

    private final JButton[][] board;
    private int[] select;
    private boolean proceed_game;
    private boolean first_game;
    private JLabel score_field;
    private JLabel time_field;
    private JButton start_btn;
    private Game game;
    private JLabel timer;
    private ImageIcon[] inImg;
    private ImageIcon[] outImg;
    private int width;

    GUI()
    {
        first_game = true;
        select = new int[4];
        select[0] = 100;
        select[1] = 100;
        select[2] = 100;
        select[3] = 100;
        board = new JButton[7][7];
        mainGui();
        uploadImage();
    }

    private void mainGui()
    {
        JPanel mainPanel = new JPanel();

        setButton(mainPanel);
        setState(mainPanel);

        mainPanel.setLayout(null);
        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };


        super.addWindowListener(windowListener);
        super.add(mainPanel);
        super.setTitle("애니팡");
        super.setVisible(true);
        super.setSize(900,675);
        super.setLocationRelativeTo(null);
        super.setResizable(false);
    }

    private void setBoard(JPanel mainPanel)
    {
        ActionListener actionListener = e -> {

            for(int i = 0; i < board.length; i++)
            {
                for(int j = 0; j < board[i].length; j++)
                {
                    if(e.getSource() == board[i][j])
                    {
                        if(select[0] != i && select[1] != j) // 중복이 아니라면 (처음 눌렀을 때)
                        {
                            select[0] = i;
                            select[1] = j;
                            btn_Action(i,j); // 십자모양 표시
                        }

                        else
                        {
                            select[2] = i;
                            select[3] = j;

                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    game.switchBoard(select[0],select[1],select[2],select[3]);
                                    setInImage();
                                }
                            });

                            if(select[0] != select[2] || select[1] != select[3])
                                t.start();

                            for(JButton[] buttons : board)
                                for(JButton button : buttons)
                                    button.setEnabled(true);

                            select[0] = 100;
                            select[1] = 100;
                            select[2] = 100;
                            select[3] = 100;
                        }

                    }
                }
            }
        };

        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[i].length; j++)
            {
                board[i][j] = new JButton(""); // 나중에 지우기
                board[i][j].setBounds(25+(75*j),50+(75*i),75,75);
                mainPanel.add(board[i][j]);
                board[i][j].setEnabled(false);
                board[i][j].addActionListener(actionListener);
            }
        }
    }

    private void setButton(JPanel mainPanel)
    {
        start_btn = new JButton("start");

        ActionListener actionListener = e -> {

            if(e.getSource() == start_btn)
            {
                width = 480;
                game = new Game(new Controller(this));

                if(first_game)
                    setBoard(mainPanel);

                enabledBoard();
                setInImage();
            }
        };

        start_btn.addActionListener(actionListener);

        start_btn.setBounds(650,525,100,50);
        start_btn.setFont(new Font("메이플스토리", Font.BOLD, 20));
        mainPanel.add(start_btn);
    }

    private void setState(JPanel mainPanel)
    {
        JLabel score = new JLabel("Score");
        score_field = new JLabel();
        JLabel time = new JLabel("Time");
        time_field = new JLabel();
        timer = new JLabel();

        score.setBounds(600,50,100,50);
        score.setFont(new Font("메이플스토리", Font.BOLD, 20));
        score_field.setBounds(700,50,200,50);
        score_field.setFont(new Font("메이플스토리", Font.BOLD, 20));
        score_field.setForeground(Color.BLACK);

        time.setBounds(600,125,100,50);
        time.setFont(new Font("메이플스토리", Font.BOLD, 20));
        time_field.setBounds(700,125,200,50);
        time_field.setFont(new Font("메이플스토리", Font.BOLD, 20));

        timer.setOpaque(true);

        mainPanel.add(time);
        mainPanel.add(time_field);
        mainPanel.add(score);
        mainPanel.add(score_field);
        mainPanel.add(timer);
    }

    private void enabledBoard()
    {
        proceed_game = !proceed_game;
        first_game = false;
        start_btn.setEnabled(!start_btn.isEnabled());

        for (JButton[] jButtons : board)
        {
            for (JButton jButton : jButtons)
            {
                jButton.setEnabled(proceed_game);
            }
        }
    }

    private void btn_Action(int i, int j)
    {
        for(JButton[] buttons : board)
            for(JButton button : buttons)
                button.setEnabled(false);


        if(i == 0)
        {
            if(j == 0)
            {
                board[i][j].setEnabled(true);
                board[i+1][j].setEnabled(true);
                board[i][j+1].setEnabled(true);
            }

            else if(j == board.length-1)
            {
                board[i][j].setEnabled(true);
                board[i+1][j].setEnabled(true);
                board[i][j-1].setEnabled(true);
            }

            else
            {
                board[i][j].setEnabled(true);
                board[i][j+1].setEnabled(true);
                board[i][j-1].setEnabled(true);
                board[i+1][j].setEnabled(true);
            }
        }

        else if(i == board.length-1)
        {
            if(j == 0)
            {
                board[i][j].setEnabled(true);
                board[i-1][j].setEnabled(true);
                board[i][j+1].setEnabled(true);
            }

            else if(j == board.length-1)
            {
                board[i][j].setEnabled(true);
                board[i-1][j].setEnabled(true);
                board[i][j-1].setEnabled(true);
            }

            else
            {
                board[i][j].setEnabled(true);
                board[i][j+1].setEnabled(true);
                board[i][j-1].setEnabled(true);
                board[i-1][j].setEnabled(true);
            }
        }

        else
        {
            if(j == 0)
            {
                board[i][j].setEnabled(true);
                board[i+1][j].setEnabled(true);
                board[i-1][j].setEnabled(true);
                board[i][j+1].setEnabled(true);
            }

            else if(j == board.length-1)
            {
                board[i][j].setEnabled(true);
                board[i+1][j].setEnabled(true);
                board[i-1][j].setEnabled(true);
                board[i][j-1].setEnabled(true);
            }

            else
            {
                board[i][j].setEnabled(true);
                board[i+1][j].setEnabled(true);
                board[i-1][j].setEnabled(true);
                board[i][j-1].setEnabled(true);
                board[i][j+1].setEnabled(true);
            }
        }
    }

    void gameSet()
    {
        System.out.println("끝!");
        JOptionPane.showMessageDialog(null,""+game.getScore(),"점수",JOptionPane.PLAIN_MESSAGE);
        enabledBoard();
    }

    void setScore()
    {
        score_field.setText(""+game.getScore());
    }

    void setTime(long time)
    {
        setScore();
        timer.setBackground(new Color(80, 156, 199, 255));
        timer.setBounds(25,580,480,50);

        if(time <= 10)
            timer.setBackground(new Color(255, 109, 109,255));

        width -= 8;
        time_field.setText("" +time);
        timer.setBounds(25,580,width,50);
    }

    void setInImage()
    {
        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[i].length; j++)
            {
                int animal = game.getAnimal(i,j);
                board[i][j].setIcon(inImg[animal]);
            }
        }
    }

    void setOutImage(HashSet<ArrayList<Integer>> set)
    {
        for(ArrayList<Integer> al : set)
        {
            int animal = game.getAnimal(al.get(0),al.get(1));
            board[al.get(0)][al.get(1)].setIcon(outImg[animal]);
        }
    }

    void uploadImage()
    {
        inImg = new ImageIcon[11];
        outImg = new ImageIcon[11];
        for(int i = 0; i < inImg.length; i++)
        {
            String str =  i + ".png";
            inImg[i] = new ImageIcon(Objects.requireNonNull(GUI.class.getResource("/InAnimal/" + str)));
            outImg[i] = new ImageIcon(Objects.requireNonNull(GUI.class.getResource("/OutAnimal/" + str)));
        }
    }
}

