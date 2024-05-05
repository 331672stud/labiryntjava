import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;


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
        JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        window.add(panel1, BorderLayout.SOUTH);
        panel1.setBackground(Color.LIGHT_GRAY);

        Button b1 = new Button("Znajdz najkrotsza sciezke");
        panel1.add(b1);

        Button b2 = new Button("Wybierz punkty wejscia i wyjscia z labiryntu");
        panel1.add(b2);

        window.setVisible(true);
    }

    public static void main(String[] args){
        GUI i = new GUI();
    }
}


