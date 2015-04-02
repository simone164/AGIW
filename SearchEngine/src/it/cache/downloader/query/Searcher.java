package it.cache.downloader.query;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;

public class Searcher {

	private SolrServer server= SolrServerFactory.getInstance().getServer();

	public QueryResponse search(String qt, String q, String rows, String indent, String start){
		ModifiableSolrParams params = new ModifiableSolrParams();
		params.set("qt", qt);
		params.set("q", q);
		params.set("rows", rows);
		params.set("indent", indent);
		params.set("start", start);
		params.set("hl", "on");
		params.set("hl.fl", "body");
		params.set("hl.fragsize", "300");
		QueryResponse response = null;
		try{
			response = this.server.query(params);
		} catch (SolrServerException e){
			e.printStackTrace();
		}
		return response;
	}	
	
	public QueryResponse test(String str) throws SolrServerException{
		HttpSolrServer client = new HttpSolrServer("http://localhost:8983/solr/CacheDownloader");
		SolrQuery query = new SolrQuery();
		query.setQuery(str);
		query.setFields("id", "content", "title", "url");
		query.setStart(0);
		QueryResponse response = client.query(query);
		return response;
	}
}
