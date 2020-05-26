package objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Topic implements Serializable {
	private static final long serialVersionUID = -5551518259509104987L;
	public static final String SEPARATOR = "/";
	private String topicName;
	private List<Publication> pubs;
	private Set<String> subtopics;

	public Topic(String topicName) {
		super();
		this.topicName = topicName;
		this.pubs = Collections.synchronizedList(new ArrayList<>());
		this.subtopics = Collections.synchronizedSet(new HashSet<>()); 
	}
	
	public void addSubtopic(String tn) {
		subtopics.add(tn);
	}
	
	public Set<String> getSubtopics() {
		return subtopics;
	}
	
	public String getTopicName() {
		return topicName;
	}
	
	public List<Publication> getPubs() {
		return pubs;
	}
	
	
	
}
