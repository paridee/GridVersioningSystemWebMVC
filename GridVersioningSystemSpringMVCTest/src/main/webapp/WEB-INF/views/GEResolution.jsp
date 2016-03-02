<%@ page import="grid.Utils"%>
<%@ page import =" grid.entities.GridElement" %>
<%@ page import =" grid.interfaces.services.*" %>
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
    	
    	
		   	function acceptGE(json) {
		   		var newjson=json.replace(/#/g,'"');
		   		jQuery.ajax({
			   		type: "POST",
			  		url: "/ISSSR/solveUpdate",
				   	contentType: "application/json; charset=utf-8",
				    dataType: "json",
				   	data: newjson,
				  	success: function (response) { 
				  		Lobibox.alert(response.type, //AVAILABLE TYPES: "error", "info", "success", "warning"
			    			{
			    			    msg: response.msg,
			    			    closeButton     : false,
			    			    callback: function ($this, type, ev) {
			    			    	 window.location.href = "/ISSSR/resolutionDashBoard";
			    			    }
			    			   
			    			   
			    			});
				  		
				  	},
					error: function (err){
						alert(err.responseText);
						Lobibox.alert("error", //AVAILABLE TYPES: "error", "info", "success", "warning"
				    			{
				    			    msg: err.responseText,
				    			    hidden: window.location.href = "/ISSSR/resolutionDashBoard",
				    			});
					}
			    });
			};
		</script>
		
        <%
        	
        	GridElement we=(GridElement)request.getAttribute("workingGE");
        	Gson gson = new Gson();
			
			
        	
        	List <GridElement> pendinglist=(List<GridElement>)request.getAttribute("updatingElements");
        	
        		%>
       				<div class="panel panel-default">
						<div class="panel-heading"><span style="font-size: 22px;">New versions available</span></div>
					  	<div class="panel-body">
							<div style="width: 50%; float: left;">
								<div class="panel panel-primary">
  									<div class="panel-heading"><b>Current Version</b></div>
									<div class="panel-body">
										<div class="panel panel-default">
											<% 
												GridElementService ges=(GridElementService)request.getAttribute("GEService");
												String geString=Utils.gridElementToHTMLString(we,ges,true);
												out.println(geString);%>
											<div class="panel-footer">
												<input type="button" value="Accept" onclick="acceptGE('<%
				      								String s=gson.toJson(we); 
				      								s=s.replaceAll("\"", "#"); 
				      								String type=we.getClass().toString();
				      								type=type.substring(6, type.length());
				      								out.print(type+","+pendinglist.size()+s);%>')"/>
											</div>
										</div>
										
									</div>
									
								</div>
							</div>
							<div style="width: 50%; float: left;">
								<div class="panel panel-danger">
  									<div class="panel-heading"><b>Updates to approve</b></div>
  									<div class="panel-body">
										<%
					       				for(GridElement ge: pendinglist){
					       				%><div class="panel panel-default">
					       				<%out.println(Utils.gridElementToHTMLString(ge,ges,true)); %>
					       					<div class="panel-footer">
												<input type="button" value="Accept" onclick="acceptGE('<%
														s=gson.toJson(ge); 
														s=s.replaceAll("\"", "#"); 
														type=ge.getClass().toString();
					      								type=type.substring(6, type.length());
					      								out.print(type+","+pendinglist.size()+s);%>')"/>
											</div>
					       				</div>
						       			<%
					       				}
					       				%>

									</div>
								</div>
							</div>
						</div>
					</div>
        		
            			
    </c:when>    
    <c:otherwise>
    	<div style="float: left;width: 100%; text-align: center;">
			<img style="max-height:100px; max-width: 100px;" alt="Alert" src="<c:url value='/resources/images/warning.png' />">
			<h1>${error}</h1>
		</div>
    </c:otherwise>
</c:choose>
    
    
	</div><!-- /.container -->
     <!--Include these script files in the <head> or <body> tag-->
      <script src="<c:url value='/resources/lobibox/lib/jquery.1.11.min.js' />"></script>
      <script src="<c:url value='/resources/lobibox/dist/js/lobibox.min.js' />"></script>
      <!-- If you do not need both (messageboxes and notifications) you can inclue only one of them -->
      <script src="<c:url value='/resources/lobibox/dist/js/messageboxes.min.js' />"></script> 
      <script src="<c:url value='/resources/lobibox/dist/js/notifications.min.js' />"></script> 
 </body>
	<%@ include file="footer.jsp" %>

   	
</html>
