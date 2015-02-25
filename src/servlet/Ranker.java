package servlet;

import java.sql.SQLException;
import java.util.*;

public class Ranker {
	
	private String[] query;
	private int numOfDocs;
	private Map<Integer, Double> urlCosineSimilarity; //Integer is the docID
	
	/*
	 * Constructor takes a query string
	 */
	
	public Ranker(String query){
		this.query = tokenizeQuery(query);
		this.urlCosineSimilarity = new HashMap<Integer, Double>();
	}
	
	public static String[] tokenizeQuery(String query){
		
		
		
		return null;
	}
	
	/*
	 * The following method actually searches the database and computes the cosine
	 * similarity for each document to the query. This will be stored in our internal
	 * HashMap.
	 */
	
	public void search() throws SQLException{
		
		
		
	}	
	
	/*
	 * This method sorts our search results by the computed cosine similarity and returns
	 * a list of URL strings based upon the doc ID.
	 */
	
	public List<String> getSearchResults(){
		
		TreeMap<Integer, Double> sorted = sortDocs(urlCosineSimilarity);
		ArrayList<String> sortedDocs = new ArrayList<>();
		for (Map.Entry<Integer, Double> entry : sorted.entrySet()){
			
		}

		
		//Record the size of arraylist while we are are it
		this.numOfDocs = sortedDocs.size();
		
		return sortedDocs;
	}
	
	/*
	 * Helper function to sort HashMap into TreeMap
	 */
	
	public static TreeMap<Integer, Double> sortDocs(Map<Integer, Double> unsorted){
		//Now that we have counted all the terms, we must sort our hash values to print most frequent terms
		DoubleComparator compare = new DoubleComparator(unsorted);
		TreeMap<Integer, Double> sortedTermFrequency = new TreeMap<Integer, Double>(compare);
		sortedTermFrequency.putAll(unsorted);
		return sortedTermFrequency;
	}
	
	/*
	 * Here is our comparator for the TreeMap
	 */
	
	public final static class DoubleComparator implements Comparator<Integer> {
	    Map<Integer, Double> map;
	    //Constructor
	    public DoubleComparator(Map<Integer,Double> construct){
	        this.map = construct;
	    }
	    //Implementing compare method, does not return 0 because we do not want to merge keys
	    public int compare(Integer x, Integer y) {
	        if (map.get(x) >= map.get(y)) {
	            return -1;
	        } else {
	            return 1;
	        } 
	    }
	}
	 
	
	

}
