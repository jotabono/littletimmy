$(document).ready(init);

function init() {
    $(window).scroll(function () {
        if($(window).scrollTop()>=100){
            $(".navbar").css('background','rgba(85, 172, 238, 0.8)');
        } else {
            $(".navbar").css('background','rgba(85, 172, 238, 1)');
        }
    });
}
