$(function () {
    console.log($("#isSuccess").text())
    console.log($("#isSuccess").text()=="true")
    if ($("#isSuccess").text()=="true") {
        $(".am-alert").css("display","block")
        setInterval(function() {
            $(".am-alert").css("display","none")
        }, 2000);
    }
})
