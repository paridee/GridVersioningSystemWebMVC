package grid;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import grid.entities.GridElement;
import grid.entities.Practitioner;
import grid.interfaces.services.GridElementService;
import grid.modification.elements.Modification;

/**
 * Utilities class with singletons methods
 * @author Paride Casulli
 * @author Lorenzo La Banca
 */
public class Utils {
	
	public static class PostSender implements Runnable{
		String url	=	"";
		String body		=	"";
		public PostSender(String url, String body) {
			this.url	=	url;
			this.body	=	body;
		}
		@Override
		public void run() {
			try {
				System.out.println("GridModificationService.java going to send Grid step 1");
				URL obj;
				obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				//add reuqest header
				con.setRequestMethod("POST");
				con.setRequestProperty("User-Agent", "Mozilla/5.0");
				con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				System.out.println("GridModificationService.java going to send Grid step 2");
	//Send post request
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(body);
				wr.flush();
				wr.close();
				System.out.println("GridModificationService.javaSending 'POST' request to URL : " + url);
				System.out.println("GridModificationService.java Post parameters : " + body);
				int responseCode = con.getResponseCode();
				System.out.println("GridModificationService.java Response Code : " + responseCode);
	
				BufferedReader in = new BufferedReader(
				        new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
	
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				//print result
				logger.info(response.toString());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Inner Class implementing a thread for email sending
	 * @author Paride Casulli
	 * @author Lorenzo La Banca	
	 */
	public static class MailSender implements Runnable{
		String title	=	"";
		String body		=	"";
		String toAddr	=	"";
		public MailSender(String title, String body, String toAddr) {
			this.title	=	title;
			this.body	=	body;
			this.toAddr	=	toAddr;
		}

		@Override
		public void run() {
			Properties props = new Properties();
			props.put("mail.smtp.host", SMTPHOST);
			props.put("mail.smtp.socketFactory.port", SMTPPORT);
			props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", SMTPPORT); 
			Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(SMTPUSER,SMTPPASS);
				}
			});
			try {
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress("gqmplusstrategy@versioning.com"));
				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toAddr));
				message.setSubject(title);
				message.setText(body,"utf-8","html");
				logger.info("email sent");
				Transport.send(message);
			} catch (MessagingException e) {
				e.printStackTrace();
			}			
		}

	}

	private static final Logger logger		=	LoggerFactory.getLogger(Utils.class);
	private static final String SMTPHOST	=	"smtps.aruba.it";
	private static final String SMTPPORT	=	"465";
	protected static final String SMTPUSER	=	"test@agriturismodegliacquedotti.it";
	protected static final String SMTPPASS	=	"fujifilm";
	public static String systemURL			=	"http://localhost:8080/ISSSR";


	/**
	 * Given 2 arraylists adds the non-duplicate elements in the first one
	 * @param one first list (will be also the result of merge operation)
	 * @param two second list to be merged
	 */
	@SuppressWarnings("unchecked")
	public static void mergeLists(@SuppressWarnings("rawtypes") ArrayList one, @SuppressWarnings("rawtypes") ArrayList two){
		for(int i=0;i<two.size();i++){
			if(!one.contains(two.get(i))){
				one.add(two.get(i));
			}
		}
	}

	/**
	 * Get a list of field name list
	 * @param c class to be checked
	 * @return field list
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> getFieldNamesForAClass(Class c){
		ArrayList<String>	fieldsL	=	new ArrayList<String>();
		Field[] fields	=	c.getDeclaredFields();
		for(int i=0;i<fields.length;i++){
			String fieldName	=	fields[i].getName();
			if(!fieldsL.contains(fieldName)){
				fieldsL.add(fieldName);
			}
		}
		return fieldsL;
	}
	
	
	/**
	 * Converts hashmap of <String,GridElement> to an hashmap <String,Object>
	 * @param map input map
	 * @return output map
	 */
	public static HashMap<String,Object> convertHashMap(HashMap<String,GridElement> map){
		HashMap<String,Object> retMap			=	new HashMap<String,Object>();
		java.util.Iterator<String> anIterator	=	map.keySet().iterator();
		while(anIterator.hasNext()){
			String key	=	anIterator.next();
			retMap.put(key, map.get(key));
		}
		return retMap;
	}
	/**
	 * Sends an email
	 * @param title title of the mail
	 * @param body body of the mail
	 * @param toAddr destination addresses, divided by commas without spaces
	 */
	public static void mailSender(String title,String body,String toAddr){
		MailSender	sender 	=	new MailSender(title,body,toAddr);
		Thread mailSend		=	new Thread(sender);
		mailSend.start();
	}

	/**
	 * Generates an editor (with firepad) for conflict resolution
	 * @param collElements colliding Grid Elements
	 * @param authorsL list of the authors involved in the resolution
	 * @param currentUser current user of the system
	 * @return HTML/javascript with an editor within
	 */
	@SuppressWarnings("rawtypes")
	public static String generateEditor(List<GridElement> collElements, ArrayList<Practitioner> authorsL, Practitioner currentUser){
		//ArrayList<GridElement> elements	=	new ArrayList<GridElement>();	//ADDED
		//elements.add(collElements.get(0));
		List<GridElement> elements	=	collElements;//ADDED
		if(elements.size()>0){
			ArrayList<String> editorsVar		=	new ArrayList<String>();
			ArrayList<String> fieldNames		=	new ArrayList<String>();
			ArrayList<String> firepads			=	new ArrayList<String>();
			ArrayList<String> buttonsId			=	new ArrayList<String>();	
			ArrayList<String> listVar			=	new ArrayList<String>();
			ArrayList<String> listFieldNames	=	new ArrayList<String>();
			String top	=	"<h2>Authors list</h2>\n<div style=\"float: left; width:100%; margin-bottom:20px;\">\n";
			for(int i=0;i<authorsL.size();i++){
				top=top+"<div style=\"-webkit-border-radius: 3px;-moz-border-radius: 3px;border-radius: 3px;padding: 3px; border: 1px solid grey; float: left;text-align:center;\">"+authorsL.get(i).getName()+"<br><div id=\"approval"+authorsL.get(i).getId()+"\"> </div></div>\n";
				top=top+"<script>\n"+
				"var approval"+elements.get(0).getLabel()+authorsL.get(i).getId()+" = new Firebase('fiery-torch-6050.firebaseio.com/"+elements.get(0).getLabel()+"approval"+authorsL.get(i).getId()+"');"+
				"var approval"+elements.get(0).getLabel()+authorsL.get(i).getId()+"state;"+
				"approval"+elements.get(0).getLabel()+authorsL.get(i).getId()+".child(\"value\").on(\"value\", function(snapshot) {"+
					 //"alert(snapshot.val());"+  // Alerts "San Francisco"
					 "if(snapshot.val()==\"approved\"){document.getElementById(\"approval"+authorsL.get(i).getId()+"\").innerHTML = \"<span class='label label-success'>\"+snapshot.val()+\"</span>\";}"+
					 "else if(snapshot.val()==\"not approved\"){document.getElementById(\"approval"+authorsL.get(i).getId()+"\").innerHTML = \"<span class='label label-danger'>\"+snapshot.val()+\"</span>\";}"+
					 "approval"+elements.get(0).getLabel()+authorsL.get(i).getId()+"state=snapshot.val();"+
					 "var allOK	=	allApproved();"+
					 "if(allOK==true){"+
					 //"document.getElementById(\"status\").innerHTML = generateString();"+
					 "var postPayLoad = generateString();"+
					 "$.post(\"../../../getConflictResolution\", generateString(), function(response){location.href = '../../../resolutionDashBoard';});"+
					 "console.log(postPayLoad);"+
					  "}"+
					 "});"+
				"</script>\n";
			}
			top	=	top+"</div><script>"+
			"function allApproved(){";
			for(int i=0;i<authorsL.size();i++){
				top=top+"if(approval"+elements.get(0).getLabel()+authorsL.get(i).getId()+"state!=\"approved\"){return false;}";
			}
			top	=	top+"return true;}";
			top	=	top+"function allRej(){";
			for(int i=0;i<authorsL.size();i++){
				top=top+"approval"+elements.get(0).getLabel()+authorsL.get(i).getId()+".set({value:\"not approved\"});";
			}
			top	=	top+"}</script>";
			top	=	top+
					"		\n<div class=\"panel panel-info\" style=\"float:left; width:49%;\">"+
			"<div class=\"panel-heading\"><p><input type=\"button\" name=\"lockBtn\" class=\"btn btn-lg btn-primary btn-block\" value=\"Lock\" onclick=\"btnLock()\"><input type=\"button\" class=\"btn btn-lg btn-primary btn-block\" name=\"approveBtn\" value=\"Approve\" onclick=\"btnApprove()\" disabled><input type=\"button\" class=\"btn btn-lg btn-primary btn-block\" name=\"rejectBtn\" value=\"Reject\" onclick=\"btnReject()\" disabled></p>";
			if(!(Modification.minorUpdateClass.contains(elements.get(0).getClass()))){
				//top	=	top	+	"<script>document.getElementsByName(\"rejectBtn\")[0].style.visibility = \"hidden\";</script>";
			}
			for(int k=0;k<elements.size();k++){
				if(k>0){
					top=	top	+ "\n<div class=\"panel panel-danger\"  style=\"float:right; width:49%;\"><div class=\"panel-heading\">\n";
				}
				GridElement editingElement	=	elements.get(k);
				top					=	top+"<div id=\""+editingElement.getLabel()+"\"><b>"+editingElement.getClass().getSimpleName()+": "+editingElement.getLabel()+" v"+editingElement.getVersion()+"</b></div></div><div class=\"panel-body\">";
				Field[] fields				=	editingElement.getClass().getDeclaredFields();
				for(int i=0;i<fields.length;i++){
					fields[i].setAccessible(true);
					String fieldName	=	fields[i].getName();
					try {
						Object value		=	fields[i].get(editingElement);
						if(k==0&&(!fieldName.equals("logger"))&&(!((value instanceof GridElement)||(value instanceof List)||(fieldName.equals("strategyType"))))){
							String temp="";
							if(value!=null) temp=value.toString();
							top				=	top+  
												"<div style=\"padding-left: 3px;float:left; width:100%; font-weight: bolder; font-size: 18px;\">"+fieldName+"</div>"+
												"<div style=\"float:left; width:100%;\" id=\""+editingElement.getIdElement()+fieldName+"\" class=\"firepad-container\">"+
												"<script>"+
												"var codeMirror"+editingElement.getLabel()+fieldName+editingElement.getVersion()+";"+
												"var firepadRef"+editingElement.getIdElement()+fieldName+";"+
												"var firepad"+editingElement.getIdElement()+fieldName+";"+
												"function init() {"+
												"firepadRef"+editingElement.getIdElement()+fieldName+" = new Firebase('fiery-torch-6050.firebaseio.com/"+editingElement.getIdElement()+fieldName+"');"+
												"codeMirror"+editingElement.getLabel()+fieldName+editingElement.getVersion()+" = CodeMirror(document.getElementById('"+editingElement.getIdElement()+fieldName+"'), {"+
												"lineNumbers: true,"+
												"mode: 'javascript'"+
												"});"+
												//"codeMirror"+editingElement.getLabel()+fieldName+editingElement.getVersion()+""+".setOption(\"readOnly\",true);"+
												"firepad"+editingElement.getIdElement()+fieldName+" = Firepad.fromCodeMirror(firepadRef"+editingElement.getIdElement()+fieldName+", codeMirror"+editingElement.getLabel()+fieldName+editingElement.getVersion()+", {"+
												"defaultText: '"+temp+"'"+
												"});"+
												"}"+
												"init();"+
												"</script>"+
												"</div>";
							editorsVar.add("codeMirror"+editingElement.getLabel()+fieldName+editingElement.getVersion());
							firepads.add("firepad"+editingElement.getIdElement()+fieldName);
							fieldNames.add(fieldName);
						}
						else if(value instanceof GridElement){
							top				=	top+  
												"<div style=\"padding-left: 3px;float:left; width:100%; font-weight: bolder; font-size: 18px;\">"+fieldName+"</div>";
							if(k>0){
								top	=	top +"<br><p>"+((GridElement)value).getLabel()+"</p><br>";
							}
							else{
								listFieldNames.add(fieldName);
								listVar.add(fieldName+"List");
								ArrayList<String> labels	=	new ArrayList<String>();
								ArrayList<String> allLabels	=	new ArrayList<String>();
								labels.add(((GridElement)value).getLabel());
								for(GridElement el:collElements){
									GridElement otherEl	=	(GridElement)	fields[i].get(el);
									String aLabel		=	otherEl.getLabel();
									if(!allLabels.contains(aLabel)){
											allLabels.add(aLabel);
									}
								}
							}
						}
						else if(value instanceof List){		
							//obtain list type
							Field thisField	=	fields[i];
							ParameterizedType listType = (ParameterizedType) thisField.getGenericType();
					        Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
							logger.info("Found a list, items are type: "+listClass.getClass().getSimpleName());
							
							List aList	=	(List)value;
							if(k==0){
								listFieldNames.add(fieldName);
								listVar.add(fieldName+"List");
							}
							top				=	top+  
									"<div style=\"padding-left: 3px;float:left; width:100%; font-weight: bolder; font-size: 18px;\">"+fieldName+"</div>";
							top	=	top+"";
							//if(aList.get(0) instanceof GridElement){
							logger.info("test assignment "+listClass.getSimpleName()+" "+GridElement.class.isAssignableFrom(listClass));
							if(GridElement.class.isAssignableFrom(listClass)){
								ArrayList<String> labels	=	new ArrayList<String>();
								ArrayList<String> allLabels	=	new ArrayList<String>();
								for(Object o:aList){
									labels.add(((GridElement)o).getLabel());
								}
								for(GridElement el:collElements){
									List links	=(List)	fields[i].get(el);
									for(Object obj:links){
										String aLabel	=	((GridElement)obj).getLabel();
										if(!allLabels.contains(aLabel)){
											allLabels.add(aLabel);
										}
									}
								}
								//top	=	top+((GridElement)aList.get(j)).getLabel();
								if(k==0){
									top	=	top+generateListViewer(fieldName,allLabels,labels,elements.get(k),buttonsId,false);
								}
								else{
									for(String label:labels){
										top	=	top	+"<br>"+label;
									}
								}
							}
							else{
								for(int j=0;j<aList.size();j++){
									top = top+aList.get(j).toString();
								}
							}
							top	=	top+"";
						}
						else{
							if(!fieldName.equals("logger")){
								top				=	top+  
											"<div style=\"padding-left: 3px;float:left; width:100%; font-weight: bolder; font-size: 18px;\">"+fieldName+"</div><p>"+value.toString()+"</p>";
							}
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				top=top+"</div></div>";
				top	=	top+"<hr>";
			}
			top	=	top+"<script>"+
					"var lock"+elements.get(0).getLabel()+" = new Firebase('fiery-torch-6050.firebaseio.com/"+elements.get(0).getLabel()+"state');"+
					"lock"+elements.get(0).getLabel()+".child(\"lock\").on(\"value\", function(snapshot) {"+
					"var result = snapshot.val();"+
					//"alert(\"cambio!!!\".concat(result));"+
					"if(snapshot.val()==\"false\"){"+
						"approval"+elements.get(0).getLabel()+currentUser.getId()+".set({value:\"not approved\"});"+
						"allRej();";
						for(int e=0;e<editorsVar.size();e++){
							top	=	top+editorsVar.get(e)+".setOption(\"readOnly\",false);";
						}
						for(int e=0;e<buttonsId.size();e++){
							top=top+"document.getElementById(\""+buttonsId.get(e)+"\").disabled=false;";
						}
				    	top=top+"document.getElementsByName(\"lockBtn\")[0].disabled=false;"+
				    	"document.getElementsByName(\"approveBtn\")[0].disabled=true;"+
				    	"document.getElementsByName(\"rejectBtn\")[0].disabled=true;"+
				     "}"+
					"else{";
						for(int e=0;e<editorsVar.size();e++){
							top	=	top+editorsVar.get(e)+".setOption(\"readOnly\",true);";
						}
						for(int e=0;e<buttonsId.size();e++){
							top=top+"document.getElementById(\""+buttonsId.get(e)+"\").disabled=true;";
						}
				    	top	=	top+"document.getElementsByName(\"lockBtn\")[0].disabled=true;"+
				    	"document.getElementsByName(\"approveBtn\")[0].disabled=false;"+
				    	"document.getElementsByName(\"rejectBtn\")[0].disabled=false;"+
				     "}});"+ 
					"function btnLock(){"+
				     	//"alert(\"btnlock\");"+
				     	"lock"+elements.get(0).getLabel()+".set({lock:\"true\"});"+
				     "}"+
				     "function btnApprove(){"+
				     	//"alert(\"btnapprove\");"+
				     	"approval"+elements.get(0).getLabel()+currentUser.getId()+".set({value:\"approved\"});"+
				     	//"lock"+elements.get(0).getLabel()+".set({lock:\"false\"});"+
				     "}"+
				     "function btnReject(){"+
				     	//"alert(\"btnreject\");"+
				     	"lock"+elements.get(0).getLabel()+".set({lock:\"false\"});"+
				     "}"+
				     "function generateString(){ var obj = \"label~"+elements.get(0).getLabel()+"#id~"+elements.get(0).getIdElement()+"#class~"+elements.get(0).getClass().getSimpleName();
						for(int e=0;e<editorsVar.size();e++){
							top	=	top+"#"+fieldNames.get(e)+"~\"+"+firepads.get(e)+".getText()+\"";
						}
						for(int e=0;e<listVar.size();e++){
							top	=	top+"#"+listFieldNames.get(e)+"~\"+"+listVar.get(e)+".join()+\"";
						}
				      top=top+"\"; return obj;"+"}"+
					 "</script>";
			return top;
		}
		return "";
	}

	/**
	 * Generates a viewer for a list (HTML)
	 * @param name name of the field (list)
	 * @param merged list of all grid elements label that can be added/removed
	 * @param actual actual list of label representing the state of the element
	 * @param element subject of this change
	 * @param buttonsId buttons id list (created will be added and controller can enable/disable newly created buttons)
	 * @param isField used in case of field instead of list (just an element can be added/removed)
	 * @return
	 */
	public static String generateListViewer(String name,List<String> merged,List<String> actual,GridElement element, ArrayList<String> buttonsId,boolean isField){
		System.out.println(""+merged.toString());
		String addL	=	"";
		String remL	=	"";
		for(int i=0;i<merged.size();i++){
			addL	=	addL+"<li><a href=\"#\" onclick=\"addMG"+name+"('"+merged.get(i)+"');\">"+merged.get(i)+"</a></li>";
			remL	=	remL+"<li><a href=\"#\" onclick=\"removeMG"+name+"('"+merged.get(i)+"');\">"+merged.get(i)+"</a></li>";
		}
		String act	=	"[";
		for(int i=0;i<actual.size();i++){
			act	=	act+"\""+actual.get(i)+"\"";
			if(i<actual.size()-1){
				act	=	act+",";
			}
		}
		act	=	act+"]";
		buttonsId.add("dropdown"+name+"rem");
		buttonsId.add("dropdown"+name+"add");
		String str	=	"<div style=\"float: left; width: 100%; background-color:#d9edf7;\"><div class=\"dropdown\" style=\"display: inline-block;\">"
						+"<button class=\"btn btn-default dropdown-toggle\" type=\"button\" id=\"dropdown"+name+"add\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"true\">"
						+"Add"+
						"<span class=\"caret\"></span></button>"+
						"<ul class=\"dropdown-menu\" aria-labelledby=\"dropdownMenu"+name+"add\">"+
						addL+
						"</ul>"+
						"</div>"
						+"<div class=\"dropdown\" style=\"display: inline-block;\">"
						+"<button class=\"btn btn-default dropdown-toggle\" type=\"button\" id=\"dropdown"+name+"rem\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"true\">"
						+"Remove"+
						"<span class=\"caret\"></span></button>"+
						"<ul class=\"dropdown-menu\" aria-labelledby=\"dropdownMenu"+name+"remove\">"+
						remL+
						"</ul>"+
						"</div>"+
						"<div style=\"background-color: #d9edf7; padding: 3px; float:left; width: 100%; margin-top: 5px;\" id=\""+name+"ListDiv\"></div></div>"
						+ "<script>var "+name+"List = [];"+
						"var list"+element.getLabel()+name+" = new Firebase('fiery-torch-6050.firebaseio.com/"+element.getLabel()+name+element.getIdElement()+"list');"+
						"function drawMGList"+name+"(){"+
						"var txt=\"\";"+
						"for (var i = 0; i < "+name+"List.length; i++) {"+
							"txt=txt+\"<span class=\'label label-success\'  style=\'float: left;margin: auto;margin-left: 5px;\'>\"+"+name+"List[i]+\"</span>\";"+
		    			"}"+
		    			"document.getElementById(\""+name+"ListDiv\").innerHTML	=	txt ;"+
		    			"}"+
		    			"list"+element.getLabel()+name+".child(\"value\").on(\"value\", function(snapshot) {"+
		    			"var temp	=	snapshot.val();"+
		    			//"alert(temp);"+
		    			"if(temp!=null){"+
		    			name+"List = snapshot.val().split(\",\");}"+
		    			"drawMGList"+name+"();"+
		    			"});"+
		    			"function addMG"+name+"(label){"+
		    			"var index = "+name+"List.indexOf(label);";
		if(isField==true){
			str	=	str+"if("+name+"List.length>1){return;}";
		}
		  str	=	str+  			"if (index == -1) {"+
		    				name+"List.push(label);"+
		    			"}"+
		    			"var tempstr	=	"+name+"List.join();"+
		    			//"alert(tempstr+\"INT\");"+
		    			"list"+element.getLabel()+name+".set({value:tempstr});"+
		    			"drawMGList"+name+"();}"+
		    			"function removeMG"+name+"(label){"+
		    				"var index = "+name+"List.indexOf(label);"+
		    			"if (index > -1) {"+
		    			name+"List.splice(index, 1);"+
		    			"}"+		    			
		    			"var tempstr	=	"+name+"List.join();"+
		    			//"alert(tempstr+\"INT\");"+
		    			"list"+element.getLabel()+name+".set({value:tempstr});"+
		    			"drawMGList"+name+"();}"+
		    			"drawMGList"+name+"();"+
		    			"</script>";
		return str;
	}
	
	/**
	 * load the content of a text file on a string
	 * @param path path of the file
	 * @return string with text
	 */
	public static String loadFile(String path){
		String everything		=	"";
		try(BufferedReader br 	= 	new BufferedReader(new FileReader(new File(path)))) {
			StringBuilder sb 	= 	new StringBuilder();
			String line 		= 	br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line 	= 	br.readLine();
			}
			everything 	= 	sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return everything;
	}

	/**
	 * Convert a Grid Element to an HTML visualization item
	 * @param ge a Grid Element
	 * @return html string to be embedded
	 */
	public static String gridElementToHTMLString(GridElement ge, GridElementService geservice, boolean updated){
		String name=ge.getClass().getSimpleName()+" "+ge.getLabel()+" - <i> "+Utils.dateStringFromTimestamp(ge.getTimestamp())+"</i><br>";
		String desc="";
		Field[] fields=ge.getClass().getDeclaredFields();
		for(int j=0; j<fields.length;j++){
			desc=desc+"<div style=\"width:100%; float: left;margin-bottom: 3px;\">";
			Field tempField=fields[j];
			tempField.setAccessible(true);
			try {
				Object fieldValue=tempField.get(ge);
				if(fieldValue instanceof GridElement){
					GridElement fieldValueGE=(GridElement)fieldValue;
					int id;
					if(updated){
						id=geservice.getLatestWorking(fieldValueGE.getLabel(), fieldValueGE.getClass().getSimpleName()).getIdElement();
					}
					else{
						id=fieldValueGE.getIdElement();
					}
					desc=desc+"<b>"+tempField.getName()+":</b>  <a style=\"text-decoration:none;\" href=\"/ISSSR/element/"+fieldValueGE.getClass().getSimpleName()+"/"+id+"\"><span class=\"label label-success\" style=\"margin-left: 5px;\">"+fieldValueGE.getLabel()+"</span></a>";
				}
				else if(fieldValue instanceof List){
					@SuppressWarnings("rawtypes")
					List myList 	=	(List)fieldValue;
					if(myList.size()>0){
						desc=desc+"<b>"+tempField.getName()+":</b> ";
						for(int i=0; i<myList.size();i++){
							Object	current	=	 myList.get(i);
							if(current instanceof GridElement){
								GridElement fieldValueGE=(GridElement)current;
								int id;
								if(updated){
									id=geservice.getLatestWorking(fieldValueGE.getLabel(), fieldValueGE.getClass().getSimpleName()).getIdElement();
								}
								else{
									id=fieldValueGE.getIdElement();
								}
								desc=desc+"<a style=\"text-decoration:none;\" href=\"/ISSSR/element/"+fieldValueGE.getClass().getSimpleName()+"/"+id+"\"><span class=\"label label-success\" style=\"margin-left: 5px;\">"+fieldValueGE.getLabel()+"</span></a>";
							}
						}
					}
				}
				else{
					//desc=desc+"<div style='float:left;min-width: 200px;'>"+tempField.getName()+": "+fieldValueStr+"</div>";
					if(fieldValue!=null){
						if(!tempField.getName().equals("logger")){
							String fieldValueStr	=	(String)fieldValue.toString();
							String txt="<b>"+tempField.getName()+":</b> "+fieldValueStr;
							int maxLength=60;
							if(txt.length()>maxLength) txt=txt.substring(0, maxLength)+"...";
							desc=desc+txt;
						}
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			desc=desc+"</div>";
		}
		String gridElementString="<div class=\"panel-heading\">"+name+"</div><div class=\"panel-body\">"+desc+"</div>";
		return gridElementString;
	}

	public static List<GridElement> removeDuplicates(List<GridElement> geList) {
		List<GridElement> temp=new ArrayList<GridElement>();
		for(GridElement ge:geList){
			if(!temp.contains(ge)){
				temp.add(ge);
			}
		}
		return temp;
	}
	
	/**
	 * Converts a timestamp to date format
	 * @return string representation
	 */
	public static String dateStringFromTimestamp(long timestamp){
	    Date date = new Date(timestamp);
	    Format format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    return format.format(date);
	}
	
}
