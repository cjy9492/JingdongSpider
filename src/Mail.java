import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;


public class Mail {


        public void sendmail(String address,String[] a,String email)throws MessagingException, GeneralSecurityException{
            Properties props = new Properties();

            // 开启debug调试
            props.setProperty("mail.debug", "true");
            // 发送服务器需要身份验证
            props.setProperty("mail.smtp.auth", "true");
            // 设置邮件服务器主机名
            props.setProperty("mail.host", "smtp.qq.com");
            // 发送邮件协议名称
            props.setProperty("mail.transport.protocol", "smtp");

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);

            Session session = Session.getInstance(props);

            Message msg = new MimeMessage(session);
            try {
                msg.setSubject(MimeUtility.encodeText("降价通知",MimeUtility.mimeCharset("gb2312"), null));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            StringBuilder builder = new StringBuilder();
            builder.append("商品："+a[0]);
            builder.append("url = " + "http://item.jd.com/"+address+".html");
            builder.append("\n已经降价！");
            builder.append("目前价格："+a[1]);
            msg.setContent(builder.toString(), "text/html;charset=UTF-8");
            msg.setFrom(new InternetAddress("你的邮箱地址"));

            Transport transport = session.getTransport();
            transport.connect("smtp.qq.com", "你的邮箱地址", "你的邮箱密码");

            transport.sendMessage(msg, new Address[] { new InternetAddress(email) });
            transport.close();
        }
    public void sendmail(int a,String b)throws MessagingException, GeneralSecurityException{
        Properties props = new Properties();

        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");
        //ssl服务器
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getInstance(props);

        Message msg = new MimeMessage(session);
        try {
            msg.setSubject(MimeUtility.encodeText("程序运行情况通知",MimeUtility.mimeCharset("gb2312"), null));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder builder = new StringBuilder();
        builder.append("系统已经运行"+a+"次!");
        builder.append("\n系统已经运行"+b);
        msg.setContent(builder.toString(), "text/html;charset=UTF-8");
        msg.setFrom(new InternetAddress("你的邮箱地址"));

        Transport transport = session.getTransport();
        transport.connect("smtp.qq.com", "你的邮箱地址", "你的邮箱密码");

        transport.sendMessage(msg, new Address[] { new InternetAddress("你的邮箱地址") });
        transport.close();
    }
}