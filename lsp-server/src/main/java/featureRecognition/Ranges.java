package featureRecognition;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Ranges {

    public Ranges() {}

    public Ranges(Range nameRange, Range codeBlockRange) {
        this.nameRange = nameRange;
        this.codeBlockRange = codeBlockRange;
    }

    @Override
    public String toString() {
        return "Ranges{" +
                "nameRange=" + nameRange +
                ", codeBlockRange=" + codeBlockRange +
                '}';
    }

    @JsonProperty("nameRange")
    public Range nameRange;

    @JsonProperty("codeBlockRange")
    public Range codeBlockRange;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ranges ranges = (Ranges) o;
        return Objects.equals(nameRange, ranges.nameRange) && Objects.equals(codeBlockRange, ranges.codeBlockRange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameRange, codeBlockRange);
    }
}
