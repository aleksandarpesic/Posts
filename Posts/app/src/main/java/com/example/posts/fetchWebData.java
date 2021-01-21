package com.example.posts;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
//Class to get data by String from url adress
public class fetchWebData implements Runnable  {

    private String webData="";
    private String url_adress;

    public fetchWebData(String url){
        url_adress=url;
    }
    public fetchWebData(){}
    @Override
    public void run() {
        try {
            URL url = new URL(url_adress);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            if (responsecode!=200)
                throw new RuntimeException("Http response code: "+responsecode);
            else {
                Scanner sc=new Scanner(url.openStream());
                while (sc.hasNext())
                {
                    webData+=sc.nextLine()+"\n";
                    //System.out.println(sc.nextLine());
                }
                System.out.println("data");
                //System.out.println(webData);
                sc.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getWebData(){
        return webData;
    }
    public void setUrlAdress(String url){
        url_adress=url;
    }

}
