<jsp:useBean id="managedemandtypeDemandCategory" scope="session" class="fr.paris.lutece.plugins.grustoragedb.web.DemandCategoryJspBean" />
<% String strContent = managedemandtypeDemandCategory.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
