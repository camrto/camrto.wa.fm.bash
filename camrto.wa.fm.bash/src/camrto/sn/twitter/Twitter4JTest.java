package camrto.sn.twitter;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Twitter4JTest {
	
	public static void main(String[] args) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("Cj2Vj9nlKMBNr8i6HAcpg")
		  .setOAuthConsumerSecret("V735rEXwSJ5x5ZuZUJS4BBldw282IDv2qBN4nxxRK8A")
		  .setOAuthAccessToken("116465178-kzbjgUapgI5EwcqnSCxiITNBZO75OVj24DIaMZFz")
		  .setOAuthAccessTokenSecret("XEtB00mYxBEI1Lw4glyaJPbP8QM28fz9dmUyG1tmgk");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		Status status=null;
		try {
			status = twitter.updateStatus("es pepe el toro inocente?");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println("Successfully updated the status to [" + status.getText() + "].");
	}

}
