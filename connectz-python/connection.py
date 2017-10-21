'''
Created on Oct 18, 2017

@author: leo <dev@connectz.cn> 
'''

from socket import socket,AF_INET,SOCK_STREAM
from connectz.zchat.myJson import MyJson
from threading import Thread, Timer  

class Connection(Thread):
    '''
    classdocs
    '''
    DELIMITER="\\0\\0"
    BUFSIZ   = 1024
    listener = None
    connected=False
    
    host     = None
    port     = 0
    
    aliveThread = None
    '''
    listener.onOpen; .onMessage ; .onClose
    '''
    def __init__(self, listener, host, port):
        '''
        Constructor
        '''
        self.listener = listener
        self.host     = host
        self.port     = port
        Thread.__init__(self)
     
    def connect(self):
        self.start()
        
    def run(self): 
        ADDR=(self.host, self.port)
        self.client=socket(AF_INET, SOCK_STREAM)
        try:
            print('(%s:%s) connecting ... '%(self.host,self.port))
            self.client.connect(ADDR)
        except Exception, e:
            print(e)
            self.onClose(e)
            return
        self.connected = True
        self.onOpen()
        
        print('recv ...')
        while 1:    
            if not self.connected:
                break   
            data=self.client.recv(self.BUFSIZ)
            if not data:
                break
            self.onMessage(data)
        self.connected=False
        self.onClose("close remote")
        
    def close(self):
        self.client.shutdown(True);
        self.connected=False
        self.onClose("Closed")
        
    def send(self, msg):    
        if msg.has_key('type') and msg['type'] != 1:
            print('send',msg)
            
        msg = MyJson(msg).toString()
        
        if self.connected:
            self.client.sendall((msg+self.DELIMITER).encode('utf8')) 
        
    def onMessage(self, msg=None, splitmsg=None):
        if splitmsg == None:
            msg = msg.decode('utf8')
            if isinstance(msg, unicode):
                msg = msg.encode('unicode-escape').decode('string_escape') 
            if msg.endswith(self.DELIMITER):
                msg =  msg.strip(self.DELIMITER)
            if self.DELIMITER in msg:
                for sMsg in msg.split(self.DELIMITER) :
                    self.onMessage(splitmsg=sMsg)
                return
        else:
            msg =  splitmsg   
        msgObj = MyJson(msg).decode()
        if not isinstance(msgObj, dict):
            print(msgObj)
            return 
        if msgObj.has_key('type') and msgObj['type'] == 1:
            return
        if self.listener != None:
            def handler():
                self.listener.onMessage(msgObj)
            Thread(target=handler).start()
         
    def onOpen(self):
        if self.listener != None:
            self.listener.onOpen() 
        self.keepAlive()
             
    def keepAlive(self): 
        if not self.connected:
            return   
        self.aliveThread = Timer(10, KeepAliveThread, [self]) 
        self.aliveThread.start()
        
    def onClose(self, reason):
        if self.listener != None:
            self.listener.onClose(reason); 
        self.aliveThread.stop()
        self.aliveThread=None
        
              
def KeepAliveThread(connection):
    connection.send({"type":1})
    connection.keepAlive()
        
            