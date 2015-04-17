import java.io.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;
import com.mysql.jdbc.*;
import java.sql.ResultSet;

public class User{
	
	public User(String useremail){
		email = useremail;
	}
	// methods
	
	private java.sql.Connection connectToDB(){
		java.sql.Connection conn = null;
		try 
			{ 
				// connect to the database
				String sqluserName="team152";
				String sqlpassword="b042ba74";
				String url="jdbc:mysql://stusql.dcs.shef.ac.uk/team152";
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				conn=DriverManager.getConnection(url,sqluserName,sqlpassword);
				System.out.println("REMEMBER TO CLOSE YOUR DB CONNECTION");
			}
		catch(Exception e)
			{
				System.out.println(e);	
			}
		return conn;
	}
	
	public int getID(){
		java.sql.Connection conn = connectToDB();
		java.sql.PreparedStatement ps=null;
		try 
			{ 
				// Get user from email
				ps = conn.prepareStatement("SELECT * FROM User WHERE Email='"+email+"';");
				
			}
		catch(Exception e)
			{
				System.out.println(e);	
			}
		try 
			{
				ResultSet results = ps.executeQuery();
				while (results.next()) 
				{     //Find user ID
					id = results.getInt("UserId");
				}
			}
		catch(SQLException e)
			{
				e.printStackTrace();	
			}
		try{conn.close();}catch(SQLException e){e.printStackTrace();}
		return id;
	}
	
	public int countArticles() { 
		java.sql.Connection conn = connectToDB();
		java.sql.PreparedStatement ps=null;
		Integer id = getID();
		
		try 
			{
				//Count articles
				ps = conn.prepareStatement("SELECT COUNT(*) FROM Article WHERE UserId='"+id+"';");
				ResultSet results = ps.executeQuery();
				while (results.next()) 
				{     
					 articlecount = results.getInt(1);
				}
			} 
		catch (SQLException e) 
			{
				e.printStackTrace();
			}
		finally
			{
				try{conn.close();}catch(SQLException e){e.printStackTrace();}
			}
	return articlecount; 
	}
	
	public int countReviews() { 
		java.sql.Connection conn = connectToDB();
		java.sql.PreparedStatement ps=null;
		Integer id = getID();
		
		try 
			{
				//Count instances of article reviews from this user
				ps = conn.prepareStatement("SELECT COUNT(*) FROM ArticleReviewer WHERE UserId='"+id+"';");
				ResultSet results = ps.executeQuery();
				while (results.next()) 
				{     
					 reviewcount = results.getInt(1);
				}
			} 
		catch (SQLException e) 
			{
				e.printStackTrace();
			}
		finally
			{
				try{conn.close();}catch(SQLException e){e.printStackTrace();}
			}
	return reviewcount; 
	}
	
	public int reviewsRequiredforPublishing(){
		countArticles();
		countReviews();
		//Number of reviews reuqired to submit a new article
		int required = 0;
		if (reviewcount - articlecount*3 < 0){
			required = reviewcount - articlecount*3;	
		}
		return -required;
	}
	

	
	
	


	
	
	
	// instance fields
	private String email;
	private String name;
	private int articlecount;
	private int reviewcount;
	private int requiredreviews;
	private int id;
}
