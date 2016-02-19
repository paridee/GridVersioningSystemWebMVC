<%@page import="grid.modification.grid.GridModification"%>
<%@ page import="grid.modification.grid.GridModificationService"%>
<%@ page import="grid.interfaces.services.GridElementService"%>
<%@ page import =" grid.entities.GridElement" %>
<%@ page import =" java.lang.reflect.Field" %>
<%@ page import =" java.util.List" %>
<%@ page import =" java.util.Map" %>

<%@ include file="head.jsp" %>

<%GridModificationService gms=(GridModificationService)request.getAttribute("GridModificationServiceInstance");
GridElementService ges=(GridElementService)request.getAttribute("GridModificationServiceInstance");%>

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
		        			<%
		        			/* Object projectGridsMainGoalListChangedObj=(Object)request.getAttribute("MajorPendingProjectsGridsElements");
		        			Map <String, String> projectGridsMainGoalListChanged
		        			int i=0;*/
		        			%>
			        		<div style="float: left; width: 100%;">
				        		Major Updates (${MajorPendingProjectsGridsElements[currentLabel].size()}):   
				        		<c:forEach items="${MajorPendingProjectsGridsElements[currentLabel]}" var="MajorPendingProjectsGridsElementsItem">
				        		<% 
				        			//check if PendingGridElements with this label are linked to other PendingGridElements
				        			//in order to limit resolution
				        		
				        			/*GridElement ge=pendinglist.get(i);
					        		List<GridElement> pending=ges.getElementByLabelAndState(ge.getLabel(), ge.getClass().getSimpleName() , GridElement.State.MAJOR_CONFLICTING);
					    			pending.addAll(ges.getElementByLabelAndState(ge.getLabel(), ge.getClass().getSimpleName(), GridElement.State.MAJOR_UPDATING));
					    			pending.addAll(ges.getElementByLabelAndState(ge.getLabel(), ge.getClass().getSimpleName(), GridElement.State.MINOR_CONFLICTING));
				    				boolean rejected=false;
					    			for(int j=0; j<pending.size()&&!rejected;j++){
				    					if(gms.isEmbeddedPending(pending.get(j))){
				    						rejected=true;
				    					}
				    				}
				        		
					        		
					        		*/
					        		%><a href="<c:url value='/GEResolution/${MajorPendingProjectsGridsElementsItem.getClass().getSimpleName()}/${MajorPendingProjectsGridsElementsItem.label}'/>">${MajorPendingProjectsGridsElementsItem.label}</a> -
					        		
					        		
					        		
			        			</c:forEach>
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
