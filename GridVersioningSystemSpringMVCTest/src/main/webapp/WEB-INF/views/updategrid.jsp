<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>

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

    <title>Update</title>

    <!-- Bootstrap core CSS -->
    <link href="<c:url value='/resources/bootstrap/dist/css/bootstrap.min.css' />" rel="stylesheet">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="<c:url value='/resources/bootstrap/assets/css/ie10-viewport-bug-workaround.css' />" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="<c:url value='/resources/bootstrap/starter-template.css' />" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="<c:url value='/resources/assets/js/ie8-responsive-file-warning.js' />"></script><![endif]-->
    <script src="<c:url value='/resources/bootstrap/assets/js/ie-emulation-modes-warning.js' />"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js' />"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js' />"></script>
    <![endif]-->
    <script type="text/javascript">
    	//$.post("/grids/add", { jsonData: "testjsonData" });
    	
    	
    function submitForm() {
    	jQuery.get('http://localhost:8080/ISSSR/resources/grid2.txt', function(data) {
    		//alert(data);
    		/*$.post("grids/add",
    				data,
    			    function(resp, status){
    			        alert("Data: " + resp + "\nStatus: " + status);
    			    });*/
    		jQuery.ajax({
           		type: "POST",
      	   		url: "grids/update",
	      	   	contentType: "application/json; charset=utf-8",
	      	    dataType: "json",
    	  	   	data: data,
    	  	  	success: function (msg) 
              	{ alert(msg) },
      			error: function (err)
      			{ alert(err.responseText)}
                
            });
    	});
    	
    }
</script>


    	
   
  </head>

  <body>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Grid Versioning System</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">Home</a></li>
            <li><a href="#about">About</a></li>
            <li><a href="#contact">Contact</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

    <div class="container">
    	<div class="starter-template">
			<input type="button" value="Click" onclick="submitForm()"/>
		</div>
		
		
		
</div>

		
		
    <!-- /.container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="<c:url value='/resources/bootstrap/assets/js/vendor/jquery.min.js' />"><\/script>')</script>
    <script src="<c:url value='/resources/bootstrap/dist/js/bootstrap.min.js' />"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="<c:url value='/resources/bootstrap/assets/js/ie10-viewport-bug-workaround.js' />"></script>
  </body>
</html>




