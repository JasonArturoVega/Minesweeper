package Ui;

import javax.swing.*;
import java.awt.*;

public class TextAbove extends JTextField {

    public TextAbove(int posX, int posY, int width, int height)
    {
        setBounds(posX, posY, width, height);

        setForeground(Color.white);
        setBackground(Color.BLACK);
        setFocusable(false);
    }

    Font textAboveFont;

    public void changeText(String newText)
    {
        setText(newText);
    }
}
