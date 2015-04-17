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

public class ReaderHome extends VelocityViewServlet {

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
			ps = conn.prepareStatement("SELECT VolumeId, Name FROM Volume;");
			
			ResultSet results = ps.executeQuery();

			int rowcount = 0;
			if (results.last()) {
			  rowcount = results.getRow();
			  results.beforeFirst(); 
			}
			
			String volumeData[][] = new String[rowcount][2];
			
			int i=0;
			while (results.next()) 	{     
				 
				 volumeData[i][0] = results.getString("VolumeId");
				 volumeData[i][1] = results.getString("Name");
				 
				 i++;
			}
			
			ctx.put("volumeData", volumeData);

			//get all editions from database
			ps1 = conn
					.prepareStatement("SELECT EditionId, VolumeId, Description FROM Edition order by VolumeId;");
			ResultSet results1 = ps1.executeQuery();

			if (results1.last()) {
			  rowcount = results.getRow();
			  results1.beforeFirst(); 
			}
			
			String editionData[][] = new String[rowcount][2];
			
			i=0;
			while (results1.next()) 	{     
				 
				 editionData[i][0] = results.getString("EditionId");
				 editionData[i][1] = results.getString("Description");
				 //editionData[i][2] = results.getString("VolumeId");
				 
				 i++;
			}
			
			ctx.put("editionData", editionData);
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
			form = getTemplate("\\vm_template\\ReaderHome.vm");
		} catch (Exception e) {
			System.out.println("ReaderHome excpetion: " + e);
		}
		return form;
	}

}
