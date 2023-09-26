package unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.FoldingRange;
import org.eclipse.lsp4j.FoldingRangeRequestParams;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.services.LanguageServer;
import org.junit.jupiter.api.Test;

import featureServer.TestFeatureServerCalls;
import variableLanguageServer.VariableLanguageServer;
import variableLanguageServer.VariableTextDocumentService;

public class FoldingRangeTests {

    /**
     * Tests that the Language-Server returns a valid, non-empty List of Folding
     * Ranges. Further it is tested, whether the result contains all expected
     * Folding Ranges for the test1.java Document, that can be found under <a href=
     * "https://git.rz.tu-bs.de/sw-technik-fahrzeuginformatik/sep/sep-2022/isf_vs_0/test-java-project/-/blob/master/src/test1/test1.java">
     */
    @Test
    void foldingRangeTest() {
        LanguageServer langServer = new VariableLanguageServer("");
        VariableTextDocumentService textDocService = (VariableTextDocumentService) langServer.getTextDocumentService();
        textDocService.setFeatureServer(new TestFeatureServerCalls());

        // simulate Behaviour of Langauge-Client
        DidOpenTextDocumentParams didOpenParams = new DidOpenTextDocumentParams(
                new TextDocumentItem(System.getProperty("user.dir") + "src/test1/test1.java", "java", 1, ""));
        textDocService.didOpen(didOpenParams);

        // get result from Language-Server
        FoldingRangeRequestParams foldingRangeParams = new FoldingRangeRequestParams(
                new TextDocumentIdentifier(System.getProperty("user.dir") + "src/test1/test1.java"));
        CompletableFuture<List<FoldingRange>> result = textDocService.foldingRange(foldingRangeParams);
        List<FoldingRange> list;
        try {
            list = result.get();
            assertEquals(false, list.isEmpty());
            assertTrue(list.contains(new FoldingRange(16, 20)));
            assertTrue(list.contains(new FoldingRange(29, 30)));
            assertTrue(list.contains(new FoldingRange(23, 26)));
        } catch (InterruptedException | ExecutionException e) {
            fail(e);
        }
    }
}
