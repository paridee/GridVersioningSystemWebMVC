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
        	GridElement ge=(GridElement)request.getAttribute("majUpdateToApprove");
        	if (ge==null) out.println("error: GridElement not available");
        	else{
        		GridElement we=(GridElement)request.getAttribute("workingElement");
            	if (we==null) out.println("Added element to approve.<br>");
            	else out.println("New version available <br>");
				
				out.println(Utils.gridElementToHTMLString(ge));
	        	
        	}
        %>
        
        
    </c:when>    
    <c:otherwise>
        ERRORE: ${error}
    </c:otherwise>
</c:choose>
    
    
	</div><!-- /.container -->
    
 </body>
	<%@ include file="footer.jsp" %>

   	
</html>
