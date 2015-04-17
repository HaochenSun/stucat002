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
import javax.servlet.http.HttpSession;

public class ReaderHome extends VelocityViewServlet {

	public Template handleRequest(HttpServletRequest request,
			HttpServletResponse response, Context ctx) {
		Template form = null;

		java.sql.Connection conn = null;
		java.sql.PreparedStatement ps = null;
		java.sql.PreparedStatement ps1=null;
		java.sql.PreparedStatement ps2=null;

		String sqluserName = "team152";
		String sqlpassword = "b042ba74";
		String url = "jdbc:mysql://stusql.dcs.shef.ac.uk/team152";

		try {
			// connect to the database
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, sqluserName, sqlpassword);
			
			// prepare statement
			ps = conn.prepareStatement("SELECT VolumeId, Name FROM Volume;");
			ResultSet results = ps.executeQuery();
			
			JSONObject volumeObj;
			JSONArray volumeArr = new JSONArray();
			
			while (results.next()) 	{     
				 
				volumeObj = new JSONObject();
				volumeObj.put("VolumeId", results.getString("VolumeId"));
				volumeObj.put("Name", results.getString("Name"));
				
				volumeArr.put(volumeObj);	
			}
			
			ctx.put("volumeArr", volumeArr);
			
			// prepare statement
			ps1 = conn.prepareStatement("SELECT EditionId, VolumeId, Description FROM Edition order by VolumeId;");
			ResultSet results1 = ps1.executeQuery();
							
			JSONObject editionObj;
			JSONArray editionArr = new JSONArray();
			JSONObject editionData = new JSONObject();
			
			String CurrentVId = "";
			String PrevVId = "";

			while (results1.next()) {     
				 
				editionObj = new JSONObject();
				editionObj.put("EditionId", results1.getString("EditionId"));
				editionObj.put("Description", results1.getString("Description"));
				
				CurrentVId = results1.getString("VolumeId");
				
				if (PrevVId.equals("")) {
					editionArr.put(editionObj);
				}
				else if (CurrentVId.equals(PrevVId)) {
					editionArr.put(editionObj);
				}
				else if (!(CurrentVId.equals(PrevVId)) ) {
					
					editionData.put(PrevVId, editionArr);
					
					editionArr = new JSONArray();
					editionArr.put(editionObj);	
				}

				PrevVId = CurrentVId;
			}
			
			editionData.put(CurrentVId, editionArr);
			
			ctx.put("editionData", editionData);
			
			// prepare statement for Article
			ps2 = conn.prepareStatement("SELECT ArticleId, Title, EditionId FROM Article order by EditionId;");
			ResultSet results2 = ps2.executeQuery();
			
			JSONObject articleObj;
			JSONArray articleArr = new JSONArray();
			JSONObject articleData = new JSONObject();
			
			String CurrentEId = "";
			String PrevEId = "";
			
			while (results2.next()) 	{   
				
				articleObj = new JSONObject();
				articleObj.put("ArticleId", results2.getString("ArticleId"));
				articleObj.put("Title", results2.getString("Title"));

				CurrentEId = results2.getString("EditionId");
				
				if (PrevEId.equals("")) {
					articleArr.put(articleObj);
				}
				else if (CurrentEId.equals(PrevEId)) {
					articleArr.put(articleObj);
				}
				else if (!(CurrentEId.equals(PrevEId)) ) {
					articleData.put(PrevEId, articleArr);
					
					articleArr = new JSONArray();
					articleArr.put(articleObj);
				}
				
				PrevEId = CurrentEId;
			}
			
			articleData.put(CurrentEId, articleArr);
			
			ctx.put("articleData", articleData);

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
			ctx.put("status","");
			checkReviewsCount(request);
			form = getTemplate("\\vm_template\\ReaderHome.vm");
		} catch (Exception e) {
			System.out.println("ReaderHome excpetion: " + e);
		}
		return form;
	}

	private void checkReviewsCount(HttpServletRequest request){
		//If user is logged in then update their required review count
		HttpSession session = request.getSession(false);
		if (session.getAttribute("email")!= null){
			String email = (String)session.getAttribute("email");
			User user = new User(email);				
			session.setAttribute("requiredreviewscount",user.reviewsRequiredforPublishing());
		}
	}
}


