<%@ include file="head.jsp" %>

  <body>

    <%@ include file="navBar.jsp" %>
    <div class="container">
    	<c:choose>
	    	<c:when test="${reqproject!=null}">
	    		<div style="width: 100%; float: left; text-align: left;"  class="page-header">
					<h1><b>Project</b> ${reqproject.projectId}<small> - <b>creation date</b> ${reqproject.creationDate}</small></h1>
					<h3><small><b>Description:</b> ${reqproject.description}</small></h3>
					<c:if test="${reqproject.projectManager!=null}">
					<h3><small><b>Project Manager:</b> ${reqproject.projectManager.name}</small></h3>
					</c:if>
					<c:if test="${reqproject.projectManager==null}">
					<h3><small><b>Project Manager:</b> Not available</small></h3>
					</c:if>					
				</div>
	    	
	    		<c:if test="${!empty listProjectGrids}">
			        
						<div style="text-align: center"><h3><b>Grids list</b>   <small>number of grids found: ${nProjectGrids }</small></h3></div>
						<div class="table-responsive"> 
							<table class="table table-striped table-hover">
								<thead>
								    <tr>
								        <th>Grid ID</th>
								        <th>Project</th>
								        <th>State</th>
								        <th>Creation date</th>
								    </tr>
							    </thead>
							    <tbody>
								    <c:forEach items="${listProjectGrids}" var="listgriditem">
								    <c:set var="currentGridId">${listgriditem.id}</c:set>
								        <c:choose>
								    		<c:when test="${status[currentGridId]=='UPDATING'||status[currentGridId]=='MGC-UPDATING'||status[currentGridId]=='MGC'}">
								    			<tr class='clickable-row danger' data-href='<c:url value='/grids/${listgriditem.id}' />' >
								    		</c:when>
								    		<c:otherwise>
								    			<tr class='clickable-row' data-href='<c:url value='/grids/${listgriditem.id}' />' >
								    		</c:otherwise>
								    	</c:choose>
								            <td>${listgriditem.id}</td>
								            <td><a href="<c:url value='/projects/${listgriditem.project.id}' />" >${listgriditem.project.projectId}</a></td>
								        	<td>${status[currentGridId]}</td>
								        	<td>${listgriditem.dateStringFromTimestamp()}</td>
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
	   				<h1>${error}</h1>
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

