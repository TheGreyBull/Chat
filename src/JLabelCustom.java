import javax.swing.*;
import java.awt.*;

public class JLabelCustom extends JLabel {
    protected JLabelCustom(String title) {
        super(title);
        this.setFont(new Font("SansSerif", Font.PLAIN, 28));
        this.setForeground(Color.WHITE);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);
    }

    protected JLabelCustom(String title, int constant) {
        super(title, constant);
        this.setFont(new Font("SansSerif", Font.PLAIN, 28));
        this.setForeground(Color.WHITE);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);
    }
}