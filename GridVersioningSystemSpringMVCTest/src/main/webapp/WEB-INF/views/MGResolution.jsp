<%@ page import="grid.Utils"%>
<%@ page import =" grid.entities.GridElement" %>
<%@ page import =" java.lang.reflect.Field" %>
<%@ page import =" java.util.List" %>
<%@ page import =" java.util.HashMap" %>
<%@ page import =" com.google.gson.Gson" %>
<%@ include file="head.jsp" %>


  <body>
	<div class="container">
    <%@ include file="navBar.jsp" %>
	
	<c:choose>
    <c:when test="${error==NULL}">
    	<script type="text/javascript">
    		function acceptList(list){
    			var newjson=JSON.stringify(list);
    			alert(newjson);
		   		jQuery.ajax({
			   		type: "POST",
			  		url: "/ISSSR/MGListUpdate",
				   	contentType: "application/json; charset=utf-8",
				    dataType: "json",
				   	data: newjson,
				  	success: function (msg) { 
				  		alert(JSON.stringify(msg));
			  		},
					error: function (err){
						alert(err.responseText);
					}
			    });
    		}
    	</script>
    	Working grid goal List:<input type="button" value="Accept" onclick="acceptList(${workingMGList})"/><br>
    	<c:forEach items="${workingGrid.mainGoals}" var="maingoal">${maingoal.label}-</c:forEach>
    	<br>
        <br>New list to approve:<input type="button" value="Accept" onclick="acceptList(${currentMGList})"/><br>
        <c:forEach items="${currentGrid.mainGoals}" var="maingoal">${maingoal.label}-</c:forEach>
    </c:when>    
    <c:otherwise>
        ERRORE: ${error}
    </c:otherwise>
</c:choose>
    
    
	</div><!-- /.container -->
    
 </body>
	<%@ include file="footer.jsp" %>

   	
</html>
