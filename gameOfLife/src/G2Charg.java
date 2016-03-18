import java.io.File;
import javax.swing.JFileChooser;

public class G2Charg{

	public G2Charg(){
		
		JFileChooser dialogue = new JFileChooser(new File("./save"));
		File fichier;
	
		if (dialogue.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
			
		    fichier = dialogue.getSelectedFile();
		    G1Board g10 = new G1Board(0,0); g10.setVisible(true);
			g10.Gol.conf("./save/" + fichier.getName());
			
			g10.initG();
		}
	}
} 