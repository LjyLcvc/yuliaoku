 //语言切换
 // 匿名函数
 $(function(){
    //切换下拉
    $('#language-btn').click(function(){
        if($("#language-menu").css("display")=="none"){
            $("#arrow").css("transform","rotate(180deg)");
            $("#language-menu").slideDown('slow')
        }else{
            $("#arrow").css("transform","rotate(360deg)");
            $("#language-menu").slideUp('slow')
        }
    })
    //清除文本框的内容
    $(".icon-del").click(function () {
        $("#text").val("");
        //jq对象转js对象
        $('#textarea-input').get(0).style.border="rgb(233, 233, 238) 1px solid";
        $('#quit').html("");
        $('#textarea-output').text("");
    })

})
 //下拉更新
 function cl_most() {
     $("#button").remove();
    //  //附加内容
    //  var html="<p style=\"color:#333333;text-align:left;\">1.noun</p>\n" +
    //      "<p style=\"font-family: '黑体';color:#666666;font-size: 16.2px;\">(汽车等的) 转向柱</p>\n" +
    //      "<p style=\"margin-bottom:8%;color:#666666;font-size: 16.2px;\">the part of a car or other vehicle that the steering wheel is fitted on</p>\n" +
    //      "<div id=\"button\" style=\"margin-left:-1.5em;margin-bottom:1.5em;text-align:center\"><button id=\"most\" style=\"background:url('images/折叠3.png') no-repeat center;border:0;height:16px;width: 16px;outline:0;\" onclick=\"cl_most()\"> </button>" +
    //      "</div>";
    //  $("#quit").append(html)
    //  //动态添加的元素,使用on或者live来绑定事件,不然元素绑定的事件会无效果.
    //  $("#most").on("click",function () {
    //      $("#button").remove();
    //      $("#quit").append(html)
    //  })

    $.ajax({
        url: 'http://112.124.19.158:8091/api/frontdesk/material_translation/chineseToEngLish?' +"chinese=" + text,
        type: 'get',
        data: $('#input').serialize(), //表单序列化
        success:function(data){
            console.log(data);
        }
    });

 }
 //文本框改变时(检测输入框是否输入)
 function Input(even) {
     if($(even).val()!==""){
         $('#textarea-input').get(0).style.border="1px solid blue";
     }else{
         $('#textarea-input').get(0).style.border="rgb(233, 233, 238) 1px solid";
     }
 }


$(document).ready(function() {
    // 选中修改选择中
    $(".language").click(function() {
        var langSel = $(this).text();
        var langVal = $(this).attr("value"); //获取.language的value，并添加修改#span-sel的value
        $("#span-sel").html(langSel);
        $('#span-sel').attr('value', langVal);
        $("#arrow").css("transform","rotate(360deg)");
    });
});





  // 关闭翻译语言菜单

  function menuClose() {
    document.getElementById("language-menu").style.display = "none";
}