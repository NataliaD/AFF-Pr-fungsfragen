package main.aff;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.miginfocom.swing.MigLayout;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;

public class AffMain {

	private JFrame frmAffTheoriefragen;
	private List<Question> fragenListe;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AffMain window = new AffMain();
					window.frmAffTheoriefragen.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AffMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmAffTheoriefragen = new JFrame();
		frmAffTheoriefragen.setTitle("AFF Theoriefragen");
		frmAffTheoriefragen.setBounds(100, 100, 450, 169);
		frmAffTheoriefragen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAffTheoriefragen.getContentPane().setLayout(new MigLayout("", "[][grow][grow]", "[][][][][][]"));

		JLabel lblAnzahlFragen = new JLabel("Anzahl Fragen");
		frmAffTheoriefragen.getContentPane().add(lblAnzahlFragen, "cell 1 1,alignx trailing");

		JComboBox comboBoxNumquestions = new JComboBox();
		comboBoxNumquestions.setModel(new DefaultComboBoxModel(new String[] {"10 Fragen", "25 Fragen", "50 Fragen", "100 Fragen"}));
		frmAffTheoriefragen.getContentPane().add(comboBoxNumquestions, "cell 2 1,growx");

		JLabel lblKategorie = new JLabel("Kategorie");
		frmAffTheoriefragen.getContentPane().add(lblKategorie, "cell 1 2,alignx trailing");

		JComboBox comboBoxSubjects = new JComboBox();
		comboBoxSubjects.setModel(new DefaultComboBoxModel(new String[] {"(Gemischte Kategorien)"}));
		frmAffTheoriefragen.getContentPane().add(comboBoxSubjects, "cell 2 2,growx");

		JButton btnAbfragen = new JButton("\u00DCben");
		btnAbfragen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AffQuestions aq = new AffQuestions(Question.buildQuestionSubset(
							fragenListe, 
							Integer.parseInt(comboBoxNumquestions.getSelectedItem().toString().split(" ")[0]),
							comboBoxSubjects.getSelectedItem().toString()),
						frmAffTheoriefragen, false);
				aq.setVisible(true);
				frmAffTheoriefragen.setVisible(false);
			}
		});
		
		frmAffTheoriefragen.getContentPane().add(btnAbfragen, "cell 2 3,growx");
		loadXML(comboBoxSubjects);
		
		JSeparator separator = new JSeparator();
		frmAffTheoriefragen.getContentPane().add(separator, "cell 0 4 3 1,growx,aligny top");
		
		JButton btnExam = new JButton("Pr\u00FCfungssimulation");
		btnExam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AffQuestions aq = new AffQuestions(Question.buildExamSubset(fragenListe),
						frmAffTheoriefragen, true);
				aq.setVisible(true);
				frmAffTheoriefragen.setVisible(false);
			}
		});
		frmAffTheoriefragen.getContentPane().add(btnExam, "cell 2 5,growx");
	}

	public void show(){
		frmAffTheoriefragen.setVisible(true);
	}
	
	private void loadXML(JComboBox comboBoxSubjects){
		fragenListe = Question.loadQuestionListFromXML(getClass().getResourceAsStream("res/aff.xml"));
		Set<String> categories = new HashSet<String>();
		for (Question question : fragenListe) {
			categories.add(question.getCategoryDesc());
		}
		for (String category : categories) {
			comboBoxSubjects.addItem(category);
		}
	}
}
