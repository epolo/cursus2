package web.mbeans;

import db.entity.Comments;
import db.entity.CourseTopics;
import db.entity.Courses;
import db.entity.ForumTopics;
import db.entity.InitiativeTopics;
import db.entity.Posts;
import db.entity.Topics;
import db.entity.Users;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class TopicsProcessor {

	private final ContextMBean ctx;
	private final List<Topics> topicsList;
	private final Users writer;
	private final boolean admin;
	
	private Topics curTopic;
	private Posts curPost;
	private Comments curComment;
	
	private String body;

	
	/**
	 * @param writer is current user in case if he is permitted to create posts or comments. 
	 * If topics are read only writer must be null.
	 * If writer has 'admin' role or 'teacher' - owner of topics' course he has admin permission,
	 * i.e. he can edit, delete and create topics.
	 */
	public TopicsProcessor(List topicsList, ContextMBean ctx) {
		this.ctx = ctx;
		this.topicsList = topicsList;
		this.writer = ctx.getUser();
		boolean a = false;
		if (writer != null) {
			if ("admin".equals(writer.getRole()))
				a = true;
			else if ("teacher".equals(writer.getRole()) 
							&& topicsList.size() > 0
							&& topicsList.get(0) instanceof CourseTopics) {
					CourseTopics t = (CourseTopics) topicsList.get(0);
					if (t != null && t.getCourse().getAuthorId().getId() == writer.getId())
						a = true;
				}
		}
		admin = a;
	}

	public List<Topics> getTopicsList() {
		return topicsList;
	}

	public boolean isAdmin() {
		return admin;
	}
	
	public boolean isWriter() {
		return writer != null;
	}

	public Topics getCurTopic() {
		return curTopic;
	}

	public void setCurTopic(Topics curTopic) {
		this.curTopic = curTopic;
	}

	public Posts getCurPost() {
		return curPost;
	}

	public void setCurPost(Posts curPost) {
		this.curPost = curPost;
	}

	public Comments getCurComment() {
		return curComment;
	}

	public void setCurComment(Comments curComment) {
		this.curComment = curComment;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public void createTopic() {
		Topics t0 = topicsList.get(0);
		Topics t;
		Courses course = null;
		if (t0 instanceof CourseTopics) {
			CourseTopics ct = new CourseTopics();
			course = ((CourseTopics)t0).getCourse();
			ct.setCourse(course);
			course.getTopicsList().add(ct);
			t = ct;
		} else {
			t = ForumTopics.TOPIC_DTYPE.equals(t0.getDtype())? new ForumTopics():
					new InitiativeTopics();
		}
		Date now = new Date();
		t.setCreatedAt(now);
		t.setUpdatedAt(now);
		t.setTitle(body);
		body = "";
		t = ctx.db.persist(t);
		topicsList.add(t);
	}

	public void updateTopic(Topics t) {
		t.setUpdatedAt(new Date());
		ctx.db.merge(t);
	}
	
	public void deleteTopic(Topics t) {
		t.setIsDelete(true);
		t.setUpdatedAt(new Date());
		ctx.db.merge(t);
	}

	public void restoreTopic(Topics t) {
		t.setIsDelete(false);
		t.setUpdatedAt(new Date());
		ctx.db.merge(t);
	}
	
	public void createPost() {
		Posts p = new Posts();
		p.setAuthorId(writer);
		p.setBody(body);
		body = "";
		p.setTopicId(curTopic);
		Date now = new Date();
		p.setCreatedAt(now);
		p.setUpdatedAt(now);
		p = ctx.db.persist(p);
		curTopic.getPostsList().add(p);
	}

	public void updatePost() {
		curPost.setUpdatedAt(new Date());
		ctx.db.merge(curPost);
	}
	
	public void deletePost(Posts p) {
		p.setIsDelete(true);
		p.setUpdatedAt(new Date());
		ctx.db.merge(p);
	}
	
	public void createComment() {
		Comments c = new Comments();
		c.setAuthorId(writer);
		c.setBody(body);
		c.setPostId(curPost);
		Date now = new Date();
		c.setCreatedAt(now);
		c.setUpdatedAt(now);
		c = ctx.db.persist(c);
		curPost.getCommentsList().add(c);
	}
}
