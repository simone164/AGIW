package it.cache.downloader.test;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

public class Scarica {
	
	
	public static void main(String[] args) {
	    try {
	    	
	    	deleteAllSolrData();
	    	
	    } catch (Exception e) {
	        e.printStackTrace();
	      }
	    
	    }
	
	public static void deleteAllSolrData() {
		int i = 0;
	    HttpSolrServer solr = new HttpSolrServer("http://localhost:8983/solr/CacheDownloader");
	    try {
	      solr.deleteByQuery("*:*");
	    } catch (SolrServerException e) {
	      throw new RuntimeException("Failed to delete data in Solr. "
	          + e.getMessage() , e);
	    } catch (IOException e) {
	      throw new RuntimeException("Failed to delete data in Solr. "
	          + e.getMessage(), e);
	    }
	}

}
