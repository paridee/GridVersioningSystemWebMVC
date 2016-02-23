<%@ include file="head.jsp" %>

  <body>

    <%@ include file="navBar.jsp" %>

    <div class="container">
    	<div style="text-align: center">
    	<c:choose>
	    	<c:when test="${grid!= null}">
			<div class="table-responsive"> 
				<table class="table table-striped table-hover">
					<thead>
					    <tr>
					        <th width="80">Grid ID</th>
					        <th width="120">Grid Version</th>
					        <th width="120">ProjectID</th>
					    </tr>
				    </thead>
				    <tbody>
					    <tr>
					            <td><a href="<c:url value='/grids/${grid.id}' />" >${grid.id}</a></td>
					            <td>${grid.version}</td>
					            <td><a href="<c:url value='/projects/${grid.project.id}' />" >${grid.project.id}</a></td>
					    </tr>
				    </tbody>
			    </table>
			</div>
			<div id="gridChart"> </div>
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
						    </tr>
					    </thead>
					    <tbody>
						    <c:forEach items="${listGrids}" var="listgriditem">
						    	<c:set var="currentGridId">${listgriditem.id}</c:set>
						        <tr class='clickable-row' data-href='<c:url value='/grids/${listgriditem.id}' />'>
						            <td>${listgriditem.id}</td>
						            <td>${listgriditem.version}</td>
						            <td><a href="<c:url value='/projects/${listgriditem.project.id}' />" >${listgriditem.project.projectId}</a></td>
						        	<td>${status[currentGridId]}</td>
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
