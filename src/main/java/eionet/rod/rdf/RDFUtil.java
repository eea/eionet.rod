package eionet.rod.rdf;

import java.math.BigDecimal;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Write RDF/XML triples.
 */
public class RDFUtil {

    /**
     * Write a reference to another resource.
     *
     * @param tag - the name of the predicate.
     * @param ref - the URL of the other object as a string.
     * @return String
     */
    public static String writeReference(final String tag, final String ref) {
        StringBuffer rdf = new StringBuffer();
        if (ref != null && ref.length() > 0) {
            rdf.append("    <").append(tag).append(" rdf:resource=\"").append(StringEscapeUtils.escapeXml(ref)).append("\"/>\n");
        }
        return rdf.toString();
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
     * @return String
     */
    public static String writeProperty(final String tag, final String val, final String langcode, final String type) {
        if (null != type && type.equals("reference")) {
            return writeReference(tag, val);
        } else {
            return writeLiteral(tag, val, langcode, type);
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
     * @return String
     */
    public static String writeLiteral(final String tag, final String val, final String langcode, final String type) {
        StringBuffer rdf = new StringBuffer();
        if (val != null && val.length() > 0) {
            rdf.append("    <").append(tag);
            if (null == type || type.equals("")) {
                // Only untyped literals can have a language.
                if (null != langcode && !langcode.equals("")) {
                    rdf.append(" xml:lang=\"").append(langcode).append("\"");
                }
            } else {
                rdf.append(" rdf:datatype=\"http://www.w3.org/2001/XMLSchema#").append(type).append("\"");
            }
            rdf.append(">").append(StringEscapeUtils.escapeXml(val))
            .append("</").append(tag).append(">\n");
        }
        return rdf.toString();
    }

    /**
     * Write a string literal where the language isn't provided.
     *
     * @param tag - the name of the predicate.
     * @param val - value to write.
     * @return String
     */
    public static String writeLiteral(final String tag, final String val) {
        return writeLiteral(tag, val, null);
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
     * @return String
     */
    public static String writeLiteral(final String tag, final String val, final String langcode) {
        StringBuffer rdf = new StringBuffer();
        if (val != null && val.length() > 0) {
            rdf.append("    <").append(tag);
            if (null != langcode && !langcode.equals("")) {
                rdf.append(" xml:lang=\"").append(langcode).append("\"");
            }
            rdf.append(">").append(StringEscapeUtils.escapeXml(val))
            .append("</").append(tag).append(">\n");
        }
        return rdf.toString();
    }

    /**
     * Write a literal of type Boolean.
     *
     * @param tag - the name of the predicate.
     * @param val - value to write.
     * @return String
     */
    public static String writeLiteral(final String tag, final Boolean val) {
        StringBuffer rdf = new StringBuffer();
        if (val != null) {
            rdf.append("    <").append(tag).append(" rdf:datatype=\"http://www.w3.org/2001/XMLSchema#boolean\">")
            .append(val ? "true" : "false").append("</").append(tag).append(">\n");
        }
        return rdf.toString();
    }

    /**
     * Write a literal of type Integer.
     *
     * @param tag - the name of the predicate.
     * @param val - value to write.
     * @return String
     */
    public static String writeLiteral(final String tag, final Integer val) {
        StringBuffer rdf = new StringBuffer();
        if (val != null) {
            rdf.append("    <").append(tag).append(" rdf:datatype=\"http://www.w3.org/2001/XMLSchema#integer\">")
            .append(val).append("</").append(tag).append(">\n");
        }
        return rdf.toString();
    }

    /**
     * Write a literal of type Decimal.
     *
     * @param tag - the name of the predicate.
     * @param val - value to write.
     * @return String
     */
    public static String writeLiteral(final String tag, final BigDecimal val) {
        StringBuffer rdf = new StringBuffer();
        if (val != null) {
            rdf.append("    <").append(tag).append(" rdf:datatype=\"http://www.w3.org/2001/XMLSchema#decimal\">")
            .append(val).append("</").append(tag).append(">\n");
        }
        return rdf.toString();
    }
}
