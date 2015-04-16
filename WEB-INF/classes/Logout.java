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
import javax.servlet.*;
import javax.servlet.http.*;

public class Logout extends VelocityViewServlet {
	
	public Template handleRequest (HttpServletRequest request, HttpServletResponse response, Context ctx) {
		Template page = null;
		response.setContentType("text/html");
		HttpSession session = request.getSession(true);
		
		//Destroy the user session data
		session.removeAttribute("username");
		session.removeAttribute("roleID");
		session.removeAttribute("email");
		ctx.put("status","Logged Out");	
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
