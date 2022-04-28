package img.artelic;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

/**
 * Servlet implementation class AddImage
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 10)//For Preventing DOS
@WebServlet("/AddImage")
public class AddImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddImage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		System.out.println("In the dopost method of addimage servlet");
		Part file = request.getPart("image");
		String imageFileName = file.getSubmittedFileName();//to get selected image filename
		System.out.println("Selected Image File Name :"+imageFileName);
		
		String uploadPath="C:/Users/HP/eclipse-workspace/art/images/"+imageFileName;
		System.out.println("Upload Path :"+uploadPath);
		
		//Uploading our selected image in the images folder
		try{
		FileOutputStream fos = new FileOutputStream(uploadPath);
		InputStream is = file.getInputStream();
		
		byte[] data = new byte[is.available()];
		is.read(data);
		fos.write(data);
		fos.close();
		}
		
		catch(Exception e) {
			e.printStackTrace();
		}
		
		//jdbc code
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/youtube","root","yourpassword");
			PreparedStatement stmt;
			//Creating Query
			String query ="insert into image(imageFileName) values(?)";
			stmt = connection.prepareStatement(query);
			stmt.setString(1, imageFileName);
			
			int row=stmt.executeUpdate();
			
			if(row>0) {
				System.out.println("Image Added into DB Succesfully");
				String status = "File Has Been Uploaded";
				request.getSession().setAttribute("message", status);
				response.sendRedirect("addImage.jsp");
			}
			else {
				System.out.println("Failed to upload image.");
				String status = "Upload Failed";
				request.getSession().setAttribute("message", status);
				response.sendRedirect("addImage.jsp");
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
	}

}
