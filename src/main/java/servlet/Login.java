package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Database.User;
import core.MyStringUtil;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String uid = request.getParameter("userId");
		JSONObject json = new JSONObject();
		int code = 1001;
		String message = "fail";
		String uuid = "";
		if (!MyStringUtil.empty(uid)) {
			uuid = User.login(uid);
			code = 0;
			message = "ok";
		}
		json.put("code", code);
		json.put("msg", message);
		json.put("uuid", uuid);
		response.setHeader("Access-Control-Allow-Headers", "http://localhost:63342");
		response.getWriter().write(json.toString());
	}

}
