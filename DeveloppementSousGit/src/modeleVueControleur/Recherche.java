package modeleVueControleur;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import java.sql.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Permet de rechercher rapidement un objet, de l'afficher, d'ajouter, modifier et supprimer
 * des mots cles : est une vue du ModelInsertion.
 *
 */

public class Recherche extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	AffichageDuModele frame;
	DefaultListModel<String> listModel_gts;
	JList<String> liste_gts;
	private JTextField tfield;
	private Controleur controler;
	protected ModelInsertion model;
	private int id = 0;


	public Recherche(final AffichageDuModele a,ModelInsertion model) {
		this.model=model;
		this.controler = new Controleur(a,model);
		model.addObserver(this);
		this.frame = a;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		// premier contentPane recherche.
		JPanel contentPane1 = new JPanel();
		JLabel recherche = new JLabel("Recherche : ");
		tfield = new JTextField("Mots Cles", 8);
		contentPane1.add(recherche);
		contentPane1.add(tfield);
		contentPane1.setBorder(BorderFactory.createMatteBorder(2, 2, 0, 2,
				Color.black));
		this.add(contentPane1);
		contentPane1.setPreferredSize((new Dimension(30, 0)));

		// 2eme contentPane resultat.
		final JPanel contentPane2 = new JPanel();
		contentPane2.setPreferredSize((new Dimension(60, 130)));
		JLabel res = new JLabel("Liste des Objets : ");
		listModel_gts = new DefaultListModel<String>();
		liste_gts = new JList<String>(listModel_gts);
		JScrollPane scroll = new JScrollPane(liste_gts);
		scroll.setPreferredSize(new Dimension(200, 150));
		contentPane2.setBorder(BorderFactory.createMatteBorder(0, 2, 3, 2,Color.black));
		contentPane2.add(res);
		contentPane2.add(scroll);
		this.add(contentPane2);

		// panel text
		JPanel contentPane5 = new JPanel();
		JLabel label_modifier = new JLabel("<html><b><font size=4>Modifier les mots cles : </font></b></html>");
		contentPane5.add(label_modifier);
		contentPane5.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 2,
				Color.black));
		// contentPane5.setSize(new Dimension(50, 50));
		this.add(contentPane5);

		// 3eme contentpane liste des mots cles.
		
		JPanel contentPane3 = new JPanel();
		JLabel label_motscles = new JLabel("Mots Cles : ");
		final DefaultListModel<String> listModel_motscles = new DefaultListModel<String>();
		final JList<String> liste_mots_clef = new JList<String>(listModel_motscles);
		JScrollPane scrol2 = new JScrollPane(liste_mots_clef);
		scrol2.setPreferredSize(new Dimension(120, 150));
		contentPane3.add(label_motscles);
		contentPane3.add(scrol2);
		final JButton supprimer = new JButton("Supprimer");
		supprimer.setEnabled(false);
		liste_mots_clef.setEnabled(false);
		contentPane3.add(supprimer);
		contentPane3.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 2,
				Color.black));
		this.add(contentPane3);
		contentPane3.setPreferredSize(new Dimension(10, 180));

		// 4eme contentpane modification des mots cles.

		JPanel contentPane4 = new JPanel();
		final JTextField tfield2 = new JTextField("New mots cles", 10);
		final JButton modifier = new JButton("modifier");
		final JButton ajouter = new JButton("Ajouter");
		tfield2.setEnabled(false);
		ajouter.setEnabled(false);
		modifier.setEnabled(false);
		contentPane4.setBorder(BorderFactory.createMatteBorder(0, 2, 2, 2,
				Color.black));
		contentPane4.add(tfield2);
		contentPane4.add(modifier);
		contentPane4.add(ajouter);
		this.add(contentPane4);
		mettreAJourListeObjet();
		this.setVisible(true);

		// Permet le traitement automatique des la saisie d'un nouveau
		// caractere
		// dans le textfield.
		
		tfield.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				mettreAJourListeObjet();
			}

			public void removeUpdate(DocumentEvent e) {
				mettreAJourListeObjet();
			}

			public void insertUpdate(DocumentEvent e) {
				mettreAJourListeObjet();
			}
		});

		// Action quand on selectionne un element de la liste des .gts.
		liste_gts.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {

				if (e.getValueIsAdjusting()) {
					return;
				}
				liste_gts.getSelectedValue();
				if ((String) liste_gts.getSelectedValue() != null) {
					controler.affichageObjet((String) liste_gts.getSelectedValue());
					
					 
					 }
				

				Connection c = null;
				Statement stmt = null;
				try {
					Class.forName("org.sqlite.JDBC");
					c = DriverManager.getConnection("jdbc:sqlite:Database.db");
					c.setAutoCommit(false);

					stmt = c.createStatement();
					String requete = "SELECT * FROM OBJETS3D INNER JOIN MOTSCLES ON OBJETS3D.ID = MOTSCLES.ID_M WHERE NAME = '"
							+ ((String) liste_gts.getSelectedValue()) + "'";

					ResultSet rs = stmt.executeQuery(requete);
					listModel_motscles.removeAllElements();
					while (rs.next()) {
						listModel_motscles.addElement((rs.getString("MOTCLE")));
						id = rs.getInt("id");
					}
					liste_mots_clef.setEnabled(true);

				} catch (Exception e1) {
					System.err.println(e1.getClass().getName() + ": "
							+ e1.getMessage());
					System.exit(0);
				} finally {
					try {
						stmt.close();
						c.commit();
						c.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

				}

			}
		});

		// Action quand on selectionne un element de la liste des mots clef.
		
		liste_mots_clef.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent arg0) {
				supprimer.setEnabled(true);
				modifier.setEnabled(true);
				ajouter.setEnabled(true);
				tfield2.setText((String) liste_mots_clef.getSelectedValue());
				tfield2.setEnabled(true);
			}
		});

		// Action du bouton Supprime.

		supprimer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = "";
				if (!listModel_motscles.isEmpty()) {
					name = listModel_motscles.get(liste_mots_clef.getSelectedIndex());
					int index = liste_mots_clef.getSelectedIndex();
					listModel_motscles.remove(index);

					if (index == listModel_motscles.getSize()) {
						// removed item in last position
						index--;

						liste_mots_clef.setSelectedIndex(index);
						liste_mots_clef.ensureIndexIsVisible(index);
					}
				}
				Connection con = null;
				Statement stmt = null;

				try {
					Class.forName("org.sqlite.JDBC");
					con = DriverManager.getConnection("jdbc:sqlite:Database.db");
					con.setAutoCommit(false);

					stmt = con.createStatement();
					stmt.executeUpdate("DELETE FROM MOTSCLES WHERE ID_M = "
							+ id + " AND MOTCLE = '" + name + "';");
					con.commit();
				} catch (Exception ea) {
					System.err.println(ea.getClass().getName() + ": "
							+ ea.getMessage());
					System.exit(0);
				} finally {
					try {
						stmt.close();
						con.close();

					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}
			}
		});
			
			
			/*
			@Override
			public void actionPerformed(ActionEvent arg0) {
				listModel_motscles.removeElement(liste_mots_clef.getSelectedValue());
				
				
				String name = "";
				if (!listModel_motscles.isEmpty()) {
					name = listModel_motscles.get(liste_mots_clef.getSelectedIndex());
					int index = liste_mots_clef.getSelectedIndex();
					listModel_motscles.remove(index);

					if (index == listModel_motscles.getSize()) {
						// removed item in last position
						index--;

						liste_mots_clef.setSelectedIndex(index);
						liste_mots_clef.ensureIndexIsVisible(index);
					}
				}
							
				Connection con = null;
				Statement stmt = null;

				try {
					Class.forName("org.sqlite.JDBC");
					con = DriverManager.getConnection("jdbc:sqlite:Database.db");
					con.setAutoCommit(false);

					stmt = con.createStatement();
					stmt.executeUpdate("DELETE FROM MOTSCLES WHERE ID_M = "
							+ id + " AND MOTCLE = '" + name + "';");
					con.commit();
				} catch (Exception ea) {
					System.err.println(ea.getClass().getName() + ": "
							+ ea.getMessage());
					System.exit(0);
				} finally {
					try {
						stmt.close();
						con.close();

					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}
			}
		});*/

		// Action du bouton Modifier.

		modifier.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = 0;
				index = liste_mots_clef.getSelectedIndex();
				if (index != 0) {
					listModel_motscles.insertElementAt(tfield2.getText(), index);
					listModel_motscles.remove(index + 1);
				}
			}
		});

		// Action du bouton Ajouter.
		ajouter.addActionListener(new ActionListener() {



				@Override
				public void actionPerformed(ActionEvent e) {
					String name = tfield2.getText();

					// User did not type in a unique name...
					if (name.equals("") || alreadyInList(name)) {
						Toolkit.getDefaultToolkit().beep();
						tfield2.requestFocusInWindow();
						tfield2.selectAll();
						return;
					}

					int index = liste_mots_clef.getSelectedIndex(); // get selected index
					if (index == -1) { // no selection, so insert at beginning
						index = 0;
					} else { // add after the selected item
						index++;

					}

					listModel_motscles.addElement(tfield2.getText());
					Connection con = null;
					Statement stmt = null;
					try {
						Class.forName("org.sqlite.JDBC");
						con = DriverManager
								.getConnection("jdbc:sqlite:Database.db");
						con.setAutoCommit(false);

						stmt = con.createStatement();
						stmt.executeUpdate("INSERT INTO MOTSCLES (ID_M,MOTCLE) "
								+ "VALUES (" + id + ", '" + name + "' );");
						con.commit();
					} catch (Exception ea) {
						System.err.println(ea.getClass().getName() + ": "
								+ ea.getMessage());
						System.exit(0);
					} finally {
						try {
							stmt.close();
							con.close();

						} catch (Exception e1) {
							e1.printStackTrace();
						}

					}
				}

				private boolean alreadyInList(String name) {
					return listModel_motscles.contains(name);

				}
				
				
				
				
				/*if (!tfield2.getText().equals("New mots cles")) {
					listModel_motscles.addElement(tfield2.getText());	
					Connection con = null;
					Statement stmt = null;
					try {
						Class.forName("org.sqlite.JDBC");
						con = DriverManager
								.getConnection("jdbc:sqlite:Database.db");
						con.setAutoCommit(false);

						stmt = con.createStatement();
						stmt.executeUpdate("INSERT INTO MOTSCLES (MOTCLE) "
								+ "VALUES (" '" + name + "' );");
						con.commit();
					} catch (Exception ea) {
						System.err.println(ea.getClass().getName() + ": "
								+ ea.getMessage());
						System.exit(0);
					} finally {
						try {
							stmt.close();
							con.close();

						} catch (Exception e1) {
							e1.printStackTrace();
						}
				} else {
					System.out
					.println("vous n'avez pas entrer de nouveau mots cles");
				}*/

			
		});
	}

	/**
	 * Permet de mettre a jour la Jlist d'objet de tous les objets de la base
	 */
	public void mettreAJourListeObjet() {
		Connection c = null;
		Statement stmt = null;
		listModel_gts.removeAllElements();
		liste_gts.clearSelection();
		liste_gts.removeAll();

		if (tfield!=null && !tfield.getText().equals("")
				&& !tfield.getText().equals("Mots Cles")) {
			try {
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:Database.db");
				c.setAutoCommit(false);
				stmt = c.createStatement();
				String requete = "SELECT DISTINCT OBJETS3D.NAME FROM OBJETS3D INNER JOIN MOTSCLES ON OBJETS3D.ID = MOTSCLES.ID_M WHERE MOTCLE LIKE '"
						+ tfield.getText()
						+ "%' OR NAME LIKE '"
						+ tfield.getText()
						+ "%' OR AUTEUR LIKE '"
						+ tfield.getText()
						+ "%' OR ID LIKE '"
						+ tfield.getText()
						+ "%' OR DATECREATION LIKE '"
						+ tfield.getText() + "%';";
				ResultSet rs = stmt.executeQuery(requete);
				while (rs.next()) {
					listModel_gts.addElement((rs.getString("NAME")));

				}
				liste_gts.setEnabled(true);

			} catch (Exception e1) {
				System.err.println(e1.getClass().getName() + ": "
						+ e1.getMessage());
				System.exit(0);
			} finally {
				try {
					stmt.close();
					c.commit();
					c.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
		} else {
			try {
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:Database.db");
				c.setAutoCommit(false);
				stmt = c.createStatement();
				String requete = "SELECT DISTINCT NAME FROM OBJETS3D;";
				ResultSet rs = stmt.executeQuery(requete);
				while (rs.next()) {
					listModel_gts.addElement((rs.getString("NAME")));

				}
				liste_gts.setEnabled(true);

			} catch (Exception e1) {
				System.err.println(e1.getClass().getName() + ": "
						+ e1.getMessage());
				System.exit(0);
			} finally {
				try {
					stmt.close();
					c.commit();
					c.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
		}

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}
}