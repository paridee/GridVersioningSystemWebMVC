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
			ArrayList<String> editorsVar	=	new ArrayList<String>();
			ArrayList<String> fieldNames	=	new ArrayList<String>();
			ArrayList<String> firepads		=	new ArrayList<String>();
			String top	=	"<h2>Authors list</h2><div id=\"status\"> </div>";
			for(int i=0;i<authorsL.size();i++){
				top=top+"<p>"+authorsL.get(i).getName()+"</p><div id=\"approval"+authorsL.get(i).getId()+"\"> </div>";
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
					"<input type=\"button\" name=\"lockBtn\" value=\"Lock\" onclick=\"btnLock()\"><input type=\"button\" name=\"approveBtn\" value=\"Approve\" onclick=\"btnApprove()\" disabled><input type=\"button\" name=\"rejectBtn\" value=\"Reject\" onclick=\"btnReject()\" disabled>";
			for(int k=0;k<elements.size();k++){;
				GridElement editingElement	=	elements.get(k);
				top					=	top+"<div id=\""+editingElement.getLabel()+"\"><h2>"+editingElement.getClass().getSimpleName()+": "+editingElement.getLabel()+" v"+editingElement.getVersion()+"</h2></br>";
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
												"function init() {"+
												"var firepadRef"+editingElement.getIdElement()+fieldName+" = new Firebase('fiery-torch-6050.firebaseio.com/"+editingElement.getIdElement()+fieldName+"');"+
												"codeMirror"+editingElement.getLabel()+fieldName+editingElement.getVersion()+" = CodeMirror(document.getElementById('"+editingElement.getIdElement()+fieldName+"'), {"+
												"lineNumbers: true,"+
												"mode: 'javascript'"+
												"});"+
												//"codeMirror"+editingElement.getLabel()+fieldName+editingElement.getVersion()+""+".setOption(\"readOnly\",true);"+
												"var firepad"+editingElement.getIdElement()+fieldName+" = Firepad.fromCodeMirror(firepadRef"+editingElement.getIdElement()+fieldName+", codeMirror"+editingElement.getLabel()+fieldName+editingElement.getVersion()+", {"+
												"defaultText: '"+value.toString()+"'"+
												"});"+
												"}"+
												"init();"+
												"</script>"+
												"</div>";
							editorsVar.add("codeMirror"+editingElement.getLabel()+fieldName+editingElement.getVersion());
							firepads.add("firepadRef"+editingElement.getIdElement()+fieldName);
							fieldNames.add(fieldName);
						}
						else if(value instanceof GridElement){
							top				=	top+"</br>"+  
												"<h3>"+fieldName+"</h3></br><p>"+((GridElement)value).getLabel()+"</p>";
						}
						else if(value instanceof List){
							List aList	=	(List)value;
							top				=	top+"</br>"+  
									"<h3>"+fieldName+"</h3></br>";
							top	=	top+"<p>";
							for(int j=0;j<aList.size();j++){
								if(aList.get(j) instanceof GridElement){
									top	=	top+((GridElement)aList.get(j)).getLabel();
								}
								else{
									top = top+aList.get(j).toString();
								}
							}
							top	=	top+"</p>";
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
				    	top=top+"document.getElementsByName(\"lockBtn\")[0].disabled=false;"+
				    	"document.getElementsByName(\"approveBtn\")[0].disabled=true;"+
				    	"document.getElementsByName(\"rejectBtn\")[0].disabled=true;"+
				     "}"+
					"else{";
						for(int e=0;e<editorsVar.size();e++){
							top	=	top+editorsVar.get(e)+".setOption(\"readOnly\",true);";
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
				     "function generateString(){ var obj = \"{id~"+elements.get(0).getIdElement()+"#class~"+elements.get(0).getClass().getSimpleName()+"}\"; return obj;";
				     top=top+";}"+
					 "</script>";
			return top;
		}
		return "";
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
	public static String gridElementToHTMLString(GridElement ge){
		String name=ge.getClass().getSimpleName()+" "+ge.getLabel()+" - <i>v"+ge.getVersion()+"</i><br>";
		String desc="";
		Field[] fields=ge.getClass().getDeclaredFields();
		for(int j=0; j<fields.length;j++){
			Field tempField=fields[j];
			tempField.setAccessible(true);
			try {
				Object fieldValue=tempField.get(ge);
				if(fieldValue instanceof GridElement){
					GridElement fieldValueGE=(GridElement)fieldValue;
					desc=desc+tempField.getName()+":  "+fieldValueGE.getLabel()+"_v"+fieldValueGE.getVersion()+"<br>";
				}
				else if(fieldValue instanceof List){
					@SuppressWarnings("rawtypes")
					List myList 	=	(List)fieldValue;
					if(myList.size()>0){
						desc=desc+tempField.getName()+": ";
						for(int i=0; i<myList.size();i++){
							Object	current	=	 myList.get(i);
							if(current instanceof GridElement){
								GridElement fieldValueGE=(GridElement)current;
								desc=desc+" "+fieldValueGE.getLabel()+"_v"+fieldValueGE.getVersion()+"<br>";
							}
						}
					}
				}
				else{
					//desc=desc+"<div style='float:left;min-width: 200px;'>"+tempField.getName()+": "+fieldValueStr+"</div>";
					if(fieldValue!=null){
						if(!tempField.getName().equals("logger")){
							String fieldValueStr	=	(String)fieldValue.toString();
							String txt=tempField.getName()+": "+fieldValueStr;
							int maxLength=60;
							if(txt.length()>maxLength) txt=txt.substring(0, maxLength)+"...";
							desc=desc+txt+"<br>";
						}
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
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
