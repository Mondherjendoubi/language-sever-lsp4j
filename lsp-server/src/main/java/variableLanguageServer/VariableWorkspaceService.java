package variableLanguageServer;

import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.services.WorkspaceService;

import featureServer.FeatureServerCalls;

public class VariableWorkspaceService implements WorkspaceService {
    private FeatureServerCalls featureServer;

    public VariableWorkspaceService(FeatureServerCalls featureServer) {
        this.featureServer = featureServer;
    }

    @Override
    public void didChangeConfiguration(DidChangeConfigurationParams didChangeConfigurationParams) {

    }

    @Override
    public void didChangeWatchedFiles(DidChangeWatchedFilesParams didChangeWatchedFilesParams) {

    }

    @Override
    public CompletableFuture<Object> executeCommand(ExecuteCommandParams params) {
        if (params.getCommand().equals("getFeatureList")) {
            return CompletableFuture.supplyAsync(() -> {
                return featureServer.getFeatureList();
            });
        }
        return null;
    }
}
