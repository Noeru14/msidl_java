import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class G0Menu extends JFrame{
	private JButton B1, B2, B3, B4, B5;
	
	public G0Menu(){
		// JFrame
		this.setTitle("Menu");
		this.setSize(1000,750);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// JPanel
		JPanel pan0 = new JPanel();
		JPanel pan1 = new JPanel();
		JPanel pan2 = new JPanel();
		JPanel pan3 = new JPanel();
		JPanel pan4 = new JPanel();
		
		this.add(pan0,BorderLayout.NORTH);
		this.add(pan1,BorderLayout.CENTER);
		this.add(pan2,BorderLayout.SOUTH);
		this.add(pan3,BorderLayout.EAST);
		this.add(pan4,BorderLayout.WEST);
		
		pan0.setPreferredSize(new Dimension(100,100));
		pan2.setPreferredSize(new Dimension(100,100));
		pan3.setPreferredSize(new Dimension(100,100));
		pan4.setPreferredSize(new Dimension(100,100));
		
		pan0.setBackground(Color.CYAN);
		pan2.setBackground(Color.CYAN);
		pan3.setBackground(Color.WHITE);
		pan4.setBackground(Color.WHITE);
		
		// Haut du menu
		ImageIcon imageG01 = new ImageIcon("./img/bg1.jpg");
		JLabel imageLabel01 = new JLabel(imageG01);
		pan0.add(imageLabel01);
		
		// Bas du menu
		ImageIcon imageG02 = new ImageIcon("./img/bg2.jpg");
		JLabel imageLabel02 = new JLabel(imageG02);
		pan2.add(imageLabel02);
		
		pan1.setLayout(new GridLayout(5,1));
		pan1.setPreferredSize(new Dimension(500,500));
		
		// Bouton et Fonts
		Font F1 = new Font("Calibri", Font.BOLD, 40);
		
		B1 = new JButton();
		B2 = new JButton();
		B3 = new JButton();
		B4 = new JButton();
		B5 = new JButton();
		
		B1.addActionListener(new g0AL());
		B2.addActionListener(new g0AL());
		B3.addActionListener(new g0AL());
		B4.addActionListener(new g0AL());
		B5.addActionListener(new g0AL());
		
		B1.setText("Population init. définie");
		B2.setText("Population init. aléatoire");
		B3.setText("Choisir conf. init.");
		B4.setText("Charger une population init.");
		B5.setText("Quitter");
		
		B1.setFont(F1);
		B2.setFont(F1);
		B3.setFont(F1);
		B4.setFont(F1);
		B5.setFont(F1);
			
		pan1.add(B1);
		pan1.add(B2);
		pan1.add(B3);
		pan1.add(B4);
		pan1.add(B5);
		
		this.setVisible(true);
	}	
	
	// selon le bouton actionné, renvoie vers une fonctionnalité
	private class g0AL implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Object source = (JButton) e.getSource();
			JButton boutonSource = (JButton) source;
			JFrame fenetreSource = (JFrame) boutonSource.getTopLevelAncestor(); // récupère Graph0
			
			if(source == B1){
				// population initiale définie par l'utilisateur
				G1Board g10 = new G1Board(0,0); g10.setVisible(true); fenetreSource.dispose();
			}	
				// population initiale aléatoire
			else if(source == B2){
				G1Board g11 = new G1Board(1,0); g11.setVisible(true); fenetreSource.dispose();	
			}
				// choisir configuration initiale
			else if(source == B3){
				G1Board g12 = new G1Board(0,1); g12.setVisible(true); fenetreSource.dispose();	
			}
			else if(source == B4){
				// charger partie
				G2Charg g2 = new G2Charg(); fenetreSource.dispose();
			}
			else {
				// quitter
				G3Quit g3 = new G3Quit(); g3.setVisible(true); fenetreSource.dispose();
			}
		}
	}
}