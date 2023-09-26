package variableLanguageServer;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;

import featureRecognition.Feature;
import featureRecognition.Ranges;
import featureServer.FeatureServerCalls;
import variableLanguageServer.references.ReferenceProcessor;
import variableLanguageServer.rename.RenameProcessor;
import variableLanguageServer.rename.RenameProcessor.PreviousName;
import variableLanguageServer.semanticTokensFull.SemanticTokensFullProcessor;

/**
 * Implementation of the TextDocumentService for use in the
 * VariableLanguageServer. This implementation uses a set of features to manage
 * the features and their positions in all open text documents.
 */
public class VariableTextDocumentService implements TextDocumentService {

    // featureServer interface to access FeatureRecognitionSoftware information.
    private FeatureServerCalls featureServer;

    /**
     * Contains a MapEntry for each open document, identified by its URI as a
     * TextDocumentIdentifier. For each open document there is a Set of Features in
     * that document. Therefore all elements in a set have the property document
     * equal to the URI of the MapEntry it is a part of. This Map is maintained
     * through the WorspaceSync functions (i.e. didOpen()...) and may only be
     * changed by them. For all other LSP-calls this Map should be read-only.
     */
    private ConcurrentHashMap<TextDocumentIdentifier, Set<Feature>> features;

    public VariableTextDocumentService(FeatureServerCalls featureServer) {
        this.featureServer = featureServer;
    }

    /*
     * As the Feature Server expects an URI which is not encoded, this function is
     * used to decode the URI sent from VSCode client
     */
    public static String convertURI(String uri) {
        try {
            return java.net.URLDecoder.decode(uri, "UTF-8").replace("file://", "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Send a FoldingRange for every feature range in the given text document to the
     * client.
     *
     * @param foldingRangeParams FoldingRangeParams
     * @return list of FoldingRange as defined in specification of LSP
     */
    @Override
    public CompletableFuture<List<FoldingRange>> foldingRange(FoldingRangeRequestParams foldingRangeParams) {
        return CompletableFuture.supplyAsync(() -> {
            List<FoldingRange> foldingRanges = new ArrayList<>();

            TextDocumentIdentifier id = foldingRangeParams.getTextDocument();
            String uri = id.getUri();

            // iterate through set of features in the given text document
            for (Feature f : features.get(new TextDocumentIdentifier(uri))) {
                List<Ranges> ranges = f.getLocations().get(convertURI(uri));

                // create a FoldingRange for every code block of a feature
                for (Ranges r : ranges) {
                    int startLine = r.codeBlockRange.start.line - 2;
                    int endLine = r.codeBlockRange.end.line - 1;
                    FoldingRange foldingRange = new FoldingRange(startLine, endLine);
                    foldingRanges.add(foldingRange);
                }
            }
            return foldingRanges;
        });
    }

    /**
     * Send a DocumentSymbol for every feature in the given text document to the
     * client
     *
     * @param documentSymbolParams DocumentSymbolParams
     * @return list of DocumentSymbol as defined in the specification of LSP
     */
    @Override
    @SuppressWarnings("deprecation")
    public CompletableFuture<List<Either<SymbolInformation, DocumentSymbol>>> documentSymbol(
            DocumentSymbolParams documentSymbolParams) {
        return CompletableFuture.supplyAsync(() -> {
            List<Either<SymbolInformation, DocumentSymbol>> symbolInformations = new ArrayList<>();
            Map<Feature, Integer> occurrenceMap = new HashMap<Feature, Integer>();
            TextDocumentIdentifier id = documentSymbolParams.getTextDocument();
            String uri = id.getUri();

            // iterate through set of features in the given text document
            for (Feature f : features.get(id)) {
                List<Ranges> ranges = f.getLocations().get(convertURI(uri));

                // create a DocumentSymbol for every code block of a feature
                for (Ranges r : ranges) {
                    if (!occurrenceMap.containsKey(f)) {
                        occurrenceMap.put(f, 1);
                    } else {
                        occurrenceMap.put(f, occurrenceMap.get(f) + 1);
                    }
                    Position startLine = new Position(r.codeBlockRange.start.line - 2,
                            r.codeBlockRange.start.character);
                    Position endLine = new Position(r.codeBlockRange.end.line, r.codeBlockRange.end.character);
                    symbolInformations.add(Either.forLeft(new SymbolInformation(f.getName(), SymbolKind.forValue(8),
                            new Location(uri, new Range(startLine, endLine)), "Occurence" + occurrenceMap.get(f))));
                }
            }
            return symbolInformations;
        });
    }

    /**
     * Send a DocumentHighlight for every feature in the given text document to the
     * client The value of DocumentHighlight.Kind depends on the activation state of
     * the feature
     *
     * @param documentHighlightParams DocumentHighlightParams
     * @return list of DocumentHighlight as defined in the specification of LSP
     */
    @Override
    public CompletableFuture<List<? extends DocumentHighlight>> documentHighlight(
            DocumentHighlightParams documentHighlightParams) {
        return CompletableFuture.supplyAsync(() -> {
            List<DocumentHighlight> DocumentHighlightLists = new ArrayList<DocumentHighlight>();

            TextDocumentIdentifier id = documentHighlightParams.getTextDocument();
            String uri = id.getUri();

            // iterate through set of features in the given text document
            for (Feature f : features.get(id)) {
                List<Ranges> ranges = f.getLocations().get(convertURI(uri));

                // create a DocumentHighlight for every code block of a feature
                for (Ranges r : ranges) {
                    Position startLine = new Position(r.codeBlockRange.start.line - 1,
                            r.codeBlockRange.start.character);
                    Position endLine = new Position(r.codeBlockRange.end.line, r.codeBlockRange.end.character);

                    DocumentHighlightLists
                            .add(new DocumentHighlight(new Range(startLine, endLine), DocumentHighlightKind.Read));
                }
            }
            return DocumentHighlightLists;
        });
    }

    @Override
    public CompletableFuture<List<ColorInformation>> documentColor(DocumentColorParams params) {
        return CompletableFuture.supplyAsync(() -> {
            List<ColorInformation> lists = new ArrayList<ColorInformation>();

            TextDocumentIdentifier id = params.getTextDocument();
            String uri = id.getUri();

            for (Feature f : features.get(id)) { // iterate over all features in the features set
                List<Ranges> ranges;
                ranges = f.getLocations().get(convertURI(uri));
                for (Ranges r : ranges) { // iterate over all ranges from that Feature
                    Position startLine = new Position(r.nameRange.start.line, r.nameRange.start.character);
                    Position endLine = new Position(r.nameRange.end.line, r.nameRange.start.character);
                    lists.add(new ColorInformation(new Range(startLine, endLine), new Color(70, 56, 63, 0.8)));
                }
            }
            return lists;
        });
    }

    public CompletableFuture<SemanticTokens> semanticTokensFull(SemanticTokensParams params) {
        return CompletableFuture.supplyAsync(() -> {
            SemanticTokens sem = new SemanticTokens(
                    new SemanticTokensFullProcessor(this.features).returnSemanticList((params.getTextDocument())));
            return sem;
        });
    }

    @Override
    public CompletableFuture<Either<Range, PrepareRenameResult>> prepareRename(PrepareRenameParams params) {
        return CompletableFuture.supplyAsync(() -> {
            PreviousName previousName = new RenameProcessor(features, featureServer).returnPreviousName(params);
            PrepareRenameResult result = new PrepareRenameResult(previousName.getRange(), previousName.getName());
            return Either.forRight(result);
        });
    }

    @Override
    public CompletableFuture<WorkspaceEdit> rename(RenameParams renameParams) {
        return CompletableFuture.supplyAsync(() -> {
            WorkspaceEdit workspaceEdit = new WorkspaceEdit(
                    new RenameProcessor(features, featureServer).changesMap(renameParams));
            return workspaceEdit;
        });
    }

    @Override
    public CompletableFuture<List<? extends Location>> references(ReferenceParams referenceParams) {
        return CompletableFuture.supplyAsync(() -> {
            List<Location> references = new ReferenceProcessor(features, featureServer).referenceList(referenceParams);
            return references;
        });
    }

    /**
     * Get feature information from feature server when a text document is opened
     *
     * @param didOpenTextDocumentParams DidOpenTextDocumentParams
     */
    @Override
    public void didOpen(DidOpenTextDocumentParams didOpenTextDocumentParams) {
        // initialize features if features not already exists
        if (features == null) {
            features = new ConcurrentHashMap<>();
        }

        TextDocumentIdentifier id = new TextDocumentIdentifier(didOpenTextDocumentParams.getTextDocument().getUri());
        Set<Feature> featuresInDocument = new HashSet<>();
        featuresInDocument.addAll(featureServer.getAllFeaturesInDocument(convertURI(id.getUri())));
        features.put(id, featuresInDocument);
    }

    /**
     * Update feature information in features if text document is changed
     *
     * @param didChangeTextDocumentParams DidChangeTextDocumentParams
     */
    @Override
    public void didChange(DidChangeTextDocumentParams didChangeTextDocumentParams) {
        TextDocumentIdentifier id = new TextDocumentIdentifier(didChangeTextDocumentParams.getTextDocument().getUri());
        Set<Feature> featuresInDocument = new HashSet<>();
        featuresInDocument.addAll(featureServer.getAllFeaturesInDocument(convertURI(id.getUri())));
        // remove previous information about features in the document
        features.remove(id);
        features.put(id, featuresInDocument);
    }

    /**
     * Remove feature information from closed text document
     *
     * @param didCloseTextDocumentParams DidCloseTextDocumentParams
     */
    @Override
    public void didClose(DidCloseTextDocumentParams didCloseTextDocumentParams) {
        TextDocumentIdentifier id = new TextDocumentIdentifier(didCloseTextDocumentParams.getTextDocument().getUri());
        features.remove(id);
    }

    @Override
    public void didSave(DidSaveTextDocumentParams didSaveTextDocumentParams) {

    }

    public FeatureServerCalls getFeatureServer() {
        return featureServer;
    }

    public void setFeatureServer(FeatureServerCalls featureServer) {
        this.featureServer = featureServer;
    }

}
