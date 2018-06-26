#!/usr/bin/python
# coding=utf-8

import constants
import os
from xin import Xfer

if __name__ == '__main__':
    xfer = Xfer()
    xfer.setFtpParams(constants.FTP_HOST,constants.FTP_UNAME, constants.FTP_PASS)
    path= constants.RELEASE_PATH
    for file in os.listdir(path):
        srcFile = path+file+'/'
        print ('srcFile='+srcFile)
        for appFile in os.listdir(srcFile):
            print ('appFile='+appFile)
            bakApkPath = srcFile+appFile
            print ('bakApkPath='+bakApkPath)
            xfer.upload(bakApkPath,constants.FTP_RELEASE_PATH + file + '/')
