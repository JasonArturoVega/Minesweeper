package Main;

import Logic.GameManager;
import Model.Board;
import Ui.GameWindow;

import javax.swing.*;
import java.awt.*;

public class GameExecution {

    static void main(String[] args) {

        //Create board with the panel and mines
        Board mainBoard = new Board(12, 12, 20);

        //Create game logic
        GameManager mainGameManager = new GameManager();

        GameWindow window = new GameWindow(615, 685, "Minesweeper", mainBoard, mainGameManager);
        window.setSizeAndPosition();

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.getContentPane().setBackground(Color.black);
    }
}
