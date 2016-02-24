<nav class="navbar navbar-inverse navbar-fixed-top">
      <a class="navbar-brand" href="<c:url value='/GVShome'/>"><img id="headim" alt="" src="resources/gqmlogo.png"></a>
      <div class="container">
	    <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li ${navClass0}><a href="<c:url value='/projects'/>">Projects</a></li>
            <li ${navClass1}><a href="<c:url value='/grids'/>">Grids</a></li>
            <li ${navClass2}><a href="<c:url value='/resolutionDashBoard'/>">Pending changes</a></li>
            <c:if test="${pageContext.request.userPrincipal.name == null}">
				<li ${navClass3}><a href="<c:url value='login'/>">Login</a></li>
			</c:if>
			<c:if test="${pageContext.request.userPrincipal.name != null}">
		  		<li ${navClass4}><a href="<c:url value='logout'/>">Logout</a></li>
		  	</c:if>
		  </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>