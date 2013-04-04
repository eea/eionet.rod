<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Countries and territories">

  <stripes:layout-component name="contents">

        <h1>Countries and territories</h1>
        <p>
    This part of ROD helps administrations co-ordinate and manage their international
    reporting obligations. It provides information about when to
    report, who is responsible for reporting, and to which organisation the data
    set should be delivered. It is geared towards EEA member countries. You can
    browse deliveries by choosing a locality below or query the contents
    of ROD and CDR by using the advanced search.
    </p>
      <table cellspacing="0" style="width: 100%;">
      <colgroup span="3" width="33%"/>
      <tr valign="top">
        <th colspan="3" style="text-align:left; font-weight:bold">EEA member countries</th>
      </tr>
      <tr>
        <td style="border-left: #008080 1px solid; border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #C0C0C0 1px solid" valign="top">
          <c:forEach items="${actionBean.memberCountries}" begin="0" end="${actionBean.membersCount1 - 1}" var="country" varStatus="loop">
              <img src="images/Folder_icon.gif" alt=""/>
              <a href="spatial/${country.countryId}/deadlines">${rodfn:replaceTags(country.name)}</a>
              <c:if test="${!loop.last}">
                <br/>
              </c:if>
          </c:forEach>
        </td>
        <td style="border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #C0C0C0 1px solid" valign="top">
          <c:forEach items="${actionBean.memberCountries}" begin="${actionBean.membersCount1}" end="${actionBean.membersCount2 - 1}" var="country" varStatus="loop">
              <img src="images/Folder_icon.gif" alt=""/>
              <a href="spatial/${country.countryId}/deadlines">${rodfn:replaceTags(country.name)}</a>
              <c:if test="${!loop.last}">
                <br/>
              </c:if>
          </c:forEach>
        </td>
        <td style="border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #008080 1px solid" valign="top">
          <c:forEach items="${actionBean.memberCountries}" begin="${actionBean.membersCount2}" var="country" varStatus="loop">
              <img src="images/Folder_icon.gif" alt=""/>
              <a href="spatial/${country.countryId}/deadlines">${rodfn:replaceTags(country.name)}</a>
              <c:if test="${!loop.last}">
                <br/>
              </c:if>
          </c:forEach>
        </td>
      </tr>
      <tr valign="top">
        <th colspan="3" style="text-align:left; font-weight:bold">Other countries and territories</th>
      </tr>
      <tr>
        <td style="border-left: #008080 1px solid; border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #C0C0C0 1px solid" valign="top">
          <c:forEach items="${actionBean.nonMemberCountries}" begin="0" end="${actionBean.nonMembersCount1 - 1}" var="country" varStatus="loop">
              <img src="images/Folder_icon.gif" alt=""/>
              <a href="spatial/${country.countryId}/deadlines">${rodfn:replaceTags(country.name)}</a>
              <c:if test="${!loop.last}">
                <br/>
              </c:if>
          </c:forEach>
        </td>
        <td style="border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #C0C0C0 1px solid" valign="top">
          <c:forEach items="${actionBean.nonMemberCountries}" begin="${actionBean.nonMembersCount1}" end="${actionBean.nonMembersCount2 - 1}" var="country" varStatus="loop">
              <img src="images/Folder_icon.gif" alt=""/>
              <a href="spatial/${country.countryId}/deadlines">${rodfn:replaceTags(country.name)}</a>
              <c:if test="${!loop.last}">
                <br/>
              </c:if>
          </c:forEach>
        </td>
        <td style="border-top: #008080 1px solid; border-bottom: #008080 1px solid; border-right: #008080 1px solid" valign="top">
          <c:forEach items="${actionBean.nonMemberCountries}" begin="${actionBean.nonMembersCount2}" var="country" varStatus="loop">
              <img src="images/Folder_icon.gif" alt=""/>
              <a href="spatial/${country.countryId}/deadlines">${rodfn:replaceTags(country.name)}</a>
              <c:if test="${!loop.last}">
                <br/>
              </c:if>
          </c:forEach>
        </td>
      </tr>
    </table>

  </stripes:layout-component>
</stripes:layout-render>
