import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class Test {

    @org.junit.jupiter.api.Test
    void test01_choice_test() {
        Choice testC1 = new Choice("test choice", false);
        Choice testC2 = new Choice("test Choice 2", true);
        if(!testC1.getChoiceString().equals("test choice"))
            fail("wrong choice string");
        if(!testC2.getChoiceValidity())
            fail("fail to set validity");
    }
    
    @org.junit.jupiter.api.Test
    void test02_question_basic() {
        Choice testC1 = new Choice("test choice", false);
        Choice testC2 = new Choice("test Choice 2", true);
        ArrayList <Choice> choice = new ArrayList<Choice>();
        choice.add(testC1);
        choice.add(testC2);
        Question testQ = new Question(choice, "do you know the answer?", "useless metadata");
        
        if(!testQ.getQuestion().equals("do you know the answer?"))
            fail("wrong question string");
        if(!testQ.getMetaData().equals("useless metadata"))
            fail("wrong meta data");
    }
    
    @org.junit.jupiter.api.Test
    void test03_question_basic() {
        Choice testC1 = new Choice("test choice", false);
        Choice testC2 = new Choice("test Choice 2", true);
        Choice testC3 = new Choice("test choice 3", false);
        Choice testC4 = new Choice("test choice 4", false);
        ArrayList <Choice> choice = new ArrayList<Choice>();
        choice.add(testC1);
        choice.add(testC2);
        choice.add(testC3);
        choice.add(testC4);
        Question testQ = new Question(choice, "do you know the answer?", "useless metadata");
        
        if(testQ.getMultipleChoice())
            fail("fail to set to a single choice question");
        
        
        Choice testC5 = new Choice("test choice", true);
        Choice testC6 = new Choice("test Choice 2", true);
        Choice testC7 = new Choice("test choice 3", false);
        Choice testC8 = new Choice("test choice 4", false);
        ArrayList <Choice> choice2 = new ArrayList<Choice>();
        choice2.add(testC5);
        choice2.add(testC6);
        choice2.add(testC7);
        choice2.add(testC8);
        Question testQ2 = new Question(choice2, "do you know the answer?", "useless metadata");
        if(!testQ2.getMultipleChoice())
            fail("fail to set to a multipule choice question");
    }

}
