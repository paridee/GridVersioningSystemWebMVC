

<%@ include file="head.jsp" %>

  <body>

    <%@ include file="navBar.jsp" %>

    <div class="container">
    	<div style="text-align: center">
    	<c:choose>
	    	<c:when test="${grid!= null}">
	    		<div style="width: 100%; float: left; text-align: left;"  class="page-header">
					<h1><b>Grid ID</b> ${grid.id}<small> - <b>project</b> <a href="<c:url value='/projects/${grid.project.id}' />" >${grid.project.projectId}</a></small></h1>
					<h3><b>creation date:</b> </h3>
					<div class="panel panel-default">
					<div class="panel-heading"><b>Grid Chart</b></div>
					<div class="panel-body">
						<div id="gridChart"> </div>
					</div>
				</div>
				</div>
				
			</c:when>
	      	<c:when test="${!empty listGrids}">
	        <h1>Grids list <small> - grids found: ${listGrids.size()}</small></h1>
				<div class="table-responsive"> 
					<table class="table table-striped table-hover">
						<thead>
						    <tr>
						        <th>Grid ID</th>
						        <th>Grid Version</th>
						        <th>Project</th>
						        <th>State</th>
						        <th>Creation date</th>
						    </tr>
					    </thead>
					    <tbody>
						    <c:forEach items="${listGrids}" var="listgriditem">
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
						            <td>${listgriditem.version}</td>
						            <td><a href="<c:url value='/projects/${listgriditem.project.id}' />" >${listgriditem.project.projectId}</a></td>
						        	<td>${status[currentGridId]}</td>
						        	<td>${listgriditem.dateStringFromTimestamp()}</td>
						        </tr>
						    </c:forEach>
					    </tbody>
			    	</table>
		    	</div>
			</c:when>
			<c:otherwise>
				<div style="float: left;width: 100%; text-align: center;">
    				<img style="max-height:100px; max-width: 100px;" alt="Alert" src="<c:url value='/resources/images/warning.png' />">
    				<h1>No Grids available in the system</h1>
 		  		</div>
			</c:otherwise>
		</c:choose>
		</div>
		
		
		
		
</div>

		
		
    <!-- /.container -->
    


	</body>

<%@ include file="footer.jsp" %>
<c:if test="${gridTreeString!=null}">
<!-- Treant javascript -->
    <script src="<c:url value='/resources/Treant/vendor/raphael.js' />"></script>
    <script src="<c:url value='/resources/Treant/Treant.min.js' />"></script>
    <script src="<c:url value='/resources/Treant/vendor/jquery.easing.js' />"></script>
	<script src="<c:url value='/resources/Treant/vendor/perfect-scrollbar/jquery.mousewheel.js' />"></script>
	<script src="<c:url value='/resources/Treant/vendor/perfect-scrollbar/perfect-scrollbar.js' />"></script><script>${gridTreeString};
    	var my_chart = new Treant(chart_config);
   	</script>
</c:if>
<script type="text/javascript">
    jQuery(document).ready(function($) {
        $(".clickable-row").click(function() {
            window.document.location = $(this).data("href");
        });
    });
    </script>
   	 
</html>
