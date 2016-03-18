import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class G1Board extends JFrame{
	ArrayList<JPanel> board = new ArrayList<JPanel>();
	ArrayList<JPanel> boardT = new ArrayList<JPanel>();
	Board Gol = new Board();
	
	JMenuItem menuItem10, menuItem11, menuItem12, menuItem20, menuItem21, menuItem30, menuItem31;
	
	// variable indiquant si génération aléatoire
	int r = 0;
	
	// variable indiquant si chargement d'une configuration aléatoire
	int c = 0;

	// timers pour enchaînement des générations
	TimerTask task;
	Timer timer;
	
	public G1Board(int rd, int co){
		this.r = rd;
		this.c = co;

		// définition d'une population aléatoire
		if(this.r == 1){
			Gol.rdm();
		}
		
		// chargement d'une configuration initiale
		if(this.c == 1){
			// poss : Pulsar, Pentadecathlon, Lightweight spaceship, Gosper glider gun
			String[] possibilities = {"Pulsar", "Pentadecathlon", "LightweightSpaceships", "GosperGliderGun"};
			
			String res = (String) JOptionPane.showInputDialog(
					this, "Choisir une configuration initiale : ", "Configuration initiale...", JOptionPane.PLAIN_MESSAGE, null, possibilities, "Pulsar"
					);
			
			Gol.conf("./conf/"+ res + ".txt");
		}
		
		// JFrame
		this.setTitle("Game of life");
		this.setSize(1200, 800);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// creer le menu bar
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menu1 = new JMenu("Evolution");
		menuBar.add(menu1);
		
		menuItem10 = new JMenuItem("Automatique");
		menuItem10.addActionListener(new g1AL());
		menuItem11 = new JMenuItem("Passer");
		menuItem11.addActionListener(new g1AL());
		menuItem12 = new JMenuItem("Arrêt");
		menuItem12.addActionListener(new g1AL());
		menu1.add(menuItem10);
		menu1.add(menuItem11);
		menu1.add(menuItem12);
		
		JMenu menu2 = new JMenu("Fichier");
		menuBar.add(menu2);
		
		menuItem20 = new JMenuItem("Sauvegarder");
		menuItem20.addActionListener(new g1AL());
		menuItem21 = new JMenuItem("Charger");
		menuItem21.addActionListener(new g1AL());
		menu2.add(menuItem20);
		menu2.add(menuItem21);
		
		JMenu menu3 = new JMenu("Quitter...");
		menuBar.add(menu3);
		
		menuItem30 = new JMenuItem("Retour au menu");
		menuItem30.addActionListener(new g1AL());
		menuItem31 = new JMenuItem("Quitter");
		menuItem31.addActionListener(new g1AL());
		
		menu3.add(menuItem30);
		menu3.add(menuItem31);
		
		this.setJMenuBar(menuBar);
		
		JPanel jPanel = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		jPanel.setLayout(gbl);
		GridBagConstraints gbc = new GridBagConstraints();
		
		// initialisation des cellules vivantes et mortes
		this.initD();
		
		// construction du plateau
		int z = 0;
		for(int y = 0 ; y < 40 ; y++){
			for(int x = 0 ; x < 60 ; x++){
				gbc.gridx = x;
			    gbc.gridy = y;
			    gbc.gridwidth = 1;
			    gbc.gridheight = 1;
			    
			    jPanel.add(board.get(z), gbc);
			    z++;
			}
		}
		
		this.setContentPane(jPanel);
		
		this.setVisible(true);
	}
	
	// création, initialisation et ajout d'un listener
	public void initD(){
		int z = 0;
		
		for (int y = 0; y < 40 ; y++){
			for (int x = 0; x < 60 ; x++){
				JPanel pCase = new JPanel();
				pCase.setPreferredSize(new Dimension(15, 15));
				pCase.setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.BLACK));
				
				if(Gol.gol[x][y] == 0){
					pCase.setBackground(Color.WHITE);
				}
				else{
					pCase.setBackground(Color.BLACK);
				}

				z++;
				pCase.addMouseListener(new g1ML(pCase, z));
				board.add(pCase);
			}
		}
	}
	
	// mise à jour de la couleur des cellules
	public void initG(){
		int z = 0;
		
		for (int y = 0; y < 40 ; y++){
			for (int x = 0; x < 60 ; x++){
				
				if(Gol.gol[x][y] == 0){
					board.get(z).setBackground(Color.WHITE);
				}
				else if(Gol.gol[x][y] == 1){
					board.get(z).setBackground(Color.GREEN);
				}
				else if(Gol.gol[x][y] == 2){
					board.get(z).setBackground(Color.BLUE);
				}
				else if(Gol.gol[x][y] == 3){
					board.get(z).setBackground(Color.RED);
				}
				else if(Gol.gol[x][y] == 4){
					board.get(z).setBackground(Color.YELLOW);
				}

				z++;
			}
		}
	}
	
	// gestion d'une génération
	public void generationG(){
		Gol.generation();
		this.initG();
	}
	
	// gestion d'une évolution automatique, 4 par seconde
	public void start(){
		this.task = new TimerTask(){
			@Override
			public void run(){
				generationG();
			}	
		};
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(task, 200, 120);
	}
	
	// arrête une exéution automatique
	public void pause(){
		this.timer.cancel();
	}
	
	// sauvegarde l'évolution en cours
	public void save(){
		String [] leaveNot = {"OUI", "NON"};
		String answerSave = (String) JOptionPane.showInputDialog(null, "Souhaitez-vous sauvegarder ?", 
				"Sauvegarde...", JOptionPane.QUESTION_MESSAGE, null, leaveNot, leaveNot[0]);
		
		// si on annule ou ferme la fenêtre
		if (answerSave != null){
			// si on choisit "OUI"
			if (answerSave.equals("OUI")){
				JFileChooser c = new JFileChooser(new File("./save"));
				
				// "Save" dialog:
				int rVal = c.showSaveDialog(null);
				
				if (rVal == JFileChooser.APPROVE_OPTION) {
			        
					try{
						PrintWriter writer = new PrintWriter(c.getSelectedFile(), "UTF-8");
						
						for (int y = 0; y < 40 ; y++){
							for (int x = 0; x < 60 ; x++){
								if(x == 59){
									writer.println(Gol.gol[x][y]);
								}
								else{
									writer.print(Gol.gol[x][y]);
								}
							}
						}
	
						writer.close();	
					} catch (Exception e){
						e.printStackTrace();
					}
			      }
			}
		}
	}

	public void load(){
		JFileChooser dialogue = new JFileChooser(new File("./save"));
		File fichier;
		
		if (dialogue.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
			
		    fichier = dialogue.getSelectedFile();
			this.Gol.conf("./save/" + fichier.getName());
			this.initG();
		}
	}
	
	private class g1AL implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			Object source = (JMenuItem) e.getSource();
			JMenuItem menuItemSource = (JMenuItem) source;
			JPopupMenu popupMenu = (JPopupMenu) menuItemSource.getParent();
			Component invoker = popupMenu.getInvoker();
			JComponent invokerAsJComponent = (JComponent) invoker;
			JFrame fenetreSource = (JFrame) invokerAsJComponent.getTopLevelAncestor();

				// Démarrer générations (automatique)
			if(source == menuItem10){
				start();
			}	
			
				// Passer à la prochaine génération
			else if(source == menuItem11){
				generationG();
			}

				// Arrêter
			else if(source == menuItem12){
				pause();
			}
			
				// Sauvegarder
			else if(source == menuItem20){
				save();
			}
			
			else if(source == menuItem21){
				load();
			}
			
				// Retour au menu
			else if(source == menuItem30){
				G0Menu g0 = new G0Menu(); g0.setVisible(true); fenetreSource.dispose();
			}
			
				// Quitter
			else if(source == menuItem31){
				G3Quit g3 = new G3Quit(); g3.setVisible(true); fenetreSource.dispose();
			}
		}
	}
	
	// permet la définition manuelle de cellules vivantes ou non
	private class g1ML implements MouseListener {
		private JPanel panel;
		private int pos;
		
		public g1ML(JPanel panel, int z){
			this.panel = panel;
			this.pos = z;
		}
		
		public void mouseClicked(MouseEvent e) {
			int state;
			
			if(panel.getBackground().equals(Color.WHITE)){
				panel.setBackground(Color.BLACK);
				state = 1;
			}
			else{
				panel.setBackground(Color.WHITE);
				state = 0;
			}
			Gol.graphToBoard(pos-1, state);
		}

		public void mouseEntered(MouseEvent arg0){

		}

		public void mouseExited(MouseEvent arg0){

		}

		public void mousePressed(MouseEvent arg0){
		
		}

		public void mouseReleased(MouseEvent arg0){

		}
	}
}