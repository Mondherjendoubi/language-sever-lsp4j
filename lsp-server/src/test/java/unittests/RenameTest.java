package unittests;

import featureServer.TestFeatureServerCalls;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.LanguageServer;
import org.junit.jupiter.api.Test;
import variableLanguageServer.VariableLanguageServer;
import variableLanguageServer.VariableTextDocumentService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class RenameTest {

    @Test
    void renameTest() {
        Map<String, List<TextEdit>> changes = new HashMap<>();

        // changes in test1.java
        List<TextEdit> listEdit = new ArrayList<>();
        listEdit.add(new TextEdit(new Range(new Position(16, 17), new Position(16, 25)), "newName"));
        listEdit.add(new TextEdit(new Range(new Position(29, 17), new Position(29, 25)), "newName"));
        changes.put("file:/" + System.getProperty("user.dir").replace('\\', '/') + "/src/test1/test1.java", listEdit);

        // changes in test2.java
        listEdit = new ArrayList<>();
        listEdit.add(new TextEdit(new Range(new Position(8, 17), new Position(8, 25)), "newName"));
        changes.put("file:/" + System.getProperty("user.dir").replace('\\', '/') + "/src/test2/test2.java", listEdit);

        WorkspaceEdit exptectedWorkspaceEdit = new WorkspaceEdit(changes);

        LanguageServer langServer = new VariableLanguageServer("");
        VariableTextDocumentService textDocService = (VariableTextDocumentService) langServer.getTextDocumentService();
        textDocService.setFeatureServer(new TestFeatureServerCalls());

        // simulate Behaviour of Langauge-Client
        DidOpenTextDocumentParams didOpenParams = new DidOpenTextDocumentParams(new TextDocumentItem(
                "file:/" + System.getProperty("user.dir").replace('\\', '/') + "/src/test1/test1.java", "java", 1, ""));
        textDocService.didOpen(didOpenParams);

        // get result from Language-Server
        RenameParams renameParams = new RenameParams(
                new TextDocumentIdentifier(
                        "file:/" + System.getProperty("user.dir").replace('\\', '/') + "/src/test1/test1.java"),
                new Position(16, 25), "newName");
        CompletableFuture<WorkspaceEdit> result = textDocService.rename(renameParams);
        WorkspaceEdit workspaceEdit;
        try {
            workspaceEdit = result.get();
            assertEquals(exptectedWorkspaceEdit.getChanges(), workspaceEdit.getChanges());
            assertEquals(exptectedWorkspaceEdit, workspaceEdit);
        } catch (InterruptedException | ExecutionException e) {
            fail(e);
        }
    }
}
