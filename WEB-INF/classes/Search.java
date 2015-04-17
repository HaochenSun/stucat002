import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.Template;
import org.apache.velocity.tools.view.VelocityViewServlet;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class Search extends VelocityViewServlet {

	public Template handleRequest(HttpServletRequest request,
			HttpServletResponse response, Context ctx) {
		Template form = null;

		try {
			form = getTemplate("\\vm_template\\Search.vm");
		} catch (Exception e) {
			System.out.println("Search excpetion: " + e);
		}
		return form;
	}

}
