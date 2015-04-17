import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.Template;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.velocity.tools.view.VelocityViewServlet;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.*;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.text.*;


public class Submit extends VelocityViewServlet {
	
	
	public Template handleRequest (HttpServletRequest request, HttpServletResponse response, Context ctx) {
		
		Template form = null;
		HttpSession session = request.getSession(true);
		String email=(String)session.getAttribute("email");;
		String  title = request.getParameter("title");
		String articalAbstract = request.getParameter("articalAbstract");
		User user = new User(email);				
		
		int user_id=user.getID();	
		
		ctx.put("UserId", user_id);
		String username = request.getParameter("username");
		
		String[] keywords=request.getParameterValues("keyselect");
		/*
		for (int i=0; i<keywords.length;i++){
			System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWW"+keywords[i]);
			
		}
		/*
		keywords[0]=request.getParameter("key1");
		keywords[1]=request.getParameter("key2");
		keywords[2]=request.getParameter("key3");
		keywords[3]=request.getParameter("key4");
		keywords[4]=request.getParameter("key5");
		keywords[5]=request.getParameter("key6");
		keywords[6]=request.getParameter("key7");
		keywords[7]=request.getParameter("key8");
		keywords[8]=request.getParameter("key9");
		keywords[9]=request.getParameter("key10");*/
		
		java.sql.Connection conn = null;
		java.sql.PreparedStatement ps=null;
		java.sql.PreparedStatement ps1=null;
		java.sql.PreparedStatement ps2=null;
		String sqluserName="team152";
		String sqlpassword="b042ba74";
		String url="jdbc:mysql://stusql.dcs.shef.ac.uk/team152";
				
		if (title == null) 
		{
			try
			{
				ctx.put("status","Please fill in the fields below. The fields marked * are mandatory.");
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				conn=DriverManager.getConnection(url,sqluserName,sqlpassword);
				// prepare statement for insertion
				Integer keywordsarrayL=0;
				ps = conn.prepareStatement("SELECT COUNT(*) FROM Keyword ;");
				ResultSet results = ps.executeQuery();
					
				while (results.next()) 
				{     
					keywordsarrayL=Integer.valueOf(results.getString("COUNT(*)"));
				}
				//keywordsarrayL+=1;
				
				String[] keywordsarray=new String[keywordsarrayL+1];
				keywordsarrayL-=1;
				ps = conn.prepareStatement("SELECT Keyword FROM Keyword ;");
				results = ps.executeQuery();
				int i=0;
				while (results.next()) 
				{     
					keywordsarray[i]=results.getString("Keyword");
					
					i++;
				}
				ctx.put("keywordsarray", keywordsarray);
				
				ctx.put("keywordsarrayL", keywordsarrayL);
				
			}
			
			catch(Exception e)
			{
				ctx.put("status","2) A technical problem occured while trying to submit your Article. please try submitting again.");
				System.out.println(e);	
			}			
			finally	
			{
				try{conn.close();}catch(SQLException e){e.printStackTrace();}
			}
			
					
			try {
				form = getTemplate("\\vm_template\\Submit.vm");
			} 
			catch (Exception e) {
			System.out.println("Submit excpetion: " + e);
			} 
		}
		else
		{
			
			
			//HttpSession session = request.getSession(true);
			String session_email = (String)session.getAttribute("email");
			if(session_email==null)
			{
				
				try
				{ 
					// connect to the database
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					conn=DriverManager.getConnection(url,sqluserName,sqlpassword);
					// prepare statement for insertion
					Integer userId=0;
					ps = conn.prepareStatement("SELECT UserId FROM User WHERE Email ='"+email+"' ;");
					ResultSet results = ps.executeQuery();
					
					while (results.next()) 
					{     
						userId=Integer.valueOf(results.getString("UserId"));
					}
					if(userId==0)
					{
						System.out.println("No user exists in the system with this email I will create one for you !!");
						String password = RandomStringUtils.random(8,"abcdefghijklmonpqrstuvwxyz*^$_!");
						ps = conn.prepareStatement("INSERT INTO User (Name,Email,Password,RoleId) VALUES ('" + username + "', '" + email + "', '" + password +"', '2')");
					 	ps.executeUpdate();
					 	ps = conn.prepareStatement("SELECT UserId FROM User WHERE Email ='"+email+"' ;");
					 	results = ps.executeQuery();
					 	while (results.next()) 
					 	{     
					 		user_id=results.getInt("UserId");
						}
					 	
						try
						{ 
				
							// connect to the database
							Class.forName("com.mysql.jdbc.Driver").newInstance();
							conn=DriverManager.getConnection(url,sqluserName,sqlpassword);
							// prepare statement for insertion
							Date date = new Date();
							SimpleDateFormat ft =  new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
							ps = conn.prepareStatement("INSERT INTO Article (Title, Abstract,UserId,UploadDate) VALUES ('" + title + "', '" + articalAbstract + "', '" + user_id +
								"', '" + ft.format(date)+" ')");
							ps.executeUpdate();
				
							int ArticleId=0;
							ps = conn.prepareStatement("SELECT ArticleId FROM Article WHERE Title ='"+title+"' and UploadDate='"+ft.format(date)+"' ;");
							results = ps.executeQuery();
							while (results.next()) 
							{     
								ArticleId=Integer.valueOf(results.getString("ArticleId"));
							}
				
							// prepare statement
				
							for (int i=0;i<10;i++)
							{
								if(!keywords[i].equals(""))
								{
									int keywordID=0;
									ps = conn.prepareStatement("SELECT KeywordId FROM Keyword WHERE Keyword ='"+keywords[i]+"' ;");
									results = ps.executeQuery();
									while (results.next()) 
									{     
										keywordID=Integer.valueOf(results.getString("KeywordId"));
									}
								if(keywordID==0)
								{
									ps = conn.prepareStatement("INSERT INTO Keyword (keyword) VALUES ('" + keywords[i] +" ')");
									ps.executeUpdate();
									ps = conn.prepareStatement("SELECT KeywordId FROM Keyword WHERE Keyword ='"+keywords[i]+"' ;");
									results = ps.executeQuery();
									while (results.next()) 
									{     
										keywordID=Integer.valueOf(results.getString("KeywordId"));
									}
									
									ps = conn.prepareStatement("INSERT INTO ArticleKeyword (ArticleId,KeywordId) VALUES ('" + ArticleId +" ','"+keywordID+"')");
									ps.executeUpdate();
								}
								else
								{
									
									ps = conn.prepareStatement("INSERT INTO ArticleKeyword (ArticleId,KeywordId) VALUES ('" + ArticleId +" ','"+keywordID+"')");
									ps.executeUpdate();	
								}	
								}
					
					
							}
				
								
							
						}
						catch(Exception e)
						{
							ctx.put("status","2) A technical problem occured while trying to submit your Article. please try submitting again.");
							System.out.println(e);	
						}			
						finally	
						{
							try{conn.close();}catch(SQLException e){e.printStackTrace();}
						}	
					 	 
					 /// SENDING EMAIL BY THE DETAILS 	
						 try {
						 	 form = getTemplate("\\vm_template\\Submit.vm");
						 } 
						 catch (Exception e) {
						 	 System.out.println("Submit excpetion: " + e);
						 } 	
					}
					else
					{
						
						System.out.println("you r in our DB.. LOGIN FIRST!! this is your user id=" +userId);
						ctx.put("username", username);
						ctx.put("email", email);
						ctx.put("UserId", userId);
						
						//////////////////////////
						///
						ctx.put("title", title);
						////////////////////////////
						for (int i=0; i<keywords.length;i++){
							ctx.put(("key"+i), keywords[i]);
						}
						/*ctx.put("key1", keywords[0]);
						ctx.put("key2", keywords[1]);
						ctx.put("key3", keywords[2]);
						ctx.put("key4", keywords[3]);
						ctx.put("key5", keywords[4]);
						ctx.put("key6", keywords[5]);
						ctx.put("key7",keywords[6]);
						ctx.put("key8", keywords[7]);
						ctx.put("key9", keywords[8]);
						ctx.put("key10", keywords[9]);*/
						ctx.put("articalAbstract", articalAbstract);
						ctx.put("status","you r in our DB LOG IN FIRST!!");
						
						form = getTemplate("\\vm_template\\LoginForm.vm");
						
					}
								
					ctx.put("status","Your Artical has been successfully submitted. we will send an email have your login details");
				}
				catch(Exception e)
				{
					ctx.put("status","1) A technical problem occured while trying to submit your Article. please try submitting again.");
					System.out.println(e);	
				}			
				finally	
				{
					try{conn.close();}catch(SQLException e){e.printStackTrace();}
				}
				
			}
			else
			{
				try
				{ 
				
				// connect to the database
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				conn=DriverManager.getConnection(url,sqluserName,sqlpassword);
				// prepare statement for insertion
				Date date = new Date();
				SimpleDateFormat ft =  new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
				ps = conn.prepareStatement("INSERT INTO Article (Title, Abstract,UserId,UploadDate) VALUES ('" + title + "', '" + articalAbstract + "', '" + user_id +
					 "', '" + ft.format(date)+" ')");
				ps.executeUpdate();
				
				int ArticleId=0;
				ps = conn.prepareStatement("SELECT ArticleId FROM Article WHERE Title ='"+title+"' and UploadDate='"+ft.format(date)+"' ;");
				ResultSet results = ps.executeQuery();
				while (results.next()) 
				{     
					ArticleId=Integer.valueOf(results.getString("ArticleId"));
				}
				
				// prepare statement
				
				for (int i=0;i<10;i++)
				{
					if(!keywords[i].equals(""))
					{
						int keywordID=0;
						ps = conn.prepareStatement("SELECT KeywordId FROM Keyword WHERE Keyword ='"+keywords[i]+"' ;");
						results = ps.executeQuery();
						while (results.next()) 
						{     
							keywordID=Integer.valueOf(results.getString("KeywordId"));
						}
						if(keywordID==0)
						{
							ps = conn.prepareStatement("INSERT INTO Keyword (keyword) VALUES ('" + keywords[i] +" ')");
							ps.executeUpdate();
							ps = conn.prepareStatement("SELECT KeywordId FROM Keyword WHERE Keyword ='"+keywords[i]+"' ;");
							results = ps.executeQuery();
							while (results.next()) 
							{     
								keywordID=Integer.valueOf(results.getString("KeywordId"));
							}
							
							ps = conn.prepareStatement("INSERT INTO ArticleKeyword (ArticleId,KeywordId) VALUES ('" + ArticleId +" ','"+keywordID+"')");
							ps.executeUpdate();
						}
						else
						{
							
							ps = conn.prepareStatement("INSERT INTO ArticleKeyword (ArticleId,KeywordId) VALUES ('" + ArticleId +" ','"+keywordID+"')");
							ps.executeUpdate();	
						}	
					}
					
					
				}
				
								
				ctx.put("status","Your Artical has been successfully submitted.");
			}
			catch(Exception e)
			{
				ctx.put("status","3) A technical problem occured while trying to submit your Article. please try submitting again.");
				System.out.println(e);	
			}			
			finally	
			{
				try{conn.close();}catch(SQLException e){e.printStackTrace();}
			}
			try {
				form = getTemplate("\\vm_template\\Submit.vm");
			} 
			catch (Exception e) {
			System.out.println("Submit excpetion: " + e);
			} 
			}
			
		}
		
		
		
		return form;
	}
	
	
	
	
	
}