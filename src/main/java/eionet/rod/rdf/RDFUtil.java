/*
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
 * The Original Code is "ROD2"
 *
 * The Initial Developer of the Original Code is European.
 * Environment Agency (EEA).
 *
 * Copyright (C) 2000-2014 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Søren Roug
 */

package eionet.rod.rdf;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.HashMap;
import eionet.rod.StringEncoder;

/**
 * Write RDF/XML triples.
 */
public class RDFUtil {

    /** The namespaces to add to the rdf:RDF element. */
    private HashMap<String, String> namespaces;

    /** The output stream to send output to. */
    private Writer outputStream;

    /** If output has started, then you can't change the nullNamespace. */
    private Boolean rdfHeaderWritten = false;

    /** Base of XML file. */
    private String baseurl;

    /** The URL of the null namespace. */
    private String nullNamespace;

    /**
     * Constructor.
     *
     * @param stream - the stream to write the output to
     */
    public RDFUtil(final Writer stream) {
        outputStream = stream;
        namespaces = new HashMap<String, String>();
        namespaces.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    }

    /**
     * Set the vocabulary in case it needs to be different from the properties file.
     *
     * @param url
     *            - namespace url.
     */
    public void setVocabulary(final String url) {
        nullNamespace = url;
    }

    /**
     * Set the base URL.
     *
     * @param url
     *            - the base url.
     */
    public void setBaseURL(final String url) {
        baseurl = url;
    }

    /**
     * Add namespace to table.
     *
     * @param name
     *            - namespace token.
     * @param url
     *            - namespace url.
     */
    public void addNamespace(final String name, final String url) {
        namespaces.put(name, url);
    }

    /**
     * Generate the RDF header element. You can in principle get the encoding
     * from the output stream, but it returns it as a string that is not
     * understandable by XML parsers.
     *
     * @throws IOException
     *             - if the output is not open.
     */
    public void writeRdfHeader() throws IOException {
        if (rdfHeaderWritten) {
            throw new RuntimeException("Can't write header twice!");
        }
        output("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        output("<rdf:RDF");
        for (Object key : namespaces.keySet()) {
            String url = namespaces.get(key).toString();
            output(" xmlns:");
            output(key.toString());
            output("=\"");
            output(url);
            output("\"\n");
        }
        if (nullNamespace != null) {
            output(" xmlns=\"");
            output(nullNamespace);
            output("\"");
        }
        if (baseurl != null) {
            output(" xml:base=\"");
            output(baseurl);
            output("\"");
        }
        output(">\n\n");
        rdfHeaderWritten = true;
    }

    /**
     * Generate the RDF footer element.
     *
     * @throws IOException
     *             - if the output is not open.
     */
    public void writeRdfFooter() throws IOException {
        if (!rdfHeaderWritten) {
            writeRdfHeader();
        }
        output("</rdf:RDF>\n");
        flush();
    }

    /**
     * Write the start of an anonymous resource.
     * @param rdfClass
     *            - the class to assign
     * @throws IOException
     *             - if the output is not open.
     */
    public void writeStartResource(final String rdfClass) throws IOException {
        writeStartResource(rdfClass, null);
    }

    /**
     * Write the start of a resource - the line with rdf:about.
     * @param rdfClass
     *            - the class to assign
     * @param url
     *            - the identifier of the resource
     * @throws IOException
     *             - if the output is not open.
     */
    public void writeStartResource(final String rdfClass, final String url) throws IOException {
        output("<");
        output(rdfClass);
        if (url != null) {
            output(" rdf:about=\"");
            output(StringEncoder.encodeToXml(StringEncoder.encodeToIRI(url)));
            output("\"");
        }
        output(">\n");
    }

    /**
     * Write the end of a resource.
     * @param rdfClass
     *            - the class to assign
     * @throws IOException
     *             - if the output is not open.
     */
    public void writeEndResource(final String rdfClass) throws IOException {
        output("</");
        output(rdfClass);
        output(">\n");
    }


    /**
     * Write a reference to another resource.
     *
     * @param tag - the name of the predicate.
     * @param ref - the URL of the other object as a string.
     * @throws IOException
     *             - if the output is not open.
     */
    public void writeReference(final String tag, final String ref) throws IOException {
        output("<");
        output(tag);
        output(" rdf:resource=\"");
        output(StringEncoder.encodeToXml(StringEncoder.encodeToIRI(ref)));
        output("\"/>\n");
    }

    /**
     * Write a property where the type and language are provided.
     * Usage is for attribute tables where the type is provided as a field.
     * Type can be one of 'reference', '' or a XML schema simple type (without
     *  namespace). It it is 'reference' then the val is a URL.
     *
     * @param tag - the name of the predicate.
     * @param val - value to write.
     * @param langcode - language code.
     * @param type - type of literal - unless "reference"
     * @throws IOException
     *             - if the output is not open.
     */
    public void writeProperty(final String tag, final String val, final String langcode, final String type) throws IOException {
        if (null != type && type.equals("reference")) {
            writeReference(tag, val);
        } else {
            writeLiteral(tag, val, langcode, type);
        }
    }

    /**
     * Write a literal where the type and language are provided.
     * Usage is for attribute tables where the type is provided as a field.
     * The language code can be null, empty string or a code. If it is the empty
     * string then the user is indicating he wants the literal to have no
     * language code. Only untyped literals can have language codes.
     * Type can be one of '' or a XML schema simple type (without namespace).
     * Could check that if you provide the "boolean" type then the only
     * legal values are "true" or "false". Could do similar checks for numbers
     * as well.
     *
     * @param tag - the name of the predicate.
     * @param val - value to write.
     * @param langcode - language code.
     * @param type - type of literal
     * @throws IOException
     *             - if the output is not open.
     */
    public void writeLiteral(final String tag, final String val, final String langcode, final String type) throws IOException {
        if (val != null && val.length() > 0) {
            output("<");
            output(tag);
            if (null == type || type.equals("")) {
                // Only untyped literals can have a language.
                if (null != langcode && !langcode.equals("")) {
                    output(" xml:lang=\"");
                    output(langcode);
                    output("\"");
                }
            } else {
                output(" rdf:datatype=\"http://www.w3.org/2001/XMLSchema#");
                output(type);
                output("\"");
            }
            output(">");
            output(StringEncoder.encodeToXml(val));
            output("</");
            output(tag);
            output(">\n");
        }
    }

    /**
     * Write a string literal where the language isn't provided.
     *
     * @param tag - the name of the predicate.
     * @param val - value to write.
     * @throws IOException
     *             - if the output is not open.
     */
    public void writeLiteral(final String tag, final String val) throws IOException {
        writeLiteral(tag, val, null);
    }

    /**
     * Write a string literal where the language is provided.
     * The language code can be null, empty string or a code. If it is the empty
     * string then the user is indicating he wants the literal to have no
     * language code. Only untyped literals can have language codes.
     *
     * @param tag - the name of the predicate.
     * @param val - value to write.
     * @param langcode - language code
     * @throws IOException
     *             - if the output is not open.
     */
    public void writeLiteral(final String tag, final String val, final String langcode) throws IOException {
        if (val != null && val.length() > 0) {
            output("<");
            output(tag);
            if (null != langcode && !langcode.equals("")) {
                output(" xml:lang=\"");
                output(langcode);
                output("\"");
            }
            output(">");
            output(StringEncoder.encodeToXml(val));
            output("</");
            output(tag);
            output(">\n");
        }
    }

    /**
     * Write a literal of type Boolean.
     *
     * @param tag - the name of the predicate.
     * @param val - value to write.
     * @throws IOException
     *             - if the output is not open.
     */
    public void writeLiteral(final String tag, final Boolean val) throws IOException {
        if (val != null) {
            output("<");
            output(tag);
            output(" rdf:datatype=\"http://www.w3.org/2001/XMLSchema#boolean\">");
            output(val ? "true" : "false");
            output("</");
            output(tag);
            output(">\n");
        }
    }

    /**
     * Write a literal of type Integer.
     *
     * @param tag - the name of the predicate.
     * @param val - value to write.
     * @throws IOException
     *             - if the output is not open.
     */
    public void writeLiteral(final String tag, final Integer val) throws IOException {
        if (val != null) {
            output("<");
            output(tag);
            output(" rdf:datatype=\"http://www.w3.org/2001/XMLSchema#integer\">");
            output(val.toString());
            output("</");
            output(tag);
            output(">\n");
        }
    }

    /**
     * Write a literal of type Decimal.
     *
     * @param tag - the name of the predicate.
     * @param val - value to write.
     * @throws IOException
     *             - if the output is not open.
     */
    public void writeLiteral(final String tag, final BigDecimal val) throws IOException {
        if (val != null) {
            output("<");
            output(tag);
            output(" rdf:datatype=\"http://www.w3.org/2001/XMLSchema#decimal\">");
            output(val.toString());
            output("</");
            output(tag);
            output(">\n");
        }
    }

    /**
     * Write the start element of a literal.
     *
     * @param tag - the name of the predicate.
     * @throws IOException
     *             - if the output is not open.
     */
    public void writeStartLiteral(final String tag) throws IOException {
        output("<");
        output(tag);
        output(">");
    }

    /**
     * Write the end element of a literal.
     *
     * @param tag - the name of the predicate.
     * @throws IOException
     *             - if the output is not open.
     */
    public void writeEndLiteral(final String tag) throws IOException {
        output("</");
        output(tag);
        output(">");
    }

    /**
     * Called from the other methods to do the output.
     *
     * @param v
     *            - value to print.
     * @throws IOException
     *             - if the output is not open.
     */
    protected void output(String v) throws IOException {
        outputStream.write(v);
    }

    /**
     * Called from the other methods to flush the output.
     *
     * @throws IOException
     *             - if the output is not open.
     */
    protected void flush() throws IOException {
        outputStream.flush();
    }

}
