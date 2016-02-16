<%@page import="grid.Utils"%>
<%@ page import =" grid.entities.GridElement" %>
<%@ page import =" java.lang.reflect.Field" %>
<%@ page import =" java.util.List" %>
<%@ include file="head.jsp" %>

  <body>
	<div class="container">
    <%@ include file="navBar.jsp" %>
	
	<c:choose>
    <c:when test="${error==NULL}">
        <%
        	GridElement we=(GridElement)request.getAttribute("workingGE");
        	List <GridElement> pendinglist=(List<GridElement>)request.getAttribute("updatingElements");
        	
        		%>
        			
        		<h1>New versions available</h1><br>
       			<div style="width: 100%; float: left;">
       				<div style="width: 50%; float: left;">
       					<h2>Current version:</h2><br>
      						<% out.println(Utils.gridElementToHTMLString(we));%>
       				</div>
       				<%
       				for(GridElement ge: pendinglist){
       				%>
       				
	       				<div style="width: 50%; float: left;">
	       					<h2>Update to approve:</h2><br>
	       					<%out.println(Utils.gridElementToHTMLString(ge)); %>
	       					<div style="width: 100%; float: left; text-align: left; margin-top: 40px;">
								<input type="button" value="Accept" onclick="acceptPE()"/>
				   				<input type="button" value="Reject" onclick="rejectPE()"/>
				   			</div>
	       				</div>
       				<%
       				}
       				%>
       				
       				
       			</div>
            			
    </c:when>    
    <c:otherwise>
        ERRORE: ${error}
    </c:otherwise>
</c:choose>
    
    
	</div><!-- /.container -->
    
 </body>
	<%@ include file="footer.jsp" %>

   	
</html>
