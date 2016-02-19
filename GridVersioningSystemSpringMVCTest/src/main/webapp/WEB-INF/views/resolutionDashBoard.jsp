<%@page import="grid.modification.grid.GridModification"%>
<%@ page import="grid.modification.grid.GridModificationService"%>
<%@ page import="grid.interfaces.services.GridElementService"%>
<%@ page import =" grid.entities.*" %>
<%@ page import =" java.lang.reflect.Field" %>
<%@ page import =" java.util.List" %>
<%@ page import =" java.util.Map" %>

<%@ include file="head.jsp" %>

<%
	GridModificationService gms=(GridModificationService)request.getAttribute("GridModificationServiceInstance");
	GridElementService ges=(GridElementService)request.getAttribute("GridElementServiceInstance");
	List<Project> projPending=(List<Project>)request.getAttribute("PendingProjects");
	Map <String, List<Grid>> projectPendingGrids=(Map <String, List<Grid>>)request.getAttribute("PendingProjectsGrids");	//map projid, list pending grids
	Map <String, List<GridElement>> projectGridsMajorPendingElements=(Map <String, List<GridElement>>)request.getAttribute("MajorPendingProjectsGridsElements");
	Map <String, List<GridElement>> projectGridsMajorConflictElements=(Map <String, List<GridElement>>)request.getAttribute("MajorConflictProjectsGridsElements");
	Map <String, List<GridElement>> projectGridsMinorConflictElements=(Map <String, List<GridElement>>)request.getAttribute("MinorConflictProjectsGridsElements");
	Map <String, String> projectGridsMainGoalListChanged=(Map <String, String>)request.getAttribute("GridsMainGoalListChanged");
	
	
	
%>

  <body>
	<div class="container">
    <%@ include file="navBar.jsp" %>
    
    <%if(projPending.size()!=0){
    	for(Project p: projPending){
    		if(projectPendingGrids.containsKey(p.getId()+"")){
    			List<Grid> pendGrids=projectPendingGrids.get(p.getId()+"");
    			%>
    			<span style="font-size: 22px;">Project Id: <%out.print(p.getProjectId()); %></span>- Number of pending Grids: <%out.print(pendGrids.size()); %>
    			<br>
    			<%
    			for(Grid g:pendGrids){
    				%>
    				<b>GridID:<%out.print(g.getId()); %> </b><br>
    				<%
    				if(projectGridsMajorPendingElements.containsKey(p.getId()+"-"+g.getId())){
    					List<GridElement> geList=projectGridsMajorPendingElements.get(p.getId()+"-"+g.getId());
    					%>MajorUpdates (<%out.print(geList.size()); %>): <%
    					for(GridElement ge:geList){
    						if (gms.isSolvable(ge)){
    							out.print("<a href=\"/ISSSR/GEResolution/"+ge.getClass().getSimpleName()+"/"+ge.getLabel()+"\">"+ge.getLabel()+"</a> - ");
					        }
    						else out.print(ge.getLabel()+" - ");
    					}
    					out.print("<br>");
    				}
    				if(projectGridsMajorConflictElements.containsKey(p.getId()+"-"+g.getId())){
    					List<GridElement> geList=projectGridsMajorConflictElements.get(p.getId()+"-"+g.getId());
    					%>MajorConflicts (<%out.print(geList.size()); %>): <%
    					for(GridElement ge:geList){
    						if (gms.isSolvable(ge)){
    							out.print("<a href=\"/ISSSR/GEResolution/"+ge.getClass().getSimpleName()+"/"+ge.getLabel()+"\">"+ge.getLabel()+"</a> - ");
					        }
    						else out.print(ge.getLabel()+" - ");
    					}
    					out.print("<br>");
    				}
    				if(projectGridsMinorConflictElements.containsKey(p.getId()+"-"+g.getId())){
    					List<GridElement> geList=projectGridsMinorConflictElements.get(p.getId()+"-"+g.getId());
    					%>MinorConflicts (<%out.print(geList.size()); %>): <%
    					for(GridElement ge:geList){
    						if (gms.isSolvable(ge)){
    							out.print("<a href=\"/ISSSR/GEResolution/"+ge.getClass().getSimpleName()+"/"+ge.getLabel()+"\">"+ge.getLabel()+"</a> - ");
					        }
    						else out.print(ge.getLabel()+" - ");
    					}
    					out.print("<br>");
    				}
    				if(projectGridsMainGoalListChanged.containsKey(p.getId()+"-"+g.getId())){
    					//get grid main goals
    					//check if main goals are linked to pending elements
    					List<Goal> mainGoal=g.getMainGoals();
    					boolean solvable=true;
    					for(Goal goal: mainGoal){
    						if(solvable){
    							if(gms.isEmbeddedPending(goal)) solvable=false;
    						}
    					}
    					if(solvable) out.print("<a href=\"/ISSSR/MGResolution/"+g.getId()+"\">MainGoalList changed</a><br>");
    					else out.print("MainGoalList changed<br>");
    				}
    				
    				
    				
    				
    			}
    		}
    	}
    }
    else{
    	out.print("projPending size =0");
    	
    }
    
    
    %>
	
	
	
	
	
	
	
	
	
    
	</div><!-- /.container -->
    
 </body>
	<%@ include file="footer.jsp" %>

   	
</html>
