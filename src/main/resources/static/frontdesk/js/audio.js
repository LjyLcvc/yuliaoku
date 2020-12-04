// 音频效果
window.onload = function(){
    var audio = document.getElementById('recording');
    audio.pause();//打开页面时无音乐
}
function play(){
    var audio = document.getElementById('recording');
    if(audio.pause){
        audio.play();
        document.getElementById('recording').src="images/Recorder.png";
    }else{
        audio.pause();
        audio.currentTime = 0;//音乐从头开始
        document.getElementById('recording').src="images/Recorder.png";
    }
}

window.onload = function(){
    var audio = document.getElementById('broadcast');
    audio.pause();//打开页面时无音乐
}
function play(){
    var audio = document.getElementById('broadcast');
    if(audio.pause){
        audio.play();
        document.getElementById('broadcast').src="images/lb.png";
    }else{
        audio.pause();
        audio.currentTime = 0;//音乐从头开始
        document.getElementById('broadcast').src="images/lb.png";
    }
}
