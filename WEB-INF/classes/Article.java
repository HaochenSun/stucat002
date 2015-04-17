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

public class Article extends VelocityViewServlet {

	public Template handleRequest(HttpServletRequest request,
			HttpServletResponse response, Context ctx) {
		Template form = null;
		
		int articleId;
		try {
			articleId = Integer.parseInt(request.getParameter("aId"));
		}
		catch (Exception e) {
			articleId = 0;
		}

		java.sql.Connection conn = null;
		java.sql.PreparedStatement ps = null;
		java.sql.PreparedStatement ps1=null;

		String sqluserName = "team152";
		String sqlpassword = "b042ba74";
		String url = "jdbc:mysql://stusql.dcs.shef.ac.uk/team152";

		try {
			// connect to the database
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, sqluserName, sqlpassword);
			
			// prepare statement
			ps = conn.prepareStatement("SELECT Title, Abstract, User.Name, User.Email FROM Article left join User on User.UserId = Article.UserId where Article.ArticleId = " + articleId + ";");	
			ResultSet results = ps.executeQuery();
			
			while (results.next()) 	{  
				
				ctx.put("Title", results.getString("Title"));
				ctx.put("Abstract", results.getString("Abstract"));
				ctx.put("Name", results.getString("Name"));
				ctx.put("Email", results.getString("Email"));
			}
			
			// prepare statement
			ps1 = conn.prepareStatement("SELECT Keyword.KeywordId, Keyword.Keyword FROM Article left join ArticleKeyword on ArticleKeyword.ArticleId = Article.ArticleId left join Keyword on Keyword.KeywordId = ArticleKeyword.KeywordId where Article.ArticleId = 1;");
			ResultSet results1 = ps1.executeQuery();
							
			JSONObject keywordObj;
			JSONArray keywordArr = new JSONArray();
			
			while (results1.next()) 	{     
				 
				keywordObj = new JSONObject();
				keywordObj.put("KeywordId", results1.getString("KeywordId"));
				keywordObj.put("Keyword", results1.getString("Keyword"));
				
				keywordArr.put(keywordObj);	
			}
			
			ctx.put("keywordArr", keywordArr);

			
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
			form = getTemplate("\\vm_template\\Article.vm");
		} catch (Exception e) {
			System.out.println("Article excpetion: " + e);
		}
		return form;
	}

}
