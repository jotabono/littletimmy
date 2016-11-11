$(document).ready(init);

function init() {
    var colorInitWebkit = "-webkit-linear-gradient(top right, rgb(73, 255, 179) 20%, rgb(57, 148, 153) 100%)";
    var colorInit = "linear-gradient(top right, rgb(73, 255, 179) 20%, rgb(57, 148, 153) 100%)";

    $(window).scroll(function () {
        var url = window.location.href.split('/#');
        if (url[1] == "/login") {
            $(".navbar").css('background', colorInit);
            $(".navbar").css('background', colorInitWebkit);
        }
        else {
            if ($(window).scrollTop() >= 100) {
                $(".navbar").css('background', 'rgba(3, 73, 123, 0.8)');
            } else {
                $(".navbar").css('background', 'rgba(3, 73, 123, 1)');
            }
        }
    });
}

