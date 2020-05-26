package objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Topic implements Serializable {
	private static final long serialVersionUID = -5551518259509104987L;
	private String topicName;
	List<Publication> pubs;
	Set<String> subtopics;
	
	public Topic(String topicName) {
		super();
		this.topicName = topicName;
		this.pubs = Collections.synchronizedList(new ArrayList<>());
		this.subtopics = Collections.synchronizedSet(new HashSet<>()); 
	}
	
	public String getTopicName() {
		return topicName;
	}
	
	public List<Publication> getPubs() {
		return pubs;
	}
	
	
	
}
