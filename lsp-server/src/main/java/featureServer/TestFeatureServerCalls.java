package featureServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import featureRecognition.Feature;
import featureRecognition.Position;
import featureRecognition.Range;
import featureRecognition.Ranges;

/**
 * Dummy implementation for Unit- and Integration-Tests. To use this class
 * replace the field featureServer in @see
 * variableLanguageserver.VariableTextDocumentService with an instance of this
 * class.
 */
public class TestFeatureServerCalls implements FeatureServerCalls {

    @Override
    public Feature getFeature(String name) {
        Map<String, List<Ranges>> locations = new HashMap<>();
        boolean state = true;

        // fin out which Feature was given and load corresponding data.
        if (name.equals("FeatureA")) {
            locations = locationsA();
            state = true;
        } else if (name.equals("FeatureB")) {
            locations = locationsB();
            state = false;
        } else if (name.equals("FeatureC")) {
            locations = locationsC();
            state = true;
        } else {
            throw new IllegalArgumentException(
                    "given Feature was not an example Feature. Please don't use the TestFeatureServerCalls class for full implementations");
        }

        Feature feature = new Feature(name, locations, state);
        return feature;
    }

    @Override
    public List<Feature> getAllFeaturesInDocument(String uri) {
        List<Feature> testList = new ArrayList<>();

        if (uri.endsWith("test1.java")) {
            // FeatureA
            List<Ranges> ranges = new ArrayList<>();

            // Occurence 1
            Range nameRange = new Range(new Position(17, 18), new Position(17, 26));
            Range codeBlockRange = new Range(new Position(18, 0), new Position(21, 0));
            Ranges occurence = new Ranges(nameRange, codeBlockRange);
            ranges.add(occurence);

            // Occurence 2
            nameRange = new Range(new Position(30, 18), new Position(30, 26));
            codeBlockRange = new Range(new Position(31, 0), new Position(31, 0));
            occurence = new Ranges(nameRange, codeBlockRange);
            ranges.add(occurence);

            Map<String, List<Ranges>> locations = new HashMap<>();
            locations.put(uri, ranges);

            Feature FeatureA = new Feature("FeatureA", locations, true);
            testList.add(FeatureA);

            // FeatureB
            ranges = new ArrayList<>();

            // Occurence 1
            nameRange = new Range(new Position(24, 18), new Position(24, 26));
            codeBlockRange = new Range(new Position(25, 0), new Position(27, 0));
            occurence = new Ranges(nameRange, codeBlockRange);
            ranges.add(occurence);

            // Occurence 2
            nameRange = new Range(new Position(30, 28), new Position(30, 36));
            codeBlockRange = new Range(new Position(31, 0), new Position(31, 0));
            occurence = new Ranges(nameRange, codeBlockRange);
            ranges.add(occurence);

            locations = new HashMap<>();
            locations.put(uri, ranges);

            Feature FeatureB = new Feature("FeatureB", locations, false);
            testList.add(FeatureB);
        } else if (uri.endsWith("test2.java")) {
            // FeatureA
            List<Ranges> ranges = new ArrayList<>();
            Range nameRange = new Range(new Position(9, 18), new Position(9, 26));
            Range codeBlockRange = new Range(new Position(10, 0), new Position(10, 0));
            Ranges occurence = new Ranges(nameRange, codeBlockRange);
            ranges.add(occurence);

            Map<String, List<Ranges>> locations = new HashMap<>();
            locations.put(uri, ranges);

            Feature FeatureA = new Feature("FeatureA", locations, true);
            testList.add(FeatureA);

            // FeatureB
            ranges = new ArrayList<>();

            nameRange = new Range(new Position(13, 18), new Position(13, 26));
            codeBlockRange = new Range(new Position(14, 0), new Position(19, 0));
            occurence = new Ranges(nameRange, codeBlockRange);
            ranges.add(occurence);

            locations = new HashMap<>();
            locations.put(uri, ranges);

            Feature FeatureB = new Feature("FeatureB", locations, false);
            testList.add(FeatureB);

            // FeatureC
            ranges = new ArrayList<>();

            nameRange = new Range(new Position(9, 30), new Position(9, 38));
            codeBlockRange = new Range(new Position(10, 0), new Position(10, 0));
            occurence = new Ranges(nameRange, codeBlockRange);
            ranges.add(occurence);

            locations = new HashMap<>();
            locations.put(uri, ranges);

            Feature FeatureC = new Feature("FeatureC", locations, true);
            testList.add(FeatureC);
        }

        return testList;
    }

    @Override
    public List<String> getFeatureList() {
        List<String> featureList = new ArrayList<>();
        featureList.add("FeatureA");
        featureList.add("FeatureB");
        featureList.add("FeatureC");
        return featureList;
    }

    private Map<String, List<Ranges>> locationsA() {
        Map<String, List<Ranges>> locations = new HashMap<>();

        List<Ranges> ranges = new ArrayList<>();

        // Occurence 1 test1
        Range nameRange = new Range(new Position(17, 18), new Position(17, 26));
        Range codeBlockRange = new Range(new Position(18, 0), new Position(21, 0));
        Ranges occurence = new Ranges(nameRange, codeBlockRange);
        ranges.add(occurence);

        // Occurence 2 test1
        nameRange = new Range(new Position(30, 18), new Position(30, 26));
        codeBlockRange = new Range(new Position(31, 0), new Position(31, 0));
        occurence = new Ranges(nameRange, codeBlockRange);
        ranges.add(occurence);

        locations.put(System.getProperty("user.dir") + "/src/test1/test1.java", ranges);

        // Occurence test2
        ranges = new ArrayList<>();

        nameRange = new Range(new Position(9, 18), new Position(9, 26));
        codeBlockRange = new Range(new Position(10, 0), new Position(10, 0));
        occurence = new Ranges(nameRange, codeBlockRange);
        ranges.add(occurence);

        locations.put(System.getProperty("user.dir") + "/src/test2/test2.java", ranges);

        return locations;
    }

    private Map<String, List<Ranges>> locationsB() {
        Map<String, List<Ranges>> locations = new HashMap<>();

        List<Ranges> ranges = new ArrayList<>();

        // Occurence 1 test1
        Range nameRange = new Range(new Position(24, 18), new Position(24, 26));
        Range codeBlockRange = new Range(new Position(25, 0), new Position(27, 0));
        Ranges occurence = new Ranges(nameRange, codeBlockRange);
        ranges.add(occurence);

        // Occurence 2 test1
        nameRange = new Range(new Position(30, 28), new Position(30, 36));
        codeBlockRange = new Range(new Position(31, 0), new Position(31, 0));
        occurence = new Ranges(nameRange, codeBlockRange);
        ranges.add(occurence);

        locations.put(System.getProperty("user.dir") + "/src/test1/test1.java", ranges);

        // Occurence test2
        ranges = new ArrayList<>();

        nameRange = new Range(new Position(13, 18), new Position(13, 26));
        codeBlockRange = new Range(new Position(14, 0), new Position(19, 0));
        occurence = new Ranges(nameRange, codeBlockRange);
        ranges.add(occurence);

        locations.put(System.getProperty("user.dir") + "/src/test2/test2.java", ranges);

        return locations;
    }

    private Map<String, List<Ranges>> locationsC() {
        Map<String, List<Ranges>> locations = new HashMap<>();

        List<Ranges> ranges = new ArrayList<>();

        // Occurence test2
        Range nameRange = new Range(new Position(9, 30), new Position(9, 38));
        Range codeBlockRange = new Range(new Position(10, 0), new Position(10, 0));
        Ranges occurence = new Ranges(nameRange, codeBlockRange);
        ranges.add(occurence);

        locations.put(System.getProperty("user.dir") + "/src/test2/test2.java", ranges);

        return locations;
    }
}
