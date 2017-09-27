package main.aff;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Question {
	
	private String Subject;
	private int    category;
	private int    number;
	private String question;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;
	private int    correctChoice;
	public final static String[] CATEGORY_DESC = {"Luftrecht", "Freifall", "Meteorologie", "Technik", "Verhalten in besonderen Fällen", "Aerodynamik", "Menschliches Leistungsvermögen"};
	public final static int[]    NUM_Q_EXAM    = { 8,           19,         8,              20,        17,                               19,            9                              };
	
	public Question(int category, int number, String question, String answer1, String answer2, String answer3, String answer4, int correctChoice) {
		this.category      = category;       
		this.number        = number;       
		this.question      = question;     
		this.answer1       = answer1;      
		this.answer2       = answer2;      
		this.answer3       = answer3;      
		this.answer4       = answer4;      
		this.correctChoice = correctChoice;
	}
	
	private static int getCategoryByDesc(String desc) {
		int cat = -1;
		for (int i = 0; cat < 0 && i < CATEGORY_DESC.length; i++) {
			if (desc.equalsIgnoreCase(CATEGORY_DESC[i])) {
				cat = i;
			}
		}
		return cat;
	}
	
	public static List<Question> buildQuestionSubset(List<Question> allQuestions, int numQuestions, String categoryDesc){
		int numAssigned = 0;
		int cat = getCategoryByDesc(categoryDesc);
		// Kandidatenliste aufbauen: Wenn alle Fragen verwendet werden sollen, komplette Liste kopieren. Falls nicht,
		// dann nur Fragen zu passendem Themengebiet kopieren
		List<Question> selectedQuestionsSubset;
		if (cat < 0) {
			selectedQuestionsSubset = new ArrayList<Question>(allQuestions);	
		} else {
			selectedQuestionsSubset = getCategorySubset(allQuestions, cat);
		}
		
		// Auswahlliste anlegen. Maximal alle Elemente aus allQuestionsCopy verwenden
		List<Question> subSet = new ArrayList<Question>(numQuestions);
		Random rnd = new Random(System.currentTimeMillis());
		while(numAssigned < numQuestions && !selectedQuestionsSubset.isEmpty()) {
			Question q = selectedQuestionsSubset.remove(rnd.nextInt(selectedQuestionsSubset.size()));
			subSet.add(q);
			numAssigned++;
		}
		return subSet;
	}
	
	public static List<Question> buildExamSubset (List<Question> allQuestions) {
		List<Question> subSet = new ArrayList<Question>();
		Random rnd = new Random(System.currentTimeMillis());
		for (int cat = 0; cat < CATEGORY_DESC.length; cat++) {
			List<Question> subList = getCategorySubset(allQuestions, cat);
			for (int j = 0; j < NUM_Q_EXAM[cat]; j++) {
				subSet.add(subList.remove(rnd.nextInt(subList.size())));
			}
		}
		return subSet;
	}
	
	private static List<Question> getCategorySubset(List<Question> allQuestions, int category) {
		List<Question> subSet = new ArrayList<Question>();
		for (Question question : allQuestions) {
			if (question.getCategory() == category) {
				subSet.add(question);
			}
		}
		return subSet;
	}
	
	public static List<Question> loadQuestionListFromXML(InputStream inputFile){
		List<Question> fragenListe = new ArrayList<Question>();
		
		try {	
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse (inputFile);
			doc.getDocumentElement().normalize();
			int questionNumber = 0;
			String questionText = "";
			String answerText1 = "";
			String answerText2 = "";
			String answerText3 = "";
			String answerText4 = "";
			int correctAnswer = 0;
			int category = 0;
			
			// Elemente zweite Ebene = Kategorien
			NodeList nList = doc.getDocumentElement().getChildNodes();
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					NodeList questionNodes = nNode.getChildNodes();
					try{
						// Alle Fragen der aktuellen Kategorie durchlaufen
						for (int question = 0; question < questionNodes.getLength(); question++) {
							Node questionNode = questionNodes.item(question);
							if(questionNode.getNodeName().equalsIgnoreCase("question")){
								NodeList questionComponents = questionNode.getChildNodes();
								category = getCategoryByDesc(nNode.getNodeName().replace('_', ' ')); // Kategorien sind in XML Datei mit Unterstrich
						
								if (category >= 0){
 									for (int questionComponent = 0; questionComponent < questionComponents.getLength(); questionComponent++) {
										Node questionComponentNode = questionComponents.item(questionComponent);
										if(questionComponentNode.getNodeName().equalsIgnoreCase("number")){
											questionNumber = Integer.parseInt(questionComponentNode.getTextContent());
										} else if(questionComponentNode.getNodeName().equalsIgnoreCase("text")){
											questionText = questionComponentNode.getTextContent();
										} else if(questionComponentNode.getNodeName().equalsIgnoreCase("answer1")){
											answerText1 = "-> " + questionComponentNode.getTextContent();
										} else if(questionComponentNode.getNodeName().equalsIgnoreCase("answer2")){
											answerText2 = "-> " + questionComponentNode.getTextContent();
										} else if(questionComponentNode.getNodeName().equalsIgnoreCase("answer3")){
											answerText3 = "-> " + questionComponentNode.getTextContent();
										} else if(questionComponentNode.getNodeName().equalsIgnoreCase("answer4")){
											answerText4 = "-> " + questionComponentNode.getTextContent();
										} else if(questionComponentNode.getNodeName().equalsIgnoreCase("correctAnswer")){
											correctAnswer = Integer.parseInt(questionComponentNode.getTextContent());
										}

									}
									
									fragenListe.add(new Question(
													category, 
													questionNumber, 
													questionText,
													answerText1,
													answerText2,
													answerText3,
													answerText4,
													correctAnswer));
									
								}
							} // if [node = "question" ]
						}
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return fragenListe;
		
	}
	
	public int getCategory() {
		return category;
	}

	public String getCategoryDesc() {
		return CATEGORY_DESC[category];
	}

	public int getNumber() {
		return number;
	}

	public String getAnswer1() {
		return answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public String getAnswer4() {
		return answer4;
	}

	public int getCorrectChoice() {
		return correctChoice;
	}

	public String getQuestion() {
		return question;
	}
	

}
