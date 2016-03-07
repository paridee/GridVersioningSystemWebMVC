package it.paridelorenzo.ISSSR;

import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import grid.JSONFactory;
import grid.JSONFactory.JSONType;
import grid.entities.Grid;
import grid.entities.GridElement;
import grid.entities.Project;
import grid.interfaces.services.GridElementService;
import grid.interfaces.services.GridService;
import grid.interfaces.services.ProjectService;
import it.ermes.Level3Request;
import it.ermes.Persistence;
 
 
@Controller
public class ErmesController {
	final static Logger logger = LoggerFactory.getLogger(ErmesController.class);

	private GridService			gridService;
	private GridElementService			gridElementService;
	private ProjectService			projectService;
	private JSONFactory jsonFactory;
	
	@Autowired(required=true)
	@Qualifier(value="jsonFactory")
	public void setJSONFactory(JSONFactory jsonFactory) {
		this.jsonFactory = jsonFactory;
	}
	@Autowired(required=true)
	@Qualifier(value="gridService")
	public void setGridService(GridService gridService) {
		this.gridService = gridService;
	}
	@Autowired(required=true)
	@Qualifier(value="projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	@RequestMapping(headers = { "content-type=application/json" }, method = RequestMethod.POST, value = "/level3Direct", produces = "application/json")
	@ResponseBody
	public Level3Request testLevel1Direct(@RequestBody Level3Request request)
			throws IOException {

		logger.info("\nDATA from LEVEL 3 direct, requested object: "
				+ request.getObject() + " from project " + request.getProject());
		Persistence persistence = new Persistence();
		String tmp = "";
		if (request.getObject().equals("Project-info")) {  //TODO Deve andare su fase 6 (ora è simulata)
			logger.info("PROJECT INFO");
			tmp = persistence.obtainProject();
		} else if (request.getObject().equals("LatestGrid")) {
			Project currentPrj=this.projectService.getProjectByProjectId(request.getProject());
			if(currentPrj!=null){
				int projid=currentPrj.getId();
				logger.info("projid: "+projid);
				Grid latest=this.gridService.getLatestWorkingGrid(projid);
				tmp = this.jsonFactory.obtainJson(latest,JSONType.FIRST , null).toString();
				logger.info(tmp);
			}
			else{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("type", "error");
				jsonObject.put("msg", "cannot find the requested project");
				tmp =jsonObject.toString();
			}
		}
		else if (request.getObject().equals("ProjectList")) {	//array con id delle grid working
			tmp = this.projectService.getJsonProjectList();
			logger.info(tmp);
		}
		else if (request.getObject().equals("GridHistory")) {	//array con id delle grid working
			Project currentPrj=this.projectService.getProjectByProjectId(request.getProject());
			if(currentPrj!=null){
				tmp = this.gridService.getJsonWorkingGridLog(currentPrj);
				logger.info(tmp);
			}
			else{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("type", "error");
				jsonObject.put("msg", "cannot find the requested project");
				tmp =jsonObject.toString();
			}
		}
		else if (request.getObject().equals("Grid")) {	
			int gid=Integer.parseInt(request.getData());
			tmp =this.jsonFactory.obtainJson(this.gridService.getGridById(gid),JSONType.FIRST , null,true).toString();
			logger.info(tmp);
		}
		else if (request.getObject().equals("GridElement")) {	
			String command=request.getData();
			String[] items=command.split(",");
			int id=Integer.parseInt(items[0]);
			String type=items[1];
			GridElement current=this.gridElementService.getElementById(id, type);
			tmp =this.jsonFactory.obtainJson(current, JSONType.FIRST).toString();
			logger.info(tmp);
		}
		else if (request.getObject().equals("GridElementHistory")) {	
			String command=request.getData();
			String[] items=command.split(",");
			String label=items[0];
			String type=items[1];
			tmp=this.gridElementService.getJsonWorkingLogList(label, type);
			logger.info(tmp);
		}
		else{
			logger.info(request.getObject());
		}
		
		
		
		Level3Request out = new Level3Request(request.getTag(),
				request.getProject(), request.getObject(),
				request.getDestinationAdress(), tmp, request.getId(),
				request.getVersion());
		return out;
	}
     
}
