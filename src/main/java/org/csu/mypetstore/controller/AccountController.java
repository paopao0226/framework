package org.csu.mypetstore.controller;

import com.google.code.kaptcha.Producer;
import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.service.AccountService;
import org.csu.mypetstore.service.CatalogService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/account")
@SessionAttributes(value = {"isLogin","myAccount","password","languageList","categoryList","mylanguagePreference","myfavouriteCategoryId","myListOpt","myBannerOpt","checkCode","kaptcha"})
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private CatalogService catalogService;
    //老验证码的设置
    private static final int widthInt = 120;
    private static final int heightInt = 30;
    private static final int wordsNumberInt = 4;
    private static final int lineNumberInt = 5;
    private static final long serialVersionUID = 3038623696184546092L;
    //新验证码的设置
//    @Autowired
//    private Producer kaptchaProducer;
    private Account loginAccout = new Account();
    //验证码
    private String verifiCode = null;
//    //判断验证码是否实现匹配
//    private boolean verifyIsFinished = true;
    @GetMapping("/signon")//从main到sign
    public String signon(Model model) {
        //注册与编辑用户时两个列表的初始化
        List<String> languageList = new ArrayList<>();
        List<String> categoryList = new ArrayList<>();
        languageList.add("english");
        languageList.add("japanese");
        categoryList.add("FISH");
        categoryList.add("DOGS");
        categoryList.add("REPTILES");
        categoryList.add("CATS");
        categoryList.add("BIRDS");
        model.addAttribute("languageList",languageList);
        model.addAttribute("categoryList",categoryList);
        return "account/SignonForm";
    }
    @PostMapping("/sign")//登录
    public String sign(String username, String password, String verifi, Model model){
        //将输入的密码进行加密，并在之后与数据库中的加密密码进行比对
        password = KL(password);
        //验证码的验证
        Account account = accountService.getAccount(username,password);
        if(!verifiCode.equals(verifi)){
//            verifyIsFinished = false;
            System.out.println(verifi +" " + verifiCode);
            String value = "<ui><li>Failed Verification</li></ui>";
            model.addAttribute("loginMessage",value);
            return "account/signonForm";
        }
        //密码匹配
        else if(account == null){
            String value = "<ui><li>Invalid username or password. Sign on failed</li></ui>";
            model.addAttribute("loginMessage",value);
            return "account/signonForm";
        }
        else {
            //设置相关参数
            boolean isLogin = true;
//            verifyIsFinished = true;
            loginAccout = account;
            model.addAttribute("loginMessage","");
            //把下面的一些参数存到session里面，便于之后的取出
            model.addAttribute("isLogin",isLogin);
            model.addAttribute("myAccount",account);
            model.addAttribute("mylanguagePreference",account.getLanguagePreference());
            model.addAttribute("myfavouriteCategoryId",account.getFavouriteCategoryId());
            model.addAttribute("myListOpt",account.isListOption());
            model.addAttribute("myBannerOpt",account.isBannerOption());
            model.addAttribute("password",password);
            return "catalog/main";
        }
    }
    @GetMapping("/newAccountForm")
    public String newAccountForm(Model model){
        return "account/NewAccountForm";
    }
    @PostMapping("/newAccount")//注册
    public String newAcount(String password,String repeatedPassword,String verifi,Account account,Model model) {
        //验证码的验证功能
        if(!verifiCode.equals(verifi)){
//            verifyIsFinished = false;
            System.out.println(verifi +" " + verifiCode);
            String value = "<ui><li>Failed Verification</li></ui>";
            model.addAttribute("registerMessage",value);
            return "account/NewAccountForm";
        }
        System.out.println(password);
        System.out.println(account.getFirstName().getClass());
        //密码匹配
        if (!password.equals(repeatedPassword)) {
            String value = "<ui><li>Register failed,Please check the password and repeatPassword</li></ui>";
            model.addAttribute("registerMessage", value);
            return "account/NewAccountForm";
        }
        else {
            //判断输入信息是否有误或者有缺
            if (account.getFirstName().equals("") || account.getLastName().equals("") || account.getAddress1().equals("")
                    || account.getCity().equals("") || account.getState().equals("") || account.getZip().equals("")
                    || account.getCountry().equals("") || account.getPhone().equals("") ) {
                String value = "<ui><li>Information missed. Please check the information</li></ui>";
                model.addAttribute("username",account.getUsername());
                model.addAttribute("password", account.getPassword());
                model.addAttribute("repeatedPassword", account.getPassword());
                model.addAttribute("registerMessage", value);
                return "account/NewAccountForm";
            }
            else {
                account.setPassword(KL(account.getPassword()));
                accountService.insertAccount(account);
                model.addAttribute("username", account.getUsername());
                return "account/signonForm";
            }
        }
    }
    @GetMapping("/editAccountForm")//从main到编辑界面
    public String editAccountForm(Model model){
        return "account/EditAccountForm";
    }
    @PostMapping("/editAccount")//编辑
    public String editAccount(String password,String repeatedPassword,Account account,Model model){
        if(!password.equals(repeatedPassword)){
            String value = "<ui><li>Edit failed,Please check the password and repeatPassword</li></ui>";
            model.addAttribute("editMessage",value);
            return "account/EditAccountForm";
        }
        else{
            if(account.getFirstName().equals("") || account.getLastName().equals("") || account.getAddress1().equals("")
                    || account.getCity().equals("") || account.getState().equals("") || account.getZip().equals("")
                    || account.getCountry().equals("") || account.getPhone().equals("")){
                String value = "<ui><li>Information missed. Please check the information</li></ui>";
                model.addAttribute("username",account.getUsername());
                model.addAttribute("password", account.getPassword());
                model.addAttribute("repeatedPassword", account.getPassword());
                model.addAttribute("editMessage", value);
                return "account/EditAccountForm";
            }
            else {
                account.setUsername(loginAccout.getUsername());
                account.setPassword(KL(password));
                accountService.updateAccount(account);
                model.addAttribute("username", account.getUsername());
                return "catalog/main";
            }
        }
    }
    @GetMapping("/signout")//登出，将session中有关用户的信息全部初始化
    public String signout(Model model){
        Account account = new Account();
        model.addAttribute("loginMessage","");
        model.addAttribute("isLogin",false);
        model.addAttribute("myAccount",account);
        model.addAttribute("mylanguagePreference","");
        model.addAttribute("myfavouriteCategoryId","");
        model.addAttribute("myListOpt","");
        model.addAttribute("myBannerOpt","");
        model.addAttribute("password","");
        return "catalog/main";
    }
    @GetMapping("/bufferImage")//验证码
    public void buffer(HttpServletResponse response, Model model) {
        //使用Kaptcha生成验证码
        // 生成验证码
//        String text = kaptchaProducer.createText();
//        BufferedImage image = kaptchaProducer.createImage(text);
//        // 将验证码存入session
//        model.addAttribute("kaptcha", text);
//        // 将突图片输出给浏览器
//        response.setContentType("image/png");
//        try {
//            OutputStream os = response.getOutputStream();
//            ImageIO.write(image, "png", os);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //之前的验证码代码实现
        //设置响应头控制浏览器每隔3s刷新页面
        response.setHeader("refresh", "2");
        //设置一个数组存储随机码格式
        String[] format = {"ch", "n", "l", "nl"};
        int randomInt = (int)(Math.random() * 4);
        String createTypeFlag = format[randomInt];
        System.out.println(createTypeFlag);
        //在内存中创建一张图片
        BufferedImage image = new BufferedImage(widthInt, heightInt, BufferedImage.TYPE_3BYTE_BGR);
        //获得图片
        Graphics graphics = image.getGraphics();
        //设置图片的背景色
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, widthInt, heightInt);
        //设置边框颜色
        graphics.setColor(Color.blue);
        graphics.drawRect(1, 1, widthInt - 2, heightInt - 2);
        //画干扰线
        graphics.setColor(Color.green);
        for (int i = 0; i < lineNumberInt; i++) {
            int x1 = new Random().nextInt(widthInt);
            int y1 = new Random().nextInt(heightInt);
            int x2 = new Random().nextInt(widthInt);
            int y2 = new Random().nextInt(heightInt);
            graphics.drawLine(x1, y1, x2, y2);
        }
        //在图片上放上随机字符
//        if(verifyIsFinished) {
            verifiCode = drawRandomNum((Graphics2D) graphics, createTypeFlag);
//        }
//        else{
//            String temp = createRandomChar((Graphics2D) graphics, verifiCode);
//        }
        //设置响应头通知浏览器以图片的形式打开
        response.setContentType("image/jpeg");
        //设置响应头控制浏览器不要缓存
        response.setDateHeader("expries", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        //将图片传给浏览器
        try {
            ImageIO.write(image, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            System.out.println("将内存中的图片通过流动形式输出到客户端失败>>>>" + e.getStackTrace());
        }
    }
    public String drawRandomNum(Graphics2D graphics,String createTypeFlag){
        //常用的中国汉字
        String baseChineseChar = "\u7684\u4e00\u4e86\u662f\u6211\u4e0d\u5728\u4eba\u4eec\u6709\u6765\u4ed6\u8fd9\u4e0a\u7740\u4e2a\u5730\u5230\u5927\u91cc\u8bf4\u5c31\u53bb\u5b50\u5f97\u4e5f\u548c\u90a3\u8981\u4e0b\u770b\u5929\u65f6\u8fc7\u51fa\u5c0f\u4e48\u8d77\u4f60\u90fd\u628a\u597d\u8fd8\u591a\u6ca1\u4e3a\u53c8\u53ef\u5bb6\u5b66\u53ea\u4ee5\u4e3b\u4f1a\u6837\u5e74\u60f3\u751f\u540c\u8001\u4e2d\u5341\u4ece\u81ea\u9762\u524d\u5934\u9053\u5b83\u540e\u7136\u8d70\u5f88\u50cf\u89c1\u4e24\u7528\u5979\u56fd\u52a8\u8fdb\u6210\u56de\u4ec0\u8fb9\u4f5c\u5bf9\u5f00\u800c\u5df1\u4e9b\u73b0\u5c71\u6c11\u5019\u7ecf\u53d1\u5de5\u5411\u4e8b\u547d\u7ed9\u957f\u6c34\u51e0\u4e49\u4e09\u58f0\u4e8e\u9ad8\u624b\u77e5\u7406\u773c\u5fd7\u70b9\u5fc3\u6218\u4e8c\u95ee\u4f46\u8eab\u65b9\u5b9e\u5403\u505a\u53eb\u5f53\u4f4f\u542c\u9769\u6253\u5462\u771f\u5168\u624d\u56db\u5df2\u6240\u654c\u4e4b\u6700\u5149\u4ea7\u60c5\u8def\u5206\u603b\u6761\u767d\u8bdd\u4e1c\u5e2d\u6b21\u4eb2\u5982\u88ab\u82b1\u53e3\u653e\u513f\u5e38\u6c14\u4e94\u7b2c\u4f7f\u5199\u519b\u5427\u6587\u8fd0\u518d\u679c\u600e\u5b9a\u8bb8\u5feb\u660e\u884c\u56e0\u522b\u98de\u5916\u6811\u7269\u6d3b\u90e8\u95e8\u65e0\u5f80\u8239\u671b\u65b0\u5e26\u961f\u5148\u529b\u5b8c\u5374\u7ad9\u4ee3\u5458\u673a\u66f4\u4e5d\u60a8\u6bcf\u98ce\u7ea7\u8ddf\u7b11\u554a\u5b69\u4e07\u5c11\u76f4\u610f\u591c\u6bd4\u9636\u8fde\u8f66\u91cd\u4fbf\u6597\u9a6c\u54ea\u5316\u592a\u6307\u53d8\u793e\u4f3c\u58eb\u8005\u5e72\u77f3\u6ee1\u65e5\u51b3\u767e\u539f\u62ff\u7fa4\u7a76\u5404\u516d\u672c\u601d\u89e3\u7acb\u6cb3\u6751\u516b\u96be\u65e9\u8bba\u5417\u6839\u5171\u8ba9\u76f8\u7814\u4eca\u5176\u4e66\u5750\u63a5\u5e94\u5173\u4fe1\u89c9\u6b65\u53cd\u5904\u8bb0\u5c06\u5343\u627e\u4e89\u9886\u6216\u5e08\u7ed3\u5757\u8dd1\u8c01\u8349\u8d8a\u5b57\u52a0\u811a\u7d27\u7231\u7b49\u4e60\u9635\u6015\u6708\u9752\u534a\u706b\u6cd5\u9898\u5efa\u8d76\u4f4d\u5531\u6d77\u4e03\u5973\u4efb\u4ef6\u611f\u51c6\u5f20\u56e2\u5c4b\u79bb\u8272\u8138\u7247\u79d1\u5012\u775b\u5229\u4e16\u521a\u4e14\u7531\u9001\u5207\u661f\u5bfc\u665a\u8868\u591f\u6574\u8ba4\u54cd\u96ea\u6d41\u672a\u573a\u8be5\u5e76\u5e95\u6df1\u523b\u5e73\u4f1f\u5fd9\u63d0\u786e\u8fd1\u4eae\u8f7b\u8bb2\u519c\u53e4\u9ed1\u544a\u754c\u62c9\u540d\u5440\u571f\u6e05\u9633\u7167\u529e\u53f2\u6539\u5386\u8f6c\u753b\u9020\u5634\u6b64\u6cbb\u5317\u5fc5\u670d\u96e8\u7a7f\u5185\u8bc6\u9a8c\u4f20\u4e1a\u83dc\u722c\u7761\u5174\u5f62\u91cf\u54b1\u89c2\u82e6\u4f53\u4f17\u901a\u51b2\u5408\u7834\u53cb\u5ea6\u672f\u996d\u516c\u65c1\u623f\u6781\u5357\u67aa\u8bfb\u6c99\u5c81\u7ebf\u91ce\u575a\u7a7a\u6536\u7b97\u81f3\u653f\u57ce\u52b3\u843d\u94b1\u7279\u56f4\u5f1f\u80dc\u6559\u70ed\u5c55\u5305\u6b4c\u7c7b\u6e10\u5f3a\u6570\u4e61\u547c\u6027\u97f3\u7b54\u54e5\u9645\u65e7\u795e\u5ea7\u7ae0\u5e2e\u5566\u53d7\u7cfb\u4ee4\u8df3\u975e\u4f55\u725b\u53d6\u5165\u5cb8\u6562\u6389\u5ffd\u79cd\u88c5\u9876\u6025\u6797\u505c\u606f\u53e5\u533a\u8863\u822c\u62a5\u53f6\u538b\u6162\u53d4\u80cc\u7ec6";
        //数字和字母的组合
        String baseNumLetter = "0123456789ABCDEFGHJKLMNOPQRSTUVWXYZ";
        //纯数字
        String baseNum = "0123456789";
        //纯字母
        String baseLetter = "ABCDEFGHJKLMNOPQRSTUVWXYZ";
        System.out.println(createTypeFlag);
        if(!createTypeFlag.equals(null)){
            if(createTypeFlag.equals("ch")){
                return createRandomChar(graphics,baseChineseChar);
            }
            else if(createTypeFlag.equals("nl")){
                return createRandomChar(graphics,baseNumLetter);
            }
            else if(createTypeFlag.equals("n")) {
                return createRandomChar(graphics,baseNum);
            }
            else if(createTypeFlag.equals("l")) {
                return createRandomChar(graphics, baseLetter);
            }
        }
        return "";
    }
    public String createRandomChar(Graphics2D g,String baseChar){
        g.setColor(Color.red);
        g.setFont(new Font("黑体",Font.BOLD,20));
        StringBuffer sb = new StringBuffer();
        int x = 5;
        String ch = "";
        //控制字数
        for (int i = 0; i < wordsNumberInt; i++) {
            // 设置字体旋转角度
            int degree = new Random().nextInt() % 30;
//            if(verifyIsFinished) {
                ch = baseChar.charAt(new Random().nextInt(baseChar.length())) + "";
//            }
//            else {
//                ch = baseChar.charAt(i) + "";
//            }
            sb.append(ch);
            // 正向角度
            g.rotate(degree * Math.PI / 180, x, 20);
            g.drawString(ch, x, 20);
            // 反向角度
            g.rotate(-degree * Math.PI / 180, x, 20);
            x += 30;
        }
        return sb.toString();
    }
    @GetMapping("usernameIsExist")//判断是否存在用户，用于注册时在界面进行用户名是否存在的提示
    public void usernameIsExist(HttpServletResponse response, @RequestParam("username") String username){
        boolean result = accountService.usernameIsExist(username);
        response.setContentType("text/plain");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(result){
            out.print("Exist");
        }
        else {
            out.print("Not Exist");
        }
        //确保在缓冲区里面的数据能够每写一次就发出去一次
        out.flush();
        out.close();
    }
    //md5密码加密函数
    public String KL(String password){
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        System.out.println(password + " " + md5Password);
        return md5Password;
    }
}

