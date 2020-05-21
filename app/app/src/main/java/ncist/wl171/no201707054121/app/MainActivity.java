package ncist.wl171.no201707054121.app;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


//import net.sf.json.JSONObject;
import net.sf.json.JSONString;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

public class MainActivity extends AppCompatActivity {

    String requestUrl = "https://v1.alapi.cn/api/mingyan";//名言
    String requestUrl2 ="http://api.tianapi.com/generalnews/index";//新闻
    TextView words ;
    TextView News ;
    Button refresh;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        words = findViewById(R.id.textwords);
        News = findViewById(R.id.textNews);
        refresh = findViewById(R.id.but_refresh);

        //在主线程中强制使用子线程
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getWords("1");//获取名言
        getNews("1");//获取新闻
        refresh.setOnClickListener(new MyOnClickListener(){//第二种

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                super.onClick(v);
                getWords("1");
                Toast.makeText(MainActivity.this, "刷新", Toast.LENGTH_LONG).show();

            }
        });

    }


    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            v.setAlpha(0.5f);
        }
    }

    public void getWords(String id) {//获取名言的 API
        //创建接口HttpClient的实例
        HttpClient client = new DefaultHttpClient();
        //创建HttpPost请求对象
        //HttpPost post = new HttpPost(requestUrl);//名言
        HttpPost post = new HttpPost(requestUrl);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //设置需要传递的参数
        //params.add(new BasicNameValuePair("type", "top"));
        // params.add(new BasicNameValuePair("key", "d502cd6dac51f1fc65741972c0fd14a1"));
      //  params.add(new BasicNameValuePair("typeid", id));

        try {
            //设置URL编码
            post.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
            //发送请求、获取响应对象
            HttpResponse response = client.execute(post);
            String result="";
            //判断请求是否成功处理
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(),"utf-8");
            } else{
                result ="没有查询结果";
            }
            //words.setText(result);  //内容为XML格式
            //用正则式过滤标签
            //words.setText(filterHtml(result));

           //提取JSON字符串中的相关信息
           // JSONObject jsonObject = JSONObject.fromObject(result);
            JSONObject jsonObject = new JSONObject(result);
            JSONObject jsonData = jsonObject.getJSONObject("data");

            String content = jsonData.getString("content");
            String author = jsonData.getString("author");
            result = "" +  content + " \n\n               —————" + author;


            words.setText(filterHtml(result));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "出错了!", Toast.LENGTH_SHORT).show();
        }
    }




    public void getNews(String id) {//获取新闻 的API
        //创建接口HttpClient的实例
        HttpClient client = new DefaultHttpClient();
        //创建HttpPost请求对象
        //HttpPost post = new HttpPost(requestUrl);//名言
        HttpPost post = new HttpPost(requestUrl2);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //设置需要传递的参数
        //params.add(new BasicNameValuePair("type", "top"));
        params.add(new BasicNameValuePair("key", "d502cd6dac51f1fc65741972c0fd14a1"));
        //  params.add(new BasicNameValuePair("typeid", id));

        try {
            //设置URL编码
            post.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
            //发送请求、获取响应对象
            HttpResponse response = client.execute(post);
            String result="";
            //判断请求是否成功处理
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(),"utf-8");
            } else{
                result ="没有查询结果";
            }
            //words.setText(result);  //内容为XML格式
            //用正则式过滤标签
            //words.setText(filterHtml(result));

        /*   //提取JSON字符串中的相关信息
           // JSONObject jsonObject = JSONObject.fromObject(result);
            JSONObject jsonObject = new JSONObject(result);
            JSONObject jsonData = jsonObject.getJSONObject("data");

            String content = jsonData.getString("content");
            String author = jsonData.getString("author");
            result = "" +  content + " \n\n               —————" + author;
*/


            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("newslist");


            List<String> ctimes = new ArrayList<>();
            List<String> titles = new ArrayList<>();
            List<String> descriptions = new ArrayList<>();
            List<String> picUrls = new ArrayList<>();
            List<String> urls = new ArrayList<>();

            int j = 1;
            for(int i=0;i<jsonArray.length();i++){
                JSONObject partresult = jsonArray.getJSONObject(i);
                String  ctime = partresult.getString("ctime");
                ctimes.add(ctime);
                String  title = partresult.getString("title");
                titles.add(title);
                String  description = partresult.getString("description");
                descriptions.add(description);
                String  picUrl = partresult.getString("picUrl");
                picUrls.add(picUrl);
                String  url = partresult.getString("url");
                urls.add(url);
            }
            News.setText("");
            for (int k=1;k<jsonArray.length();k++){
                //words.append(titles.get(k));
                // words.append("\n\n");
                // words.append(urls.get(k));
                //  words.append("\n\n");
                String x=titles.get(k);
                String y=urls.get(k);
                News.append(
                        Html.fromHtml(
                                "<a href='"+y+"'>"+x+"</a> ")
                );
                News.append("\n\n");
                News.setMovementMethod(LinkMovementMethod.getInstance());//激活链接
            }

            // words.setText(filterHtml(result));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "出错了!", Toast.LENGTH_SHORT).show();
        }
    }







    //使用正则表达式过滤XML文档中的标签（如<、>及空格等）
    private String filterHtml(String source) {
        if(null == source){
            return "";
        }
        return source.replaceAll("</?[^>]+>","").trim();
    }
}
