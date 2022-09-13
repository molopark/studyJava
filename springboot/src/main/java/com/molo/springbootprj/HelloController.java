package com.molo.springbootprj;

import java.net.http.HttpRequest;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpServletRequest;

import com.molo.springbootprj.dbsample.mapper.SampleMapper;
import com.molo.springbootprj.dbsample.model.UserProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class HelloController {
    
    @Autowired SampleMapper sampleMapper;
    
    @RequestMapping("/")
    public String index(@RequestParam(name="name", required=false, defaultValue = "World") String name, Model model, HttpServletRequest req) {
        model.addAttribute("name", name);
        model.addAttribute("dbdata", sampleMapper.selectSampleData());

        // System.out.println("call service");
        // RestTemplate restTemplate = new RestTemplate();
        // UserProfile u = restTemplate.postForObject("http://localhost:8080/local/user/1", req, UserProfile.class);
        // System.out.print("u:"+u.toString());

        return "index";
    }
}
