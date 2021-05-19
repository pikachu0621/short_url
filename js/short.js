let xmlHttp = null;
//定时器
let r1 = null, r2 = null;


window.onload = function () {
    dynamicSize();
}
//调整窗口
window.onresize = function () {
    dynamicSize();
}

//动态布局
function dynamicSize() {
    const offsetWid = document.documentElement.clientWidth;
    const offsetHei = document.documentElement.clientHeight;

    const footer_id = document.getElementById("footer_id");
    const title_id = document.getElementById("title_id");
    const edit_id = document.getElementById("edit_id");
    const edit2_id = document.getElementById("edit2_id");
    const toast_id = document.getElementById("toast_id");


    if (offsetWid < offsetHei) {
        if (isMobile()) {
            //小屏幕
            title_id.style.marginTop = (offsetHei * 0.1) + "px";
            edit2_id.style.marginTop = (offsetHei * 0.1) + "px";
            footer_id.style.bottom = "0px";
            edit_id.style.display = "none"; //隐藏翻页下的编辑框
            edit2_id.style.display = "block"; //显示主页编辑框
            toast_id.style.width = "80%";
        }

    } else {
        if (isMobile()) {
            edit2_id.style.marginTop = (offsetHei * 0.1) + "px";
        }
        if (!isMobile()) {
            //大屏幕
            title_id.style.marginTop = (offsetHei / 3) + "px";
            footer_id.style.bottom = "10%";
            edit_id.style.display = "block"; //显示翻页下的编辑框
            edit2_id.style.display = "none"; //显示主页编辑框
            toast_id.style.width = "30%";
        }
    }


}

function sll() {
    //alert("JavaScript");
    //document.body.scrollTop= 20;
    document.body.scrollTop = 5;

}


function isMobile() {
    const sUserAgent = navigator.userAgent.toLowerCase();
    const bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
    const bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
    const bIsMidp = sUserAgent.match(/midp/i) == "midp";
    const bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
    const bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
    const bIsAndroid = sUserAgent.match(/android/i) == "android";
    const bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
    const bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
    if (bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM)
        //移动
        return true;
    //PC
    return false;
}


function addShort(id) {

    const input_id = document.getElementById(id);
    if (input_id.value == null || input_id.value === '') {
        toast(2, "网址或短链不能为空");
        return;
    }

    loadXMLDoc('./api/?url=' + input_id.value + '&t=1', function () {
        if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
            const json = JSON.parse(xmlHttp.responseText);
            if (json["fs"]) {
                toast(3, json["msg"], json["short"], "复制", 5000);
                input_id.value = json["short"];
            } else toast(1, json["msg"]);
        }else toast(1, '请求失败:'+xmlHttp.readyState);
    });


}

function openUrl(id) {


    const input_id = document.getElementById(id);
    if (input_id.value == null || input_id.value === '') {
        toast(2, "网址或短链不能为空");
        return;
    }

    loadXMLDoc('./api/?url=' + input_id.value + '&t=2', function () {
        if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
            const json = JSON.parse(xmlHttp.responseText);
            if (json["fs"]) {
                toast(3, json["msg"]);
                //input_id.value = json["short"];
            } else toast(1, json["msg"]);

        }else toast(1, '请求失败:'+xmlHttp.readyState);
    });

}

function checkNum(id) {


    const input_id = document.getElementById(id);
    if (input_id.value == null || input_id.value === '') {
        toast(2, "网址或短链不能为空");
        return;
    }

    loadXMLDoc('./api/?url=' + input_id.value + '&t=3', function () {
        if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
            const json = JSON.parse(xmlHttp.responseText);
            if (json["fs"]) {
                toast(3, json["msg"] + "，访问量" + json["bat"] + "次",);
            } else toast(1, json["msg"]);

        }else toast(1, '请求失败:'+xmlHttp.readyState);

    });


}


/**
 * 提示
 * @param type ==1错误  ==2警告 ==3成功
 * @param str
 * @param url_str
 * @param bottom
 * @param time
 */
function toast(type, str, url_str = null, bottom = null, time = 3000) {

    const toast_id1 = document.getElementById("toast_id1");
    const toast_id = document.getElementById("toast_id");

    //if (toast_id1.style.display.toString() === "")
    if (r2 != null) {
        clearInterval(r2);
    }

    if (r1 != null) {
        clearInterval(r1);//移除上一次的定时器
        toast_id1.style.display = "none";
    }

    r2 = setTimeout(function () {
        toast_id1.style.display = "block";

        //toast_id1.style.display = "block";
        r1 = setTimeout(function () {
            toast_id1.style.display = "none";
        }, time);
        let html = ``, color = "#a7ff9d";

        if (type === 1) {
            color = "#ff9daf";
        } else if (type === 2) {
            color = "#ffffff";
        }
        html += `<strong style="color: ${color}"> ${str} </strong>`;
        if (url_str !== null && url_str !== "")
            html += `<samp><a  id="copy" href="${url_str}" target="_blank" style="color: #FFFFFF" >${url_str}</a></samp>`;
        if (bottom != null && bottom !== "")
            html += `<button type="button" class="btn btn-primary container"  id="toast_copy"  onclick="copyTkl() ">${bottom}</button>`;
        toast_id.innerHTML = html;

    }, 200);
}


function copyTkl() {
    const range = document.createRange();
    range.selectNode(document.getElementById('copy'));
    const selection = window.getSelection();
    if (selection.rangeCount > 0) selection.removeAllRanges();
    selection.addRange(range);
    document.execCommand('copy');
    if (r1 != null) {
        clearInterval(r1);
        document.getElementById("toast_id1").style.display = "none";
    }
}


function loadXMLDoc(getUrl, fun) {

    if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    } else {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlHttp.onreadystatechange = fun;
    xmlHttp.open("GET", getUrl, true);
    xmlHttp.send();
}


