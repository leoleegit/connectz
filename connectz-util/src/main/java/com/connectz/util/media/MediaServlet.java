package com.connectz.util.media;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.connectz.util.CoderUtils;
import com.connectz.util.ReflectUtils;
import com.connectz.util.json.JsonObject;

public class MediaServlet extends HttpServlet {
	protected Logger logger = Logger.getLogger(this.getClass());
	private static final String MEDIA_FACTORY = "media.factory";
	private static final String DEFAULT_MEDIA_FACTORY = "com.protelnet.util.media.DefaultMediaFactory";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MediaFactory mediaFactory;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String factory = config.getInitParameter(MEDIA_FACTORY);
		if(factory==null)
			factory = System.getProperty(MEDIA_FACTORY,
					DEFAULT_MEDIA_FACTORY);
		mediaFactory = (MediaFactory) ReflectUtils.newInstance(factory, null);
		logger.info(mediaFactory);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");

		String errormsg = null;
		List<FileItem> list = new ArrayList<FileItem>();
		List<MediaFile> urls = new ArrayList<MediaFile>();
		if (ServletFileUpload.isMultipartContent(request)) {
			FileItem item = null;
			try {
				ServletFileUpload upload = new ServletFileUpload(
						new DiskFileItemFactory());
				@SuppressWarnings("unchecked")
				List<FileItem> items = upload.parseRequest(request);
				Iterator<FileItem> it = items.iterator();
				while (it.hasNext()) {
					item = it.next();
					if (!item.isFormField()) {
						list.add(item);
					}
				}
			} catch (Throwable e) {
				logger.error("", e);
				errormsg = "transfer exception";
			}
		} else {
			errormsg = "not found multipart content";
		}
		if (errormsg != null) {
			error(response, errormsg);
			return;
		}
		for (FileItem item : list) {
			try {
				String file_name = item.getName();
				String file_id = mediaFactory.generateMediaId();
				String content_type = item.getContentType();

				MediaFile media = new MediaFile(null);
				media.setContentType(content_type);
				media.setName(file_name);
				media.setSize(item.getSize());
				media.setCreateTime(System.currentTimeMillis());
				media.setType("upload");
				media.setMediaID(file_id);

				InputStream is = item.getInputStream();
				if(mediaFactory.isSaveFile()){
					java.io.File file = mediaFactory.getTempFile(file_id);
					media.setFile(file);
					FileOutputStream out_file = new FileOutputStream(file);
					logger.info("uploading...........");
					byte[] b = new byte[1024];
					int nRead;
					while ((nRead = is.read(b, 0, 1024)) > 0) {
						out_file.write(b, 0, nRead);
					}
					try {
						out_file.close();
						out_file.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						file.delete();
						throw e1;
					}
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						throw e;
					}
				}else{
					media.setInputStream(is);
				}
				
				mediaFactory.uploaded(media);
				logger.info(media.toJson());
				urls.add(media);

			} catch (Throwable e) {
				logger.error("", e);
				errormsg = "transfer exception";
			}
		}
		if (errormsg != null) {
			error(response, errormsg);
			return;
		} else {
			String msg = "";
			if (urls.size() > 0) {
				if (urls.size() == 1)
					msg = urls.get(0).toJson();
				else
					msg = JsonObject.toJsonArray(urls);
			}
			logger.info(String.format("response:%s", msg));
			response.getWriter().println(msg);
		}
	}

	protected void error(HttpServletResponse response, String msg)
			throws IOException {
		response.getWriter().println(msg);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String path = request.getPathInfo();
		logger.info("path:" + path);
		response.setDateHeader("expries",  
                System.currentTimeMillis() + 7 * 24 * 3600 * 1000);  
		String errormsg = null;
		if (path == null)
			errormsg = "access path is null";
		else {
			MediaFile media = mediaFactory.getMediaFile(path);
			if (media == null) {
				errormsg = "file not found";
			} else {
				String file_name = media.getName();
				long file_size   = media.getSize();
				String content_type = media.getContentType();
				java.io.File file = media.getFile();
				InputStream is    = media.getInputStream();
				
				if (file != null && file.exists()) {
					is = new FileInputStream(file);
					file_size = file.length();
				} 
				if(is==null){
					logger.warn("file not found:" + file_name);
					errormsg = "file not found";
				}
				else {
					try {
						String user_agent = request.getHeader("User-Agent");
						if (user_agent.toLowerCase().indexOf("msie") != -1) {
							file_name = CoderUtils.encode(file_name);
						} else {
							file_name = file_name.replaceAll(" ", "");
							file_name = new String(file_name.getBytes("utf-8"),
									"iso-8859-1");
						}
						logger.info(file_name);
						response.reset();
						response.setContentType(content_type);
						response.addHeader("Content-Disposition",
								"attachment;filename=\"" + file_name + "\"");
						response.addHeader("Content-Length", "" + file_size);

						OutputStream out = response.getOutputStream();
						//GZIPOutputStream gos = new GZIPOutputStream(out);  
						byte[] b = new byte[1024];
						int nRead;
						while ((nRead = is.read(b, 0, 1024)) > 0) {
							out.write(b, 0, nRead);
						}
						try {
							is.close();
							//gos.finish();  
							out.close();
							out.flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							throw e1;
						}
					} catch (IOException e) {
						logger.error("", e);
						errormsg = e.getMessage();
					}
				}
			}

		}
		if (errormsg != null) {
			logger.info(errormsg);
			response.getWriter().print(errormsg);
		}
	}

}
