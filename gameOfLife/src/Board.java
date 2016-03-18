import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import javax.swing.JOptionPane;

public class Board{
	// dimensions
	private final int xg = 60;
	private final int yg = 40;
	
	// generation actuelle
		// plateau principal, sert a compter le nbre de voisins pour prevoir la generation n+2
	int gol[][] = new int [xg][yg];
	
	// copie de la generation actuelle
		// sert a determiner les cellules vivantes et mortes de la generation actuelle
		// permet de realiser une copie sur laquelle on peut compter le nbre de voisins
		// pour la generation n+1
	private int golCopy[][] = new int [xg][yg];
	
	// etat des cellules
	public static final int BLANC = 0;
	public static final int VERT = 1;
	public static final int BLEU = 2;
	public static final int ROUGE = 3;
	public static final int JAUNE = 4;
	
	// récupère les modifications effectuées sur l'interface graphique
	public void graphToBoard(int pos, int state){
		int x = pos % 60;
		int y = Math.abs(pos/60);
		
		this.gol[x][y] = state;
	}
	
	// initialise toutes les cellules à mortes
	public void initDef(){
		for(int y = 0 ; y < this.yg ; y++){
			for(int x = 0 ; x < this.xg ; x++){
				this.gol[x][y] = 0;
			}
		}
	}
	
	// définit une valeur aléatoire pour chaque cellule du board
	public void rdm(){		
		for (int y = 0; y < this.yg ; y++){
			for (int x = 0; x < this.xg ; x++){
				int valeurMax = 2;
				int valeurMin = 1;
				Random r = new Random();
				int target = valeurMin + r.nextInt(valeurMax);
			
				if(target == 1){
					this.gol[x][y] = 1;
				}
				else{
					this.gol[x][y] = 0;
				}
			}
		}
	}
		
	// initialise le tableau en fx d'une conf initiale (chargement fichier txt)
	public void conf(String res){
		int x, y = 0, z;
		
		try{
			InputStream is = new FileInputStream(res);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			String line = br.readLine();
			
			while(line != null){
				for(x = 0 ; x < 60 ; x++){
					z = Integer.parseInt("" + line.charAt(x));
					this.gol[x][y] = z;
				}
				
				line = br.readLine();
				y++;
			}
			
			br.close();
			isr.close();
			is.close();
		} catch (Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog (null, "Erreur lors du chargement de la partie", "Oops !", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// traitements pour une génération
	public void generation(){
		
		// effectue les operations necessaires sur la generation actuelle
		for(int i = 0 ; i < this.yg ; i++){
			for(int j = 0 ; j < this.xg ; j++){
				this.golCopy[j][i] = this.gol[j][i];
			}
		}
		
		// determine les cellules vivantes et mortes
		for(int y = 0 ; y < this.yg ; y++){
			for(int x = 0 ; x < this.xg ; x++){
				lifeDeath(x, y);
			}
		}	
		
		// determine la coloration des cellules
		for(int y = 0 ; y < this.yg ; y++){
			for(int x = 0 ; x < this.xg ; x++){
				if (this.gol[x][y] == 1){
					coloration(x, y);
				}
			}
		}
	}
	
	// selon le nbre de voisins, définit à vivant ou mort
	public void lifeDeath(int x, int y){
		int bnc = this.neighbourCount(x, y, this.golCopy);
		
		// verifie condition mort
		if((bnc < 2) || (bnc > 3)){
			this.gol[x][y] = BLANC;
		}
		// verifie condition vie
		else if(bnc == 3){
			this.gol[x][y] = VERT;
		}
	}
	
	// attribue des valeurs differentes aux cellules pour determiner la coloration
	public void coloration(int x, int y){
		// compte le nbre de voisins pour la generation suivante
		int bnc = this.neighbourCount(x, y, this.gol);
		
		// vert : cellules naissantes (et qui ne meurt pas à la gen suivante)
		if(this.golCopy[x][y] == BLANC && (bnc ==  3 || bnc == 2)){
			this.gol[x][y] = VERT;
		}
		
		// bleu : cellules en cours de vie (si a vécu à la dernière gen et continue de vivre à la gen suivante
		if((this.golCopy[x][y] == VERT || this.golCopy[x][y] == BLEU) && (bnc ==  3 || bnc == 2)){
			this.gol[x][y] = BLEU;
		}
		
		// rouge : cellules mourantes (a survécu plus d'une gen et meurt gen suivante)
		if((this.golCopy[x][y] == VERT || this.golCopy[x][y] == BLEU) && ((bnc < 2) || (bnc > 3))){
			this.gol[x][y] = ROUGE;
		}
		
		// jaune : cellule ne vivant qu'une génération
		if(this.golCopy[x][y] == BLANC && ((bnc < 2) || (bnc > 3))){
			this.gol[x][y] = JAUNE;
		}
	}
	
	// gestion des valeurs négatives
	public static int mod (int x, int m){
		return (x + m) % m;
	} 
	
	// compte le nbre de voisins pour une cellule(x, y)
	public int neighbourCount(int x, int y, int gol[][]){
		int nC = 0; 

		// haut
		if(y-1 >= 0 && checkValues(gol, x, mod(y-1, yg))){
			nC++;
		}
		
		// bas
		if(y+1 < 40 && checkValues(gol, x, mod(y+1, yg))){
			nC++;
		}
		
		// gauche
		if(x-1 >= 0 && checkValues(gol, mod(x-1, xg), y)){
			nC++;
		}
		
		// droite
		if(x+1 < 60 && checkValues(gol, mod(x+1, xg), y)){
			nC++;
		}
		
		// haut gauche
		if(x-1 >= 0 && y-1 >= 0 && checkValues(gol, mod(x-1, xg), mod(y-1, yg))){
			nC++;
		}
		
		// haut droite
		if(x+1 < 60 && y-1 >= 0 && checkValues(gol, mod(x+1, xg), mod(y-1, yg))){
			nC++;
		}
		
		// bas gauche
		if(x-1 >= 0 && y+1 < 40 && checkValues(gol, mod(x-1, xg), mod(y+1, yg))){
			nC++;
		}
		
		// bas droite
		if(x+1 < 60 && y+1 < 40 && checkValues(gol, mod(x+1, xg), mod(y+1, yg))){
			nC++;
		}
			
		return nC;
	}
	
	// renvoie vrai si la concernée est vivante (une des quatre couleurs)
	public boolean checkValues(int gol[][], int x, int y){
		boolean check = false;
		
		if(gol[x][y] == VERT || gol[x][y] == BLEU || gol[x][y] == ROUGE || gol[x][y] == JAUNE){
			check = true;
		}
		
		return check;
	}
}