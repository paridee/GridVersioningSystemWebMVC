<%@ include file="head.jsp" %>

  <body>

    <%@ include file="navBar.jsp" %>
    <div class="container">
    	<c:choose>
	    	<c:when test="${reqproject!=null}">
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
			        
						<h3>Grids list   <small>number of grids found: ${nProjectGrids }</small></h3>
						<div class="table-responsive"> 
							<table class="table table-striped table-hover">
								<thead>
								    <tr>
								        <th>Grid ID</th>
								        <th>Grid Version</th>
								        <th>ProjectID</th>
								        <th>State</th>
								    </tr>
							    </thead>
							    <tbody>
								    <c:forEach items="${listProjectGrids}" var="listprojectgriditem">
								    <c:set var="currentGridId">${listprojectgriditem.id}</c:set>
								        <tr>
								            <td class='clickable-row' data-href='<c:url value='/grids/${listprojectgriditem.id}' />'>${listprojectgriditem.id}</td>
								            <td>${listprojectgriditem.version}</td>
								            <td><a href="<c:url value='/projects/${listprojectgriditem.project.id}' />" >${listprojectgriditem.project.id}</a></td>
								        	<td>${status[currentGridId]}</td>
								        </tr>
								    </c:forEach>
							    </tbody>
					    </table>
					    </div>
				    
				</c:if>
	    	
	    	
	    	</c:when>
	    	
	    
	    
			<c:when test="${!empty listProjects}">
		      <div style="text-align: center;">
		      	<h1>Project list <small> - projects found: ${listProjects.size()}</small></h1>
		      </div>
				<div class="table-responsive"> 
					<table class="table table-striped table-hover">
						<thead>
						    <tr>
						        <th>id</th>
						        <th>ProjectID</th>
						        <th>description</th>
						        <th>Project Manager</th>
						        <th>creationDate</th>
						    </tr>
					    </thead>
					    <tbody>
						    <c:forEach items="${listProjects}" var="project">
						        <tr class='clickable-row' data-href='<c:url value='/projects/${project.id}' />'>
						            <td>${project.id}</td>
						            <td>${project.projectId}</td>
						            <td>${project.description}</td>
						            <td>${project.projectManager.name}</td>
						            <td>${project.creationDate}</td>
						        </tr>
						    </c:forEach>
					    </tbody>
				    </table>
			    </div>
			</c:when> 
		
			<c:otherwise>
				<div style="float: left;width: 100%; text-align: center;">
	   				<img style="max-height:100px; max-width: 100px;" alt="Alert" src="<c:url value='/resources/images/warning.png' />">
	   				<h1>No Projects available in the system</h1>
	  			</div>
			</c:otherwise>
		</c:choose>
		

    </div><!-- /.container -->
    

<%@ include file="footer.jsp" %>
<script type="text/javascript">
    jQuery(document).ready(function($) {
        $(".clickable-row").click(function() {
            window.document.location = $(this).data("href");
        });
    });
    </script>

</html>

