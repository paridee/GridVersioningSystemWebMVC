<%@ include file="head.jsp" %>


  <body>
	<div class="container">
    <%@ include file="navBar.jsp" %>
    	 <h1>Element History <small> - ${type}-${label}</small></h1>
    		<div class="table-responsive"> 
					<table class="table table-striped table-hover">
						<thead>
						    <tr>
						        <th>ID</th>
						        <th>Version</th>
						        <th>State</th>
						    </tr>
					    </thead>
					    <tbody>
						    <c:forEach items="${listGridElements}" var="listgriditem">
						    	<c:choose>
						    		<c:when test="${listgriditem.state=='MAJOR_UPDATING'||gridElement.state=='MAJOR_CONFLICTING'||gridElement.state=='MINOR_CONFLICTING'}">
						    			<tr class='clickable-row danger' data-href='<c:url value='/element/${listgriditem.getClass().getSimpleName()}/${listgriditem.idElement}' />' >
						    		</c:when>
						    		<c:otherwise>
						    			<tr class='clickable-row' data-href='<c:url value='/element/${listgriditem.getClass().getSimpleName()}/${listgriditem.idElement}' />' >
						    		</c:otherwise>
						    	</c:choose>
						    	
						            <td>${listgriditem.idElement}</td>
						            <td>${listgriditem.version}</td>
						            <td>${listgriditem.state}</td>
						        </tr>
						    </c:forEach>
					    </tbody>
			    	</table>
		    	</div>
    </div><!-- /.container -->
    
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
    