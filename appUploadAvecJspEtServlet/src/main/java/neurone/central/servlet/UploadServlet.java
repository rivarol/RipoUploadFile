package neurone.central.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.sun.javafx.css.parser.DeriveSizeConverter;

import sun.security.timestamp.HttpTimestamper;


@MultipartConfig(fileSizeThreshold = 1024 * 1024,
maxFileSize = 1024 * 1024 * 5, 
maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final int TAILLE_BUFFER=10240;
	private static final String TYPE_CONTENU="content-disposition";
	private static final String NOM_TYPE_CONTENU="filename";
	private static final boolean MODE_MULTIPART=true;
       
	
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String destination=request.getParameter("destination");
		final Part part=request.getPart("fichier");
		
		String filename = proccessFile(destination, part);
		
		request.setAttribute("filename", filename);
		request.getRequestDispatcher("/index.jsp").forward(request, response);
		
		
		/*for (Part part : request.getParts()) {
			String typeContent = part.getContentType();
			
			if(typeContent != null) {
				Part p = this.updateFile(part);
				request.setAttribute("part", p);
			}
			
			request.getRequestDispatcher("/update.jsp").forward(request, response);
			
		}*/
	}
	
	private String getFileName(Part part) {
		for (String content : part.getHeader(TYPE_CONTENU).split(";")) {
			if (content.trim().startsWith(NOM_TYPE_CONTENU)) {
				return content.substring(content.indexOf("=") + 2, content.length() - 1);
			}
		}
		return null;
	}
	//Première méthode
	protected String proccessFile(String destination, Part part) throws IOException {
		//final String destination=request.getParameter("destination");
		//final Part part=request.getPart("fichier");
		final String filename=this.getFileName(part);
		
		InputStream is=null;
		OutputStream os=null;
		
		try {
			os=new FileOutputStream(new File(File.separator+"home"+
					File.separator+"malanda"+destination+
					File.separator+filename));
			is=part.getInputStream();
			int len=0;
			byte[] bytes=new byte[1024];
			
			while((len=is.read(bytes)) != -1) {
				os.write(bytes, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(os!=null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return filename;
		
	}
	
	//Deuxième méthode
	private Part updateFile(Part part) throws IOException{
		String filename=this.getFileName(part);
		String prefix=filename;
		String suffix="";
		
		File directory=new File(File.separator+"home"+File.separator+"malanda"+File.separator+"MAGASIN");
		if(!directory.exists()) {
			directory.mkdir();
		}
		
		if(filename.contains(".")) {
			prefix=filename.substring(0, filename.lastIndexOf('.'));
			suffix=filename.substring(filename.lastIndexOf('.'));
		}
		
		File file = File.createTempFile(prefix +" ", suffix, directory);
		
		if(MODE_MULTIPART) {
			part.write(file.getName());
		}else {
		
			InputStream is=null;
			OutputStream os=null;
		
			try {
				is=new BufferedInputStream(part.getInputStream());
				os=new BufferedOutputStream(new FileOutputStream(file),TAILLE_BUFFER);
				byte[] bytes=new byte[TAILLE_BUFFER];
			
				for(int i=0; (( i= is.read(bytes)) > 0 );)
					os.write(bytes, 0, i);
			}
			catch(FileNotFoundException e) {
				e.printStackTrace();
			}
			finally { 
				if(os!=null) {
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(is!=null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		part.delete();
		
		return part;
	}

}
