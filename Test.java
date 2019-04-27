import static org.junit.jupiter.api.Assertions.*;

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

}
