	//灯箱过渡效果
    $(document).ready(function() {
        $("#login").click(function() {
            $("#LoginBox").fadeIn("slow");
            $("#loginMenu").fadeIn(3);
            $("#BoxInput").fadeIn(30000);
            dj();
        });
        //表单验证
        var ischeck=true;
        $('.input input').bind('input',function () {
            //验证码
            if($(this).val().length<4&&$('input').index(this)==2){
                $('.toast_code').css('background-image',"url('images/hebingxingzhuangx.png')");
                $('.toast_code').show("slow")
                $('.toast_code').bind('click',function (){
                    $('.input input').eq(2).val('');
                });
            }else{
                var val = document.getElementById("code").value;
                var num = show_num.join("");
                if(val == num){
                    $('.toast_code').css('background-image',"url('images/dui.png')");
                    $('.toast_code').show("slow")
                    $('.toast_code').unbind('click');
                }
            }
            //用户名和密码
            if($(this).val().length<8){
                if($('input').index(this)==1){
                    $('label').eq(1).text('密码必须大于八位!')
                    $('label').eq(1).css('color','red');
                    $('label').eq(1).css('display','block');
                    $('label').eq(1).css('margin-bottom','-12px');
                    $('label').eq(1).css('margin-top','-10px');
                    $('label').eq(1).show("slow")
                    $('.toast_pass').css('background-image',"url('images/hebingxingzhuangx.png')");
                    $('.toast_pass').show("slow")
                    $('.toast_pass').bind('click',function (){
                        $('.input input').eq(1).val('');
                    });
                }else if($('input').index(this)==0){
                    $('label').eq(0).css('display','block');
                    $('label').eq(0).css('margin-bottom','20px');
                    $('label').eq(0).css('margin-top','-10px');
                    $('label').eq(0).text('用户名必须大于八位!')
                    $('label').eq(0).css('color','red');
                    $('label').eq(0).css('height','18px');
                    $('label').eq(0).show("slow")
                    $('.toast_user').css('background-image',"url('images/hebingxingzhuangx.png')");
                    $('.toast_user').show("slow")
                    $('.toast_user').bind('click',function (){
                        $('.input input').eq(0).val('');
                    });
                }
            }else{
                if($('input').index(this)==1){
                    $('label').eq(1).text('')
                    $('label').eq(1).hide("slow")
                    $('.toast_pass').css('background-image',"url('images/dui.png')");
                    $('.toast_pass').show("slow")
                    $('.toast_pass').unbind('click');

                }else{
                    $('label').eq(0).text('')
                    $('label').eq(0).hide("slow")
                    $('.toast_user').css('background-image',"url('images/dui.png')");
                    $('.toast_user').show("slow")
                    $('.toast_user').unbind('click');

                }
            }
        });
        //查看密码
        $('.check').bind('click',function(){
            if (ischeck){
                ischeck=false;
                $('.check').css('background-image',"url('images/隐藏.png')");
                $('#password').attr('type','text');

            }else{
                ischeck=true;
                $('.check').css('background-image',"url('images/显示.png')");
                $('#password').attr('type','password');
            }
        });
    });
        function closeLogin() {
            document.getElementById('LoginBox').style.display = "none";
        }
        var show_num = [];
        draw(show_num);
        function dj() {
            draw(show_num);
        }
        function sublim() {
            var val = document.getElementById("code").value;
            var num = show_num.join("");
            if (val == '') {
                popup({type:'tip',msg:"请输入验证码",delay:1200});
            } else if (val == num) {
                // alert('登录成功！');
                // document.getElementById(".input-val").val('');
                // draw(show_num);
                popup({type:'success',msg:"登录成功",delay:1200})
                //location.replace(location.href);
            } else {
                popup({type:'error',msg:"验证码错误！",delay:1200});
                document.getElementById("text").value = '';
                draw(show_num);
            }
        }
        //验证码
        function draw(show_num) {
            var canvas_width = document.getElementById('canvas').clientWidth;
            var canvas_height = document.getElementById('canvas').clientHeight;
            var canvas = document.getElementById("canvas"); //获取到canvas的对象，演员
            var context = canvas.getContext("2d"); //获取到canvas画图的环境，演员表演的舞台
            canvas.width = canvas_width;
            canvas.height = canvas_height;
            var sCode = "A,B,C,E,F,G,H,J,K,L,M,N,P,Q,R,S,T,W,X,Y,Z,1,2,3,4,5,6,7,8,9,0,q,w,e,r,t,y,u,i,o,p,a,s,d,f,g,h,j,k,l,z,x,c,v,b,n,m";
            var aCode = sCode.split(",");
            var aLength = aCode.length; //获取到数组的长度

            for (var i = 0; i <= 3; i++) {
                var j = Math.floor(Math.random() * aLength); //获取到随机的索引值
                var deg = Math.random() * 30 * Math.PI / 180; //产生0~30之间的随机弧度
                var txt = aCode[j]; //得到随机的一个内容
                show_num[i] = txt;
                var x = 5 + i * 15; //文字在canvas上的x坐标
                var y = 15 + Math.random() * 3; //文字在canvas上的y坐标
                context.font = "bold 19px 微软雅黑";

                context.translate(x, y);
                context.rotate(deg);

                context.fillStyle = randomColor();
                context.fillText(txt, 0, 0);

                context.rotate(-deg);
                context.translate(-x, -y);
            }
            for (var i = 0; i <= 5; i++) { //验证码上显示线条
                context.strokeStyle = randomColor();
                context.beginPath();
                context.moveTo(Math.random() * canvas_width, Math.random() * canvas_height);
                context.lineTo(Math.random() * canvas_width, Math.random() * canvas_height);
                context.stroke();
            }
            for (var i = 0; i <= 30; i++) { //验证码上显示小点
                context.strokeStyle = randomColor();
                context.beginPath();
                var x = Math.random() * canvas_width;
                var y = Math.random() * canvas_height;
                context.moveTo(x, y);
                context.lineTo(x + 1, y + 1);
                context.stroke();
            }
        }

        function randomColor() { //得到随机的颜色值
            var r = Math.floor(Math.random() * 256);
            var g = Math.floor(Math.random() * 256);
            var b = Math.floor(Math.random() * 256);
            return "rgb(" + r + "," + g + "," + b + ")";
        }

    // 对话框例子
    // clickDomCancel:true, 点击遮罩层，弹出效果立即消失 默认false
    // delay:1000 时间,1000ms
    // bg:true ,是否显示背景遮罩层 默认有浅黑色背景遮罩
    // callBack 回调函数
    // width:180, 动画容器的宽度 宽度默认180px
    // height:150,  动画容器的高度 宽度默认150px
    $('#success').click(function(){
        popup({type:'success',msg:"操作成功",delay:1000,callBack:function(){
                console.log('callBack~~~');
            }});
    })
    $('#error').click(function(){
        popup({type:'error',msg:"操作失败",delay:1000,bg:true,clickDomCancel:true});
    })
    $('#load').click(function(){
        popup({type:'load',msg:"请等待",delay:1500,callBack:function(){
                popup({type:"success",msg:"加载成功",delay:1000});
            }});
    })
    $('#tip').click(function(){
        popup({type:'tip',msg:"提示信息",delay:null});
    })

    