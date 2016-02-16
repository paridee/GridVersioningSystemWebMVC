<nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="<c:url value='/GVShome'/>">Grid Versioning System</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li ${navClass0}><a href="<c:url value='/projects'/>">Projects</a></li>
            <li ${navClass1}><a href="<c:url value='/grids'/>">Grids</a></li>
            <li ${navClass2}><a href="<c:url value='/resolutionDashBoard'/>">Pending changes</a></li>
		  </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>