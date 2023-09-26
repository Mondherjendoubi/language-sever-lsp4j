package variableLanguageServer.semanticTokensFull;

import featureRecognition.Feature;
import featureRecognition.Range;
import featureRecognition.Ranges;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import variableLanguageServer.VariableTextDocumentService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SemanticTokensFullProcessor {
    ConcurrentHashMap<TextDocumentIdentifier, Set<Feature>> features;

    public SemanticTokensFullProcessor(ConcurrentHashMap<TextDocumentIdentifier, Set<Feature>> features) {
        this.features = features;
    }

    /**
     * Return the Semantic tokens list
     *
     * @param params TextDocumentIdentifier
     */

    public List<Integer> returnSemanticList(TextDocumentIdentifier params) {
        // iterator For tokenTypes line
        int lineIndex = 0;
        // iterator For tokenTypes Character
        int charIndex = 0;
        Map<Range, Feature> changes = new TreeMap<Range, Feature>();
        ArrayList<Integer> data = new ArrayList<>();
        changes = nameRangesList(params);
        // get the first element of semantic list
        Map.Entry<featureRecognition.Range, Feature> entry1 = changes.entrySet().iterator().next();
        lineIndex = entry1.getKey().start.line - 1;
        charIndex = entry1.getKey().start.character - 1;
        for (Map.Entry<featureRecognition.Range, Feature> entry : changes.entrySet()) {
            int start = entry.getKey().start.line - 1;
            // check if Feature is enabled
            if (entry.getValue().getState() == true) {
                if (start == lineIndex && entry.getKey().start.character - 1 == charIndex) {
                    data.addAll(Arrays.asList(lineIndex, entry.getKey().start.character - 1, 8, 0, 0));
                } else if (start == lineIndex && entry.getKey().start.character - 1 != charIndex) {
                    data.addAll(Arrays.asList(0, entry.getKey().start.character - charIndex - 1, 8, 0, 0));
                } else {
                    data.addAll(Arrays.asList(start - lineIndex, entry.getKey().start.character - 1, 8, 0, 0));
                    lineIndex = start;
                }
                // check if Feature isnot enabled
            } else {
                if (start == lineIndex && entry.getKey().start.character - 1 == charIndex) {
                    data.addAll(Arrays.asList(lineIndex, entry.getKey().start.character - 1, 8, 1, 1));
                } else if (start == lineIndex && entry.getKey().start.character - 1 != charIndex) {
                    data.addAll(Arrays.asList(0, entry.getKey().start.character - charIndex - 1, 8, 1, 1));
                } else {
                    data.addAll(Arrays.asList(start - lineIndex, entry.getKey().start.character - 1, 8, 1, 1));
                    lineIndex = start;
                }
            }
        }
        return data;
    }

    /**
     * Return a sorted map of the Features in each Range
     *
     * @param id TextDocumentIdentifier
     */

    private Map<Range, Feature> nameRangesList(TextDocumentIdentifier id) {
        String uri = id.getUri();
        Map<Range, Feature> changes = new TreeMap<Range, Feature>();
        for (Feature f : features.get(id)) { // iterate over all features in the features set
            List<Ranges> ranges = f.getLocations().get(VariableTextDocumentService.convertURI(uri));

            for (Ranges r : ranges) { // iterate over all ranges from that Feature
                changes.put(r.nameRange, f);
            }
        }
        return changes;
    }

}
