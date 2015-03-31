package it.cache.downloader.query;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
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
}
