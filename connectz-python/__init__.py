'''
Created on Oct 18, 2017

@author: leo <dev@connectz.cn> 
'''
from connectz.zchat.connection import Connection

VERSION = 'v-20171016'
NioHost = 'io.connectz.cn'
NioPort = 5086
ZUserName   = 'test'
ZDomain     = 'connectz.cn'


class CallBack:
    
    def onMessage(self, msg):
        print(msg)
    
    def onOpen(self):  
        print('onOpen')
        ##Register
        connection.send({"type":2,"uid":ZUserName,"token":"password","domain":ZDomain})
            
    def onClose(self, reason):
        print('onClose',reason)
    

print('Version: %s'%(VERSION))
print('Author : %s'%('dev@connectz.cn'))
callback   = CallBack()
connection = Connection(callback,NioHost, NioPort)  
connection.connect()   

##connection.send({"type":3,"to":"alex@connectz.cn","ext":{"msg":'hello alex'}})
    
pass