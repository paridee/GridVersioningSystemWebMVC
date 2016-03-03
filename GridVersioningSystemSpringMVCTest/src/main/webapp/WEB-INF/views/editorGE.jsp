<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>

<!DOCTYPE html>
<head>
  <meta charset="utf-8" />
  
      <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="resources/bootstrap/favicon.ico">

    <title>${pageTitle}${pageTitleDetails}</title>
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
     <link href="<c:url value='/resources/bootstrap/carousel.css' />" rel="stylesheet">
     <!-- Added styles -->
     <link href="<c:url value='/resources/gvs.css' />" rel="stylesheet">
       <!-- jQuery -->
     <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  
  
  <!-- Firebase -->
  <script src="https://cdn.firebase.com/js/client/2.3.2/firebase.js"></script>

  <!-- CodeMirror and its JavaScript mode file -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.2.0/codemirror.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.2.0/mode/javascript/javascript.js"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.2.0/codemirror.css" />

  <!-- Firepad -->
  <link rel="stylesheet" href="https://cdn.firebase.com/libs/firepad/1.3.0/firepad.css" />
  <script src="https://cdn.firebase.com/libs/firepad/1.3.0/firepad.min.js"></script>

  <style>
    html { height: 100%; }
    body { margin: 0; height: 100%; position: relative; }
    /* Height / width / positioning can be customized for your use case.
       For demo purposes, we make firepad fill the entire browser. */
    .firepad-container {
      width: 100%;
      height: 50px;
    }
  </style>
</head>

<body>
 <%@ include file="navBar.jsp" %>
 <div class="container">
${pad}
</div>

<%@ include file="footer.jsp" %>

   	 
</body>
</html>