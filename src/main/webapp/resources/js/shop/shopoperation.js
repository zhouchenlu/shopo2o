/**
 *
 */
$(function () {
    //获取shopId
    shopId = getQueryString('shopId');
    //shopId有可能是空
    isEdit = shopId ? true : false;
    //获取店铺的初始信息
    initUrl = '/ShopO2O/shopadmin/getshopinitinfo';
    //注册店铺
    registerShopUrl = '/ShopO2O/shopadmin/registershop';
    //根据店铺id获取店铺信息
    shopInfoUrl = '/ShopO2O/shopadmin/getshopbyid?shopId=' + shopId;
    editShopUrl = '/ShopO2O/shopadmin/modifyshop';
    //页面加载完毕后自动执行
    //判断是编辑操作还是注册操作
    if (!isEdit) {
        getShopInitInfo();
    } else {
        getShopInfo(shopId);
    }

    // 通过店铺Id获取店铺信息
    function getShopInfo(shopId) {
        $.getJSON(shopInfoUrl, function (data) {
            if (data.success) {
                // 若访问成功，则依据后台传递过来的店铺信息为表单元素赋值
                var shop = data.shop;
                $("#shop-name").val(shop.shopName);
                $("#shop-addr").val(shop.shopAddr);
                $("#shop-phone").val(shop.phone);
                $("#shop-desc").val(shop.shopDesc);
                // 给店铺类别选定原先的店铺类别值
                var shopCategory = '<option data-id="'
                    + shop.shopCategory.shopCategoryId + '" selected>'
                    + shop.shopCategory.shopCategoryName + '</option>';
                var tempAreaHtml = '';
                // 初始化区域列表
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                $("#shop-category").html(shopCategory);
                // 不允许选择店铺类别
                $("#shop-category").attr('disabled', 'disabled');
                $("#area").html(tempAreaHtml);
                // 给店铺选定原先的所属的区域
                $("#area option[data-id='" + shop.area.areaId + "']").attr(
                    "selected", "selected");
            }
        });
    }

    //获取店铺的基本信息
    function getShopInitInfo() {
        //是一个获取信息的JSON方法，是以JSON串形式返回的
        $.getJSON(initUrl, function (data) {
            if (data.success) {
                var tempHtml = "";
                var tempAreaHtml = "";
                data.shopCategoryList.map(function (item, index) {
                    tempHtml += '<option data-id="' + item.shopCategoryId + '">'
                        + item.shopCategoryName + '</option>';
                });
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                //获取信息后就加进前台设定好的id里
                $("#shop-category").html(tempHtml);
                $("#area").html(tempAreaHtml);
            }
        });
    }
});

//提交按钮，获取数据
$("#submit").click(function () {
    var shop = {};
    if (isEdit) {
        // 若属于编辑，则给shopId赋值
        shop.shopId = shopId;
    }
    shop.shopName = $("#shop-name").val();
    shop.shopAddr = $("#shop-addr").val();
    shop.phone = $("#shop-phone").val();
    shop.shopDesc = $("#shop-desc").val();
    shop.shopCategory = {
        shopCategoryId: $("#shop-category").find("option").not(function () {
            //双重否定等于肯定
            return !this.selected;
        }).data("id")
    };
    shop.area = {
        areaId: $("#area").find("option").not(function () {
            return !this.selected;
        }).data("id")
    };
    var shopImg = $("#shop-img")[0].files[0];
    //声明formData，放这些数据，然后打包发送
    var fromData = new FormData();
    fromData.append("shopImg", shopImg);
    fromData.append("shopStr", JSON.stringify(shop));
    var verifyCodeActual = $("#j_captcha").val();
    //如果是空，提示用户需要输入验证码
    if (!verifyCodeActual) {
        $.toast("请输入验证码!");
        return;
    }
    fromData.append("verifyCodeActual", verifyCodeActual);
    $.ajax({
        url: (isEdit ? editShopUrl : registerShopUrl),
        type: "post",
        data: fromData,
        contentType: false,//即传文件，也传图片，所以为false
        processData: false,
        cache: false,
        success: function (data) {
            if (data.success) {
                //弹出一个信息
                $.toast("提交成功");
                if (!isEdit) {
                    // 若为注册操作，成功后返回店铺列表页
                    window.location.href = "/ShopO2O/shopadmin/shoplist";
                }
            } else {
                $.toast("提交失败" + data.errMsg);
            }
            $("#captcha_img").click();
        }
    });
});