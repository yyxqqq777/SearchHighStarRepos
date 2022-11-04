package com.example.project4task1;/*
 * Author: Tiantian Fang
 * AndrewID: tiantiaf
 * Email: tiantiaf@andrew.cmu.edu
 * Last Modified: Apr 3, 2022
 *
 * This is a short example of MVC. If the user enter the name of a singer,
 * then they will get singer's latest three album names and publish date,
 * if the artist released less than 3 albums, only display the actual number of albums
 * If there are more than one singer have the same name,
 * It will choose the first one in the API result page.
 * If no such singer, it will show no singer.
 * The servlet is acting as the controller.
 * There is no view.
 * The model is provided by MusixmatchServletModel.
 */
import java.io.*;
import java.util.List;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name = "repoServlet",urlPatterns = {"/*"})
public class RepoServlet extends HttpServlet {
    RepoModel repo = null;

    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        repo = new RepoModel();
    }

    // This servlet will reply to HTTP GET requests via this doGet method
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        JSONObject responseJSON = new JSONObject();

        String pathInfo = request.getPathInfo();
        System.out.println("search: " + pathInfo);
        // get the search parameter if it exists
        String language = pathInfo.split("/")[1];

        List<RepoModel.Repo> repos = repo.doSearch(language);

        // create the response json
        if (repos.isEmpty()){
            responseJSON.put("result", "no matching repos in " + language);
        }
        else{
            JSONArray result = new JSONArray();
            for (RepoModel.Repo r : repos) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("owner", r.owner);
                jsonObject.put("description", r.description);
                jsonObject.put("stars", r.star);
                jsonObject.put("url", r.htmlUrl);
                result.put(jsonObject);
            }
            responseJSON.put("result", result);
        }
        PrintWriter out = response.getWriter();
        out.println(responseJSON);
    }
}