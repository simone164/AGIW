package it.cache.downloader.warc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.jwat.warc.WarcReader;
import org.jwat.warc.WarcReaderFactory;
import org.jwat.warc.WarcRecord;

public class WarcExtractor {
	
	/*
	 * ATTENZIONE: MODIFICARE IL PATH DOVE VENGONO RECUPERATI I FILE
	 */
	static String warcFile = "C:/Users/Think/Documents/Progetti/Dati_Agiw/00.warc";
    //static String warcFile = "/home/nicl/Downloads/MYWARC.warc";
 
    public static void main(String[] args) {
        File file = new File( warcFile );
        try {
        	InputStream in = new FileInputStream( file );

        	int records = 0;
            int errors = 0;
 
            WarcReader reader = WarcReaderFactory.getReader( in );
            WarcRecord record;
           
            while ( (record = reader.getNextRecord()) != null ) {
                
                if(record.warcTargetUriUri != null && record.warcTargetUriStr != null){
                	System.out.println(record.warcRecordIdUri.toString());
                	System.out.println(record.warcTargetUriUri.toString());
                	System.out.println(record.getPayload().toString());
                	System.out.println(record.getPayloadContent().toString());
                	System.out.println(record.getPayload().getInputStreamComplete().toString());
                	if(record.warcTypeIdx == 2){
	                	String nomeFIle = record.warcRecordIdUri.toString().substring(9);
	                	/*
	                	 * ATTENZIONE: MODIFICARE IL PATH DI SALVATAGGIO
	                	 */
	                    File filehtml = new File("C:/Users/Think/Documents/Progetti/Dati_Agiw/Estratto/" + nomeFIle + ".html");
	                    filehtml.createNewFile();
	                    InputStream is = record.getPayload().getInputStream();
	                    FileUtils.copyInputStreamToFile(is, filehtml);
	                    System.out.println(record.warcTypeIdx.toString());
	                    System.out.println("creato#######");
                	}
                }

                ++records;
 
                if (record.hasErrors()) {
                    errors += record.getValidationErrors().size();
                }
            }
            
            System.out.println("--------------");
            System.out.println("       Records: " + records);
            System.out.println("        Errors: " + errors);
            reader.close();
            in.close();
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}