import javax.swing.*;
import java.awt.*;

public class JButtonCustom extends javax.swing.JButton {
    protected JButtonCustom(String title) {
        super(title);
        this.setFocusable(false);
        this.setFont(new Font("SansSerif", Font.BOLD, 24));
        this.setBackground(Frame.lightBgColor);
        this.setForeground(Color.WHITE);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(8, 8, 8, 8),
                BorderFactory.createLineBorder(Color.WHITE, 1)
        ));
    }
}