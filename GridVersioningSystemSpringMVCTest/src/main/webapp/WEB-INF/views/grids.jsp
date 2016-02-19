<%@ include file="head.jsp" %>

  <body>

    <%@ include file="navBar.jsp" %>

    <div class="container">
    	
    	<c:if test="${(gridAdded!= null)&&(grid==null)}">
		<div class="starter-template">
			${gridAdded} test
		</div>
		</c:if>
    	<c:if test="${grid!= null}">
		<div class="starter-template">
			<table class="tg">
		    <tr>
		        <th width="80">Grid ID</th>
		        <th width="120">Grid Version</th>
		        <th width="120">ProjectID</th>
		    </tr>
		    <tr>
		            <td><a href="<c:url value='/grids/${grid.id}' />" >${grid.id}</a></td>
		            <td>${grid.version}</td>
		            <td><a href="<c:url value='/projects/${grid.project.id}' />" >${grid.project.id}</a></td>
		    </tr>
		    </table>
		</div>
		<div id="gridChart"> </div>
		    
		</c:if>
      	
        <c:if test="${!empty listGrids}">
        <div class="starter-template">
			<h3>Lista Griglie</h3>
			<p class="lead">Trovate: ${nGrids } griglie</p>
		    <table class="tg">
		    <tr>
		        <th width="80">Grid ID</th>
		        <th width="120">Grid Version</th>
		        <th width="120">ProjectID</th>
		        <th width="120">State</th>
		    </tr>
		    <c:forEach items="${listGrids}" var="listgriditem">
		    	<c:set var="currentGridId">${listgriditem.id}</c:set>
		        <tr>
		            <td><a href="<c:url value='/grids/${listgriditem.id}' />" >${listgriditem.id}</a></td>
		            <td>${listgriditem.version}</td>
		            <td><a href="<c:url value='/projects/${listgriditem.project.id}' />" >${listgriditem.project.id}</a></td>
		        	<td>${status[currentGridId]}</td>
		        </tr>
		    </c:forEach>
		    </table>
		    </div>
		</c:if>
		
		
		
		
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
   	 
</html>
