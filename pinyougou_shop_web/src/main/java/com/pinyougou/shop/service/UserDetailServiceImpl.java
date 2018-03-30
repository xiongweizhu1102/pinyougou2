package com.pinyougou.shop.service;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bear
 * @version 1.0
 * @description com.pinyougou.shop.service
 * @date 2018/3/27
 */
public class UserDetailServiceImpl implements UserDetailsService {
    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("进入授权了");
        //授权集合
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        //授权集合添加了角色与之绑定
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        //获取商家对象
        TbSeller seller = sellerService.findOne(username);
        if (seller!=null){
            //用户进行审核了才能进行登录1
            if (seller.getStatus().equals("1")){
                //用户密码与用户名与授权进行绑定
                System.out.println("审核过了才能登陆");
                System.out.println(seller.getPassword());
                return new User(username,seller.getPassword(),grantedAuthorities);
            }else {
                return null;
            }
        }else{
            return null;
        }
       // return new User(username,"123456",grantedAuthorities);
    }
}
