<%@ page import="grid.Utils"%>
<%@ page import =" grid.interfaces.services.*" %>
<%@ page import =" grid.entities.*" %>
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
    		var newMainGoalList=[];
    		var workingMainGoalList=${workingMGList};
    		function acceptEditedList(){
    			var edited=[];
    			edited.push(workingMainGoalList[0]);
    			edited.push(workingMainGoalList[1]);
    			if(newMainGoalList.length>0){
	    			for (var i = 0; i < newMainGoalList.length; i++) {
	    				edited.push(newMainGoalList[i]);
	    		    }
	    			acceptList(edited);
    			}
    			else{
    				alert("Main Goal List can not be empty");
    			}
    			
    		}
    		function drawMGList(){
    			var txt="";
    			for (var i = 0; i < newMainGoalList.length; i++) {
    				txt=txt+"<span class=\"label label-success\"  style=\"margin-left: 5px;\">"+newMainGoalList[i]+"</span>";
    		    }
    			$("#newGoalList").html( txt );
    		}
    		function addMG(label){
    			var index = newMainGoalList.indexOf(label);
    			if (index == -1) {
    				newMainGoalList.push(label);
    			}
    			drawMGList();
    		}
    		function removeMG(label){
    			var index = newMainGoalList.indexOf(label);
    			if (index > -1) {
    				newMainGoalList.splice(index, 1);
    			}
    			drawMGList();
    		}
    		function acceptList(list){
    			var newjson=JSON.stringify(list);
    			jQuery.ajax({
			   		type: "POST",
			  		url: "/ISSSR/MGListUpdate",
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
    		}
    	</script>
    	<%GridElementService ges=(GridElementService)request.getAttribute("GEService");
    	%>
    	<div class="panel panel-default">
			<div class="panel-heading">
				<b>Working main goal list</b>
			</div>
			<div class="panel-body">
<%
				Grid workingGrid=(Grid)request.getAttribute("workingGrid");
				List<Goal> currentGoals=workingGrid.getMainGoals();
				for(Goal fieldValueGE: currentGoals){
					int id=ges.getLatestWorking(fieldValueGE.getLabel(), fieldValueGE.getClass().getSimpleName()).getIdElement();
					out.print("<a style=\"text-decoration:none;\" href=\"/ISSSR/element/"+fieldValueGE.getClass().getSimpleName()+"/"+id+"\"><span class=\"label label-success\" style=\"margin-left: 5px;\">"+fieldValueGE.getLabel()+"</span></a>");
					
				}
				
				%>			</div>
			<div class="panel-footer">
				<input type="button" value="Accept" onclick="acceptList(${workingMGList})"/><br>
			</div>
		</div>
    	<div class="panel panel-danger">
			<div class="panel-heading">
				<b>New list to approve</b>
			</div>
			<div class="panel-body">
				<%
				Grid currentGrid=(Grid)request.getAttribute("currentGrid");
				currentGoals=currentGrid.getMainGoals();
				for(Goal fieldValueGE: currentGoals){
					int id=ges.getLatestWorking(fieldValueGE.getLabel(), fieldValueGE.getClass().getSimpleName()).getIdElement();
					out.print("<a style=\"text-decoration:none;\" href=\"/ISSSR/element/"+fieldValueGE.getClass().getSimpleName()+"/"+id+"\"><span class=\"label label-success\" style=\"margin-left: 5px;\">"+fieldValueGE.getLabel()+"</span></a>");
					
				}
				
				%>
			
			
			</div>
			<div class="panel-footer">
				<input type="button" value="Accept" onclick="acceptList(${currentMGList})"/><br>
			</div>
		</div>
		
		<div class="panel panel-info">
			<div class="panel-heading">
				<b>Create new main goal list</b>
				<div class="dropdown" style="display: inline-block;">
				  <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
				    Add Main Goal
				    <span class="caret"></span>
				  </button>
				  <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
				    <c:forEach items="${mergedGoalLabels}" var="mergedGoalLabelsItem">
				  		<li><a href="#" onclick="addMG('${mergedGoalLabelsItem}');">${mergedGoalLabelsItem}</a></li>
				  	</c:forEach>
				  </ul>
				</div>
				<div class="dropdown" style="display: inline-block;">
				  <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
				    Remove Main Goal
				    <span class="caret"></span>
				  </button>
				  <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
				    <c:forEach items="${mergedGoalLabels}" var="mergedGoalLabelsItem">
				  		<li><a href="#" onclick="removeMG('${mergedGoalLabelsItem}');">${mergedGoalLabelsItem}</a></li>
				  	</c:forEach>
				  </ul>
				</div>
				
			</div>
			<div class="panel-body" id="newGoalList">
			
			</div>
			<div class="panel-footer">
				<input type="button" value="Accept" onclick="acceptEditedList()"/><br>
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
    
 </body>
	<%@ include file="footer.jsp" %>

   	
</html>
