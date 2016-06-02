(function(){
    'use strict';

    angular.module('MDC')
        .service('commonService', [CommonService]);

    /**
     * common Data Service
     */
    function CommonService() {
        var lastWeek = new Date();
        var offset = (24*60*60*1000) * 7; //7 days
        lastWeek.setTime(lastWeek.getTime() - offset);
        return {
            from: lastWeek,
            till: new Date()
        };
    }
})();