package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Database.Chat;
import Database.User;
import bean.UserBean;
import core.MyStringUtil;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ClearMsg
 */
@WebServlet("/clearMsg")
public class ClearMsg extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ClearMsg() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String uid = request.getParameter("uid");
		String uuid = request.getParameter("uuid");
		JSONObject json = new JSONObject();
		int code = 0;
		String message = "ok";
		boolean invalid = false;
		if (MyStringUtil.empty(uid) || MyStringUtil.empty(uuid)) {
			code = 1001;
			invalid = true;
		}
		if (!invalid) {
			UserBean userInfo = User.getUserByUid(uid);
			if (userInfo == null || !uuid.equals(userInfo.getUuid())) {
				code = 1002;
				invalid = true;
			}
		}
		if (invalid) {
			message = "fail";
		} else {
			Chat.removeUserLastChatsByUid(uid);
		}
		json.put("code", code);
		json.put("msg", message);
		response.getWriter().write(json.toString());
		response.setHeader("Access-Control-Allow-Headers", "*");
	}

}
