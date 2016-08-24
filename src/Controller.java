import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Date;
import java.text.SimpleDateFormat;

import static java.lang.Thread.sleep;


@WebServlet("/mytest")
public class Controller extends HttpServlet {
    boolean flag = false; //线程开关
    boolean flag2 = true; //判断是否存在
    boolean starttime = true; //判断是否第一次运行
    boolean timeflag = true; //判断是否是第一次发邮件
    boolean timeflag1 =true; //判断是否是第一次启动时间线程
    String type = "无";
    String starttime1 = null;
    int num = 0;
    int runnum = 0;
    List<Shop> shopList = new ArrayList<Shop>();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");//设置日期格式

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static boolean isemaill(String str) {
        Pattern pattern = Pattern.compile("[\\w[.-]]+@[\\w[.-]]+\\.[\\w]+");
        return pattern.matcher(str).matches();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String text = req.getParameter("text");
        if ("closss".equals(text)) {
            flag = true;
            for (int i = 0; i < num; i++) {
                shopList.get(i).setFlag(true);
                shopList.get(i).setType("关闭中");
            }
            type = "操作完成，结果如下";
            req.setAttribute("name", type);
            req.setAttribute("myprace", shopList);
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
        if ("selects".equals(text)) {
            type = "程序运行次数：";
            System.out.println(runnum);
            req.setAttribute("name", type);
            req.setAttribute("myprace", shopList);
            req.setAttribute("num", runnum);
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
        if ("delss".equals(text)) {
            for (int i = 0; i < num; i++) {
                shopList.get(i).setFlag(true);
                shopList.get(i).setType("关闭中");
            }
            flag = true;
            num = 0;
            runnum = 0;
            starttime = true;
            timeflag1 = false;
            type = "数据已经清除";
            shopList.clear();
            req.setAttribute("name", type);
            req.setAttribute("shopid", shopList);
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String text = req.getParameter("text");
        req.setCharacterEncoding("UTF-8");
        final String shopid = req.getParameter("shopid");
        final String prace = req.getParameter("prace");
        final String email = req.getParameter("email");
        final Shop ss = new Shop();
        if (starttime) {
            starttime1 = df.format(new Date());
            starttime = false;
        }


        //———————————————线程定义————————————————————
        class MyThread2 implements Runnable {
            private String name;
            private String prices;

            public void setName(String name) {
                this.name = name;
            }

            public void setprices(String prices) {
                this.prices = prices;
            }

            public void run() {
                while (true) {
                    String[] b = Commodity.shop(name);
                    String[] obj = b[1].split("\\.");
                    ss.setName(b[0]);
                    ss.setPrice(b[1]);
                    System.out.println(ss.getName());
                    runnum++;
                    if (Integer.parseInt(obj[0]) < Integer.parseInt(prices)) {
                        try {
                            new Mail().sendmail(name, b, ss.getEmail());
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        } catch (GeneralSecurityException e) {
                            e.printStackTrace();
                        }
                        type = "已发送邮件！";
                        ss.setType("已发送邮件！");
                        for (int k = 0; k < shopList.size(); k++) {
                            if (shopList.get(k).getId().equals(name)) {
                                shopList.remove(k);
                                k--;
                                num--;
                            }
                        }
                        break;
                    }
                    if (flag) {
                        break;
                    }
                    try {
                        sleep(1800000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        //———————————————删除时间线程定义————————————————————
        class MyThread3 implements Runnable {
            private int nummm = 0;

            public void run() {
                while (true) {
                    flag = true;
                    nummm++;
                    System.out.println(nummm);
                    if (nummm > 6) {
                        flag = false;
                        for (int k = 0; k < num; k++) {
                            MyThread2 myThread = new MyThread2();
                            myThread.setName(shopList.get(k).getId());
                            myThread.setprices(shopList.get(k).getMyprice());
                            Thread thread = new Thread(myThread);
                            thread.start();
                        }
                        break;
                    }
                    try {
                        sleep(600000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        //———————————————时间线程定义————————————————————
        class MyThread4 implements Runnable {
            public void run() {
                while (true) {
                    String time = df.format(new Date());
                    String time2 = df2.format(new Date());
                    if ("08:00".equals(time2) || "15:00".equals(time2)) {
                        if (timeflag) {
                            try {
                                new Mail().sendmail(runnum, Dataout.getDistanceTime(starttime1, time));
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            } catch (GeneralSecurityException e) {
                                e.printStackTrace();
                            }
                            timeflag = false;
                        }
                    } else {
                        timeflag = true;

                    }
                }
            }

        }
        //---------------线程启动---------------------------
        if ("open".equals(text)) {
            flag = false;
            type = "操作完成，结果如下";
            for (int i = 0; i < num; i++) {
                shopList.get(i).setFlag(false);
                shopList.get(i).setType("运行中");
            }
            for (int k = 0; k < num; k++) {
                MyThread2 myThread = new MyThread2();
                myThread.setName(shopList.get(k).getId());
                myThread.setprices(shopList.get(k).getMyprice());
                Thread thread = new Thread(myThread);
                thread.start();
            }
            req.setAttribute("name", type);
            req.setAttribute("myprace", shopList);
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
        if ("addd".equals(text)) {
            if (timeflag1) {
                MyThread4 myThread1 = new MyThread4();
                Thread thread1 = new Thread(myThread1);
                thread1.start();
                timeflag1 = false;
            }
            if (shopid != "" && prace != "" && email != "" && isNumeric(shopid) && isemaill(email) && isNumeric(prace)) {
                for (int l = 0; l < shopList.size(); l++) {
                    if (shopList.get(l).getId().equals(shopid)&&shopList.get(l).getEmail().equals(email)) {
                        flag2 = false;
                        break;
                    } else flag2 = true;
                }
            if (flag2){
            if(Commodity.price(Spider.SendGet("http://p.3.cn/prices/get?skuid=J_"+shopid)).equals("error")){
                flag2 = false;
            }
            }
                if (flag2) {
                    num++;
                    flag = false;
                    ss.setId(shopid);
                    ss.setMyprice(prace);
                    ss.setEmail(email);
                    ss.setFlag(false);
                    MyThread2 myThread = new MyThread2();
                    myThread.setName(ss.getId());
                    myThread.setprices(ss.getMyprice());
                    Thread thread = new Thread(myThread);
                    thread.start();
                    type = "添加成功，请通过输入邮箱查询结果！";
                    ss.setType("运行中");
                    shopList.add(ss);
                    req.setAttribute("name", type);
                    //根据mail找出所属的进程
                    List<Shop> shopList1 = new ArrayList<Shop>();
                    for (int i = 0; i < num; i++) {
                        if (shopList.get(i).getEmail().equals(email)) {
                            shopList1.add(shopList.get(i));
                        }
                    }
                    req.setAttribute("myprace", shopList1);
                    req.getRequestDispatcher("index.jsp").forward(req, resp);
                } else {
                    type = "该id已经包含在列表中或者该id不是个有效的id！请检查！";
                    req.setAttribute("name", type);
                    req.getRequestDispatcher("index.jsp").forward(req, resp);
                }
            } else {
                type = "请输入正确的商品ID和预期价格以及邮箱！";
                req.setAttribute("name", type);
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            }
        }
        if ("emaill".equals(text)) {
            List<Shop> shopList1 = new ArrayList<Shop>();
            String time = df.format(new Date());
            if (email != "" && isemaill(email)) {
                for (int i = 0; i < num; i++) {
                    if (shopList.get(i).getEmail().equals(email)) {
                        shopList1.add(shopList.get(i));
                    }
                }
                type = "当前已运行次数为:";
                req.setAttribute("num", runnum+",服务器已运行"+Dataout.getDistanceTime(starttime1, time));
                req.setAttribute("name", type);
                req.setAttribute("myprace", shopList1);
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            } else {
                type = "请输入正确的邮箱进行查询！";
                req.setAttribute("name", type);
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            }
        }
        if ("dellll".equals(text)) {
            flag = true;
            boolean a = false;
            List<Shop> shopList1 = new ArrayList<Shop>();
            if (email != "" && shopid != "" && isNumeric(shopid) && isemaill(email)) {
                for (int i = 0; i < num; i++) {
                    if (shopList.get(i).getId().equals(shopid) && shopList.get(i).getEmail().equals(email)) {
                        shopList.remove(i);
                        i--;
                        num--;
                        a = true;
                    }
                }
                for (int k = 0; k < num; k++) {
                    if (shopList.get(k).getEmail().equals(email)) {
                        shopList1.add(shopList.get(k));
                    }
                }
                if (a) {

                    flag = false;
                    MyThread3 myThread = new MyThread3();
                    Thread thread = new Thread(myThread);
                    thread.start();
                    type = "删除成功！";
                    req.setAttribute("name", type);
                    req.setAttribute("myprace", shopList1);
                    req.getRequestDispatcher("index.jsp").forward(req, resp);
                } else {
                    type = "删除失败，请确定商品ID和Email是否保持一致！";
                    req.setAttribute("name", type);
                    req.setAttribute("myprace", shopList1);
                    req.getRequestDispatcher("index.jsp").forward(req, resp);
                }
            } else {
                flag = false;
                type = "请输入正确的要删除的商品ID和所属邮箱进行删除！";
                req.setAttribute("name", type);
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            }
        }

    }

}

