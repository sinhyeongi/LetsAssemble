package com.la.letsassemble.Util;



import com.la.letsassemble.config.InicisConfig;

import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;

import lombok.RequiredArgsConstructor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;




@RequiredArgsConstructor

public class InicisUtil {

    private final InicisConfig config;
    //결제 금액 가져오기
    public String getPrice(String uid){
        String token = getToken();

        JsonObject data = null;
        if(token == null || token.isBlank()){
            return "connection err";
        }

        String price = "-1";
        try{
            String url_uid = URLEncoder.encode(uid,"UTF-8");
            String geturl = "https://api.iamport.kr/payments/find/"+url_uid+"/";
            URL url = new URL(geturl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("Authorization","Bearer "+token);
            con.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            JsonObject json = new JsonObject();
            json.addProperty("merchant_uid",uid);
            bw.write(json.toString());
            bw.flush();
            bw.close();
            data = getData(con);
            price = getValue(data,"amount");
            con.disconnect();
        }catch(Exception e){
            e.printStackTrace();
        }
        return price;
    }
    //결제 취소 금액 가져오기
    public String getCancel_Amount(String uid){
        String token = getToken();

        JsonObject data;
        if(token == null || token.isBlank()){
            return "connection err";
        }

        String price = "-1";
        try{
            String url_uid = URLEncoder.encode(uid,"UTF-8");
            String geturl = "https://api.iamport.kr/payments/find/";
            URL url = new URL(geturl+url_uid+"/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("Authorization","Bearer "+token);

            data = getData(con);
            price = getValue(data,"cancel_amount");
            con.disconnect();
        }catch(Exception e){
            e.printStackTrace();
        }
        return price;
    }
    //결제 취소 가능 금약 (결제금액 - 결제 취소금액)
    public int RefundableAmount(String uid){
        String price = null;
        String cancleprice = null;
        String token = getToken();
        JsonObject data;
        if(token == null || token.isBlank()){
            return -1;
        }
        try{
            String url_uid = URLEncoder.encode(uid,"UTF-8");
            String geturl = "https://api.iamport.kr/payments/find/";
            URL url = new URL(geturl+url_uid+"/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("Authorization","Bearer "+token);

            data = getData(con);
            if(data == null){
                return -1;
            }
            price = getValue(data,"amount");
            cancleprice = getValue(data,"cancel_amount");
            con.disconnect();
        }catch(Exception e){
            e.printStackTrace();
        }
        if(price != null && cancleprice != null){
            int val = (Integer.parseInt(price) - Integer.parseInt(cancleprice));
            return val;
        }
        return -1;
    }
    //Token 받아오기
    private String getToken(){
        String surl = "https://api.iamport.kr/users/getToken";
        String token = null;
        try{
            URL url = new URL(surl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json");
            con.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("imp_key",config.getApiKey().toString());
            jsonObject.addProperty("imp_secret",config.getApisecret().toString());
            bw.write(jsonObject.toString());
            bw.flush();
            bw.close();
            JsonObject data = getData(con);

            token = getValue(data,"access_token");
            con.disconnect();
        }catch(Exception e){
        }
        return token;
    }
    //JsonObject 받아서 key값에 해당하는값 리턴 없다면 null
    private String getValue(JsonObject obj, String key){
        if(obj == null) {return null;}

        if (obj.get("code") == null ||!(obj.get("code").toString().equals("0"))) {
            return null;
        }
        if(obj.get("response") == null || ((JsonObject)obj.get("response")).get(key) == null){
            return null;
        }
        JsonObject respon = (JsonObject)obj.get("response");
        String value = respon.get(key).toString().replaceAll("\"","");
        return value;
    }
    //받아오는 json데이터 처리
    private JsonObject getData(HttpURLConnection con){
        StringBuilder sb = null;
        JsonObject object = null;
        try {
            int code = con.getResponseCode();

            BufferedReader br = null;
            if (code == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            JsonParser parser = new JsonParser();
            object = (JsonObject) parser.parse(sb.toString());


        }catch (Exception e){
            e.printStackTrace();
        }

        return object;
    }
    //결제 취소 기능
    public String Cancel(String uid,int price){
        if(uid == null || uid.isBlank()){
            return "Empty Data";
        }
        int cancel_price = RefundableAmount(uid);
        if(cancel_price <= 0){
            return "Payment already canceled";
        }else if(cancel_price < price){
            return "Cancellation amount exceeded";
        }

        String surl = "https://api.iamport.kr/payments/cancel";
        String token = getToken();
        try{
            URL url = new URL(surl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("Authorization","Bearer "+token);
            con.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("merchant_uid",uid);
            jsonObject.addProperty("amount",price);
            bw.write(jsonObject.toString());
            bw.flush();
            bw.close();
            con.disconnect();
            JsonObject data = getData(con);
            int code = Integer.parseInt(data.get("code").toString());
            if(code != 0){
                return "err";
            }

        }catch(Exception e){
        }
        return "ok";
    }
}
