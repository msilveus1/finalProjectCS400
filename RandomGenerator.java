import java.util.ArrayList;
import java.util.Random;
/**
 * This class generates a random number of questions to select from each topic
 * @author Matthew Silveus
 *
 */
public class RandomGenerator {

	public RandomGenerator() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * This comes up with a random sequence of numbers based off the number of topics
	 * and the number of questions
	 * @param topics
	 * @param questions
	 */
	public ArrayList<TopicQuestionCountPair> randomSequenceGenerator(int topics, int questions) {
		Random rand = new Random();
		//This is the random sequence generator
		//This counts how many questions are left 
		int questionCounter = questions; 
		int topicCounter = topics;
		ArrayList<TopicQuestionCountPair> generatedNumbers = new ArrayList<TopicQuestionCountPair>();
		//Create the list to store objects
		for (int i=1;i<topics;i++) {
			topicCounter--;
			//This generates the random numbers for each question
			int numberQ= rand.nextInt(questionCounter-topicCounter);
			//So there is an assurance of that at least one question will be picked from each topic
			//But it will be a random
			questionCounter = questionCounter-numberQ;
			TopicQuestionCountPair temp = new TopicQuestionCountPair(i,numberQ);
			//This will add one more
			generatedNumbers.add(temp);
		}
		//Finally the remaining questions will be added for the last topic number
		TopicQuestionCountPair r = new TopicQuestionCountPair(topics,questionCounter);
		generatedNumbers.add(r);
		return generatedNumbers;
	}
	/**
	 * This is an inner class to help with the pairing
	 * @author matthew Silveus
	 *
	 */
	private class TopicQuestionCountPair{
		int topic;
		int questions;
		public TopicQuestionCountPair(int topic, int questions){
			this.topic=topic;
			this.questions=questions;
		}
		protected int intgetTopicNumber(){
			return topic;
		}
		protected int intgetQuestionNumber() {
			return questions;
		}
	}
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
