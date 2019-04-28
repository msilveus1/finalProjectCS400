import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.parser.ParseException;

class Test {

    @org.junit.jupiter.api.Test
    void test01_choice_setup() {
        Choice testc1 = new Choice("test choice", true);
        Choice testc2 = new Choice("test choice", false);
        if(!testc1.getChoiceString().equals("test choice"))
            fail("wrong choice string");
        if(testc2.getChoiceValidity())
            fail("fail to set validity");
    }
    
    @org.junit.jupiter.api.Test
    void test02_Question_setup() {
        Choice testc1 = new Choice("test choice", true);
        Choice testc2 = new Choice("test choice", false);
        ArrayList<Choice> testCL = new ArrayList<Choice>();
        testCL.add(testc1);
        testCL.add(testc2);
        Question testQ = new Question(testCL,"do you know the answer?", "meta data", "");
        
        if(!testQ.getQuestion().equals("do you know the answer?"))
            fail("wrong question string");
        if(!testQ.getMetaData().equals("meta data"))
            fail("wrong meta data");
        
    }

    @org.junit.jupiter.api.Test
    void test03_Question_multipleChoice() {
        Choice testc1 = new Choice("test choice", true);
        Choice testc2 = new Choice("test choice", false);
        ArrayList<Choice> testCL = new ArrayList<Choice>();
        testCL.add(testc1);
        testCL.add(testc2);
        Question testQ = new Question(testCL,"do you know the answer?", "meta data", "");
        
        if(testQ.getMultipleChoice())
            fail("fail to setup single choice");
        
        Choice testc3 = new Choice("test choice", true);
        Choice testc4 = new Choice("test choice", true);
        ArrayList<Choice> testCL1 = new ArrayList<Choice>();
        testCL1.add(testc3);
        testCL1.add(testc4);
        Question testQ1 = new Question(testCL1,"do you know the answer?", "meta data", "");
        if(!testQ1.getMultipleChoice())
            fail("fail to setup multiple choice question");
        
    }
    
    @org.junit.jupiter.api.Test
    void test04_Reader_FileNotExist() {
        Reader testReader = new Reader(new HashMap<String, ArrayList<Question>>());
        try{
            testReader.parseJSONFile("nonexist location");
        }
        catch(FileNotFoundException e) {
        }
        catch(JSONInputException e) {
            fail("unexpected exception");
        }
        catch(ParseException e) {
            fail("unexpected exception");
        }
        
        catch(Exception e) {
            fail("unexpected exception");
        }
    }
    
    @org.junit.jupiter.api.Test
    void test05_Reader_correctFile() {
        Reader testReader = new Reader(new HashMap<String, ArrayList<Question>>());
        try{
            testReader.parseJSONFile("test.json");
        }
        catch(FileNotFoundException e) {
            fail("unexpected exception1");
        }
        catch(JSONInputException e) {
            fail("unexpected exception2");
        }
        catch(ParseException e) {
            fail("unexpected exception3");
        }
        
        catch(Exception e) {
            e.printStackTrace();
            fail("unexpected exception4");
        }
    }
    
    
    @org.junit.jupiter.api.Test
    void test06_Reader_ImageNotExist() {
        Reader testReader = new Reader(new HashMap<String, ArrayList<Question>>());
        try{
            testReader.parseJSONFile("test2.json");
        }
        catch(FileNotFoundException e) {
            fail("unexpected exception1");
        }
        catch(JSONInputException e) {
        }
        catch(ParseException e) {
            fail("unexpected exception3");
        }        
        catch(Exception e) {
            e.printStackTrace();
            fail("unexpected exception4");
        }
    }
}
