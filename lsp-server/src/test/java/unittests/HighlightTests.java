package unittests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.LanguageServer;
import org.junit.jupiter.api.Test;

import featureServer.TestFeatureServerCalls;
import variableLanguageServer.VariableLanguageServer;
import variableLanguageServer.VariableTextDocumentService;

public class HighlightTests {

    /**
     * Tests that the Language-Server returns a valid, non-empty List of Folding
     * Ranges. Further it is tested, whether the result contains all expected
     * Folding Ranges for the test1.java Document, that can be found under <a href=
     * "https://git.rz.tu-bs.de/sw-technik-fahrzeuginformatik/sep/sep-2022/isf_vs_0/test-java-project/-/blob/master/src/test1/test1.java">
     */
    @Test
    void highlightTest() {
        /*DocumentHighlight highlight1= new DocumentHighlight(new Range(new Position(23,17),new Position(23,25)),DocumentHighlightKind.Text);
        DocumentHighlight highlight2= new DocumentHighlight(new Range(new Position(29,27),new Position(29,35)),DocumentHighlightKind.Text);
        DocumentHighlight highlight3= new DocumentHighlight(new Range(new Position(16,17),new Position(16,25)),DocumentHighlightKind.Write);
        DocumentHighlight highlight4= new DocumentHighlight(new Range(new Position(29,17),new Position(29,25)),DocumentHighlightKind.Write);*/

        DocumentHighlight highlight1= new DocumentHighlight(new Range(new Position(17,0),new Position(21,0)),DocumentHighlightKind.Read);
        DocumentHighlight highlight2= new DocumentHighlight(new Range(new Position(24,0),new Position(27,0)),DocumentHighlightKind.Read);
        DocumentHighlight highlight3= new DocumentHighlight(new Range(new Position(30,0),new Position(31,0)),DocumentHighlightKind.Read);
        DocumentHighlight highlight4= new DocumentHighlight(new Range(new Position(30,0),new Position(31,0)),DocumentHighlightKind.Read);



        LanguageServer langServer = new VariableLanguageServer("");
        VariableTextDocumentService textDocService = (VariableTextDocumentService) langServer.getTextDocumentService();
        textDocService.setFeatureServer(new TestFeatureServerCalls());

        // simulate Behaviour of Langauge-Client
        DidOpenTextDocumentParams didOpenParams = new DidOpenTextDocumentParams(
                new TextDocumentItem(System.getProperty("user.dir") + "src/test1/test1.java", "java", 1, ""));
        textDocService.didOpen(didOpenParams);

        // get result from Language-Server
        DocumentHighlightParams params = new DocumentHighlightParams(new TextDocumentIdentifier(System.getProperty("user.dir") + "src/test1/test1.java"),new Position(0,1));
        CompletableFuture<List<? extends DocumentHighlight>> result = textDocService.documentHighlight(params);
        List<? extends DocumentHighlight> list;
        try {
            list = result.get();
            //Color active Features should be highlighted in
            assertTrue(list.contains(highlight1));
            assertTrue(list.contains(highlight2));
            assertTrue(list.contains(highlight3));
            assertTrue(list.contains(highlight4));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
