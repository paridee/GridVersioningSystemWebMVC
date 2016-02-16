<%@ include file="head.jsp" %>

  <body>
	<div class="container">
    <%@ include file="navBar.jsp" %>
	
	<c:if test="${!empty PendingProjects}">
    	<c:forEach items="${PendingProjects}" var="PendingProjectsItem">
		        
		        	<span style="font-size: 22px;">Project Id: ${PendingProjectsItem.id}</span>-
		        	<c:set var="currentProjId">${PendingProjectsItem.id}</c:set>
		        	NPendingGrids: ${PendingProjectsGrids[currentProjId].size()}<br><br>
		        	<c:forEach items="${PendingProjectsGrids[currentProjId]}" var="PendingProjectsGridItem">
		        		<c:set var="currentGridId">${PendingProjectsGridItem.id}</c:set>
		        		<c:set var="currentLabel">${PendingProjectsItem.id}-${PendingProjectsGridItem.id}</c:set>
		        		<b>GridID:${currentGridId} </b><br> 
		        		<c:if test="${!empty MajorPendingProjectsGridsElements[currentLabel]}">
			        		<div style="float: left; width: 100%;">
				        		Major Updates (${MajorPendingProjectsGridsElements[currentLabel].size()}):   
				        		<c:forEach items="${MajorPendingProjectsGridsElements[currentLabel]}" var="MajorPendingProjectsGridsElementsItem">
				        			<a href="<c:url value='/GEResolution/${MajorPendingProjectsGridsElementsItem.getClass().getSimpleName()}/${MajorPendingProjectsGridsElementsItem.label}'/>">${MajorPendingProjectsGridsElementsItem.label}</a> -</c:forEach>
			        		</div>
		        		</c:if>
			        	<c:if test="${!empty MajorConflictProjectsGridsElements[currentLabel]}">
			        		<div style="float: left; width: 100%;">
				        		Major Conflicts (${MajorConflictProjectsGridsElements[currentLabel].size()}):   
				        		<c:forEach items="${MajorConflictProjectsGridsElements[currentLabel]}" var="MajorConflictProjectsGridsElementsItem">
				        			<a href="<c:url value='/GEResolution/${MajorConflictProjectsGridsElementsItem.getClass().getSimpleName()}/${MajorConflictProjectsGridsElementsItem.label}'/>">${MajorConflictProjectsGridsElementsItem.label}</a> -</c:forEach>
			        		</div>
			        	</c:if>
			        	<c:if test="${!empty MinorConflictProjectsGridsElements[currentLabel]}">
			        		<div style="float: left; width: 100%;">
				        		Minor conflicts (${MinorConflictProjectsGridsElements[currentLabel].size()}):   
				        		<c:forEach items="${MinorConflictProjectsGridsElements[currentLabel]}" var="MinorConflictProjectsGridsElementsItem">
				        			<a href="<c:url value='/GEResolution/${MinorConflictProjectsGridsElementsItem.getClass().getSimpleName()}/${MinorConflictProjectsGridsElementsItem.label}'/>">${MinorConflictProjectsGridsElementsItem.label}</a> -</c:forEach>
			        		</div>
			        	</c:if>
		        		<c:if test="${!empty GridsMainGoalListChanged[currentLabel]}">
		        			<div style="float: left; width: 100%;">
		        				<a href="<c:url value='/MGLCResolution/${currentGridId}'/>">Main Goal List Changed</a>
		        			</div>
		        		</c:if>
		        		<br><br>
		        	</c:forEach>
		   		<br>
		        
		    </c:forEach>
    	
	</c:if>
    
    
	</div><!-- /.container -->
    
 </body>
	<%@ include file="footer.jsp" %>

   	
</html>
