import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tfidf.TfidfCalculator;
import tfidf.TfidfUtils;
import tfidf.contentRetrievers.ContentRetrieverType;
import tfidf.entities.TfidfResult;
import tfidf.entities.TfidfTopScores;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TfidfTests {

    private String testDocsPath = "C:\\Guni\\TestMaven\\src\\test\\TestDocsPaths.txt";
    private String doc1Path = "C:\\Guni\\TestMaven\\src\\test\\TestDoc1.txt";
    private String doc2Path = "C:\\Guni\\TestMaven\\src\\test\\TestDoc2.txt";
    private String doc1Content = "this is a sample a";
    private String doc2Content = "another example, this is another example. example.";

    Map<String, Double> doc1ExpectedTfMap;
    Map<String, Double> doc2ExpectedTfMap;
    Map<String, Set<String>> expectedCorpus;
    Map<String, Map<String, Double>> expectedDocument2TfMap;
    TfidfResult expectedResult;

    @Before
    public void init(){
        doc1ExpectedTfMap = new HashMap<>(4);
        doc1ExpectedTfMap.put("this", 0.2);
        doc1ExpectedTfMap.put("is", 0.2);
        doc1ExpectedTfMap.put("a", 0.4);
        doc1ExpectedTfMap.put("sample", 0.2);

        doc2ExpectedTfMap = new HashMap<>(4);
        doc2ExpectedTfMap.put("this", TfidfUtils.doubleToPrecision((double)1/7));
        doc2ExpectedTfMap.put("is", TfidfUtils.doubleToPrecision((double)1/7));
        doc2ExpectedTfMap.put("another", TfidfUtils.doubleToPrecision((double)2/7));
        doc2ExpectedTfMap.put("example", TfidfUtils.doubleToPrecision((double)3/7));

        expectedDocument2TfMap = new HashMap<>(2);
        expectedDocument2TfMap.put(doc1Path, doc1ExpectedTfMap);
        expectedDocument2TfMap.put(doc2Path, doc2ExpectedTfMap);

        expectedCorpus = new HashMap<>(6);
        expectedCorpus.put("this", new HashSet<>(Arrays.asList(doc1Path, doc2Path)));
        expectedCorpus.put("is", new HashSet<>(Arrays.asList(doc1Path, doc2Path)));
        expectedCorpus.put("a", new HashSet<>(Collections.singletonList(doc1Path)));
        expectedCorpus.put("sample", new HashSet<>(Collections.singletonList(doc1Path)));
        expectedCorpus.put("another", new HashSet<>(Collections.singletonList(doc2Path)));
        expectedCorpus.put("example", new HashSet<>(Collections.singletonList(doc2Path)));

        expectedResult = new TfidfResult(expectedCorpus, expectedDocument2TfMap, 2);

    }

    @Test
    public void testTfMapCalculation() throws IOException, InterruptedException {
        Map<String, Double> tfMap1 = TfidfUtils.getTfMap(TfidfUtils.getWordsArrayOfContent(doc1Content));
        Map<String, Double> tfMap2 = TfidfUtils.getTfMap(TfidfUtils.getWordsArrayOfContent(doc2Content));

        Assert.assertEquals(tfMap1, doc1ExpectedTfMap);
        Assert.assertEquals(tfMap2, doc2ExpectedTfMap);
    }

    @Test
    public void testTfidfResultCalculation() throws IOException, InterruptedException {
        TfidfCalculator calculator = new TfidfCalculator(testDocsPath, 2);
        TfidfResult result = calculator.calculate(ContentRetrieverType.Local);

        Assert.assertEquals(result, expectedResult);
    }

    @Test
    public void testRegex(){

        String test = "With HBO GO you can watch big premieres as they air—plus every episode of HBO's addictive series such as Game of Thrones, Westworld, Big Little Lies, and so much more.\n \nIn addition to getting new movies every week, you also get news, comedy specials, smart talk shows, thought-provoking documentaries, and the special events everyone's talking about.\n\nYou can watch HBO GO on your favorite screen—phone, tablet, computer, connected TV or gaming device.\n\nHBO GO® is only accessible in the U.S. and certain U.S. territories. Some restrictions apply. ©2017 Home Box Office, Inc. All rights reserved. HBO GO® and related channels and service marks are the property of Home Box Office, Inc.\n\nFor help, please visit help.hbogo.com.\n\nPrivacy Policy: http://play.hbogo.com/privacy \nTerms of Service:  http://play.hbogo.com/terms";
        String[] bySpaces = test.split("\\s+");
        for(int index =0; index<bySpaces.length; index++){
            bySpaces[index] = bySpaces[index].replaceAll("\\.|,", "");
        }
        System.out.println(bySpaces);
//        String afterRegex = test.replaceAll(simpleRegex, " ");
//        System.out.println(afterRegex);
    }
}
