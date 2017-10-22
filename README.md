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
		    		
