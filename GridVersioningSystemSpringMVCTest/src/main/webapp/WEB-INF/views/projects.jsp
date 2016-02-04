<%@ include file="head.jsp" %>

  <body>

    <%@ include file="navBar.jsp" %>
    <div class="container">
    	<c:if test="${reqproject!=null}">
    		<div>
	    		<table class="tg">
			    <tr>
			        <th width="80">id</th>
			        <th width="120">ProjectID</th>
			        <th width="120">description</th>
			        <th width="120">creationDate</th>
			    </tr>
			    <tr>
			            <td><a href="<c:url value='/projects/${project.id}' />">${project.id}</a></td>
			            <td>${reqproject.projectId}</td>
			            <td>${reqproject.description}</td>
			            <td>${reqproject.creationDate}</td>
			       </tr>
			    </table>
		    </div>
		    <c:if test="${!empty listProjectGrids}">
		        <div class="starter-template">
					<h3>Lista Griglie</h3>
					<p class="lead">Trovate: ${nProjectGrids } griglie</p>
				    <table class="tg">
				    <tr>
				        <th width="80">Grid ID</th>
				        <th width="120">Grid Version</th>
				        <th width="120">ProjectID</th>
				    </tr>
				    <c:forEach items="${listProjectGrids}" var="listprojectgriditem">
				        <tr>
				            <td><a href="<c:url value='/grids/${listprojectgriditem.id}' />" >${listprojectgriditem.id}</a></td>
				            <td>${listprojectgriditem.version}</td>
				            <td><a href="<c:url value='/projects/${listprojectgriditem.project.id}' />" >${listprojectgriditem.project.id}</a></td>
				        </tr>
				    </c:forEach>
				    </table>
			    </div>
			</c:if>
    	
    	
    	</c:if>
    
    
    
		<c:if test="${!empty listProjects}">
      <div class="starter-template">
        <h1>Lista Progetti</h1>
        <p class="lead">Trovati: ${nProjects } progetti</p>
      </div>
		
			<h3>Lista Progetti</h3>
		    <table class="tg">
		    <tr>
		        <th width="80">id</th>
		        <th width="120">ProjectID</th>
		        <th width="120">description</th>
		        <th width="120">creationDate</th>
		    </tr>
		    <c:forEach items="${listProjects}" var="project">
		        <tr>
		            <td><a href="<c:url value='/projects/${project.id}' />">${project.id}</a></td>
		            <td>${project.projectId}</td>
		            <td>${project.description}</td>
		            <td>${project.creationDate}</td>
		        </tr>
		    </c:forEach>
		    </table>
		</c:if> 
		
		

    </div><!-- /.container -->

<%@ include file="footer.jsp" %>




