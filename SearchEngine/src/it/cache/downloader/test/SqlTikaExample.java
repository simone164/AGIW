package it.cache.downloader.test;


import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
//import org.apache.solr.client.solrj.impl.StreamingUpdateSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.CompositeParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.LinkContentHandler;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

/* Example class showing the skeleton of using Tika and
   Sql on the client to index documents from
   both structured documents and a SQL database.

   NOTE: The SQL example and the Tika example are entirely orthogonal.
   Both are included here to make a
   more interesting example, but you can omit either of them.

 */
public class SqlTikaExample {
  private HttpSolrServer _server;
  private long _start = System.currentTimeMillis();
  private AutoDetectParser _autoParser;
  private int _totalTika = 0;
  private int _totalSql = 0;
  private static int count = 0;
  

  private Collection _docs = new ArrayList();

  public static void main(String[] args) {
    try {
      SqlTikaExample idxer = new SqlTikaExample("http://localhost:8983/solr/CacheDownloader");
      
      idxer.doTikaDocuments(new File("/Users/Stefano/Documents/DatiParsati"));
      idxer.doSqlDocuments();
      idxer.endIndexing();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private SqlTikaExample(String url) throws IOException, SolrServerException {
      // Create a multi-threaded communications channel to the Solr server.
      // Could be CommonsHttpSolrServer as well.
      //
    _server = new HttpSolrServer(url);
    _server.deleteByQuery("*:*");

    _server.setSoTimeout(1000000000);  // socket read timeout
    _server.setConnectionTimeout(1000000000);
    _server.setMaxRetries(1); // defaults to 0.  > 1 not recommended.
         // binary parser is used by default for responses
    //_server.setParser(new XMLResponseParser()); 

      // One of the ways Tika can be used to attempt to parse arbitrary files.
    _autoParser = new AutoDetectParser();
  }

    // Just a convenient place to wrap things up.
  private void endIndexing() throws IOException, SolrServerException {
    if (_docs.size() > 0) { // Are there any documents left over?
      _server.add(_docs, 300000); // Commit within 5 minutes
    }
    _server.commit(); // Only needs to be done at the end,
                      // commitWithin should do the rest.
                      // Could even be omitted
                      // assuming commitWithin was specified.
    long endTime = System.currentTimeMillis();
    log("Total Time Taken: " + (endTime - _start) +
         " milliseconds to index " + _totalSql +
        " SQL rows and " + _totalTika + " documents");
  }
  
  public  void parserCustom(File file) throws IOException, SAXException, TikaException{
	  	InputStream input = new FileInputStream(file);
	    LinkContentHandler linkHandler = new LinkContentHandler();
	    ContentHandler textHandler = new BodyContentHandler();
	    ToHTMLContentHandler toHTMLHandler = new ToHTMLContentHandler();
	    TeeContentHandler teeHandler = new TeeContentHandler(new ContentHandler[] { linkHandler, textHandler, toHTMLHandler });
	    Metadata metadata = new Metadata();
	    ParseContext parseContext = new ParseContext();
	    HtmlParser parser = new HtmlParser();
	    parser.parse(input, teeHandler, metadata, parseContext);
	    log("Dumping metadata for file: " );
	    for (String name : metadata.names()) {
	      log(name + ":" + metadata.get(name) + ("title:\n" + metadata.get("title"))
	    		  + ("links:\n" + linkHandler.getLinks()) + ("text:\n" + textHandler.toString())
	    				  +("html:\n" + toHTMLHandler.toString()));
	    }
	    log("\n\n");
//	    
//	    System.out.println("title:\n" + metadata.get("title"));
//	    System.out.println("links:\n" + linkHandler.getLinks());
//	    System.out.println("text:\n" + textHandler.toString());
//	    System.out.println("html:\n" + toHTMLHandler.toString());
  }

  // I hate writing System.out.println() everyplace,
  // besides this gives a central place to convert to true logging
  // in a production system.
  private static void log(String msg) {
	  String counter = Integer.toString(count);
    System.out.println(msg + " " + counter);
  }

//  private void parserJsoup(File root) throws IOException, SolrServerException{
//	  for (File file : root.listFiles()) {
//	      if (file.isDirectory()) {
//	    	  parserJsoup(file);
//	    	  continue;
//	      }
//	     
//	      Document i = Jsoup.parse(file.getCanonicalPath(), "UTF-8");
//		//Element link = i.select("a").first();
//		//String text = i.body().text(); 
//		String body = i.body().text();
//		System.out.println(body);
//		String title = i.title();
//		
//		 SolrInputDocument doc = new SolrInputDocument();
//		 
//		 doc.addField("id", i.id());
//		 //doc.addField("body", i.body());
//		 doc.addField("content", i.body());
//		// doc.addField("url", i.);
//		 doc.addField("title", i.title());
//		
//		 
//	        UpdateResponse resp = _server.add(doc);
//	       
//	        if (resp.getStatus() != 0) {
//	          log("Some horrible error has occurred, status is: " +
//	                  resp.getStatus());
//	        
//	      }
//		
//	  }
//		
//  }
  
  /**
   * ***************************Tika processing here
   */
  // Recursively traverse the filesystem, parsing everything found.
  private void doTikaDocuments(File root) throws IOException, SolrServerException {

    // Simple loop for recursively indexing all the files
    // in the root directory passed in.
    for (File file : root.listFiles()) {
      if (file.isDirectory()) {
        doTikaDocuments(file);
        continue;
      }
        // Get ready to parse the file.
      ContentHandler textHandler = new BodyContentHandler();
      Metadata metadata = new Metadata();
      ParseContext context = new ParseContext();

      InputStream input = new FileInputStream(file);

        // Try parsing the file. Note we haven't checked at all to
        // see whether this file is a good candidate.
      try {
    	  //parserCustom(file);
    	 
        _autoParser.parse(input, textHandler, metadata, context);
      } catch (Exception e) {
          // Needs better logging of what went wrong in order to
          // track down "bad" documents.
        log(String.format("File %s failed", file.getCanonicalPath()));
        e.printStackTrace();
        continue;
      }
      // Just to show how much meta-data and what form it's in.
      dumpMetadata(file.getCanonicalPath(), metadata);

      // Index just a couple of the meta-data fields.
      SolrInputDocument doc = new SolrInputDocument();

      Document i = Jsoup.parse(file, "UTF-8"); 
		String body = i.body().text();
		Elements links = i.select("a[href]");
		Element link = links.first();
		
      
      doc.addField("id", file.getCanonicalPath());
      doc.addField("title", metadata.get("title"));
      doc.addField("content", body);
      if(link != null){
      doc.addField("url", link.attr("abs:href"));
      }
      // Crude way to get known meta-data fields.
      // Also possible to write a simple loop to examine all the
      // metadata returned and selectively index it and/or
      // just get a list of them.
      // One can also use the LucidWorks field mapping to
      // accomplish much the same thing.
      String author = metadata.get("Author");
      

      if (author != null) {
        doc.addField("author", author);
      }

      doc.addField("text", textHandler.toString());

      _docs.add(doc);
      ++_totalTika;
      ++count;

      // Completely arbitrary, just batch up more than one document
      // for throughput!
      if (count == 100) {
          // Commit within 5 minutes.
        UpdateResponse resp = _server.add(_docs);
        count = 0;
        if (resp.getStatus() != 0) {
          log("Some horrible error has occurred, status is: " +
                  resp.getStatus());
        }
        _docs.clear();
      }
    }
  }

    // Just to show all the metadata that's available.
  private void dumpMetadata(String fileName, Metadata metadata) {
    log("Dumping metadata for file: " + fileName);
    for (String name : metadata.names()) {
      log(name + ":" + metadata.get(name));
    }
    log("\n\n");
  }

  /**
   * ***************************SQL processing here
   */
  private void doSqlDocuments() throws SQLException {
    Connection con = null;
    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      log("Driver Loaded......");

      con = DriverManager.getConnection("jdbc:mysql://192.168.1.103:3306/test?"
                + "user=testuser&password=test123");

      Statement st = con.createStatement();
      ResultSet rs = st.executeQuery("select id,title,text from test");

      while (rs.next()) {
        // DO NOT move this outside the while loop
        // or be sure to call doc.clear()
        SolrInputDocument doc = new SolrInputDocument();
        String id = rs.getString("id");
        String title = rs.getString("title");
        String text = rs.getString("text");

        doc.addField("id", id);
        doc.addField("title", title);
        doc.addField("text", text);

        _docs.add(doc);
        ++_totalSql;

        // Completely arbitrary, just batch up more than one
        // document for throughput!
        if (count == 1000) {
             // Commit within 5 minutes.
          UpdateResponse resp = _server.add(_docs, 300000);
          count = 0;
          if (resp.getStatus() != 0) {
            log("Some horrible error has occurred, status is: " +
                  resp.getStatus());
          }
          _docs.clear();
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (con != null) {
        con.close();
      }
    }
  }
}