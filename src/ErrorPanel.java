import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class ErrorPanel extends JPanel {
    private static final int MAX_MESSAGES = 50;
    private LinkedList<String> errorMessages;

    public ErrorPanel() {
        errorMessages = new LinkedList<>();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(200, getHeight()));
        setBackground(Color.GRAY); 
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    public void addErrorMessage(String message) {
        errorMessages.addFirst(message);

        if (errorMessages.size() > MAX_MESSAGES) {
            removeErrorMessageLabel();
            errorMessages.removeLast();
        }

        addErrorMessageLabel(message); 

        revalidate();
        repaint();
    }

    private void addErrorMessageLabel(String message) {
        JLabel errorMessageLabel = new JLabel(message);
        errorMessageLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, errorMessageLabel.getPreferredSize().height));
        errorMessageLabel.setBackground(Color.WHITE);
        errorMessageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
        errorMessageLabel.setForeground(Color.BLACK); 
        errorMessageLabel.setOpaque(true);
        add(errorMessageLabel);
    }

    private void removeErrorMessageLabel() {
        Component[] components = getComponents();
        if (components.length > 0) {
            remove(components[0]); 
        }
    }
}
