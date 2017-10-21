(function(){
	connectz.cookies.setCookie('cz-debug',true,7*60*24);
	var roomAddr = "room0001@im.plugin";
	var connect = function(nickname){
		var this_ = this;
		$('.chat').css('display','block');
		$('.login').css('display','none');
		
		this.onopen = function(e){ 
			logMsg("Welcome to ConnectZ Room {roomID:room0001@im.plugin}");
			//Register user
			chatIO.send({"type":2,"uid":connectz.util.random(6),"token":"password","domain":"connectz.cn"});
		}
		this.onclose = function(e){ 
			logMsg('Disconnect');
			$('.inputMessage').arrt('disable','disable');
		}
		this.onmessage = function(msg){ 
			if(msg.type == 0 && msg.title=='Register'){
				logMsg(msg.body + '   ('+nickname+')');
				if('Register succ'==msg.body){
					 //join room
					var ext = new connectz.json({"type":1,"user":{"nickname":nickname}}).toString();
					chatIO.send({"type":3,"to":roomAddr,"ext":ext});
				}
			}else if(msg.type == 3){
				var event = new connectz.json(msg.ext).decode();
				if(event.type==4){
					var name = event.user.nickname;
					var message = new connectz.json(event.packet).decode();
					sysMsg(name, message.text, 'rgb(43, 170, 36)');
				}
				else if(event.type==1){
					// someone join
					//"{"user":{"username":"alex%40connectz.cn","nickname":"alex"},"ID":"201710120dP2B63g","from":"alex%40connectz.cn","type":1}"
					var name = event.user.nickname;
					logMsg(name + ' join room');
				}
				else if(event.type==2){
					// someone leave
					if(!event.user)return;
					var name = event.user.nickname;
					logMsg(name + ' leave room');
				}else if(event.type==12){
					// room status update
					//"{"status":{"parter":[{"username":"alex%40connectz.cn","nickname":"alex"}],"roomid":"room0001%40im.plugin","expires":0},"ID":"20171012C0fCQx01","to":"alex%40connectz.cn","from":"room0001%40im.plugin","type":12}"
					var parter = event.status.parter;
					logMsg("there are "+ parter.length +" participants");
				}
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
		
		$('.inputMessage').on('keyup',function(e){
			if((connectz.browser.msie && e.keyCode==13) || connectz.util.isKeyTrigger(e,13,false)){
				var msg = $(this).val();
				if(!msg || msg.trim()=='')return;
				var ext = new connectz.json({"type":4,"packet":{type:"text",text:msg}}).toString();
				chatIO.send({"type":3,"to":roomAddr,"ext":ext});
				sysMsg(nickname, msg, 'rgb(56, 36, 170)');
				$(this).val('');
			}
		});
	}
	
	var logMsg = function(msg){
		var source   = $("#waterfall-log").html();
		var template = Handlebars.compile(source);
		var context = {'msg':msg};
		var html    = template(context);
		$('.messages').append(html);
		var parent  = $('.messages');
		parent.scrollTop(parent[0].scrollHeight);
	}
	
	var sysMsg = function(from,msg,color){
		var source   = $("#waterfall-msg").html();
		var template = Handlebars.compile(source);
		var context = {'msg':msg,'nickname':from, 'color':color};
		var html    = template(context);
		$('.messages').append(html);
		var parent  = $('.messages');
		parent.scrollTop(parent[0].scrollHeight);
	}
	
	$('.usernameInput').on('keyup',function(e){
		if((connectz.browser.msie && e.keyCode==13) || connectz.util.isKeyTrigger(e,13,false)){
			var nickname = $(this).val();
			connect(nickname);
		}
	});
	
})();