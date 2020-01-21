package com.example.neiwai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@SpringBootApplication
public class NeiwaiApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(NeiwaiApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(NeiwaiApplication.class, args);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(NeiwaiApplication.class);

    @GetMapping("test")
    public String judgeIp(HttpServletRequest request) {
        return isOutside(request) + "aaaaaa";
    }

    @GetMapping("testHttpsRedirect")
    public static ModelAndView testHttpsRedirect(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/test");
        return mv;
    }

    private Boolean isOutside(HttpServletRequest request) {
        Boolean result = false;
        // 获取客户端IP地址，考虑反向代理的问题
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!StringUtils.isEmpty(ip) && ip.length() > 15) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        System.out.println(ip);
        /*
         *  判断客户单IP地址是否为内网地址
         *  内网IP网段：
         *  10.0.0.0-10.255.255.255
         *  172.16.0.0-172.31.255.255
         *  192.168.0.0-192.168.255.255
         */
        String reg = "^(192\\.168|172\\.(1[6-9]|2\\d|3[0,1]))(\\.(2[0-4]\\d|25[0-5]|[0,1]?\\d?\\d)){2}$|^10(\\.([2][0-4]\\d|25[0-5]|[0,1]?\\d?\\d)){3}$";
        Pattern p = Pattern.compile(reg);
        Matcher matcher = p.matcher(ip);
        result = matcher.find();

        return !result;
    }

    @GetMapping("testE")
    public static void testException() {
        int i = 10;

        System.out.println( 100 / i);

//        try {
//            InputStream is = new FileInputStream(new File("xxx.xxx"));
//        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        LOGGER.error("DDDDDDDDD.XXXXXXXXXXXXX");

    }

}
