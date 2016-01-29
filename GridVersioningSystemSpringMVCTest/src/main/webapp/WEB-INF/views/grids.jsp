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

    <title>Lista Grids</title>
    <!--Treant core CSS -->
	    <link rel="stylesheet" href="<c:url value='/resources/Treant/Treant.css' />" type="text/css"/>
	    <link href="<c:url value='/resources/Treant/vendor/perfect-scrollbar/perfect-scrollbar.css' />" rel="stylesheet" type="text/css"/>
		<link rel="stylesheet" href="<c:url value='/resources/Treant/layoutChart.css' />" type="text/css"/>

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
    	
    	<c:if test="${(gridAdded!= null)&&(grid==null)}">
		<div class="starter-template">
			${gridAdded} test
		</div>
		</c:if>
    	<c:if test="${grid!= null}">
		<div class="starter-template">
			<table class="tg">
		    <tr>
		        <th width="80">Grid ID</th>
		        <th width="120">Grid Version</th>
		        <th width="120">ProjectID</th>
		    </tr>
		    <tr>
		            <td><a href="<c:url value='/grids/${grid.id}' />" >${grid.id}</a></td>
		            <td>${grid.version}</td>
		            <td><a href="<c:url value='/projects/${grid.project.id}' />" >${grid.project.id}</a></td>
		    </tr>
		    </table>
		</div>
		<div id="gridChart"> </div>
		    
		</c:if>
      	
        <c:if test="${!empty listGrids}">
        <div class="starter-template">
			<h3>Lista Griglie</h3>
			<p class="lead">Trovate: ${nGrids } griglie</p>
		    <table class="tg">
		    <tr>
		        <th width="80">Grid ID</th>
		        <th width="120">Grid Version</th>
		        <th width="120">ProjectID</th>
		    </tr>
		    <c:forEach items="${listGrids}" var="listgriditem">
		        <tr>
		            <td><a href="<c:url value='/grids/${listgriditem.id}' />" >${listgriditem.id}</a></td>
		            <td>${listgriditem.version}</td>
		            <td><a href="<c:url value='/projects/${listgriditem.project.id}' />" >${listgriditem.project.id}</a></td>
		        </tr>
		    </c:forEach>
		    </table>
		    </div>
		</c:if>
		
		
		
		
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
  
      <!-- Treant javascript -->
    <script src="<c:url value='/resources/Treant/vendor/raphael.js' />"></script>
    <script src="<c:url value='/resources/Treant/Treant.min.js' />"></script>
    <script src="<c:url value='/resources/Treant/vendor/jquery.easing.js' />"></script>
	<script src="<c:url value='/resources/Treant/vendor/perfect-scrollbar/jquery.mousewheel.js' />"></script>
	<script src="<c:url value='/resources/Treant/vendor/perfect-scrollbar/perfect-scrollbar.js' />"></script><script>
    	${gridTreeString};
    	var my_chart = new Treant(chart_config);
   	</script>
  
  </body>
</html>


