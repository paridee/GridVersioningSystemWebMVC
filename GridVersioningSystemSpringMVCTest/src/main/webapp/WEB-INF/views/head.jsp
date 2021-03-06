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

    <title>${pageTitle}${pageTitleDetails}</title>
    <!--Treant core CSS -->
	    <link rel="stylesheet" href="<c:url value='/resources/Treant/Treant.css' />" type="text/css"/>
	    <link href="<c:url value='/resources/Treant/vendor/perfect-scrollbar/perfect-scrollbar.css' />" rel="stylesheet" type="text/css"/>
		<link rel="stylesheet" href="<c:url value='/resources/Treant/layoutChart.css' />" type="text/css"/>

    <!-- Bootstrap core CSS -->
    <link href="<c:url value='/resources/bootstrap/dist/css/bootstrap.min.css' />" rel="stylesheet">

    
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
     <link rel="stylesheet" href="<c:url value='/resources/lobibox/dist/css/Lobibox.min.css' />"/>
     
  </head>