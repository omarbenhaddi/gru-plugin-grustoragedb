<jsp:useBean id="managegrudataDemand" scope="session" class="fr.paris.lutece.plugins.grustoragedb.web.DemandJspBean" />
<% String strContent = managegrudataDemand.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
