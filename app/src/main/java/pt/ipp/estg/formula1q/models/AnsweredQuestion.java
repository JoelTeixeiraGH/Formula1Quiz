package pt.ipp.estg.formula1q.models;

public class AnsweredQuestion extends Question{
    private int userAnswer, points;

    public AnsweredQuestion(){}

    public AnsweredQuestion(Question question, int userAnswer, int points) {
        super.setQuestion(question.getQuestion());
        super.setAnswer(question.getAnswer());
        super.setChoiceA(question.getChoiceA());
        super.setChoiceB(question.getChoiceB());
        super.setChoiceC(question.getChoiceC());
        super.setId(question.getId());
        super.setAnswered(question.isAnswered());
        super.setCategory(question.getCategory());

        this.userAnswer = userAnswer;
        this.points = points;
    }

    public int getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(int userAnswer) {
        this.userAnswer = userAnswer;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getStringAnswer(){
        String answer = null;
        switch(getAnswer()){
            case 0:
                answer = getChoiceA();
            break;
            case 1:
                answer = getChoiceB();
            break;
            case 2:
                answer = getChoiceC();
            break;
        }
        return answer;
    }

    public String getStringUserAnswer(){
        String answer = null;
        switch(userAnswer){
            case 0:
                answer = getChoiceA();
                break;
            case 1:
                answer = getChoiceB();
                break;
            case 2:
                answer = getChoiceC();
                break;
        }
        return answer;
    }

    public int getAwardedPoints(){
        return userAnswer == getAnswer() ? points : 0;
    }
}
