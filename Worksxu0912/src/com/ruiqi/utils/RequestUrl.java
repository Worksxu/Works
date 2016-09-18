package com.ruiqi.utils;
/**
 * 网络请求的接口
 * @author Administrator
 *shipperproduct confirmdeposit

 */
public interface RequestUrl {
	//promotions

	String IP = "cztest.ruiqi100.com";//待定服务器
//	String IP = "sr.ruiqi100.com";//线上测试服务器
	String test="测试app";
	//String IP = "182.92.97.39";//线上服务器
	String LOGIN_PATH = "http://"+IP+"/appworks/login";//登录
	String LOGIN_BOTTLELIST = "http://"+IP+"/appworks/bottlelist";//送气工钢瓶库存信息
	String PASS_RESET = "http://"+IP+"/appworks/passreset";//找回密码
	String GET_CODE = "http://"+IP+"/appworks/sendcode";//发送验证码
	String PWD = "http://"+IP+"/appworks/logout";//退出账号
	
	String SELF_REPORT = "http://"+IP+"/apptype/report";//安全报告
	
	String USER_INFO = "http://"+IP+"/appworks/userinfo";//获取老用户信息
	
	String BOTTLE_TYPE = "http://"+IP+"/apptype/bottletype"; //押金
	
	String TAOCAN_TYPE = "http://"+IP+"/apptype/commditylist"; //套餐类型
	
	String ORDET_LIST =  "http://"+IP+"/appworks/orderlist";//全部订单
	
	String ORDER_INFO = "http://"+IP+"/appworks/orderinfo";//订单详情
	
	String DESPOIT = "http://"+IP+"/appworks/deposit";//退押金详情
	
	String CONFIRMORDER = "http://"+IP+"/appworks/confirmorder";//确认收款   
	
	String SHIPPERPRODUCT = "http://"+IP+"/appworks/shipperproduct";//配件入库记录
	
	String CONFIRM_DEPOSIT = "http://"+IP+"/appworks/confirmdeposit";//退押金提交
	
	String STOCK_LIST = "http://"+IP+"/appworks/kucunlist";//钢瓶库存
	
	String PJ_STOCK = "http://"+IP+"/appworks/kcproduct";//配件库存
	
	String IN_SHIPER = "http://"+IP+"/appworks/inshipper";//领瓶
	
	String PEIJIAN_LIST = "http://"+IP+"/appworks/shipperproduct";//
	
	String ORDER_IN = "http://"+IP+"/appworks/orderpayment";//订单收入
	
	String CREATE_ORDER= "http://"+IP+"/appworks/createorder";//创建订单
	
	String OUT= "http://"+IP+"/appworks/shipperpayment";
	
	String PAYLIST = "http://"+IP+"/appworks/nopaylist";//欠款用户
	
	String REPAYMENTLIST = "http://"+IP+"/appworks/repaymentlist";//还款记录
	
	String NOORDERLIST = "http://"+IP+"/appworks/noorderlist";//欠款记录
	
	String ADDREPAYMENT = "http://"+IP+"/appworks/addrepayment";//收缴欠款
	
	String IS_BOTTLE = "http://"+IP+"/appworks/isbottle";//扫描判定钢瓶
	
	String PJ = "http://"+IP+"/apptype/producttype";//配件列表
	
	String FORPAY = "http://"+IP+"/appworks/shipperforpay";//上交钱
	
	String APPLY_PJ= "http://"+IP+"/appworks/inproduct";//配件申请    
	
	String EDITOR_ORDER= "http://"+IP+"/appworks/editorder";//修改订单
	
	String SHIPPER_ARREARS= "http://"+IP+"/appworks/shipperarrears";//收缴欠款记录(我的财务)
	
	String OUT_PING= "http://"+IP+"/appworks/cklist";//钢瓶出库记录
	
	String IN_PING= "http://"+IP+"/appworks/rklist";//钢瓶入库记录
	
	String DEPOSIT_ORDER= "http://"+IP+"/appworks/depositorder";//退押金支出记录
	
	String BOOTLE_LIST= "http://"+IP+"/apptype/bottlelist";//折旧瓶列表
	
	String UP_LOAD_DATA= "http://"+IP+"/appworks/safeimage";//上传图片
	
	String PEIJIANLIEBIAO="";//配件列表
	
	String NEWCREATEORDER="http://"+IP+"/appworks/createkhorder";//新订单提交
	
	String ADDRESSLIST="http://"+IP+"/apptype/useraddress";//地址库
	
	String YOUHUI="http://"+IP+"/appworks/promotions";//优惠折扣
	String NewYOUHUI="http://"+IP+"/apptype/promotions";//优惠折扣活动
	String WorksYOUHUI="http://"+IP+"/apptype/promotionshiper";//送气工优惠
	
	String REGISTER="http://"+IP+"/appworks/createkehu";//注册新用户
	
	String SELFUSER="http://"+IP+"/appworks/reportuserinfo";//用户安全检测
	
	String SAFEREPORT = "http://" + IP + "/appworks/safereport";// 提交安全报告信息
	String NUllBOTTLE = "http://" + IP + "/appworks/backorder";// 创建退瓶订单
	String REPAIRLIST = "http://" + IP + "/appworks/baoxiulist";// 报修订单列表
	String REPAIRORDER = "http://" + IP + "/appworks/treatebaoxiu";// 报修订单详情
	String BACKSHOP = "http://" + IP + "/appworks/backshop";// 库存回门店
	String RECEIDEAIL = "http://" + IP + "/appworks/userqkorder";//欠款记录(新)
	String CHANGE = "http://" + IP + "/appworks/editkehu";//修改用户基础信息
	String BACKLIST = "http://" + IP + "/appworks/backshipper";//回库列表
	String CHANGEPASS = "http://" + IP + "/appworks/changepassword";//修改密码
	String LOCATION = "http://" + IP + "/appworks/shipperposition";//送气工坐标
	String ZHEJIU = "http://" + IP + "/apptype/depreciation";//折旧瓶新
	String ZHEJIUORDER = "http://" + IP + "/appworks/createdepreciation";//折旧订单
	String ZHEJIULIST = "http://" + IP + "/appworks/depreciationlist";//折旧订单列表
	String CHANGEORDER = "http://" + IP + "/appworks/createchangebottle";//置换/退瓶订单
	String CHANGEORDERLIST = "http://" + IP + "/appworks/changeorderlist";//置换订单列表
	String ADVANCE = "http://" + IP + "/appworks/paymentorder";//预支付结算
	String ROBORDER = "http://" + IP + "/appworks/grabone";//抢单订单
	String ROBORDERSURE = "http://" + IP + "/appworks/shippergrabone";//抢单确认
	String GENGXIN = "http://" + IP + "//appworks/appversion";//更新
	String SHOURU = "http://" + IP + "//appworks/financialincome";//财务收入(新)
	String ZHICHU = "http://" + IP + "//appworks/financialexpenses";//财务支出(新)
	
}
