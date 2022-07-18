package subscribers.Zad1;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.function.Function;

/**
 * Represents data that will be sent pver the network.
 * Helps preparing payload to send.
 *
 */
public abstract class Payload {
	
	List<TransitibleData> transitData = new ArrayList<>();
	final static String op="{", cl="}", sep=";"; // open close separator
	protected String payload;
	protected Payload() {
		
	}
	/**
	 * 
	 * @param payload
	 * @param maps map containing functions to form data from string and indexes in payload from which to apply function
	 * @throws Exception 
	 */
	public Payload(String payload, SortedMap<Integer, Function<String, TransitibleData>> maps, int to) throws Exception {
		this.payload = this.extractPayload(payload);
		Function<String, TransitibleData> toTrans = maps.get(maps.firstKey());
		final int limit = to<0 ? this.contentCount(this.payload) : to;
		for(int i=maps.firstKey(); i<limit; i++) {
			if(maps.keySet().contains(i)) toTrans = maps.get(i);
			final String segment = Payload.readContent(i, this.payload);
			transitData.add(toTrans.apply(segment));
		}
	}
	
	/**
	 * Loads data to this object the order does matter.
	 * @param data data to load to this object.
	 */
	public void addTransData(TransitibleData data) {
		if(data==null) throw new NullPointerException();
		this.transitData.add(data);
	}
	
	public void addTransDatas(List<TransitibleData> datas) {
		if(datas==null) throw new NullPointerException();
		for(TransitibleData data : datas)
			this.transitData.add(data);
	}
	
	public TransitibleData getTransData(int i) {
		return this.transitData.get(i);
	}
	
	public int transDataCount() {
		return this.transitData.size();
	}
	
	/**
	 * Makes text representation to sent via network.
	 * @return String representation of this message
	 */
	public String getMessage() {
		
		StringBuilder bdr = new StringBuilder();
		bdr.append(this.op + this.preambule() + this.cl);
		final int dataSize = this.transitData.size();
		if(dataSize>0) {
			bdr.append(this.op);
			for(int i=0; i<dataSize-1; i++) {
				bdr.append(this.transitData.get(i).toSentMessage()+this.sep);
			}
			bdr.append(this.transitData.get(dataSize-1).toSentMessage()+this.cl);
		}
		return bdr.toString()+this.sep+this.sep;
	}
	/**
	 * Counts how many segments there are in a message {} is one segment
	 * @param message
	 * @return
	 * @throws Exception
	 */
	protected static int segmentCount(String message) throws Exception {
		int escape = 0;
		int next = 0;
		String op="{", cl="}";
		StringBuilder extraction=new StringBuilder();//op);
		boolean started = false;
		for(int i = 0; i<message.length(); i++){
			if(message.charAt(i)==op.charAt(0))	escape++;
			if(message.charAt(i)==cl.charAt(0))	escape--;
			if(started)	extraction.append(message.charAt(i));
			if(escape==0) next++;
		}
		return next;
	}
	
	/**
	 * Extracts payload from the form {something}{something else}
	 * @param which segment have to be taken
	 * @param payload
	 * @return
	 */
	protected static String extractPayload(Integer which, String payload) {
		int escape = 0;
		int next = 0;
		String op="{", cl="}";
		StringBuilder extraction=new StringBuilder();
		boolean started = false;
		for(int i = 0; i<payload.length() && next<=which; i++){
			if(payload.charAt(i)==op.charAt(0)) {
				if(escape==0 && next==which) started=true;
				escape++;
			}
			if(payload.charAt(i)==cl.charAt(0)) {
				escape--;
				
			}
			if(started) {
				extraction.append(payload.charAt(i));
			}
			if(escape==0) next++;
		}
		return extraction.toString();
	}
	/**
	 * Reads the {one;two;three} content.
	 * @param which
	 * @param payload
	 * @return
	 * @throws Exception 
	 */
	protected static String readContent(Integer which, String payload) throws Exception {
		int escape = 0;
		int next = 0;
		payload = TransitibleData.truncBrackets(payload);
		
		String op="{", cl="}", sep=";";
		StringBuilder extraction=new StringBuilder();
		boolean started = false;
		for(int i = 0; i<payload.length() && next<=which; i++){
			if(payload.charAt(i)==op.charAt(0)) {
				if(escape==0 && next==which) started=true;
				escape++;
			}
			if(payload.charAt(i)==cl.charAt(0)) {
				escape--;
			}
			if(payload.charAt(i)!=sep.charAt(0) && next==which) {
				started = true;
			}
			if(payload.charAt(i)==sep.charAt(0) && escape==0) {
				started = false;
			}
			if(started) {
				extraction.append(payload.charAt(i));
			}
			if(escape==0 && payload.charAt(i)==sep.charAt(0)) next++;
		}
		return extraction.toString();
	}
	/**
	 * counts the {one;two;three} content
	 * @param which
	 * @param payload
	 * @return
	 * @throws Exception 
	 */
	protected static Integer contentCount(String payload) throws Exception {
		int escape = 0;
		int next = 0;
		payload = TransitibleData.truncBrackets(payload);
		String op="{", cl="}", sep=";";
		if(payload.isBlank()) return 0;
		boolean started = false;
		for(int i = 0; i<payload.length(); i++){
			if(payload.charAt(i)==op.charAt(0)) {
				escape++;
			}
			if(payload.charAt(i)==cl.charAt(0)) {
				escape--;
			}
			
			if(escape==0 && payload.charAt(i)==sep.charAt(0)) next++;
		}
		return next+1;
	}
	/**
	 * Defoult payload is the {first}{secount} secound segment.
	 * @return
	 */
	public String  getPayload() {
		this.payload = extractPayload(1, this.getMessage());
		return this.payload;
	}
	/**
	 * starting of the message identifying operation
	 * @return
	 */
	protected abstract String preambule();
	protected abstract String extractPayload(String toExtract);
}
