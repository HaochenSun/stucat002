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

public class ReaderAbout extends VelocityViewServlet {

	public Template handleRequest(HttpServletRequest request,
			HttpServletResponse response, Context ctx) {
		Template form = null;

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

			//get all volumes from database
			ps = conn.prepareStatement("SELECT AimId, Description FROM Aim;");
			
			ResultSet results = ps.executeQuery();

			int rowcount = 0;
			if (results.last()) {
			  rowcount = results.getRow();
			  results.beforeFirst(); 
			}
			
			String aimData[][] = new String[rowcount][2];
			
			int i=0;
			while (results.next()) 	{     
				 
				 aimData[i][0] = results.getString("AimId");
				 aimData[i][1] = results.getString("Description"); 
				 i++;
			}
			
			ctx.put("aimData", aimData);

			//get all editions from database
			ps1 = conn
					.prepareStatement("SELECT GoalId, Description FROM Goal;");
			ResultSet results1 = ps1.executeQuery();

			if (results1.last()) {
			  rowcount = results.getRow();
			  results1.beforeFirst(); 
			}
			
			String goalData[][] = new String[rowcount][2];
			
			i=0;
			while (results1.next()) 	{     
				 
				goalData[i][0] = results.getString("GoalId");
				goalData[i][1] = results.getString("Description");				 
				i++;
			}
			
			ctx.put("goalData", goalData);
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
			form = getTemplate("\\vm_template\\ReaderAbout.vm");
		} catch (Exception e) {
			System.out.println("ReaderAbout excpetion: " + e);
		}
		return form;
	}

}
