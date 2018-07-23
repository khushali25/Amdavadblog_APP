//package Core;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//
//import Model.Category;
//import okhttp3.Response;
//
//public class ApiService {
//    private String hostName;
//    //@SuppressWarnings("deprecation")
//    private static HttpClient httpClient = new DefaultHttpClient();
//
//    public String getHostName() {
//        return HostName;
//    }
//
//    public void setHostName(String hostName) {
//        HostName = hostName;
//    }
//
//    public String HostName;
//
//
//    public ApiService(String hostName) {
//    }
//
//    public final ArrayList<Category> GetAllCategories(String lang)
//    {
//        return GetAllCategories(lang, 10);
//    }
//
//    public final ArrayList<Category> GetAllCategories(int page)
//    {
//        return GetAllCategories("en", 10);
//    }
//
//    public final ArrayList<Category> GetAllCategories(String lang, int i)
//    {
//        return GetAllCategories("en", 10);
//    }
//
//    public final ArrayList<Category> GetAllCategories(int page, String lang) throws IOException {
//        ArrayList<Category> categories = new ArrayList<Category>();
//        String url = HostName + "/wp-json/wp/v2/categories";
//        url = url + String.format("?page=%1$s&per_page=%2$s&lang=%3$s", page, lang);
//        HttpGet httpGet = new HttpGet(url);
//        HttpResponse response = httpClient.execute(httpGet);
//
//        int status = response.getStatusLine().getStatusCode();
//        if (status == 200)
//        {
//            HttpEntity entity = response.getEntity();
//            String data = EntityUtils.toString(entity);
//            JSONObject jsono = new JSONObject(data);
//            HttpResponse execute = httpClient.execute(httpGet);
//            InputStream content = execute.getEntity().getContent();
//            //Parse Data - 200 status code -
//            String categoriesAsyncRes = response.toString();
//            categories = JsonConvert.DeserializeObject<ArrayList<Category>>DeserializeObject(categoriesAsyncRes);
//        }
//        else
//        {
//            // Non 200 status code
//        }
//
//        return categories;
//    }
//
//}
