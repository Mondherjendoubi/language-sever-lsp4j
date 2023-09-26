package featureRecognition;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Range implements Comparable<Range> {
    public Range() {}

    public Range(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "Range{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    @JsonProperty("start")
    public Position start;

    @JsonProperty("end")
    public Position end;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range range = (Range) o;
        return Objects.equals(start, range.start) && Objects.equals(end, range.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public int compareTo(@NotNull Range o) {
        if (o.start.line==this.start.line && o.start.character==this.start.character){
            return 0;
        } else if (this.start.line>o.start.line || (this.start.line==o.start.line && this.start.character>o.start.character )){
            return 1;
        }   else{
            return  -1 ;
        }
    }
}
