package gestionDeLAffichage3D;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

public class Ajout extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static JFrame frame = new JFrame("Veuillez charger le fichier et ajouter les infos n�c�ssaires :   ");
	
	public static void createAndDisplayGUI()throws IOException
	{ 
		 
				JFileChooser dialogue = new JFileChooser(new File("."));
				
				
				
		Dimension d=new Dimension(100,27);
		Container c=frame.getContentPane();
		c.setLayout(new GridLayout(8,1,7,7));
		c.add(dialogue);
		
		
		c.add(new JLabel("  VEUILLEZ ENTRER LES INFORMATIONS RELATIVES A VOTRE OBJET:     "));
		
		final JPanel contentPane = new JPanel();

		final JTextField tfield = new JTextField(15);
		tfield.setEnabled(false);

		final JCheckBox cbox = new JCheckBox("Nom de l'objet: ", false);

		ItemListener itemListener = new ItemListener()
		{
			public void itemStateChanged(ItemEvent ie)
			{
				tfield.setEnabled(ie.getStateChange() == ItemEvent.SELECTED);
			}
		};
		cbox.addItemListener(itemListener);

		contentPane.add(cbox);
		contentPane.add(tfield);

		c.add(contentPane);

		final JPanel contentPane1 = new JPanel();

		final JTextField tfield1 = new JTextField(15);

		tfield1.setEnabled(false);

		final JCheckBox cbox1 = new JCheckBox("Auteur:    ", false);

		ItemListener itemListener1 = new ItemListener()
		{
			public void itemStateChanged(ItemEvent ie)
			{
				tfield1.setEnabled(ie.getStateChange() == ItemEvent.SELECTED);

			}
		};
		cbox1.addItemListener(itemListener1);

		contentPane1.add(cbox1);
		contentPane1.add(tfield1);
		c.add(contentPane1);



		final JPanel contentPane2  = new JPanel();

		final JComboBox<String>  perso = new JComboBox<String>();
		perso.setEnabled(false);
		perso.setBounds(100,120,100,25);


		perso.addItem("0" );
		perso.addItem("1" );
		perso.addItem("2" );
		perso.addItem("3" );
		perso.addItem("4" );
		perso.addItem("5" );


		final JCheckBox cbox2 = new JCheckBox("Nombre de trinangles:    ", false);


		ItemListener itemListener2 = new ItemListener()
		{
			public void itemStateChanged(ItemEvent ie)
			{
				perso.setEnabled(ie.getStateChange() == ItemEvent.SELECTED);
			}
		};
		cbox2.addItemListener(itemListener2);

		contentPane2.add(cbox2);
		contentPane2 .add(perso);
		contentPane.setPreferredSize(d);
		contentPane1.setPreferredSize(d);
		contentPane1.setPreferredSize(d);
		c.add(contentPane2);
		JPanel pa1=new JPanel();
		JButton bu=new JButton("Valider ");
		bu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if((cbox1.isSelected()&&tfield1.getText().isEmpty())||(cbox.isSelected()&&tfield.getText().isEmpty())){
					JOptionPane.showMessageDialog(frame,
							"Un champ coch� ne peut �tre vide.",
							"Attention",
							JOptionPane.WARNING_MESSAGE);
				}
				//else insertion dans la base
				// String ac = perso.getSelectedItem().toString()
				// la date de l'objet sera ajout�e directement � la base avec une fonction qui renvoie la date du jour



			}
		});

		bu.setPreferredSize(d);
		pa1.add(bu);
		c.add(pa1);


		JPanel pa2=new JPanel();
		JButton e=new JButton("Retour ");
		e.setPreferredSize(d);
		pa2.add(e);
		c.add(pa2);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(640,480);
		frame.setResizable(false);
		frame.setVisible(true);
		


	}
	public static void main(String... args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				try {
					createAndDisplayGUI();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}


}