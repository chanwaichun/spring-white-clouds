package com.byx.pub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.AddOrUpdateAdminQo;
import com.byx.pub.bean.qo.PageListAdminQo;
import com.byx.pub.bean.vo.AdminHomePageVo;
import com.byx.pub.bean.vo.AdminPageListVo;
import com.byx.pub.bean.vo.LoginAdminVo;
import com.byx.pub.bean.vo.SelectAdminListVo;
import com.byx.pub.plus.entity.Admin;

import java.util.List;

/**
 * @author Jump
 * @date 2023/5/7 14:20:00
 */
public interface AdminService {
    /**
     * 保存帐号
     * @param addOrUpdateAdminQo
     */
    void addOrUpdateAdmin(AddOrUpdateAdminQo addOrUpdateAdminQo);

    /**
     * 删除用户
     * @param adminId
     */
    void delAdmin(String adminId);

    /**
     * 分页查询账户列表
     * @param qo
     * @return
     */
    Page<AdminPageListVo> pageList(PageListAdminQo qo);

    /**
     * 扫码登录微信回调接口
     * @param code
     * @param state
     * @return
     */
    String callBack(String code, String state);

    /**
     * 企业微信登录
     * @param code
     * @param state
     * @return
     */
    String cropAuth(String code, String state);

    /**
     * 获取登录用户信息
     * @param adminId
     * @param role
     * @return
     */
    LoginAdminVo getAdminInfo(String adminId,String role);

    /**
     * 绑定账户
     * @param openId
     * @param mobile
     * @param password
     * @param xToken
     * @return
     */
    LoginAdminVo bandAdmin(String openId, String mobile, String password, String xToken);

    /**
     * 根据手机号获取用户信息
     * @param mobile
     * @return
     */
    LoginAdminVo getLoginAdminInfoByMobile(String mobile);

    /**
     * 获取所有咨询师账户(有效的)
     * @return
     */
    List<Admin> getAllConsultant();

    /**
     * 根据手机号码获取管理后台帐号
     * @param mobile
     * @return
     */
    Admin getAdminByMobile(String mobile);

    /**
     * 帐号密码登录
     * @param mobile
     * @param password
     * @return
     */
    String accountLogin(String mobile,String password);

    /**
     * 获取账户详情
     * @param id
     * @return
     */
    Admin getAdminDetail(String id);

    /**
     * 管理后台前端选择咨询师列表
     * @param roleId
     * @param adminId
     * @return
     */
    List<SelectAdminListVo> getAdminListByRole(String roleId, String adminId);

    /**
     * 根据id查询admin
     * @param adminId
     * @return
     */
    Admin getAdminById(String adminId);

    /**
     * 修改咨询师信息
     * @param updateBean
     */
    void updateAdmin(Admin updateBean);

    /**
     * 根据角色分页查询商家下的咨询师
     * @param sjId
     * @param roleIds
     * @param adminName
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<AdminPageListVo> getSjAdminByRole(String sjId,List<Integer> roleIds,String adminName,Integer pageNum,Integer pageSize);

    /**
     * 获取商家管理员信息
     * @param sjId
     * @return
     */
    Admin getBusinessMangeAdmin(String sjId);

    /**
     * 获取首页咨询师登录信息
     * @param adminId
     * @return
     */
    AdminHomePageVo getAdminHomePageVo(String adminId);

    /**
     * 根据商家id获取帐号列表
     * @param sjId
     * @return
     */
    List<Admin> getAdminListBySj(String sjId);

    /**
     * 查询商家有授权小红书店铺的咨询师
     * @param sjId
     * @return
     */
    List<Admin> getXhsAdminListBySj(String sjId);
}
