$(function () {

    $("#miss-user-button").click(function () {
        getHttpResponse("/miss/users", "", function (e) {
            let userInfos = JSON.parse(e);
            let trList = "";
            for (let i = 0; i < userInfos.length; i++) {
                let userInfo = userInfos[i];
                trList += "<tr><td>" + userInfo.id + "</td>\n" +
                    "<td>" + userInfo.name + "</td>\n" +
                    "<td>" + userInfo.sex + "</td>\n" +
                    "<td>" + userInfo.className + "</td></tr>"
            }
            $("#miss-user-list").html(trList)
        })
    });

    //选择用户 展示历史记录
    $("#select-user").click(function () {
        getHttpResponse("/today/user/list", "", function (e) {
            let userLsit = JSON.parse(e);
            let content = "";
            for (let i = 0; i < userLsit.length; i++) {
                let userInfo = userLsit[i];
                content += "<tr>\n" +
                    "<td id='select-user-id'>" + userInfo.id + "</td>\n" +
                    "<td>" + userInfo.name + "</td>\n" +
                    "<td>" + userInfo.sex + "</td>\n" +
                    "<td>" + userInfo.cardId + "</td>\n" +
                    "<td>" + userInfo.className + "</td>\n" +
                    "<td><div class=\"am-btn-toolbar\">\n" +
                    "<div class=\"am-btn-group am-btn-group-xs \">\n" +
                    "<button class=\"am-btn am-btn-default am-btn-xs am-text-secondary user-select-button\" onclick='userSelect(\"" + userInfo.id + "\")'><span class=\"am-icon-check-square-o\"></span> 选择</button>\n" +
                    "</div>\n" +
                    "</div></td>\n" +
                    "</tr>\n";
            }
            $("#today-user-list").html(content)
        })
        //选择用户

    });


    openSocket();

    initUserData();
});

function userSelect(id) {
    console.log("userId:" + id)
    $("#doc-modal-2").modal("close");
    updateEChart(id);
    getHttpResponse("/userInfo/" + id, "", function (e) {
        addTable(JSON.parse(e))
    })
}


function initUserData() {
    //当天所有人温度
    let temperatureList = JSON.parse($("#sumOfDateInDay").val());
    initEchartsData(temperatureList, "all")
    //当天发烧人员
    let todayUserList = JSON.parse($("#todayUserInfo").val());
    for (let i = 0; i < todayUserList.length; i++) {
        addFaverTable(todayUserList[i])
    }
    let showUser = null;
    //默认展示第一个发烧人员信息
    if (todayUserList.length >= 1) {
        showUser = todayUserList;
    } else {
        showUser = temperatureList
    }
    getHttpResponse("/userInfo/" + showUser[0].id, "", function (e) {
        addTable(JSON.parse(e))
    })
    updateEChart(showUser[0].id)
    initEchartsData(showUser, "single");

}

function initEchartsData(tempList, id) {
    let xData = new Array();
    let yData = new Array();
    for (let i = 0; i < tempList.length; i++) {
        let time = new Date(tempList[i].gmtCreate).format("yyyy-MM-dd hh:mm:ss");
        let tempData = new Array();
        tempData.push(tempList[i].name);
        tempData.push(time);
        xData.push(time);
        yData.push(tempList[i].temperature);
    }
    if (id !== "all") {
        singleUserData(xData, yData);
        return;
    }
    echartsBuild(xData, yData)
}

function echartsBuild(x, data) {
    console.log(data)
    var myChart1 = echarts.init(document.getElementById('tpl-echarts-A'));
// 指定图表的配置项和数据
    option = {
        tooltip: {
            trigger: 'axis',
            position: function (pt) {
                return [pt[0], '10%'];
            }
        },
        title: {
            left: 'center',
            text: '今日体温数据总览',
        },
        toolbox: {
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                restore: {},
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: true,
            data: x
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, '100%']
        },
        dataZoom: [{
            type: 'inside',
            start: 0,
            end: 100
        }, {
            start: 0,
            end: 10,
            handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
            handleSize: '100%',
            handleStyle: {
                color: '#fff',
                shadowBlur: 3,
                shadowColor: 'rgba(0, 0, 0, 0.6)',
                shadowOffsetX: 2,
                shadowOffsetY: 2
            }
        }],
        series: [
            {
                name: "实时温度",
                type: 'line',
                smooth: true,
                symbol: 'none',
                sampling: 'average',
                itemStyle: {
                    color: 'rgb(255, 70, 131)'
                },
                areaStyle: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: 'rgb(255, 70, 131)'
                    }, {
                        offset: 1,
                        color: 'rgb(255, 158, 68)'
                    }])
                },
                data: data
            }
        ]
    };
// 使用刚指定的配置项和数据显示图表。
    myChart1.setOption(option);
}

function singleUserData(xData, data) {
    var myChart2 = echarts.init(document.getElementById('tpl-echarts-B'));
    let option = {
        title: {
            text: '个人体温数据',
            left: 'center'
        },
        xAxis: {
            type: 'category',
            data: xData
        },
        yAxis: {
            type: 'value'
        },
        tooltip: {
            data: data
        },
        dataZoom: [{
            type: 'inside',
            start: 0,
            end: 100
        }, {
            start: 0,
            end: 10,
            handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
            handleSize: '100%',
            handleStyle: {
                color: '#fff',
                shadowBlur: 3,
                shadowColor: 'rgba(0, 0, 0, 0.6)',
                shadowOffsetX: 2,
                shadowOffsetY: 2
            }
        }],
        series: [{
            data: data,
            type: 'line',
            smooth: true,
            symbolSize: 16,
            label: {
                normal: {
                    show: true,
                    position: 'top'
                }
            },
        }]
    };

    myChart2.setOption(option);
}


function addTable(userInfo) {
    let sex = userInfo.sex === 1 ? "男" : "女";
    let content = "<thead>\n" +
        "<tr>\n" +
        "<td>ID</td>\n" +
        "<td>名字</td>\n" +
        "<td>性别</td>\n" +
        "<td>部门</td>\n" +
        "<td>卡号</td>\n" +
        "</tr>\n" +
        "</thead>\n" +
        "<tbody>\n" +
        "<tr>\n" +
        "<tr>\n" +
        "<td>" + userInfo.id + "</td>\n" +
        "<td>" + userInfo.name + "</td>\n" +
        "<td>" + sex + "</td>\n" +
        "<td>" + userInfo.className + "</td>\n" +
        "<td>" + userInfo.cardId + "</td>\n" +
        "</tr>\n" +
        "</tr>\n" +
        "</tbody>";
    $("#sigle-user").html(content)

}

function addFaverTable(userInfo) {
    let trCount = $("#all-temperature-list").children("#temperature-tr-list").length
    if (trCount > 10) {
        $("#all-temperature-list").children("#temperature-tr-list").eq(0).remove()
    }
    $("#all-temperature-list").append("<tr id='temperature-tr-list'>\n" +
        "<td>" + userInfo.id + "</td>\n" +
        "<td>" + userInfo.name + "</td>\n" +
        "<td>" + userInfo.sex + "</td>\n" +
        "<td>" + userInfo.cardId + "</td>\n" +
        "<td>" + userInfo.className + "</td>\n" +
        "<td style='color: #be2924'>" + userInfo.temperature + "</td>\n" +
        "<td>" + new Date(userInfo.gmtCreate).format("yyyy-MM-dd hh:mm:ss") + "</td>\n" +
        "<td></td>\n" +
        "</tr>\n")
}


var socket;

function openSocket() {
    if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
    } else {
        console.log("您的浏览器支持WebSocket");
        var socketUrl = "http://localhost:8089/imserver/10";
        socketUrl = socketUrl.replace("https", "ws").replace("http", "ws");
        console.log(socketUrl);
        if (socket != null) {
            socket.close();
            socket = null;
        }
        socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function () {
            console.log("websocket已打开");
            //socket.send("这是来自客户端的消息" + location.href + new Date());
        };
        //获得消息事件
        socket.onmessage = function (msg) {
            console.log(msg.data);
            const userInfo = JSON.parse(msg.data);
            //更新顶部计数
            updateCount(userInfo)
            //更新大盘数据的数据
            updateEChart("all")
        };
        //关闭事件
        socket.onclose = function () {
            console.log("websocket已关闭");
        };
        //发生了错误事件
        socket.onerror = function () {
            console.log("websocket发生了错误");
        }
    }


    function updateCount() {
        getHttpResponse("/user/list", "", function (data) {
            data = JSON.parse(data)
            $("#user-count").html(data.userCount);
            $("#actual-user-count").html(data.todayCount);
            $("#faver-count").html(data.feverUser);
            $("#miss-count").html(data.userCount - data.todayCount);
        })
    }
}

function updateEChart(id) {
    getHttpResponse("/echart/update/" + id, {}, function (e) {
        let tempList = JSON.parse(e);
        initEchartsData(tempList, id);
    })
}

function getHttpResponse(url, data, action) {
    $.ajax({url: url, context: data, success: action})
}

//时间格式化
Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "h+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };

    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }

    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(
                RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }

    return fmt;
}