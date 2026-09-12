package Ui;

import javax.swing.*;
import java.awt.*;

public class CellButton extends JButton {

    public CellButton(int buttonRow, int buttonColumn)
    {
        this.buttonRow = buttonRow;
        this.buttonColumn = buttonColumn;

        //Get the correct sprites for the button
        regularIcon = new ImageIcon("Sprites/Regular_Cell.png");
        revealedIcon = new ImageIcon("Sprites/Revealed_Cell.png");
        flaggedIcon = new ImageIcon("Sprites/Flagged_Cell.png");
        mineIcon = new ImageIcon("Sprites/Mine_Cell.png");

        setIcon(regularIcon);

        textInButtonFont = new Font("Britannic Negrita", Font.BOLD, 30);
        setFont(textInButtonFont);

        //Adjust the text and icon of the button to be perfectly in the center and without margin
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.CENTER);
        setIconTextGap(0);
        setMargin(new Insets(0, 0, 0, 0));
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);

        setFocusable(false);

        buttonCellState = cellState.COVERED;
    }

    public enum cellState{COVERED, REVEALED, FLAG, MINE};
    private cellState buttonCellState;

    private final Font textInButtonFont;

    private final int buttonRow;
    private final int buttonColumn;

    private final ImageIcon regularIcon;
    private final ImageIcon revealedIcon;
    private final ImageIcon flaggedIcon;
    private final ImageIcon mineIcon;

    public int getButtonRow() {
        return buttonRow;
    }

    public int getButtonColumn() {return buttonColumn;}

    public void changeButtonToFlag()
    {
        buttonCellState = cellState.FLAG;

        setForeground(Color.red);
        setIcon(flaggedIcon);
    }

    public void changeButtonToCovered()
    {
        buttonCellState = cellState.COVERED;
        setText("");
        setIcon(regularIcon);
    }

    public void changeButtonToMine()
    {
        setText("");
        setIcon(mineIcon);
        buttonCellState = cellState.MINE;
        //setEnabled(false);
    }

    public void changeButtonToEmpty(int minesAround)
    {
        Color textColor = null;

        //Change text in the button
        if(minesAround == 0)
            setText("");

        if(minesAround > 0)
        {
            setText(String.valueOf(minesAround));

            //Color depending on how many mines have in adjacent panels
            textColor = switch (minesAround) {
                case 1 -> (Color.blue);
                case 2 -> (Color.green);
                case 3 -> (Color.red);
                default -> textColor;
            };
        }

        if(minesAround > 3)
        {
            setText("3");
            textColor =(Color.red);
        }

        setIcon(revealedIcon);
        setForeground(textColor);
        buttonCellState = cellState.REVEALED;
        //setEnabled(false);
    }
}
