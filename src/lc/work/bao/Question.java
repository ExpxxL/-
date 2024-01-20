package lc.work.bao;

class Question {
	//定义题目类型
    private String content;
    private String answer;
    private String type;

    public Question(String content, String answer, String type) {
        this.content = content;
        this.answer = answer;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public String getAnswer() {
        return answer;
    }

    public String getType() {
        return type;
    }
}
