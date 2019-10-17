//窗口激活状态
var isWindowActive = true;
var timerArr;

(function ($) {
    $.extend({
        /**
         * 调用方法： var timerArr = $.blinkTitle.show();
         *     $.blinkTitle.clear(timerArr);
         */
        blinkTitle: {
            show: function () { //有新消息时在title处闪烁提示
                var step = 0, _title = document.title;
                var timer = setInterval(function () {
                    step++;
                    if (step === 3) {
                        step = 1
                    }
                    if (step === 1) {
                        document.title = '【　　　】' + _title
                    }
                    if (step === 2) {
                        document.title = '【新消息】' + _title
                    }
                }, 500);
                return [timer, _title];
            },
            /**
             * @param timerArr[0], timer标记
             * @param timerArr[1], 初始的title文本内容
             */
            clear: function (timerArr) {
                //去除闪烁提示，恢复初始title文本
                if (timerArr) {
                    clearInterval(timerArr[0]);
                    document.title = timerArr[1];
                }
            }
        }
    });
})(jQuery);

//窗口title闪烁
function titleTwinkle() {
    timerArr = $.blinkTitle.show();
}

$(document).ready(function () {
    // 不同浏览器 hidden 名称
    var hiddenProperty = 'hidden' in document ? 'hidden' :
        'webkitHidden' in document ? 'webkitHidden' :
            'mozHidden' in document ? 'mozHidden' :
                null;
    // 不同浏览器的事件名
    var visibilityChangeEvent = hiddenProperty.replace(/hidden/i, 'visibilitychange');
    var onVisibilityChange = function(){
        if (document[hiddenProperty]) {
            isWindowActive = false;
        } else {
            isWindowActive = true;
            //窗口激活时自动关闭title闪烁
            $.blinkTitle.clear(timerArr);
        }
    };
    document.addEventListener(visibilityChangeEvent, onVisibilityChange);
});