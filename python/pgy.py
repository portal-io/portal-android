#!/usr/bin/python
# coding=utf-8

import socket
import sys
import json
import time
import smtplib;
import random
import string
import urllib2
import cStringIO
import constants
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.mime.image import MIMEImage



timeout = 400    
socket.setdefaulttimeout(timeout)#这里对整个socket层设置超时时间。后续文件中如果再使用到socket，不必再设置
#蒲公英应用上传地址
url = 'http://www.pgyer.com/apiv1/app/upload'

installPassword=''.join(random.sample(string.ascii_lowercase,5))
reload(sys) # Python2.5 初始化后会删除 sys.setdefaultencoding 这个方法，我们需要重新载入
sys.setdefaultencoding('utf-8')
#############################################################
#请求参数字典

class UpdataPGY(object):


    def _encode_multipart(self,file):
        boundary = '----------%s' % hex(int(time.time() * 1000))
        print ('boundary='+boundary)
        params_dict={
                    'uKey': constants.PGY_USER_KEY,
                    '_api_key': constants.PGY_API_KEY,
                    'file': self.f,
                    'installType': '2',
                    'password': installPassword,
                    'updateDescription':""
            }
        data = []
        for k, v in params_dict.items():
            data.append('--%s' % boundary)
            if hasattr(v, 'read'):
                filename = getattr(v, 'name', '')
                print ('filename='+filename)
                content = v.read()
                print ('k='+k)
                decoded_content = content.decode('ISO-8859-1')
                data.append('Content-Disposition: form-data; name="%s"; filename="%s"' % (k, file))
                data.append('Content-Type: application/octet-stream\r\n')
                data.append(decoded_content)
            else:
                data.append('Content-Disposition: form-data; name="%s"\r\n' % k)
                data.append(v if isinstance(v, str) else v.decode('utf-8'))
        data.append('--%s--\r\n' % boundary)
        return '\r\n'.join(data), boundary

    def handle_resule(self,result):
        json_result = json.loads(result)
        if json_result['code'] is 0:
           print ('上传蒲公英成功')
           self.send_Email(json_result)
        
    def uploadPGY(self,srcFile,file):
        print ('uploadPGY')
        self.f=open(srcFile, 'rb')
        coded_params, boundary = self._encode_multipart(file)
        req = urllib2.Request(constants.PGY_URL, coded_params.encode('ISO-8859-1'))
        req.add_header('Content-Type', 'multipart/form-data; boundary=%s' % boundary)
        try:
           resp = urllib2.urlopen(req)#这里是要读取内容的url
           body = resp.read().decode('utf-8')
           print ('start='+body)
           self.handle_resule(body)
           self.f.close()
           resp.close()#记得要关闭
        
        except Exception as e:
           print('Exception: send email failed', e)

    def send_Email(self,json_result):
         appName = json_result['data']['appName']
         appKey = json_result['data']['appKey']
         appVersion = json_result['data']['appVersion']
         appBuildVersion = json_result['data']['appBuildVersion']
         appShortcutUrl = json_result['data']['appShortcutUrl']
         appQRCodeURL = json_result['data']['appQRCodeURL']
         appVersionNo = json_result['data']['appVersionNo']


         msg = MIMEMultipart()
         environsString = '<h3>本次打包相关信息</h3><p>'
         environsString = '<h3>app版本号（推送账号）:'+ appVersionNo.decode('utf-8').encode('gb18030')+'</h3><p>'
         environsString += '<p>apk 包下载安装地址 :  '+'<a href=http://www.pgyer.com/' + appShortcutUrl.decode('utf-8').encode('gb18030')+'>http://www.pgyer.com/' + appShortcutUrl.decode('utf-8').encode('gb18030')+'</a>' + '   密码 : ' + installPassword + '<p>'
         environsString += '<p><img src="cid:image1"><p>'
         message = environsString
         htm = MIMEText(message, _subtype='html', _charset='utf-8')
         msg.attach(htm)
         imgFile = cStringIO.StringIO(urllib2.urlopen(appQRCodeURL).read())
         msgImage=MIMEImage(imgFile.read())
         msgImage.add_header("Content-ID", "<image1>")
         msg.attach(msgImage)
         msg['To'] = ','.join(constants.mail_receiver)
         msg['from'] = constants.mail_user
         msg['subject'] = '最新打包文件_'+appVersion
    
         try:
             s = smtplib.SMTP_SSL(constants.mail_host,465)
             s.login(constants.mail_user, constants.mail_pwd )
             s.sendmail(constants.mail_user, constants.mail_receiver,msg.as_string())
             s.close()
             print ('success')
         except Exception as e:
             print ("Exception: send email failed "+e)
    
