<%@page import="grid.modification.grid.GridModification"%>
<%@ page import="grid.modification.grid.GridModificationService"%>
<%@ page import="grid.interfaces.services.*"%>
<%@ page import =" grid.entities.*" %>
<%@ page import =" java.lang.reflect.Field" %>
<%@ page import =" java.util.List" %>
<%@ page import =" java.util.Map" %>

<%@ include file="head.jsp" %>

<%
	GridModificationService gms=(GridModificationService)request.getAttribute("GridModificationServiceInstance");
	Practitioner currentUser=(Practitioner)request.getAttribute("Practitioner");
	GridElementService ges=(GridElementService)request.getAttribute("GridElementServiceInstance");
	DefaultResponsibleService drs=(DefaultResponsibleService)request.getAttribute("DefaultResponsibleServiceInstance");
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
    		System.out.println("test1 "+p);
    		Practitioner pm=p.getProjectManager();
    		if(pm==null){
    			pm=drs.getResponsibleByClassName("pm").getPractitioner();
    		}
    		if(projectPendingGrids.containsKey(p.getId()+"")){
    			List<Grid> pendGrids=projectPendingGrids.get(p.getId()+"");
    			%>
    			<div class="panel panel-default">
    				<div class="panel-heading">
    					<span style="font-size: 22px;">Project Id: <%out.print(p.getProjectId()); %></span><div class="badge" style="float: right">Number of pending Grids: <%out.print(pendGrids.size()); %></div>
    				</div>
    				<div class="panel-body"><%
    			for(Grid g:pendGrids){   
    				%><div  class="panel panel-danger">
    				<div class="panel-heading">
    					<b>GridID: <a href="/ISSSR/grids/<%out.print(g.getId());%>" ><%out.print(g.getId()); %></a> </b>
    				</div>
    				<div class="panel-body">
    				<ul class="list-group">
    				<%
    				if(projectGridsMajorPendingElements.containsKey(p.getId()+"-"+g.getId())){
    					List<GridElement> geList=projectGridsMajorPendingElements.get(p.getId()+"-"+g.getId());
    					%><li class="list-group-item"><div style="float:left;">MajorUpdates</div> <div class="badge" style="float:left; margin-left:5px; margin-right: 5px;"><%out.print(geList.size()); %></div> <%
    					for(GridElement ge:geList){
    						if(currentUser!=pm){
    							out.print("<span class=\"label label-danger\" style=\"margin-left: 5px;\" data-toggle=\"tooltip\" data-placement=\"auto right\" title=\"You cannot solve this element\">"+ge.getLabel()+"</span>");
    						}
    						else if (gms.isSolvable(ge)){
    							out.print("<a style=\"text-decoration:none\" href=\"/ISSSR/GEResolution/"+p.getId()+"/"+ge.getClass().getSimpleName()+"/"+ge.getLabel()+"\">"+"<span class=\"label label-success\"  style=\"margin-left: 5px;\">"+ge.getLabel()+"</span>"+"</a> ");
					        }
    						else out.print("<span class=\"label label-warning\" style=\"margin-left: 5px;\" data-toggle=\"tooltip\" data-placement=\"auto right\" title=\"You have to solve other pending Grid Elements before\">"+ge.getLabel()+"</span>");
    					}
    					out.print("</li>");
    				}
    				if(projectGridsMajorConflictElements.containsKey(p.getId()+"-"+g.getId())){
    					List<GridElement> geList=projectGridsMajorConflictElements.get(p.getId()+"-"+g.getId());
    					%><li class="list-group-item"><div style="float:left;">MajorConflicts</div> <div class="badge" style="float:left; margin-left:5px; margin-right: 5px;"><%out.print(geList.size()); %></div> <%
    					for(GridElement ge:geList){
    						if(currentUser!=pm){
    							out.print("<span class=\"label label-danger\" style=\"margin-left: 5px;\" data-toggle=\"tooltip\" data-placement=\"auto right\" title=\"You cannot solve this element\">"+ge.getLabel()+"</span>");
    						}
    						else if(gms.isSolvable(ge)){
    							out.print("<a style=\"text-decoration:none\" href=\"/ISSSR/GEResolution/"+p.getId()+"/"+ge.getClass().getSimpleName()+"/"+ge.getLabel()+"\">"+"<span class=\"label label-success\" style=\"margin-left: 5px;\">"+ge.getLabel()+"</span>"+"</a> ");
					        }
    						else out.print("<span class=\"label label-warning\" style=\"margin-left: 5px;\" data-toggle=\"tooltip\" data-placement=\"auto right\" title=\"You have to solve other pending Grid Elements before\">"+ge.getLabel()+"</span>");
    					}
    					out.print("</li>");
    				}
    				if(projectGridsMinorConflictElements.containsKey(p.getId()+"-"+g.getId())){
    					List<GridElement> geList=projectGridsMinorConflictElements.get(p.getId()+"-"+g.getId());
    					%><li class="list-group-item"><div style="float:left;">MinorConflicts</div> <div class="badge" style="float:left; margin-left:5px; margin-right: 5px;"><%out.print(geList.size()); %></div> <%
    					for(GridElement ge:geList){
    						List<Practitioner> practList=new ArrayList<Practitioner>();
    						
    						if(ge.getAuthors().size()>0) practList.addAll(ge.getAuthors());
    						List<GridElement> tempList=ges.getElementByLabelAndState(ge.getLabel(), ge.getClass().getSimpleName(), GridElement.State.MINOR_CONFLICTING);
    						tempList.add(ges.getLatestWorking(ge.getLabel(), ge.getClass().getSimpleName()));
    						for(GridElement currentGE: tempList){
    							practList.addAll(currentGE.getAuthors());
    						}
    						
    						if(practList.size()==0){
    							DefaultResponsible tempdef=drs.getResponsibleByClassName(ge.getClass().getSimpleName());
    							Practitioner tempPr=tempdef.getPractitioner();
    							practList.add(tempPr);
    						}
    						if(!practList.contains(currentUser)){
    							out.print("<span class=\"label label-danger\" style=\"margin-left: 5px;\" data-toggle=\"tooltip\" data-placement=\"auto right\" title=\"You cannot solve this element\">"+ge.getLabel()+"</span>");
    						}
    						else if(gms.isSolvable(ge)){
    							out.print("<a style=\"text-decoration:none\" href=\"/ISSSR/confEditor/"+ge.getClass().getSimpleName()+"/"+ge.getLabel()+"/"+p.getId()+"\">"+"<span class=\"label label-success\" style=\"margin-left: 5px;\">"+ge.getLabel()+"</span>"+"</a> ");
    						}
    						else out.print("<span class=\"label label-warning\" style=\"margin-left: 5px;\" data-toggle=\"tooltip\" data-placement=\"auto right\" title=\"You have to solve other pending Grid Elements before\">"+ge.getLabel()+"</span>");
    					}
    					out.print("</li>");
    				}
    				if(projectGridsMainGoalListChanged.containsKey(p.getId()+"-"+g.getId())){
    					//get grid main goals
    					//check if main goals are linked to pending elements
    					List<Goal> mainGoal=g.getMainGoals();
    					boolean solvable=true;
    					for(Goal ge: mainGoal){
    						List<GridElement> pending=ges.getElementByLabelAndState(ge.getLabel(), "Goal", GridElement.State.MAJOR_CONFLICTING);
    						pending.addAll(ges.getElementByLabelAndState(ge.getLabel(), "Goal", GridElement.State.MAJOR_UPDATING));
    						pending.addAll(ges.getElementByLabelAndState(ge.getLabel(), "Goal", GridElement.State.MINOR_CONFLICTING));
    						if(pending.size()>0){
    							solvable=false;
    						}
    						
    						if(solvable){
    							if(gms.isEmbeddedPending(ge)) solvable=false;
    						}
    					}
    					out.print("<li class=\"list-group-item\">");
    					if(solvable) out.print("<a style=\"text-decoration:none\" href=\"/ISSSR/MGResolution/"+p.getId()+"/"+g.getId()+"\"><span class=\"label label-success\">Main Goal List changed</span></a><br>");
    					else out.print("<span class=\"label label-danger\" data-toggle=\"tooltip\" data-placement=\"auto right\" title=\"You have to solve other pending Grid Elements before\">MainGoalList changed</span><br>");
    					out.print("</li>");
    				}
    				
    				
    				%></ul></div>
    				</div>
    				<%
    				
    			}
    			
    			%></div></div> <!-- Fine project panel --><%
    		}
    	}
    }
    else{
    	%><div style="float: left;width: 100%; text-align: center;">
    		<img style="max-height:100px; max-width: 100px;" alt="Alert" src="<c:url value='/resources/images/warning.png' />">
    			<h1>No projects with pending updates</h1>
  		  </div><%
  		}%>
	
	
	
	
	
	
	
	
	
    
	</div><!-- /.container -->
    
 </body>
	<%@ include file="footer.jsp" %>
<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
});
</script>
   	
</html>
