package subscribers.Zad1;

public class User implements Comparable <User>{
	String username;
	public User(String name) {
		this.username = name;
	}
	public String getName(){
		return username;
	}
	@Override
	public int compareTo(User o) {
		return username.compareTo(o.getName());
	}
}
