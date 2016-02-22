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
        		%>
        			<script type="text/javascript">
				   	function acceptPE() {
				   		jQuery.ajax({
					   		type: "POST",
					  		url: "/ISSSR/acceptPendingUpdate",
						   	contentType: "application/json; charset=utf-8",
						    dataType: "json",
						   	data: "{\"label\":\"<%out.print(ge.getLabel());%>\",\"type\":\"<%out.print(ge.getClass().getSimpleName());%>\",  }",
						  	success: function (msg) { 
						  		alert(msg) 
					  		},
							error: function (err){
								alert(err.responseText)
							}
					    });
					};
					</script>
				   	<script type="text/javascript">
				   	function rejectPE() {
				   		var data= "{\"label\":\"test\" }";
						jQuery.ajax({
					   		type: "POST",
					  		url: "/ISSSR/rejectPendingUpdate",
						   	contentType: "application/json; charset=utf-8",
						    dataType: "json",
						   	data: "{\"label\":\"<%out.print(ge.getLabel());%>\",\"type\":\"<%out.print(ge.getClass().getSimpleName());%>\",  }",
						  	success: function (msg) { 
						  		alert(msg) 
					  		},
							error: function (err){
								alert(err.responseText)
							}
					    });
					}
				</script>
        		<%
        		GridElement we=(GridElement)request.getAttribute("workingElement");
            	if (we==null) {
            		%>	<h1>Added Grid Element to approve</h1><br>
        				<div style="width: 100%; float: left;">
        					<h2>Update to approve:</h2><br>
        					<%out.println(Utils.gridElementToHTMLString(ge)); %>
        					<div style="width: 100%; float: left; text-align: left; margin-top: 40px;">
								<input type="button" value="Accept" onclick="acceptPE()"/>
			    				<input type="button" value="Reject" onclick="rejectPE()"/>
			    			</div>
        				</div>
        			
        			
        			
        		<%
            		
            	}
            	else {
            		%>	<h1>New version available</h1><br>
            			<div style="width: 100%; float: left;">
            				<div style="width: 50%; float: left;">
            					<h2>Current version:</h2><br>
           						<% out.println(Utils.gridElementToHTMLString(we));%>
            				</div>
            				<div style="width: 50%; float: left;">
            					<h2>Update to approve:</h2><br>
            					<%out.println(Utils.gridElementToHTMLString(ge)); %>
            					<div style="width: 100%; float: left; text-align: left; margin-top: 40px;">
									<input type="button" value="Accept" onclick="acceptPE()"/>
				    				<input type="button" value="Reject" onclick="rejectPE()"/>
				    			</div>
            				</div>
            			</div>
            			
            			
            		<%
            		
            	}
            	
           	} %>
    	</c:when>    
    <c:otherwise>
        ERRORE: ${error}
    </c:otherwise>
</c:choose>
    
    
	</div><!-- /.container -->
    
 </body>
	<%@ include file="footer.jsp" %>

   	
</html>
