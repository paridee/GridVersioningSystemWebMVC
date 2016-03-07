package it.paridelorenzo.ISSSR;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.ermes.Request;

@Controller
public class DemoController {
	final static Logger logger = LoggerFactory.getLogger(DemoController.class);

	@RequestMapping(value = "/DEMOgetLatestGrid", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody boolean getGrid(@RequestBody String jsonData)
			throws IOException {
		logger.info("Entro in Demo get latest grid "+jsonData);
		final String GRID_SERVICE_URL = "http://192.168.56.1:8080/Tesi/inboundChannel.html";
		JSONObject obj=new JSONObject(jsonData);
		String project=obj.getString("project");
		String result;
		try {
			RestTemplate restTemplate = new RestTemplate();
			//result = restTemplate.getForObject(GRID_SERVICE_URL, String.class);
//LUCA FANELLI
			ArrayList<String> arr = new ArrayList<String>();
			arr.add(project);
			arr.add("LatestGrid");
			Request request = new Request("level3Direct", arr, "http://192.168.56.101:8080", null, null);
			Request requestOut = restTemplate.postForObject(GRID_SERVICE_URL, request, Request.class);
			result = requestOut.getContent().get(0);
//LUCA FANELLI	
		} catch (RestClientException ex) {
			throw new IOException();
		} catch (IllegalArgumentException ex) {
			throw new IOException();
		}

		logger.info("Ricevuta griglia: " + result);
		
		return true;
	}

}
