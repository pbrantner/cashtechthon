(function(){
    'use strict';

    angular.module('MDC');

    angular.module('MDC')
        .service('ReportHistoryService', [ReportHistoryService]);

    function ReportHistoryService() {

        var history = [];

        return {
            addToHistory : function(d){

                history.push(angular.extend({
                    requestDate: new Date()
                }, d));
            },
            getHistory : function () {
                return history;
            }
        };
    }

})();
