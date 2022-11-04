package com.example.project4task1;
/*
 * Author: Tiantian Fang
 * AndrewID: tiantiaf
 * Email: tiantiaf@andrew.cmu.edu
 * Last Modified: Apr 3, 2022
 *
 * This file is the Model component of the MVC, and it models the business
 * logic for the web application.  In this case, the business logic involves
 * making a request to Musixmatch and returns the album name and publish date.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class RepoModel {
//
//    String appId = "e0f913cf";
//    String apiKey = "39d47328eb07675367cb5ccd221c9187";

    class Repo{
        String owner;
        String name;
        String htmlUrl;
        int star;
        String description;

        public Repo(String owner, String name, String htmlUrl, int star, String description) {
            this.owner = owner;
            this.htmlUrl = htmlUrl;
            this.star = star;
            this.description = description;
            this.name = name;
        }
    }

    List<Repo> repos;
    String language;

    public List<Repo> doSearch(String language)
            throws UnsupportedEncodingException {
        /*
         * URL encode the searchTag, e.g. to encode spaces as %20
         *
         * There is no reason that UTF-8 would be unsupported.  It is the
         * standard encoding today.  So if it is not supported, we have
         * big problems, so don't catch the exception.
         */
        repos = new ArrayList<>();
        language = URLEncoder.encode(language.toLowerCase(), "UTF-8");
        this.language = language;

        // search for artist id
        String URL = "https://api.github.com/search/repositories?q=language:" + language
                + "&sort=stars&order=desc";

        // Fetch the page
        return fetch(URL);
    }

    // fetch artist id
    private List<Repo> fetch(String urlString) {
        String response = "";
        JSONObject responseJSON = new JSONObject();
        JSONArray items = new JSONArray();
        try {
            URL url = new URL(urlString);
            /*
             * Create an HttpURLConnection.  This is useful for setting headers
             * and for getting the path of the resource that is returned (which
             * may be different from the URL above if redirected).
             * HttpsURLConnection (with an "s") can be used if required by the site.
             */
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String str;
            // Read each line of "in" until done, adding each to "response"
            while ((str = in.readLine()) != null) {
                // str is one line of text readLine() strips newline characters
                response += str;
            }
            in.close();
            responseJSON = new JSONObject(response);
            items = responseJSON.getJSONArray("items");

            if (!items.isEmpty()) {
                System.out.println("The 5 top " + language + " github repo info as below:");
                for (int i = 0; i < 5; i++) {
                    String owner = items.getJSONObject(i).getJSONObject("owner").get("login").toString();
                    String htmlUrl = items.getJSONObject(i).get("html_url").toString();
                    int star = Integer.valueOf(items.getJSONObject(i).get("stargazers_count").toString());
                    String description = items.getJSONObject(i).get("description").toString();
                    String name = items.getJSONObject(i).get("name").toString();
                    System.out.println(" - name: " + name
                            + ", owner: " + owner
                            + ", url: " + htmlUrl
                            + ", description: " + description
                            + ", stars: " + star);
                    repos.add(new Repo(owner, name, htmlUrl, star, description));
                }
            }

        } catch (IOException e) {
            System.out.println("error");
        }
        return this.repos;
    }

}