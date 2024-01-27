import javax.swing.*;
import java.awt.*;

public class ChatNameButton extends JButtonCustom {
    protected ChatNameButton(String title) {
        super(title);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
    }
}
