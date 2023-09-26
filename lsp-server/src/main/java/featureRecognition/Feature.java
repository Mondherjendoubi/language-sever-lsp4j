package featureRecognition;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public class Feature implements Comparable<Feature> {

    public Feature() {

    }

    public Feature(String name, Map<String, List<Ranges>> locations, Boolean state)  {
        this.name = name;
        this.enabled = state;
        this.locations = locations;
    }

    public void print() {
        System.out.println("Name: "+name+" | "+"enabled: "+enabled);
        System.out.println("--------------------------------------------------");
        /*for(Ranges range : ranges) {
            System.out.println("nameRangeStartLine: "+range.nameRange.start.line+" | "+"nameRangeStartCharacter: "+range.nameRange.start.character);
            System.out.println("nameRangeEndLine: "+range.nameRange.end.line+" | "+"nameRangeEndCharacter: "+range.nameRange.end.character);
            System.out.println("codeBlockRangeStartLine: "+range.codeBlockRange.start.line+" | "+"codeBlockRangeStartCharacter: "+range.codeBlockRange.start.character);
            System.out.println("codeBlockRangeEndLine: "+range.codeBlockRange.end.line+" | "+"codeBlockRangeEndCharacter: "+range.codeBlockRange.end.character);
        }*/
        System.out.println("--------------------------------------------------");
    }

    @Override
    public String toString() {
        return "Feature{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public Boolean getState() {
        return enabled;
    }

    public Map<String, List<Ranges>> getLocations() {
        return locations;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(Boolean state) {
        this.enabled = state;
    }

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("name")
    private String name;

    @JsonProperty("locations")
    private Map<String, List<Ranges>> locations;

    @JsonProperty("enabled")
    private Boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feature feature = (Feature) o;
        return Objects.equals(uri, feature.uri) && Objects.equals(name, feature.name) && Objects.equals(locations, feature.locations) && Objects.equals(enabled, feature.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, name, locations, enabled);
    }




    @Override
    public int compareTo(@NotNull Feature o) {
        if (this.name.equals(o.getName())) {
            return 0;
        } else if (this.name.length()>(o.getName().length()) ){
            return 1;
        }else{
            return -1;
        }
    }
}
