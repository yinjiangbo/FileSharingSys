package controllers;

import play.*;
import play.mvc.*;
import play.libs.F.Function;
import play.libs.F;
import play.libs.F.Promise;
import play.libs.ws.*;
import views.html.*;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import java.io.File;  
import java.io.InputStreamReader;  
import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.FileInputStream;  
import java.io.FileWriter;

import play.api.data.*;
import play.data.Form;
import play.data.DynamicForm;
import java.util.ArrayList; 
import models.User;

public class Application extends Controller {

    public static Result index() {
		WSRequestHolder wsreqHolder = WS.url("https://pv.sohu.com/cityjson?ie=utf-8");
		Promise<WSResponse> promiseOfResult = wsreqHolder.get();
		WSResponse response = promiseOfResult.get(12000); //block here
		String jsonData =  response.getBody();
		String ip=jsonData.substring(jsonData.indexOf("cip")+7,jsonData.indexOf("cid")-4);
		
		String preIp="";
		String prePort="";
		try {
		File file = new File("data/pre_node.txt"); 
		InputStreamReader reader = new InputStreamReader( new FileInputStream(file)); 
		BufferedReader br = new BufferedReader(reader);
		String line =br.readLine(); 
		String [] arr=line.split(" ");
		preIp=arr[0];
		prePort=arr[1];
		br.close();
		}catch(Exception e){
			e.printStackTrace();  
		}
		
		ArrayList<String> fingers=new ArrayList<String>();
		try {
		File file = new File("data/fingerTable.txt"); 
		InputStreamReader reader = new InputStreamReader( new FileInputStream(file)); 
		BufferedReader br = new BufferedReader(reader);
		String line =br.readLine(); 
		line+=" "+line.substring(2).hashCode();
		fingers.add(line);
		while (line != null){
                line = br.readLine();
				line+=" "+line.substring(2).hashCode();
				fingers.add(line);
            } 
		//fingers=fingers.substring(0,fingers.length()-1);
		br.close();
		
		}catch(Exception e){
			e.printStackTrace();  
		}

		//Form<User> messageForm=Form.form(User.class);
		return ok(index.render(ip,"Comp631 project: Chord-Files-System",preIp,prePort,fingers));
    }
	public static Result feedTitle(String url) {
		//WSRequestHolder wsreqHolder = WS.url("https://www.google.com/");
		//wsreqHolder.setQueryParameter("id", "100");
		//Promise<WSResponse> promiseOfResult = wsreqHolder.get();
		//WSResponse response = promiseOfResult.get(12000); //block here
		//String jsonData =  response.getBody();
		return ok("Client:test"+request() );
	}
	
	public static Result Addnode(String host) {
		WSRequestHolder wsreqHolder = WS.url("https://www.google.com/");
		//wsreqHolder.setQueryParameter("id", "100");
		Promise<WSResponse> promiseOfResult = wsreqHolder.get();
		WSResponse response = promiseOfResult.get(12000); //block here
		String jsonData =  response.getBody();
		return ok("Client:"+jsonData);
	}
	public static Result Updatenode() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String port = requestData.get("ret");
		return ok("Client:"+port);
	}	
	public static Result Lookupnode() {
		DynamicForm requestData = Form.form().bindFromRequest();
		String nodeid = requestData.get("ret");
		return ok("Client:"+nodeid);
	}

}
