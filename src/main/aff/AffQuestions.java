package main.aff;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.SystemColor;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AffQuestions extends JFrame {
	
	private int numQuestions;
	private String subject;
	private List<Question> fragenListe;
	private List<Question> correctAnswers;
	private List<Question> wrongAnswers;
	private int numAnsweredQuestions = 0;
	private int numCorrectAnswers = 0;
	private int numWrongAnswers = 0;
	private Question currentQuestion = null;
	private boolean pruefungsmodus = false;
	
	private JPanel contentPane;
	private JButton btnOK;
	private JLabel lblResult;
	private JTextField textFieldStatus;
	private JButton btnWeiter;
	private JTextArea textQ;
	private JTextArea textA1;
	private JTextArea textA2;
	private JTextArea textA3;
	private JTextArea textA4;
	private ImagePanel panelImg;
	private int selection = 0;
	private JTextArea selectedAnswer;
	
	/**
	 * Create the frame.
	 */
	public AffQuestions(List<Question> fragenListe, JFrame parentWindow, boolean pruefungsmodus) {
		this.pruefungsmodus = pruefungsmodus;
		setTitle("AFF \u00DCbungsfragen " + (pruefungsmodus ? "(Prüfungmodus)" : "(üben)"));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				parentWindow.setVisible(true);
			}
		});
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		this.fragenListe = fragenListe;
		this.wrongAnswers = new ArrayList<Question>();
		this.correctAnswers = new ArrayList<Question>();
		numQuestions = fragenListe.size();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 781, 649);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[][][grow][][][][][][]"));
		
		textQ = new JTextArea();
		textQ.setBackground(SystemColor.control);
		textQ.setWrapStyleWord(true);
		textQ.setLineWrap(true);
		textQ.setEditable(false);
		contentPane.add(textQ, "flowx,cell 0 0 1 2,grow");
		
		panelImg = new ImagePanel();
		contentPane.add(panelImg, "cell 0 2,grow");
		
		btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int correctAnswer = answerQuestion(selection);
				if (correctAnswer != selection) {
					selectedAnswer.setBackground(Color.red);
					JTextArea correctTextArea = null;
					switch(correctAnswer){
					case 1: correctTextArea = textA1; break;
					case 2: correctTextArea = textA2; break;
					case 3: correctTextArea = textA3; break;
					case 4: correctTextArea = textA4; break;
					}
					correctTextArea.setBackground(Color.green);
				}
				textFieldStatus.setText(numAnsweredQuestions + " von " + numQuestions + " beantwortet. " + numCorrectAnswers + " korrekt, " 
						                + numWrongAnswers + " falsch.");
				btnOK.setEnabled(false);
				btnWeiter.setEnabled(true);
			}
		});
		contentPane.add(btnOK, "flowx,cell 0 7,alignx left,aligny bottom");
		
		lblResult = new JLabel("   ");
		contentPane.add(lblResult, "cell 0 7,alignx left,growy");
		
		textFieldStatus = new JTextField();
		textFieldStatus.setEditable(false);
		textFieldStatus.setForeground(SystemColor.textText);
		textFieldStatus.setBackground(SystemColor.info);
		contentPane.add(textFieldStatus, "cell 0 8,growx,aligny bottom");
		textFieldStatus.setColumns(10);
		
		btnWeiter = new JButton("Weiter");
		btnWeiter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayQuestion();
				btnWeiter.setEnabled(false);
			}
		});
		btnWeiter.setEnabled(false);
		contentPane.add(btnWeiter, "cell 0 7,alignx right,aligny bottom");
		
		textA1 = new JTextArea();
		textA1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				selectAnswer(textA1);
			}
		});
		textA1.setBackground(SystemColor.control);
		textA1.setLineWrap(true);
		textA1.setWrapStyleWord(true);
		textA1.setEditable(false);
		contentPane.add(textA1, "cell 0 3,growx,aligny bottom");
		
		textA2 = new JTextArea();
		textA2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectAnswer(textA2);
			}
		});
		textA2.setBackground(SystemColor.control);
		textA2.setWrapStyleWord(true);
		textA2.setLineWrap(true);
		textA2.setEditable(false);
		contentPane.add(textA2, "cell 0 4,growx,aligny bottom");
		
		textA3 = new JTextArea();
		textA3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectAnswer(textA3);
			}
		});
		textA3.setBackground(SystemColor.control);
		textA3.setWrapStyleWord(true);
		textA3.setLineWrap(true);
		textA3.setEditable(false);
		contentPane.add(textA3, "cell 0 5,growx,aligny bottom");
		
		textA4 = new JTextArea();
		textA4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectAnswer(textA4);
			}
		});
		textA4.setBackground(SystemColor.control);
		textA4.setWrapStyleWord(true);
		textA4.setLineWrap(true);
		textA4.setEditable(false);
		contentPane.add(textA4, "cell 0 6,growx,aligny bottom");
		displayQuestion();
	}
	
	private void displayQuestion(){
		if (fragenListe.isEmpty()) {
			AffResult ar = new AffResult(correctAnswers, wrongAnswers, pruefungsmodus);
			ar.setVisible(true);
			dispose();
		} else {
			currentQuestion = fragenListe.remove(0);
			textQ.setText(currentQuestion.getCategoryDesc() + " Frage " + currentQuestion.getNumber() + ":\n" + currentQuestion.getQuestion());
			textA1.setText(currentQuestion.getAnswer1());
			textA2.setText(currentQuestion.getAnswer2());
			textA3.setText(currentQuestion.getAnswer3());
			textA4.setText(currentQuestion.getAnswer4());
			textA1.setBackground(null);	
			textA2.setBackground(null);	
			textA3.setBackground(null);	
			textA4.setBackground(null);
			selection = 0;
			selectedAnswer = null;
			// ggf. Bild anzeigen
			panelImg.loadRessource("res/" + currentQuestion.getCategoryDesc() + "_" + currentQuestion.getNumber() + ".png");
			btnOK.setEnabled(false);
			this.repaint();
		}
	}
	
	private void selectAnswer(JTextArea answerWidget) {
		selectedAnswer = answerWidget;
		if (answerWidget == textA1) {
			selection = 1;
			textA1.setBackground(Color.green);	
			textA2.setBackground(null);	
			textA3.setBackground(null);	
			textA4.setBackground(null);	
		} else if (answerWidget == textA2) {
			selection = 2;
			textA2.setBackground(Color.green);	
			textA1.setBackground(null);	
			textA3.setBackground(null);	
			textA4.setBackground(null);	
		} else if (answerWidget == textA3) {
			selection = 3;
			textA3.setBackground(Color.green);	
			textA1.setBackground(null);	
			textA2.setBackground(null);	
			textA4.setBackground(null);	
		} else if (answerWidget == textA4) {
			selection = 4;
			textA4.setBackground(Color.green);	
			textA1.setBackground(null);	
			textA2.setBackground(null);	
			textA3.setBackground(null);	
		} 
		btnOK.setEnabled(selection > 0);
	}
	
	private int answerQuestion(int selection){
		if(selection == currentQuestion.getCorrectChoice()) {
			numCorrectAnswers++;
			correctAnswers.add(currentQuestion);
		} else {
			numWrongAnswers++;
			wrongAnswers.add(currentQuestion);
		}
		numAnsweredQuestions++;
		return currentQuestion.getCorrectChoice();
	}
}
