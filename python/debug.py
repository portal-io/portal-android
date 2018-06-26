#!/usr/bin/python
# coding=utf-8

import datetime
import constants
import os
from pgy import UpdataPGY
from xin import Xfer
today=datetime.date.today().strftime("%Y_%m_%d")


if __name__ == '__main__':
    xfer = Xfer()
    xfer.setFtpParams(constants.FTP_HOST,constants.FTP_UNAME, constants.FTP_PASS)
    path= constants.DUBUG_PATH    
    for file in os.listdir(path):
        srcFile = path+file
        xfer.upload(srcFile,constants.FTP_DUBUG_PATH + today + '/')
        UpdataPGY().uploadPGY(srcFile,file)
