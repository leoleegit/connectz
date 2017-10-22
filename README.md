# connectz
一个分布式通信平台，通过简单协议即实现互联通信。  http://connectz.cn/

<html xmlns="http://www.w3.org/1999/xhtml"><head>
    <meta charset="utf-8"></meta>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"></meta>
    <meta name="renderer" content="webkit"></meta>

    <title>ConnectZ</title>

    <meta name="keywords" content="ConnectZ"></meta>
    <meta name="description" content="ConnectZ"></meta>
    <link rel="shortcut icon" href="./image/cz.png"></link>
    
    <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
    
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="./dist/css/bootstrap.min.css"></link>

    <!-- Optional theme -->
    <link rel="stylesheet" href="./dist/css/bootstrap-theme.min.css"></link>

    <!-- Latest compiled and minified JavaScript -->
    <script src="./dist/js/bootstrap.min.js"></script>	

    <link rel="stylesheet" href="./css/googleapis.css"></link>
    <link rel="stylesheet" href="./css/index.css"></link>
   

</head>
<body>
   <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
           <span class="sr-only">Toggle navigation</span>
           <span class="icon-bar"></span>
           <span class="icon-bar"></span>
           <span class="icon-bar"></span>
         </button>
         <a class="navbar-brand" href="./index.html"><img alt="" src="./image/logo.png"></img></a>  
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li><a href="./index.html">Home</a></li>
            <li class="active"><a href="./gettingstarted.html">Getting Started</a></li>
            <li><a href="./demos.html">Demos</a></li>
          </ul>
        </div> 
      </div>
    </nav>
    
    <div class="site-content container">
	    <div class="sidebar">
	      <ul id="posts">
	        <li id="parent"><a href="#">Overview</a></li>
	        <li><a href="#connectz-connect">Connect</a></li>
	        <li><a href="#connectz-register">Register</a></li>
	        <li><a href="#connectz-keepAlive">KeepAlive</a></li>
	        <li><a href="#connectz-extPacket">ExtPacket</a></li>
	        
	        <li class="anchor"></li>
	        <li id="parent"><a href="#Presence-Plugins">Plugins</a></li>
	        <li><a href="#Presence-Plugins">Presence Plugins</a></li>
	        <li><a href="#Room-Plugins">Room Plugins</a></li>

 			<li class="anchor"></li>
	        <li id="parent"><a href="#Code-Examples">Example Code</a></li>
	        <li><a href="#Code-Examples">Python</a></li>
	        <li><a href="#Examples-JavaScript">JavaScript</a></li>
	        <li><a href="#Examples-Java">Java</a></li>
	      </ul>
	    </div>
	    <div class="content-area with-sidebar">
		  <main class="site-main">
		    <article id="post-26" class="post-26 page type-page status-publish hentry"> 
		    	<div class="entry-content">
		    		<h1 id="connectz-connect">Connect Server</h1>
		    		<h2>UDP / TCP Socket</h2>
		    		<pre><code>$ io.connectz.cn:5086
		    		</code></pre>
		    		<h2>Socket IO</h2>
		    		<pre><code>$ ws://connectz.cn/ws.io/
		    		</code></pre>
		    		<h2>Web Socket</h2>
		    		<pre><code>$ ws://connectz.cn/ws/
		    		</code></pre>
		    		
		    		<h1 id="connectz-register">Protocol Packet</h1>
		    		<h2>Register</h2>
		    		<h3> Register User leo@connectz.cn</h3>
		    		<pre><code>$ {"type":2,"uid":"leo","token":"password","domain":"connectz.cn"}
		    		</code></pre>
		    		<h2 id="connectz-keepAlive">KeepAlive</h2>
		    		<h3>KeepAlive packet ( send a keepalive packet in every 6 ~ 10s )</h3>
		    		<pre><code>$ {"type":1}
		    		</code></pre>
		    		<h2 id="connectz-extPacket">ExtPacket</h2>
		    		<h3>'ext' can be any string ( Depending on your Protocol )</h3>
		    		<pre><code>$ {"type":3,"to":"alex@connectz.cn","ext":{"msg":'hello alex'}}
		    		</code></pre>
		    		
		    		<h1 id="Presence-Plugins">Presence Plugins ( Presence ID: presence@im.plugin )</h1>
		    		<h2>Subscribe</h2>
		    		<h3>Subscribe a user status</h3>
		    		<pre><code>$ {"type":3,"to":"presence@im.plugin","ext":{"expires":3600,"to":"alex@connectz.cn","from":"leo@connectz.cn","type":11}}
		    		</code></pre>
		    		
		    		<h2>Publish</h2>
		    		<h3>Publish the status</h3>
		    		<pre><code>$ {"type":3,"to":"presence@im.plugin","from":"alex@connectz.cn","ext":{"expires":3600,"status":{"status":"NLN"},"type":4}}
		    		</code></pre>
		    		
		    		<h2>Notify</h2>
		    		<h3>Notify the status</h3>
		    		<pre><code>$ {"type":3,"from":"presence@im.plugin","to":"leo@connectz.cn","ext":{"expires":3600,"status":{"status":"NLN"},"type":12}}
		    		</code></pre>
		    		
		    		<h1 id="Room-Plugins">Room Plugins ( Room ID: room@im.plugin )</h1>
		    		<h2>Create a room</h2>
		    		<h3>send a create a room request and get a room id from response body</h3>
		    		<pre><code>$ {"type":3,"to":"room@im.plugin" ,"from":"leo@connectz.cn" ,"ext":{"type":3}}
		    		</code></pre>
		    		<pre><code>$ {"code":200,"title":"New room","to":"leo@connectz.cn","from":"room@im.plugin","body":"201710-29wika1V@im.plugin","type":0}
		    		</code></pre>
		    		
		    		<h2>Join room</h2>
		    		<h3>Join room</h3>
		    		<pre><code>$ {"type":3,"to":"201710-29wika1V@im.plugin","from":"leo@connectz.cn","ext":{"user":{"username":"leo@connectz.cn","nickname":"leo"},"type":1}}
		    		</code></pre>
		    		
		    		<h2>Message</h2>
		    		<h3>Message packet can be anything</h3>
		    		<pre><code>$ {"type":3,"to":"201710-29wika1V@im.plugin","from":"leo@connectz.cn","ext":{"packet":{"msg":"hello guys!"},"type":4}}
		    		</code></pre>
		    		
		    		<h2>Leave room</h2>
		    		<h3>Leave room</h3>
		    		<pre><code>$ {"type":3,"to":"201710-29wika1V@im.plugin","from":"leo@connectz.cn","ext":{"type":2}}
		    		</code></pre>
		    		
		    		<h1 id="Code-Examples">Example Code</h1>
		    		<h2 id="Code-Examples">Python</h2>
		    		<h3><a href="https://github.com/leoleegit/connectz/tree/master/connectz-python">https://github.com/leoleegit/connectz/tree/master/connectz-python</a></h3>
		    		<pre><code>
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
        
            
		    		</code></pre>
		    		
		    		<h2 id="Examples-JavaScript">JavaScript</h2>
		    		<h3><a href="https://github.com/leoleegit/connectz/tree/master/connectz-demos/src/main/webapp/examples">
		    			https://github.com/leoleegit/connectz/tree/master/connectz-demos/src/main/webapp/examples</a></h3>
		    		<pre><code>
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
        
            
		    		</code></pre>
		    	</div>
		    </article> 
		  </main> 
		</div> 

  </div>
    
    
     <footer class="footer">
      <div class="container">
        <p class="text-muted">© 2017 ConnectZ</p>
      </div>
    </footer>
    <script src="http://connectz.cn/js/connectz.js"></script>
    <script src="./js/common.js"></script>
    
</body>
</html>
