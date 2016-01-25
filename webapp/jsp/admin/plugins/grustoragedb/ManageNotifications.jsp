<jsp:useBean id="managegrudataNotification" scope="session" class="fr.paris.lutece.plugins.grustoragedb.web.NotificationJspBean" />
<% String strContent = managegrudataNotification.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
