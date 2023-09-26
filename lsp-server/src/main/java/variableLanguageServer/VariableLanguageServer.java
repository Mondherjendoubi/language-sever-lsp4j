package variableLanguageServer;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageClientAware;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

import featureServer.FeatureServer;
import featureServer.FeatureServerCalls;
import featureServer.TestFeatureServerCalls;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VariableLanguageServer implements LanguageServer, LanguageClientAware {
    private TextDocumentService textDocumentService;
    private WorkspaceService workspaceService;
    LanguageClient client;
    private int errorCode = 1;
    private FeatureServerCalls featureServer;

    public VariableLanguageServer(String url) {
        this.featureServer = new FeatureServer(url);
        // this.featureServer = new TestFeatureServerCalls();
        this.textDocumentService = new VariableTextDocumentService(featureServer);
        this.workspaceService = new VariableWorkspaceService(featureServer);
    }

    /**
     * initialize the LSP and return server capabilities. For detailed Information
     * about each capability see the LSP <a href=
     * "https://microsoft.github.io/language-server-protocol/specifications/lsp/3.17/specification/">documentation</a>
     */
    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams initializeParams) {
        // Initialize the InitializeResult for this LS.
        final InitializeResult initializeResult = new InitializeResult(new ServerCapabilities());

        // Set the capabilities for syncing information about opened TextDocuments.
        initializeResult.getCapabilities().setTextDocumentSync(TextDocumentSyncKind.Incremental);

        initializeResult.getCapabilities().setDocumentSymbolProvider(true);
        // initializeResult.getCapabilities().setDocumentHighlightProvider(true);

        List<String> commands = new ArrayList<String>();
        commands.add("getFeatureList");

        initializeResult.getCapabilities().setExecuteCommandProvider(new ExecuteCommandOptions(commands));

        initializeResult.getCapabilities().setFoldingRangeProvider(true);
        initializeResult.getCapabilities().setRenameProvider(new RenameOptions(true));
        initializeResult.getCapabilities().setReferencesProvider(true);

        // Set capabilities for the semantic Token request
        List<String> list = new ArrayList<>();
        list.add("activeFeature");
        list.add("inactiveFeature");
        List<String> list1 = new ArrayList<>();
        list1.add("private");
        initializeResult.getCapabilities().setSemanticTokensProvider(new SemanticTokensWithRegistrationOptions(
                new SemanticTokensLegend(list, list1), Boolean.TRUE, Boolean.FALSE));

        return CompletableFuture.supplyAsync(() -> initializeResult);
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        // If shutdown request comes from client, set the error code to 0.
        errorCode = 0;
        return null;
    }

    @Override
    public void exit() {
        // Kill the LS on exit request from client.
        System.exit(errorCode);
    }

    @Override
    public TextDocumentService getTextDocumentService() {
        // Return the endpoint for language features.
        return this.textDocumentService;
    }

    @Override
    public WorkspaceService getWorkspaceService() {
        // Return the endpoint for workspace functionality.
        return this.workspaceService;
    }

    @Override
    public void connect(LanguageClient languageClient) {
        // Get the client which started this LS.
        this.client = languageClient;
    }
}
