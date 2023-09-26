package integration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.jupiter.api.Test;

import featureRecognition.Feature;
import featureRecognition.Ranges;
import featureServer.FeatureServer;

public class FeatureServerTests {
    public FeatureServerTests() {
	featureServer = new FeatureServer("http://localhost:8383");
	workingDirectory = "/home/user/dev/java/myproject";
    }

    @Test
    void getAllFeaturesInDocumentTest() throws UnsupportedEncodingException {
	String uri = workingDirectory+"/src/test1/test1.java";
	List<Feature> features = featureServer.getAllFeaturesInDocument(uri);

	assertEquals(2, features.size());

	//Tests for FeatureA
	Feature featureA = features.get(0);
	assertEquals("FeatureA",featureA.getName());
	assertEquals(true,featureA.getState());

	List<Ranges> rangesA = featureA.getLocations().get(uri);
	assertEquals(17,rangesA.get(0).nameRange.start.line);
	assertEquals(18,rangesA.get(0).nameRange.start.character);
	assertEquals(17,rangesA.get(0).nameRange.end.line);
	assertEquals(26,rangesA.get(0).nameRange.end.character);
	assertEquals(18,rangesA.get(0).codeBlockRange.start.line);
	assertEquals(0,rangesA.get(0).codeBlockRange.start.character);
	assertEquals(21,rangesA.get(0).codeBlockRange.end.line);
	assertEquals(0,rangesA.get(0).codeBlockRange.end.character);

	assertEquals(30,rangesA.get(1).nameRange.start.line);
	assertEquals(18,rangesA.get(1).nameRange.start.character);
	assertEquals(30,rangesA.get(1).nameRange.end.line);
	assertEquals(26,rangesA.get(1).nameRange.end.character);
	assertEquals(31,rangesA.get(1).codeBlockRange.start.line);
	assertEquals(0,rangesA.get(1).codeBlockRange.start.character);
	assertEquals(31,rangesA.get(1).codeBlockRange.end.line);
	assertEquals(0,rangesA.get(1).codeBlockRange.end.character);

	uri = workingDirectory+"/src/test2/test2.java";
	rangesA = featureA.getLocations().get(uri);
	assertEquals(9,rangesA.get(0).nameRange.start.line);
	assertEquals(18,rangesA.get(0).nameRange.start.character);
	assertEquals(9,rangesA.get(0).nameRange.end.line);
	assertEquals(26,rangesA.get(0).nameRange.end.character);
	assertEquals(10,rangesA.get(0).codeBlockRange.start.line);
	assertEquals(0,rangesA.get(0).codeBlockRange.start.character);
	assertEquals(10,rangesA.get(0).codeBlockRange.end.line);
	assertEquals(0,rangesA.get(0).codeBlockRange.end.character);

	//Tests for FeatureB
	Feature featureB = features.get(1);
	assertEquals("FeatureB",featureB.getName());
	assertEquals(false,featureB.getState());

	uri = workingDirectory+"/src/test1/test1.java";
	List<Ranges> rangesB = featureB.getLocations().get(uri);
	assertEquals(24,rangesB.get(0).nameRange.start.line);
	assertEquals(18,rangesB.get(0).nameRange.start.character);
	assertEquals(24,rangesB.get(0).nameRange.end.line);
	assertEquals(26,rangesB.get(0).nameRange.end.character);
	assertEquals(25,rangesB.get(0).codeBlockRange.start.line);
	assertEquals(0,rangesB.get(0).codeBlockRange.start.character);
	assertEquals(27,rangesB.get(0).codeBlockRange.end.line);
	assertEquals(0,rangesB.get(0).codeBlockRange.end.character);

	assertEquals(30,rangesB.get(1).nameRange.start.line);
	assertEquals(28,rangesB.get(1).nameRange.start.character);
	assertEquals(30,rangesB.get(1).nameRange.end.line);
	assertEquals(36,rangesB.get(1).nameRange.end.character);
	assertEquals(31,rangesB.get(1).codeBlockRange.start.line);
	assertEquals(0,rangesB.get(1).codeBlockRange.start.character);
	assertEquals(31,rangesB.get(1).codeBlockRange.end.line);
	assertEquals(0,rangesB.get(1).codeBlockRange.end.character);

	uri = workingDirectory+"/src/test2/test2.java";
	rangesB = featureB.getLocations().get(uri);
	assertEquals(13,rangesB.get(0).nameRange.start.line);
	assertEquals(18,rangesB.get(0).nameRange.start.character);
	assertEquals(13,rangesB.get(0).nameRange.end.line);
	assertEquals(26,rangesB.get(0).nameRange.end.character);
	assertEquals(14,rangesB.get(0).codeBlockRange.start.line);
	assertEquals(0,rangesB.get(0).codeBlockRange.start.character);
	assertEquals(19,rangesB.get(0).codeBlockRange.end.line);
	assertEquals(0,rangesB.get(0).codeBlockRange.end.character);
    } 

    @Test
    void getFeatureListTest() {
	List<String> featureNames = featureServer.getFeatureList();	
	assertEquals(3,featureNames.size());
	assertEquals("FeatureA",featureNames.get(0));
	assertEquals("FeatureB",featureNames.get(1));
	assertEquals("FeatureC",featureNames.get(2));
    }

    @Test
    void getFeatureTest() {
	Feature featureA = featureServer.getFeature("FeatureA");
	assertEquals("FeatureA",featureA.getName());
	assertEquals(true,featureA.getState());

	String uri = workingDirectory+"/src/test1/test1.java";
	List<Ranges> rangesA = featureA.getLocations().get(uri);
	assertEquals(17,rangesA.get(0).nameRange.start.line);
	assertEquals(18,rangesA.get(0).nameRange.start.character);
	assertEquals(17,rangesA.get(0).nameRange.end.line);
	assertEquals(26,rangesA.get(0).nameRange.end.character);
	assertEquals(18,rangesA.get(0).codeBlockRange.start.line);
	assertEquals(0,rangesA.get(0).codeBlockRange.start.character);
	assertEquals(21,rangesA.get(0).codeBlockRange.end.line);
	assertEquals(0,rangesA.get(0).codeBlockRange.end.character);

	assertEquals(30,rangesA.get(1).nameRange.start.line);
	assertEquals(18,rangesA.get(1).nameRange.start.character);
	assertEquals(30,rangesA.get(1).nameRange.end.line);
	assertEquals(26,rangesA.get(1).nameRange.end.character);
	assertEquals(31,rangesA.get(1).codeBlockRange.start.line);
	assertEquals(0,rangesA.get(1).codeBlockRange.start.character);
	assertEquals(31,rangesA.get(1).codeBlockRange.end.line);
	assertEquals(0,rangesA.get(1).codeBlockRange.end.character);

	uri = workingDirectory+"/src/test2/test2.java";
	rangesA = featureA.getLocations().get(uri);
	assertEquals(9,rangesA.get(0).nameRange.start.line);
	assertEquals(18,rangesA.get(0).nameRange.start.character);
	assertEquals(9,rangesA.get(0).nameRange.end.line);
	assertEquals(26,rangesA.get(0).nameRange.end.character);
	assertEquals(10,rangesA.get(0).codeBlockRange.start.line);
	assertEquals(0,rangesA.get(0).codeBlockRange.start.character);
	assertEquals(10,rangesA.get(0).codeBlockRange.end.line);
	assertEquals(0,rangesA.get(0).codeBlockRange.end.character);

    }

    private FeatureServer featureServer;
    private String workingDirectory;
}
