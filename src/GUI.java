import javax.swing.AbstractButton;
import javax.swing.JFrame;
//import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class GUI {
    
    private JFrame window;

    public GUI(){
        show();
    }

    public void show(){
        //okno programu
        window = new JFrame();
        window.setTitle("LabSolver");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(960, 540);
        window.setLocationRelativeTo(null);

        //pasek z przyciskami
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        window.add(panel, BorderLayout.SOUTH);
        panel.setBackground(Color.LIGHT_GRAY);

        Button b1 = new Button("Wybierz punkty wejscia i wyjscia z labiryntu");
        panel.add(b1);

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            // na bledy
            //JOptionPane.showMessageDialog(window, "blad");
            //odklikniecie przycisku
            AbstractButton abstractButton = (AbstractButton) e.getSource();
            abstractButton.getModel().setPressed(false);
            abstractButton.getModel().setArmed(false);
            }
        });

        Button b2 = new Button("Znajdz najkrotsza sciezke");
        panel.add(b2);


        //panel.setPreferredSize(new Dimension(100, 30));

        window.setVisible(true);
    }

    public static void main(String[] args){
        GUI i = new GUI();
    }
}


