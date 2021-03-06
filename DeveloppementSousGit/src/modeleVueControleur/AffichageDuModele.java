package modeleVueControleur;

import gestiondelaffichage3d.PanelAffichage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import menuetoptions.Description;
import menuetoptions.Enregistrer;
import menuetoptions.ModifiAjout;
import menuetoptions.OptionCouleur;

import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Observable;
import java.util.Observer;

/**
 * Fenetre principale du programme : est une vue du ModelInsertion.
 * 
 */
@SuppressWarnings("serial")
public class AffichageDuModele extends JFrame implements Observer {
	private JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

	private JMenuBar jmenubar;
	public JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	public static JMenuItem modifierInfos = new JMenuItem("Modifier les infos");

	private boolean recherche = false;

	private ImageIcon closeXIcon = new ImageIcon(
			"./ressources/imageMenu/close.gif");
	private Dimension closeButtonSize = new Dimension(
			closeXIcon.getIconWidth() + 2, closeXIcon.getIconHeight() + 2);
	private boolean musiqueActive = true;
	Description description;
	protected Controleur controler;
	private ModelInsertion model;
	private Recherche r;

	/**
	 * Constructeur de AffichageDuModele
	 */
	public AffichageDuModele() {
		model = new ModelInsertion();
		controler = new Controleur(this, model);
		model.addObserver(this);
		r = new Recherche(this, this.model);
		jmenubar = new JMenuBar();
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		int X = 900;
		int Y = 700;
		this.setSize(X, Y);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setBackground(Color.WHITE);
		this.setTitle("ballin-octo-computing-machine");
		JMenu j1 = new JMenu("Fichier");
		JMenu j2 = new JMenu("Options");
		JMenu j3 = new JMenu("?");
		JMenuItem ajout = new JMenuItem("Ajouter un objet");
		JMenuItem recherche = new JMenuItem("Rechercher un objet");
		JMenuItem enregistre = new JMenuItem("Enregistrer sous..");
		JMenuItem fermer = new JMenuItem("Fermer");
		JMenuItem aide = new JMenuItem("Aide");
		JMenuItem aPropos = new JMenuItem("A Propos");
		JMenuItem Zoom = new JMenuItem("Zoom par Default");
		JMenuItem color = new JMenuItem("Modifier les couleurs");
		JMenu resolution = new JMenu("Resolution");
		JMenuItem music = new JMenuItem("Activer/Desactiver la musique");

		JRadioButtonMenuItem full = new JRadioButtonMenuItem("Plein ecran");
		JRadioButtonMenuItem full2 = new JRadioButtonMenuItem("900*700", true);
		ButtonGroup bg = new ButtonGroup();
		bg.add(full);
		bg.add(full2);

		// raccourci clavier
		ajout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				KeyEvent.CTRL_MASK));
		recherche.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				KeyEvent.CTRL_MASK));
		enregistre.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.CTRL_MASK));
		fermer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
				KeyEvent.ALT_DOWN_MASK));
		aide.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		music.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
				KeyEvent.CTRL_MASK));

		j1.add(ajout);
		j1.add(recherche);
		j1.add(enregistre);
		j1.add(fermer);
		j2.add(resolution);
		j2.add(Zoom);
		j2.add(music);
		j2.add(modifierInfos);
		j2.add(color);
		j3.add(aide);
		j3.add(aPropos);
		resolution.add(full);
		resolution.add(full2);
		jmenubar.add(j1);
		jmenubar.add(j2);
		jmenubar.add(j3);
		this.setJMenuBar(jmenubar);

		this.add(tabbedPane, BorderLayout.CENTER);

		sp.add(tabbedPane, JSplitPane.RIGHT);
		sp.setVisible(true);
		this.add(sp);

		// ouvre l'ajout d'objet
		ajout.addActionListener(new ActionListenerAjout(this));

		// ferme l'application
		fermer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		// Ajout de l'aide
		aide.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane jop = new JOptionPane();
				String mess = "Vous pouvez ajouter un objet de format \".gts\" ainsi que rechercher\n";
				mess += "un objet deja present dans la base de donnees du logiciel.\n";
				mess += "Vous pouvez affiner votre recherche en rentrant des mots cles\n";
				mess += "et vous avez la possibilite d'en ajouter ou d'en supprimer.";
				mess += "\n___________________________________________________\n\n";
				mess += "Clique Gauche -> Bouge la figure\n";
				mess += "Clique droit -> Tourne la figure\n";
				mess += "Molette -> Zoom \n";
				jop.showMessageDialog(null, mess, "Aide",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		// A Propos
		aPropos.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane jop = new JOptionPane();
				String mess = "Ce logiciel a ete concu pour permettre d'afficher un objet 3D\n";
				mess += "de facon a pouvoir le voir sous tous ses angles ainsi que\n";
				mess += "de pouvoir l'agrandir ou le diminuer.\n";
				mess += "_____________________________________________________________________\n\n";
				mess += "Realise par Clement Dumazy, Karen Migan, Damien Lepeltier,\n";
				mess += "Camille Regnier et Ludovic Lorthios";
				jop.showMessageDialog(null, mess, "A propos",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		// Option enregistrer
		enregistre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(tabbedPane.getTabCount()>0){
				new Enregistrer(tabbedPane.getTitleAt(tabbedPane
						.getSelectedIndex()));
				}
			}
		});

		// ouvre la recherche d'objet

		recherche.addActionListener(new RechercheListener(this));

		// si case cocher alors plein ecran
		full.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Dimension tailleEcran = Toolkit.getDefaultToolkit()
						.getScreenSize();
				int Y = (int) tailleEcran.getHeight();
				int X = (int) tailleEcran.getWidth();
				AffichageDuModele.this.setSize(X, Y);
				AffichageDuModele.this.setLocationRelativeTo(null);
			}
		});

		// si case cocher alors taille fenetre = 900/700
		full2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AffichageDuModele.this.setSize(900, 700);

			}
		});

		// Si on clique sur le bouton zoom par default, le zoom se remet par
		// default
		Zoom.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				PanelAffichage p = (PanelAffichage) tabbedPane
						.getComponentAt(tabbedPane.getSelectedIndex());
				p.remettreZoomParDefaut();
			}
		});

		music.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < tabbedPane.getTabCount(); i++) {
					PanelAffichage p = (PanelAffichage) tabbedPane
							.getComponentAt(i);
					p.desactiverMusique();

				}
				if (musiqueActive) {
					musiqueActive = false;
				} else {
					musiqueActive = true;
				}
			}
		});

		// pour modifier les informations associees a l'objet courant
		modifierInfos.addActionListener(new ActionListenerModification(this));
		nouvelOnglet(obtenirLeNomDuPremierObjet());

		// Gestion des couleurs

		color.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new OptionCouleur((PanelAffichage) tabbedPane
						.getComponentAt(tabbedPane.getSelectedIndex()));
			}
		});
		description = new Description(tabbedPane.getTitleAt(tabbedPane
				.getSelectedIndex()));
		add(description, BorderLayout.SOUTH);
		tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (tabbedPane.getSelectedIndex() >= 0) {
					mettreAJourDescription(tabbedPane.getTitleAt(tabbedPane
							.getSelectedIndex()));
				}

			}
		});
		this.setVisible(true);

	}

	/**
	 * Permet de faire un nouvel onglet.
	 * 
	 * @param name
	 *            nom de l'objet .gts a afficher (sans l'extension)
	 */
	public void nouvelOnglet(final String name) {
		final PanelAffichage p = new PanelAffichage(this, name, musiqueActive);
		JPanel tab = new JPanel();
		tab.setOpaque(false);

		JLabel tabLabel = new JLabel(name);

		JButton tabCloseButton = new JButton(closeXIcon);
		tabCloseButton.setPreferredSize(closeButtonSize);
		tabCloseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int closeTabNumber = tabbedPane.indexOfComponent(p);
				tabbedPane.removeTabAt(closeTabNumber);
			}
		});

		tab.add(tabLabel, BorderLayout.WEST);
		tab.add(tabCloseButton, BorderLayout.EAST);
		tabbedPane.addTab(name, p);
		tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tab);
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
		mettreAJourDescription(name);
	}

	/**
	 * Listener qui permet d'enlever d'afficher la fenetre recherche.
	 * 
	 */
	public class RechercheListener implements ActionListener {
		AffichageDuModele a;

		public RechercheListener(AffichageDuModele a) {
			this.a = a;
		}

		public void actionPerformed(ActionEvent arg0) {
			if (recherche) {
				sp.remove(r);
				recherche = false;
			} else {
				sp.add(r, JSplitPane.LEFT);
				recherche = true;
			}
		}
	}

	/**
	 * Permet de connaitre la hauteur de de l'onglet courant.
	 * 
	 * @return hauteur de de l'onglet courant.
	 */
	public double getHauteur() {
		return tabbedPane.getSize().getHeight();
	}

	/**
	 * Permet de connaitre la largeur de de l'onglet courant.
	 * 
	 * @return largeur de de l'onglet courant.
	 */
	public double getLargeur() {
		return tabbedPane.getSize().getWidth();
	}

	/**
	 * Listener qui permet de faire appel a la fenetre d'ajout
	 * 
	 */
	public class ActionListenerAjout implements ActionListener {

		private AffichageDuModele a;

		public ActionListenerAjout(AffichageDuModele a) {
			this.a = a;
		}

		public void actionPerformed(ActionEvent e) {
			new Ajout(a, a.model);
		}

	}

	/**
	 * Listener qui permet de faire appel a la fenetre de modification
	 * 
	 */
	public class ActionListenerModification implements ActionListener {

		private AffichageDuModele a;

		public ActionListenerModification(AffichageDuModele a) {
			this.a = a;

		}

		public void actionPerformed(ActionEvent e) {
			new ModifiAjout(
					tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()), a);
		}

	}

	/**
	 * Permet de mettre a jour la liste d'objets de la classe recherche
	 */
	public void mettreAJourRecherche() {
		r.mettreAJourListeObjet();
	}

	/**
	 * Permet de mettre a jour la description pour qu'elle s'affiche pour
	 * l'objet de nom name
	 * 
	 * @param name
	 *            nom de l'objet dont on doit afficher la description
	 */
	public void mettreAJourDescription(String name) {
		if (description != null) {
			this.remove(description);
		}
		description = new Description(name);
		add(description, BorderLayout.SOUTH);
		this.invalidate();
		this.validate();
		this.repaint();
	}

	/**
	 * Permet de changer le nom de l'onglet cournt
	 * 
	 * @param name
	 *            nouveau nom
	 */
	public void changerNomOngletCourant(String name) {
		tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
		nouvelOnglet(name);
	}

	/**
	 * Permet de recuperer le nom du premier objet de la base de donnee
	 * 
	 * @return le nom du premier objet de la base de donnee
	 */
	public String obtenirLeNomDuPremierObjet() {
		Connection con = null;
		String name = "null";
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:Database.db");
			con.setAutoCommit(false);

			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM OBJETS3D;");
			rs.next();
			name = rs.getString("name");

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();

			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
		return name;
	}

	public static void main(String[] args) {
		new AffichageDuModele();
	}

	@Override
	public void update(Observable o, Object arg) {
		nouvelOnglet((String) arg);

	}
}
