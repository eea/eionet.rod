<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>	

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Search">

	<stripes:layout-component name="contents">
	
        <h1>Results of search</h1>
        
        <stripes:form action="/simpleSearch" method="get">
		    <table width="530" style="border: 1px solid #006666">
				<tr>
					<td style="vertical-align:middle" width="30%">
						<label for="queryfld" style="font-weight:bold">Search ROD website:</label>
					</td>
					<td style="vertical-align:middle">
						<stripes:text name="expression" size="44" maxlength="255" id="queryfld"/>
						<stripes:submit name="execute" value="GO" class="go_btn"/>
					</td>
				</tr>
			</table>
		</stripes:form>
        <br/>
        <c:choose>
            <c:when test="${not empty actionBean.result && not empty actionBean.result.rows}">
                <display:table name="${actionBean.result.rows}" class="datatable" pagesize="30" sort="list" id="listItem" htmlId="listItem" requestURI="/simpleSearch" decorator="eionet.rod.web.util.SimpleSearchTableDecorator">
                    <display:column property="columnValue" title="Found" sortable="true" sortProperty="sortValue"/>
                </display:table>
            </c:when>
            <c:otherwise>
                <div class="system-msg">The query gave no results!</div>
            </c:otherwise>
        </c:choose>
        
        
	</stripes:layout-component>
</stripes:layout-render>
