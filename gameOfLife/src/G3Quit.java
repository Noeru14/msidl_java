import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class G3Quit extends JFrame{
	JLabel labQ = new JLabel("<html>Merci d'avoir utilisé l'application !<br>Extinction dans 2 secondes...</html>", SwingConstants.CENTER);
	
	public G3Quit(){
		this.setSize(400, 90);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Quitter...");
		
		JPanel pan00 = new JPanel ();
		
		pan00.add(labQ);
		this.add(pan00);
		
		TimerTask task = new TimerTask(){
			@Override
			public void run(){
				extinction();
			}	
		};
				
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(task, 2000, 420);
	}

	public void extinction(){
		this.dispose();
	}
}