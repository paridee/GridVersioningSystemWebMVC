
<%@ page import =" grid.interfaces.services.*" %>
<%@ include file="head.jsp" %>


  <body>
	<div class="container">
    <%@ include file="navBar.jsp" %>
    	<c:choose>
    		<c:when test="${error==NULL}">
	    		<div class="panel panel-primary">
					<% GridElement ge=(GridElement)request.getAttribute("element");
					GridElementService ges=(GridElementService)request.getAttribute("GEService");
					String geString=Utils.gridElementToHTMLString(ge,ges,false);
					out.println(geString);
					%>
					<div class="panel-footer" style="text-align: right;">
						<a class="btn btn-primary" href="/ISSSR/elementhistory/<%out.print(ge.getClass().getSimpleName()); %>/<%out.print(ge.getLabel()); %>" role="button">Show element history</a>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading"><b>Grid Element Chart</b></div>
					<div class="panel-body">
						<div id="gridChart"> </div>
					</div>
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
    