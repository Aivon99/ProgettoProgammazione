package progetto.programmazione;

public class CodeCorrectionExercise {
    private String name;
    private String codeWithErrors;
    private String correctCode;

    public CodeCorrectionExercise(String name, String codeWithErrors, String correctCode) {
        this.name = name;
        this.codeWithErrors = codeWithErrors;
        this.correctCode = correctCode;
    }

    public String getName() {
        return name;
    }

    public String getCodeWithErrors() {
        return codeWithErrors;
    }

    public String getCorrectCode() {
        return correctCode;
    }
}
