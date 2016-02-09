<%@ include file="head.jsp" %>

  <body>
	<div class="container">
    <%@ include file="navBar.jsp" %>
	
	<c:if test="${!empty PendingProjects}">
    	<c:forEach items="${PendingProjects}" var="PendingProjectsItem">
		        
		        	ProjId: ${PendingProjectsItem.id}-
		        	<c:set var="currentProjId">${PendingProjectsItem.id}</c:set>
		        	NPendingGrids: ${PendingProjectsGrids[currentProjId].size()}<br>
		        	<c:forEach items="${PendingProjectsGrids[currentProjId]}" var="PendingProjectsGridItem">
		        		<c:set var="currentGridId">${PendingProjectsGridItem.id}</c:set>
		        		<c:set var="currentLabel">${PendingProjectsItem.id}-${PendingProjectsGridItem.id}</c:set>
		        		GridID:${currentGridId} - n elementi da approvare:${PendingProjectsGridsElements[currentLabel].size()} <br>
		        	
		        	</c:forEach>
		   
		        
		    </c:forEach>
    	
	</c:if>
    
    
	</div><!-- /.container -->
    
 </body>
	<%@ include file="footer.jsp" %>

   	
</html>
