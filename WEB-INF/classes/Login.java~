import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.Template;
import org.apache.velocity.tools.view.VelocityViewServlet;
import java.io.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;
import com.mysql.jdbc.*;

public class Login extends VelocityViewServlet {
	
	public Template handleRequest (HttpServletRequest request, HttpServletResponse response, Context ctx) {
		Template page = null;
		response.setContentType("text/html");
		java.sql.Connection conn = null;
		java.sql.PreparedStatement ps=null;
		String username = request.getParameter("name");
		String password = request.getParameter("pwd");
		String checkname = "";
		String checkemail = "";
		Integer checkrole = 0;
		HttpSession session = request.getSession(true);
		try 
			{ 
				// connect to the database
				String sqluserName="team152";
				String sqlpassword="b042ba74";
				String url="jdbc:mysql://stusql.dcs.shef.ac.uk/team152";
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				conn=DriverManager.getConnection(url,sqluserName,sqlpassword);
				// prepare statement
				ps = conn.prepareStatement("SELECT * FROM User WHERE Name='" + username + "' AND Password='"+password+"';");
			}
		catch(Exception e)
			{
				System.out.println(e);	
			}			
		try 
			{
				ResultSet results = ps.executeQuery();
				while (results.next()) 
				{     
					 checkname = results.getString("Name");
					 checkrole = results.getInt("RoleId");
					 checkemail = results.getString("Email");
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
			
		
		if (checkname.equals(username) && checkname != "")
		//Successful login
			{
				ctx.put("status","Logged in as: " + checkname);
				//Store important user values in session
				session.setAttribute("username", checkname);
				session.setAttribute("roleID", checkrole);
				session.setAttribute("email", checkemail);
			} 
		else if (username == null && password == null)
		//No login attempt
			{
				ctx.put("status","Username and Password not found");
			}	
		else 
		//Any other reason for failure
			{
				ctx.put("status","Unsuccessful login");
			}	
		try 
		{
			//Render homepage
			page = getTemplate("\\vm_template\\ReaderHome.vm");
		} 
		catch (Exception e) 
		{
			System.out.println("VelocityForm excpetion: " + e);
		}
		return page;
	}
}