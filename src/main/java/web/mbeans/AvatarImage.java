package web.mbeans;

import db.entity.Courses;
import db.entity.Users;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import net.coobird.thumbnailator.Thumbnails;

public class AvatarImage {

	public enum AVATAR_TYPE { LARGE("_l", 240), MEDIUM("_m", 120), SMALL("_s", 60); 
		final String suffix;
		final int size;
		AVATAR_TYPE(String suffix, int size) { this.suffix = suffix; this.size = size; }
// 		static AVATAR_TYPE 
	}
	
	// static final String UPLOAD_URL = "/upload/";
	private String uploadUrl;
	private String uploadDir;
			// UPLOAD_DIR = "/Java/projects/cursus2m/deploy/tomcat/webapps/ROOT" + UPLOAD_URL;
	static final String DEF_AVATAR_FILE = "avatar.png";
	static final String DEF_COURSE_FILE = "course.png";
	
	AvatarImage() {
	}

	public String getUploadUrl() {
		return uploadUrl;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}

	/**
	 * Splits file path to file parts: name and ext.
	 * Returns file parts as string array.
	 */
	private String[] split(String path) {
		if (path == null)
			throw new IllegalArgumentException("'path' can not be null");
		int i = path.lastIndexOf('.');
		if (i < 0)
			throw new IllegalArgumentException("'path' must have extension: " + path);
		return new String[] { path.substring(0, i), path.substring(i + 1) };
	}
	
	private String buildFileName(String[] fileParts, AVATAR_TYPE type) {
		return fileParts[0] + type.suffix + '.' + fileParts[1];
	}
	
	private OutputStream getOutputStream(File imgDir, String[] fileParts, AVATAR_TYPE type) throws IOException {
		FileOutputStream fos = new FileOutputStream(new File(imgDir, buildFileName(fileParts, type)));
		return fos;
	}

	// returns image filename that should be persisted into the Users entity
	public String uploadUserImg(Users user, Part part) throws IOException {
		String fn = part.getSubmittedFileName();
		if (fn == null || fn.isEmpty() || part.getSize() <= 0)
			throw new IllegalArgumentException("Bad format of uploading image: filename=" + fn + ", sz=" + part.getSize());
		
		String[] ss = split(fn);
		File imgDir = new File(uploadDir + user.getUuid() + "/img/");
		imgDir.mkdirs();
		
		// NB! very long operation
		BufferedImage bi = ImageIO.read(part.getInputStream());
		// make kuadrant
		Rectangle rect = new Rectangle(0, 0, bi.getWidth(), bi.getHeight());
		if (rect.width > rect.height) {
			rect.x = (rect.width - rect.height) / 2;
			rect.width = rect.height;
		} else if (rect.height > rect.width) {
			rect.y = (rect.height - rect.width) / 2;
			rect.height = rect.width;
		}

		for (AVATAR_TYPE at : AVATAR_TYPE.values()) {
			OutputStream fos = getOutputStream(imgDir, ss, at);
			Thumbnails.of(bi)
					.sourceRegion(rect)
					.size(at.size, at.size)
					.outputFormat(ss[1])
					.toOutputStream(fos);
			fos.flush();
			fos.close();
		}

		return fn;
	}

	public String getUserUrl(Users user, String type) {
		return getUserUrl(user, AVATAR_TYPE.valueOf(type));
	}
	
	public String getUserUrl(Users user, AVATAR_TYPE type) {
		String fn = user.getAvatarFile();
		String imgUrl = uploadUrl;
		if (fn == null || fn.isEmpty())
			fn = DEF_AVATAR_FILE;
		else
			imgUrl += user.getUuid() + "/img/";
		return imgUrl + buildFileName(split(fn), type);
	}

	public String getCourseUrl(Courses course, AVATAR_TYPE type) {
		String fn = course.getCoverUrl();
		String imgUrl = uploadUrl;
		if (fn == null || fn.isEmpty())
			fn = DEF_COURSE_FILE;
		else
			imgUrl += course.getUuid() + "/img/";
		return imgUrl + buildFileName(split(fn), type);
	}

	// returns image filename that should be persisted into the Users entity
	public String uploadCourseImg(Courses course, Part part) throws IOException {
		String fn = part.getSubmittedFileName();
		if (fn == null || fn.isEmpty() || part.getSize() <= 0)
			throw new IllegalArgumentException("Bad format of uploading image: filename=" + fn + ", sz=" + part.getSize());
		
		String[] ss = split(fn);
		File imgDir = new File(uploadDir + course.getUuid() + "/img/");
		imgDir.mkdirs();
		
		// NB! very long operation
		BufferedImage bi = ImageIO.read(part.getInputStream());
		// make kuadrant
		Rectangle rect = new Rectangle(0, 0, bi.getWidth(), bi.getHeight());
		if (rect.width > rect.height) {
			rect.x = (rect.width - rect.height) / 2;
			rect.width = rect.height;
		} else if (rect.height > rect.width) {
			rect.y = (rect.height - rect.width) / 2;
			rect.height = rect.width;
		}

		for (AVATAR_TYPE at : AVATAR_TYPE.values()) {
			OutputStream fos = getOutputStream(imgDir, ss, at);
			Thumbnails.of(bi)
					.sourceRegion(rect)
					.size(at.size, at.size)
					.outputFormat(ss[1])
					.toOutputStream(fos);
			fos.flush();
			fos.close();
		}

		return fn;
	}

}
