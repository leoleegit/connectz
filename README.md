# connectz
一个分布式通信平台，通过简单协议即实现互联通信。  http://connectz.cn/

Connect Server

UDP / TCP Socket

$ io.connectz.cn:5086
		    		
Socket IO

$ ws://connectz.cn/ws.io/
		    		
Web Socket

$ ws://connectz.cn/ws/
		    		
Protocol Packet

Register

Register User leo@connectz.cn

$ {"type":2,"uid":"leo","token":"password","domain":"connectz.cn"}
		    		
KeepAlive

KeepAlive packet ( send a keepalive packet in every 6 ~ 10s )

$ {"type":1}
		    		
ExtPacket

'ext' can be any string ( Depending on your Protocol )

$ {"type":3,"to":"alex@connectz.cn","ext":{"msg":'hello alex'}}
		    		
Presence Plugins ( Presence ID: presence@im.plugin )

Subscribe

Subscribe a user status

$ {"type":3,"to":"presence@im.plugin","ext":{"expires":3600,"to":"alex@connectz.cn","from":"leo@connectz.cn","type":11}}
		    		
Publish

Publish the status

$ {"type":3,"to":"presence@im.plugin","from":"alex@connectz.cn","ext":{"expires":3600,"status":{"status":"NLN"},"type":4}}
		    		
Notify

Notify the status

$ {"type":3,"from":"presence@im.plugin","to":"leo@connectz.cn","ext":{"expires":3600,"status":{"status":"NLN"},"type":12}}
		    		
Room Plugins ( Room ID: room@im.plugin )

Create a room

send a create a room request and get a room id from response body

$ {"type":3,"to":"room@im.plugin" ,"from":"leo@connectz.cn" ,"ext":{"type":3}}
		    		
$ {"code":200,"title":"New room","to":"leo@connectz.cn","from":"room@im.plugin","body":"201710-29wika1V@im.plugin","type":0}
		    		
Join room

Join room

$ {"type":3,"to":"201710-29wika1V@im.plugin","from":"leo@connectz.cn","ext":{"user":{"username":"leo@connectz.cn","nickname":"leo"},"type":1}}
		    		
Message

Message packet can be anything

$ {"type":3,"to":"201710-29wika1V@im.plugin","from":"leo@connectz.cn","ext":{"packet":{"msg":"hello guys!"},"type":4}}
		    		
Leave room

Leave room

$ {"type":3,"to":"201710-29wika1V@im.plugin","from":"leo@connectz.cn","ext":{"type":2}}
		    		
Example Code

Python

https://github.com/leoleegit/connectz/tree/master/connectz-python


'''
Created on Oct 18, 2017

@author: leo  
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
        
            
		    		
JavaScript

https://github.com/leoleegit/connectz/tree/master/connectz-demos/src/main/webapp/examples


this.onopen = function(e){ 
	chatIO.send({"type":2,"uid":nickname,"token":"password","domain":domain});
}
this.onclose = function(e){ 
	logMsg('Disconnect');
}
this.onmessage = function(msg){ 
	if(msg.type == 0 && msg.title=='Register'){
		logMsg(msg.body + '   ('+nickname+')');

	}else if(msg.type == 3){
		var event = new connectz.json(msg.ext).decode();
	}
}

var chatIO  = new connectz.io({
	host:'connectz.cn',
	port:80,
	resource:'ws.io',
	secure:false,// (location.href.startWith('https://')?true:false),
	onopen:this.onopen,
	onclose:this.onclose,
	onmessage:this.onmessage
});
