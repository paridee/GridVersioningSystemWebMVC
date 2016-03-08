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

	@RequestMapping(value = "/DEMORequest", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody String getGrid(@RequestBody String jsonData)
			throws IOException {
		logger.info("Entro in Demo get latest grid "+jsonData);
		final String GRID_SERVICE_URL = "http://192.168.56.1:8080/Tesi/inboundChannel.html";
		JSONObject obj=new JSONObject(jsonData);
		String project=obj.getString("project");
		String ermesRequest=obj.getString("request");
		String result;
		logger.info(project);
		try {
			RestTemplate restTemplate = new RestTemplate();
			ArrayList<String> arr = new ArrayList<String>();
			arr.add(project);
			arr.add(ermesRequest);
			//TODO add parameters to data
			Request request = new Request("level3Direct", arr, "http://192.168.56.101:8080", null, null);
			Request requestOut = restTemplate.postForObject(GRID_SERVICE_URL, request, Request.class);
			result = requestOut.getContent().get(0);
		} catch (RestClientException ex) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("type", "error");
			jsonObject.put("msg", "Rest Client Exception");
			logger.info("another test");
			return jsonObject.toString();
		} catch (IllegalArgumentException ex) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("type", "error");
			jsonObject.put("msg", "Illegal Argument Exception");
			logger.info("catched exception");
			return jsonObject.toString();
		}
		logger.info("Result: " + result);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", "result");
		jsonObject.put("msg", result);
		return jsonObject.toString();
	}

}
