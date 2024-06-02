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
        setBackground(Color.GRAY); // Set background color for the whole panel
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    public void addErrorMessage(String message) {
        errorMessages.addFirst(message);

        // Limit the number of messages to MAX_MESSAGES
        if (errorMessages.size() > MAX_MESSAGES) {
            removeErrorMessageLabel();
            errorMessages.removeLast();
        }

        addErrorMessageLabel(message); // Add the newest message label

        // Refresh the UI
        revalidate();
        repaint();
    }

    private void addErrorMessageLabel(String message) {
        JLabel errorMessageLabel = new JLabel(message);
        errorMessageLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, errorMessageLabel.getPreferredSize().height));
        errorMessageLabel.setBackground(Color.WHITE);
        errorMessageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Set black border
        errorMessageLabel.setForeground(Color.BLACK); // Set text color to black
        errorMessageLabel.setOpaque(true);
        add(errorMessageLabel);
    }

    private void removeErrorMessageLabel() {
        Component[] components = getComponents();
        if (components.length > 0) {
            remove(components[0]); // Remove the oldest message label
        }
    }
}
