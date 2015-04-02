<%@page import="org.apache.solr.logging.log4j.Log4jInfo"%>
<%@page import="org.apache.solr.common.SolrDocument"%>
<%@page import="org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion"%>
<%@page import="org.apache.solr.common.SolrDocumentList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="it.cache.downloader.query.Searcher"%>
<%@ page import="org.apache.solr.client.solrj.response.QueryResponse"%>
<%@ page import="java.util.*"%>
<%@page import="org.apache.solr.client.solrj.impl.HttpSolrServer"%>
<%@page import="org.apache.solr.client.solrj.SolrQuery" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Risultato</title>
</head>
<body>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td><img alt="" src="img/CacheDownloaderl.png"
				style="border-radius: 15px 15px 15px 15px;" align="left"
				width="350px" height="90px"></td>
				
			<td><form id="form_ricerca" name="form_ricerca"
					action="altro.jsp" method="get" style="margin-top: 15px">
					<input name="query" class="input_ricerca" style="margin-right: 20px;" align="left"
						value="<%=request.getParameter("query")%>"> </input> <img alt=""
						src="img/search.png" width="60" height="60"
						style="margin-right: 150px; margin-bottom: -8px; cursor: pointer" align="center"
						onclick='document.form_ricerca.submit()'>
				</form></td>

		</tr>
	</table>
	
	<table width="100%" border="0" cellpadding="0" cellspacing="0"
		style="margin: 10px">
		<tr>
			<td>
			
		<% Searcher searcher = new Searcher();
		 	String query = request.getParameter("query");
		 	String cont = request.getParameter("cont");
		 	//searcher.test(query);
			QueryResponse resp = searcher.search("/sHandler", query, "10", "on", cont);
			
			System.out.println("######################");
			System.out.println( "query: " + query);
			
			
			SolrDocumentList list = resp.getResults();
			
			if(list.isEmpty()) {
				System.out.println("Vuota");
			} else {	
				System.out.println("PIENA"); 
			}
			
			Long size = list.getNumFound();
			Long maxNumPages = new Long(8);
			Long max;
			Long numPages;
			if ((size / 2) > maxNumPages) {
				max = maxNumPages;
				numPages = max;
			} else {
				max = size;
				numPages = max / 2;
			}
			
			System.out.println( "size: " + size);
			System.out.println( "max: " + max);
			
			if(resp.getSpellCheckResponse() == null){
				System.out.println("NULLO");
			} else {	
				System.out.println("NON NULLO"); 
			}
			
			if(resp.getSpellCheckResponse().getSuggestions().isEmpty()){
				System.out.println("Vuota SPELLCHECK");
			} else {	
				System.out.println("PIENA SPELLCHECK"); 
			}
			
 			List<Suggestion> suggestions = new LinkedList<Suggestion>(resp.getSpellCheckResponse().getSuggestions());
			if (suggestions != null && suggestions.size() > 0) {
		%>
		<div id="suggestion" class="forse_cercavi">
			<b>Forse cercavi...</b>
			<%
				for (Suggestion suggestion : suggestions) {
			%>
			<a href="altro.jsp?query=<%=suggestion.getAlternatives().get(0)%>">
				<%=suggestion.getAlternatives().get(0)%>
			</a>
		</div> <%
 	}
 	}
 %>	 
 
			<div id="numPages"  style="border-radius: 15px 15px 15px 15px; margin:20px" align="right">
			<%
				if (numPages != 0) {
					for (int i = 1; i <= numPages + 1; i++) {
			%>
			<a href="altro.jsp?query=<%=query%>&cont=<%=10 * (i - 1)%>"><%=i%></a>
			<%
				}
				}
			%>
		</div>
 
		<div id="numberResults">
			About <%=size%>	 results (<%=resp.getElapsedTime()%>milliseconds) 
				</div> 
	<% System.out.println( "resp.getElapsedTime(): " + resp.getElapsedTime());
	
	 	String id = null;
	 	String text = null;
	 	String title = null;
	 	String titleVisualizzato = null;
	 	String body = null;
	 	String url = null;
	
 	 	for (SolrDocument document : list) {
 	 		
 	 		System.out.println( "FORRRRRRRRRR: ");
 	 		
	 		id = (String) document.getFieldValue("id").toString();
	 		title = (String) document.getFieldValue("title").toString();
	 		titleVisualizzato = title.substring(1, (title.length() - 1));
	 		body = (String) document.getFieldValue("content").toString();
	 		url = (String) document.getFieldValue("url").toString();
	 	//	body = resp.getHighlighting().get(id).get("content").get(0);
	 	//	text = (String) document.getFieldValue("content").toString();
	 	
	 %>
	 
				<div id="title">
					<a href=<%=url%> class="titolo_pagina"><%=titleVisualizzato%></a>
				</div>
				<div id="url" class="link_pagina">
					<%=url%>
				</div>
				<div id="text" class="testo_pagina" style="margin-bottom: 10px">
					<p><%=body%></p>
				</div> <%
	}
 %>

			</td>
		</tr>
	</table>
</body>
</html>