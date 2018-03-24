$.urlParam = function(name) {
	var results = new RegExp('[\?&]' + name + '=([^&#]*)')
			.exec(window.location.href);
	if (results == null) {
		return null;
	} else {
		return decodeURI(results[1]) || 0;
	}
}
$.winUrlParam = function(win,name) {
	var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(win.location);
	if (results == null) {
		return null;
	} else {
		return decodeURI(results[1]) || 0;
	}
}

window.onload=function(){
			$('#get_token').click();
};
$(document).ready(function() {
            $('#get_token').click(function ()
            {

            	$('#authorize').attr("disabled","disabled");
            	document.getElementById("oauth_timestamp").value=parseInt(new Date().getTime()/1000);
            	
            	console.log("get_token");
                $.ajax({
                    type: "post",
                    url: "/get_oauth_token", 
                    data: "input=twitter",
                    success: function(msg){   

                    		console.log(msg);
                    	var str=msg.split("&");
                    	document.getElementById("oauth_token_input").value=msg.trim();
                    	document.getElementById("oauth_token").value=str[0].trim();
                    	document.getElementById("oauth_token_secret").value=str[1].trim();
                    	$('#authorize').attr("disabled",false);

                    },
                    error:function(msg){
                    	console.log("error1"+msg)
                    }
                });
            });
            $('#authorize').click(function ()
                    {
            			console.log("authorize");

            			console.log($('#oauth_token').val());
            			var a=$('#oauth_token')
                    	var data=$('#oauth_token_input').val();
                    	console.log(data);
            			$.ajax({
                            type: "post",
                            url: "/authorize_me", 
                            data: a,
                            success: function(msg){   
                            		console.log(msg.trim());
                                    $('#login_block').html(msg);
                            		var win=window.open('about:blank');
                            		win.location=msg.trim();
                                    with(win.document)
                                    {
                                        //open();
                                        //write(msg);
                                        var waiting=setInterval(function(){
                                        	var oauth_t=$.winUrlParam(win,"oauth_token");
                                        	var oauth_v=$.winUrlParam(win,"oauth_verifier");
                                        	if(oauth_t==null || oauth_v==null){
                                        		console.log(win);
                                        		console.log(win.location);
                                        		console.log("Auth token and varifier doesnot exist"+win.location);
                                        	}else{
                                        		console.log("Auth token and varifier found");
                                            	$('#oauth_token_verifier').val(oauth_t);
                                            	$('#oauth_verifier').val(oauth_v);
                                                clearInterval(waiting);
                                                win.close();

                                        	}
                                        }, 1000);

                                        //close();
                                        
                                    }
                                    console.log(win);
                                    console.log("Hello new twitter auth window");
                            },
                            error:function(msg){
                            	console.log("error_oauth_token"+msg)
                            }
                        });
                    });
                    
            $('#validate').click(function ()
                    {
            			console.log("validate");
            			console.log($('#oauth_verifier').val());
            			//+"&"+$('#oauth_token').val()
            			var data="oauth_verifier="+$('#oauth_verifier').val()+"&"+"oauth_token_verifier="+$('#oauth_token_verifier').val();

            			console.log(data);
                    	$.ajax({
                            type: "post",
                            url: "/validate_me", 
                            data: data,
                            success: function(msg){ 
                            		msg=msg.trim();
                            		console.log(msg);
                            		document.getElementById('verified_token').value=msg;
                                    $('#verified_block').html(msg);
                                    
                            		
                            },
                            error:function(msg){
                            	console.log("error_validate_token"+msg.toString());
                            }
                        });
                    });
                    
            $('#timeline').click(function ()
                    {
            			console.log("timeline");
            			//console.log($('#verified_token').val());
            			//+"&"+$('#oauth_token').val()
            			var data=$('#verified_token').val()+"&timestamp="+$('#oauth_timestamp').val()+"&oauth_consumer_key="+$('#oauth_consumer_key').val()+"&oauth_nonce="+$('#oauth_nonce').val()+"&oauth_consumer_secret="+$('#oauth_consumer_secret').val();
            			console.log("data is"+data);
                    	$.ajax({
                            type: "get",
                            url: "/get_timeline", 
                            data: data,
                            success: function(msg){   
                            		console.log(msg.trim());
                            		var timeline=JSON.parse(msg);
                            		var data="";
                            		
                                    $('#api_result_block').html(JSON.stringify(timeline,null,2));
                            		
                            },
                            error:function(msg){
                            	console.log("error_timeline_token"+msg.toString());
                            }
                        });
                    });
            
            
            
            $('#friends').click(function ()
                    {
            			console.log("triends");
            			//console.log($('#verified_token').val());
            			//+"&"+$('#oauth_token').val()
            			var data=$('#verified_token').val()+"&timestamp="+$('#oauth_timestamp').val()+"&oauth_consumer_key="+$('#oauth_consumer_key').val()+"&oauth_nonce="+$('#oauth_nonce').val()+"&oauth_consumer_secret="+$('#oauth_consumer_secret').val();
            			console.log("data is"+data);
                    	$.ajax({
                            type: "get",
                            url: "/get_friend_list", 
                            data: data,
                            success: function(msg){   
                            	console.log(msg.trim());
                        		var timeline=JSON.parse(msg);
                        		var data="";
                        		
                                $('#api_result_block').html(JSON.stringify(timeline,null,2));
                        		
                            },
                            error:function(msg){
                            	console.log("error_friends_token"+msg.toString());
                            }
                        });
                    });
            $('#followers').click(function ()
                    {
            			console.log("followers");
            			//console.log($('#verified_token').val());
            			//+"&"+$('#oauth_token').val()
            			var data=$('#verified_token').val()+"&timestamp="+$('#oauth_timestamp').val()+"&oauth_consumer_key="+$('#oauth_consumer_key').val()+"&oauth_nonce="+$('#oauth_nonce').val()+"&oauth_consumer_secret="+$('#oauth_consumer_secret').val();
            			console.log("data is"+data);
                    	$.ajax({
                            type: "get",
                            url: "/get_follower_list", 
                            data: data,
                            success: function(msg){   
                            	console.log(msg.trim());
                        		var timeline=JSON.parse(msg);
                        		var data="";
                        		
                                $('#api_result_block').html(JSON.stringify(timeline,null,2));
                        		                            		
                            },
                            error:function(msg){
                            	console.log("error_tollowers_token"+msg.toString());
                            }
                        });
                    });
                    


});