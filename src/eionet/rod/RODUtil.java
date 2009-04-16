/**
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is "EINRC-5 / WebROD Project".
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (C) 2000-2002 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Kaido Laine (TietoEnator)
 */

package eionet.rod;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

public class RODUtil {
    
   /**
   *  Dummy method for getting request parameter
   */
   public static String getParameter(HttpServletRequest req, String prmName) {

      
      String p =  req.getParameter(prmName);
      //strongly ugly and bad quick-fix
      if ( prmName.equals("printmode") &&  ( p ==null || p.trim().equals("") ) )
        p="N";
      return p;
      
   }
   
   /*
    * 
    */
	public static String threeDots(String s, int len){
		
		if (len<=0) return s;
		if (s==null || s.length()==0) return s;
		
		if (s.length()>len){
			StringBuffer buf = new StringBuffer(s.substring(0,len));
			buf.append("...");
			return buf.toString();
		}
		else
			return s;
	}
	
	/*
    * 
    */
	public static String concatRole(String param1, String param2, String param3){
		
		return param1 + param2 + param3;
	}
	
	/*
	 * 
	 */
	public static Date getDate(String s){
		if (s == null || s.equals("")) return null;
		Date date = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			date = sdf.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}
    
    public static String replaceTags2(String string) {
        if(string != null){
            StringBuffer sb = new StringBuffer(string.length());
            // true if last char was blank
            boolean lastWasBlankChar = false;
            int len = string.length();
            char c;
    
            for (int i = 0; i < len; i++)
                {
                c = string.charAt(i);
                if (c == ' ') {
                    // blank gets extra work,
                    // this solves the problem you get if you replace all
                    // blanks with &nbsp;, if you do that you loss 
                    // word breaking
                    if (lastWasBlankChar) {
                        lastWasBlankChar = false;
                        sb.append("&nbsp;");
                        }
                    else {
                        lastWasBlankChar = true;
                        sb.append(' ');
                        }
                    }
                else {
                    lastWasBlankChar = false;
                    //
                    // HTML Special Chars
                    if (c == '"')
                        sb.append("&quot;");
                    else if (c == '&')
                        sb.append("&amp;");
                    else if (c == '<')
                        sb.append("&lt;");
                    else if (c == '>')
                        sb.append("&gt;");
                    else if (c == '\n')
                        // Handle Newline
                        sb.append("<br/>");
                    else {
                        int ci = 0xffff & c;
                        if (ci < 160 )
                            // nothing special only 7 Bit
                            sb.append(c);
                        else {
                            // Not 7 Bit use the unicode system
                            sb.append("&#");
                            sb.append(new Integer(ci).toString());
                            sb.append(';');
                            }
                        }
                    }
                }
            return sb.toString();
        }
        return null;
    }
    
    /**
     * 
     * @param in
     * @return
     */
    public static String replaceTags(String in){
        return replaceTags(in, false, false);
    }
    
    /**
     * 
     * @param in
     * @param dontCreateHTMLAnchors
     * @return
     */
    public static String replaceTags(String in, boolean dontCreateHTMLAnchors){
        return replaceTags(in, dontCreateHTMLAnchors, false);
    }
    
    /**
     * 
     * @param in
     * @param inTextarea
     * @return
     */
    public static String replaceTags(
            String in, boolean dontCreateHTMLAnchors, boolean dontCreateHTMLLineBreaks){
        
        in = (in != null ? in : "");
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < in.length(); i++) {
          char c = in.charAt(i);
          if (c == '<')
            ret.append("&lt;");
          else if (c == '>')
            ret.append("&gt;");
          else if (c == '"')
              ret.append("&quot;");
          else if (c == '\'')
              ret.append("&#039;");
          else if (c == '\\')
              ret.append("&#092;");
          else if (c == '&'){
              boolean startsEscapeSequence = false;
              int j = in.indexOf(';', i);
              if (j>0){
                  String s = in.substring(i,j+1);
                  UnicodeEscapes unicodeEscapes = new UnicodeEscapes();
                  if (unicodeEscapes.isXHTMLEntity(s) || unicodeEscapes.isNumericHTMLEscapeCode(s))
                      startsEscapeSequence = true;
              }
              
              if (startsEscapeSequence)
                  ret.append(c);
              else
                  ret.append("&amp;");
          }
          else if (c == '\n' && dontCreateHTMLLineBreaks==false)
            ret.append("<br/>");
          else if (c == '\r' && in.charAt(i+1)=='\n' && dontCreateHTMLLineBreaks==false){
            ret.append("<br/>");
            i = i + 1;
          }
          else
            ret.append(c);
        }
        
        String retString = ret.toString();
        if (dontCreateHTMLAnchors==false)
            retString=setAnchors(retString, false, 50);

        return retString;
    }
    
    /**
    * Finds all urls in a given string and replaces them with HTML anchors.
    * If boolean newWindow==true then target will be a new window, else no.
    * If boolean cutLink>0 then cut the displayed link lenght cutLink.
    */
    public static String setAnchors(String s, boolean newWindow, int cutLink){

        StringBuffer buf = new StringBuffer();
        
        StringTokenizer st = new StringTokenizer(s, " \t\n\r\f", true);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (!isURL(token))
                buf.append(token);
            else{
                StringBuffer _buf = new StringBuffer("<a ");
                if (newWindow) _buf.append("target=\"_blank\" ");
                _buf.append("href=\"");
                _buf.append(token);
                _buf.append("\">");
                
                if (cutLink<token.length())
                    _buf.append(token.substring(0, cutLink)).append("...");
                else
                    _buf.append(token);
                    
                _buf.append("</a>");
                buf.append(_buf.toString());
            }
        }
        
        return buf.toString();
    }
      
    /**
    * Finds all urls in a given string and replaces them with HTML anchors.
    * If boolean newWindow==true then target will be a new window, else no.
    */
    public static String setAnchors(String s, boolean newWindow){
        
        return setAnchors(s, newWindow, 0);
    }
  
    /**
    * Finds all urls in a given string and replaces them with HTML anchors
    * with target being a new window.
    */
    public static String setAnchors(String s){
        
        return setAnchors(s, true);
    }
    
    /**
    * Checks if the given string is a well-formed URL
    */
    public static boolean isURL(String s){
        try {
            URL url = new URL(s);
        }
        catch (MalformedURLException e){
            return false;
        }
        
        return true;
    }
    
    /**
     * 
     * @param s
     * @return
     */
    public static boolean isNullOrEmpty(String s){
    	return s==null || s.length()==0;
    }
    
    /**
     * 
     * @param s
     * @return
     */
    public static boolean isNumber(String s){
    	boolean ret = true;
    	try {    
    		new Integer(s).intValue(); 
    	} catch (NumberFormatException e) { 
    		ret = false;
    	}
    	return ret;
    }
    
    public static String str2Date(String date) {
    	if(RODUtil.isNullOrEmpty(date))
           return "NULL";
    	
    	int len = date.length();
           
        //formats the input string in the form dd/mm/yyyy to MySQL date format yyyy-mm-dd
        //
        //                 0123456789
        // WebROD format: dd/mm/yyyy
        // MySQL format:   yyyy-mm-dd
        //
        
        if (len == 10) {
           char d1 = date.charAt(0);
           char d2 = date.charAt(1);
           char m1 = date.charAt(3);
           char m2 = date.charAt(4);
           char y1 = date.charAt(6);
           char y2 = date.charAt(7);
           char y3 = date.charAt(8);
           char y4 = date.charAt(9);
           char s1 = date.charAt(2);
           char s2 = date.charAt(5);
           
           if (Character.isDigit(d1) &&
               Character.isDigit(d2) &&
               Character.isDigit(m1) &&
               Character.isDigit(m2) &&
               Character.isDigit(y1) &&
               Character.isDigit(y2) &&
               Character.isDigit(y3) &&
               Character.isDigit(y4) &&
               s1 == '/' && s2 == '/') {
              StringBuffer ret = new StringBuffer(10);
              ret.insert(0, y1)
                 .insert(1, y2)
                 .insert(2, y3)
                 .insert(3, y4)
                 .insert(4, '-')
                 .insert(5, m1)
                 .insert(6, m2)
                 .insert(7, '-')
                 .insert(8, d1)
                 .insert(9, d2);
                 
              return ret.toString();      
           }
        }

        return "";
     }

}
