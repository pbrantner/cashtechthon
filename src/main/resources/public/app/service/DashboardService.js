(function(){
    'use strict';

    angular.module('MDC');

    angular.module('MDC')
        .service('dashboardService', ['$http', DashboardService]);

    /**
     * Dashboard Data Service
     *
     * @returns {{loadStatistics: Function}}
     */
    function DashboardService($http) {
        return {
            loadStats : function(){
                return $http.get("/stats");
            },
            loadStatistics : function(from, till) {
                return $http.get('/classifications/summary?from=' + from + '&till=' + till);
            },
            loadStatisticsCsv: function(from, till) {
                return $http.get('/classifications/summary?from=' + from + '&till=' + till);
            }
        };
    }

})();
