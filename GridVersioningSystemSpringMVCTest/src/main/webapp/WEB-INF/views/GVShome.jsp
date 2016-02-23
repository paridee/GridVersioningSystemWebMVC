<%@ include file="head.jsp" %>

  <body>
	<div class="container">
    <%@ include file="navBar.jsp" %>

    
    
    	<!-- Carousel
    ================================================== -->
    <div id="myCarousel" class="carousel slide" data-ride="carousel">
      <!-- Indicators -->
      <ol class="carousel-indicators">
        <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
        <li data-target="#myCarousel" data-slide-to="1"></li>
        <li data-target="#myCarousel" data-slide-to="2"></li>
      </ol>
      <div class="carousel-inner" role="listbox">
        <div class="item active">
          <img class="first-slide" src="<c:url value='/resources/images/projects2.jpg'/>" alt="First slide">
          <div class="container">
            <div class="carousel-caption">
              <h1 style="color: black;">Available Projects</h1>
              <p style="color: black;">See the full list of projects available on GVS</p>
              <p style="color: black;"><a class="btn btn-lg btn-primary" href="<c:url value='/projects'/>" role="button">Projects</a></p>
            </div>
          </div>
        </div>
        <div class="item">
          <img class="second-slide" src="<c:url value='/resources/images/gqm2.png'/>" alt="Second slide">
          <div class="container">
            <div class="carousel-caption">
              <h1 style="color: black;">View Grids repository</h1>
              <p style="color: black;">Examine grids created through GQM+Strategies process</p>
              <p style="color: black;"><a class="btn btn-lg btn-primary" href="<c:url value='/grids'/>" role="button">Grids</a></p>
            </div>
          </div>
        </div>
        <div class="item">
          <img class="third-slide" src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" alt="Third slide">
          <div class="container">
            <div class="carousel-caption">
              <h1 style="color: black;">Pending Updates and conflicts</h1>
              <p style="color: black;">Manage pending updates and conflicts occurred during concurrent modifications of Grid Elements</p>
              <p style="color: black;"><a class="btn btn-lg btn-primary" href="<c:url value='resolutionDashBoard'/>" role="button">Pending changes</a></p>
            </div>
          </div>
        </div>
      </div>
      <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
        <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
      </a>
      <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
        <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
      </a>
    </div><!-- /.carousel -->
    </div><!-- /.container -->
 </body>
	<%@ include file="footer.jsp" %>

   	
</html>
