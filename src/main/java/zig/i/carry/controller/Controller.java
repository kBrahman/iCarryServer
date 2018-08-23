package zig.i.carry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zig.i.carry.model.Ad;
import zig.i.carry.model.OfferAd;
import zig.i.carry.model.OrderAd;
import zig.i.carry.model.User;
import zig.i.carry.repo.AdRepo;
import zig.i.carry.repo.UserRepo;
import zig.i.carry.sms.NeutrinoAPIClient;
import zig.i.carry.sms.SMSVerifyResponse;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

@RestController
public class Controller {

    private static final String VALIDATE = "/validate";
    private static final String YANDEX_COM = "smtp.yandex.com";
    private static final String VERIFY = "/verify";
    private static final String REGISTER = "/register";
    private static final String REMIND = "/remind";
    private static final String REMIND_ERROR = "REMIND_ERROR";
    private static final String PASSWORD_SENT = "PASSWORD_SENT";
    private static final String NOT_REGISTERED = "NOT_REGISTERED";
    private static final String SIGN_IN = "/sign_in";
    private static final String PUBLISH = "/publish";
    private static final String OFFERS = "/offers";
    private static final String ORDERS = "/orders";
    private static final String MY_ADS = "/my-ads";
    private static final String DELETE = "/delete";
    private Map<String, Integer> map = new HashMap<>();
    private final UserRepo uRepo;
    private final AdRepo adRepo;

    @Value("${user-id}")
    private String userId;

    @Value("${api-key}")
    private String apiKey;

    @Autowired
    public Controller(UserRepo userRepo, AdRepo adRepo) {
        this.uRepo = userRepo;
        this.adRepo = adRepo;
    }

    @RequestMapping(value = VALIDATE, method = RequestMethod.POST)
    private boolean validate(@RequestBody String login) {
        login = login.substring(1, login.length() - 1);
        System.out.println("validate login=>" + login);
        if (login.matches("\\+?[0-9]{10,13}")) {
            System.out.println("matches");
            boolean sms = sendVerificationSMS(login);
            return sms;
        }
        System.out.println("sending email");
        return sendVerificationEmail(login);
    }

    @RequestMapping(value = VERIFY, method = RequestMethod.POST)
    private boolean verify(@RequestBody String emailAndCode) {
        String substring = emailAndCode.substring(1, emailAndCode.length() - 1);
        String[] strings = substring.split(":");
        System.out.println(VERIFY);
        return map.remove(strings[0]) != null;
    }

    @RequestMapping(value = REGISTER, method = RequestMethod.POST)
    private boolean register(@RequestBody String params) {
        String[] paramArr = params.substring(1, params.length() - 1).split(":");
        User one = uRepo.findOne(Example.of(new User(paramArr[2])));
        if (one != null) {
            one.setName(paramArr[0]);
            one.setPwd(paramArr[1]);
        } else {
            one = new User(paramArr[0], paramArr[1], paramArr[2]);
        }
        uRepo.save(one);
        System.out.println("registered");
        return true;
    }

    @RequestMapping(value = REMIND, method = RequestMethod.POST)
    private String remind(@RequestBody String login) {
        User one = uRepo.findOne(Example.of(new User(login.substring(1, login.length() - 1))));
        if (one != null) {
            return sendPwd(login, one.getPwd());
        }
        return NOT_REGISTERED;
    }

    @RequestMapping(value = SIGN_IN, method = RequestMethod.POST)
    private boolean signIn(@RequestBody String loginPwd) {
        loginPwd = loginPwd.substring(1, loginPwd.length() - 1);
        String[] paramArr = loginPwd.split(":");
        return uRepo.exists(Example.of(new User(paramArr[0], paramArr[1])));
    }

    @RequestMapping(value = PUBLISH, method = RequestMethod.POST)
    private boolean publish(@RequestBody Ad ad) {
        System.out.println(ad);
        adRepo.save(ad);
        return true;
    }

    @RequestMapping(value = OFFERS, method = RequestMethod.GET)
    private List<Ad> getOffers() {
        return adRepo.findAll(Example.of(new OfferAd()), new Sort(Sort.Direction.DESC, "id"));
    }

    @RequestMapping(value = ORDERS, method = RequestMethod.GET)
    private List<Ad> getOrders() {
        return adRepo.findAll(Example.of(new OrderAd()), new Sort(Sort.Direction.DESC, "id"));
    }

    @RequestMapping(value = MY_ADS, method = RequestMethod.POST)
    private List<Ad> getMyAds(@RequestBody String login) {
        System.out.println("login=>" + login);
        List<Ad> ads = adRepo.getAdsByUserLogin(login.substring(1, login.length() - 1));
        System.out.println(ads);
        return ads;
    }

    @RequestMapping(value = DELETE, method = RequestMethod.POST)
    private boolean delete(@RequestBody Ad ad) {
        System.out.println("delete=>" + ad);
        adRepo.delete(ad);
        return true;
    }


    private String sendPwd(String login, String pwd) {
        try {
            Message message = createMsg(login);
            message.setSubject("PASSWORD");
            message.setText(pwd);
            Transport smtp = message.getSession().getTransport("smtp");
            smtp.connect("dev.zig.dev@yandex.ru", "9266877411");
            smtp.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException e) {
            e.printStackTrace();
            return REMIND_ERROR;
        }
        return PASSWORD_SENT;
    }

    private boolean sendVerificationEmail(String email) {
        int i;
        try {
            Message message = createMsg(email);
            message.setSubject("VERIFICATION");
            Random r = new Random();
            i = r.nextInt(1001);
            String randomNumber = String.format("%04d", i);
            message.setText(randomNumber);
            Transport smtp = message.getSession().getTransport("smtp");
            smtp.connect("dev.zig.dev@yandex.ru", "9266877411");
            smtp.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        map.put(email, i);
        System.out.println("email is sent");
        return true;
    }

    private Message createMsg(String email) throws MessagingException {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", YANDEX_COM);
        properties.put("mail.smtp.password", "9266877411");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("dev.zig.dev@yandex.ru"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        return message;
    }


    private boolean sendVerificationSMS(String login) {
        login = correct(login);

        NeutrinoAPIClient client = new NeutrinoAPIClient(userId, apiKey);
        System.out.println(userId + ";" + apiKey);
        try {
            SMSVerifyResponse response = client.getTelephony().sMSVerify(login, 4, null,
                    null, null);
            System.out.println(response.getSecurityCode());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return true;
    }

    private String correct(String login) {
        if (login.startsWith("8")) {
            login = login.replaceFirst("8", "+7");
        }
        return login;
    }
}
