package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Main extends JFrame{
        static int SIZE = 10;
        static int RULE = 0;
        static long DELAY = 500;
        static int STOP = 0;
        static int SEED = 0;
    /**
     * the func that start the game, the board and everything
     * @param args not in use
     * @throws InterruptedException for sleep func
     */
    public static void main(String[] args) throws InterruptedException {
        settings();
        JFrame frame = new JFrame("Conway’s Game of Life.");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(SIZE *65, SIZE *65);
        JButton[][] grid = new JButton[SIZE][SIZE];
        JPanel panel = new JPanel();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = new JButton(/*i+","+j*/);
                grid[i][j].setBackground(Color.white);
                grid[i][j].setPreferredSize(new Dimension(55,55));
                int finalI = i;
                int finalJ = j;
                grid[i][j].addActionListener(e -> {
                    //System.out.println(e.getActionCommand() +")))))))))))))))))))))))))))))))))))))");
                    if (grid[finalI][finalJ].getBackground().equals(Color.white))
                        grid[finalI][finalJ].setBackground(Color.black);
                    else if (grid[finalI][finalJ].getBackground().equals(Color.black))
                        grid[finalI][finalJ].setBackground(Color.white);
                });
                panel.add(grid[i][j]);
            }
        }
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        randomStart(grid);
        while (STOP < 10) {
            TimeUnit.MILLISECONDS.sleep(DELAY);
            nextGen(grid);
            //System.out.println("stop: "+STOP);
        }
        //System.out.println("GAME OVER");
        JOptionPane.showMessageDialog(frame, "Stable State has been reached.\n GAME OVER");
        frame.setVisible(false);
        frame.dispose();
    }
    /**
     * to get random numbers
     * @param min Lower threshold
     * @param max Higher threshold
     * @return a random number in grid limits
     */
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    /**
     * create random number of live cells
     * @param grid to put live cells on
     */
    public static void randomStart(JButton[][] grid){
        //System.out.println(72);
        int rand = getRandomNumber(0, SIZE -1);
        int howMany = switch (SEED) {
            case 1 -> (int) (SIZE * 1.25);
            case 2 -> (int) (SIZE * 2.5);
            default -> rand;
        };
        System.out.println(howMany);
        for (int i = 0; i < howMany; i++) {
            grid[getRandomNumber(0, SIZE -1)][getRandomNumber(0,SIZE -1)].setBackground(Color.black);
        }
    }
    /**
     * calculate the next generation
     * @param grid to put next gen on it
     */
    public static void nextGen(JButton[][] grid){
        //System.out.println(90);
        int[][] past = convert(grid);
        int[][] future = switch (RULE) {
            case 1 -> applyHyperactive(past);
            case 2 -> applyHighLife(past);
            case 3 -> applySpontaneous(past);
            default -> applyConway(past);
        };
        boolean same = true;//stable state detector
        for (int i = 0; i < SIZE && same; i++) {
            for (int j = 0; j < SIZE && same; j++) {
                if (past[i][j] != future[i][j]){
                    same = false;
                    //System.out.println("103: "+ i+","+j);
                }
            }
        }
        if (!same)
            STOP = 0;
        else
            STOP++;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (future[i][j] == 1)
                    grid[i][j].setBackground(Color.black);
                else
                    grid[i][j].setBackground(Color.white);
            }
        }
    }
    /**
     * count live neighbors of specific cell
     * @param i to know which cell
     * @param j to Know which cell
     * @param past to look on it for live neighbors
     * @return count of live neighbors
     */
    public static int countLiveNeighbours(int i,int j, int[][]past){
        int aliveNeighbours = 0,beginI = -1,beginJ = -1,endI = 1,endJ = 1;
        if (i == 0)
            beginI = 0;
        if (j == 0)
            beginJ = 0;
        if (i == SIZE -1)
            endI = 0;
        if (j == SIZE - 1)
            endJ = 0;
        for (int k = beginI; k <= endI; k++)
            for (int l = beginJ; l <= endJ; l++)
                aliveNeighbours += past[i + k][j + l];
        aliveNeighbours -= past[i][j];
        return aliveNeighbours;
    }
    /**
     * apply conway rules on the next generation
     * @param past to apply on
     * @return the future generation
     */
    public static int[][] applyConway(int[][] past){
        int[][] future = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE ; i++)
        {
            for (int j = 0; j < SIZE ; j++)
            {
                int aliveNeighbours = countLiveNeighbours(i,j,past);
                if ((past[i][j] == 1) && (aliveNeighbours < 2))
                    future[i][j] = 0;
                else if ((past[i][j] == 1) && (aliveNeighbours > 3))
                    future[i][j] = 0;
                else if ((past[i][j] == 0) && (aliveNeighbours == 3))
                    future[i][j] = 1;
                else
                    future[i][j] = past[i][j];
            }
        }
        return future;
    }
    /**
     * apply hyperactive rules
     * @param past to apply on
     * @return the future generation
     */
    public static int[][] applyHyperactive(int[][] past){
        int[][] future = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE ; i++)
        {
            for (int j = 0; j < SIZE ; j++)
            {
                int aliveNeighbours = countLiveNeighbours(i,j,past);
                if ((past[i][j] == 1) && (aliveNeighbours < 2))
                    future[i][j] = 0;
                else if ((past[i][j] == 1) && (aliveNeighbours > 5))
                    future[i][j] = 0;
                else if ((past[i][j] == 1) && (aliveNeighbours == 2 || aliveNeighbours == 3))
                    future[i][j] = 0;
                else if ((past[i][j] == 0) && (aliveNeighbours == 3))
                    future[i][j] = 1;
                else if ((past[i][j] == 0) && (aliveNeighbours != 3))
                    future[i][j] = 0;
                else
                    future[i][j] = past[i][j];
            }
        }
        return future;
    }
    /**
     * apply high life rules
     * @param past to apply on
     * @return the future generation
     */
    public static int[][] applyHighLife(int[][] past){
        int[][] future = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE ; i++)
        {
            for (int j = 0; j < SIZE ; j++)
            {
                int aliveNeighbours = countLiveNeighbours(i,j,past);
                if ((past[i][j] == 1) && (aliveNeighbours < 2))
                    future[i][j] = 0;
                else if ((past[i][j] == 1) && (aliveNeighbours > 3))
                    future[i][j] = 0;
                else if ((past[i][j] == 1) && (aliveNeighbours == 2 || aliveNeighbours == 3))
                    future[i][j] = 0;
                else if ((past[i][j] == 0) && (aliveNeighbours == 3))
                    future[i][j] = 1;
                else if ((past[i][j] == 0) && (aliveNeighbours != 3))
                    future[i][j] = 0;
                else
                    future[i][j] = past[i][j];
            }
        }
        return future;
    }
    /**
     * apply spontaneous rules
     * @param past to apply on
     * @return the future generation
     */
    public static int[][] applySpontaneous(int[][] past){
        int[][] future = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE ; i++)
        {
            for (int j = 0; j < SIZE ; j++)
            {
                int aliveNeighbours = countLiveNeighbours(i,j,past);
                if ((past[i][j] == 1) && (aliveNeighbours < 2))
                    future[i][j] = 0;
                else if ((past[i][j] == 1) && (aliveNeighbours > 3))
                    future[i][j] = 0;
                else if ((past[i][j] == 0) && (aliveNeighbours == 3))
                    future[i][j] = 1;
                else if ((past[i][j] == 0) && (getRandomNumber(0,200) == 98))
                    future[i][j] = 1;
                else
                    future[i][j] = past[i][j];
            }
        }
        return future;
    }
    /**
     * convert Button grid to int grid, to easy calculation
     * @param grid of buttons
     * @return grid of int's
     */
    public static int[][] convert(JButton[][] grid){
        int[][] help = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j].getBackground().equals(Color.black)){
                    help[i][j] = 1;
                }else {
                    help[i][j]=0;
                }
            }
        }
        return help;
    }
    /**
     * print ints grid for debugging
     * @param grid to print
     */
    public static void printGrid(int[][] grid){
        System.out.println("=====================");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(grid[i][j]+" ");
            }
            System.out.println();
        }
    }

    /**
     * here we set SIZE, RULES, DELAY, and SEED
     */
    public static void settings(){
        String s = JOptionPane.showInputDialog("Choose number of Cells:", 10);
        System.out.println(s);
        SIZE = Integer.parseInt(s);
        String[] options = {"Very Slow", "Slow", "Normal", "Fast", "Very Fast"};
        String time = (String)JOptionPane.showInputDialog(null, "Choose time between generations:",
                "Time", JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        System.out.println(time);
        long[] delayOptions = {2000,1000,500,200,0};
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(time))
                DELAY = delayOptions[i];
        }
        String[] rules = {"Conway","Hyperactive","High Life","Spontaneous"};
        String game = (String)JOptionPane.showInputDialog(null, "Choose which rules:",
                "Rules", JOptionPane.QUESTION_MESSAGE, null, rules, rules[0]);
        System.out.println(game);
        for (int i = 0; i < rules.length; i++) {
            if (rules[i].equals(game))
                RULE = i;
        }
        String[] seeds = {"Low","Medium","Large"};
        String seed = (String)JOptionPane.showInputDialog(null, "Choose seed:",
                "Seed", JOptionPane.QUESTION_MESSAGE, null, seeds, seeds[0]);
        System.out.println(seed);
        for (int i = 0; i < seeds.length; i++) {
            if (seeds[i].equals(seed))
                SEED = i;
        }
    }
}
