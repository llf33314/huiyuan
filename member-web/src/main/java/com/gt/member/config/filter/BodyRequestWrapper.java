package com.gt.member.config.filter;


import com.gt.member.util.HttpUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class BodyRequestWrapper extends HttpServletRequestWrapper
{
    private final byte[] body;

    public BodyRequestWrapper(HttpServletRequest request)
		    throws IOException
    {
	super(request);
	this.body = HttpUtils.getBodyString(request).getBytes(Charset.forName("UTF-8"));
    }

    public BufferedReader getReader() throws IOException
    {
	return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    public ServletInputStream getInputStream()
		    throws IOException
    {
	final ByteArrayInputStream bais = new ByteArrayInputStream(this.body);

	return new ServletInputStream()
	{
	    public boolean isFinished() {
		return false;
	    }

	    public boolean isReady()
	    {
		return false;
	    }

	    public void setReadListener(ReadListener readListener)
	    {
	    }

	    public int read()
			    throws IOException
	    {
		return bais.read();
	    }
	};
    }

    public String getHeader(String name)
    {
	return super.getHeader(name);
    }

    public Enumeration<String> getHeaderNames()
    {
	return super.getHeaderNames();
    }

    public Enumeration<String> getHeaders(String name)
    {
	return super.getHeaders(name);
    }
}