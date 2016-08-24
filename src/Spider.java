/**
 * Created by hspcadmin on 2016/8/10.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Spider {
    static String SendGet(String url) {
        // 定义一个字符串用来存储网页内容
        String result = "";
        // 定义一个缓冲字符输入流
        BufferedReader in = null;
        try {
            // 将string转成url对象
            URL realUrl = new URL(url);
            // 初始化一个链接到那个url的连接
            URLConnection connection = realUrl.openConnection();
            // 开始实际的连接
            connection.connect();
            // 初始化 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "gb2312"));
            // 用来临时存储抓取到的每一行的数据
            String line;
            while ((line = in.readLine()) != null) {
                // 遍历抓取到的每一行并将其存储到result里面
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    // 获取所有的编辑推荐的知乎内容
  /*  static ArrayList<zhihu> GetRecommendations(String content) {
        // 预定义一个ArrayList来存储结果
        ArrayList<zhihu> results = new ArrayList<zhihu>();
        // 用来匹配url，也就是问题的链接
        Pattern pattern = Pattern
                .compile("<h2>.+?question_link.+?href=\"(.+?)\".+?</h2>");
        Matcher matcher = pattern.matcher(content);
        // 是否存在匹配成功的对象
        Boolean isFind = matcher.find();
        while (isFind) {
            // 定义一个知乎对象来存储抓取到的信息
            zhihu zhihuTemp = new zhihu(matcher.group(1));
            // 添加成功匹配的结果
            results.add(zhihuTemp);
            // 继续查找下一个匹配对象
            isFind = matcher.find();
        }
        return results;
    }*/
}
