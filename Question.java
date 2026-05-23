interface QuizEngine {
    String getQuestionText();
    boolean evaluateAnswer(String selectedAnswer);
}

public abstract class Question implements QuizEngine {
    protected String questionText;
    protected String correctAnswer;

    public Question(String questionText, String correctAnswer) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String getQuestionText() { return this.questionText; }

    @Override
    public boolean evaluateAnswer(String selectedAnswer) {
        return this.correctAnswer.equalsIgnoreCase(selectedAnswer);
    }
}

class MCQQuestion extends Question {
    private String[] options;

    public MCQQuestion(String questionText, String optA, String optB, String optC, String optD, String correct) {
        super(questionText, correct);
        this.options = new String[]{optA, optB, optC, optD};
    }

    public String[] getOptions() { return this.options; }
}

class TFQuestion extends Question {
    public TFQuestion(String questionText, String correct) {
        super(questionText, correct);
    }
}