package com.project.twitch.servlet;

import com.project.twitch.db.MySQLConnection;
import com.project.twitch.db.MySQLException;
import com.project.twitch.entity.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user=ServletUtil.readRequestBody(User.class,request);
        if(user==null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean isUserAdded=false;
        MySQLConnection connection=null;
        try{
            connection=new MySQLConnection();
            user.setPassword(ServletUtil.encryptPassword(user.getUserId(),user.getPassword()));

            isUserAdded = connection.addUser(user);
        } catch (MySQLException e) {
            throw new ServletException(e);
        } finally {
            connection.close();
        }

        if (!isUserAdded) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }

    }
}
