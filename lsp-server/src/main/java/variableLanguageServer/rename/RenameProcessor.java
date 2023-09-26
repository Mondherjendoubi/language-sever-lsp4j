package variableLanguageServer.rename;

import featureRecognition.Feature;
import featureRecognition.Ranges;
import featureServer.FeatureServerCalls;

import org.eclipse.lsp4j.*;
import variableLanguageServer.VariableTextDocumentService;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RenameProcessor {
    ConcurrentHashMap<TextDocumentIdentifier, Set<Feature>> features;
    private FeatureServerCalls featureServer;

    public RenameProcessor(ConcurrentHashMap<TextDocumentIdentifier, Set<Feature>> features,
            FeatureServerCalls featureServer) {
        this.features = features;
        this.featureServer = featureServer;
    }

    /**
     * Return the name and range of the Feature at the requested positon
     *
     * @param params RenameParams of the request, that is being answered
     * @throws IllegalArgumentException if there is no Feature at the given position
     */
    public PreviousName returnPreviousName(TextDocumentPositionParams params) {
        Position pos = params.getPosition();
        TextDocumentIdentifier id = params.getTextDocument();
        String uri = id.getUri();
        for (Feature f : features.get(params.getTextDocument())) { // iterate over all Features
            List<Ranges> ranges;
            ranges = f.getLocations().get(VariableTextDocumentService.convertURI(uri));
            for (Ranges r : ranges) { // iterate over all ranges from that Feature
                // if the given position is contained in the features nameRange
                if (r.nameRange.start.line - 1 <= pos.getLine() && r.nameRange.end.line - 1 >= pos.getLine()
                        && r.nameRange.start.character - 1 <= pos.getCharacter()
                        && r.nameRange.end.character - 1 >= pos.getCharacter()) {
                    Position startPosition = new Position(r.nameRange.start.line - 1, r.nameRange.start.character - 1);
                    Position endPosition = new Position(r.nameRange.end.line - 1, r.nameRange.end.character - 1);
                    // found a Feature at that location
                    return new PreviousName(f.getName(), new Range(startPosition, endPosition));
                }
            }
        }
        throw new IllegalArgumentException("No Feature name at given position");
    }

    /**
     * Return the changes map
     *
     * @param params RenameParams
     */
    public Map<String, List<TextEdit>> changesMap(RenameParams params) {
        Map<String, List<TextEdit>> changes = new HashMap<>();
        String newName = params.getNewName();
        String previousName = returnPreviousName(params).getName();

        // the feature to be renamed
        Feature feature = featureServer.getFeature(previousName);

        feature.getLocations().forEach((uri, ranges) -> { // iterate over all location entries
            if (ranges != null) {
                List<TextEdit> listEdit = new ArrayList<>();
                for (Ranges r : ranges) { // iterate over all ranges from that Feature
                    // Range featurePosition = f.gcodeBlockRange; //can't use
                    // featureRecognition.Range class, because two classes with same name is
                    // prohibited
                    if (newName.length() == previousName.length()) {
                        TextEdit t = new TextEdit(new org.eclipse.lsp4j.Range(
                                new Position(r.codeBlockRange.start.line - 2, r.nameRange.start.character - 1),
                                new Position(r.codeBlockRange.start.line - 2,
                                        r.nameRange.start.character + newName.length() - 1)),
                                newName);
                        listEdit.add(t);
                    } else {
                        TextEdit t = new TextEdit(
                                new Range(
                                        new Position(r.codeBlockRange.start.line - 2, r.nameRange.start.character - 1),
                                        new Position(r.codeBlockRange.start.line - 2, r.nameRange.end.character - 1)),
                                newName + returnSpaceString(r.nameRange.end.character - r.nameRange.start.character
                                        - newName.length() - 1));
                        listEdit.add(t);
                    }
                }
                changes.put(new File(uri).toURI().toString(), listEdit);
            }
        });
        return changes;
    }

    /**
     * contains the name and range for the name of a Feature
     */
    public class PreviousName {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private Range range;

        public Range getRange() {
            return range;
        }

        public void setRange(Range range) {
            this.range = range;
        }

        PreviousName(String name, Range range) {
            this.name = name;
            this.range = range;
        }
    }

    /**
     * Return String with i spaces
     *
     * @param number int
     */

    private String returnSpaceString(int number) {
        String outPut = "";
        for (int j = number; j <= number; j++) {
            outPut += outPut + "";
        }
        return outPut;
    }
}
