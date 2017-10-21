(function(){
	connectz.cookies.setCookie('cz-debug',true,7*60*24);
	var connect = function(nickname){
		var this_ = this;
		$('.chat').css('display','block');
		$('.login').css('display','none');
		
		var domain = connectz.cookies.getCookie('cz-domain');
		if(!domain){
			domain = connectz.util.random(6);
			connectz.cookies.setCookie('cz-domain',domain,7*60*24);
		}
		$(window).on('unload',function(e){
			removeUser(nickname);
		});
		var refreshUserlist = function(){
			var users = connectz.cookies.getCookie('cz-users');
			userlist  = users.split(',');
			for(var u of userlist){
				if($('.userlist').find('[name="'+u+'"]').length>0)
					continue;
				if(u!=nickname)
					$('.userlist').append('<option name="'+u+'">'+u+'</option>');
			}
			var option = $('.userlist option');
			for(var i=0;i<option.length;i++){
				var txt = $(option[i]).text();
				if($.inArray(txt, userlist)<0){
					$(option[i]).remove();
				}
			}
		}
		this.healthTimer = setInterval(refreshUserlist, 3000);
		
		this.onopen = function(e){ 
			chatIO.send({"type":2,"uid":nickname,"token":"password","domain":domain});
		}
		this.onclose = function(e){ 
			logMsg('Disconnect');
			removeUser(nickname);
			if(this_.healthTimer)
				clearInterval(this_.healthTimer);
		}
		this.onmessage = function(msg){ 
			if(msg.type == 0 && msg.title=='Register'){
				logMsg(msg.body + '   ('+nickname+')');
				if('Register succ'==msg.body){
					addUser(nickname);
				}
			}else if(msg.type == 3){
				var event = new connectz.json(msg.ext).decode();
				sysMsg(event.from, event.msg, 'rgb(43, 170, 36)');
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
				var to  =  $(".userlist").find("option:selected").text(); 
				if(!to){
					if($(".userlist option").length==0)
						logMsg('Open a new Tab and login one test user');
					else
						logMsg('Pls select a recipient');
				}
				else{
					to = to + '@' + domain;
					var ext = new connectz.json({"msg":msg,"from":nickname}).toString();
					chatIO.send({"type":3,"to":to,"ext":ext});
					sysMsg(nickname, msg, 'rgb(56, 36, 170)');
				}
				$(this).val('');
			}
		});
	}
	
	
	
	var addUser = function(user){
		var users = connectz.cookies.getCookie('cz-users');
		if(!users){
			users = user;
		}else{
			users = users + ','+user;	
		}
		connectz.cookies.setCookie('cz-users',users,7*60*24);
		console.log(users);
	}
	
	var removeUser = function(user){
		var users = connectz.cookies.getCookie('cz-users');
		if(users){
			 var userlist = users.split(',');
			 users = '';
			 for(var u of userlist){
				 if(u != user){
					 if(!users){
						users = u;
					 }else{
						users = users + ','+u; 
					 }
				 }
			 }
			 connectz.cookies.setCookie('cz-users',users,7*60*24);
		}
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