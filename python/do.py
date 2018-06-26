#!/usr/bin/python
# coding=utf-8
import sys
import os
import json
import datetime
import pipes
import subprocess
import ftplib
import smtplib;
from ftplib import FTP
from email.mime.text import MIMEText

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

    def uploadDir(self, localdir='./', remotedir='./'):
        if not os.path.isdir(localdir):
            return
        self.ftp.cwd(remotedir)
        for file in os.listdir(localdir):
            src = os.path.join(localdir, file)
            if os.path.isfile(src):
                self.uploadFile(src, file)
            elif os.path.isdir(src):
                try:
                    self.ftp.mkd(file)
                except:
                    sys.stderr.write('the dir is exists %s'%file)
                self.uploadDir(src, file)
        self.ftp.cwd('..')

    def uploadFile(self, localpath, remotepath='./'):
        if not os.path.isfile(localpath):
            return
        print ('+++ upload %s to %s:%s'%(localpath, self.ip, remotepath))
        self.ftp.storbinary('STOR ' + remotepath, open(localpath, 'rb'))


    def __filetype(self, src):
        if os.path.isfile(src):
            index = src.rfind('\\')
            if index == -1:
                index = src.rfind('/')
            return _XFER_FILE, src[index+1:]
        elif os.path.isdir(src):
            return _XFER_DIR, ''

    def upload(self, src):
        filetype, filename = self.__filetype(src)
        self.initEnv()
        file='./android/'+today+'/'
        try:
           self.ftp.cwd(file)
        except ftplib.error_perm:
           try:
              self.ftp.mkd(file)
              self.ftp.cwd(file)
           except ftplib.error_perm:
              sys.stderr.write('the dir is exists %s'%file)
        self.uploadFile(src, filename)
        self.clearEnv()









if __name__ == '__main__':
    xfer = Xfer()
    xfer.setFtpParams('172.29.200.31', 'mbapp', 'mb#2016MB')
    path='/root/.jenkins/workspace/Android/WhaleyVR/app/build/outputs/apk/'
    for file in os.listdir(path):
        srcFile = path+file
        xfer.upload(srcFile)
