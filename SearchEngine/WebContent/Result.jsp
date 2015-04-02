<%@page import="org.apache.solr.client.solrj.impl.HttpSolrClient"%>
<%@page import="org.apache.solr.client.solrj.SolrClient"%>
<%@page import="org.apache.poi.util.CommonsLogger"%>
<%@page import="it.cache.downloader.query.Searcher"%>
<%@ page import="org.apache.solr.*"%>
<%@ page import="org.apache.solr.common.SolrDocument"%>
<%@ page import="org.apache.solr.common.SolrDocumentList"%>
<%@ page import="org.apache.solr.client.solrj.SolrServer"%>
<%@ page import="org.apache.solr.client.solrj.response.QueryResponse"%>
<%@ page import="org.apache.solr.common.params.ModifiableSolrParams"%>
<%@ page import="org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion"%>
<%@ page import="java.util.*"%>

<html>
<head>
<title>Risultato ricerca</title>
<link rel="stylesheet" type="text/css" href="css/result.css">
<link href='http://fonts.googleapis.com/css?family=Gochi+Hand'
	rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Sniglet'
	rel='stylesheet' type='text/css'>
</head>
<body>
	<table width="100%" border="0" cellpadding="0" cellspacing="0"
		bgcolor="#E1E1E1">
		<tr>
			<td height="70">
				<div class="logo" align="center">SAF</div>
			</td>
			<td><form id="form_ricerca" name="form_ricerca"
					action="Result.jsp" method="get" style="margin-top: 8px">
					<input name="query" class="input_ricerca"
						value="<%=request.getParameter("query")%>"> </input> <img alt=""
						src="img/search.png" width="28" height="28"
						style="margin-left: -35px; margin-bottom: -8px; cursor: pointer"
						onclick='document.form_ricerca.submit()'>
				</form></td>
			<td><img alt="" src="css/img/logoRoma3.jpg"
				style="border-radius: 6px 6px 6px 6px; margin: 5px" align="right"
				width="80px" height="30px"></td>
		</tr>
	</table>
	<table width="100%" border="0" cellpadding="0" cellspacing="0"
		style="margin: 10px">
		<tr>
			<td>
				<%
					String url = new String("http://localhost:8983/solr/admin.html#/CacheDownloader");
					//HttpSolrServer server = new HttpSolrServer(url);
					SolrClient client = new HttpSolrClient(url);
					ModifiableSolrParams params = new ModifiableSolrParams();

					String cont = request.getParameter("cont");
					System.out.println("######################");
					System.out.println("CONT: " + cont);
					if (cont == null)
						cont = "0";
					//			Searcher searcher = new Searcher();
					String word = request.getParameter("query");
					if (word.equals("")) {
						out.clear();
						RequestDispatcher rd = application
								.getRequestDispatcher("/index.jsp");
						rd.forward(request, response);
						return;
					}
				/* 	params.set("qt", "/select");
					params.set("q", word);
					params.set("rows", "10");
					params.set("indent", "on");
					params.set("start", cont);
					params.set("hl", "on");
					params.set("hl.fl", "body");
					params.set("hl.fragsize", "300");
 */
					//searcher.search("/sHandler", word, "10", "on", cont);

					QueryResponse rst = client.query(params);
					SolrDocumentList list = rst.getResults();
					Long size = list.getNumFound();
					Long maxNumPages = new Long(20);
					Long max;
					Long numPages;
					if ((size / 10) > maxNumPages) {
						max = maxNumPages;
						numPages = max;
					} else {
						max = size;
						numPages = max / 10;
					}

					List<Suggestion> suggestions = new LinkedList<Suggestion>(rst
							.getSpellCheckResponse().getSuggestions());
					if (suggestions != null && suggestions.size() > 0) {
				%>
				<div id="suggestion" class="forse_cercavi">
					<b>Forse cercavi...</b>
					<%
						for (Suggestion suggestion : suggestions) {
					%>
					<a href="Result.jsp?query=<%=suggestion.getAlternatives().get(0)%>">
						<%=suggestion.getAlternatives().get(0)%>
					</a>
				</div> <%
 	}
 	}
 %>

				<div id="numberResults">
					About
					<%=size%>
					results (<%=rst.getElapsedTime()%>milliseconds)
				</div> <%
 	String id = null;
 	String text = null;
 	String title = null;
 	String titleVisualizzato = null;
 	String body = null;

 	for (SolrDocument document : list) {
 		id = (String) document.getFieldValue("id").toString();
 		title = (String) document.getFieldValue("title").toString();
 		titleVisualizzato = title.substring(1, (title.length() - 1));
 		body = rst.getHighlighting().get(id).get("body").get(0);
 		text = (String) document.getFieldValue("body").toString();
 %>
				<div id="title">
					<a href=<%=id%> class="titolo_pagina"><%=titleVisualizzato%></a>
				</div>
				<div id="url" class="link_pagina">
					<%=id%>
				</div>
				<div id="text" class="testo_pagina" style="margin-bottom: 10px">
					<p><%=body%></p>
				</div> <%
 	}
 %>
				<div id="numPages">
					<%
						if (numPages != 0) {
							for (int i = 1; i <= numPages + 1; i++) {
					%>
					<a href="Result.jsp?query=<%=word%>&cont=<%=10 * (i - 1)%>"><%=i%></a>
					<%
						}
						}
					%>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>