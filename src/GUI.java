import javax.swing.JFrame;

public class GUI {
    
    private JFrame window;

    public GUI(){
        show();
    }

    public void show(){
        window = new JFrame();
        this.window.setTitle("LabSolver");
        this.window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.window.setSize(960, 540);
        this.window.setLocationRelativeTo(null);
        this.window.setVisible(true);
    }

    public static void main(String[] args){
        GUI i = new GUI();
    }
}


