package com.company.servlet;

import com.company.helper.LdapADHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by huxm11315 on 2017/7/18.
 */
public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        request.setCharacterEncoding("utf-8");

        String host = request.getParameter("host");
        String adminName = request.getParameter("adminName");
        String adminPassword = request.getParameter("adminPassword");
        String type = request.getParameter("type");
        String filter = request.getParameter("filter");
        String name = request.getParameter("name");
        String node = request.getParameter("node");

        LdapADHelper ad = new LdapADHelper(host, adminName, adminPassword);

        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        String ret = ad.GetADInfo(type, filter, name, node);
        out.print("<div style='word-break:break-all; width='95%'>");
        out.print(ret);
        out.print("</div>");
        out.flush();
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        doGet(request, response);
    }

}
