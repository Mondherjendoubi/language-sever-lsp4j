package featureServer;

import com.github.arteam.simplejsonrpc.client.JsonRpcClient;
import com.github.arteam.simplejsonrpc.client.Transport;

import featureRecognition.Feature;
import featureRecognition.FeatureService;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FeatureServer implements FeatureServerCalls {
    JsonRpcClient jsonRpcClient;

    public FeatureServer(String url) {
        jsonRpcClient = new JsonRpcClient(new Transport() {

            // Apache HttpClient is used as an example
            CloseableHttpClient httpClient = HttpClients.createDefault();

            @Override
            public String pass(String request) throws IOException {
                HttpPost post = new HttpPost(url);
                post.setEntity(new StringEntity(request, StandardCharsets.UTF_8));
                post.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString());
                try (CloseableHttpResponse httpResponse = httpClient.execute(post)) {
                    return EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return request;
            }
        });
    }

    @Override
    public List<Feature> getAllFeaturesInDocument(String uri) {
        return jsonRpcClient.onDemand(FeatureService.class).getAllFeaturesInDocument(uri);
    }

    @Override
    public List<String> getFeatureList() {
        return jsonRpcClient.onDemand(FeatureService.class).getFeatureList();
    }

    @Override
    public Feature getFeature(String name) {
        return jsonRpcClient.onDemand(FeatureService.class).getFeature(name);
    }
}
