package servlet;

import bean.ChatBean;
import core.MyStringUtil;
import core.TimeUtil;
import database.Chat;
import database.User;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class Post
 */
@WebServlet("/post")
public class Post extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Post() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String uid = request.getParameter("uid");
        String uuid = request.getParameter("uuid");
        String id2 = request.getParameter("id2");
        String msg = request.getParameter("msg");
        JSONObject json = new JSONObject();
        int code = 0;
        String message = "ok";
        boolean invalid = false;
        if (MyStringUtil.empty(uid) || MyStringUtil.empty(uuid) || MyStringUtil.empty(id2) || MyStringUtil.empty(msg)) {
            code = 1001;
            invalid = true;
        }
        if (!invalid) {
            if (!User.verifyUser(uid, uuid)) {
                code = 1009;
                invalid = true;
            }
        }
        if (!invalid) {
            if (!User.checkUserActive(id2)) {
                code = 1003;
                invalid = true;
            }
        }
        if (invalid) {
            message = "fail";
        } else {
            ChatBean chat = new ChatBean();
            chat.setId1(uid);
            chat.setId2(id2);
            chat.setMessage(msg);
            chat.setTime(TimeUtil.getTimeInMillions());
            Chat.addChat(chat);
        }
        json.put("code", code);
        json.put("msg", message);
        response.getWriter().write(json.toString());
        response.setHeader("Access-Control-Allow-Headers", "*");
    }

}
