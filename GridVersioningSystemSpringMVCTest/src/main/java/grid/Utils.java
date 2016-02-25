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

	//TODO remove obsolete code
	/*
	public static String generateEditor(List<GridElement> elements){
		if(elements.size()>0){
			String top	=	"";
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
												"function init() {"+
												"var firepadRef"+editingElement.getIdElement()+fieldName+" = new Firebase('fiery-torch-6050.firebaseio.com/"+editingElement.getIdElement()+fieldName+"');"+
												"var codeMirror = CodeMirror(document.getElementById('"+editingElement.getIdElement()+fieldName+"'), {"+
												"lineNumbers: true,"+
												"mode: 'javascript'"+
												"});"+
												"var firepad"+editingElement.getIdElement()+fieldName+" = Firepad.fromCodeMirror(firepadRef"+editingElement.getIdElement()+fieldName+", codeMirror, {"+
												"defaultText: '"+value.toString()+"'"+
												"});"+
												"}"+
												"init();"+
												"</script>"+
												"</div>";	
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
			return top;
		}
		return "";
	}*/

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
