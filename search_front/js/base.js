var baseUrl = "http://localhost:10010/"; // 公共路径
var urls = {
    search_goods : baseUrl + "search-service/search",
    find_good_byid : baseUrl + "goods-service/goods/",
    find_order_info_byid: baseUrl + "order-service/order/submitInfo/",
    submit_order : baseUrl + "order-service/order/submitOrder",
    find_order_byid: baseUrl + "order-service/order/findById/",
    pay_order_success: baseUrl + "order-service/order/paySuccess/",

    // search_goods : "http://localhost:9201/search",
    // find_good_byid : "http://localhost:9101/goods/",
    // find_order_info_byid: "http://localhost:9401/order/submitInfo/",
    // submit_order : "http://localhost:9401/order/submitOrder",
    // find_order_byid: "http://localhost:9401/order/findById/",
    // pay_order_success: "http://localhost:9401/order/paySuccess/",
}


/**
 * 获取请求路径?后面的参数值
 * @returns {Object}
 * @constructor
 */
function GetRequest() {
    var url = location.search; //获取url中"?"符后的字串
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for(var i = 0; i < strs.length; i ++) {
            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
    }
    return theRequest;
}

