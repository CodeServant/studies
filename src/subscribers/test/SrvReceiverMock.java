package subscribers.test;

import java.util.Set;

import subscribers.Zad1.*;

public class SrvReceiverMock extends SrvReceiver {

	public SrvReceiverMock(String ms) {
		super(ms);
		
	}
	public Set<Topic> justTopics() throws Exception {
		return super.justTopics();
	}
	public Set<Information> justInformations() {
		return super.justInformations();
	}
	public Information infoWithTopics() throws Exception{
		return super.infoWithTopics();
	}
}
