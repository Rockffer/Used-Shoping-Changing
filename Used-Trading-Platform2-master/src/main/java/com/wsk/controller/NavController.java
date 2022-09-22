package com.wsk.controller;

import com.wsk.bean.UserWantBean;
import com.wsk.bean.WantContentBean;
import com.wsk.pojo.ShopInformation;
import com.wsk.pojo.UserInformation;
import com.wsk.pojo.UserWant;
import com.wsk.pojo.WantContext;
import com.wsk.service.*;
import com.wsk.token.TokenProccessor;
import com.wsk.tool.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class NavController {

    @Resource
    private ShopInformationService shopInformationService;
    @Resource
    private ShopContextService shopContextService;
    @Resource
    private UserInformationService userInformationService;
    @Resource
    private SpecificeService specificeService;
    @Resource
    private ClassificationService classificationService;
    @Resource
    private AllKindsService allKindsService;
    @Resource
    private UserWantService userWantService;
    @Resource
    private  WantContextService wantContextService;

    @RequestMapping(value = "/comment.do")
    public String comment(HttpServletRequest request, Model model) {
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (StringUtils.getInstance().isNullOrEmpty(userInformation)) {
            userInformation = new UserInformation();
            model.addAttribute("userInformation", userInformation);
        }

        List<WantContentBean>wantContentBeans=new ArrayList<>();
        List<WantContext> wantContexts=wantContextService.selectAll();
        for(WantContext w:wantContexts){
            UserInformation u=userInformationService.selectByPrimaryKey(w.getUwid());
            WantContentBean wantContentBean=new WantContentBean();
            wantContentBean.setName(u.getUsername());
            wantContentBean.setId(w.getId());
            wantContentBean.setUid(u.getId());
            wantContentBean.setContent(w.getContext());
            wantContentBean.setModified(w.getModified());
            wantContentBeans.add(wantContentBean);
        }

        String userToken = TokenProccessor.getInstance().makeToken();
        request.getSession().setAttribute("userToken", userToken);
        System.out.println(userToken);
        model.addAttribute("token", userToken);
        model.addAttribute("userInformation",userInformation);
        model.addAttribute("wantContentBeans",wantContentBeans);
        return "page/comment";
    }

    @RequestMapping(value="/insertUserContext.do")
    @ResponseBody
    public Map insertUserContext(HttpServletRequest request,@RequestParam String context,@RequestParam String token){
        String userToken=(String) request.getSession().getAttribute("userToken");
        Map<String,String>map=new HashMap<>();
        map.put("result","1");
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (StringUtils.getInstance().isNullOrEmpty(userInformation)) {
            map.put("result", "2");
            return map;
        }
        if (StringUtils.getInstance().isNullOrEmpty(userToken) || !token.equals(userToken)) {
            return map;
        }

        WantContext wantContext=new WantContext();
        wantContext.setContext(context);
        Date date=new Date();
        wantContext.setModified(date);
        int uid = (int) request.getSession().getAttribute("uid");
        wantContext.setUwid(uid);
        wantContext.setDisplay(1);
        int result=wantContextService.insert(wantContext);
        if (result != 1) {
            return map;
        }


        map.put("result", "1");
        map.put("context",context);
        map.put("username", userInformation.getUsername());
        map.put("time", StringUtils.getInstance().DateToString(date));
        return map;
    }


    @RequestMapping(value = "/contact.do")
    public String contact(HttpServletRequest request, Model model){
        UserInformation userInformation = (UserInformation) request.getSession().getAttribute("userInformation");
        if (StringUtils.getInstance().isNullOrEmpty(userInformation)) {
            userInformation = new UserInformation();
            model.addAttribute("userInformation", userInformation);
        }
        model.addAttribute("userInformation", userInformation);
        return "page/contact";
    }
}
