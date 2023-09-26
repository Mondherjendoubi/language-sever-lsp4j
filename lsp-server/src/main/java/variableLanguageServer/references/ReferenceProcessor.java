package variableLanguageServer.references;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ReferenceParams;
import org.eclipse.lsp4j.TextDocumentIdentifier;

import featureRecognition.Feature;
import featureRecognition.Ranges;
import featureServer.FeatureServerCalls;
import variableLanguageServer.VariableTextDocumentService;

public class ReferenceProcessor {

    private ConcurrentHashMap<TextDocumentIdentifier, Set<Feature>> features;
    private FeatureServerCalls featureServer;

    public ReferenceProcessor(ConcurrentHashMap<TextDocumentIdentifier, Set<Feature>> features,
            FeatureServerCalls featureServer) {
        this.features = features;
        this.featureServer = featureServer;
    }

    public List<Location> referenceList(ReferenceParams params) {
        String featureName = findName(params.getTextDocument(), params.getPosition());
        Feature feature = featureServer.getFeature(featureName);

        List<Location> references = new ArrayList<>();

        feature.getLocations().forEach((doc, rangesList) -> {
            rangesList.forEach((ranges) -> {
                Position startPosition = new Position(ranges.nameRange.start.line - 1,
                        ranges.nameRange.start.character - 1);
                Position endPosition = new Position(ranges.nameRange.end.line - 1, ranges.nameRange.end.character - 1);
                Range range = new Range(startPosition, endPosition);
                references.add(new Location(new File(doc).toURI().toString(), range));
            });
        });

        return references;
    }

    /**
     * find the name of the Feature at the given location
     * 
     * @param docId    Document to search in
     * @param position Position to search at
     * @return the name of the Feature at the given location
     */
    String findName(TextDocumentIdentifier docId, Position position) {
        for (Feature f : features.get(docId)) {
            for (Ranges r : f.getLocations().get(VariableTextDocumentService.convertURI(docId.getUri()))) {
                // if position is contained in the name range
                if (r.nameRange.start.line <= position.getLine() + 1 && r.nameRange.end.line >= position.getLine() + 1
                        && r.nameRange.start.character - 1 <= position.getCharacter()
                        && r.nameRange.end.character >= position.getCharacter()) {
                    return f.getName();
                }
            }
        }
        throw new IllegalArgumentException("No Feature name at given position");
    }
}
