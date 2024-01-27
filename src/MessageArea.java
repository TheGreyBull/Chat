import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class MessageArea extends JTextArea {
    protected MessageArea(String text, int rows, int column, boolean isYourself, String time) {
        super(text, rows, column);
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setEditable(false);
        this.setBackground(Frame.darkBgColor);
        this.setFont(new Font("SansSerif", Font.PLAIN, 24));
        this.setForeground(Color.WHITE);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.WHITE),
                        isYourself ? "Tu - " + time : time,
                        TitledBorder.LEFT,
                        TitledBorder.DEFAULT_POSITION,
                        new Font("SansSerif", Font.BOLD, 14),
                        Color.LIGHT_GRAY
                ),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
    }
}
