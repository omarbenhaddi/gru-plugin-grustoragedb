<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="managegrudata" scope="session" class="fr.paris.lutece.plugins.grustoragedb.web.ManageGRUDataJspBean" />

<% managegrudata.init( request, managegrudata.RIGHT_MANAGEGRUDATA ); %>
<%= managegrudata.getManageGRUDataHome ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
