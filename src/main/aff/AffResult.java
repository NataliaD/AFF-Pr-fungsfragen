package main.aff;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.SystemColor;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

public class AffResult extends JDialog {

	private List<Question> correctAnswers;
	private List<Question> wrongAnswers;

	private final JPanel contentPanel = new JPanel();
	private JTextArea textResult = null;
	/**
	 * Create the dialog.
	 */
	public AffResult(List<Question> correctAnswers, List<Question> wrongAnswers, boolean pruefungsmodus) {
		setTitle("Ergebnis");
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			textResult = new JTextArea();
			textResult.setLineWrap(true);
			textResult.setForeground(SystemColor.windowText);
			textResult.setFont(new Font("Tahoma", textResult.getFont().getStyle() & ~Font.ITALIC | Font.BOLD, 12));
			textResult.setBackground(SystemColor.control);
			textResult.setEditable(false);
			contentPanel.add(textResult, BorderLayout.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		if (pruefungsmodus == false) {
			double tmp = ((double)correctAnswers.size() / ((double)(correctAnswers.size() + wrongAnswers.size()))) * 100.0;
			int quote = (int)Math.round(tmp);
			textResult.setText(quote + "% Korrekt\n"
			           + (quote >= 85 ? "Bestanden!" : "Durchgefallen!"));
			textResult.setForeground(quote >= 85 ? Color.green : Color.red);
		} else {
			boolean gesamtBestanden = true;
			String result = "Ergebnis nach Bereich:\n";
			for (int i = 0; i < Question.NUM_Q_EXAM.length; i++) {
				int correct = 0;
				int wrong = 0;				
				for (Question question : correctAnswers) {
					if (question.getCategory() == i){
						correct++;
					}
				}
				for (Question question : wrongAnswers) {
					if (question.getCategory() == i){
						wrong++;
					}
				}
				int quote = (int)Math.round(100.0 * (double)correct / ((double)correct + (double)wrong));
				if (quote < 75) {
					gesamtBestanden = false;
				}
				result = result + Question.CATEGORY_DESC[i] + ": " + quote + "% (" + correct + " von " + (correct + wrong) + ")\n";
			}
			result = result + "\n" + 
					(gesamtBestanden ? "Alle Bereiche mindestens zu 75% korrekt, Prüfung bestanden!" : "Mindetens ein bereich mit weniger als 75% korrekt, leider nicht bestanden.");
			textResult.setText(result);
		}
		
	}

}
