import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

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
    
    void test02_Question_setup() {
        Choice testc1 = new Choice("test choice", true);
        Choice testc2 = new Choice("test choice", false);
        ArrayList<Choice> testCL = new ArrayList<Choice>();
        testCL.add(testc1);
        testCL.add(testc2);
        Question testQ = new Question(testCL,"do you know the answer?", "meta data", "");
        
        if(!testQ.getQuestion().equals("do you know the answer"))
            fail("wrong question string");
        if(!testQ.getMetaData().equals("meta data"))
            fail("wrong meta data");
        
    }

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
        if(testQ1.getMetaData().equals("meta data"))
            fail("fail to setup multiple choice question");
        
    }
}
