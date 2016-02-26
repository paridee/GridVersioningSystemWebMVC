package grid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
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

/**
 * Utilities class with singletons methods
 * @author Paride Casulli
 * @author Lorenzo La Banca
 */
public class Utils {
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


	public static String generateEditor(List<GridElement> elements, ArrayList<Practitioner> authorsL, Practitioner currentUser){
		if(elements.size()>0){
			ArrayList<String> editorsVar		=	new ArrayList<String>();
			ArrayList<String> fieldNames		=	new ArrayList<String>();
			ArrayList<String> firepads			=	new ArrayList<String>();
			ArrayList<String> buttonsId			=	new ArrayList<String>();	
			ArrayList<String> listVar			=	new ArrayList<String>();
			ArrayList<String> listFieldNames	=	new ArrayList<String>();
			String top	=	"<h2>Authors list</h2><div id=\"status\"> </div>";
			for(int i=0;i<authorsL.size();i++){
				top=top+"<h3>"+authorsL.get(i).getName()+" approval status: <div id=\"approval"+authorsL.get(i).getId()+"\"> </div></h3><hr>";
				top=top+"<script>"+
				"var approval"+elements.get(0).getLabel()+authorsL.get(i).getId()+" = new Firebase('fiery-torch-6050.firebaseio.com/"+elements.get(0).getLabel()+"approval"+authorsL.get(i).getId()+"');"+
				"var approval"+elements.get(0).getLabel()+authorsL.get(i).getId()+"state;"+
				"approval"+elements.get(0).getLabel()+authorsL.get(i).getId()+".child(\"value\").on(\"value\", function(snapshot) {"+
					 //"alert(snapshot.val());"+  // Alerts "San Francisco"
					 "document.getElementById(\"approval"+authorsL.get(i).getId()+"\").innerHTML = snapshot.val();"+
					 "approval"+elements.get(0).getLabel()+authorsL.get(i).getId()+"state=snapshot.val();"+
					 "var allOK	=	allApproved();"+
					 "if(allOK==true){document.getElementById(\"status\").innerHTML = generateString();}"+
					 "});"+
				"</script>";
			}
			top	=	top+"<script>"+
			"function allApproved(){";
			for(int i=0;i<authorsL.size();i++){
				top=top+"if(approval"+elements.get(0).getLabel()+authorsL.get(i).getId()+"state!=\"approved\"){return false;}";
			}
			top	=	top+"return true;}</script>";
			top	=	top+
					"		<div class=\"panel panel-info\">"+
			"<div class=\"panel-heading\"><p><input type=\"button\" name=\"lockBtn\" class=\"btn btn-lg btn-primary btn-block\" value=\"Lock\" onclick=\"btnLock()\"><input type=\"button\" class=\"btn btn-lg btn-primary btn-block\" name=\"approveBtn\" value=\"Approve\" onclick=\"btnApprove()\" disabled><input type=\"button\" class=\"btn btn-lg btn-primary btn-block\" name=\"rejectBtn\" value=\"Reject\" onclick=\"btnReject()\" disabled></p>";
			for(int k=0;k<elements.size();k++){;
				GridElement editingElement	=	elements.get(k);
				top					=	top+"<div id=\""+editingElement.getLabel()+"\"><b>"+editingElement.getClass().getSimpleName()+": "+editingElement.getLabel()+" v"+editingElement.getVersion()+"</b></div></div></br>";
				Field[] fields				=	editingElement.getClass().getDeclaredFields();
				for(int i=0;i<fields.length;i++){
					fields[i].setAccessible(true);
					String fieldName	=	fields[i].getName();
					try {
						Object value		=	fields[i].get(editingElement);
						if(k==0&&(!((value instanceof GridElement)||(value instanceof List)||(fieldName.equals("strategyType"))))){
							top				=	top+"</br>"+  
												"<h3>"+fieldName+"</h3>"+
												"<div id=\""+editingElement.getIdElement()+fieldName+"\" class=\"firepad-container\">"+
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
												"defaultText: '"+value.toString()+"'"+
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
							top				=	top+"</br>"+  
												"<h3>"+fieldName+"</h3></br><p>"+((GridElement)value).getLabel()+"</p>";
						}
						else if(value instanceof List){						
							List aList	=	(List)value;
							listFieldNames.add(fieldName);
							listVar.add(fieldName+"List");
							top				=	top+"</br>"+  
									"<h3>"+fieldName+"</h3></br>";
							top	=	top+"";
							for(int j=0;j<aList.size();j++){
								if(aList.get(j) instanceof GridElement){
									ArrayList<String> labels	=	new ArrayList<String>();
									for(Object o:aList){
										labels.add(((GridElement)o).getLabel());
									}
									//top	=	top+((GridElement)aList.get(j)).getLabel();
									top	=	top+generateListViewer(fieldName,labels,labels,elements.get(k),buttonsId);
								}
								else{
									top = top+aList.get(j).toString();
								}
							}
							top	=	top+"";
						}
						else{
							top				=	top+  
											"<h3>"+fieldName+"</h3></br><p>"+value.toString()+"</p>";
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				top=top+"</div>";
				top	=	top+"<hr>";
			}
			top	=	top+"<script>"+
					"var lock"+elements.get(0).getLabel()+" = new Firebase('fiery-torch-6050.firebaseio.com/"+elements.get(0).getLabel()+"state');"+
					"lock"+elements.get(0).getLabel()+".child(\"lock\").on(\"value\", function(snapshot) {"+
					"var result = snapshot.val();"+
					//"alert(\"cambio!!!\".concat(result));"+
					"if(snapshot.val()==\"false\"){"+
						"approval"+elements.get(0).getLabel()+currentUser.getId()+".set({value:\"not approved\"});";
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
				     "function generateString(){ var obj = \"{id~"+elements.get(0).getIdElement()+"#class~"+elements.get(0).getClass().getSimpleName();
						for(int e=0;e<editorsVar.size();e++){
							top	=	top+"#"+fieldNames.get(e)+"~\"+"+firepads.get(e)+".getText()+\"";
						}
						for(int e=0;e<listVar.size();e++){
							top	=	top+"#"+listFieldNames.get(e)+"~\"+"+listVar.get(e)+".join()+\"";
						}
				      top=top+"}\"; return obj;"+"}"+
					 "</script>";
			return top;
		}
		return "";
	}

	public static String generateListViewer(String name,List<String> merged,List<String> actual,GridElement element, ArrayList<String> buttonsId){
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
		String str	=	"<div class=\"panel-heading\"><div class=\"dropdown\" style=\"display: inline-block;\">"
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
						"<div id=\""+name+"ListDiv\"></div></div>"
						+ "<script>var "+name+"List = [];"+
						"var list"+element.getLabel()+name+" = new Firebase('fiery-torch-6050.firebaseio.com/"+element.getLabel()+name+element.getIdElement()+"list');"+
						"function drawMGList"+name+"(){"+
						"var txt=\"\";"+
						"for (var i = 0; i < "+name+"List.length; i++) {"+
							"txt=txt+\"<span class=\'label label-success\'  style=\'margin-left: 5px;\'>\"+"+name+"List[i]+\"</span>\";"+
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
		    			"var index = "+name+"List.indexOf(label);"+
		    			"if (index == -1) {"+
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
		String name=ge.getClass().getSimpleName()+" "+ge.getLabel()+" - <i>v"+ge.getVersion()+"</i><br>";
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
	
	public static int getPendingNumber(List<Integer> projectIds){
		List<Integer> list = new ArrayList<>();
		for(int i:list){
			System.out.println(i);
		}
		return 1;
	}
	
	
}
