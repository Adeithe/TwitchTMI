import tv.twitch.ClientBuilder;
import tv.twitch.TwitchClient;
import tv.twitch.api.TwitchAPI;

public class APITest {
	public static void main(String[] args) throws Exception {
		ClientBuilder Builder = new ClientBuilder();
				Builder.withClientID(System.getenv("CLIENT_ID"));
				Builder.withClientSecret(System.getenv("CLIENT_SECRET"));
		
		TwitchClient Client = Builder.build();
		
		try {
			System.out.println(Client.getAPI(System.getenv("TWITCH_OAUTH_TEST")).getVersions().helix.getUsers().getOwnUser().toString());
		} catch(TwitchAPI.APIException e) {
			System.out.println(e.toString());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
