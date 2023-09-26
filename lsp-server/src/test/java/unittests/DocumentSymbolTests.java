package unittests;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.LanguageServer;
import org.junit.jupiter.api.Test;

import featureServer.TestFeatureServerCalls;
import variableLanguageServer.VariableLanguageServer;
import variableLanguageServer.VariableTextDocumentService;

import static org.junit.jupiter.api.Assertions.*;


public class DocumentSymbolTests {

    @Test
    @SuppressWarnings("deprecation")
    void documentSymbol() {
        SymbolInformation expectedFeatureB = new SymbolInformation("FeatureB",SymbolKind.forValue(8),
                new Location(System.getProperty("user.dir") + "src/test1/test1.java",
                        new Range(new Position(23,0),new Position(27,0))),"Occurence1");
        SymbolInformation expectedFeatureA = new SymbolInformation("FeatureA",SymbolKind.forValue(8),
                new Location(System.getProperty("user.dir") + "src/test1/test1.java",
                        new Range(new Position(16,0),new Position(21,0))),"Occurence1");
        SymbolInformation expectedFeatureB1 = new SymbolInformation("FeatureB",SymbolKind.forValue(8),
                new Location(System.getProperty("user.dir") + "src/test1/test1.java",
                        new Range(new Position(29,0),new Position(31,0))),"Occurence2");
        SymbolInformation expectedFeatureA1 = new SymbolInformation("FeatureA",SymbolKind.forValue(8),
                new Location(System.getProperty("user.dir") + "src/test1/test1.java",
                        new Range(new Position(29,0),new Position(31,0))),"Occurence2");
        LanguageServer langServer = new VariableLanguageServer("");
        VariableTextDocumentService textDocService = (VariableTextDocumentService) langServer.getTextDocumentService();
        textDocService.setFeatureServer(new TestFeatureServerCalls());

        // simulate Behaviour of Langauge-Client
        DidOpenTextDocumentParams didOpenParams = new DidOpenTextDocumentParams(
                new TextDocumentItem(System.getProperty("user.dir") + "src/test1/test1.java", "java", 1, ""));
        textDocService.didOpen(didOpenParams);

        // get result from Language-Server
        DocumentSymbolParams foldingRangeParams = new DocumentSymbolParams(
                new TextDocumentIdentifier(System.getProperty("user.dir") + "src/test1/test1.java"));
        CompletableFuture<List<Either<SymbolInformation, DocumentSymbol>>> result = textDocService.documentSymbol(foldingRangeParams);
        List<Either<SymbolInformation, DocumentSymbol>> list;
        try {
            list = result.get();
            ArrayList<SymbolInformation> symbolList= new ArrayList<>();
            for(Either<SymbolInformation, DocumentSymbol> either : list){
                symbolList.add(either.getLeft());
            }

            assertTrue(symbolList.contains(expectedFeatureB));
            assertTrue(symbolList.contains(expectedFeatureB1));
            assertTrue(symbolList.contains(expectedFeatureA));
            assertTrue(symbolList.contains(expectedFeatureA1));

        } catch (InterruptedException | ExecutionException e) {
            fail(e);
        }
    }
}
