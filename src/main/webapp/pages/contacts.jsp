<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>

<stripes:layout-render name="/pages/common/template.jsp" pageTitle="Contacts">

    <stripes:layout-component name="contents">

        <%-- Section displayed when the role was successfully found from Eionet's directory service. --%>

        <c:if test="${empty actionBean.dirRole}">
           <div class="system-msg">
               <c:if test="${actionBean.directoryError}">
                   <p>Technical error when getting the requested role's description from Eionet's directory service!</p>
               </c:if>
               <c:if test="${!actionBean.directoryError}">
                   <p>Found no description for the requested role in Eionet's directory service!</p>
               </c:if>
           </div>
        </c:if>

        <%-- Section displayed when the role was NOT successfully found from Eionet's directory service. --%>

        <c:if test="${not empty actionBean.dirRole}">

            <h1>
                <c:out value="${not empty actionBean.dirRole.description ? actionBean.dirRole.description : actionBean.roleId}"/>
            </h1>

            <table class="datatable" style="width:80%">
                <col style="width:20%"/>
                <col style="width:80%"/>
                <tr>
                    <th scope="row" class="scope-row">Role name</th>
                    <td>
                        <c:out value="${actionBean.dirRole.name}"/>
                    </td>
                </tr>
                <tr>
                    <th scope="row" class="scope-row">Parent role</th>
                    <c:if test="${empty actionBean.parentRoleId}">
                        <td>N/A</td>
                    </c:if>
                    <c:if test="${not empty actionBean.parentRoleId}">
                        <td>
                            <stripes:link beanclass="${actionBean['class'].name}">
                                <stripes:param name="roleId" value="${actionBean.parentRoleId}"/>
                                <c:out value="${actionBean.parentRoleId}"/>
                            </stripes:link>
                        </td>
                    </c:if>
                </tr>
                <c:if test="${not empty actionBean.dirRole.membersUrl}">
                    <tr>
                        <th scope="row" class="scope-row">Eionet site directory</th>
                        <td>
                            <a href="${rodfn:replaceTags2(actionBean.dirRole.membersUrl,true,true)}">
                                Additional details for authenticated users
                            </a>
                        </td>
                    </tr>
                </c:if>
            </table>

            <c:if test="${empty actionBean.dirRole.subroles && empty actionBean.dirRole.members}">
               <div class="tip-msg">No sub-roles and no members found!</div>
            </c:if>

            <c:if test="${not empty actionBean.dirRole.subroles}">
                <h4>Sub-roles:</h4>
                <table class="datatable" style="width:50em; max-width:100%">
                    <c:forEach items="${actionBean.dirRole.subroles}" var="subrole" varStatus="loop">
                        <tr class="${loop.index % 2 == 0 ? 'zebraodd' : 'zebraeven'}">
                            <td>
                                <a href="contacts?roleId=${subrole.id}">${rodfn:replaceTags(subrole.id)} (${rodfn:replaceTags(subrole.description)})</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>

            <c:if test="${not empty actionBean.dirRole.members}">
                <h4>Members:</h4>
                <table class="datatable" style="width:50em; max-width:100%">
                    <c:forEach items="${actionBean.dirRole.members}" var="member" varStatus="loop">
                        <tr class="${loop.index % 2 == 0 ? 'zebraodd' : 'zebraeven'}">
                            <td>
                                <c:if test="${!empty member.fullName}">
                                    <b>${rodfn:replaceTags(member.fullName)}</b>
                                </c:if>
                                <c:if test="${!empty member.mail && actionBean.isUserLoggedIn}">
                                    <a href="mailto:${member.mail}">${member.mail}</a>
                                </c:if>
                                <c:if test="${!empty member.description && member.description != ' '}">
                                    <br/>${rodfn:replaceTags(member.description)}
                                </c:if>
                                <c:if test="${!empty member.phone}">
                                    <br/>Tel: ${member.phone}
                                </c:if>
                                <c:if test="${!empty member.fax}">
                                    Fax: ${member.fax}
                                </c:if>
                                <c:if test="${!empty member.organisation.name && !empty member.organisation.url}">
                                    <br/><a href="${rodfn:replaceTags2(member.organisation.url,true,true)}">${rodfn:replaceTags(member.organisation.name)}</a>
                                </c:if>
                                <c:if test="${!empty member.organisation.name && empty member.organisation.url}">
                                    <br/>${rodfn:replaceTags(member.organisation.name)}
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>

        </c:if>

    </stripes:layout-component>
</stripes:layout-render>
