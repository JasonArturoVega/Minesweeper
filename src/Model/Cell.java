package Model;

public class Cell {

    public Cell()
    {
        this.hasMine = false;
        this.isFlagged = false;
        this.isRevealed = false;
        this.minesAround = 0;
    }

    private boolean hasMine;
    private boolean isFlagged;
    private boolean isRevealed;

    private int minesAround;

    public boolean isHasAMine() {
        return hasMine;
    }

    public boolean isCellFlagged() {return isFlagged;}

    public boolean isRevealed() {
        return isRevealed;
    }

    public int getMinesAround() { return minesAround;}

    public void placeMine()
    {
        hasMine = true;
    }

    public void revealCell()
    {
        isRevealed = true;
    }

    public void toggleFlag()
    {
        if(isRevealed)
            return;

        if(isFlagged)
            isFlagged = false;
        else
            isFlagged = true;
    }

    public void setMinesAround(int mines)
    {
        minesAround = mines;
    }
}
