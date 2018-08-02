package zig.i.carry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zig.i.carry.model.Ad;
import zig.i.carry.model.OfferAd;
import zig.i.carry.model.User;
import zig.i.carry.repo.AdRepo;
import zig.i.carry.repo.ContactRepo;
import zig.i.carry.repo.UserRepo;

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
    private static final String OK_EMAIL = "ok_email";
    private static final String ERROR_EMAIL = "error_email";
    private static final String VERIFY = "/verify";
    private static final String REGISTER = "/register";
    private static final String REMIND = "/remind";
    private static final String REMIND_ERROR = "REMIND_ERROR";
    private static final String PASSWORD_SENT = "PASSWORD_SENT";
    private static final String NOT_REGISTERED = "NOT_REGISTERED";
    private static final String SIGN_IN = "/sign_in";
    private static final String PUBLISH = "/publish";
    private static final String OFFERS = "/offers";
    private Map<String, Integer> map = new HashMap<>();
    private final UserRepo uRepo;
    private final ContactRepo contactRepo;
    private final AdRepo adRepo;

    @Autowired
    public Controller(UserRepo userRepo, ContactRepo contactRepo, AdRepo adRepo) {
        this.uRepo = userRepo;
        this.contactRepo = contactRepo;
        this.adRepo = adRepo;
    }

    @RequestMapping(value = VALIDATE, method = RequestMethod.POST)
    private String validate(@RequestBody String email) {
        return sendVerificationCode(email);
    }

    @RequestMapping(value = VERIFY, method = RequestMethod.POST)
    private String verify(@RequestBody String emailAndCode) {
        String substring = emailAndCode.substring(1, emailAndCode.length() - 1);
        String[] strings = substring.split(":");
        System.out.println(VERIFY);
        return map.remove(strings[0]) == null ? "code_incorrect" : "code_ok";
    }

    @RequestMapping(value = REGISTER, method = RequestMethod.POST)
    private String register(@RequestBody String params) {
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
        return "OK";
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
        System.out.println("sign in");
        boolean exists = uRepo.exists(Example.of(new User(paramArr[0], paramArr[1])));
        System.out.println("user exists=>" + exists);
        return exists;
    }

    @RequestMapping(value = PUBLISH, method = RequestMethod.POST)
    private boolean publish(@RequestBody Ad ad) {
        System.out.println("ad=>" + ad.toString());
        long t1 = System.currentTimeMillis();
        adRepo.save(ad);
        System.out.println((System.currentTimeMillis() - t1));
        return true;
    }

    @RequestMapping(value = OFFERS, method = RequestMethod.GET)
    private List<Ad> getOffers() {
        List<Ad> all = adRepo.findAll(Example.of(new OfferAd()));
        System.out.println(all);
        return all;
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

    private String sendVerificationCode(String email) {
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
            return ERROR_EMAIL;
        }
        map.put(email.substring(1, email.length() - 1), i);
        System.out.println("email is sent");
        return OK_EMAIL;
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
}
