package lib.web.bookstore.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WordOfDay {

    private String word;
    private Object definitions;

    public WordOfDay(String word, Object definitions) {
        this.word = word;
        this.definitions = definitions;
    }

    public Object getDefinitions() {
        return definitions;
    }

    public void setDefinitions(Object definitions) {
        this.definitions = definitions;
    }

    @Override
    public String toString() {

        String[] noneDefinitions = null;
        String wordOfDay;
        wordOfDay = word+": ";
        noneDefinitions = definitions.toString().split("=");

        
        
//        for (String noneDefinition : noneDefinitions) {
//            System.out.println(noneDefinition);
//        }


        for (int i = 0; i < noneDefinitions.length; i++) {
            noneDefinitions[i] = noneDefinitions[i].replace(", note", "");

        }
        String[] realDefinitions = {noneDefinitions[2]};
        for (int i = 0; i < realDefinitions.length; i++) {
            realDefinitions[i]= "-"+(i+1)+". "+realDefinitions[i];
            wordOfDay+= realDefinitions[i];
        }
        return wordOfDay;
    }

    public WordOfDay() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
