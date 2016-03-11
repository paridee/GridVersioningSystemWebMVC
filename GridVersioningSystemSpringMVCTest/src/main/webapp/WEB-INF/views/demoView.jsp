<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="resources/bootstrap/favicon.ico">
<link rel="stylesheet" href="<c:url value='/resources/lobibox/dist/css/Lobibox.min.css' />"/>
<title>GQM+S GridVersioningSystem DEMO</title>

<!-- Bootstrap core CSS -->
<link
	href="<c:url value='/resources/bootstrap/dist/css/bootstrap.min.css' />"
	rel="stylesheet">

<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<link
	href="<c:url value='/resources/bootstrap/assets/css/ie10-viewport-bug-workaround.css' />"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link href="<c:url value='/resources/bootstrap/starter-template.css' />"
	rel="stylesheet">

<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
<!--[if lt IE 9]><script src="<c:url value='/resources/assets/js/ie8-responsive-file-warning.js' />"></script><![endif]-->
<script
	src="<c:url value='/resources/bootstrap/assets/js/ie-emulation-modes-warning.js' />"></script>

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js' />"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js' />"></script>
    <![endif]-->
<script type="text/javascript">
	function makeRequest(project,request, parms) {
		$.ajax({
			type : "POST",
			url : "/ISSSR/Requests",
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			data : JSON.stringify({
				"project" : project,
				"request" : request,
				"parms" : parms
			}),
			success : function(msg) {
				$( "#ErmesResponse" ).html(JSON.stringify(msg));
				//alert(JSON.stringify(msg))
			},
			error : function(err) {
				$( "#ErmesResponse" ).html(JSON.stringify(msg));
				//alert(err.responseText)
			}
		});
	}

	 function uploadGrid(filename) {
		 	$.ajax({url: "/ISSSR/resources/"+filename, success: function(data) {
	    		jQuery.ajax({
	           		type: "POST",
	      	   		url: "grids/update",
		      	   	contentType: "application/json; charset=utf-8",
		      	    dataType: "json",
	    	  	   	data: data,
	    	  	  	success: function (response) { 
				  		Lobibox.alert(response.type, //AVAILABLE TYPES: "error", "info", "success", "warning"
			    			{
			    			    msg: response.msg,
			    			    closeButton     : false,
			    			});
				  	},
					error: function (err){
						Lobibox.alert("error", //AVAILABLE TYPES: "error", "info", "success", "warning"
				    			{
				    			    msg: err.responseText,
				    			});
					}
	                
	            });
	    	}, cache: false});
			
	 }
	 function addGrid(filename) {
			$.ajax({url: "/ISSSR/resources/"+filename, success: function(data) {
	    		jQuery.ajax({
	           		type: "POST",
	      	   		url: "grids/add",
		      	   	contentType: "application/json; charset=utf-8",
		      	    dataType: "json",
	    	  	   	data: data,
	    	  	  	success: function (response) { 
				  		Lobibox.alert(response.type, //AVAILABLE TYPES: "error", "info", "success", "warning"
				    			{
				    			    msg: response.msg,
				    			    closeButton     : false,
				    			});
					  	},
						error: function (err){
							alert(err.responseText);
							Lobibox.alert("error", //AVAILABLE TYPES: "error", "info", "success", "warning"
					    			{
					    			    msg: err.responseText,
					    			});
						}
	                
	            });
	    	}, cache: false});
			
	 }
</script>




</head>

<body>
	<div class="container" style="margin-top: 0px;">
		<h1>GQM+S GridVersioningSystem DEMO</h1>
		<div class="panel panel-success">
			<div class="panel-heading">Upload Grids to GVS</div>
			<div class="panel-body" style="text-align: center;">
				<div class="panel panel-success" style="width: 30%; display: inline-block;margin: auto;">
					<div class="panel-heading">1) First Grid for a project (Phase 2)</div>
					<div class="panel-body">
						<input class="btn btn-primary" type="button" value="Upload FIRST Grid" onclick="addGrid('grid.txt')"/>
					</div>
				</div>
				<div class="panel panel-success" style="width: 30%; display: inline-block;margin: auto;">
					<div class="panel-heading">2) First update</div>
					<div class="panel-body">
						<input class="btn btn-primary" type="button" value="Send first update" onclick="uploadGrid('grid2.txt')"/>
					</div>
				</div>
				<div class="panel panel-success" style="width: 30%; display: inline-block;margin: auto;">
					<div class="panel-heading">3) Second update</div>
					<div class="panel-body">
						<input class="btn btn-primary" type="button" value="Send second update" onclick="uploadGrid('grid3.txt')"/>
					</div>
				</div>
				
				
				
				
			</div>
		</div>
		<div style="width: 100%; float: left;position: relative;">
		<div class="panel panel-info" style="float: left; width: 49%">
				<div class="panel-heading"><b>Requests</b></div>
				<div class="panel-body">
					<div class="panel-group">
						<div class="panel panel-default">
							<div class="panel-heading">Request the list of all projects available on GVS</div>
							<div class="panel-body">
								<input class="btn btn-primary" type="button" value="Get project list"
									onclick="makeRequest('','ProjectList','')" />
							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading">Request Project-info</div>
							<div class="panel-body">
								<input class="btn btn-primary" type="button" value="Get project info"
									onclick="makeRequest('','Project-info','')" />
							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading">Get the working grid for the selected project</div>
							<div class="panel-body">
								<input type="text" id="demoData1"/>
								<input class="btn btn-primary" type="button" value="Get working Grid"
									onclick="makeRequest(document.getElementById('demoData1').value,'LatestGrid','')" />
							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading">Get all grids stored on GVS for the selected project</div>
							<div class="panel-body">
								<input type="text" id="demoData2"/>
								<input class="btn btn-primary" type="button" value="Get grid history"
									onclick="makeRequest(document.getElementById('demoData2').value,'GridHistory','')" />							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading">Request a single grid</div>
							<div class="panel-body">
								<input type="text" id="demoData5"/>
								<input class="btn btn-primary" type="button" value="Get Grid"
									onclick="makeRequest('','Grid', document.getElementById('demoData5').value)" />
							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading">Request a single grid element</div>
							<div class="panel-body">
								<div style="float: left; width: 100%; text-align: center;">
									<div style="float:left; width: 50%;">ID<br>
								 		<input type="text" id="demoData3"/>
									</div>
									<div style="float:left; width: 50%;">Type<br>
										<select id="elementList1">
										  <option value="Goal">Goal</option>
										  <option value="MeasurementGoal">MeasurementGoal</option>
										  <option value="Metric">Metric</option>
										  <option value="Question">Question</option>
										  <option value="Strategy">Strategy</option>
										</select>					
									</div>
								</div>
								<div style="float: left; width: 100%; text-align: center; margin-top: 5px;">
									<input class="btn btn-primary" type="button" value="Get GridElement"
									onclick="makeRequest('','GridElement', document.getElementById('demoData3').value+','+document.getElementById('elementList1').value)" />
								</div>
							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading">Get the list of all version of a grid element</div>
							<div class="panel-body">
								<div style="float: left; width: 100%; text-align: center;">
									<div style="float:left; width: 50%;">Label<br>
								 		<input type="text" id="demoData6"/>
									</div>
									<div style="float:left; width: 50%;">Type<br>
										<select id="elementList2">
										  <option value="Goal">Goal</option>
										  <option value="MeasurementGoal">MeasurementGoal</option>
										  <option value="Metric">Metric</option>
										  <option value="Question">Question</option>
										  <option value="Strategy">Strategy</option>
										</select>					
									</div>
								</div>
								<div style="float: left; width: 100%; text-align: center; margin-top: 5px;">
									<input class="btn btn-primary" type="button" value="Get GridElementHistory"
									onclick="makeRequest('','GridElementHistory', document.getElementById('demoData6').value+','+document.getElementById('elementList2').value)" />
								</div>
							</div>
						</div>
					</div>



				</div>
			</div>
			<div class="panel panel-danger" style="position: absolute; width: 49%; top:0; bottom: 0; right: 0; ">
				<div class="panel-heading" ><b>Response from ERMES Bus</b></div>
				<div class="panel-body" id="ErmesResponse" style="overflow: auto; max-height: 90%;"></div>
				
			</div>
	

</div>














	</div>



	<!-- /.container -->


	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script>
		window.jQuery
				|| document
						.write('<script src="<c:url value='/resources/bootstrap/assets/js/vendor/jquery.min.js' />"><\/script>')
	</script>
	<script
		src="<c:url value='/resources/bootstrap/dist/js/bootstrap.min.js' />"></script>
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script
		src="<c:url value='/resources/bootstrap/assets/js/ie10-viewport-bug-workaround.js' />"></script>
	<!--Include these script files in the <head> or <body> tag-->
      <script src="<c:url value='/resources/lobibox/lib/jquery.1.11.min.js' />"></script>
      <script src="<c:url value='/resources/lobibox/dist/js/lobibox.min.js' />"></script>
      <!-- If you do not need both (messageboxes and notifications) you can inclue only one of them -->
      <script src="<c:url value='/resources/lobibox/dist/js/messageboxes.min.js' />"></script> 
      <script src="<c:url value='/resources/lobibox/dist/js/notifications.min.js' />"></script> 



</body>
</html>




