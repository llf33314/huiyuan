package com.gt.member.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.ServletRequest;

public class HttpUtils {

    public static String sendPostByHeaders( String url, Map< String,String > headers, String param ) {
	PrintWriter out = null;
	BufferedReader in = null;
	String result = "";
	try {
	    URL realUrl = new URL( url );

	    URLConnection conn = realUrl.openConnection();

	    conn.setRequestProperty( "accept", "*/*" );
	    conn.setRequestProperty( "connection", "Keep-Alive" );
	    conn.setRequestProperty( "user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)" );

	    conn.setRequestProperty( "Content-type", "application/json;charset=UTF-8" );

	    if ( ( headers != null ) && ( !headers.isEmpty() ) ) {
		for ( String key : headers.keySet() ) {
		    conn.setRequestProperty( key, (String) headers.get( key ) );
		}

	    }

	    conn.setDoOutput( true );
	    conn.setDoInput( true );
	    conn.setUseCaches( false );

	    out = new PrintWriter( conn.getOutputStream() );

	    out.print( param );

	    out.flush();

	    InputStreamReader isr = new InputStreamReader( conn.getInputStream() );
	    in = new BufferedReader( isr );
	    String line;
	    while ( ( line = in.readLine() ) != null ) result = new StringBuilder().append( result ).append( new String( line.getBytes() ) ).toString();
	} catch ( Exception e ) {
	    e.printStackTrace();
	} finally {
	    try {
		if ( out != null ) {
		    out.close();
		}
		if ( in != null ) in.close();
	    } catch ( IOException ex ) {
		ex.printStackTrace();
	    }
	}
	return result;
    }

    public static String getBodyString( ServletRequest request ) {
	StringBuilder sb = new StringBuilder();
	InputStream inputStream = null;
	BufferedReader reader = null;
	try {
	    inputStream = request.getInputStream();
	    reader = new BufferedReader( new InputStreamReader( inputStream, Charset.forName( "UTF-8" ) ) );
	    String line = "";
	    while ( ( line = reader.readLine() ) != null ) sb.append( line );
	} catch ( IOException e ) {
	    e.printStackTrace();
	} finally {
	    if ( inputStream != null ) {
		try {
		    inputStream.close();
		} catch ( IOException e ) {
		    e.printStackTrace();
		}
	    }
	    if ( reader != null ) {
		try {
		    reader.close();
		} catch ( IOException e ) {
		    e.printStackTrace();
		}
	    }
	}
	return sb.toString();
    }
}