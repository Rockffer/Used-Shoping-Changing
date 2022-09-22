package com.wsk.controller.webSocket;

import com.wsk.bean.ShopInformationBean;
import com.wsk.bean.UserInformationBean;
import com.wsk.pojo.*;
import com.wsk.service.*;
import com.wsk.token.TokenProccessor;
import com.wsk.tool.SaveSession;
import com.wsk.tool.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class AdminController {
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
    @Resource
    private  AdminInformationService adminInformationService;
    //进入管理员登录界面
    @RequestMapping(value = "/admin_login.do", method = RequestMethod.GET)
    public String login(HttpServletRequest request, Model model) {
        String token = TokenProccessor.getInstance().makeToken();
        request.getSession().setAttribute("token", token);
        model.addAttribute("token", token);
        return "page/admin_login";
    }

    //验证登录
    @RequestMapping(value = "/admin_login.do", method = RequestMethod.POST)
    public String login(HttpServletRequest request,
                        @RequestParam String id, @RequestParam String password, @RequestParam String token) {
        String loginToken = (String) request.getSession().getAttribute("token");
        if (StringUtils.getInstance().isNullOrEmpty(id) || StringUtils.getInstance().isNullOrEmpty(password)) {
            return "redirect:/admin_login.do";
        }
        //防止重复提交
        if (StringUtils.getInstance().isNullOrEmpty(token) || !token.equals(loginToken)) {
            return "redirect:/admin_login.do";
        }
        boolean b = getId(id, password, request);
        //失败，不存在该手机号码
        if (!b) {
            return "redirect:/admin_login.do?msg=不存在该管理员ID";
        }
        return "redirect:/admin_index";//管理页面
    }


    private boolean getId(String id, String password, HttpServletRequest request) {
        AdminInformation adminInformation=adminInformationService.selectByPrimaryKey(Integer.valueOf(id));
        if(adminInformation==null){
            return false;
        }
        String password1 = adminInformation.getPassword();
        if(!password.equals(password1)){
            return false;
        }
        request.getSession().setAttribute("adminInformation",adminInformation );
        return true;
    }
    //退出
    @RequestMapping(value = "/admin_logout.do")
    public String logout(HttpServletRequest request) {
        try {
            request.getSession().removeAttribute("adminInformation");
            System.out.println("logout");
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/home.do";
        }
        return "redirect:/";
    }

    @RequestMapping(value = {"/admin_index"})
    public String home(HttpServletRequest request, Model model){
        AdminInformation adminInformation= (AdminInformation) request.getSession().getAttribute("adminInformation");
        System.out.println("admin:"+adminInformation.getAno());
        if(!StringUtils.getInstance().isNullOrEmpty(adminInformation)){
            model.addAttribute("adminInformation",adminInformation);
        }
        else {
            adminInformation=new AdminInformation();
            model.addAttribute("adminInformation",adminInformation);
            UserInformation userInformation = new UserInformation();
            model.addAttribute("userInformation", userInformation);
            return "index";
        }
        return "admin_index";
    }


    //管理页面

    //查看管理发布的商品
    @RequestMapping(value = "/admin_shop_manage")
    public String getReleasePublishShop(HttpServletRequest request, Model model) {
        AdminInformation adminInformation= (AdminInformation) request.getSession().getAttribute("adminInformation");
        System.out.println("admin:"+adminInformation.getAno()+"aaaaaaaaaa");
        if(!StringUtils.getInstance().isNullOrEmpty(adminInformation)){
            model.addAttribute("adminInformation",adminInformation);
        }
        else {
            adminInformation=new AdminInformation();
            model.addAttribute("adminInformation",adminInformation);
            UserInformation userInformation = new UserInformation();
            model.addAttribute("userInformation", userInformation);
            return "index";
        }

        List<ShopInformation> shopInformations = shopInformationService.selectAll();

        List<ShopInformationBean> list = new ArrayList<>();
        String stringBuffer;
        for (ShopInformation shopInformation : shopInformations) {
            if(shopInformation.getDisplay()==1){
                stringBuffer = getSort(shopInformation.getSort());
                ShopInformationBean shopInformationBean = new ShopInformationBean();
                shopInformationBean.setId(shopInformation.getId());
                shopInformationBean.setName(shopInformation.getName());
                shopInformationBean.setLevel(shopInformation.getLevel());
                shopInformationBean.setPrice(shopInformation.getPrice().doubleValue());
                shopInformationBean.setRemark(shopInformation.getRemark());
                shopInformationBean.setSort(stringBuffer);
                shopInformationBean.setQuantity(shopInformation.getQuantity());
                shopInformationBean.setTransaction(shopInformation.getTransaction());
                shopInformationBean.setUid(shopInformation.getUid());
                shopInformationBean.setImage(shopInformation.getImage());
                list.add(shopInformationBean);
            }
        }
        model.addAttribute("shopInformationBean", list);
        return "page/personal/admin_shop_manage";
    }
    private String getSort(int sort) {
        StringBuilder sb = new StringBuilder();
        Specific specific = selectSpecificBySort(sort);
        int cid = specific.getCid();
        Classification classification = selectClassificationByCid(cid);
        int aid = classification.getAid();
        AllKinds allKinds = selectAllKindsByAid(aid);
        sb.append(allKinds.getName());
        sb.append("-");
        sb.append(classification.getName());
        sb.append("-");
        sb.append(specific.getName());
        return sb.toString();
    }
    private Specific selectSpecificBySort(int sort) {
        return specificeService.selectByPrimaryKey(sort);
    }

    //获得第二层分类
    private Classification selectClassificationByCid(int cid) {
        return classificationService.selectByPrimaryKey(cid);
    }

    //获得第一层分类
    private AllKinds selectAllKindsByAid(int aid) {
        return allKindsService.selectByPrimaryKey(aid);
    }


    @RequestMapping(value = "/modifiedPublishProduct.do")
    public String modifiedMyPublishProduct(HttpServletRequest request, Model model,
                                           @RequestParam int id) {
        AdminInformation adminInformation=(AdminInformation)request.getSession().getAttribute("adminInformation") ;
        System.out.println("admin:"+adminInformation);
        if (StringUtils.getInstance().isNullOrEmpty(adminInformation)) {
            return "redirect:/admin_login.do";
        }

        String goodsToken = TokenProccessor.getInstance().makeToken();
        request.getSession().setAttribute("goodsToken", goodsToken);
        model.addAttribute("token", goodsToken);
        ShopInformation shopInformation = shopInformationService.selectByPrimaryKey(id);
        model.addAttribute("adminInformation", adminInformation);
        model.addAttribute("shopInformation", shopInformation);
        model.addAttribute("action", 2);
        model.addAttribute("sort", getSort(shopInformation.getSort()));
        return "page/admin_public_product";
    }


    //下架商品
    @RequestMapping(value = "/admin_deleteShop.do")
    public String deleteShop(HttpServletRequest request, Model model, @RequestParam int id) {
//        Map<String, Integer> map = new HashMap<>();
        AdminInformation adminInformation=(AdminInformation)request.getSession().getAttribute("adminInformation") ;
        System.out.println("admin:"+adminInformation);
        if (StringUtils.getInstance().isNullOrEmpty(adminInformation)) {
            return "redirect:/admin_login.do";
        }else{
            model.addAttribute("adminInformation",adminInformation);
        }
        ShopInformation shopInformation = new ShopInformation();
        shopInformation.setModified(new Date());
        shopInformation.setDisplay(0);
        shopInformation.setId(id);
        try {
            int result = shopInformationService.updateByPrimaryKeySelective(shopInformation);
            System.out.println("result:"+result+"  Id:"+id);

            if (result != 1) {
                return "redirect:admin_shop_manage";
            }
            return "redirect:admin_shop_manage";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:admin_shop_manage";
        }
    }


    //查看用户
    @RequestMapping(value = "/admin_user_manage")
    public String getAdminUser(HttpServletRequest request, Model model) {
        AdminInformation adminInformation= (AdminInformation) request.getSession().getAttribute("adminInformation");
        System.out.println("admin:"+adminInformation.getAno()+"aaaaaaaaaa");
        if(!StringUtils.getInstance().isNullOrEmpty(adminInformation)){
            model.addAttribute("adminInformation",adminInformation);
        }
        else {
            adminInformation=new AdminInformation();
            model.addAttribute("adminInformation",adminInformation);
            UserInformation userInformation = new UserInformation();
            model.addAttribute("userInformation", userInformation);
            return "index";
        }
        List<UserInformationBean> list=new ArrayList<>();
        List<UserInformation> userInformations=userInformationService.getAll();
        for(UserInformation u:userInformations){
            UserInformationBean userInformationBean=new UserInformationBean();
            userInformationBean.setId(u.getId());
            userInformationBean.setUsername(u.getUsername());
            userInformationBean.setRealname(u.getRealname());
            userInformationBean.setGender(u.getGender());
            userInformationBean.setPhone(u.getPhone());
            userInformationBean.setDormitory(u.getDormitory());
            userInformationBean.setSno(u.getSno());
            list.add(userInformationBean);
        }
        model.addAttribute("userInformationBeans",list);
        return "page/personal/admin_user_manage";
    }


    //删除用户
    @RequestMapping(value = "/admin_deleteUser.do")
    public String deleteUser(HttpServletRequest request, Model model, @RequestParam int id) {
        AdminInformation adminInformation= (AdminInformation) request.getSession().getAttribute("adminInformation");
        System.out.println("admin:"+adminInformation.getAno()+"aaaaaaaaaa");
        if(!StringUtils.getInstance().isNullOrEmpty(adminInformation)){
            model.addAttribute("adminInformation",adminInformation);
        }
        else {
            adminInformation=new AdminInformation();
            model.addAttribute("adminInformation",adminInformation);
            UserInformation userInformation = new UserInformation();
            model.addAttribute("userInformation", userInformation);
            return "index";
        }
       UserInformation userInformation=userInformationService.selectByPrimaryKey(id);
        int result=userInformationService.deleteByPrimaryKey(id);
        System.out.println("resultttttttttttt:"+result+"namemmm:"+userInformation.getUsername());
        return "redirect:admin_user_manage";

//        try {
//            int result = shopInformationService.updateByPrimaryKeySelective(shopInformation);
//            System.out.println("result:"+result+"  Id:"+id);
//            if (result != 1) {
//                return "redirect:my_publish_product_page.do";
//            }
//            return "redirect:my_publish_product_page.do";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "redirect:my_publish_product_page.do";
//        }
    }
}
