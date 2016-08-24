/**
 * Created by hspcadmin on 2016/8/11.
 */
public class Commodity {
    public static String  price(String result){
       /* ArrayList<String> imgSrc = RegexString(result, "FIX_ACC:(.*?),OP_FLAG");*/
      /*  ArrayList<String> imgSrc = RegexString(result, "bold.>(.+?)<");*/
      /*  ArrayList<String> imgSrc1 = RegexString(result, "num.>(.+?)<");
        ArrayList<String> imgSrc2 = RegexString(result, "compareUp.>(.+?)<");
        ArrayList<String> imgSrc3 = RegexString(result, "link current.>(.+?)<");*/
        if("skuids input error".equals(result)){
            return "error";
        }
        else {
        String[] obj=result.split(",");
        String[] obj1=obj[1].split("\"");
        /*System.out.println(obj1[3]);*/
        return obj1[3];}
    }
    public static String title(String result){
        String[] obj=result.split("<title>");
        String[] obj1=obj[1].split("</title>");
       /* System.out.print(obj1[0]);*/
        return obj1[0];
    }
    public static String[] shop(String result){
        String url1 = "http://item.jd.com/"+result+".html";
        String url = "http://p.3.cn/prices/get?skuid=J_"+result;
        String content = Spider.SendGet(url);
        String title=Spider.SendGet(url1);
        String a[]=new String[64];
        a[0]=title(title);
        /*a[0]="暂时无法显示";*/
        a[1]=price(content);
       /* a[1]="199.00";*/
        return a;
    }

}
