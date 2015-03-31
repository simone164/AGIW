package it.cache.downloader.query;



import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

public class SolrServerFactory {
	private static SolrServerFactory instance;
	private SolrServer solrServer;
	
	private SolrServerFactory() {
		try{
		solrServer = new HttpSolrServer("http://localhost:8983/solr/#/CacheDownloader/");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static SolrServerFactory getInstance() {
		if (instance == null) {
			instance = new SolrServerFactory();
		}
		return instance;
	}
	
	public SolrServer getServer(){
		return this.solrServer;
	}
}