package servlet;

import core.MyStringUtil;
import database.User;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/keep")
public class Keep extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            if (!User.verifyUser(uid, uuid)) {
                code = 1009;
                invalid = true;
            }
        }
        if (invalid) {
            message = "fail";
        } else {
            User.renewUserLastLogin(uid);
        }
        json.put("code", code);
        json.put("msg", message);
        response.getWriter().write(json.toString());
    }
}
