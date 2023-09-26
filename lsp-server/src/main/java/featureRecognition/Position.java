package featureRecognition;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Position {

    public Position() {}

    public Position(int line, int character) {
        this.line = line;
        this.character = character;
    }

    @Override
    public String toString() {
        return "Position{" +
                "line=" + line +
                ", character=" + character +
                '}';
    }

    @JsonProperty("line")
    public int line;

    @JsonProperty("character")
    public int character;
}
