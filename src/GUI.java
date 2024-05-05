import javax.swing.JFrame;
//import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
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

        JButton b1 = new JButton("Wybierz punkty wejscia i wyjscia z labiryntu");
        panel.add(b1);
        b1.setFocusable(false);
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        JButton b2 = new JButton("Znajdz najkrotsza sciezke");
        panel.add(b2);
        b2.setFocusable(false);
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        window.setVisible(true);
    }

    public static void main(String[] args){
        GUI i = new GUI();
    }
}


