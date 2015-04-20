import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.Template;
import org.apache.velocity.tools.view.VelocityViewServlet;
import javax.servlet.http.HttpSession;

public class ReaderHelp extends VelocityViewServlet {

	public Template handleRequest(HttpServletRequest request,
			HttpServletResponse response, Context ctx) {
		Template form = null;		
		try {
			ctx.put("status","");
			form = getTemplate("\\vm_template\\ReaderHelp.vm");
		} catch (Exception e) {
			System.out.println("ReaderHelp excpetion: " + e);
		}
		return form;
	}

}