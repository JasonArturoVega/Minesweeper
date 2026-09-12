package Model;

import Logic.GameManager;
import java.util.Random;

public class Board {

    public Board(int rows, int columns, int minesInTotal)
    {
        this.rows = rows;
        this.columns = columns;
        this.minesInTotal = minesInTotal;

        //Calculate the amount of cells without a mine
        cellsWithoutMines = (rows * columns) - minesInTotal;

        cellsRevealed = 0;

        generateBoard();
    }

    public int getMinesInTotal() {
        return minesInTotal;
    }

    public int getCellsWithoutMines() {
        return cellsWithoutMines;
    }

    public int getCellsRevealed() {
        return cellsRevealed;
    }

    Cell[][] cellsInTheBoard;

    private final int rows; //Horizontal
    private final int columns; //Vertical

    private final int minesInTotal;
    private final int cellsWithoutMines;
    private int cellsRevealed;

    private void generateBoard()
    {
        //Create grid for the cells
        cellsInTheBoard = new Cell[rows][columns];

        for(int i = 0; i < rows; i ++)      //Lines horizontally
            for(int j = 0; j < columns; j ++)  //Lines vertically
                cellsInTheBoard[i][j] = new Cell();
    }

    public void placeMines(int firstClickedRow, int firstClickedColumn)
    {
        Random randomNumbers = new Random();
        int minesPlaced = 0;

        while (minesPlaced < minesInTotal)
        {
            //Choose a random row between 0 and the max amount of rows
            int randomRow = randomNumbers.nextInt(rows);

            //Choose a random column between 0 and the max amount of columns
            int randomColumn = randomNumbers.nextInt(columns);

            Cell selectedCell = cellsInTheBoard[randomRow][randomColumn];

            //You can NOT place a mine if already that cell has a mine, and also if it was the first cell to click
            //Include also the adjacent ones
            boolean isSafeArea = Math.abs(randomRow - firstClickedRow) <= 1 &&
                                 Math.abs(randomColumn - firstClickedColumn) <= 1;
            boolean randomCellAlreadyHasAMine = selectedCell.isHasAMine();

            if(!isSafeArea && !randomCellAlreadyHasAMine)
            {
                selectedCell.placeMine();
                minesPlaced ++;
            }
        }
    }

    public void calculateAdjacentMines()
    {
        //Goes to each cell in the board
        for(int currentRow = 0; currentRow < rows; currentRow++)
        {
            for(int currentColumn = 0; currentColumn < columns; currentColumn++)
            {
                //The cell does NOT calculate if that cell has a mine
                if(cellsInTheBoard[currentRow][currentColumn].isHasAMine())
                    continue;

                int minesFound = 0;

                //In each cell it checks the cells around
                for(int i = -1; i <= 1; i++)
                {
                    for(int j = -1; j <= 1; j++)
                    {
                        //Ignore the cell itself
                        if(i == 0 && j == 0)
                            continue;

                        int adjacentRow = currentRow + i;
                        int adjacentColumn = currentColumn + j;

                        //The cell needs to be valid. Example -1, -5 is not valid because it starts in 0,0
                        if(isValidPosition(adjacentRow, adjacentColumn))
                        {
                            if(cellsInTheBoard[adjacentRow][adjacentColumn].isHasAMine())
                                minesFound++;
                        }
                    }
                }

                //Give the info to the cell
                cellsInTheBoard[currentRow][currentColumn].setMinesAround(minesFound);
            }
        }
    }

    public void revealCurrentAndAdjacentCells(int row, int column)
    {
        if(!isValidPosition(row, column))
            return;

        Cell currentCell = cellsInTheBoard[row][column];

        //Don't reveal if it has a mine, it was already reveal or if it is flagged
        if(currentCell.isRevealed() || currentCell.isHasAMine() || currentCell.isCellFlagged())
            return;

        currentCell.revealCell();
        cellsRevealed++;
        System.out.println(cellsRevealed + "/" + cellsWithoutMines);

        //Win condition
        if(cellsRevealed == cellsWithoutMines)
        {
            GameManager.win();
            return;
        }

        //Only reveal other panels if it does NOT have mines around
        if(currentCell.getMinesAround() > 0)
            return;

        //Check adjacent panels with cero mines
        for(int i = -1; i <= 1; i++)
        {
            for(int j = -1; j <= 1; j++)
            {
                //Don't check current cell
                if(i == 0 && j == 0)
                    continue;

                revealCurrentAndAdjacentCells(row + i, column + j);
            }
        }
    }

    public Cell getCell(int currentRow, int currentColumn)
    {
        return cellsInTheBoard[currentRow][currentColumn];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public boolean isValidPosition(int currentRow, int currentColumn)
    {
        return currentRow >= 0 && currentRow < rows && currentColumn >= 0 && currentColumn < columns;
    }
}
