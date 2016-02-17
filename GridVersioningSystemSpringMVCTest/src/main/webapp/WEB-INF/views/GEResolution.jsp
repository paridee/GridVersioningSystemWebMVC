<%@page import="grid.Utils"%>
<%@ page import =" grid.entities.GridElement" %>
<%@ page import =" java.lang.reflect.Field" %>
<%@ page import =" java.util.List" %>
<%@ page import =" com.google.gson.Gson" %>
<%@ include file="head.jsp" %>

  <body>
	<div class="container">
    <%@ include file="navBar.jsp" %>
	
	<c:choose>
    <c:when test="${error==NULL}">
    	<script type="text/javascript">
		   	function acceptGE(json) {
		   		var newjson=json.replace(/#/g,'"');
		   		jQuery.ajax({
			   		type: "POST",
			  		url: "/ISSSR/solveUpdate",
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
			};
		</script>
        <%
        	
        	GridElement we=(GridElement)request.getAttribute("workingGE");
        	Gson gson = new Gson();
			
			
        	
        	List <GridElement> pendinglist=(List<GridElement>)request.getAttribute("updatingElements");
        	
        		%>
        			
        		<h1>New versions available</h1><br>
       			<div style="width: 100%; float: left;">
       				<div style="width: 50%; float: left;">
       					<h2>Current version:</h2><br>
      						<% out.println(Utils.gridElementToHTMLString(we));%><br>
      						<input type="button" value="Accept" onclick="acceptGE('<%String s=gson.toJson(we); s=s.replaceAll("\"", "#"); out.print(s);%>')"/>
       				</div>
       				<%
       				for(GridElement ge: pendinglist){
       				%>
       				
	       				<div style="width: 50%; float: left;">
	       					<h2>Update to approve:</h2><br>
	       					<%out.println(Utils.gridElementToHTMLString(ge)); %>
	       					<div style="width: 100%; float: left; text-align: left; margin-top: 40px;">
								<input type="button" value="Accept" onclick="acceptGE('<%s=gson.toJson(ge); s=s.replaceAll("\"", "#"); out.print(s);%>')"/>
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
