#!/usr/bin/python
# coding=utf-8


############################ 上传FTP的常量值 ########################################
FTP_HOST = '172.29.200.31'
FTP_UNAME = 'mbapp'
FTP_PASS = 'mb#2016MB'
FTP_DUBUG_PATH= './android/dubug/'
FTP_RELEASE_PATH= './android/release/'

FTP_APK_DUBUG_PATH = 'WhaleyVR/launcher/build/outputs/apk/'
FTP_APK_RELEASE_PATH = 'WhaleyVR/launcher/build/bakApk/'

FTP_CE_APK_DUBUG_PATH = 'E:/newApp/WhaleyVR/app/build/outputs/apk/'
FTP_CE_APK_RELEASE_PATH = 'E:/newApp/WhaleyVR/app/build/bakApk/'


DUBUG_PATH = FTP_APK_DUBUG_PATH

RELEASE_PATH = FTP_APK_RELEASE_PATH
############################ 蒲公英上传的常量值 ######################################

#蒲公英应用上传地址
PGY_URL = 'http://www.pgyer.com/apiv1/app/upload'
#蒲公英提供的 用户Key
PGY_USER_KEY = '192737af2228997bfa0916e84fc1382c'
#蒲公英提供的 API Key
PGY_API_KEY = 'ff86e9c9f1022ab53046ff095a78ed8a'

############################ 发送邮件的常量值 ########################################
#邮件接受者
mail_receiver_all = ['cai.shunyi@whaley.cn','li.chong@whaley.cn',
                   'huang.lingli@whaley.cn','liu.liming@whaley.cn',
                   'liu.guoquan@whaley.cn',  'geng.zhonghua@whaley.cn',
                   'cai.shengwei@whaley.cn','peng.long@whaley.cn',
                   'li.pang@whaley.cn','jiang.haohong@whaley.cn',
                   'li.xin@whaley.cn','xie.dongsheng@whaley.cn']

mail_receiver_ce_shi=['qu.xiawei@whaley.cn']

mail_receiver = mail_receiver_all

#根据不同邮箱配置 host，user，和pwd
mail_host = 'smtp.exmail.qq.com'
mail_user = 'qu.xiawei@whaley.cn'
mail_pwd = '1Xf2PXhiaFXue92C'
