package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.Chat;
import database.User;
import bean.ChatBean;
import bean.UserBean;
import core.MyStringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class Get
 */
@WebServlet("/get")
public class Get extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Get() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String uid = request.getParameter("uid");
        String uuid = request.getParameter("uuid");
        String id2 = request.getParameter("id2");
        JSONObject json = new JSONObject();
        int code = 0;
        String message = "ok";
        ArrayList<ChatBean> chatList = new ArrayList<>();
        boolean invalid = false;
        if (MyStringUtil.empty(uid) || MyStringUtil.empty(uuid) || MyStringUtil.empty(id2)) {
            code = 1001;
            invalid = true;
        }
        if (!invalid) {

            if (!User.verifyUser(uid, uuid)) {
                code = 1002;
                invalid = true;
            }
        }
        if (invalid) {
            message = "fail";
        } else {
            chatList = Chat.getChatByUid(id2, uid);
        }
        json.put("code", code);
        json.put("msg", message);
        json.put("chat_list", JSONArray.fromObject(chatList));
        response.getWriter().write(json.toString());
        response.setHeader("Access-Control-Allow-Headers", "*");
    }

}
