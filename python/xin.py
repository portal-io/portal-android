#!/usr/bin/python
# coding=utf-8

import sys
import os
import datetime
import ftplib
import smtplib
import string
from ftplib import FTP
_XFER_FILE = 'FILE'
_XFER_DIR = 'DIR'

today=datetime.date.today().strftime("%Y_%m_%d")

class Xfer(object):

    def __init__(self):
        self.ftp = None

    def __del__(self):
        pass

    def setFtpParams(self, ip, uname, pwd, port = 21, timeout = 60):
        self.ip = ip
        self.uname = uname
        self.pwd = pwd
        self.port = port
        self.timeout = timeout

    def initEnv(self):
        if self.ftp is None:
            self.ftp = FTP()
            print ('### connect ftp server: %s ...'%self.ip)
            self.ftp.connect(self.ip, self.port, self.timeout)
            self.ftp.login(self.uname, self.pwd)
            print (self.ftp.getwelcome())

    def clearEnv(self):
        if self.ftp:
            self.ftp.close()
            print ('### disconnect ftp server: %s!'%self.ip)
            self.ftp = None

    def uploadFile(self, localpath, remotepath='./'):
        if not os.path.isfile(localpath):
            return
        print ('+++ upload %s to %s:%s'%(localpath, self.ip, remotepath))
        
        self.ftp.storbinary('STOR ' + remotepath, self.f)
        
    def __filetype(self, src):
        if os.path.isfile(src):
            index = src.rfind('\\')
            if index == -1:
                index = src.rfind('/')
            return _XFER_FILE, src[index+1:]
        elif os.path.isdir(src):
            return _XFER_DIR, ''


    def isCwdPath(self, file):
        print ('CwdPathFile='+file)
        try:
           self.ftp.cwd(file)
           print ('isCwdPath=True')
           return True
        except Exception as e:
           print (e)
           return False 

    def isMkdPath(self, file):
        print ('MkdPathFile='+file)
        try:
           self.ftp.mkd(file)
           print ('isMkdPath=True')
           return True
        except Exception as e:
           print (e)
           return False 
            
    def openPath(self, openPath):
        print ('openPath='+openPath)
        print ('ftpPath='+self.ftp.pwd())
        
        if self.isCwdPath(openPath):
           return        
        if self.isMkdPath(openPath):
           self.ftp.cwd(openPath)
           return
        path = file[ : openPath.rfind('/')]
        self.openPath(path[ :path.rfind('/')+1])
 

    def upload(self, src,updataPath):
        print ("updataPath = "+updataPath)
        filetype, filename = self.__filetype(src)
        print ("filename = "+filename)
        self.initEnv()
        file='./android/debug/'+today+'/'
        self.openPath(updataPath)
        self.f=open(src, 'rb')
        self.uploadFile(src, filename)
        self.f.close()
        self.clearEnv()
        



    
