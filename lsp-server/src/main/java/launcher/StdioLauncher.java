package launcher;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;

import variableLanguageServer.VariableLanguageServer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Entrance Point to the LanguageServer. IDE Extensions can start the server by
 * initiating this class and connecting to the Standart I/O.
 */
public class StdioLauncher {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // As we are using system std io channels
        // we need to reset and turn off the logging globally
        // So our client->server communication doesn't get interrupted.
        LogManager.getLogManager().reset();
        Logger globalLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        globalLogger.setLevel(Level.OFF);

        String uri = "http://localhost:8383";
        if (args.length != 0) {
            uri = args[0];
        }

        // start the language server
        startServer(System.in, System.out, uri);
    }

    /**
     * Start the language server.
     * 
     * @param in  System Standard input stream
     * @param out System standard output stream
     * @throws ExecutionException   Unable to start the server
     * @throws InterruptedException Unable to start the server
     */
    private static void startServer(InputStream in, OutputStream out, String url)
            throws ExecutionException, InterruptedException {
        // Initialize the variableLanguageServer
        VariableLanguageServer variableLanguageServer = new VariableLanguageServer(url);
        // Create JSON RPC launcher for VariableLanguageServer instance.
        Launcher<LanguageClient> launcher = LSPLauncher.createServerLauncher(variableLanguageServer, in, out);

        // Get the client that request to launch the LS.
        LanguageClient client = launcher.getRemoteProxy();

        // Set the client to language server
        variableLanguageServer.connect(client);

        // Start the listener for JsonRPC
        Future<?> startListening = launcher.startListening();

        // Get the computed result from LS.
        startListening.get();
    }
}
