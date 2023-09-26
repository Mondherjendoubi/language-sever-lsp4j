package featureRecognition;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcMethod;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcParam;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcService;

import java.util.List;

@JsonRpcService
public interface FeatureService {

    @JsonRpcMethod
    Feature getFeature(@JsonRpcParam("name") String name);
   
    @JsonRpcMethod
    List<Feature> getAllFeaturesInDocument(@JsonRpcParam("uri") String uri); 

    @JsonRpcMethod
    List<String> getFeatureList(); 

}
