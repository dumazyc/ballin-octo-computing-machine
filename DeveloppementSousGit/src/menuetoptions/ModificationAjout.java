package menuetoptions;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

//Classe pour modifier les informations relatives a un objet ajoute

public class ModificationAjout {

	// objet a modifier et fenetre
	String nomObjet;
	final JFrame frame = new JFrame();

	// constructeur
	public ModificationAjout(final String nomObjet) {
		this.nomObjet = nomObjet;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String name = "null";
		String auteur = "null";
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:Database.db");
			con.setAutoCommit(false);

			stmt = con.createStatement();
			// ResultSet rs = stmt.executeQuery( "SELECT * FROM OBJETS3D;" );
			rs = stmt
					.executeQuery("SELECT * FROM OBJETS3D INNER JOIN MOTSCLES ON OBJETS3D.ID = MOTSCLES.ID_M WHERE NAME = '"
							+ nomObjet + "' ORDER BY ID_M ;");
			while (rs.next()) {
				name = rs.getString("name");
				auteur = rs.getString("auteur");
			}
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

		Dimension d = new Dimension(100, 27);
		Container c = frame.getContentPane();
		c.setLayout(new GridLayout(8, 1, 7, 7));

		c.add(new JLabel("  VEUILLEZ ENTRER LES INFORMATIONS A MODIFIER   "));

		// gestion des checkbox

		final JPanel contentPane = new JPanel();

		final JTextField tfield = new JTextField(15);
		tfield.setText(name);
		tfield.setEnabled(false);

		final JCheckBox cbox = new JCheckBox("Nom de l'objet: ", false);

		ItemListener itemListener = new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				tfield.setEnabled(ie.getStateChange() == ItemEvent.SELECTED);
			}
		};
		cbox.addItemListener(itemListener);

		contentPane.add(cbox);
		contentPane.add(tfield);

		c.add(contentPane);

		final JPanel contentPane1 = new JPanel();

		final JTextField tfield1 = new JTextField(15);
		tfield1.setText(auteur);
		tfield1.setEnabled(false);

		final JCheckBox cbox1 = new JCheckBox("Auteur:    ", false);

		ItemListener itemListener1 = new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				tfield1.setEnabled(ie.getStateChange() == ItemEvent.SELECTED);

			}
		};
		cbox1.addItemListener(itemListener1);

		contentPane1.add(cbox1);
		contentPane1.add(tfield1);
		c.add(contentPane1);

		final JPanel contentPane2 = new JPanel();

		contentPane.setPreferredSize(d);
		contentPane1.setPreferredSize(d);
		contentPane1.setPreferredSize(d);
		c.add(contentPane2);
		JPanel pa1 = new JPanel();
		final JButton valider = new JButton("Valider ");

		// quand on valide
		valider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// tester ce qui a ete coche

				if ((cbox1.isSelected() && tfield1.getText().isEmpty())
						|| (cbox.isSelected() && tfield.getText().isEmpty())) {
					JOptionPane.showMessageDialog(frame,
							"Un champ coche ne peut etre vide.", "Attention",
							JOptionPane.WARNING_MESSAGE);
				} else {
					String sql = "";
					String name;
					String auteur;
					name = tfield.getText();
					auteur = tfield1.getText();

					// on interroge la base de donnees
					if (cbox.isSelected() && !tfield.getText().equals(null))
						sql = "UPDATE OBJETS3D  SET NAME =" + "'" + name + "'"
								+ "WHERE NAME =" + "'"
								+ ModificationAjout.this.nomObjet + "';";

					if (cbox1.isSelected() && !tfield1.getText().equals(null))
						sql = "UPDATE OBJETS3D  SET AUTEUR =" + "'" + auteur
								+ "'" + "WHERE NAME =" + "'"
								+ ModificationAjout.this.nomObjet + "';";

					if (cbox1.isSelected() && !tfield1.getText().equals(null)
							&& cbox.isSelected()
							&& !tfield.getText().equals(null)) {

						sql = "UPDATE OBJETS3D  SET NAME =" + "'" + name + "'"
								+ ", AUTEUR =" + "'" + auteur + "'"
								+ "WHERE NAME =" + "'" + nomObjet + "';";

					}

					Connection c = null;
					Statement stmt = null;
					try {
						Class.forName("org.sqlite.JDBC");
						c = DriverManager
								.getConnection("jdbc:sqlite:Database.db");
						c.setAutoCommit(false);
						stmt = c.createStatement();
						stmt.executeUpdate(sql);

						c.commit();

						if (cbox.isSelected() && !tfield.getText().equals(null)) { // changer
																					// le
																					// nom
																					// physiquement
																					// parlan
							String ligne;
							int cpt = 0;
							FileReader flux;
							BufferedReader entree = null;
							PrintWriter sortie = null;
							ArrayList<String> liste = new ArrayList<String>();
							try {
								flux = new FileReader("./ressources/modeles/"
										+ nomObjet + ".gts");
								entree = new BufferedReader(flux);
								sortie = new PrintWriter(
										"./ressources/modeles/" + name + ".gts");
								while ((ligne = entree.readLine()) != null) {
									liste.add(ligne);
									sortie.println(liste.get(cpt++));
								}
							} catch (Exception exception) {
								System.err.println(exception.toString());
							} finally {
								try {
									entree.close();
									sortie.close();
								} catch (Exception e1) {
									e1.printStackTrace();
								}

							}
							File MyFile = new File("./ressources/modeles/"
									+ nomObjet + ".gts");
							MyFile.delete();

						}

						// pour confirmer
						// custom title, no icon
						JOptionPane.showMessageDialog(frame,
								"Modification bien prise en compte",
								"Modification", JOptionPane.PLAIN_MESSAGE);

						// en cas d'erreur
					} catch (Exception e1) {
						System.err.println(e1.getClass().getName() + ": "
								+ e1.getMessage());
						System.exit(0);

					} finally {
						try {
							stmt.close();
							c.commit();
							c.close();
							frame.dispose();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

					}
				}
			}

		});

		valider.setPreferredSize(d);
		pa1.add(valider);
		c.add(pa1);

		JPanel pa2 = new JPanel();
		JButton annuler = new JButton("Annuler ");
		annuler.setPreferredSize(d);
		pa2.add(annuler);
		c.add(pa2);

		// ferme la fenetre
		annuler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(640, 480);
		frame.setResizable(false);
		frame.setVisible(true);

	}
}
