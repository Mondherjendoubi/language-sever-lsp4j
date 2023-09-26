package featureServer;

import java.util.List;

import featureRecognition.Feature;

public interface FeatureServerCalls {

    /**
     * The getFeature request is sent from the language-server to the feature-server
     * to obtain the Ranges and further information of a given Feature in the entire
     * project, not only the opened files.
     * 
     * @param name target Feature
     * @return positions and activation status of the target feature project-wide
     */
    default Feature getFeature(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * The getAllFeaturesInDocument request is sent from the langauge-server to the
     * feature-server to obtain a list of all Features and their positions in the
     * specified File
     * 
     * @param uri target Document
     * @return a list of all Features and their Positions in the target Document
     */
    default List<Feature> getAllFeaturesInDocument(String uri) {
        throw new UnsupportedOperationException();
    }

    /**
     * The getFeatureList request is sent from the language-server to the
     * feature-server to obtain a list of all Feature names in the entire project.
     * 
     * @return a list of all Feature names project-wide
     */
    default List<String> getFeatureList() {
        throw new UnsupportedOperationException();
    }
}
