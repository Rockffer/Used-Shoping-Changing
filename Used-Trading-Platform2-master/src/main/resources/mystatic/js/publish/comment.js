// 发送评论按钮

$(function () {
    $('.nav_send_comment_button').click(function () {

        var value=$('.nav_comment_textarea').val();
        var token=$('.val_token').val();
        var $comment = $('.nav_comment_content');
        $.ajax({
            url: 'insertUserContext.do',
            type: 'post',
            dataType:'JSON',
            data:{context:value,token:token},
            success:function (data) {
                var result = data.result;
                if (result == 2){
                    alert('请先登录！！！');
                } else if (result == 0){
                    alert("发表留言失败，请先检查格式");
                }else if(result==1){
                    var name = data.username;
                    var time = data.time;
                    var context = data.context;
                    var cc = "<div class='nav_one_comment'><span class='nav_username'>用户："+name+"</span><span class='time'>发表于："+time+"</span><p class='nav_content'>"+context+"</p></div>";
                    $comment.append(cc);
                }
            }
        });
    });
});