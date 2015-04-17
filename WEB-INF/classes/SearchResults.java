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

public class SearchResults extends VelocityViewServlet {

	public Template handleRequest(HttpServletRequest request,
			HttpServletResponse response, Context ctx) {
		Template form = null;
		
	
		String Title = request.getParameter("Title");
		String Author = request.getParameter("Author");
		String DateFrom = request.getParameter("DateFrom");
		String DateTo = request.getParameter("DateTo");
		String Keyword1 = request.getParameter("Keyword1");
		String Keyword2 = request.getParameter("Keyword2");
		String Keyword3 = request.getParameter("Keyword3");
		
		java.sql.Connection conn = null;
		java.sql.PreparedStatement ps = null;

		String sqluserName = "team152";
		String sqlpassword = "b042ba74";
		String url = "jdbc:mysql://stusql.dcs.shef.ac.uk/team152";

		try {
			// connect to the database
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, sqluserName, sqlpassword);
			
			// prepare statement
			ps = conn.prepareStatement("SELECT ArticleId, Title, Abstract, User.Name, User.Email FROM Article left join User on User.UserId = Article.UserId where Article.Title like '%" + Title + "%';");
			ResultSet results = ps.executeQuery();
			
			JSONObject articleObj;
			JSONArray articleArr = new JSONArray();
			
			while (results.next()) 	{     
				 
				articleObj = new JSONObject();
				articleObj.put("ArticleId", results.getInt("ArticleId"));
				articleObj.put("Title", results.getString("Title"));
				articleObj.put("Name", results.getString("Name"));
				articleObj.put("Email", results.getString("Email"));
				
				articleArr.put(articleObj);	
			}
			
			ctx.put("articleArr", articleArr);
			
	
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		try {
			form = getTemplate("\\vm_template\\SearchResults.vm");
		} catch (Exception e) {
			System.out.println("SearchResults excpetion: " + e);
		}
		return form;
	}

}
