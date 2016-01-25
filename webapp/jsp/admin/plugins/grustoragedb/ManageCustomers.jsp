<jsp:useBean id="managegrudataCustomer" scope="session" class="fr.paris.lutece.plugins.grustoragedb.web.CustomerJspBean" />
<% String strContent = managegrudataCustomer.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
