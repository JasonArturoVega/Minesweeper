package Ui;

import Logic.GameManager;
import Model.Board;
import Model.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameWindow extends JFrame {

    public GameWindow(int sizeX, int sizeY, String titleOnTop, Board mainBoard, GameManager mainGameManager)
    {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.mainBoard = mainBoard;
        this.mainGameManager = mainGameManager;

        setTitle(titleOnTop);
        setResizable(false);
        setExtendedState(Frame.NORMAL);
        setLayout(null);

        textAndRestartFont = new Font("Britannic Negrita", Font.BOLD, 25);

        createBoardPanel();
        amountOfClicks = 0;
    }

    private Board mainBoard;
    private final GameManager mainGameManager;

    private CellButton[][] buttonsOnPanel;
    private JButton restartButton;
    private TextAbove mainText;
    private final Font textAndRestartFont;

    private int sizeX;
    private int sizeY;

    private int amountOfClicks;

    public void setSizeAndPosition()
    {
        //Width and length
        setSize(sizeX, sizeY);

        //Window in the center of the screen
        setLocationRelativeTo(null);
    }

    private void createBoardPanel()
    {
        int rowsInPanel = mainBoard.getRows();
        int columnsInPanel = mainBoard.getColumns();

        //Create text and restart button
        JPanel textAndRestartPanel = new JPanel();
        textAndRestartPanel.setBounds(0,0,600,50);

        mainText = new TextAbove(0, 0, 300, 50);
        mainText.setText("Remaining panels: 0/" + mainBoard.getCellsWithoutMines());
        textAndRestartPanel.setBackground(Color.BLACK);
        mainText.setFont(textAndRestartFont);
        textAndRestartPanel.add(mainText);

        restartButton = new JButton();
        restartButton.setBounds(500,0,100, 50);
        restartButton.setText("Restart");
        textAndRestartPanel.add(restartButton);
        restartButton.setFont(textAndRestartFont);

        restartButton.addActionListener(e -> restartGame());

        //Create panel for the cells
        JPanel mainPanel = new JPanel(new GridLayout(rowsInPanel, columnsInPanel, 3, 3));
        mainPanel.setBounds(0, 50, 600, 600);

        //Create buttons for the panel
        buttonsOnPanel = new CellButton[rowsInPanel][columnsInPanel];

        for(int i = 0; i < rowsInPanel; i++)
        {
            for(int j = 0; j < columnsInPanel; j++)
            {
                //Assign correct row and column
                CellButton button = new CellButton(i, j);
                buttonsOnPanel[i][j] = button;
                mainPanel.add(button);

                //Assign the event in the button for the left click
                button.addActionListener(e -> onLeftClickButtonEvent(button.getButtonRow(), button.getButtonColumn()));

                //Assign the event in the button for the right click
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(SwingUtilities.isRightMouseButton(e))
                            onRightClickButtonEvent(button.getButtonRow(), button.getButtonColumn());

                    }
                });
            }
        }

        //Add panels of the text and cells to the window
        add(textAndRestartPanel);
        add(mainPanel);
    }

    public void onRightClickButtonEvent(int row, int column)
    {
        //Cant play if you won or lost
        if(mainGameManager.getMainState() != GameManager.MainState.PLAYING)
            return;

        Cell cellClicked = mainBoard.getCell(row, column);

        //You can Not click on a cell that has already revealed
        if(cellClicked.isRevealed())
            return;

        //Activate of deactivate flag
        cellClicked.toggleFlag();

        //Change icon on the buttons
        if(cellClicked.isCellFlagged())
            buttonsOnPanel[row][column].changeButtonToFlag();
        else
            buttonsOnPanel[row][column].changeButtonToCovered();
    }

    public void onLeftClickButtonEvent(int row, int column)
    {
        //Cant play if you won or lost
        if(mainGameManager.getMainState() != GameManager.MainState.PLAYING)
            return;

        //Place mines after you click for the first time
        if(amountOfClicks == 0)
        {
            mainBoard.placeMines(row, column);
            mainBoard.calculateAdjacentMines();
        }

        Cell cellClicked = mainBoard.getCell(row, column);

        //You can Not click on a cell that has already revealed
        if(cellClicked.isRevealed() || cellClicked.isCellFlagged())
            return;

        //Reveal all the mines on the board
        if(cellClicked.isHasAMine())
        {
            for(int i = 0; i < mainBoard.getRows(); i++)
            {
                for (int j = 0; j < mainBoard.getColumns(); j++)
                {
                    Cell currentCell = mainBoard.getCell(i, j);

                    if(currentCell.isHasAMine())
                    {
                        currentCell.revealCell();
                        buttonsOnPanel[i][j].changeButtonToMine();
                    }

                }
            }

            //Lose
            mainGameManager.gameOver();
            mainText.setText("Game Over");

            return;
        }

        //Reveal cell and show adjacent mines
        mainBoard.revealCurrentAndAdjacentCells(row, column);
        updateUiBoard();

        amountOfClicks++;
    }

    private void updateUiBoard()
    {
        //Check all the buttons on the board and reveal the correct ones
        for(int i = 0; i < mainBoard.getRows(); i++)
        {
            for(int j = 0; j < mainBoard.getColumns(); j++)
            {
                Cell currentCell = mainBoard.getCell(i, j);

                //Only reveal a cell if it was revealed and does NOT have a mine
                if(currentCell.isRevealed() && !currentCell.isHasAMine())
                    buttonsOnPanel[i][j].changeButtonToEmpty(currentCell.getMinesAround());
            }
        }

        //Show on text the remaining cells
        if(mainBoard.getCellsRevealed() == mainBoard.getCellsWithoutMines())
            mainText.setText("Winner");
        else
            mainText.setText("Remaining panels: " + mainBoard.getCellsRevealed() + "/" +
                             mainBoard.getCellsWithoutMines());
    }

    void restartGame()
    {
        mainGameManager.restart();

        //Reset text
        mainText.setText("Remaining panels: 0/" + mainBoard.getCellsWithoutMines());

        //Create new board
        mainBoard = new Board(12, 12, 20);
        getContentPane().removeAll();
        amountOfClicks = 0;

        createBoardPanel();

        revalidate();
        repaint();
    }
}
