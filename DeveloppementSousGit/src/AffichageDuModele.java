import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
public class AffichageDuModele extends JFrame {
	private List<Point> list_points = new ArrayList<Point>();
	private List<Segment> list_segments = new ArrayList<Segment>();
	private List<Face> list_faces = new ArrayList<Face>();
	Telecommande t = new Telecommande(this);
	
	private boolean RecupDonneeFichier(){
		List<String> fichier = new ArrayList<String>();
		JFileChooser fc = new JFileChooser("modeles/");
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				 return f.getName().endsWith(".gts");
			}
			@Override
			public String getDescription() {
				return "Fichier GNU Triangulated Surface Library (.gts)";
			}
		});
		fc.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				 return f.getName().endsWith(".txt");
			}
			@Override
			public String getDescription() {
				return "Fichier texte (.txt)";
			}
		});
		fc.showOpenDialog(getParent());
		this.setTitle("Affichage de " + fc.getSelectedFile().getName());
		try {
			String ligne;
			FileReader flux;
			BufferedReader entree;
			flux = new FileReader(fc.getSelectedFile().getAbsolutePath());
			entree = new BufferedReader(flux);
			while ((ligne = entree.readLine()) != null) {
				fichier.add(ligne);
			}
			entree.close();
		} catch (Exception e) {
			System.err.println(e.toString());
			return false;
		}
		String first = fichier.get(0);
		int nbPoints = Integer.parseInt(first.substring(0,first.indexOf(' ')));
		int nbSegments = Integer.parseInt(first.substring(first.indexOf(' ')+1,first.indexOf(' ')+1+first.substring(first.indexOf(' ')+1).indexOf(' ')));
		int nbFaces = Integer.parseInt(first.substring(first.indexOf(' ')+1+first.substring(first.indexOf(' ')+1).indexOf(' ')+1));
		int parcoursDeLigne = 1;
		for (int i = parcoursDeLigne; i < parcoursDeLigne+nbPoints; i++) {
			list_points.add(new Point(Double.parseDouble(fichier.get(i).substring(0,fichier.get(i).indexOf(' '))), Double.parseDouble(fichier.get(i).substring(fichier.get(i).indexOf(' ')+1,fichier.get(i).indexOf(' ')+1+fichier.get(i).substring(fichier.get(i).indexOf(' ')+1).indexOf(' '))), Double.parseDouble(fichier.get(i).substring(fichier.get(i).indexOf(' ')+1+fichier.get(i).substring(fichier.get(i).indexOf(' ')+1).indexOf(' ')+1)),(i-parcoursDeLigne+1)));
			//System.out.println(list_points.get(i-parcoursDeLigne));
		}
		parcoursDeLigne+=nbPoints;
		for (int i = parcoursDeLigne; i < parcoursDeLigne+nbSegments; i++) {
			list_segments.add(new Segment(list_points.get(Integer.parseInt(fichier.get(i).substring(0,fichier.get(i).indexOf(' ')))-1), list_points.get(Integer.parseInt(fichier.get(i).substring(fichier.get(i).indexOf(' ')+1))-1),(i-parcoursDeLigne+1)));
			//System.out.println(list_segments.get(i-parcoursDeLigne).getP1().getNumero() + " | " +list_segments.get(i-parcoursDeLigne).getP2().getNumero());
		}
		parcoursDeLigne+=nbSegments;
		for (int i = parcoursDeLigne; i < parcoursDeLigne+nbFaces; i++) {
			list_faces.add(new Face(list_segments.get(Integer.parseInt(fichier.get(i).substring(0,fichier.get(i).indexOf(' ')))-1), list_segments.get(Integer.parseInt(fichier.get(i).substring(fichier.get(i).indexOf(' ')+1,fichier.get(i).indexOf(' ')+1+fichier.get(i).substring(fichier.get(i).indexOf(' ')+1).indexOf(' ')))-1), list_segments.get(Integer.parseInt(fichier.get(i).substring(fichier.get(i).indexOf(' ')+1+fichier.get(i).substring(fichier.get(i).indexOf(' ')+1).indexOf(' ')+1))-1),(i-parcoursDeLigne+1)));
			//System.out.println(list_segments.get(Integer.parseInt(fichier.get(i).substring(0,fichier.get(i).indexOf(' ')))-1)+" | "+list_segments.get(Integer.parseInt(fichier.get(i).substring(fichier.get(i).indexOf(' ')+1,fichier.get(i).indexOf(' ')+1+fichier.get(i).substring(fichier.get(i).indexOf(' ')+1).indexOf(' ')))-1)+" | "+list_segments.get(Integer.parseInt(fichier.get(i).substring(fichier.get(i).indexOf(' ')+1+fichier.get(i).substring(fichier.get(i).indexOf(' ')+1).indexOf(' ')+1))-1)+" | "+(i-parcoursDeLigne+1));
			//System.out.println(list_faces.get(i-parcoursDeLigne).getS1().getNumero() + " | "+list_faces.get(i-parcoursDeLigne).getS2().getNumero() + " | "+list_faces.get(i-parcoursDeLigne).getS3().getNumero());
		}
		return true;
	}

	
	public AffichageDuModele() {
		/*if(!RecupDonneeFichier()){
			this.setTitle("ERROR");
			this.setSize(700, 500);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLocationRelativeTo(null);
			this.setResizable(false);
			panel1.setBackground(Color.RED);
			Label error = new Label("ERROR");
			Font font = new Font("Arial",Font.BOLD,150);
			error.setFont(font);		
			panel1.add(error);
			this.add(panel1);
			this.setVisible(true);			
		}else{*/
			RecupDonneeFichier();
			this.setSize(700, 700);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLocationRelativeTo(null);
			this.setResizable(true);
			this.setBackground(Color.WHITE);
			this.setVisible(true);
		//}
	}
	public void paint (Graphics ga){
		Graphics2D g = (Graphics2D)ga;
		g.setColor(Color.WHITE);
		g.fillRect(0, 0,3000, 3000);
		for (int i = 0; i < list_faces.size(); i++) {
			
		}
		for (int i = 0; i < list_faces.size(); i++) {
			g.setColor(list_faces.get(i).getColor());
			//g.setColor(new Color(r.nextInt(255)+1, r.nextInt(255)+1, r.nextInt(255)+1));
			g.fill(generatePolygon(list_faces.get(i),t.getCoeffXetY(),t.getDecalageX(),t.getDecalageY()));
		}
	}
	
	private Polygon generatePolygon(Face f,int cXY, int dX, int dY) {
		double[] x = new double[3];
		double[] y = new double[3];
		double[] z = new double[3];
		for (int i = 0; i < x.length; i++) {
			//x[i] = f.getPoint(i + 1).getX() * cXY + dX;
			x[i] = f.getPoint(i + 1).getX();
			//y[i] = f.getPoint(i + 1).getY() * cXY + dY;
			y[i] = f.getPoint(i + 1).getY();
			//z[i] = f.getPoint(i + 1).getZ() * cXY;
			z[i] = f.getPoint(i + 1).getZ();
		}
		Matrice m = new Matrice(x, y, z);
		//System.out.println(m.rotateX(t.getRotationX()).rotateY(t.getRotationY()).rotateZ(t.getRotationZ()));
		m = m.rotateX(t.getRotationX()).rotateY(t.getRotationY()).rotateZ(t.getRotationZ());
		for (int i = 0; i < 3; i++) {
			x[i]=m.getTabX()[i]* cXY + dX;
			y[i]=m.getTabY()[i] * cXY + dY;
			z[i]=m.getTabZ()[i]* cXY;
		}
		m = new Matrice(x, y, z);
		return m.PolygonGeneratorFromMatrice();
	}
	public static void main(String[] args) {
		new AffichageDuModele();
	}
}
