<%@page contentType="text/html;charset=UTF-8"%>

<%@ include file="/pages/common/taglibs.jsp"%>

<h1>Parameters for ${rodfn:replaceTags(actionBean.obligation.title)}</h1>

<c:if test="${!empty actionBean.obligation.parameters}">
    <table class="datatable">
        <tr>
            <th scope="col" class="scope-col" style="text-align: left">Parameters</th>
        </tr>
        <tr>
            <td>
                ${rodfn:replaceTags(actionBean.obligation.parameters)}
            </td>
        </tr>
    </table>
</c:if>